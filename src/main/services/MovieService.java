package main.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.Main;
import main.models.Movie;
import main.models.Genre;
public class MovieService {
    public boolean addMovie(Movie movie) {
        String sql = "INSERT INTO Movie (title, description, rating, genre, language, release_year, rental_price, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = Main.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setString(3, movie.getRating());
            preparedStatement.setString(4, movie.getGenre().getGenreName());
            preparedStatement.setString(5, movie.getLanguage());
            preparedStatement.setString(6, movie.getReleaseYear());
            preparedStatement.setDouble(7, movie.getRentalPrice());
            preparedStatement.setInt(7, movie.getAvailableCopies());
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
     public boolean updateMovie(Movie movie) {
        String sql = "UPDATE Movie SET title = ?, description = ?, rating = ?, genre = ?, language = ?, release_year = ?, rental_price = ?, available_copies = ? WHERE movie_id = ?";
        try (Connection connection = Main.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setString(3, movie.getGenre().getGenreName());
            preparedStatement.setString(4, movie.getLanguage());
            preparedStatement.setString(5, movie.getReleaseYear());
            preparedStatement.setDouble(6, movie.getRentalPrice());
            preparedStatement.setInt(7, movie.getAvailableCopies());
            preparedStatement.setInt(8, movie.getMovieId());

             int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMovie(int movieId) {
        String sql = "DELETE FROM Movie WHERE movie_id = ?";
        try (Connection connection = Main.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, movieId);

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    
}
     public List<Movie> getAllMovies() {
        String sql = "SELECT * FROM Movie";
        List<Movie> movies = new ArrayList<>();
        
        try (Connection connection = Main.connect();
             Statement statement = connection.createStatement()) {
             
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Movie movie = new Movie(
                    resultSet.getInt("movie_id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("rating"),
                    getGenreByName(resultSet.getString("genre_name")),
                    resultSet.getString("language"),
                    resultSet.getString("release_year"),
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
     public List<Movie> searchMovies(String genreName, String releaseYear, String language, String rating) {
        String sql = "SELECT * FROM Movie WHERE 1=1";  
        List<Movie> movies = new ArrayList<>();

        if (genreName != null && !genreName.isEmpty()) {
            sql += " AND genre_name = ?";
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

        try (Connection connection = Main.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int parameterIndex = 1;

            if (genreName != null && !genreName.isEmpty()) {
                preparedStatement.setString(parameterIndex++, genreName);
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
                    resultSet.getInt("movie_id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("rating"),
                    getGenreByName(resultSet.getString("genre_name")),
                    resultSet.getString("language"),
                    resultSet.getString("release_year"),
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
     private Genre getGenreByName(String genreName) {
        return new Genre(1, genreName); 
    }
}
    
