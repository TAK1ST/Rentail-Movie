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
import main.dto.Review;
import main.config.Database;

/**
 *
 * @author trann
 */
public class ReviewDAO {
    
    public static boolean addReviewToDB(Review review) {
        String sql = "INSERT INTO Reviews (review_id, movie_id, customer_id, review_text, rating, review_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, review.getId());
            preparedStatement.setString(2, review.getMovieID());
            preparedStatement.setString(3, review.getCustomerID());
            preparedStatement.setString(4, review.getReviewText());
            preparedStatement.setInt(5, review.getRating());
            preparedStatement.setDate(6, Date.valueOf(review.getReviewDate()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateReviewInDB(Review review) {
        String sql = "UPDATE Reviews SET customer_id = ?, movie_id = ?, rating = ?, review_date = ?, review_text = ? WHERE review_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, review.getCustomerID());
            preparedStatement.setString(2, review.getMovieID());
            preparedStatement.setInt(3, review.getRating());
            preparedStatement.setDate(4, Date.valueOf(review.getReviewDate()));
            preparedStatement.setString(5, review.getReviewText());
            preparedStatement.setString(6, review.getId());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteReviewFromDB(String reviewID) {
        String sql = "DELETE FROM Reviews WHERE review_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, reviewID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Review> getAllReviews() {
        String sql = "SELECT * FROM Reviews";
        List<Review> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Review review = new Review(
                    resultSet.getString("review_id"),
                    resultSet.getString("customer_id"),
                    resultSet.getString("movie_id"),
                    resultSet.getInt("rating"),
                    resultSet.getString("review_text"),
                    resultSet.getDate("review_date").toLocalDate()
                );
                list.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
