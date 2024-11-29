package main.dao;

import main.dto.Wishlist;
import main.config.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.constants.WishlistPriority;

public class WishlistDAO {

    public static boolean addWishlistToDB(Wishlist wishlist) {
        String sql = "INSERT INTO Wishlists (wishlist_id, movie_id, customer_id, added_date, priority) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, wishlist.getId());
            preparedStatement.setString(2, wishlist.getMovieId());
            preparedStatement.setString(3, wishlist.getCustomerId());
            preparedStatement.setDate(4, Date.valueOf(wishlist.getAddedDate()));
            preparedStatement.setString(5, wishlist.getPriority().name());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateWishlistInDB(Wishlist wishlist) {
        String sql = "UPDATE Wishlists SET movie_id = ?, customer_id = ?, added_date = ?, priority = ? WHERE wishlist_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, wishlist.getMovieId());
            preparedStatement.setString(2, wishlist.getCustomerId());
            preparedStatement.setDate(3, Date.valueOf(wishlist.getAddedDate()));
            preparedStatement.setString(4, wishlist.getPriority().name());
            preparedStatement.setString(5, wishlist.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteWishlistFromDB(String wishlistId) {
        String sql = "DELETE FROM Wishlists WHERE wishlist_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, wishlistId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Wishlist> getAllWishlists() {
        String sql = "SELECT * FROM Wishlists";
        List<Wishlist> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Wishlist wishlist = new Wishlist(
                        resultSet.getString("wishlist_id"),
                        resultSet.getString("movie_id"),
                        resultSet.getString("customer_id"),
                        resultSet.getDate("added_date").toLocalDate(),
                        WishlistPriority.valueOf(resultSet.getString("priority"))
                );
                list.add(wishlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
