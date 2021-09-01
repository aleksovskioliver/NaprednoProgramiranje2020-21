package mk.ukim.finki.av5;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;

public class MyMathGeneric {

    public static String statistic(List<? extends Number> numbers){
        DoubleSummaryStatistics dss = new DoubleSummaryStatistics();
        numbers.stream()
                .forEach(number -> dss.accept(number.doubleValue()));

        double sumStd = 0.0;
        for (Number n : numbers)
            sumStd += (n.doubleValue()- dss.getAverage()) * (n.doubleValue()- dss.getAverage());
        double std = Math.sqrt(sumStd/numbers.size());

        return String.format("Min: %.2f\nMax: %.2f\nAverage: %.2f\n" +
                "Standard deviation: %.2f\nCount: %d\nSum: %.2f\n",
                dss.getMin(),
                dss.getMax(),
                dss.getAverage(),
                std,
                dss.getCount(),
                dss.getSum());
    }

    public static void main(String[] args) {
        List<Integer> integers = new ArrayList<>();
        List<Double> doubles = new ArrayList<>();
        Random rnd = new Random();
        for (int i=0;i<100;i++)
            integers.add(rnd.nextInt(100)+1);

        for (int i=0;i<100;i++)
            doubles.add(rnd.nextDouble()*100.0);

        System.out.println(statistic(integers));
        System.out.println(statistic(doubles));
    }
}
