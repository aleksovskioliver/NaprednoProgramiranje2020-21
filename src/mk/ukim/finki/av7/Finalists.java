package mk.ukim.finki.av7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Finalists {
    public static List<Integer> generate(int finalists,int awards){
        Random random = new Random();
        List<Integer> awarded = new ArrayList<>();

        while (awarded.size() != awards){
            int finalist = random.nextInt(finalists) + 1;
            if (!awarded.contains(finalist))
                awarded.add(finalist);
        }
        return awarded;
    }

    public static void main(String[] args) {
        List<Integer> awarded = generate(30,3);
        System.out.println(awarded);
    }
}

