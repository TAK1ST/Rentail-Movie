
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

    public static boolean addMovieGenres(String movieID, String genreIDs) {
        String sql = "INSERT INTO Movie_Genre (movie_id, genre_name) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String genreID : genreIDs.split(", ")) {
                ps.setString(1, movieID);
                ps.setString(2, genreID);
                ps.addBatch();  
            }
            ps.executeBatch();  
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addMovieActors(String movieID, String actorIDs) {
        String sql = "INSERT INTO Movie_Actor (movie_id, actor_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String actorID : actorIDs.split(", ")) {
                ps.setString(1, movieID);
                ps.setString(2, actorID);
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean addMovieLanguage(String movieID, String languageCodes) {
        String sql = "INSERT INTO Movie_Genre (movie_id, language_code) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String languageCode : languageCodes.split(", ")) {
                ps.setString(1, movieID);
                ps.setString(2, languageCode);
                ps.addBatch();  
            }
            ps.executeBatch();  
            return true;
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
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {

                Movie movie = new Movie(
                        resultSet.getString("movie_id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDouble("avg_rating"),
                        getGenresByMovieId(resultSet.getString("movie_id")),
                        getActorsByMovieId(resultSet.getString("movie_id")),
                        getLaguagesByMovieId(resultSet.getString("movie_id")),
                        resultSet.getDate("release_year").toLocalDate(),
                        resultSet.getDouble("rental_price"),
                        resultSet.getInt("available_copies"),
                        resultSet.getDate("created_at").toLocalDate(),
                        resultSet.getDate("updated_at").toLocalDate()
                );
                list.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getGenresByMovieId(String movieID) {
        String sql = "DELETE FROM Movies WHERE movie_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            ps.setString(1, movieID);
            return ps.executeUpdate() > 0 ? rs.getString("moive_id") : "";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getActorsByMovieId(String movieID) {
        String sql = "SELECT actor_id FROM Movie_Actor WHERE movie_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            ps.setString(1, movieID);
            return ps.executeUpdate() > 0 ? rs.getString("moive_id") : "";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    private static String getLaguagesByMovieId(String movieID) {
        String sql = "SELECT language_code FROM Movie_Language WHERE movie_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            ps.setString(1, movieID);
            return ps.executeUpdate() > 0 ? rs.getString("moive_id") : "";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

}
