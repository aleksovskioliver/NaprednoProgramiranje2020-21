package mk.ukim.finki.code;

import java.io.*;
import java.util.*;

class CosineSimilarityCalculator {
    public static double cosineSimilarity (Collection<Integer> c1, Collection<Integer> c2) {
        int [] array1;
        int [] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1=0, down2=0;

        for (int i=0;i<c1.size();i++) {
            up+=(array1[i] * array2[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down1+=(array1[i]*array1[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down2+=(array2[i]*array2[i]);
        }

        return up/(Math.sqrt(down1)*Math.sqrt(down2));
    }
}
class TextProcessor{
    List<String> rawText;
    Map<String ,Integer> wordsByFrequency;
    List<Map<String ,Integer>> allWordsByFreq;

    public TextProcessor(){
        this.rawText = new ArrayList<>();
        this.wordsByFrequency = new TreeMap<>();
        this.allWordsByFreq = new ArrayList<>();
    }
    void fillMap(Map<String ,Integer> map, String line){
        String[] parts = line.split ("\\s+");
        Arrays.stream (parts)
                .forEach (part->{
                    map.putIfAbsent (part, 0);
                    map.computeIfPresent (part, (k, v) -> {
                        v++;
                        return v;
                    });
                });
    }
    void fillMaps(){
        wordsByFrequency.keySet()
                .forEach(key->{
                    allWordsByFreq.forEach(map->{
                        map.putIfAbsent(key,0);
                    });
                });
    }
    void readText(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line=br.readLine())!=null){
            rawText.add(line);

            line = line.replaceAll("[^A-Za-z\\s+]","");
            line = line.toLowerCase();

            Map<String,Integer> result = new TreeMap<>();

            fillMap(wordsByFrequency,line);
            fillMap(result,line);

            allWordsByFreq.add(result);
        }
        fillMaps();
    }
    void printTextsVectors(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        allWordsByFreq.stream()
                .map(map->map.values())
                .forEach(pw::println);
        pw.flush();
    }
    void printCorpus(OutputStream os, int n, boolean ascending){
        Comparator<Map.Entry<String,Integer>> comparator = Comparator.comparing (Map.Entry::getValue);
        PrintWriter pw = new PrintWriter(os);

        wordsByFrequency.entrySet().stream()
                .sorted(ascending ? comparator : comparator.reversed())
                .limit(n)
                .forEach(entry->pw.println(String.format ("%s : %d",entry.getKey (),entry.getValue ())));
        pw.flush();
    }
    public void mostSimilarTexts(OutputStream os){
        PrintWriter pw = new PrintWriter(os);

        double maxSimilarity = 0.0;
        int maxI=0,maxJ=0;
        for (int i=0;i<rawText.size();i++){
            for (int j=0;j<rawText.size();j++){
                if (i!=j){
                    double similarity = CosineSimilarityCalculator.cosineSimilarity(
                            allWordsByFreq.get(i).values(),
                            allWordsByFreq.get(j).values()
                    );
                    if (similarity>maxSimilarity){
                        maxSimilarity = similarity;
                        maxI = i;
                        maxJ = j;
                    }
                }
            }
        }
        pw.println(rawText.get(maxI));
        pw.println(rawText.get(maxJ));
        pw.println(String.format("%.10f",maxSimilarity));

        pw.flush();
    }

}

public class TextProcessorTest {

    public static void main(String[] args) throws IOException {
        TextProcessor textProcessor = new TextProcessor();

        textProcessor.readText(System.in);

        System.out.println("===PRINT VECTORS===");
        textProcessor.printTextsVectors(System.out);

        System.out.println("PRINT FIRST 20 WORDS SORTED ASCENDING BY FREQUENCY ");
        textProcessor.printCorpus(System.out,  20, true);

        System.out.println("PRINT FIRST 20 WORDS SORTED DESCENDING BY FREQUENCY");
        textProcessor.printCorpus(System.out, 20, false);

        System.out.println("===MOST SIMILAR TEXTS===");
        textProcessor.mostSimilarTexts(System.out);
    }
}