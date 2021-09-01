//package mk.ukim.finki.code;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//class Student{
//    String index;
//    List<Integer> points;
//
//    public Student(String index, List<Integer> points) {
//        this.index = index;
//        this.points = points;
//    }
//    double averagePoint(){
//        int sumPoints = points.stream()
//                .mapToInt(i-> i)
//                .sum();
//        return sumPoints/10.0;
//    }
//    boolean hasFailed(){
//        return points.size()<8;
//    }
//    int getYear(){
//        int god = Integer.parseInt(index.substring(1,2));
//        return 10-god;
//    }
//    //135042 NO 0.00
//
//    @Override
//    public String toString() {
//        return String.format("%s %s %.2f",index,
//                hasFailed() ? "NO" : "YES",averagePoint());
//    }
//
//    public String getIndex() {
//        return index;
//    }
//}
//class LabExercises{
//    List<Student> students;
//
//    public LabExercises(){
//        this.students = new ArrayList<>();
//    }
//    public void addStudent(Student student){
//        this.students.add(student);
//    }
//    public void printByAveragePoints(boolean asceding,int n){
//        Comparator<Student> comparator = Comparator.comparing(Student::averagePoint)
//                .thenComparing(Student::getIndex);
//        if (asceding){
//            students.stream()
//                    .sorted(comparator)
//                    .limit(n)
//                    .forEach(System.out::println);
//        }else{
//            students.stream()
//                    .sorted(comparator.reversed())
//                    .limit(n)
//                    .forEach(System.out::println);
//        }
//    }
//    public List<Student> failedStudents (){
//        Comparator<Student> comparator = (student1,student2)->{
//            int x = student1.index.compareTo(student2.index);
//            if (x==0)
//                return Double.compare(student1.averagePoint(), student2.averagePoint());
//            return x;
//        };
//        return students.stream()
//                .filter(s->s.hasFailed())
//                .sorted(comparator)
//                .collect(Collectors.toList());
//    }
//    public Map<Integer,Double> getStatisticsByYear(){
//        return students.stream()
//                .filter(student -> !student.hasFailed())
//                .collect(Collectors.groupingBy(
//                        Student::getYear,
//                        TreeMap::new,
//                        Collectors.averagingDouble(Student::averagePoint)
//                ));
//    }
//
//
//}
//
//public class LabExercisesTest {
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        LabExercises labExercises = new LabExercises();
//        while (sc.hasNextLine()) {
//            String input = sc.nextLine();
//            String[] parts = input.split("\\s+");
//            String index = parts[0];
//            List<Integer> points = Arrays.stream(parts).skip(1)
//                    .mapToInt(Integer::parseInt)
//                    .boxed()
//                    .collect(Collectors.toList());
//
//            labExercises.addStudent(new Student(index, points));
//        }
//
//        System.out.println("===printByAveragePoints (ascending)===");
//        labExercises.printByAveragePoints(true, 100);
//        System.out.println("===printByAveragePoints (descending)===");
//        labExercises.printByAveragePoints(false, 100);
//        System.out.println("===failed students===");
//        labExercises.failedStudents().forEach(System.out::println);
//        System.out.println("===statistics by year");
//        labExercises.getStatisticsByYear().entrySet().stream()
//                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
//                .forEach(System.out::println);
//
//    }
//}