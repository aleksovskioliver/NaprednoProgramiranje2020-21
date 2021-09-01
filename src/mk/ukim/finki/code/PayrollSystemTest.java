//package mk.ukim.finki.code;
//import java.io.*;
//import java.util.*;
//import java.util.stream.Collectors;
//
//abstract class Employee implements Comparable<Employee> {
//    String ID;
//    String level;
//    double rate;
//
//    public Employee (String ID, String level, double rate) {
//        this.ID = ID;
//        this.level = level;
//        this.rate = rate;
//    }
//
//    public String getID () {
//        return ID;
//    }
//
//    public String getLevel () {
//        return level;
//    }
//
//    abstract double calculateSalary();
//
//    @Override
//    public int compareTo (Employee o) {
//       return Comparator.comparing (Employee::calculateSalary).reversed ()
//                .thenComparing (Employee::getLevel).compare (this,o);
//    }
//
//    @Override
//    public String toString () {
//        //Employee ID: bca8f6 Level: level9 Salary: 1127.50
//        return String.format ("Employee ID: %s Level: %s Salary: %.2f",ID,level,calculateSalary ());
//    }
//}
//class HourlyEmployee extends Employee{
//    double hours;
//    double regular;
//    double overtime;
//
//    public HourlyEmployee (String ID, String level, double rate, double hours) {
//        super (ID, level, rate);
//        this.hours = hours;
//        this.overtime = Math.max (hours-40,0);
//        this.regular = hours - overtime;
//    }
//
//    @Override
//    double calculateSalary () {
//        return regular*rate + overtime*rate*1.5;
//    }
//
//    @Override
//    public String toString () {
//        return super.toString () + String.format (" Regular hours: %.2f Overtime hours: %.2f",regular,overtime);
//    }
//}
//class FreelanceEmployee extends Employee{
//    List<Integer> tickets;
//
//    public FreelanceEmployee (String ID, String level, double rate, List<Integer> tickets) {
//        super (ID, level, rate);
//        this.tickets = new ArrayList<> ();
//        this.tickets = tickets;
//    }
//
//    int sumOfPoints(){
//        return tickets.stream ()
//                .mapToInt (i->i)
//                .sum ();
//    }
//    @Override
//    double calculateSalary () {
//        return sumOfPoints () * rate;
//    }
//
//    @Override
//    public String toString () {
//        return super.toString () + String.format (" Tickets count: %d Tickets points: %d",
//                tickets.size (),
//                sumOfPoints ());
//    }
//}
//class PayrollSystem{
//    Map<String ,Double> hourlyRateByLevel;
//    Map<String ,Double> ticketRateByLevel;
//    List<Employee> employees;
//
//
//    public PayrollSystem (Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
//        this.hourlyRateByLevel = hourlyRateByLevel;
//        this.ticketRateByLevel = ticketRateByLevel;
//        this.employees = new ArrayList<> ();
//    }
//    void readEmployees(InputStream is){
//        BufferedReader br = new BufferedReader (new InputStreamReader (is));
//        employees = br.lines ()
//                .map (line->EmployeeFactory.createEmployee
//                        (line,hourlyRateByLevel,ticketRateByLevel))
//                .collect(Collectors.toList());
//    }
//    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels){
//        PrintWriter pw = new PrintWriter (os);
//
//       Map<String ,Set<Employee>> resultMap = employees.stream ()
//                .collect(Collectors.groupingBy (
//                        Employee::getLevel,
//                        TreeMap::new,
//                        Collectors.toCollection (TreeSet::new)
//                ));
//       Set<String > keys = new HashSet<> (resultMap.keySet ());
//       keys.stream ()
//               .filter (k->!levels.contains (k))
//               .forEach (resultMap::remove);
//       return resultMap;
//    }
//}
//class EmployeeFactory{
//    //H;1ff293;level7;41.93
//    //F;bca8f6;level9;3;6;7;1;6;2;7;7;2
//    public static Employee createEmployee(String line,Map<String ,Double> hourlyRateByLevel,Map<String ,Double> ticketRateByLevel){
//        String[] parts = line.split (";");
//        String ID = parts[1];
//        String level = parts[2];
//        if (parts[0].equals ("H")){ //hourly
//            double rate = hourlyRateByLevel.get (level);
//            double hours = Double.parseDouble (parts[3]);
//            return new HourlyEmployee (ID,level,rate,hours);
//        }else{      //freelance
//            double rate = ticketRateByLevel.get (level);
//            List<Integer> result = new ArrayList<> ();
//            for (int i=3;i<parts.length;i++){
//                result.add (Integer.parseInt (parts[i]));
//            }
//            return new FreelanceEmployee (ID,level,rate,result);
//        }
//    }
//}
//public class PayrollSystemTest {
//
//    public static void main(String[] args) {
//
//        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<> ();
//        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
//        for (int i = 1; i <= 10; i++) {
//            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
//            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
//        }
//
//        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
//
//        System.out.println("READING OF THE EMPLOYEES DATA");
//        payrollSystem.readEmployees(System.in);
//
//        System.out.println("PRINTING EMPLOYEES BY LEVEL");
//        Set<String> levels = new LinkedHashSet<> ();
//        for (int i=5;i<=10;i++) {
//            levels.add("level"+i);
//        }
//        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
//        result.forEach((level, employees) -> {
//            System.out.println("LEVEL: "+ level);
//            System.out.println("Employees: ");
//            employees.forEach(System.out::println);
//            System.out.println ("------------");
//        });
//
//
//    }
//}