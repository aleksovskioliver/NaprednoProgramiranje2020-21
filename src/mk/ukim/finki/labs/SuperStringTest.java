package mk.ukim.finki.labs;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

class SuperString{
    LinkedList<String > linkedList;
    Stack<String > history;

    public SuperString(){
        this.linkedList = new LinkedList<> ();
        this.history = new Stack<> ();
    }
    void append(String s){
        linkedList.addLast (s);
        history.push (s);
    }
    void insert(String s){
        linkedList.addFirst (s);
        history.push (s);
    }

    @Override
    public String toString () {
       StringBuilder sb = new StringBuilder ();
       for (String string : linkedList){
           sb.append (string);
       }
       return sb.toString ();
    }

    boolean contains(String s){
        return toString ().contains (s);
    }
    void reverse(){
        LinkedList<String > reversed = new LinkedList<String > ();
        String str = null;
        while (!linkedList.isEmpty ()){
            str = linkedList.getLast ();
            str = reversedString (str);
            reversed.addLast (str);
            linkedList.removeLast ();
        }
        for (String s : reversed){
            linkedList.addLast (s);
        }
    }
    String reversedString(String zbor){
        StringBuilder sb = new StringBuilder (zbor);
        return sb.reverse ().toString ();
    }
    public void removeLast(int k){
        while (k>0){
            String toRemove = history.pop ();
            linkedList.remove(toRemove);
            String toRemoveReversed = reversedString (toRemove);
            linkedList.remove(toRemoveReversed);
            k--;
        }
    }
}

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
