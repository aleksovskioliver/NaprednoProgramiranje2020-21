package mk.ukim.finki.code;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

interface ILocation{
    double getLongitude();

    double getLatitude();

    LocalDateTime getTimestamp();
}
class LocationUtils{
    public static double distanceBetween(ILocation iLocation1,ILocation iLocation2){
        return Math.sqrt (Math.pow (iLocation1.getLongitude ()-iLocation2.getLongitude (),2)
        +Math.pow (iLocation1.getLatitude ()-iLocation2.getLatitude (),2));
    }
    public static double timeBetween(ILocation iLocation1,ILocation iLocation2){
        return Math.abs (Duration.between (iLocation1.getTimestamp (), iLocation2.getTimestamp ()).getSeconds ());
    }
    public static boolean isDanger(ILocation iLocation1,ILocation iLocation2){
        return distanceBetween (iLocation1,iLocation2)<=2 && timeBetween (iLocation1,iLocation2)<=300;
    }

}
class User{
    String name;
    String id;
    List<ILocation> iLocations;

    public User(String name,String id){
        this.name = name;
        this.id = id;
        this.iLocations = new ArrayList<> ();
    }
    public void addLocations(List<ILocation> iLocations){
        this.iLocations.addAll (iLocations);
    }
    int countContacts(User u){
        return iLocations.stream ()
                .flatMapToInt (i->u.iLocations.stream ()
                            .mapToInt (j->LocationUtils.isDanger (i,j) ? 1 : 0))
                .sum ();
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        User user = (User) o;
        return Objects.equals (id, user.id);
    }

    @Override
    public int hashCode () {
        return Objects.hash (id);
    }

    public String getName () {
        return name;
    }

    public String getId () {
        return id;
    }
    String complete(){
        return String.format ("%s %s",name,id);
    }
    String hidden(){
        return String.format ("%s %s***",name,id.substring (0,4));
    }
}
class StopCoronaApp{
    //Map<ID,USer>
    //Map<ID,timeSTamp>
    Map<String ,User> usersByID;
    Map<String ,LocalDateTime> infected;

    public StopCoronaApp(){
        this.usersByID = new HashMap<> ();
        this.infected = new HashMap<> ();
    }
    void addUser(String name,String id) throws UserAlreadyExistException {
        if (usersByID.containsKey (id))
            throw new UserAlreadyExistException("TEST");
        usersByID.put (id,new User (name,id));
    }
    void addLocations (String id, List<ILocation> iLocations){
        usersByID.get (id).addLocations (iLocations);
    }
    void detectNewCase (String id, LocalDateTime timestamp){
        infected.put (id,timestamp);
    }
    Map<User, Integer> getDirectContacts (User u){
        Map<User,Integer> result = new TreeMap<> (Comparator.comparing (User::getName)
                                            .thenComparing (User::getId));

        usersByID.values ()
                .stream ()
                .filter (user -> !user.equals (u))
                .filter (user -> user.countContacts (u) != 0)
                .forEach (user -> result.put (user,u.countContacts (user)));
        return result;
    }
    Collection<User> getIndirectContacts(User u){
        Map<User,Integer> directContacts = getDirectContacts (u);
        Set<User> indirectContacts = new TreeSet<> (Comparator.comparing (User::getName)
                                                        .thenComparing (User::getId));

        directContacts.keySet ()
                .stream ()
                .flatMap (user -> getDirectContacts (user).keySet ().stream ())
                .filter (user -> !indirectContacts.contains (user) && !directContacts.containsKey (user) && !user.equals (u))
                .forEach (indirectContacts::add);
        return indirectContacts;
    }
//    [user_name] [user_id] [timestamp_detected]
//    Direct contacts:
//            [contact1_name] [contact1_first_five_letters_of_id] [number_of_detected_contacts1]
//            [contact2_name] [contact2_first_five_letters_of_id] [number_of_detected_contacts1]
//            ...
//    Count of direct contacts: [sum]
//    Indirect contacts:
//            [contact1_name] [contact1_first_five_letters_of_id]
//            [contact2_name] [contact2_first_five_letters_of_id]
//            ...
//    Count of indirect contacts: [count]
    void createReport(){
        List<Integer> countOfDirectContacts = new ArrayList<> ();
        List<Integer> countOfIndirectContacts = new ArrayList<> ();

        infected.entrySet ().stream ()
                .sorted (Map.Entry.comparingByValue ())
                .forEach (entry -> printInfectedUserEntry(entry,
                        countOfDirectContacts,
                        countOfIndirectContacts));

        System.out.printf ("Average direct contacts: %.4f\n",countOfDirectContacts.stream (). mapToDouble (i->i).average ().getAsDouble ());
        System.out.printf ("Average indirect contacts: %.4f\n",countOfIndirectContacts.stream ().mapToDouble (i->i).average ().getAsDouble ());

    }
    private void printInfectedUserEntry(Map.Entry<String ,LocalDateTime> entry,
                                        List<Integer> countOfDirectContacts,
                                        List<Integer> countOfIndirectContacts){
        User user = usersByID.get (entry.getKey ());
        System.out.printf ("%s %s\n",user.complete (),entry.getValue ());
        System.out.println ("Direct contacts:");

        Map<User,Integer> directContacts = getDirectContacts (user);
        directContacts.entrySet ()
                .stream ()
                .sorted (Map.Entry.comparingByValue (Comparator.reverseOrder ()))
                .forEach (e-> System.out.printf ("%s %d\n",e.getKey ().hidden (),e.getValue ()));
        int countOfDirectContact = directContacts.values ().stream ().mapToInt (i->i).sum();
        System.out.printf ("Count of direct contacts: %d\n",countOfDirectContact);
        countOfDirectContacts.add (countOfDirectContact);

        System.out.println ("Indirect contacts: ");
        Collection<User> indirectContacts = getIndirectContacts (user);
        indirectContacts.forEach (u-> System.out.println (u.hidden ()));
        System.out.printf ("Count of indirect contacts: %d\n",indirectContacts.size ());
        countOfIndirectContacts.add (indirectContacts.size ());

    }
}
class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException (String message) {
        super (message);
    }
}
public class StopCoronaTest {

    public static double timeBetweenInSeconds(ILocation location1, ILocation location2) {
        return Math.abs(Duration.between(location1.getTimestamp(), location2.getTimestamp()).getSeconds());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StopCoronaApp stopCoronaApp = new StopCoronaApp();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "REG": //register
                    String name = parts[1];
                    String id = parts[2];
                    try {
                        stopCoronaApp.addUser(name, id);
                    } catch (UserAlreadyExistException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "LOC": //add locations
                    id = parts[1];
                    List<ILocation> locations = new ArrayList<>();
                    for (int i = 2; i < parts.length; i += 3) {
                        locations.add(createLocationObject(parts[i], parts[i + 1], parts[i + 2]));
                    }
                    stopCoronaApp.addLocations(id, locations);

                    break;
                case "DET": //detect new cases
                    id = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    stopCoronaApp.detectNewCase(id, timestamp);

                    break;
                case "REP": //print report
                    stopCoronaApp.createReport();
                    break;
                default:
                    break;
            }
        }
    }

    private static ILocation createLocationObject(String lon, String lat, String timestamp) {
        return new ILocation() {
            @Override
            public double getLongitude() {
                return Double.parseDouble(lon);
            }

            @Override
            public double getLatitude() {
                return Double.parseDouble(lat);
            }

            @Override
            public LocalDateTime getTimestamp() {
                return LocalDateTime.parse(timestamp);
            }
        };
    }
}
