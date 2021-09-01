package mk.ukim.finki.code;

import java.util.*;
import java.util.stream.Collectors;

class Movie{
    String title;
    List<Integer> ratings;

    public Movie(String title, List<Integer> ratings) {
        this.title = title;
        this.ratings = new ArrayList<>();
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }
    double ratingCoef(){
        return avgRating() * ratings.size();
    }

    double avgRating(){
        return ratings.stream()
                .mapToDouble(v->v.doubleValue())
                .average().orElse(0.0);
    }
    //The Outlaw Josey Wales (1976) (7.33) of 12 ratings

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",title,avgRating(),ratings.size());
    }
}
class MoviesList{
    List<Movie> movies;

    public MoviesList(){
        this.movies = new ArrayList<>();
    }
    public void addMovie(String title,int[] ratings){
        List<Integer> resultRating = Arrays.stream(ratings)
                .boxed().collect(Collectors.toList());
        movies.add(new Movie(title,resultRating));
    }
    public List<Movie> top10ByAvgRating(){
        Comparator<Movie> avgRatingComparator = Comparator.comparing(Movie::avgRating).reversed()
                .thenComparing(Movie::getTitle).reversed();
        return movies.stream()
                .sorted(avgRatingComparator.reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
    int ratingCoef(){
        return movies.stream()
                .mapToInt(m->m.ratings.size())
                .max().orElse(0);
    }
    public List<Movie> top10ByRatingCoef(){
        Comparator<Movie> comparator = (movie1,movie2)->{
            int res = Double.compare(movie1.ratingCoef()/ratingCoef(),movie2.ratingCoef()/ratingCoef());
            if (res==0)
                return movie2.title.compareTo(movie1.title);
            return res;
        };
        return movies.stream()
                .sorted(comparator.reversed())
                .limit(10).collect(Collectors.toList());
    }
}

public class MoviesTestCode {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde