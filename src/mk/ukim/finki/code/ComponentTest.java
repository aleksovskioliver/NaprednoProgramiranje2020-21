package mk.ukim.finki.code;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

class Component implements Comparable<Component>{
    String color;
    int weight;
    TreeSet<Component> components;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.components = new TreeSet<>();
    }
    void addComponent(Component component){
        components.add(component);
    }

    @Override
    public int compareTo(Component o) {
        int x = Integer.compare(this.weight,o.weight);
        if (x==0)
            return this.color.compareTo(o.color);
        return x;
    }
    public String toString(String zbor) {
        StringBuilder sb = new StringBuilder();
        sb.append(zbor).append(String.format("%d:%s\n",weight,color));
        for (Component component : components){
            sb.append(component.toString(zbor + "---"));
        }
        return sb.toString();
    }
    void changeColor(int weight,String color){
       if (this.weight<weight)
           this.color = color;
       for (Component c : components){
           c.changeColor(weight,color);
       }
    }
}
class Window{
    String name;
    //Map<Position,Component>
    Map<Integer,Component> componentByPosition;

    public Window(String name) {
        this.name = name;
        this.componentByPosition = new TreeMap<>();
    }
    void addComponent(int position,Component component) throws InvalidPositionException {
        if (componentByPosition.containsKey(position))
            throw new InvalidPositionException(position);

        componentByPosition.put(position,component);
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       sb.append("WINDOW ").append(name + "\n");
       for (Integer position : componentByPosition.keySet()){
           sb.append(position + ":");
           sb.append(componentByPosition.get(position).toString(""));
       }
        return sb.toString();
    }
    void changeColor(int weight,String color){
        componentByPosition.values()
                .stream()
                .forEach(c->c.changeColor(weight,color));
    }
    void swichComponents(int pos1,int pos2){
        Component c1 = componentByPosition.get(pos1);
        Component c2 = componentByPosition.get(pos2);

        componentByPosition.put(pos1,c2);
        componentByPosition.put(pos2,c1);
    }
}
class InvalidPositionException extends Exception{
    public InvalidPositionException(int pos) {
        super(String.format("Invalid position %d, alredy taken!",pos));
    }
}
public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде