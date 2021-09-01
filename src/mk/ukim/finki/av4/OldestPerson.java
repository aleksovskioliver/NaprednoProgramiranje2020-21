package mk.ukim.finki.av4;

import java.io.*;
import java.util.Comparator;

class Person implements Comparable<Person>{
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public Person(String line){
        String[] parts = line.split("\\s+");
        this.name = parts[0];
        this.age = Integer.parseInt(parts[1]);
    }

    @Override
    public int compareTo(Person o) {
        return Integer.compare(this.age,o.age);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

public class OldestPerson {

    public static Person getOldestPerson(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        Person oldest = br.lines()
                .map(line -> new Person(line))
                .max(Comparator.naturalOrder())
                .get();
        return oldest;
    }
    public static void getOldestPerson2(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        Person oldest = br.lines()
                .map(line -> new Person(line))
                .max(Comparator.naturalOrder())
                .get();
        System.out.println(oldest);
    }

    public static void main(String[] args) {
        File file = new File("src/mk/ukim/finki/av4/Person");
        try {
            getOldestPerson2(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
