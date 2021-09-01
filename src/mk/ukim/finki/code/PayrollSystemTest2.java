package mk.ukim.finki.code;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

interface Employee{
    double calculateSalary();
    double getOvertimeSalary();
    int ticketsDone();
    double getBonus();

    void updateBonus(double amount);
    String getLevel();
}


abstract class EmployeeBase implements Employee,Comparable<EmployeeBase> {
    String ID;
    String level;
    double rate;
    double totalBonus;

    public EmployeeBase (String ID, String level, double rate) {
        this.ID = ID;
        this.level = level;
        this.rate = rate;
        this.totalBonus = 0.0;
    }

    public double getRate () {
        return rate;
    }

    public double getBonus () {
        return totalBonus;
    }

    public String getID () {
        return ID;
    }

    public String getLevel () {
        return level;
    }

    @Override
    public void updateBonus (double amount) {
        this.totalBonus += amount;
    }


    @Override
    public int compareTo (EmployeeBase o) {
        return Comparator.comparing (EmployeeBase::calculateSalary)
                .thenComparing (EmployeeBase::getLevel).compare (this,o);
    }

    @Override
    public String toString () {
        //Employee ID: bca8f6 Level: level9 Salary: 1127.50
        return String.format ("Employee ID: %s Level: %s Salary: %.2f",
                ID,level,calculateSalary () + totalBonus);
    }
}
class HourlyEmployee extends EmployeeBase{
    double hours;
    double regular;
    double overtime;

    public HourlyEmployee (String ID, String level, double rate, double hours) {
        super (ID, level, rate);
        this.hours = hours;
        this.overtime = Math.max (hours-40,0);
        this.regular = hours - overtime;
    }

    @Override
    public double calculateSalary () {
        return regular*rate + overtime*rate*1.5;
    }

    @Override
    public double getOvertimeSalary () {
        return overtime * rate * 1.5;
    }

    @Override
    public int ticketsDone () {
        return -1;
    }

    @Override
    public String toString () {
        return super.toString () + String.format (" Regular hours: %.2f Overtime hours: %.2f",regular,overtime);
    }
}
class FreelanceEmployee extends EmployeeBase{
    List<Integer> tickets;

    public FreelanceEmployee (String ID, String level, double rate, List<Integer> tickets) {
        super (ID, level, rate);
        this.tickets = new ArrayList<> ();
        this.tickets = tickets;
    }

    int sumOfPoints(){
        return tickets.stream ()
                .mapToInt (i->i)
                .sum ();
    }
    @Override
    public double calculateSalary () {
        return sumOfPoints () * rate;
    }

    @Override
    public double getOvertimeSalary () {
        return -1;
    }

    @Override
    public int ticketsDone () {
        return tickets.size ();
    }

    @Override
    public String toString () {
        return super.toString () + String.format (" Tickets count: %d Tickets points: %d",
                tickets.size (),
                sumOfPoints ());
    }
}
abstract class BonusDecorator implements Employee{
    Employee wrappedEmploye;

    public BonusDecorator (Employee wrappedEmploye) {
        this.wrappedEmploye = wrappedEmploye;
    }

    @Override
    public double getOvertimeSalary () {
        return wrappedEmploye.getOvertimeSalary ();
    }

    @Override
    public int ticketsDone () {
        return wrappedEmploye.ticketsDone ();
    }


    @Override
    public String getLevel () {
        return wrappedEmploye.getLevel ();
    }

    @Override
    public String toString () {
        return wrappedEmploye.toString () + String.format (" Bonus: %.2f",getBonus ());
    }


    @Override
    public void updateBonus (double amount) {
        wrappedEmploye.updateBonus (amount);
    }
}
class FixedBonusDecorator extends BonusDecorator{
    double fixedBonus;

    public FixedBonusDecorator (Employee wrappedEmploye, double fixedBonus) {
        super (wrappedEmploye);
        this.fixedBonus = fixedBonus;
    }

    @Override
    public double calculateSalary () {
        return wrappedEmploye.calculateSalary () + fixedBonus;
    }

    @Override
    public double getBonus () {
        return fixedBonus;
    }
}
class PercentageBonusDecorator extends BonusDecorator{
    double percent;
    double bonus;

    public PercentageBonusDecorator (Employee wrappedEmploye, double percent) {
        super (wrappedEmploye);
        this.percent = percent;
        this.bonus = wrappedEmploye.calculateSalary () * percent / 100.0;
        wrappedEmploye.updateBonus (this.bonus);
    }

    @Override
    public double calculateSalary () {
        double salaryWithoutBonus = wrappedEmploye.calculateSalary ();
        return salaryWithoutBonus + bonus;
    }

    @Override
    public double getBonus () {
        return bonus;
    }
}
class PayrollSystem{
    Map<String ,Double> hourlyRateByLevel;
    Map<String ,Double> ticketRateByLevel;
    List<Employee> employees;


    public PayrollSystem (Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        this.employees = new ArrayList<> ();
    }
    public Employee createEmployee (String line) throws BonusNotAllowedException {
        Employee e = EmployeeFactory.createEmployee (line,hourlyRateByLevel,ticketRateByLevel);
        employees.add (e);
        return e;
    }
    Map<String, Double> getOvertimeSalaryForLevels (){
        Map<String,Double> result = employees.stream ()
                .collect (Collectors.groupingBy (
                        Employee::getLevel,
                      //  TreeMap::new,
                        Collectors.summingDouble (Employee::getOvertimeSalary)
                ));
        List<String > keysWithZeroForRemove = result.keySet ()
                .stream ()
                .filter (key->result.get (key) == -1)
                .collect(Collectors.toList());
        keysWithZeroForRemove.forEach (result::remove);
        return result;
    }
    void printStatisticsForOvertimeSalary (){
        DoubleSummaryStatistics dss = employees.stream ()
                .mapToDouble (Employee::getOvertimeSalary)
                .summaryStatistics ();
        System.out.printf ("Statistics for overtime salary: Min: %.2f Average: %.2f Max: %.2f Sum: %.2f",
                dss.getMin (),
                dss.getAverage (),
                dss.getMax (),
                dss.getSum ());
    }
    Map<String, Integer> ticketsDoneByLevel(){
        return employees.stream ()
                .filter (e->e.ticketsDone () != -1)
                .collect (Collectors.groupingBy (
                        Employee::getLevel,
                        //  TreeMap::new,
                        Collectors.summingInt (Employee::ticketsDone)
                ));
    }
    Collection<Employee> getFirstNEmployeesByBonus (int n){
        Comparator<Employee> comparator = Comparator.comparing (Employee::getBonus).reversed ();

        return employees.stream ()
                .sorted (comparator)
                .limit (n)
                .collect(Collectors.toList());
    }
    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels){
        PrintWriter pw = new PrintWriter (os);

        Map<String ,Set<Employee>> resultMap = employees.stream ()
                .collect(Collectors.groupingBy (
                        Employee::getLevel,
                        TreeMap::new,
                        Collectors.toCollection (TreeSet::new)
                ));
        Set<String > keys = new HashSet<> (resultMap.keySet ());
        keys.stream ()
                .filter (k->!levels.contains (k))
                .forEach (resultMap::remove);
        return resultMap;
    }
}
class BonusNotAllowedException extends Exception{
    public BonusNotAllowedException (String percentage) {
        super(String.format("Bonus of %s is not allowed",percentage));
    }
}
class EmployeeFactory{
    //H;1ff293;level7;41.93
    //F;bca8f6;level9;3;6;7;1;6;2;7;7;2

    public static Employee createEmployee(String line,Map<String ,Double> hourlyRateByLevel,Map<String ,Double> ticketRateByLevel) throws BonusNotAllowedException {
        String[] partsBySpace = line.split ("\\s+");
        Employee e = createEmployeeWithoutBonus (partsBySpace[0],hourlyRateByLevel,ticketRateByLevel);

        if (partsBySpace.length>1){
            if (partsBySpace[1].contains ("%")){
                double percentage = Double.parseDouble (partsBySpace[1].substring (0,partsBySpace.length-1));
                if (percentage>20)
                    throw new BonusNotAllowedException(partsBySpace[1]);
                e = new PercentageBonusDecorator (e,percentage);
            }else{
                double bonusAmount = Double.parseDouble (partsBySpace[1]);
                if (bonusAmount>1000)
                    throw new BonusNotAllowedException(partsBySpace[1] + "$");
                e = new FixedBonusDecorator (e,bonusAmount);
            }
        }
        return e;
    }


    public static Employee createEmployeeWithoutBonus(String line,Map<String ,Double> hourlyRateByLevel,Map<String ,Double> ticketRateByLevel){
        String[] parts = line.split (";");
        String ID = parts[1];
        String level = parts[2];
        if (parts[0].equals ("H")){ //hourly
            double rate = hourlyRateByLevel.get (level);
            double hours = Double.parseDouble (parts[3]);
            return new HourlyEmployee (ID,level,rate,hours);
        }else{      //freelance
            double rate = ticketRateByLevel.get (level);
            List<Integer> result = new ArrayList<> ();
            for (int i=3;i<parts.length;i++){
                result.add (Integer.parseInt (parts[i]));
            }
            return new FreelanceEmployee (ID,level,rate,result);
        }
    }
}

public class PayrollSystemTest2 {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
        }

        Scanner sc = new Scanner(System.in);

        int employeesCount = Integer.parseInt(sc.nextLine());

        PayrollSystem ps = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
        Employee emp = null;
        for (int i = 0; i < employeesCount; i++) {
            try {
                emp = ps.createEmployee(sc.nextLine());
            } catch (BonusNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        int testCase = Integer.parseInt(sc.nextLine());

        switch (testCase) {
            case 1: //Testing createEmployee
                if (emp != null)
                    System.out.println(emp);
                break;
            case 2: //Testing getOvertimeSalaryForLevels()
                ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
                });
                break;
            case 3: //Testing printStatisticsForOvertimeSalary()
                ps.printStatisticsForOvertimeSalary();
                break;
            case 4: //Testing ticketsDoneByLevel
                ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
                });
                break;
            case 5: //Testing getFirstNEmployeesByBonus (int n)
                ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
                break;
        }

    }
}