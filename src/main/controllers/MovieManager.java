package main.controllers;

import base.Manager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.models.Movie;
import main.utils.DatabaseUtil;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import main.utils.Utility;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.Console.getDouble;
import static main.utils.Utility.Console.getInteger;
import static main.utils.Validator.getDate;

public class MovieManager extends Manager<Movie> {

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
                    new Menu.MenuOption("Back", () -> {
                        /* Exit action */ })
                },
                new Menu.MenuAction[]{() -> Menu.getSaveMessage(isNotSaved)},
                true
        );
    }

    public boolean addMovie(String userID) {
        String id = list.isEmpty() ? "M00001" : Utility.generateID(list.getLast().getId(), "M");

        String title = getString("Enter title: ", false);
        String description = getString("Enter description: ", false);
        double rating = getDouble("Enter rating (1-5): ", 1, 5, false);
        String language = getString("Enter language: ", false);
        LocalDate releaseYear = getDate("Enter release date: ", false);
        Double rentalPrice = getDouble("Enter rental price: ", 0, 1000, false);
        int availableCopies = getInteger("Enter available copies: ", 0, 100, false);

        Movie newMovie = new Movie(
                id,
                title,
                description,
                language,
                releaseYear,
                rentalPrice,
                availableCopies
        );

        list.add(newMovie);
        addMovieToDB(newMovie);
        return true;
    }

    public boolean updateMovie() {
        if (checkEmpty(list)) {
            return false;
        }

        Movie foundMovie = (Movie) getById("Enter movie's id to update: ");
        if (checkNull(foundMovie)) {
            return false;
        }

        String title = getString("Enter title: ", true);
        String description = getString("Enter description: ", true);
        double rating = getDouble("Enter rating (1-5): ", 1, 5, true);
        String language = getString("Enter language: ", true);
        LocalDate releaseYear = getDate("Enter release date: ", true);
        Double rentalPrice = getDouble("Enter rental price: ", 0, 1000, true);

        if (!title.isEmpty()) {
            foundMovie.setTitle(title);
        }
        if (!description.isEmpty()) {
            foundMovie.setDescription(description);
        }
        if (!language.isEmpty()) {
            foundMovie.setLanguage(language);
        }
        if (releaseYear != null) {
            foundMovie.setReleaseYear(releaseYear);
        }
        if (rentalPrice > 0) {
            foundMovie.setRentalPrice(rentalPrice);
        }
        updateMovieFromDB(foundMovie);
        return true;
    }

    public boolean deleteMovie() {
        if (checkEmpty(list)) {
            return false;
        }

        Movie foundMovie = (Movie) getById("Enter movie's id to delete: ");
        if (checkNull(foundMovie)) {
            return false;
        }

        list.remove(foundMovie);
        deleteMovieFromDB(foundMovie.getId());
        return true;
    }

    public void display(List<Movie> list, String title) {
        if (checkEmpty(list)) {
            return;
        }

        if (!title.isBlank()) {
            Menu.showTitle(title);
        }

        list.forEach(item -> System.out.println(item));
    }

    public void searchMovie() {
        if (checkEmpty(list)) {
            return;
        }

        display(getMovieBy("Enter movie's property to search: "), DISPLAY_TITLE);
    }

    public List<Movie> getMovieBy(String message) {
        return searchBy(getString(message, false));
    }

    @Override
    public List<Movie> searchBy(String property) {
        List<Movie> result = new ArrayList<>();
        for (Movie item : list) {
            if (item.getTitle().equalsIgnoreCase(property)
                    || item.getDescription().contains(property)
                    || item.getLanguage().equalsIgnoreCase(property)
                    || item.getReleaseYear().toString().contains(property)
                    || String.valueOf(item.getRentalPrice()).contains(property)) {
                result.add(item);
            }
        }
        return result;
    }

    public boolean addMovieToDB(Movie movie) {
        String sql = "INSERT INTO Movie (movieId, title, description, language, releaseYear, rentalPrice, availableCopies) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getId());
            preparedStatement.setString(2, movie.getTitle());
            preparedStatement.setString(3, movie.getDescription());
            preparedStatement.setString(4, movie.getLanguage());
            preparedStatement.setDate(5, Date.valueOf(movie.getReleaseYear())); // Sử dụng Date.valueOf() để chuyển LocalDate thành java.sql.Date
            preparedStatement.setDouble(6, movie.getRentalPrice());
            preparedStatement.setInt(7, movie.getAvailableCopies());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMovieFromDB(Movie movie) {
        String sql = "UPDATE Movie SET title = ?, description = ?, language = ?, releaseYear = ?, rentalPrice = ?, availableCopies = ? WHERE movieId = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setString(3, movie.getLanguage());
            preparedStatement.setDate(4, Date.valueOf(movie.getReleaseYear())); // Chuyển LocalDate thành java.sql.Date
            preparedStatement.setDouble(5, movie.getRentalPrice());
            preparedStatement.setInt(6, movie.getAvailableCopies());
            preparedStatement.setString(7, movie.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMovieFromDB(String movieID) {
        String sql = "DELETE FROM Movie WHERE movieId = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movieID);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getAllMovie() {
        String sql = "SELECT * FROM Movie";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Movie movie = new Movie(
                        resultSet.getString("movieId"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("language"),
                        resultSet.getDate("releaseYear").toLocalDate(),
                        resultSet.getDouble("rentalPrice"),
                        resultSet.getInt("availableCopies")
                );
                list.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
