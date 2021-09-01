package mk.ukim.finki.av6_kol1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


class FileNameExistsException extends Exception{
    String name;
    String nameFolder;

    public FileNameExistsException(String name,String nameFolder) {
        this.name = name;
        this.nameFolder = nameFolder;

    }

    @Override
    public String getMessage() {
        return String.format("There is already a file named %s in the folder %s",name,nameFolder);
    }
}
interface IFile extends Comparable<IFile>{
    String getFileName();
    long getFileSize();
    String getFileInfo(String spaces);
    void sortBySize();
    long findLargestFile();

    @Override
    default int compareTo(IFile o) {
        return Long.compare(this.getFileSize(),o.getFileSize());
    }
}
class File implements IFile{
    String name;
    long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(String spaces) {
        return spaces + String.format("File name: %10s File size: %10d\n",name,size);
    }

    @Override
    public void sortBySize() {
    }

    @Override
    public long findLargestFile() {
        return size;
    }
}
class Folder implements IFile{

    String nameFolder;
    List<IFile> iFiles;

    public Folder(String name) {
        this.nameFolder = name;
        iFiles = new ArrayList<>();
    }
    void addFile(IFile file) throws FileNameExistsException {
        boolean flag = iFiles.stream()
                .anyMatch(f->f.getFileName().equals(file.getFileName()));
        if (flag)
            throw new FileNameExistsException(file.getFileName(),nameFolder);
        iFiles.add(file);
    }

    @Override
    public String getFileName() {
        return nameFolder;
    }

    @Override
    public long getFileSize() {
//        long sum = 0;
//        for (IFile file: iFiles)
//            sum+=file.getFileSize();
//        return sum;
        return iFiles.stream()
                .mapToLong(f->f.getFileSize())
                .sum();
    }

    @Override
    public String getFileInfo(String spaces) {
        String result = spaces + String.format("Folder name: %10s Folder size: %10s\n",nameFolder,getFileSize());
        for (IFile file : iFiles)
            result += file.getFileInfo(spaces + "\t");
        return result;
    }

    @Override
    public void sortBySize() {
        Collections.sort(iFiles);
        for (IFile file : iFiles)
            file.sortBySize();
    }

    @Override
    public long findLargestFile() {
        return iFiles.stream()
                .mapToLong(i->i.findLargestFile())
                .max().orElse(0);
    }
}
class FileSystem{
    Folder rootDirectory;

    public FileSystem(){
        rootDirectory = new Folder("root");
    }
    public void addFile(IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }
    public long findLargestFile(){
        return rootDirectory.findLargestFile();
    }
    public void sortBySize(){
        rootDirectory.sortBySize();
    }

    @Override
    public String toString() {
        return rootDirectory.getFileInfo("");
    }
}

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());


    }
}