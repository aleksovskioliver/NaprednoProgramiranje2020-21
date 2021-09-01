//package mk.ukim.finki.code;
//import java.io.*;
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Stream;
//
//class CosineSimilarityCalculator {
//    public static double cosineSimilarity (Collection<Integer> c1, Collection<Integer> c2) {
//        int [] array1;
//        int [] array2;
//        array1 = c1.stream().mapToInt(i -> i).toArray();
//        array2 = c2.stream().mapToInt(i -> i).toArray();
//        double up = 0.0;
//        double down1=0, down2=0;
//
//        for (int i=0;i<c1.size();i++) {
//            up+=(array1[i] * array2[i]);
//        }
//
//        for (int i=0;i<c1.size();i++) {
//            down1+=(array1[i]*array1[i]);
//        }
//
//        for (int i=0;i<c1.size();i++) {
//            down2+=(array2[i]*array2[i]);
//        }
//        return up/(Math.sqrt(down1)*Math.sqrt(down2));
//    }
//}
//class NGrams{
//    //Map<ngram,freq> sort leksikografski -> TreeMap
//    Map<String ,Integer> ngramsByFreq;
//    List<String > allNGrams; //-> lista so site NGrams
//    List<Map<String ,Integer>> textNGramsList;
//    public NGrams(){
//        this.ngramsByFreq = new TreeMap<>();
//        this.allNGrams = new ArrayList<>();
//        this.textNGramsList = new ArrayList<>();
//    }
//    public static List<String > getNGrams(String word){
//        List<String > result = new ArrayList<>();
//        for (int i=1;i<word.length(); i++){
//            for (int j=0;j<word.length()-i;j++){
//                result.add(word.toLowerCase().substring(j,j+i));
//            }
//        }
//        return result;
//    }
//    private void fillMap(Map<String ,Integer> map,String word){
//        map.putIfAbsent(word,0);
//        map.computeIfPresent(word,(key,value)->{
//            value++;
//            return value;
//        });
//    }
//    private void fillMaps(){
//        this.ngramsByFreq.keySet()
//                .forEach(word->{
//                    this.textNGramsList.forEach(map->{
//                        map.putIfAbsent(word,0);
//                    });
//                });
//    }
//    public void readTexts(InputStream is) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String line;
//        while ((line = br.readLine()) != null){
//            this.allNGrams.add(line);
//            line = line.replaceAll("[^A-Za-z0-9]","");
//            Map<String ,Integer> result = new TreeMap<>();
//
//            String[] words = line.split("\\s");
//            Arrays.stream(words)
//                    .map(w->NGrams.getNGrams(w))    //all ngrams
//                    .flatMap(Collection::stream)    //stream of all angrams
//                    .forEach(word->{
//                        fillMap(this.ngramsByFreq,word);
//                        fillMap(result,word);
//                    });
//            this.textNGramsList.add(result);
//        }
//    fillMaps();
//    }
//    public void printVectors(OutputStream os){
//        PrintWriter pw = new PrintWriter(os);
//        this.textNGramsList.forEach(pw::println);
//        pw.flush();
//    }
//    public void printAllNGrams(OutputStream os, int n, String sortBy){
//        PrintWriter pw = new PrintWriter(os);
//
//        Function<Map.Entry<String ,Integer>,String > mapToString = e->String.format("%s : %d",e.getKey(),e.getValue());
//        Stream<Map.Entry<String ,Integer>> filtered = this.ngramsByFreq.entrySet()
//                                                    .stream().filter(entry->entry.getKey().length()==n);
//
//        if (sortBy.equals("n-grams")){
//            filtered.map(mapToString).forEach(pw::println);
//        }else{
//            this.ngramsByFreq.entrySet()
//                    .stream()
//                    .filter(entry->entry.getKey().length()==n)
//                    .sorted(Map.Entry.comparingByValue())
//                    .map(e->String.format("%s : %d",e.getKey(),e.getValue()))
//                    .forEach(pw::println);
//        }
//        pw.flush();
//    }
//    public void mostSimilarTexts(OutputStream os){
//        PrintWriter pw = new PrintWriter(os);
//        double maxSimilarity = 0;
//        int iMax = 0,jMax = 0;
//        for (int i=0;i<allNGrams.size();i++){
//            for (int j=0;j<allNGrams.size();j++){
//                if (i!=j){
//                    double similarity = CosineSimilarityCalculator.cosineSimilarity(
//                            textNGramsList.get(i).values(),
//                            textNGramsList.get(j).values());
//                    if (similarity>maxSimilarity){
//                        maxSimilarity = similarity;
//                        iMax = i;
//                        jMax = j;
//                    }
//                }
//            }
//        }
//        pw.println(allNGrams.get(iMax));
//        pw.println(allNGrams.get(jMax));
//        pw.println(String.format("%.10f",maxSimilarity));
//        pw.flush();
//
//    }
//
//}
//
//public class NGramsTest {
//    public static void main(String[] args) {
//        NGrams nGrams = new NGrams();
//
//        try {
//            nGrams.readTexts(System.in);
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//
//        System.out.println("===PRINT VECTORS===");
//        nGrams.printVectors(System.out);
//
//        System.out.println("PRINT 3-grams SORTED BY FREQUENCY");
//        nGrams.printAllNGrams(System.out, 3, "value");
//
//        System.out.println("PRINT 1-grams SORTED BY FREQUENCY");
//        nGrams.printAllNGrams(System.out, 1, "n-grams");
//
//        System.out.println("===MOST SIMILAR TEXTS===");
//        nGrams.mostSimilarTexts(System.out);
//    }
//}