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
import main.models.Review;
import main.utils.DatabaseUtil;

/**
 *
 * @author trann
 */
public class ReviewDAO {
    
    public static boolean addReviewToDB(Review review) {
        String sql = "INSERT INTO Review (reviewId, userId, movieId, rating, reviewDate, reviewText) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, review.getId());
            preparedStatement.setString(2, review.getUserID());
            preparedStatement.setString(3, review.getMovieID());
            preparedStatement.setDouble(4, review.getRating());
            preparedStatement.setString(5, review.getReviewDate());
            preparedStatement.setString(6, review.getReviewText());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateReviewFromDB(Review review) {
        String sql = "UPDATE Review SET userId = ?, movieId = ?, rating = ?, reviewDate = ?, reviewText = ? WHERE reviewId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, review.getUserID());
            preparedStatement.setString(2, review.getMovieID());
            preparedStatement.setDouble(3, review.getRating());
            preparedStatement.setString(4, review.getReviewDate());
            preparedStatement.setString(5, review.getReviewText());
            preparedStatement.setString(6, review.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteReviewFromDB(String reviewID) {
        String sql = "DELETE FROM Review WHERE reviewId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, reviewID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Review> getAllReview() {
        String sql = "SELECT * FROM Review";
        List<Review> list = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Review review = new Review(
                    resultSet.getString("reviewId"),
                    resultSet.getString("userID"),
                    resultSet.getString("movieId"),
                    resultSet.getDouble("rating"),
                    resultSet.getString("reviewDate"),
                    resultSet.getString("reviewText")
                );
                list.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
