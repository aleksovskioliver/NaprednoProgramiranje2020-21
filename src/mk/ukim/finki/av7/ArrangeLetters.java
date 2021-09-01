package mk.ukim.finki.av7;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ArrangeLetters {

    public static String arrange(String input){
        String[] parts = input.split("\\s+");

        for (int i=0;i<parts.length;i++){
            char[] tmp = parts[i].toCharArray();
            Arrays.sort(tmp);
            parts[i] = new String(tmp);
        }
        return Arrays.stream(parts)
                .sorted().collect(Collectors.joining(" "));
    }
    public static String arrangeFunc(String input){
        String[] parts = input.split("\\s+");

       return Arrays.stream(parts)
                .map(part->{
                    char[] tmp = part.toCharArray();
                    Arrays.sort(tmp);
                    return new String(tmp);
                }).sorted().collect(Collectors.joining(" "));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();


        System.out.println(arrange(input));
        System.out.println(arrangeFunc(input));
    }
}
