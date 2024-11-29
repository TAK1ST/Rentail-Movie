
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
        String sql = "INSERT INTO Movies (movie_id, title, description, avg_rating, release_year, rental_price, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getId());
            preparedStatement.setString(2, movie.getTitle());
            preparedStatement.setString(3, movie.getDescription());
            preparedStatement.setDouble(4, movie.getAvgRating());
            preparedStatement.setDate(5, Date.valueOf(movie.getReleaseYear()));
            preparedStatement.setDouble(6, movie.getRentalPrice());
            preparedStatement.setInt(7, movie.getAvailableCopies());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addMovieGenres(String movieID, List<String> genreIDs) {
        String sql = "INSERT INTO Movie_Genre (movie_id, genre_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String genreID : genreIDs) {
                ps.setString(1, movieID);
                ps.setString(2, genreID);
                ps.addBatch();  // Thêm vào batch để giảm số lần truy vấn
            }
            ps.executeBatch();  // Thực thi tất cả các câu lệnh batch
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addMovieActors(String movieID, List<String> actorIDs) {
        String sql = "INSERT INTO Movie_Actor (movie_id, actor_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String actorID : actorIDs) {
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
    
    public static boolean addMovieLanguage(String movieID, List<String> languageCodes) {
        String sql = "INSERT INTO Movie_Genre (movie_id, language_code) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String languageCode : languageCodes) {
                ps.setString(1, movieID);
                ps.setString(2, languageCode);
                ps.addBatch();  // Thêm vào batch để giảm số lần truy vấn
            }
            ps.executeBatch();  // Thực thi tất cả các câu lệnh batch
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateMovieInDB(Movie movie) {
        String sql = "UPDATE Movies SET title = ?, description = ?, release_year = ?, rental_price = ?, available_copies = ? WHERE movie_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setDate(3, Date.valueOf(movie.getReleaseYear())); 
            preparedStatement.setDouble(4, movie.getRentalPrice());
            preparedStatement.setInt(5, movie.getAvailableCopies());
            preparedStatement.setString(6, movie.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteMovieFromDB(String movieID) {
        String sql1 = "DELETE FROM Movie_Actor WHERE movie_id = ?;";
        String sql2 = "DELETE FROM Movie_Genre WHERE movie_id = ?;";
        String sql3 = "DELETE FROM Movie_Language WHERE movie_id = ?;";
        String sql4 = "DELETE FROM Movie WHERE movie_id = ?;";

        try (Connection connection = Database.getConnection()) {
            try (PreparedStatement pstmt1 = connection.prepareStatement(sql1); 
                    PreparedStatement pstmt2 = connection.prepareStatement(sql2); 
                    PreparedStatement pstmt3 = connection.prepareStatement(sql3);
                    PreparedStatement pstmt4 = connection.prepareStatement(sql4);) {

                pstmt1.setString(1, movieID);
                pstmt2.setString(1, movieID);
                pstmt3.setString(1, movieID);
                pstmt4.setString(1, movieID);

                int rowsAffected1 = pstmt1.executeUpdate();
                int rowsAffected2 = pstmt2.executeUpdate();
                int rowsAffected3 = pstmt3.executeUpdate();
                int rowsAffected4 = pstmt4.executeUpdate();

                return (rowsAffected1 > 0) && (rowsAffected2 > 0) && (rowsAffected3 > 0) && (rowsAffected4 > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Movie> getAllMovies() {
        String sql = "SELECT * FROM Movies";
        List<Movie> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                // Lấy danh sách genres và actors từ bảng trung gian
                List<String> genres = getGenresByMovieId(resultSet.getString("movie_id"));
                List<String> actors = getActorsByMovieId(resultSet.getString("movie_id"));
                List<String> languages = getLaguagesByMovieId(resultSet.getString("movie_id"));

                Movie movie = new Movie(
                        resultSet.getString("movie_id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDouble("avg_rating"),
                        genres,
                        actors,
                        languages,
                        resultSet.getDate("release_year").toLocalDate(),
                        resultSet.getDouble("rental_price"),
                        resultSet.getInt("available_copies")
                );
                list.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<String> getGenresByMovieId(String movieId) {
        List<String> genres = new ArrayList<>();
        String sql = "SELECT genre_id FROM Movie_Genre WHERE movie_id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                genres.add(rs.getString("genre_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

    private static List<String> getActorsByMovieId(String movieId) {
        List<String> actors = new ArrayList<>();
        String sql = "SELECT actor_id FROM Movie_Actor WHERE movie_id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                actors.add(rs.getString("actor_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }
    
    private static List<String> getLaguagesByMovieId(String movieId) {
        List<String> actors = new ArrayList<>();
        String sql = "SELECT language_code FROM Movie_Language WHERE movie_id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                actors.add(rs.getString("language_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }

}
