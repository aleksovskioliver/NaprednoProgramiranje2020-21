package mk.ukim.finki.code;

import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<T>>{
    List<TreeSet<T>> blocks;
    int capacity;

    public BlockContainer(int n){
        this.blocks = new ArrayList<>();
        this.capacity = n;
    }
    public void add(T a){
        if (blocks.isEmpty()){
            TreeSet<T> newSet = new TreeSet<>();
            newSet.add(a);
            blocks.add(newSet);
        }else if(blocks.get(blocks.size()-1).size() < capacity){
            blocks.get(blocks.size()-1).add(a);
        }else{
            TreeSet<T> newSet = new TreeSet<>();
            newSet.add(a);
            blocks.add(newSet);
        }
    }
    public boolean remove(T a){
        boolean flag = blocks.get(blocks.size()-1).remove(a);
        if (blocks.get(blocks.size()-1).isEmpty())
            blocks.remove(blocks.size()-1);
        return flag;
    }
    public void sort(){
        List<T> all = blocks.stream()   //stream od treeset
                .flatMap(block->block.stream()) //stream od site elementiu treset
                .sorted(Comparable::compareTo)  //sortirani
                .collect(Collectors.toList());  //zbereni vo lista
        blocks.clear();

        for (T element : all){
            this.add(element);
        }
    }

    //[7, 8, 9],[1, 2, 3],[5, 6, 12],[4, 10, 8]
    @Override
    public String toString() {
        return blocks.stream()
                .map(block->"[" + block.stream()
                        .map(each->each.toString())
                        .collect(Collectors.joining(", "))
                        + "]").collect(Collectors.joining(","));
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде



