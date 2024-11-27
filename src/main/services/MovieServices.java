package main.services;

import base.ListManager;
import constants.Constants;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.DAO.ActorDAO;
import main.DAO.GenreDAO;
import main.DAO.MovieDAO;
import main.models.Actor;
import main.models.Genre;
import main.models.Movie;
import static main.services.Services.getAS;
import static main.services.Services.getGS;
import static main.utils.DatabaseUtil.getConnection;
import main.utils.IDGenerator;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.Console.getDouble;
import static main.utils.Utility.Console.getInteger;
import static main.utils.Validator.getDate;

public class MovieServices extends ListManager<Movie> {

    public MovieServices() throws IOException {
        super(Movie.className());
        list = MovieDAO.getAllMovie();
    }

    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
                "Movie Management",
                null,
                new Menu.MenuOption[]{
                    new Menu.MenuOption("Add movie", () -> showSuccess(addMovie(Constants.DEFAULT_ADMIN_ID)), true),
                    new Menu.MenuOption("Delete movie", () -> showSuccess(deleteMovie()), true),
                    new Menu.MenuOption("Update movie", () -> showSuccess(updateMovie()), true),
                    new Menu.MenuOption("Search movie", () -> searchMovie(), true),
                    new Menu.MenuOption("Show all movie", () -> display(list, "List of Movie"), false),
                    new Menu.MenuOption("Back", () -> { /* Exit action */ }, false)
                },
                null
        );
    }

    public boolean addMovie(String userID) {
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "M");

        list.add(new Movie(
                id,
                getString("Enter title", false),
                getString("Enter description", false),
                0,
                selectGenres("Enter genre's ids (Comma-separated)", GenreDAO.getAllGenre()),
                selectActors("Enter actor's ids (Comma-separated)", ActorDAO.getAllActor()),
                getString("Enter language", false),
                getDate("Enter release date", false),
                getDouble("Enter rental price", 0, Double.MAX_VALUE, false),
                getInteger("Enter available copies", 0, Integer.MAX_VALUE, false)
        ));
        
        boolean movieSaved = MovieDAO.addMovieToDB(list.getLast());
        if (movieSaved) {
            MovieDAO.addMovieGenres(list.getLast().getId(), list.getLast().getGenreIds());  
            MovieDAO.addMovieActors(list.getLast().getId(), list.getLast().getActorIds()); 
            return true;
        }
        return false;
    }
    
    public boolean addMovie(Movie movie) {
        list.add(movie);
        boolean movieSaved = MovieDAO.addMovieToDB(list.getLast());
        if (movieSaved) {
            MovieDAO.addMovieGenres(list.getLast().getId(), list.getLast().getGenreIds());  
            MovieDAO.addMovieActors(list.getLast().getId(), list.getLast().getActorIds()); 
            return true;
        }
        return false;
    }

    private List<String> selectGenres(String message, List<Genre> options) {
        getGS().display(options, "");
        
        String input = getString(message, false); 
        String[] genreIDs = input.split(","); 
        for (String item : genreIDs) item = item.trim();
        for (String itemTest : genreIDs) System.out.println(itemTest);
        return List.of(genreIDs);  
    }

    private List<String> selectActors(String message, List<Actor> options) {
        getAS().display(options, "");
        
        String input = getString(message, false); 
        String[] actorIDs = input.split(","); 
        for (String item : actorIDs) item = item.trim();
        
        return List.of(actorIDs);  
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
                    || item.getTitle().equalsIgnoreCase(property)
                    || item.getDescription().contains(property)
                    || item.getLanguage().equalsIgnoreCase(property)
                    || item.getReleaseYear().toString().contains(property)
                    || String.valueOf(item.getRentalPrice()).contains(property)) {
                result.add(item);
            }
        }
        return result;
    }
    
    public static double calculateAverageRating(String movieID) throws SQLException {
        String query = "SELECT AVG(rating) AS average_rating FROM Review WHERE movie_id = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, movieID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("average_rating");
                }
            }
        }
        return 0; // dont have rating
    }
}
