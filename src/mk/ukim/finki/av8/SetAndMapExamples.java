package mk.ukim.finki.av8;

import java.util.*;
import java.util.function.Function;

public class SetAndMapExamples {
    public static void main(String[] args) {

        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("123123");
        hashSet.add("Aleksovski");
        hashSet.add("Oliver");
        hashSet.add("Stefan");
        hashSet.add("Oliver");
        System.out.println(hashSet);

        LinkedHashSet<String> LinkedHashSet = new LinkedHashSet<>();
        LinkedHashSet.add("123123");
        LinkedHashSet.add("Oliver");
        LinkedHashSet.add("Aleksovski");
        LinkedHashSet.add("Stefan");
        LinkedHashSet.add("Oliver");
        System.out.println(LinkedHashSet);

        TreeSet<String> TreeSet = new TreeSet<>(Comparator.comparing(String::length).reversed().thenComparing(Function.identity()));
        TreeSet.add("123123");
        TreeSet.add("Oliver");
        TreeSet.add("Aleksovski");
        TreeSet.add("Stefan");
        TreeSet.add("Oliver");
        System.out.println(TreeSet);

        Map<String,String > hashMap = new HashMap<>();
        hashMap.put("Oliver","123123");
        hashMap.put("44444","finki123");
        hashMap.put("Stefan","skrskr");
        hashMap.put("Aleksovski","bammmmmmmbammmm");
        System.out.println(hashMap);

        Map<String,String > LinkedHashMap = new LinkedHashMap<>();
        LinkedHashMap.put("Oliver","123123");
        LinkedHashMap.put("44444","finki123");
        LinkedHashMap.put("Stefan","skrskr");
        LinkedHashMap.put("Aleksovski","bammmmmmmbammmm");
        System.out.println(LinkedHashMap);

        Map<String,String > TreeMap = new TreeMap<>(Comparator.comparing(String::length).thenComparing(Function.identity()));
        TreeMap.put("Oliver","123123");
        TreeMap.put("44444","finki123");
        TreeMap.put("Stefan","skrskr");
        TreeMap.put("Aleksovski","bammmmmmmbammmm");
        System.out.println(TreeMap);


    }
}
