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
import static main.utils.LogMessage.errorLog;

/**
 * Data Access Object for Reviews
 * Provides methods to interact with the Reviews table in the database.
 */
public class ReviewDAO {

    public static boolean addReviewToDB(Review review) {
        String sql = "INSERT INTO Reviews ("
                + "movie_id, "
                + "customer_id, "
                + "review_text, "
                + "rating, "
                + "review_date"
                + ") VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, review.getMovieID());
            ps.setString(++count, review.getCustomerID());
            ps.setString(++count, review.getReviewText());
            ps.setInt(++count, review.getRating());
            ps.setDate(++count, review.getReviewDate() != null ? Date.valueOf(review.getReviewDate()) : null);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateReviewInDB(Review review) {
        String sql = "UPDATE Reviews SET "
                + "rating = ?, "
                + "review_date = ?, "
                + "review_text = ? "
                + "WHERE "
                + "customer_id = ? AND "
                + "movie_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setInt(++count, review.getRating());
            ps.setDate(++count, review.getReviewDate() != null ? Date.valueOf(review.getReviewDate()) : null);
            ps.setString(++count, review.getReviewText()); 
            ps.setString(++count, review.getCustomerID());
            ps.setString(++count, review.getMovieID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        errorLog("cann not");
        return false;
    }

    public static boolean deleteReviewFromDB(String customerID, String movieID) {
        String sql = "DELETE FROM Reviews WHERE customer_id = ? AND movie_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, customerID);
            ps.setString(2, movieID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteUserReviewFromDB(String customerID) {
        String sql = "DELETE FROM Reviews WHERE customer_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, customerID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Review> getAllReviews() {
        String sql = "SELECT * FROM Reviews";
        List<Review> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Review review = new Review(
                        rs.getString("customer_id"),
                        rs.getString("movie_id"),
                        rs.getInt("rating"),
                        rs.getString("review_text"),
                        rs.getDate("review_date") != null ? rs.getDate("review_date").toLocalDate() : null
                );
                list.add(review);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<Review> getUserReviews(String customerId) {
        String query = "SELECT movie_id, rating, review_text, review_date FROM Reviews WHERE customer_id = ?";

        List<Review> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Review review = new Review(
                        customerId,
                        rs.getString("movie_id"),
                        rs.getInt("rating"),
                        rs.getString("review_text"),
                        rs.getDate("review_date") != null ? rs.getDate("review_date").toLocalDate() : null
                );
                list.add(review);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
