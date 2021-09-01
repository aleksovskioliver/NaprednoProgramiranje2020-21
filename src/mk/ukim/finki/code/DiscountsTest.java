package mk.ukim.finki.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class Store{
    String name;
    //Map<originalPrice,discountPrice>
    Map<Integer,Integer> originalAndDiscountPrice;
    public Store(String line){
        String[] parts = line.split("\\s+");
        this.name = parts[0];
        this.originalAndDiscountPrice = new TreeMap<>();
        for (int i=1;i<parts.length;i++){
            String[] pricesParts = parts[i].split(":");
            int discountPrice = Integer.parseInt(pricesParts[0]);
            int originalPrice = Integer.parseInt(pricesParts[1]);
            originalAndDiscountPrice.put(originalPrice,discountPrice);
        }
    }

    public String getName() {
        return name;
    }

    double getPercentage(int originalPrice, int discountPrice){
        return Math.floor(100.0 - (100.0/originalPrice*discountPrice));
    }
    int getDiscount(int originalPrice,int discountPrice){
        return originalPrice-discountPrice;
    }
    double averageDiscount(){
        return originalAndDiscountPrice.entrySet()
                .stream()
                .mapToDouble(entry->getPercentage(entry.getKey(),entry.getValue()))
                .average().orElse(0.0);
    }
    int totalDiscount(){
        return originalAndDiscountPrice.entrySet()
                .stream()
                .mapToInt(entry->getDiscount(entry.getKey(),entry.getValue()))
                .sum();
    }

    @Override
    public String toString() {
        Comparator<Map.Entry<Integer,Integer>> comparator = (first,second)->{
            double p1 = getPercentage(first.getKey(),first.getValue());
            double p2 = getPercentage(second.getKey(),second.getValue());
            int res = Double.compare(p1,p2);
            if (res == 0){
                int y = second.getKey().compareTo(first.getKey());
                if (y==0)
                    return second.getValue().compareTo(first.getValue());
                else return y;
            }
            return -res;
        };

        return String.format("%s\nAverage discount: %.1f%%\nTotal discount: %d\n" +
                        "%s",name, averageDiscount(),totalDiscount(),
                originalAndDiscountPrice.entrySet()
                        .stream()
                        .sorted(comparator)
                        .map(entry->String.format("%2.0f%% %d/%d",
                                getPercentage(entry.getKey(), entry.getValue()),
                                entry.getValue(),entry.getKey())).collect(Collectors.joining("\n")));
    }
}
class Discounts{
    List<Store> stores;

    public Discounts(){
        this.stores = new ArrayList<>();
    }
    public int readStores(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line=br.readLine())!=null){
            stores.add(new Store(line));
        }
        return stores.size();
    }
    public List<Store> byAverageDiscount(){
        Comparator<Store> comparator = Comparator.comparing(Store::averageDiscount)
                .thenComparing(Store::getName);
        return stores.stream()
                .sorted(comparator.reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
    public List<Store> byTotalDiscount(){
        Comparator<Store> TotalComparator = Comparator.comparing(Store::totalDiscount)
                .thenComparing(Store::getName);

        return stores.stream()
                .sorted(TotalComparator)
                .limit(3)
                .collect(Collectors.toList());
    }
}

public class DiscountsTest {
    public static void main(String[] args) throws IOException {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde