package mk.ukim.finki.code;
import java.nio.file.FileStore;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class File implements Comparable<File>{
    String name;
    Integer size;
    LocalDateTime createdAt;

    public File(String name, Integer size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s",name,size,createdAt.toString());
    }
    public String printWithMonthAndDay(){
        return String.format("%s-%s",createdAt.getMonth(),createdAt.getDayOfMonth());
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public int compareTo(File o) {
        Comparator<File> comparator = Comparator.comparing(File::getCreatedAt)
                .thenComparing(File::getName)
                .thenComparing(File::getSize);
        return comparator.compare(this,o);
    }
    boolean isHiddenAndWithSizeLessThen(int size){
        return this.name.startsWith(".") && this.size<size;
    }
}
class FileSystem{
    //Map<folder,TreeSet<File>>
    Map<Character,TreeSet<File>> filesByFolderName;
    List<File> allFiles;

    public FileSystem(){
        this.filesByFolderName = new HashMap<>();
        allFiles = new ArrayList<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        File file = new File(name,size,createdAt);

        filesByFolderName.putIfAbsent(folder,new TreeSet<>());
        filesByFolderName.computeIfPresent(folder,(k,v)->{
            v.add(file);
            return v;
        });
        allFiles.add(file);
    }
    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return allFiles.stream()
                .filter(file -> file.isHiddenAndWithSizeLessThen(size))
                .sorted()
                .collect(Collectors.toList());
    }
    public int totalSizeOfFilesFromFolders(List<Character> folders){
        return folders.stream()
                .flatMapToInt(folder->filesByFolderName.get(folder)
                        .stream().mapToInt(file->file.size))
                .sum();
    }
    public Map<Integer, Set<File>> byYear(){
        return allFiles.stream()
                .collect(Collectors.groupingBy(
                        f->f.createdAt.getYear(),  //key
                        Collectors.toCollection(TreeSet::new)  //value
                ));
    }
    public Map<String, Long> sizeByMonthAndDay(){
        return allFiles.stream()
                .collect(Collectors.groupingBy(
                        f->f.printWithMonthAndDay(), //key
                        TreeMap::new,           //init empty map
                        Collectors.summingLong(File::getSize)  //fill the value
                ));
    }
}


public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

