package mk.ukim.finki.code;

import java.util.*;

class Names{
    //Map<Name,freq> = new TreeMap
    Map<String ,Integer> namesAndFreq;

    public Names(){
        this.namesAndFreq = new TreeMap<>();
    }
    public void addName(String name){
        this.namesAndFreq.putIfAbsent(name,0);
        this.namesAndFreq.computeIfPresent(name,(k,v)->{
            v++;
            return v;
        });
    }
    int uniquesChars(String name){
        return (int) name.toLowerCase()
                .chars()
                .distinct()
                .count();
    }
    public void printN(int n){
        namesAndFreq.entrySet()
                .stream()
                .filter(key->key.getValue()>=n)
                //    //Hannah (10) 3
                .forEach(e-> System.out.printf("%s (%d) %d\n",e.getKey(),e.getValue(),uniquesChars(e.getKey())));
    }
    public String findName(int len,int x){
        int count = (int) namesAndFreq.entrySet()
                .stream()
                .filter(e->e.getKey().length()<len)
                .count();

        Map.Entry<String ,Integer> entry = namesAndFreq.entrySet()
                .stream()
                .filter(e->e.getKey().length()<len)
                .skip(x%count)
                .findFirst().orElse(null);
        return entry.getKey();
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde