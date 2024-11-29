
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.dao.ActorDAO;
import main.dao.GenreDAO;
import main.dao.MovieDAO;
import main.dto.Actor;
import main.dto.Genre;
import main.dto.Movie;
import static main.controllers.Managers.getAM;
import static main.controllers.Managers.getGM;
import main.dto.Language;
import main.utils.IDGenerator;
import static main.utils.Input.getDouble;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Utility.toInt;
import main.utils.Validator;
import static main.utils.Validator.getDate;

public class MovieManager extends ListManager<Movie> {

    public MovieManager() throws IOException {
        super(Movie.className());
        list = MovieDAO.getAllMovie();
    }

    public boolean addMovie(String userID) {
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "M");

        list.add(new Movie(
                id,
                getString("Enter title", false),
                getString("Enter description", false),
                0,
                selectGenres("Enter genres (Comma-separated)", GenreDAO.getAllGenre()),
                selectActors("Enter actors (Comma-separated)", ActorDAO.getAllActor()),
                getString("Enter language", false),
                getDate("Enter release date", false),
                getDouble("Enter rental price", 0, Double.MAX_VALUE, false),
                getInteger("Enter available copies", 0, Integer.MAX_VALUE, false)
        ));

        boolean isSuccess = MovieDAO.addMovieToDB(list.getLast());
        if (isSuccess) 
            return MovieDAO.addMovieGenres(list.getLast().getId(), list.getLast().getGenreIds()) &&
                    MovieDAO.addMovieActors(list.getLast().getId(), list.getLast().getActorIds());
        return false;
    }

    public boolean addMovie(Movie movie) {
        list.add(movie);
        boolean isSuccess = MovieDAO.addMovieToDB(list.getLast());
        if (isSuccess) 
            return MovieDAO.addMovieGenres(list.getLast().getId(), list.getLast().getGenreIds()) &&
                    MovieDAO.addMovieActors(list.getLast().getId(), list.getLast().getActorIds());
        return false;
    }

    private List<String> selectGenres(String message, List<Genre> options) {
        getGM().display(options, "");
        List<String> genreIDs = new ArrayList<>();

        String input = getString(message, false);
        String[] genreNames = input.split(",");

        for (String item : genreNames) {
            item = item.trim();
            int index = toInt(item);
            if (index > 0 && index <= options.size()) {
                genreIDs.add(options.get(index).getId());
            }
        }

        return genreIDs;
    }

    private List<String> selectActors(String message, List<Actor> options) {
        getAM().display(options, "");
        List<String> actorIDs = new ArrayList<>();

        String input = getString(message, false);
        String[] actorNames = input.split(",");

        for (String item : actorNames) {
            item = item.trim();
            int index = toInt(item);
            if (index > 0 && index <= options.size()) {
                actorIDs.add(options.get(index).getId());
            }
        }

        return actorIDs;
    }
    
    private List<String> selectLanguages(String message, List<Language> options) {
        getAM().display(options, "");
        List<String> actorIDs = new ArrayList<>();

        String input = getString(message, false);
        String[] actorNames = input.split(",");

        for (String item : actorNames) {
            item = item.trim();
            int index = toInt(item);
            if (index > 0 && index <= options.size()) {
                actorIDs.add(options.get(index).getId());
            }
        }

        return actorIDs;
    }

    public boolean updateMovie() {
        if (checkEmpty(list)) return false;    

        Movie foundMovie = (Movie) getById("Enter movie's id");
        if (checkNull(foundMovie)) return false;

        String title = getString("Enter title", true);
        String description = getString("Enter description", true);
        String language = getString("Enter language", true);
        LocalDate releaseYear = getDate("Enter release date", true);
        Double rentalPrice = getDouble("Enter rental price", 0, Double.MAX_VALUE, true);

        if (!title.isEmpty()) 
            foundMovie.setTitle(title);

        if (!description.isEmpty()) 
            foundMovie.setDescription(description);

        if (!language.isEmpty()) 
            foundMovie.setLanguage(language);

        if (releaseYear != null) 
            foundMovie.setReleaseYear(releaseYear);

        if (rentalPrice > 0) 
            foundMovie.setRentalPrice(rentalPrice);

        MovieDAO.updateMovieFromDB(foundMovie);
        return true;
    }

    public boolean deleteMovie() {
        if (checkEmpty(list)) return false;

        Movie foundMovie = (Movie) getById("Enter movie's id");
        if (checkNull(foundMovie)) return false;

        list.remove(foundMovie);
        MovieDAO.deleteMovieFromDB(foundMovie.getId());
        return true;
    }

    public void searchMovie() {
        display(getMovieBy("Enter movie's property"), "Search Results");
    }

    public List<Movie> getMovieBy(String message) {
        return searchBy(getString(message, false));
    }

    @Override
    public List<Movie> searchBy(String property) {
        List<Movie> result = new ArrayList<>();
        for (Movie item : list) {
            if (item.getId().equals(property)
                    || item.getTitle().contains(property.trim().toLowerCase())
                    || item.getDescription().contains(property.trim().toLowerCase())
                    || item.getLanguage().contains(property.trim().toLowerCase())
                    || item.getReleaseYear().format(Validator.DATE).contains(property)
                    || String.valueOf(item.getRentalPrice()).contains(property)) {
                result.add(item);
            }
        }
        return result;
    }
    
    @Override
    public void display(List<Movie> movies, String title) {
        if (checkEmpty(list)) return;
        
        System.out.println(title);
        System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        System.out.printf("|%-10s | %-30s | %-30s | %-10s | %-15s | %-20s | %-10s | %-10s |\n",
                "Movie ID", "Title", "Description", "Avg Rating", "Genres", "Actors", "Language", "Release Year", "Available Copies");
        System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------------------|");

        for (Movie movie : movies) {
            String genres = String.join(", ", movie.getGenreIds());
            String actors = String.join(", ", movie.getActorIds());
            System.out.printf("|%-10s | %-30s | %-30s | %-10s | %-15s | %-20s | %-10s | %-10s | %-10s |\n",
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDescription().isEmpty() ? "N/A" : movie.getDescription() ,
                    movie.getAVGRating(),
                    genres.isEmpty() ? "N/A" : genres,
                    actors.isEmpty() ? "N/A" : actors,
                    movie.getLanguage(),
                    movie.getReleaseYear(),
                    movie.getAvailable_copies());
        }

        System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------------------|");
    }

}
