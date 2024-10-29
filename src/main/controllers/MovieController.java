
package main.controllers;

import java.util.Collections;
import java.util.List;
import main.models.Movie;
import main.services.MovieService;

public class MovieController {
      private final MovieService movieService = new MovieService();

    public boolean addMovie(Movie movie) {
        if (movie == null) {
            System.out.println("Movie object is null. Please provide valid movie details.");
            return false;
        }
        try {
            return movieService.addMovie(movie);
        } catch (Exception e) {
            System.out.println("Error adding movie: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMovie(Movie movie) {
        if (movie == null || movie.getMovieId() <= 0) {
            System.out.println("Invalid movie details. Please provide a valid movie object with an ID.");
            return false;
        }
        try {
            return movieService.updateMovie(movie);
        } catch (Exception e) {
            System.out.println("Error updating movie: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMovie(int movieId) {
        if (movieId <= 0) {
            System.out.println("Invalid movie ID. Please provide a valid movie ID greater than 0.");
            return false;
        }
        try {
            return movieService.deleteMovie(movieId);
        } catch (Exception e) {
            System.out.println("Error deleting movie: " + e.getMessage());
            return false;
        }
    }

    public List<Movie> getAllMovies() {
        try {
            List<Movie> movies = movieService.getAllMovies();
            return movies != null ? movies : Collections.emptyList();
        } catch (Exception e) {
            System.out.println("Error retrieving movies: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Movie> searchMovies(String genreName, String releaseYear, String language, String rating) {
        try {
            return movieService.searchMovies(genreName, releaseYear, language, rating);
        } catch (Exception e) {
            System.out.println("Error searching movies: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
