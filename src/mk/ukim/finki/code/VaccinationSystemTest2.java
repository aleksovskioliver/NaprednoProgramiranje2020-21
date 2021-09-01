package mk.ukim.finki.code;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

enum TYPE{
    PRIORITY,
    BASIC
}
abstract class Application implements Comparable<Application>{
    String ID;
    String name;
    String surname;
    String profession;
    Map<Integer,String > vaccines;
    int priority;

    public Application(String ID,String name,String surname,String profession,String vaccine1,String vaccine2,String vaccine3,int priority){
        this.ID = ID;
        this.name = name;
        this.surname = surname;
        this.profession = profession;
        this.vaccines = new TreeMap<> ();
        this.vaccines.putIfAbsent (1,vaccine1);
        this.vaccines.putIfAbsent (2,vaccine2);
        this.vaccines.putIfAbsent (3,vaccine3);
        this.priority = priority;
    }
    static int calculateAge(String ID){
        int day = Integer.parseInt (ID.substring (0,2));
        int month = Integer.parseInt (ID.substring (2,4));
        String year = ID.substring (4,7);
        String fullYear;
        if (year.charAt (0) == '0'){
            fullYear = "2" + year;
        }else{
            fullYear = "1" + year;
        }

        if (month<8)
            return 2021-Integer.parseInt (fullYear);
        else if (month==8 && day<=29)
            return 2021 - Integer.parseInt (fullYear);
        else
            return 2021-Integer.parseInt (fullYear) - 1;
    }
    int getAge(){
        return calculateAge (this.ID);
    }

    @Override
    public String toString () {
        //ID name surname age applicationType vaccine1 vaccine2 vaccine3
        return String.format ("%s %s %s %d %s %s",
                ID,name,surname,getAge (),applicationType ().toString (),
                vaccines.values ().stream ()
                        .map (value->String.format ("%s",value))
                        .collect(Collectors.joining(" ")));

//        vaccines.entrySet ().stream ()
//                .map (entry->String.format ("%s",entry.getValue ()))
//                .collect(Collectors.joining(" ")));
    }
    abstract TYPE applicationType();

    public String getName () {
        return name;
    }

    public int getPriority () {
        return priority;
    }

    public String getID () {
        return ID;
    }

    public String getSurname () {
        return surname;
    }

    public String getProfession () {
        return profession;
    }

    @Override
    public int compareTo (Application o) {
        return Comparator.comparing (Application::getPriority).reversed ()
                .thenComparing (Application::getAge).reversed ()
                .thenComparing (Application::getName).compare (this,o);
    }

    public Map<Integer, String> getVaccines () {
        return vaccines;
    }
}
class PriorityApplication extends Application{

    public PriorityApplication (String ID, String name, String surname, String profession, String vaccine1, String vaccine2, String vaccine3) {
        super (ID, name, surname, profession, vaccine1, vaccine2, vaccine3, 1);
    }

    @Override
    TYPE applicationType () {
        return TYPE.PRIORITY;
    }
}
class BasicApplication extends Application{

    public BasicApplication (String ID, String name, String surname, String profession, String vaccine1, String vaccine2, String vaccine3) {
        super (ID, name, surname, profession, vaccine1, vaccine2, vaccine3, 2);
    }

    @Override
    TYPE applicationType () {
        return TYPE.BASIC;

    }
}
class InvalidApplicationException extends Exception{
    public InvalidApplicationException (String part1,String part2,String part3) {
        super (String.format ("The application for %s %s with ID: %s is not allowed, because the citizen is under 18 years old.",part1,part2,part3));
    }
}
class ApplicationCreator{
    public static Application createApplication(String line) throws InvalidApplicationException {
        String[] parts = line.split ("\\s+");
        int years = Application.calculateAge (parts[0]);
        if (years<18)
            throw new InvalidApplicationException(parts[1],parts[2],parts[3]);


        if (parts[3].equals ("DOCTOR") || parts[3].equals ("NURSE") ||
                parts[3].equals ("POLICEMAN") || parts[3].equals ("FIREMAN")){
            return new PriorityApplication (parts[0],parts[1],parts[2],parts[3],parts[4],parts[5],parts[6]);
        }else{
            return new BasicApplication (parts[0],parts[1],parts[2],parts[3],parts[4],parts[5],parts[6]);
        }
    }

}
class VaccinationAcumulator{
    int pfizer;
    int astraZeneca;
    int sinoFarm;

    public VaccinationAcumulator (int pfizer, int astraZeneca, int sinoFarm) {
        this.pfizer = pfizer;
        this.astraZeneca = astraZeneca;
        this.sinoFarm = sinoFarm;
    }
    public void combine(VaccinationAcumulator vaccinationAcumulator){
        this.pfizer += vaccinationAcumulator.pfizer;
        this.astraZeneca += vaccinationAcumulator.astraZeneca;
        this.sinoFarm += vaccinationAcumulator.sinoFarm;
    }
    boolean takePH(){
        if (pfizer>=2){
            pfizer-=2;
            return true;
        }else return false;
    }
    boolean takeAZ(){
        if (astraZeneca>=2){
            astraZeneca-=2;
            return true;
        }else return false;
    }
    boolean takeSF(){
        if (sinoFarm>=2){
            sinoFarm-=2;
            return true;
        }else return false;
    }
    boolean takeDose(String vaccineName){
        switch (vaccineName){
            case "PH":
                return takePH ();
            case "AZ":
                return takeAZ ();
            case "SF":
                return takeSF ();
            default:
                return false;
        }
    }
    public String getDose(Application application){
        String first = application.getVaccines ().get (1);
        String second = application.getVaccines ().get (2);
        String third = application.getVaccines ().get (3);

        if (!takeDose (first)){
            if (!takeDose (second)){
                if (takeDose (third)){
                    return third;
                }else
                {
                    return null;
                }
            }else{
                return second;
            }
        }else{
            return first;
        }
    }
}
class VaccinationSystem{
    List<Application> applications;
    //Map<EMBG,ImeVakcina>
    Map<String,String > vaccined;
    //Map<EMBG,Aplikacija>
    Map<String ,Application> applicationsByEMBG;
    VaccinationAcumulator vaccinationAcumulator;
    public VaccinationSystem(){
        this.applications = new ArrayList<> ();
        this.vaccined = new TreeMap<> ();
        this.applicationsByEMBG = new TreeMap<> ();
        this.vaccinationAcumulator = new VaccinationAcumulator (0,0,0);
    }
    void readApplications(InputStream inputStream){
        BufferedReader br = new BufferedReader (new InputStreamReader (inputStream));
        applications = br.lines ()
                .map (line-> {
                    try {
                        return ApplicationCreator.createApplication (line);
                    } catch (InvalidApplicationException e) {
                        System.out.println (e.getMessage ());
                        return null;
                    }
                })
                .filter (Objects::nonNull)
                .collect(Collectors.toList());
        this.applications.forEach (i->applicationsByEMBG.putIfAbsent (i.getID(),i));
    }
    void vaccinate (int numPH, int numAZ, int numSF){
        Collections.sort (applications);

        vaccinationAcumulator.combine (new VaccinationAcumulator (numPH,numAZ,numSF));
        for (Application application : applications){
            if (!vaccined.containsKey (application.getID ())){
                String appliedVaccine = vaccinationAcumulator.getDose (application);
                if (appliedVaccine==null)
                    break;
                vaccined.putIfAbsent (application.getID (),appliedVaccine);
            }
        }
    }
   public Map<String ,String> vaccinatedCitizens(){
        return this.vaccined;
    }
    Map<String, List<Application>> vaccinatedCitizensByVaccineType(){
        Map<String ,List<Application>> result = new HashMap<> ();

        for (Map.Entry<String ,String > entry : this.vaccined.entrySet ()){
            result.putIfAbsent (entry.getValue (),new ArrayList<> ());
            result.get (entry.getValue ()).add (this.applicationsByEMBG.get (entry.getKey ()));
        }
        return result;
    }
    Collection<Application> nextToVaccinate (int n){
        return this.applications.stream ()
                .filter (i->i.applicationType ().name ().equals ("BASIC"))
                .filter (i->!this.vaccined.containsKey (i.getID ()))
                .limit (n)
                .collect (Collectors.toList ());
    }
    Map<Integer,Integer> appliedVaccinesPerPriority(){
        Map<Integer,Integer> result = new HashMap<> ();
        result.putIfAbsent (1,0);
        result.putIfAbsent (2,0);
        result.putIfAbsent (3,0);

        for (int i=1;i<=3;i++){
            int finalI = i;
            int count = (int) this.vaccined.entrySet ().stream ()
                    .filter (k->k.getValue ().equals (this.applicationsByEMBG.get (k.getKey ()).getVaccines ().get (finalI)))
                    .count ();
            result.put(i,count);
        }
        return result;
    }
    void printApplications(OutputStream outputStream){
        PrintWriter pw = new PrintWriter (outputStream);
        applications.stream ()
                .sorted()
                .forEach (pw::println);
        pw.flush ();
        pw.close ();
    }
}public class VaccinationSystemTest2 {

    public static Map<Integer, List<Integer>> populateCombinations() {
        Map<Integer, List<Integer>> map = new HashMap<>();

        map.put(1, new ArrayList<>(Arrays.asList(2, 0, 2)));
        map.put(2, new ArrayList<>(Arrays.asList(2, 2, 2)));
        map.put(3, new ArrayList<>(Arrays.asList(2, 4, 2)));
        map.put(4, new ArrayList<>(Arrays.asList(2, 4, 4)));
        map.put(5, new ArrayList<>(Arrays.asList(4, 4, 4)));

        return map;
    }

    public static void main(String[] args) {
        VaccinationSystem vaccinationSystem = new VaccinationSystem();

        System.out.println("------------ READING FROM INPUT STREAM ------------");
        vaccinationSystem.readApplications(System.in);
        System.out.println("------------ READING SUCCESSFUL ------------");
        System.out.println();

        Map<Integer, List<Integer>> combinations = populateCombinations();

        for (int i = 1; i <= combinations.size(); i++) {
            List<Integer> combination = combinations.get(i);

            System.out.println("------------ CASE " + i + "------------");
            System.out.println();
            vaccinationSystem.vaccinate(combination.get(0), combination.get(1), combination.get(2));

            System.out.println("------------------------ TEST VACCINATED CITIZENS ------------");
            Map<String, String> map1 = vaccinationSystem.vaccinatedCitizens();
            map1.forEach((key, value) -> System.out.println(key + " " + value));
            System.out.println();

            System.out.println("------------------------ TEST APPLIED VACCINES PER PRIORITY ------------");
            Map<Integer, Integer> map2 = vaccinationSystem.appliedVaccinesPerPriority();
            map2.forEach((key, value) -> System.out.println(key + " " + value));
            System.out.println();

            System.out.println("------------------------ TEST VACCINATED CITIZENS BY VACCINE TYPE ------------");
            Map<String, List<Application>> map3 = vaccinationSystem.vaccinatedCitizensByVaccineType();
            map3.forEach((key, value) -> System.out.println(key + " " + value));
            System.out.println();

            System.out.println("------------------------ TEST NEXT TO VACCINATE 3 ------------");
            Collection<Application> collection1 = vaccinationSystem.nextToVaccinate(3);
            collection1.forEach(System.out::println);
            System.out.println();

            System.out.println("------------------------ TEST NEXT TO VACCINATE 1 ------------");
            Collection<Application> collection2 = vaccinationSystem.nextToVaccinate(1);
            collection2.forEach(System.out::println);
            System.out.println();
        }
    }
}
