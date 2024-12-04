package main.dao;

import main.dto.Wishlist;
import main.config.Database;
import main.constants.wishlist.WishlistPriority;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

    public static boolean addWishlistToDB(Wishlist wishlist) {
        String sql = "INSERT INTO Wishlists ("
                + "wishlist_id, "
                + "movie_id, "
                + "customer_id, "
                + "added_date, "
                + "priority"
                + ") VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, wishlist.getId());
            ps.setString(++count, wishlist.getMovieId());
            ps.setString(++count, wishlist.getCustomerId());
            ps.setDate(++count, Date.valueOf(wishlist.getAddedDate()));
            ps.setString(++count, wishlist.getPriority().name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateWishlistInDB(Wishlist wishlist) {
        String sql = "UPDATE Wishlists SET "
                + "movie_id = ?, "
                + "customer_id = ?, "
                + "added_date = ?, "
                + "priority = ? "
                + "WHERE wishlist_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, wishlist.getMovieId());
            ps.setString(++count, wishlist.getCustomerId());
            ps.setDate(++count, Date.valueOf(wishlist.getAddedDate()));
            ps.setString(++count, wishlist.getPriority().name());
            ps.setString(++count, wishlist.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteWishlistFromDB(String wishlistId) {
        String sql = "DELETE FROM Wishlists WHERE wishlist_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, wishlistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Wishlist> getAllWishlists() {
        String sql = "SELECT * FROM Wishlists";
        List<Wishlist> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet resultSet = ps.executeQuery()) {

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
