/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.CRUD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Movie;
import main.utils.DatabaseUtil;

/**
 *
 * @author trann
 */
public class MovieCRUD {
    
    public static boolean addMovieToDB(Movie movie) {
        String sql = "INSERT INTO Movie (movieId, title, description, language, releaseYear, rentalPrice, availableCopies) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getId());
            preparedStatement.setString(2, movie.getTitle());
            preparedStatement.setString(3, movie.getDescription());
            preparedStatement.setString(4, movie.getLanguage());
            preparedStatement.setDate(5, movie.getReleaseYear()); // Sử dụng Date.valueOf() để chuyển LocalDate thành java.sql.Date
            preparedStatement.setDouble(6, movie.getRentalPrice());
            preparedStatement.setInt(7, movie.getAvailable_copies());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateMovieFromDB(Movie movie) {
        String sql = "UPDATE Movie SET title = ?, description = ?, language = ?, releaseYear = ?, rentalPrice = ?, availableCopies = ? WHERE movieId = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setString(3, movie.getLanguage());
            preparedStatement.setDate(4, movie.getReleaseYear()); // Chuyển LocalDate thành java.sql.Date
            preparedStatement.setDouble(5, movie.getRentalPrice());
            preparedStatement.setInt(6, movie.getAvailable_copies());
            preparedStatement.setString(7, movie.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteMovieFromDB(String movieID) {
        String sql = "DELETE FROM Movie WHERE movieId = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movieID);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Movie> getAllMovie() {
        String sql = "SELECT * FROM Movie";
        List<Movie> list = new ArrayList<>();
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
        return list;
    }
}
