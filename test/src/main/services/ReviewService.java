package main.services;

import main.models.Genre;
import main.models.Movie;
import main.models.Review;
import main.models.Users;
import main.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewService {
    public boolean addReview(Review Review) {
        String sql = "INSERT INTO Review (title, description, rating, genre, language, release_year, rental_price, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Review.getId());
            preparedStatement.setObject(2, Review.getMovie_id());
            preparedStatement.setObject(3, Review.getUsers_id());
            preparedStatement.setString(4, Review.getReview_text());
            preparedStatement.setString(5, Review.getReview_date());
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReview(Review Review) {
        String sql = "UPDATE Review SET title = ?, description = ?, rating = ?, genre = ?, language = ?, release_year = ?, rental_price = ?, available_copies = ? WHERE Review_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, Review.getId());
            preparedStatement.setObject(2, Review.getMovie_id());
            preparedStatement.setObject(3, Review.getUsers_id());
            preparedStatement.setObject(4, Review.getUsers_id());
            preparedStatement.setInt(5, Review.getRating());
            preparedStatement.setString(6, Review.getReview_date());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteReview(int ReviewId) {
        String sql = "DELETE FROM Review WHERE Review_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, ReviewId);

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public List<Review> getAllReviews() {
        String sql = "SELECT * FROM Review";
        List<Review> Reviews = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery(sql);

            while (resultSet.next()) {
                Review Review = new Review(
                        resultSet.getInt("Review_id"),
                        (Movie) resultSet.getObject("Movie_id"),
                        (Users) resultSet.getObject("Users_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("Review_text"),
                        resultSet.getString("Review_date")
                );
                Reviews.add(Review);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Reviews;
    }

    public List<Review> searchReviews(int id, Movie movie_id, String review_date, String review_text, Users users_id) {
        String sql = "SELECT * FROM Review WHERE 1=1";
        List<Review> Reviews = new ArrayList<>();

        if (id >= 1) {
            sql += " AND id = ?";
        }
        if (movie_id != null) {
            sql += " AND movie_id = ?";
        }
        if (users_id != null) {
            sql += " AND user_id = ?";
        }
        if (review_text != null && !review_text.isEmpty()) {
            sql += " AND review_text = ?";
        }

        if (review_date != null && !review_date.isEmpty()) {
            sql += " AND review_date = ?";
        }
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int parameterIndex = 1;

            if (id >= 0) {
                preparedStatement.setInt(parameterIndex++, id);
            }
            if (movie_id != null) {
                preparedStatement.setObject(parameterIndex++, movie_id);
            }
            if (users_id != null) {
                preparedStatement.setObject(parameterIndex++,users_id );
            }
            if (review_text != null && !review_text.isEmpty()) {
                preparedStatement.setString(parameterIndex++, review_text);
            }

            if (review_date != null && !review_date.isEmpty()) {
                preparedStatement.setString(parameterIndex++, review_date);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Review Review = new Review(
                        resultSet.getInt("Review_id"),
                        (Movie) resultSet.getObject("Movie_id"),
                        (Users) resultSet.getObject("Users_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("Review_text"),
                        resultSet.getString("Review_date")
                );
                Reviews.add(Review);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Reviews;
    }

    private Genre getGenreByName(String id) {
        return new Genre(1, id);
    }
}


