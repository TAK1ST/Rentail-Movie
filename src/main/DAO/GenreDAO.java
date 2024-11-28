/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Genre;
import main.config.Database;

/**
 *
 * @author trann
 */
public class GenreDAO {
    
    public static boolean addGenreToDB(Genre genre) {
        String sql = "INSERT INTO Genre (genre_id, genre_name) VALUES (?, ?)";
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
    
    public static boolean updateGenreFromDB(Genre genre) {
        String sql = "UPDATE Genre SET genre_name = ? WHERE genre_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, genre.getGenreName());
            preparedStatement.setString(2, genre.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteGenreFromDB(String genre_id) {
        String sql = "DELETE FROM Genre WHERE genre_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, genre_id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Genre> getAllGenre() {
        String sql = "SELECT * FROM Genre";
        List<Genre> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Genre genre = new Genre(
                    resultSet.getString("genre_id"),
                    resultSet.getString("genre_name")
                );
                list.add(genre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
