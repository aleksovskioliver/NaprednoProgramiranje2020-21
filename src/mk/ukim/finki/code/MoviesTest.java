//package mk.ukim.finki.code;
//import java.util.*;
//import java.util.stream.Collectors;
//
//class Movie implements Comparable<Movie>{
//    String title;
//    String director;
//    String genre;
//    float rating;
//
//    public Movie(String title, String director, String genre, float rating) {
//        this.title = title;
//        this.director = director;
//        this.genre = genre;
//        this.rating = rating;
//    }
//
//    @Override
//    public String toString() {
//        return String.format("%s (%s, %s) %.2f",title,director,genre,rating);
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getDirector() {
//        return director;
//    }
//
//    public String getGenre() {
//        return genre;
//    }
//
//    public float getRating() {
//        return rating;
//    }
//
//    @Override
//    public int compareTo(Movie o) {
//        Comparator<Movie> MovieComparator =
//                Comparator.comparing(Movie::getTitle)
//                .thenComparing(Movie::getRating);
//        return MovieComparator.compare(this,o);
//    }
//}
//class MoviesCollection{
//    List<Movie> movies;
//    //Map<genre,treeset<movies>>
//    Map<String ,Set<Movie>> byGenre;
//
//    public MoviesCollection(){
//        this.movies = new ArrayList<>();
//        this.byGenre = new HashMap<>();
//    }
//    public void addMovie(Movie movie){
//        this.movies.add(movie);
//
//        byGenre.putIfAbsent(movie.genre,new TreeSet<>());
//        byGenre.get(movie.genre).add(movie);
//    }
//
//    public void printByGenre(String genre) {
//        byGenre.get(genre)
//                //.stream().sorted()
//                .forEach(System.out::println);
//    }
//
//    public List<Movie> getTopRatedN(int n) {
//        Comparator<Movie> ratingComparator = Comparator.comparing(Movie::getRating);
//
//        return movies.stream()
//                .sorted(ratingComparator)
//                .limit(n)
//                .collect(Collectors.toList());
//    }
//
////     return allFiles.stream()
////             .collect(Collectors.groupingBy(
////            File::toString,      //key
////                      TreeMap::new,       //empty value
////                      Collectors.summingLong(File::getSize)     //fill the value
////                ));
//
//    public Map<String, Integer> getCountByDirector() {
////        return movies.stream()
////                .collect(Collectors.groupingBy(
////                        Movie::getDirector, //key
////                        HashMap::new,       //init map
////                        Collectors.summingInt(m-> Integer.parseInt(m.director))
//                    ////Collectors.summingInt(Movie::getDirector)
////                ));
//
//        return movies.stream()
//                .collect(Collectors.toMap(
//                        Movie::getDirector,     //key
//                        movie -> 1,         //init value
//                        (movie1,movie2) -> {        //left and right
//                            movie1 += movie2;       //sum of movies by director
//                            return movie1;
//                        },
//                        TreeMap::new    //init map
//                ));
//    }
//}
//
//public class MoviesTest {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        int n = scanner.nextInt();
//        int printN = scanner.nextInt();
//        scanner.nextLine();
//        MoviesCollection moviesCollection = new MoviesCollection();
//        Set<String> genres = fillCollection(scanner, moviesCollection);
//        System.out.println("=== PRINT BY GENRE ===");
//        for (String genre : genres) {
//            System.out.println("GENRE: " + genre);
//            moviesCollection.printByGenre(genre);
//        }
//        System.out.println("=== TOP N BY RATING ===");
//        printList(moviesCollection.getTopRatedN(printN));
//
//        System.out.println("=== COUNT BY DIRECTOR ===");
//        printMap(moviesCollection.getCountByDirector());
//
//
//    }
//
//    static void printMap(Map<String,Integer> countByDirector) {
//        countByDirector.entrySet().stream()
//                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
//    }
//
//    static void printList(List<Movie> movies) {
//        for (Movie movie : movies) {
//            System.out.println(movie);
//        }
//    }
//
//    static TreeSet<String> fillCollection(Scanner scanner,
//                                          MoviesCollection collection) {
//        TreeSet<String> categories = new TreeSet<String>();
//        while (scanner.hasNext()) {
//            String line = scanner.nextLine();
//            String[] parts = line.split(":");
//            Movie movie = new Movie(parts[0], parts[1], parts[2], Float.parseFloat(parts[3]));
//            collection.addMovie(movie);
//            categories.add(parts[2]);
//        }
//        return categories;
//    }
//}
