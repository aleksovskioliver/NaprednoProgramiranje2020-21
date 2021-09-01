package mk.ukim.finki.code;//package mk.ukim.finki.code;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;


class Event implements Comparable<Event>{
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    @Override
    public int compareTo(Event o) {
        int x = date.compareTo(o.date);
        if (x==0)
            return name.compareTo(o.name);
        return x;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd MMM, YYY HH:mm");
        String newDate = df.format(date);
        return String.format("%s at %s, %s",newDate,location,name);
    }
}
class EventCalendar{
    int year;
    //Map<DayAndMonth,TreeSet<Event>>
    Map<String , TreeSet<Event>> eventByDate;
    //Map<Month,NoEvents>
    Map<Integer,Integer> monthlyEvent;

    public EventCalendar(int year){
        this.year = year;
        this.eventByDate = new HashMap<>();
        this.monthlyEvent = new TreeMap<>();
        IntStream.range(0,12)
                .forEach(i->monthlyEvent.put(i,0));
    }
    public void addEvent(String name, String location, Date date) throws WrongDateException {
        Event e = new Event(name,location,date);
        int dateYear = date.getYear();
        if (dateYear != (year-1900))
            throw new WrongDateException(date);
        String time = String.format("%d %d",date.getDate(),date.getMonth());
        eventByDate.putIfAbsent(time,new TreeSet<>());
        eventByDate.computeIfPresent(time,(key,value)->{
            value.add(e);
            return value;
        });

        int dateMonth = date.getMonth();
        monthlyEvent.computeIfPresent(dateMonth,(k,v)->{
            v++;
            return v;
        });
    }
    public void listEvents(Date date){
        String time = String.format("%d %d",date.getDate(),date.getMonth());
        TreeSet<Event> result = eventByDate.get(time);
        if (result==null){
            System.out.println("No events on this day!");
        }else{
            result.stream()
                    .forEach(System.out::println);
        }
    }
    public void listByMonth(){
        monthlyEvent.entrySet()
                .stream()
                .map(entry -> String.format("%d : %d",entry.getKey()+1,entry.getValue()))
                .forEach(System.out::println);
    }

}
class WrongDateException extends Exception{
    public WrongDateException(Date date) {
        super(String.format("Wrong date: %s",date.toString().replaceAll("GMT","UTC")));
    }
}


public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde