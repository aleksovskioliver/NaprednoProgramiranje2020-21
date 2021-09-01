package mk.ukim.finki.code;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class Element{
    int redenBr;
    String from;
    String to;
    String text;

    public Element(ArrayList<String> list){
        this.redenBr = Integer.parseInt(list.get(0));
        String[] parts = list.get(1).split(" --> ");
        this.from = parts[0];
        this.to = parts[1];
        for (int i=2;i<list.size();i++){
            text += list.get(i) + "\n";
        }
    }

    @Override
    public String toString() {
        return String.format("%d\n%s --> %s\n%s",redenBr,from,to,text);
    }
    void shift(int ms) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");
        Date timeFrom = sdf.parse(from);
        Date timeTo = sdf.parse(to);

        String newFrom;
        String newTo;

        newFrom = sdf.format(timeFrom.getTime() + ms);
        newTo = sdf.format(timeTo.getTime() + ms);

        this.from = newFrom;
        this.to = newTo;
    }
}
class Subtitles{
    List<Element> elementList;
   // int vkupnoSubs;
    public Subtitles(){
        this.elementList = new ArrayList<>();
     //   this.vkupnoSubs = 0;
    }
    int loadSubtitles(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        ArrayList<String > lines = new ArrayList<>();
        while ((line=br.readLine())!=null){
            if (line.isEmpty()){
                elementList.add(new Element(lines));
                lines = new ArrayList<>();

            }else{
                lines.add(line);
            }
        }
        if (lines.size()>1){
            elementList.add(new Element(lines));
        }
        br.close();
        return elementList.size();
    }
    void print(){
        elementList.stream()
                .forEach(System.out::println);
    }
    void shift(int ms){
        elementList.stream()
                .forEach(e-> {
                    try {
                        e.shift(ms);
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                });
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = 0;
        try {
            n = subtitles.loadSubtitles(System.in);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде
