package mk.ukim.finki.code;

import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 2
 */
class Point2D{
    long id;
    float x;
    float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    double distance(Point2D other){
        return Math.sqrt(Math.pow(this.x-other.x,2) + Math.pow(this.y-other.y,2));
    }
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%d",id);
    }
}
class WrappedElement<T extends Point2D>{
    T element;
    double distance;

    public WrappedElement(T element, double distance) {
        this.element = element;
        this.distance = distance;
    }

    public T getElement() {
        return element;
    }

    public double getDistance() {
        return distance;
    }
}
class Cluster<T extends Point2D>{

    Map<Long,T> elements;

    public Cluster(){
        this.elements = new HashMap<>();
    }
    public void addItem(T element){
        elements.put(element.id, element);
    }
    void near(long id, int top){
        T target = elements.get(id);

        List<WrappedElement> nearest = elements.values()
                .stream()
                .filter(e->e.getId()!= target.getId())
                .map(x->new WrappedElement<>(x,x.distance(target)))
                .sorted(Comparator.comparing(WrappedElement::getDistance))
                .limit(top)
                .collect(Collectors.toList());

        for (int i=0;i<nearest.size();i++){
            System.out.printf("%d. %s -> %.3f\n",i+1,nearest.get(i).getElement(),nearest.get(i).getDistance());
        }
    }
}

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here