package main.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate; // Import LocalDate để xử lý ngày tháng
import main.models.Movie;
import main.utils.DatabaseUtil;

public class MovieService {

    // Add a new movie to the database
public boolean addMovie(Movie movie) {
    String sql = "INSERT INTO Movie (movie_id, title, description, language, release_year, rental_price, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection connection = DatabaseUtil.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        preparedStatement.setString(1, movie.getId()); // movie_id is CHAR(8)
        preparedStatement.setString(2, movie.getTitle());
        preparedStatement.setString(3, movie.getDescription());
        preparedStatement.setString(4, movie.getLanguage());
        preparedStatement.setDate(5, Date.valueOf(movie.getReleaseYear())); 
        preparedStatement.setDouble(6, movie.getRentalPrice()); 
        preparedStatement.setInt(7, movie.getAvailableCopies());

        int rowsInserted = preparedStatement.executeUpdate();
        return rowsInserted > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    // Update movie details in the database
    public boolean updateMovie(Movie movie) {
        String sql = "UPDATE Movie SET title = ?, description = ?, rating = ?, language = ?, release_year = ?, rental_price = ?, available_copies = ? WHERE movie_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setDouble(3, movie.getRentalPrice()); 
            preparedStatement.setString(4, movie.getLanguage());
            preparedStatement.setDate(5, Date.valueOf(movie.getReleaseYear())); 
            preparedStatement.setDouble(6, movie.getRentalPrice());
            preparedStatement.setInt(7, movie.getAvailableCopies());
            preparedStatement.setString(8, movie.getId());  

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a movie from the database by its movie_id
    public boolean deleteMovie(String movieId) {
        String sql = "DELETE FROM Movie WHERE movie_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movieId);

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all movies from the database
    public List<Movie> getAllMovies() {
        String sql = "SELECT * FROM Movie";
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Movie movie = new Movie(
                    resultSet.getString("movie_id"),  
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("language"),
                    resultSet.getDate("release_year").toLocalDate(), // Convert SQL Date to LocalDate
                    resultSet.getDouble("rental_price"),
                    resultSet.getInt("available_copies")
                );
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    // Search for movies based on criteria
    public List<Movie> searchMovies(String genreId, String releaseYear, String language, String rating) {
        String sql = "SELECT * FROM Movie WHERE 1=1";  
        List<Movie> movies = new ArrayList<>();

        if (genreId != null && !genreId.isEmpty()) {
            sql += " AND genre_id = ?";
        }
        if (releaseYear != null && !releaseYear.isEmpty()) {
            sql += " AND release_year = ?";
        }
        if (language != null && !language.isEmpty()) {
            sql += " AND language = ?";
        }
        if (rating != null && !rating.isEmpty()) {
            sql += " AND rating = ?";
        }

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int parameterIndex = 1;

            if (genreId != null && !genreId.isEmpty()) {
                preparedStatement.setString(parameterIndex++, genreId);
            }
            if (releaseYear != null && !releaseYear.isEmpty()) {
                preparedStatement.setString(parameterIndex++, releaseYear);
            }
            if (language != null && !language.isEmpty()) {
                preparedStatement.setString(parameterIndex++, language);
            }
            if (rating != null && !rating.isEmpty()) {
                preparedStatement.setString(parameterIndex++, rating);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Movie movie = new Movie(
                    resultSet.getString("movie_id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("language"),
                    resultSet.getDate("release_year").toLocalDate(),
                    resultSet.getDouble("rental_price"),
                    resultSet.getInt("available_copies")
                );
                movies.add(movie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
