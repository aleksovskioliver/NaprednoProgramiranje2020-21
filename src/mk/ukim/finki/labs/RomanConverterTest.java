package mk.ukim.finki.labs;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        StringBuilder sb = new StringBuilder ();

        String[] romans = {"I","IV","V","VI","IX","X","XL","L","XC","C","CD","D","CM","M"};
        int[] integers = {1,4,5,6,9,10,40,50,90,100,400,500,900,1000};
        int tmp;
        for (int i=romans.length-1;i>=0;i--){
            tmp = n/integers[i];
            n %=integers[i];
            while (tmp>0){
                sb.append (romans[i]);
                tmp--;
            }
        }

        return sb.toString ();
    }

}
