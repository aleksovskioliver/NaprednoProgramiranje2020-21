package mk.ukim.finki.av4;

import java.io.*;
import java.util.function.Consumer;

class LineConsumer implements Consumer<String>{

    int lines = 0, words = 0, chars = 0;

    @Override
    public void accept(String s) {
        lines++;
        words += s.split("\\s+").length;
        chars += s.length();
    }

    @Override
    public String toString() {
        return "LineConsumer{" +
                "lines=" + lines +
                ", words=" + words +
                ", chars=" + chars +
                '}';
    }
}


class LineCounter{
    int lines;
    int words;
    int characters;

    public LineCounter(int lines, int words, int characters) {
        this.lines = lines;
        this.words = words;
        this.characters = characters;
    }
    public LineCounter(String line){
        this.lines = 1;
        this.words = line.split("\\s+").length;
        this.characters = line.length();
    }
    public LineCounter sum(LineCounter other){
        return new LineCounter(
                this.lines + other.lines,
                this.words + other.words,
                this.characters + other.characters
        );
    }

    @Override
    public String toString() {
        return "LineCounter{" +
                "lines=" + lines +
                ", words=" + words +
                ", characters=" + characters +
                '}';
    }
}

public class WordCountTest2 {

    public static void calculateWithBufferedReaderAndMapReduce(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        LineCounter result = br.lines()
                .map(line -> new LineCounter(line))
                .reduce(
                        new LineCounter(0,0,0),
                        (left,right) -> left.sum(right)
                );
        System.out.println(result);
    }
    public static void calculateWithBufferedReaderAndConsumer(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        LineConsumer lineConsumer = new LineConsumer();
        br.lines().forEach(lineConsumer);
        System.out.println(lineConsumer);
    }

    public static void calculateWithBufferedReader(InputStream inputStream) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        int lines = 0, words = 0, characters = 0;
        String line;
        while ((line = bf.readLine()) != null){
            lines++;
            words += line.split("\\s+").length;
            characters += line.length();
        }

        System.out.printf("Lines: %d , words: %d , chars: %d",lines,words,characters);
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("src/mk/ukim/finki/av4/myFile");
        try {
            calculateWithBufferedReader(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            calculateWithBufferedReaderAndMapReduce(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        calculateWithBufferedReaderAndConsumer(new FileInputStream(file));
    }
}
