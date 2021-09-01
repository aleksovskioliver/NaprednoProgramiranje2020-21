package mk.ukim.finki.av4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class WordCountTest {

    public static void calculateWithScanner(InputStream inputStream){
        int lines = 0,words = 0 ,characters = 0;

        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            lines++;
            words += line.split("\\s+").length;
            characters += line.length();
        }
        sc.close();
        System.out.printf("Lines: %d , words: %d , characters: %d",lines,words,characters);

    }

    public static void main(String[] args) {
        File file = new File("src/mk/ukim/finki/av4/myFile");
        try {
            calculateWithScanner(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
