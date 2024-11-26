/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import base.Manager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Movie;
import main.utils.DatabaseUtil;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import main.utils.Utility;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.Console.getDouble;
import static main.utils.Validator.getDate;

/**
 *
 * @author trann
 */
public class MovieManager extends Manager<Movie>{
    private static final String DISPLAY_TITLE = "List of Movie:";
    
    public MovieManager() throws IOException {
        super(Movie.className());
        getAllMovie();
    }
    
    public void managerMenu() throws IOException {  
        Menu.showManagerMenu(
            "Movie Management",
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Add movie", () -> showSuccess(addMovie("U00000"))),
                new Menu.MenuOption("Delete movie", () -> showSuccess(deleteMovie())),
                new Menu.MenuOption("Update movie", () -> showSuccess(updateMovie())),
                new Menu.MenuOption("Search movie", () -> searchMovie()),
                new Menu.MenuOption("Show all movie", () -> display(list, DISPLAY_TITLE)),
                new Menu.MenuOption("Back", () -> { /* Exit action */ })
            },
            new Menu.MenuAction[] { () -> Menu.getSaveMessage(isNotSaved) },
            true
        );
    }

    public boolean addMovie(String userID) {
        String id = list.isEmpty() ? "M00001" : Utility.generateID(list.getLast().getId(), "M");
        
        String title = getString("Enter title: ", false);
        String description = getString("Enter description: ", false);
        double rating = getDouble("Enter rating (1-5): ", 1, 5, false);
        String language = getString("Enter language: ", false);
        String releaseYear = getDate("Enter release date: ", false).toString();
        Double rentalPrice = getDouble("Enter retal price: ", 0, 1000, false);
       
        String genreID = "";
        
        list.add(new Movie(
            id,
            title, 
            description, 
            rating, 
            genreID, 
            language, 
            releaseYear, 
            rentalPrice));
        addMovieToDB(list.getLast());
        return true;
    }

    public boolean updateMovie() {
        if (checkEmpty(list)) return false;

        Movie foundMovie = (Movie)getById("Enter user's id to update: ");
        if (checkNull(foundMovie)) return false;
        
        String title = getString("Enter title: ", true);
        String description = getString("Enter description: ", true);
        double rating = getDouble("Enter rating (1-5): ", 1, 5, true);
        String language = getString("Enter language: ", true);
        String releaseYear = getDate("Enter release date: ", true).toString();
        Double rentalPrice = getDouble("Enter retal price: ", 0, 1000, true);
        
        if (!title.isEmpty()) foundMovie.setTitle(title);
        if (!description.isEmpty()) foundMovie.setDescription(description);
        if (rating > 0) foundMovie.setRating(rating);
        if (!language.isEmpty()) foundMovie.setLanguage(language);
        if (!releaseYear.isEmpty()) foundMovie.setReleaseYear(releaseYear);
        if (rentalPrice > 0) foundMovie.setRentalPrice(rentalPrice);
        
        updateMovieFromDB(foundMovie);
        return true;
    }

    public boolean deleteMovie() { 
        if (checkEmpty(list)) return false;       

        Movie foundMovie = (Movie)getById("Enter user's id to update: ");
        if (checkNull(foundMovie)) return false;

        list.remove(foundMovie);
        deleteMovieFromDB(foundMovie.getId());
        return true;
    }

    public void display(List<Movie> list, String title) {
        if (checkEmpty(list)) return;
        
        if (!title.isBlank()) Menu.showTitle(title);

        list.forEach(item -> System.out.println(item));
    }

    public void searchMovie() {
        if (checkEmpty(list)) return;

        display(getMovieBy("Enter movie's propety to search: "), DISPLAY_TITLE);
    }

    public List<Movie> getMovieBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<Movie> searchBy(String propety) {
        List<Movie> result = new ArrayList<>();
        for (Movie item : list) 
            if (item.getGenre().equals(propety)
                    || item.getDescription().equals(propety)
                    || item.getReleaseYear().equals(propety)
                    || item.getLanguage().equals(propety)
                    || String.valueOf(item.getRating()).equals(propety)
                    || String.valueOf(item.getRentalPrice()).equals(propety)
                    || item.getTitle().equalsIgnoreCase(propety)
            ) result.add(item);
        return result;
    }
    
    public boolean addMovieToDB(Movie movie) {
        String sql = "INSERT INTO Movie (movieId, title, description, rating, genreID, language, releaseYear, rentalPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movie.getId());
            preparedStatement.setString(2, movie.getTitle());
            preparedStatement.setString(3, movie.getDescription());
            preparedStatement.setDouble(4, movie.getRating());
            preparedStatement.setString(5, movie.getGenre());
            preparedStatement.setString(6, movie.getLanguage());
            preparedStatement.setString(7, movie.getReleaseYear());
            preparedStatement.setDouble(8, movie.getRentalPrice());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateMovieFromDB(Movie movie) {
        String sql = "UPDATE Movie SET title = ?, description = ?, rating = ?, genreId = ?, language = ?, releaseYear = ?, rentalPrice = ? WHERE movieId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setDouble(3, movie.getRating());
            preparedStatement.setString(4, movie.getGenre());
            preparedStatement.setString(5, movie.getLanguage());
            preparedStatement.setString(6, movie.getReleaseYear());
            preparedStatement.setDouble(7, movie.getRentalPrice());
            preparedStatement.setString(8, movie.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteMovieFromDB(String movieID) {
        String sql = "DELETE FROM Movie WHERE movieId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movieID);
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void getAllMovie() {
        String sql = "SELECT * FROM Movie";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Movie movie = new Movie(
                    resultSet.getString("movieId"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getDouble("rating"),
                    resultSet.getString("genreId"),
                    resultSet.getString("language"),
                    resultSet.getString("releaseYear"),
                    resultSet.getDouble("rentalYear")
                );
                list.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
