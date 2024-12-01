package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Genre;
import main.config.Database;

public class GenreDAO {

    public static boolean addGenreToDB(Genre genre) {
        String sql = "INSERT INTO Genres (genre_name, description) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {


            int count = 0;
            ps.setString(++count, genre.getGenreName());
            ps.setString(++count, genre.getDescription());


            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return false;
    }

    public static boolean updateGenreInDB(Genre genre) {
        String sql = "UPDATE Genres SET description = ? WHERE genre_name = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, genre.getDescription());
            ps.setString(++count, genre.getGenreName());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return false;
    }


    public static boolean deleteGenreFromDB(String genreName) {
        String sql = "DELETE FROM Genres WHERE genre_name = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, genreName);
            return ps.executeUpdate() > 0;


        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return false;
    }

    public static List<Genre> getAllGenres() {
        String sql = "SELECT * FROM Genres";
        List<Genre> genres = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Genre genre = new Genre(
                        resultSet.getString("genre_name"),
                        resultSet.getString("description")
                );
                genres.add(genre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }
}
