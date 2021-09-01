//package mk.ukim.finki.code;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Scanner;
//import java.util.TreeSet;
//
//class Measurement implements Comparable<Measurement>{
//    float temperature;
//    float wind;
//    float humidity;
//    float visibility;
//    Date date;
//
//    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
//        this.temperature = temperature;
//        this.wind = wind;
//        this.humidity = humidity;
//        this.visibility = visibility;
//        this.date = date;
//    }
//
//    @Override
//    public String toString() {
//        //DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz YYYY");
//        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz YYY");
//        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",temperature,wind,humidity,visibility,df.format(date));
//    }
//    void isOlderThan(){
//
//    }
//
//    @Override
//    public int compareTo(Measurement o) {
//        return date.compareTo(o.date);
//    }
//}
//class WeatherStation{
//    int days;
//    TreeSet<Measurement> measurements;  //vo TreeSet za da bidat podredeni spored compareTo
//
//    WeatherStation(int days){
//        this.days = days;
//    }
//    static void checkOlderThan(Date d){
//        int newDateDay = d.getDay();
//        if (newDateDay)
//    }
//    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date){
//            Measurement newMeasurement = new Measurement(temperature,wind,humidity,visibility,date);
//        novioDatum - starioDatum < days;
//            int day = date.getDay();
//
//            for (Measurement m : measurements){
//                if (day-m.date.getDay() < days){
//                    measurements.remove(m);
//                }
//                if (m.date.)
//            }
//
//    }
//}
//
//public class WeatherStationTest {
//    public static void main(String[] args) throws ParseException {
//        Scanner scanner = new Scanner(System.in);
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//        int n = scanner.nextInt();
//        scanner.nextLine();
//        WeatherStation ws = new WeatherStation(n);
//        while (true) {
//            String line = scanner.nextLine();
//            if (line.equals("=====")) {
//                break;
//            }
//            String[] parts = line.split(" ");
//            float temp = Float.parseFloat(parts[0]);
//            float wind = Float.parseFloat(parts[1]);
//            float hum = Float.parseFloat(parts[2]);
//            float vis = Float.parseFloat(parts[3]);
//            line = scanner.nextLine();
//            Date date = df.parse(line);
//            ws.addMeasurment(temp, wind, hum, vis, date);
//        }
//        String line = scanner.nextLine();
//        Date from = df.parse(line);
//        line = scanner.nextLine();
//        Date to = df.parse(line);
//        scanner.close();
//        System.out.println(ws.total());
//        try {
//            ws.status(from, to);
//        } catch (RuntimeException e) {
//            System.out.println(e);
//        }
//    }
//}
//
//// vashiot kod ovde