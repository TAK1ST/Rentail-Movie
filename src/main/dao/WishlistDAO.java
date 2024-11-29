package main.dao;

import main.dto.Wishlist;
import main.config.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.dto.Payment;

public class WishlistDAO {

    // Add a new Wishlist entry to the database
    public static boolean addWishlistToDB(Wishlist wishlist) {
        String sql = "INSERT INTO Wishlist (wishlist_id, movie_id, customer_id, added_date, priority) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, wishlist.getWishlistId());
            preparedStatement.setString(2, wishlist.getMovieId());
            preparedStatement.setString(3, wishlist.getCustomerId());
            preparedStatement.setDate(4, Date.valueOf(wishlist.getAddedDate()));
            preparedStatement.setInt(5, wishlist.getPriority());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update an existing Wishlist entry in the database
    public static boolean updateWishlistFromDB(Wishlist wishlist) {
        String sql = "UPDATE Wishlist SET movie_id = ?, customer_id = ?, added_date = ?, priority = ? WHERE wishlist_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, wishlist.getMovieId());
            preparedStatement.setString(2, wishlist.getCustomerId());
            preparedStatement.setDate(3, Date.valueOf(wishlist.getAddedDate()));
            preparedStatement.setInt(4, wishlist.getPriority());
            preparedStatement.setString(5, wishlist.getWishlistId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a Wishlist entry from the database
    public static boolean deleteWishlistFromDB(String wishlistId) {
        String sql = "DELETE FROM Wishlist WHERE wishlist_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, wishlistId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get a Wishlist entry by wishlist_id
    public static Wishlist getWishlistById(String wishlistId) {
        String sql = "SELECT * FROM Wishlist WHERE wishlist_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, wishlistId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Wishlist(
                        resultSet.getString("id"),
                        resultSet.getString("wishlist_id"),
                        resultSet.getString("movie_id"),
                        resultSet.getString("customer_id"),
                        resultSet.getDate("added_date").toLocalDate(),
                        resultSet.getInt("priority")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all Wishlist entries
    public static List<Wishlist> getAllWishlist() {
        String sql = "SELECT * FROM Wishlist";
        List<Wishlist> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Wishlist wishlist = new Wishlist(
                        resultSet.getString("id"),
                        resultSet.getString("wishlist_id"),
                        resultSet.getString("movie_id"),
                        resultSet.getString("customer_id"),
                        resultSet.getDate("added_date").toLocalDate(),
                        resultSet.getInt("priority")
                );
                list.add(wishlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Wishlist> getAllPayment() {
        String sql = "SELECT * FROM Payment";
        List<Wishlist> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Wishlist payment = new Wishlist(
                        resultSet.getString("id"),
                        resultSet.getString("wishlist_id"),
                        resultSet.getString("movie_id"),
                        resultSet.getString("customer_id"),
                        resultSet.getDate("added_date").toLocalDate(),
                        resultSet.getInt("priority")
                );
                list.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
