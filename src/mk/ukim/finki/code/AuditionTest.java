package mk.ukim.finki.code;

import java.util.*;

class Participant{
    String city;
    String code;
    String name;
    int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
    public String getCode(){
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    //005 Наташа 19

    @Override
    public String toString() {
        return String.format("%s %s %d",code,name,age);
    }
}
class Audition{
    //Map<City,TreeSet<Particion>>
    Map<String , Set<Participant>> particpantsByCity;

    public Audition(){
        this.particpantsByCity = new HashMap<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        Participant participant = new Participant(city,code,name,age);

        particpantsByCity.putIfAbsent(participant.city, new HashSet<>());
        particpantsByCity.computeIfPresent(participant.city, (k,v)->{
            v.add(participant);
            return v;
        });
    }
    void listByCity(String city){
        Comparator<Participant> comparator = Comparator.comparing(Participant::getName)
                .thenComparing(Participant::getAge)
                .thenComparing(Participant::getCode);
        particpantsByCity.get(city)
                .stream()
                .sorted(comparator)
                .forEach(System.out::println);
    }
}


public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}