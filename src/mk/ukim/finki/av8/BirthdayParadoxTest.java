package mk.ukim.finki.av8;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BirthdayParadoxTest {

    public static boolean singleTrial(int person){
        Random random = new Random();
        Set<Integer> birthdays = new HashSet<>();

        for (int i=0;i<person;i++){
            int rndBday = random.nextInt(364)+1;
            if (birthdays.contains(rndBday))
                return true;
            birthdays.add(rndBday);
        }
        return false;
    }
    public static double experiment(int person){
        int counter = 0;
        for (int i=0;i<10000;i++){
            if (singleTrial(person))
                ++counter;
        }
        return counter/10000.0;
    }

    public static void main(String[] args) {

        for (int i=2;i<=50;i++){
            System.out.printf("For %d people, the probability of two birthdays is about: %.5f\n",i,experiment(i));
        }
    }
}
