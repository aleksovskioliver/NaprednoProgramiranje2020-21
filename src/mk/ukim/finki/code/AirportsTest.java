package mk.ukim.finki.code;
import java.util.*;

//vo aerodrom treba da gi imam site letovi do //Map<flightTo,TreeSet<Flight>
class Airport{
    String name;
    String country;
    String code;
    int passengers;
    Map<String ,TreeSet<Flight>> flightsTo;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flightsTo = new TreeMap<>();
    }
//    London Heathrow (LHR)
//    United Kingdom
//    70037417
//        1. LHR-ATL 12:44-17:35 4h51m

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d",
                name,
                code,
                country,
                passengers);
    }
    public void addFlight(String to,Flight flight){
        flightsTo.computeIfPresent(to,(key,value)->{
            value.add(flight);
            return value;
        });
        flightsTo.computeIfAbsent(to,(key)->{
            TreeSet<Flight> set = new TreeSet<>();
            set.add(flight);
            return set;
        });
    }
}
class Flight implements Comparable<Flight>{
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    @Override
    public int compareTo(Flight o) {
        int x = this.to.compareTo(o.to);
        if (x==0)
            return Integer.compare(this.time,o.time);
        return x;
    }
    String startTime(){
        int hours = time/60;
        int minutes = time%60;
        return String.format("%02d:%02d",hours,minutes);
    }
    String endTime(){
        int hours = (time+duration)/60;
        int minutes = (time+duration)%60;
        if (hours>=24)
            hours-=24;
        return String.format("%02d:%02d",hours,minutes);
    }
    String duration(){
        int startHour = time/60;
        int startMinutes = time%60;
        int endHour = (time+duration)/60;
        int endMinutes = (time+duration)%60;
        boolean day = false;

        int durMinutes = endMinutes - startMinutes;
        if (endHour>=24)
            day = true;
        if (durMinutes < 0){
            durMinutes += 60;
            endHour--;
        }
        int durHour = endHour - startHour;
        if (day)
            return String.format("+1d %dh%02dm",durHour,durMinutes);
        else
            return String.format("%dh%02dm",durHour,durMinutes);
    }
    //15. LHR-PHX 20:48-01:42 +1d 4h54m
    //8. LHR-HND 07:10-08:38 1h28m
    @Override
    public String toString() {
        return String.format("%s-%s %s-%s %s",
                from,to,
                startTime(),endTime(),duration());
    }
}
class Airports{
    //Map<Code,Airport>
    Map<String ,Airport> airportsByCode;
    public Airports(){
        this.airportsByCode = new HashMap<>();
    }
    public void addAirport(String name,String country,String code,int passengers){
        Airport airport = new Airport(name,country,code,passengers);
        airportsByCode.put(code,airport);
    }
    public void addFlights(String from, String to, int time, int duration){
        Flight flight = new Flight(from,to,time,duration);

        airportsByCode.computeIfPresent(from,(key,value)->{
            value.addFlight(to,flight);
            return value;
        });
    }
    public void showFlightsFromAirport(String code){
        Airport airport = airportsByCode.get(code);
        System.out.println(airport);

        int index = 1;
        for (String to : airport.flightsTo.keySet()){
            for (Flight flight : airport.flightsTo.get(to)){
                System.out.printf("%d. %s\n",index++,flight);
            }
        }
       // airport.flightsTo.values()
        //        .stream()
        //        .forEach(System.out::println);
    }
    public void showDirectFlightsFromTo(String from, String to){
        TreeSet<Flight> flightTreeSet = new TreeSet<>();
        Airport airport = airportsByCode.get(from);
        if (airport.flightsTo.get(to) == null) {
            System.out.printf("No flights from %s to %s\n", from, to);
            return;
        }
        airport.flightsTo.get(to)
                .stream().forEach(System.out::println);
    }
    public void showDirectFlightsTo(String to){
        TreeSet<Flight> flightsTo = new TreeSet<>();
        airportsByCode.values()
                .stream()
                .filter(each -> each.flightsTo.get(to) != null)
                .forEach(each -> each.flightsTo.get(to)
                        .stream()
                        .forEach(flight -> flightsTo.add(flight)));
        flightsTo.stream()
                .forEach(System.out::println);
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

