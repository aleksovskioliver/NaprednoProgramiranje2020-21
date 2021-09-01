package mk.ukim.finki.labs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
       Scanner scanner = new Scanner (inputStream);
       Map<String ,TreeSet<String >> words = new TreeMap<> ();

       while (scanner.hasNextLine ()){
           String line = scanner.nextLine ();

           boolean check = false;
           for (String key : words.keySet ()){
               check = isAnagram(key,line);
               if (check){
                   words.get (key).add (line);
                   break;
               }
           }
           if (!check){
               TreeSet<String > value = new TreeSet<> ();
               value.add (line);
               words.put (line,value);
           }
       }
       StringBuilder sb = new StringBuilder ();
       for (String key : words.keySet ()){
           if (words.get (key).size () >= 5){
               sb.append (String.join (" ",words.get (key)));
               sb.append ("\n");
           }
       }
        System.out.println (sb);
    }
    public static boolean isAnagram(String words1,String words2){
        char[] first = words1.toCharArray ();
        char[] second = words2.toCharArray ();

        Arrays.sort (first);
        Arrays.sort (second);
        return Arrays.equals (first,second);
    }
}
