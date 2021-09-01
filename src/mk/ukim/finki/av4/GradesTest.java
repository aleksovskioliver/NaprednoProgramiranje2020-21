package mk.ukim.finki.av4;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Student implements Comparable<Student> {
    private String name;
    private String lastName;
    private int exam1;
    private int exam2;
    private  int exam3;

    public Student(String name, String lastName, int exam1, int exam2, int exam3) {
        this.name = name;
        this.lastName = lastName;
        this.exam1 = exam1;
        this.exam2 = exam2;
        this.exam3 = exam3;
    }
    public static Student crateStudent(String line){
        String[] parts = line.split(":");
        return new Student(parts[1],
                parts[0],
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]));
    }
    public double getTotalPoints(){
        return exam1*0.25 + exam2*0.3 + exam3*0.45;
    }
    public char getGrade(){
        double points = getTotalPoints();
        char grade = 'F';
        if(points >= 90)
            grade = 'A';
        else if (points >= 80)
            grade = 'B';
        else if (points >= 70)
            grade = 'C';
        else if (points >= 60)
            grade = 'D';
        else if (points >= 50)
            grade = 'E';

        return grade;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d %d %d %f %c",lastName,
                name,exam1,exam2,exam3,getTotalPoints(),getGrade());
    }

    public String printWithGrade(){
        return String.format("%s %s %c",lastName,name,getGrade());
    }
    @Override
    public int compareTo(Student o) {
        return Character.compare(this.getGrade(),o.getGrade());
    }
}
class Course{
    List<Student> students;

    public Course(){
        students = new ArrayList<>();
    }

    public void readStudents(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        br.lines()
                .map(line -> Student.crateStudent(line))
                .forEach(s -> students.add(s));
    }
    public void printSortedStudents(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        students.stream()
                .sorted(Comparator.naturalOrder())
                .forEach(s -> pw.println(s.printWithGrade()));

        pw.flush();
    }
    public void printDetailedReport(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        students.forEach(student -> pw.println(student.toString()));
        pw.flush();
    }
    public void printGradeDistribution(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        int [] gradeDistribution = new int[6];
        for (Student s : students)
            gradeDistribution[s.getGrade() - 'A']++;

        for (int i=0;i<6;i++)
            pw.printf("%c -> %d\n",i+'A',gradeDistribution[i]);

        pw.flush();
    }
}


public class GradesTest {

    public static void main(String[] args) throws FileNotFoundException {

        Course course = new Course();
        File inputFile = new File("src/mk/ukim/finki/av4/inputFile");
        File outputFile = new File("src/mk/ukim/finki/av4/results");
        course.readStudents(new FileInputStream(inputFile));

        System.out.println("===Printing sorted students to screen===");
        course.printSortedStudents(System.out);

        System.out.println("===Printing detailed students to screen===");
        course.printDetailedReport(new FileOutputStream(outputFile));

        System.out.println("===Printing grade distribution students to screen===");
        course.printGradeDistribution(System.out);
    }
}
