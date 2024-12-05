
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Movie;
import main.config.Database;
import static main.dao.MiddleTableDAO.getSubIdsByMainId;

/**
 *
 * @author trann
 */
public class MovieDAO {

    public static boolean addMovieToDB(Movie movie) {
        String sql = "INSERT INTO Movies ("
                + "movie_id, "
                + "title, "
                + "description, "
                + "avg_rating, "
                + "release_year, "
                + "rental_price, "
                + "available_copies, "
                + "created_at, "
                + "updated_at "
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, movie.getId());
            ps.setString(++count, movie.getTitle());
            ps.setString(++count, movie.getDescription());
            ps.setDouble(++count, movie.getAvgRating());
            ps.setDate(++count, Date.valueOf(movie.getReleaseYear()));
            ps.setDouble(++count, movie.getRentalPrice());
            ps.setInt(++count, movie.getAvailableCopies());
            ps.setDate(++count, Date.valueOf(movie.getCreateDate()));
            ps.setDate(++count, Date.valueOf(movie.getUpdateDate()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateMovieInDB(Movie movie) {
        String sql = "UPDATE Movies SET "
                + "title = ?, "
                + "description = ?, "
                + "release_year = ?, "
                + "rental_price = ?, "
                + "available_copies = ?,"
                + "created_at = ?,"
                + "updated_at = ? "
                + "WHERE movie_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, movie.getTitle());
            ps.setString(++count, movie.getDescription());
            ps.setDate(++count, Date.valueOf(movie.getReleaseYear()));
            ps.setDouble(++count, movie.getRentalPrice());
            ps.setInt(++count, movie.getAvailableCopies());
            ps.setDate(++count, Date.valueOf(movie.getCreateDate()));
            ps.setDate(++count, Date.valueOf(movie.getUpdateDate()));
            ps.setString(++count, movie.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteMovieFromDB(String movieID) {
        String sql = "DELETE FROM Movies WHERE movie_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, movieID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Movie> getAllMovies() {
        String sql = "SELECT * FROM Movies";
        List<Movie> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                Movie movie = new Movie(
                        rs.getString("movie_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("avg_rating"),
                        getSubIdsByMainId("Movie_Genre", rs.getString("movie_id"), "movie_id", "genre_name"),
                        getSubIdsByMainId("Movie_Actor", rs.getString("movie_id"), "movie_id", "actor_id"),
                        getSubIdsByMainId("Movie_Language", rs.getString("movie_id"), "movie_id", "language_code"),
                        rs.getDate("release_year").toLocalDate(),
                        rs.getDouble("rental_price"),
                        rs.getInt("available_copies"),
                        rs.getDate("created_at").toLocalDate(),
                        rs.getDate("updated_at").toLocalDate()
                );
                list.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
