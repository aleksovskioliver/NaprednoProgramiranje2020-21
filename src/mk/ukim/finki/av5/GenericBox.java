package mk.ukim.finki.av5;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

interface Drawable{
    void draw();
}

class Box<T extends Drawable>{
    private List<T> elements;
    private static Random RANDOM = new Random();

    public Box(){
        elements = new ArrayList<>();
    }
    public void addElement(T element){
        elements.add(element);
    }
    public boolean isEmpty(){
       return elements.size() == 0;
    }
    public void drawElement(){
        if (isEmpty())
            return;

//        int idx = RANDOM.nextInt(elements.size());
//        T element = elements.get(idx);
//        elements.remove(element);
//        return element;

        T element =  elements.remove(RANDOM.nextInt(elements.size()));
        element.draw();
    }
}

public class GenericBox {
    static Random RANDOM = new Random();

    public static void main(String[] args) {

        Box<Drawable> box = new Box<>();
        for (int i=0;i<100;i++)
            box.addElement(() -> System.out.println(RANDOM));

        for (int i=0;i<102;i++)
            box.drawElement();
    }
}
