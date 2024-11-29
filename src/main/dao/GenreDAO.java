/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Genre;
import main.config.Database;

/**
 *
 * @author trann
 */
public class GenreDAO {
    
    public static boolean addGenreToDB(Genre genre) {
        String sql = "INSERT INTO Genres (genre_name, description) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, genre.getId());
            preparedStatement.setString(2, genre.getGenreName());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateGenreInDB(Genre genre) {
        String sql = "UPDATE Genres SET description = ? WHERE genre_name = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, genre.getDescription());
            preparedStatement.setString(2, genre.getGenreName());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteGenreFromDB(String genre_id) {
        String sql = "DELETE FROM Genres WHERE genre_name = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, genre_id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Genre> getAllGenres() {
        String sql = "SELECT * FROM Genres";
        List<Genre> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Genre genre = new Genre(
                    resultSet.getString("genre_name"),
                    resultSet.getString("description")
                );
                list.add(genre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
