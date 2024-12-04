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
 * Data Access Object for Reviews
 * Provides methods to interact with the Reviews table in the database.
 */
public class ReviewDAO {

    public static boolean addReviewToDB(Review review) {
        String sql = "INSERT INTO Reviews ("
                + "review_id, "
                + "movie_id, "
                + "customer_id, "
                + "review_text, "
                + "rating, "
                + "review_date"
                + ") VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, review.getId());
            ps.setString(++count, review.getMovieID());
            ps.setString(++count, review.getCustomerID());
            ps.setString(++count, review.getReviewText());
            ps.setInt(++count, review.getRating());
            ps.setDate(++count, Date.valueOf(review.getReviewDate()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateReviewInDB(Review review) {
        String sql = "UPDATE Reviews SET "
                + "customer_id = ?, "
                + "movie_id = ?, "
                + "rating = ?, "
                + "review_date = ?, "
                + "review_text = ? "
                + "WHERE review_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, review.getCustomerID());
            ps.setString(++count, review.getMovieID());
            ps.setInt(++count, review.getRating());
            ps.setDate(++count, Date.valueOf(review.getReviewDate()));
            ps.setString(++count, review.getReviewText());
            ps.setString(++count, review.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteReviewFromDB(String reviewID) {
        String sql = "DELETE FROM Reviews WHERE review_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, reviewID);
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
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Review review = new Review(
                        resultSet.getString("review_id"),
                        resultSet.getString("movie_id"),
                        resultSet.getString("customer_id"),
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
