package mk.ukim.finki.av7;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

interface NumberReader{
    List<Integer> read(InputStream inputStream);
}
class LineNumbersReader implements NumberReader{

    @Override
    public List<Integer> read(InputStream inputStream) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines()
                    .filter(line -> !line.isEmpty())
                    .map(line -> Integer.parseInt(line.trim()))
                    .collect(Collectors.toList());
        }catch (IOException exception){
            System.out.println(exception.getMessage());
            return Collections.emptyList();
        }
    }
}
class CountVisualizer{
    private final int n;

    public CountVisualizer(int n) {
        this.n = n;
    }

    public void visualize(OutputStream outputStream,int[] counts){
        PrintWriter printWriter = new PrintWriter(outputStream);
        for (Integer count : counts){
            while (count>0){
                printWriter.write("*");
                count -= n;
            }
            printWriter.println();
        }
        printWriter.flush();
    }
}

public class BenfordLawTest {

    public static String INPUT_FILE = "C:\\Users\\Oliver\\IdeaProjects\\NaprednoProgramiranje2020-21\\src\\mk\\ukim\\finki\\av7\\numbersTest";

    public int[] counts(List<Integer> numbers){
        int[] result = new int[10];
        for (Integer number: numbers){
            int digit = firstDigit(number);
            result[digit]++;
        }
        return result;
    }
    static int firstDigit(int num){
        while (num>=10){
            num/=10;
        }
        return num;
    }

    public static void main(String[] args) {
        NumberReader numberReader = new LineNumbersReader();
        try {
            List<Integer> numbers = numberReader.read(new FileInputStream(INPUT_FILE));
            BenfordLawTest benfordLawTest = new BenfordLawTest();
            CountVisualizer countVisualizer = new CountVisualizer(10);
            countVisualizer.visualize(System.out, benfordLawTest.counts(numbers));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
