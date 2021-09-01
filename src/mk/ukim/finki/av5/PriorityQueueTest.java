package mk.ukim.finki.av5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Node<T> implements Comparable<Node<T>>{
    T item;
    int priority;

    public Node(T item,int priority){
        this.item = item;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Node" +
                "item=" + item +
                ", priority=" + priority;
    }

    @Override
    public int compareTo(Node<T> o) {
        return Integer.compare(this.priority,o.priority);
    }
}

class PriorityQueue<T>{
    List<Node<T>> elements;

    public PriorityQueue(){
        this.elements = new ArrayList<>();
    }
    public void add(T item,int priority){
        Node<T> newNode = new Node<>(item,priority);
        int i;
        for (i=0;i< elements.size();i++){
            if (newNode.compareTo(elements.get(i))<=0) {
                break;
            }
        }
        elements.add(i,newNode);
    }
    public T remove(){
        if(elements.size() == 0)
            return null;

        return elements.remove(elements.size()-1).item;
    }
}

public class PriorityQueueTest {
    public static void main(String[] args) {
        PriorityQueue<String> queue = new PriorityQueue<>();
        queue.add("sreden0",49);
        queue.add("sreden2",55);
        queue.add("najnizok",20);
        queue.add("najvisok",99);
        queue.add("sreden1",50);
        queue.add("visok",69);

        String element;

        while ((element = queue.remove()) != null)
            System.out.println(element);


    }
}
