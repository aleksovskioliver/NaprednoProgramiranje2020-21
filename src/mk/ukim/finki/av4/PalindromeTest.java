package mk.ukim.finki.av4;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PalindromeTest {

    public static List<String> readWords(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        return br.lines()
                .map(line -> line.toLowerCase())
                .filter(word -> isPalindrome(word))
                .collect(Collectors.toList());
    }

    public static boolean isPalindrome(String word){
        String reverseWord = new StringBuilder()
                .append(word)
                .reverse()
                .toString();
        return word.equals(reverseWord);
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("src/mk/ukim/finki/av4/words");
        List<String> words = readWords(new FileInputStream(file));

        String maxLengthPalindrome = words.get(0);
        for (String word : words){
            if (word.length() > maxLengthPalindrome.length())
                maxLengthPalindrome = word;
        }
        System.out.println(maxLengthPalindrome);


        maxLengthPalindrome = words.stream()
                .max(Comparator.comparingInt(w->w.length()))
                .get();

        System.out.println(maxLengthPalindrome);

    }
}
