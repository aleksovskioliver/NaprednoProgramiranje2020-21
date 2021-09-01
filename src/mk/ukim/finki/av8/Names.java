package mk.ukim.finki.av8;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Name{
    String name;
    int counter;

    public Name(String name, int counter) {
        this.name = name;
        this.counter = counter;
    }

    public static Name createName(String line){
        String[] parts = line.split("\\s+");
        return new Name(parts[0],Integer.parseInt(parts[1]));
    }
}
public class Names {

    public static Map<String,Integer> createMapFromFile(String file){
        Map<String ,Integer> result = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.lines()
                    .map(line->Name.createName(line))
                    .forEach(name -> {
                        result.put(name.name, name.counter);
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String ,Integer> boys = createMapFromFile("C:\\Users\\Oliver\\IdeaProjects\\NaprednoProgramiranje2020-21\\src\\mk\\ukim\\finki\\av8\\boys");
        Map<String ,Integer> girls = createMapFromFile("C:\\Users\\Oliver\\IdeaProjects\\NaprednoProgramiranje2020-21\\src\\mk\\ukim\\finki\\av8\\girls");

        Map<String ,Integer> resultMap = new HashMap<>();
        Set<String> boysAndGirls = new HashSet<>();

        for (String boyName : boys.keySet()){
            if (girls.containsKey(boyName)){
                System.out.println(boyName + "-->" + (boys.get(boyName) + girls.get(boyName)));
            }
        }
        for (String maleName : boys.keySet()){
            if (girls.containsKey(maleName)){
                boysAndGirls.add(maleName);
            }
        }
        System.out.println(boysAndGirls);

        for (String boy : boys.keySet()){
            if (girls.containsKey(boy)){
                resultMap.put(boy,boys.get(boy)+girls.get(boy));
            }
        }
        System.out.println(resultMap);


    }
}
