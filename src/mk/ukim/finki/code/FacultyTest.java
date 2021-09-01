//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//class OperationNotAllowedException extends Exception{
//    public OperationNotAllowedException (String message) {
//        super (message);
//    }
//}
//abstract class Student{
//    String id;
//    //Map<Term,List<Grades>>
//    Map<Integer,List<Integer>> gradesByTerm;
//    Set<String > courses;
//
//    public Student(String studentId){
//        this.id = studentId;
//        this.gradesByTerm = new TreeMap<> ();
//        this.courses = new TreeSet<> ();
//    }
//    void validate(int term) throws OperationNotAllowedException {
//        if (!gradesByTerm.containsKey (term))
//            throw new OperationNotAllowedException(String.format ("Term %d is not possible for student with ID %s",term,id));
//        if (gradesByTerm.get (term).size ()==3)
//            throw new OperationNotAllowedException(String.format ("Student %s already has 3 grades in term %d",id,term));
//    }
//    double averageGrade(){
//
//        return gradesByTerm.values ()
//                .stream ()
//                .flatMap (Collection::stream)
//                .mapToInt (i->i)
//                .average ().orElse (5.0);
//    }
//    public String getGraduationLog(){
//        return String.format ("Student with ID %s graduated with average grade %.2f",id,averageGrade ());
//    }
//    double averageGradeForTerm(int term){
//        return gradesByTerm.get (term)
//                .stream ()
//                .mapToInt (i->i)
//                .average ().orElse (5.0);
//    }
//    abstract boolean addGrade(int term,String courseName,int grade) throws OperationNotAllowedException;
//
//    public String getId () {
//        return id;
//    }
//    public String shortReport(){
//    //Student: [id] Courses passed: [coursesPassed] Average grade: [averageGrade].
//    return String.format ("Student: %s Courses passed: %d Average grade: %.2f",id,countOfCoursesPassed (),averageGrade ());
//    }
//
//    public String getTermReport(int term){
////        Term 1:
////        Courses for term: [count]
////        Average grade for term: [average]
//        return String.format ("Term %d\nCourses for term: %d\nAverage grade for term: %.2f",
//                term,
//                gradesByTerm.get (term).size (),
//                averageGradeForTerm (term));
//    }
//
//    public String getDetailedReport(){
//        StringBuilder sb = new StringBuilder ();
//        sb.append (String.format ("Student: %s\n",id));
//        gradesByTerm.keySet ()
//                .forEach (term->sb.append (getTermReport(term)).append ("\n"));
//        sb.append (String.format ("Average grade: %.2f\nCourses attended: %s",
//                averageGrade (),String.join (",",courses)));
//        return sb.toString ();
//    }
//    int countOfCoursesPassed(){
//        return gradesByTerm.values ()
//                .stream ()
//                .mapToInt (List::size)
//                .sum ();
//    }
//}
//class StudentOnThreeYearsStudies extends Student{
//
//    public StudentOnThreeYearsStudies (String studentId) {
//        super (studentId);
//        IntStream.range (1,7).forEach (i->gradesByTerm.putIfAbsent (i,new ArrayList<> ()));
//    }
//
//    @Override
//    boolean addGrade (int term, String courseName, int grade) throws OperationNotAllowedException {
//        validate (term);
//        gradesByTerm.get (term).add (grade);
//        courses.add (courseName);
//        return countOfCoursesPassed() == 18;
//    }
//
//    @Override
//    public String getGraduationLog () {
//        return super.getGraduationLog () + " in 3 years.";
//    }
//}
//class StudentOnFourYearsStudies extends Student{
//
//    public StudentOnFourYearsStudies (String studentId) {
//        super (studentId);
//        IntStream.range (1,9).forEach (i->gradesByTerm.putIfAbsent (i,new ArrayList<> ()));
//    }
//
//    @Override
//    boolean addGrade (int term, String courseName, int grade) throws OperationNotAllowedException {
//        validate (term);
//        gradesByTerm.get (term).add (grade);
//        courses.add (courseName);
//        return countOfCoursesPassed () == 24;
//    }
//
//    @Override
//    public String getGraduationLog () {
//        return super.getGraduationLog () + " in 4 years.";
//    }
//}
//class Course{
//    String courseName;
//    IntSummaryStatistics iss;
//
//    public Course(String courseName){
//        this.courseName = courseName;
//        this.iss = new IntSummaryStatistics ();
//    }
//    public void acceptGrade(int grade){
//        iss.accept (grade);
//    }
//
//    @Override
//    public String toString () {
//        return String.format ("%s %d %.2f",courseName,iss.getCount (),iss.getAverage ());
//    }
//
////    int getStudentCount(){
//         (int) iss.getCount ();
//    }
//    double averageGrade(){
//        return iss.getAverage ();
//    }
//    String getCourseName(){
//        return courseName;
//    }
//}
//
//class Faculty {
//    //Map<ID,Stude3nt>
//    //Map<courseName,Course>
//    Map<String ,Student> studentsByID;
//    Map<String ,Course> coursesByName;
//    StringBuilder logs;
//
//    public Faculty() {
//        this.studentsByID = new HashMap<> ();
//        this.coursesByName = new HashMap<> ();
//        this.logs = new StringBuilder ();
//    }
//
//    void addStudent(String id, int yearsOfStudies) {
//       studentsByID.put (id,yearsOfStudies == 3 ? new StudentOnThreeYearsStudies (id) : new StudentOnFourYearsStudies (id));
//    }
//
//    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
//        Student student = studentsByID.get (studentId);
//        boolean graduated = student.addGrade (term,courseName,grade);
//        coursesByName.putIfAbsent (courseName,new Course (courseName));
//        coursesByName.get (courseName).acceptGrade (grade);
//        if (graduated){
//            logs.append (student.getGraduationLog ()).append ("\n");
//            studentsByID.remove (studentId);
//        }
//    }
//
//    String getFacultyLogs() {
//        return logs.deleteCharAt (logs.length ()-1).toString ();
//    }
//
//    String getDetailedReportForStudent(String id) {
//        return studentsByID.get (id).getDetailedReport ();
//    }
//
//    void printFirstNStudents(int n) {
//        Comparator<Student> comparator = Comparator.comparing (Student::countOfCoursesPassed)
//                .thenComparing (Student::averageGrade)
//                .thenComparing (Student::getId).reversed ();
//
//        TreeSet<Student > students = new TreeSet<> (comparator);
//        students.addAll (studentsByID.values ());
//        students.stream ()
//                .limit (n)
//                .forEach (student -> System.out.println (student.shortReport ()));
//    }
//
//    void printCourses() {
//        Comparator<Course> comparator = Comparator.comparing (Course::getStudentCount)
//                .thenComparing (Course::averageGrade)
//                .thenComparing (Course::getCourseName);
//        TreeSet<Course> courses = new TreeSet<> (comparator);
//        courses.addAll (coursesByName.values ());
//        courses.stream ()
//                .forEach (System.out::println);
//    }
//}
//
//public class FacultyTest {
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int testCase = sc.nextInt();
//
//        if (testCase == 1) {
//            System.out.println("TESTING addStudent AND printFirstNStudents");
//            Faculty faculty = new Faculty();
//            for (int i = 0; i < 10; i++) {
//                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
//            }
//            faculty.printFirstNStudents(10);
//
//        } else if (testCase == 2) {
//            System.out.println("TESTING addGrade and exception");
//            Faculty faculty = new Faculty();
//            faculty.addStudent("123", 3);
//            faculty.addStudent("1234", 4);
//            try {
//                faculty.addGradeToStudent("123", 7, "NP", 10);
//            } catch (OperationNotAllowedException e) {
//                System.out.println(e.getMessage());
//            }
//            try {
//                faculty.addGradeToStudent("1234", 9, "NP", 8);
//            } catch (OperationNotAllowedException e) {
//                System.out.println(e.getMessage());
//            }
//        } else if (testCase == 3) {
//            System.out.println("TESTING addGrade and exception");
//            Faculty faculty = new Faculty();
//            faculty.addStudent("123", 3);
//            faculty.addStudent("1234", 4);
//            for (int i = 0; i < 4; i++) {
//                try {
//                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
//                } catch (OperationNotAllowedException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//            for (int i = 0; i < 4; i++) {
//                try {
//                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
//                } catch (OperationNotAllowedException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        } else if (testCase == 4) {
//            System.out.println("Testing addGrade for graduation");
//            Faculty faculty = new Faculty();
//            faculty.addStudent("123", 3);
//            faculty.addStudent("1234", 4);
//            int counter = 1;
//            for (int i = 1; i <= 6; i++) {
//                for (int j = 1; j <= 3; j++) {
//                    try {
//                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
//                    } catch (OperationNotAllowedException e) {
//                        System.out.println(e.getMessage());
//                    }
//                    ++counter;
//                }
//            }
//            counter = 1;
//            for (int i = 1; i <= 8; i++) {
//                for (int j = 1; j <= 3; j++) {
//                    try {
//                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
//                    } catch (OperationNotAllowedException e) {
//                        System.out.println(e.getMessage());
//                    }
//                    ++counter;
//                }
//            }
//            System.out.println("LOGS");
//            System.out.println(faculty.getFacultyLogs());
//            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
//            faculty.printFirstNStudents(2);
//        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
//            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
//            Faculty faculty = new Faculty();
//            for (int i = 1; i <= 10; i++) {
//                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
//                int courseCounter = 1;
//                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
//                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
//                        try {
//                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
//                        } catch (OperationNotAllowedException e) {
//                            System.out.println(e.getMessage());
//                        }
//                        ++courseCounter;
//                    }
//                }
//            }
//            if (testCase == 5)
//                faculty.printFirstNStudents(10);
//            else if (testCase == 6)
//                faculty.printFirstNStudents(3);
//            else
//                faculty.printFirstNStudents(20);
//        } else if (testCase == 8 || testCase == 9) {
//            System.out.println("TESTING DETAILED REPORT");
//            Faculty faculty = new Faculty();
//            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
//            int grade = 6;
//            int counterCounter = 1;
//            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
//                for (int j = 1; j < 3; j++) {
//                    try {
//                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
//                    } catch (OperationNotAllowedException e) {
//                        e.printStackTrace();
//                    }
//                    grade++;
//                    if (grade == 10)
//                        grade = 5;
//                    ++counterCounter;
//                }
//            }
//            System.out.println(faculty.getDetailedReportForStudent("student1"));
//        } else if (testCase==10) {
//            System.out.println("TESTING PRINT COURSES");
//            Faculty faculty = new Faculty();
//            for (int i = 1; i <= 10; i++) {
//                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
//                int courseCounter = 1;
//                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
//                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
//                        int grade = sc.nextInt();
//                        try {
//                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
//                        } catch (OperationNotAllowedException e) {
//                            System.out.println(e.getMessage());
//                        }
//                        ++courseCounter;
//                    }
//                }
//            }
//            faculty.printCourses();
//        } else if (testCase==11) {
//            System.out.println("INTEGRATION TEST");
//            Faculty faculty = new Faculty();
//            for (int i = 1; i <= 10; i++) {
//                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
//                int courseCounter = 1;
//                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
//                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
//                        int grade = sc.nextInt();
//                        try {
//                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
//                        } catch (OperationNotAllowedException e) {
//                            System.out.println(e.getMessage());
//                        }
//                        ++courseCounter;
//                    }
//                }
//
//            }
//
//            for (int i=11;i<15;i++) {
//                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
//                int courseCounter = 1;
//                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
//                    for (int k = 1; k <= 3; k++) {
//                        int grade = sc.nextInt();
//                        try {
//                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
//                        } catch (OperationNotAllowedException e) {
//                            System.out.println(e.getMessage());
//                        }
//                        ++courseCounter;
//                    }
//                }
//            }
//            System.out.println("LOGS");
//            System.out.println(faculty.getFacultyLogs());
//            System.out.println("DETAILED REPORT FOR STUDENT");
//            System.out.println(faculty.getDetailedReportForStudent("student2"));
//            try {
//                System.out.println(faculty.getDetailedReportForStudent("student11"));
//                System.out.println("The graduated students should be deleted!!!");
//            } catch (NullPointerException e) {
//                System.out.println("The graduated students are really deleted");
//            }
//            System.out.println("FIRST N STUDENTS");
//            faculty.printFirstNStudents(10);
//            System.out.println("COURSES");
//            faculty.printCourses();
//        }
//    }
//}
