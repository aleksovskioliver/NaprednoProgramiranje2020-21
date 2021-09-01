package mk.ukim.finki.labs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency{
    Map<String ,Integer> wordsByFreq;
    int count;

    public TermFrequency(){
        this.wordsByFreq = new TreeMap<> ();
        this.count = 0;
    }
    public TermFrequency(InputStream inputStream,String[] stopWords){
        this();

        Scanner scanner = new Scanner (inputStream);
        while (scanner.hasNext ()){
            String word = scanner.next ();
            word = word.toLowerCase ().replace (",","").replace (".","").trim ();
            if (!(word.isEmpty () || Arrays.asList (stopWords).contains (word))){
                wordsByFreq.putIfAbsent (word,0);
                wordsByFreq.computeIfPresent (word,(k,v)->{
                    ++v;
                    return v;
                });
                count++;
            }
        }
    }
    int countTotal(){
        return count;
    }
    int countDistinct(){
        return wordsByFreq.size ();
    }
    public List<String > mostOften(int k){
        return wordsByFreq.keySet ().stream ()
                .sorted (Comparator.comparing (key->
                        wordsByFreq.get (key)).reversed ())
        .collect(Collectors.toList()).subList (0,k);
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in, stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
