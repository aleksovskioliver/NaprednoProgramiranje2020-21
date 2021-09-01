package mk.ukim.finki.av8;

import java.util.*;
import java.util.stream.Collectors;

class Book implements Comparable<Book>{
    String title;
    String category;
    float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
    public String getTitle() {
        return title;
    }


    @Override
    public int compareTo(Book o) {
        int x = this.title.compareTo(o.title);
        if (x==0)
            return Float.compare(this.price,o.price);
        return x;
    }
    //A Brief History of Time (A) 25.66

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f",title,category,price);
    }
}
class BookCollection{
    List<Book> books;
    //Map<Category,TreeSet<Book>>
    Map<String,TreeSet<Book>> booksByCategory;

    public BookCollection(){
        this.books = new ArrayList<>();
        this.booksByCategory = new HashMap<>();
    }
    public void addBook(Book book){
        books.add(book);

        String category = book.getCategory().toLowerCase();
        booksByCategory.putIfAbsent(category,new TreeSet<>());
        booksByCategory.computeIfPresent(category,(k,v)->{
            v.add(book);
            return v;
        });
    }
    public void printByCategory(String category){
        if (!booksByCategory.containsKey(category.toLowerCase())){
            System.out.println("Nema knigi vo kategorijata");
        }else{
            booksByCategory.get(category.toLowerCase())
                    .forEach(System.out::println);
        }
    }
    public List<Book> getCheapestN(int n){
        Comparator<Book> comparator = Comparator.comparing(Book::getPrice)
                .thenComparing(Book::getTitle);
        return books.stream()
                .sorted(comparator)
                .limit(n).collect(Collectors.toList());
    }
}

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде