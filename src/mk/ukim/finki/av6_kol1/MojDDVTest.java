package mk.ukim.finki.av6_kol1;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;

class AmountNotAllowedException extends Exception{
    int sumItems;

    public AmountNotAllowedException(int sumItems) {
        this.sumItems = sumItems;
    }
    @Override
    public String getMessage() {
        return String.format("Receipt with amount %d is not allowed to be scanned", sumItems);
    }
}

class Item{
    int price;
    char type;

    public Item(int price, char type) {
        this.price = price;
        this.type = type;
    }
    public double taxType(){
        switch (type){
            case 'A': return price*0.18;
            case 'B': return price*0.05;
            default: return price*0;
        }
    }
    public double returnDDV(){
        return taxType() * 0.15;
    }
}
class Receipt{
    String ID;
    List<Item> items;

    public Receipt(String ID, List<Item> items) {
        this.ID = ID;
        this.items = items;
    }

    public int priceSum(){
        return items.stream()
                .mapToInt(i->i.price)
                .sum();
    }
    public double taxSum(){
        return items.stream()
                .mapToDouble(i->i.returnDDV())
                .sum();
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f",ID,priceSum(),taxSum());
    }
    public static Receipt createReceipt(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        String ID = parts[0];
        List<Item> itemsList = new ArrayList<>();
        for (int i=1;i<parts.length;i+=2){
            int price = Integer.parseInt(parts[i]);
            char type = parts[i+1].charAt(0);
            itemsList.add(new Item(price,type));
        }
        Receipt receipt = new Receipt(ID,itemsList);
        if (receipt.priceSum()>30000)
            throw new AmountNotAllowedException(receipt.priceSum());
        return receipt;
    }
}
class MojDDV{
    List<Receipt> receipts;

    public MojDDV(){
        this.receipts = new ArrayList<>();
    }

    public void readRecords(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line=br.readLine())!=null){
            try {
                receipts.add(Receipt.createReceipt(line));
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        for (Receipt r : receipts)
            pw.println(r.toString());
        pw.flush();
    }

    public void printStatistics(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        DoubleSummaryStatistics dss = receipts.stream()
                .mapToDouble(i->i.taxSum())
                .summaryStatistics();
        pw.printf("min:\t%5.3f\nmax:\t%5.3f\nsum:\t%5.3f\ncount:\t%5d\naverage:\t%5.3f\n",
                dss.getMin(),
                dss.getMax(),
                dss.getSum(),
                dss.getCount(),
                dss.getAverage());


        pw.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        try {
            mojDDV.readRecords(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}