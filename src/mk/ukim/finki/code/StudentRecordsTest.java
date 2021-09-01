//package mk.ukim.finki.code;
//
//import java.io.*;
//import java.util.*;
//import java.util.stream.Collectors;
//
//
//class Student implements Comparable<Student>{
//    String code;
//    String nasoka;
//    List<Integer> grades;
//
//    public Student(String line) {
//        String[] parts = line.split("\\s+");
//        this.code = parts[0];
//        this.nasoka = parts[1];
//        this.grades = new ArrayList<>();
//        for (int i=2;i<parts.length;i++){
//            grades.add(Integer.parseInt(parts[i]));
//        }
//    }
//    public double averageGrades(){
//        return grades.stream()
//                .mapToDouble(i->i.doubleValue())
//                .average().orElse(5.0);
//    }
//
//    @Override
//    public String toString() {
//        return String.format("%s %.2f",code,averageGrades());
//    }
//
//    @Override
//    public int compareTo(Student o) {
//        int x = Double.compare(this.averageGrades(),o.averageGrades());
//        if (x==0)
//            return this.code.compareTo(o.code);
//        return -x;
//    }
//    public String getNasoka(){
//        return nasoka;
//    }
//
//    public List<Integer> getGrades() {
//        return grades;
//    }
//}
//class StudentRecords{
//    //Map<Nasoka,Set<Student>>
//    //Map<Nasoka,List<Oceni>>
//    Map<String ,TreeSet<Student>> studentsByNasoka;
//    Map<String,List<Integer>> gradesByNasoka;
//    int vkupnoStudenti;
//
//    public StudentRecords(){
//        this.studentsByNasoka = new TreeMap<>();
//        this.gradesByNasoka = new TreeMap<>();
//        this.vkupnoStudenti = 0;
//    }
//    //ioqmx7 MT 10 8 10 8 10 7 6 9 9 9 6 8 6 6 9 9 8
//    int readRecords(InputStream inputStream) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//        String line;
//        while ((line=br.readLine())!=null){
//            Student student = new Student(line);
//            vkupnoStudenti++;
//            String nasoka = student.getNasoka();
//
//            studentsByNasoka.putIfAbsent(nasoka,new TreeSet<>());
//            studentsByNasoka.computeIfPresent(nasoka,(k,v)->{
//                v.add(student);
//                return v;
//            });
//
//            gradesByNasoka.putIfAbsent(nasoka,new ArrayList<>());
//            gradesByNasoka.computeIfPresent(nasoka,(k,v)->{
//                v.addAll(student.getGrades());
//                return v;
//            });
//        }
//        return vkupnoStudenti;
//    }
//    void writeTable(OutputStream outputStream){
//        PrintWriter pw = new PrintWriter(outputStream);
//        String result = studentsByNasoka.entrySet()
//                .stream()
//                .map(entry -> String.format("%s\n%s",entry.getKey(),
//                        entry.getValue().stream()
//                                .map(v->v.toString())
//                                .collect(Collectors.joining("\n"))))
//                .collect(Collectors.joining("\n"));
//        pw.println(result);
//        pw.flush();
//    }
//    String getStars(long number){
//        StringBuilder sb = new StringBuilder();
//        double count = Math.ceil(number/10.0);
//        for (int i=0;i<count;i++){
//            sb.append("*");
//        }
//        return sb.toString();
//    }
//
//    void writeDistribution(OutputStream outputStream){
//        PrintWriter pw = new PrintWriter(outputStream);
//        Comparator<Map.Entry<String,List<Integer>>> comparator = (map1,map2)->{
//            long first = map1.getValue().stream().filter(num->num==10).count();
//            long second = map2.getValue().stream().filter(num->num==10).count();
//            return Long.compare(second,first);
//        };
//        gradesByNasoka.entrySet()
//                .stream().sorted(comparator)
//                .forEach(entry->{
//                    System.out.println(entry.getKey());
//                    List<Integer> grade = entry.getValue();
//                    for (int i=6;i<11;i++){
//                        int finalI = i;
//                        long count = grade.stream().filter(num->num==finalI).count();
//                        System.out.printf("%2d | %s(%d)\n",i,getStars(count),count);
//                    }
//                });
//        pw.flush();
//    }
//}
//
//public class StudentRecordsTest {
//    public static void main(String[] args) {
//        System.out.println("=== READING RECORDS ===");
//        StudentRecords studentRecords = new StudentRecords();
//        int total = 0;
//        try {
//            total = studentRecords.readRecords(System.in);
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//        System.out.printf("Total records: %d\n", total);
//        System.out.println("=== WRITING TABLE ===");
//        studentRecords.writeTable(System.out);
//        System.out.println("=== WRITING DISTRIBUTION ===");
//        studentRecords.writeDistribution(System.out);
//    }
//}
//
//// your code here