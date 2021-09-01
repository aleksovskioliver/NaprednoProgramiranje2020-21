package mk.ukim.finki.av6_kol1;

import java.util.Scanner;

public class MinAndMax{
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
class MinMax <T extends Comparable<T>>{
    T min;
    T max;
    int total;
    int minCount;
    int maxCount;

    public MinMax(){
        total = 0;
        minCount = 0;
        maxCount = 0;
    }
    void update(T element){
        if (total == 0){
            min = element;
            max = element;
        }
        ++total;
        if (element.compareTo(min)<0) {
            minCount = 1;
            min = element;
        }else{
            if (element.compareTo(min)==0){
                minCount++;
            }
        }
        if (element.compareTo(max)>0) {
            maxCount = 1;
            max = element;
        }else{
            if (element.compareTo(max)==0){
                maxCount++;
            }
        }

    }
    public T max(){
        return max;
    }
    public T min(){
        return min;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d",min,max,total);
    }
}