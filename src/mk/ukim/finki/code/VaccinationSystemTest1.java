//package mk.ukim.finki.code;
//
//import java.io.*;
//import java.util.*;
//import java.util.stream.Collectors;
//
//enum TYPE{
//    PRIORITY,
//    BASIC
//}
//abstract class Application implements Comparable<Application>{
//    String ID;
//    String name;
//    String surname;
//    String profession;
//    Map<Integer,String > vaccines;
//    int priority;
//
//    public Application(String ID,String name,String surname,String profession,String vaccine1,String vaccine2,String vaccine3,int priority){
//        this.ID = ID;
//        this.name = name;
//        this.surname = surname;
//        this.profession = profession;
//        this.vaccines = new TreeMap<> ();
//        this.vaccines.putIfAbsent (1,vaccine1);
//        this.vaccines.putIfAbsent (2,vaccine2);
//        this.vaccines.putIfAbsent (3,vaccine3);
//        this.priority = priority;
//    }
//    static int calculateAge(String ID){
//        int day = Integer.parseInt (ID.substring (0,2));
//        int month = Integer.parseInt (ID.substring (2,4));
//        String year = ID.substring (4,7);
//        String fullYear;
//        if (year.charAt (0) == '0'){
//            fullYear = "2" + year;
//        }else{
//            fullYear = "1" + year;
//        }
//
//        if (month<8)
//            return 2021-Integer.parseInt (fullYear);
//        else if (month==8 && day<=29)
//            return 2021 - Integer.parseInt (fullYear);
//        else
//            return 2021-Integer.parseInt (fullYear) - 1;
//    }
//    int getAge(){
//        return calculateAge (this.ID);
//    }
//
//    @Override
//    public String toString () {
//        //ID name surname age applicationType vaccine1 vaccine2 vaccine3
//        return String.format ("%s %s %s %d %s %s",
//                ID,name,surname,getAge (),applicationType ().toString (),
//                vaccines.values ().stream ()
//                     .map (value->String.format ("%s",value))
//                        .collect(Collectors.joining(" ")));
//
////        vaccines.entrySet ().stream ()
////                .map (entry->String.format ("%s",entry.getValue ()))
////                .collect(Collectors.joining(" ")));
//    }
//    abstract TYPE applicationType();
//
//    public String getName () {
//        return name;
//    }
//
//    public int getPriority () {
//        return priority;
//    }
//
//    @Override
//    public int compareTo (Application o) {
//        return Comparator.comparing (Application::getPriority).reversed ()
//                .thenComparing (Application::getAge).reversed ()
//                .thenComparing (Application::getName).compare (this,o);
//    }
//}
//class PriorityApplication extends Application{
//
//    public PriorityApplication (String ID, String name, String surname, String profession, String vaccine1, String vaccine2, String vaccine3) {
//        super (ID, name, surname, profession, vaccine1, vaccine2, vaccine3, 1);
//    }
//
//    @Override
//    TYPE applicationType () {
//        return TYPE.PRIORITY;
//    }
//}
//class BasicApplication extends Application{
//
//    public BasicApplication (String ID, String name, String surname, String profession, String vaccine1, String vaccine2, String vaccine3) {
//        super (ID, name, surname, profession, vaccine1, vaccine2, vaccine3, 2);
//    }
//
//    @Override
//    TYPE applicationType () {
//        return TYPE.BASIC;
//
//    }
//}
//class InvalidApplicationException extends Exception{
//    public InvalidApplicationException (String part1,String part2,String part3) {
//        super (String.format ("The application for %s %s with ID: %s is not allowed, because the citizen is under 18 years old.",part1,part2,part3));
//    }
//}
//class ApplicationCreator{
//    public static Application createApplication(String line) throws InvalidApplicationException {
//        String[] parts = line.split ("\\s+");
//        int years = Application.calculateAge (parts[0]);
//        if (years<18)
//            throw new InvalidApplicationException(parts[1],parts[2],parts[3]);
//
//
//        if (parts[3].equals ("DOCTOR") || parts[3].equals ("NURSE") ||
//                parts[3].equals ("POLICEMAN") || parts[3].equals ("FIREMAN")){
//            return new PriorityApplication (parts[0],parts[1],parts[2],parts[3],parts[4],parts[5],parts[6]);
//        }else{
//            return new BasicApplication (parts[0],parts[1],parts[2],parts[3],parts[4],parts[5],parts[6]);
//        }
//    }
//}
//class VaccinationSystem{
//    List<Application> applications;
//
//    public VaccinationSystem(){
//        this.applications = new ArrayList<> ();
//    }
//    void readApplications(InputStream inputStream){
//        BufferedReader br = new BufferedReader (new InputStreamReader (inputStream));
//        applications = br.lines ()
//                .map (line-> {
//                    try {
//                        return ApplicationCreator.createApplication (line);
//                    } catch (InvalidApplicationException e) {
//                        System.out.println (e.getMessage ());
//                        return null;
//                    }
//                })
//                .filter (Objects::nonNull)
//                .collect(Collectors.toList());
//    }
//    void printApplications(OutputStream outputStream){
//        PrintWriter pw = new PrintWriter (outputStream);
//        applications.stream ()
//                .sorted()
//                .forEach (pw::println);
//        pw.flush ();
//        pw.close ();
//    }
//}
//public class VaccinationSystemTest1 {
//
//    public static void main(String[] args) {
//        VaccinationSystem vaccinationSystem = new VaccinationSystem();
//
//        System.out.println("--- READING FROM INPUT STREAM ---");
//        vaccinationSystem.readApplications(System.in);
//
//        System.out.println();
//        System.out.println("--- PRINTING TO OUTPUT STREAM ---");
//        vaccinationSystem.printApplications(System.out);
//    }
//}