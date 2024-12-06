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
                + "movie_id, "
                + "customer_id, "
                + "added_date, "
                + "priority "
                + ") VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, wishlist.getMovieId());
            ps.setString(++count, wishlist.getCustomerId());
            ps.setDate(++count, wishlist.getAddedDate() != null ? Date.valueOf(wishlist.getAddedDate()) : null);
            ps.setString(++count, wishlist.getPriority() != null ? wishlist.getPriority().name() : null);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateWishlistInDB(Wishlist wishlist) {
        String sql = "UPDATE Wishlists SET "
                + "added_date = ?, "
                + "priority = ? "
                + "WHERE "
                + "movie_id = ?, "
                + "customer_id = ?";
        try (Connection connection = Database.getConnection(); 
            PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setDate(++count, wishlist.getAddedDate() != null ? Date.valueOf(wishlist.getAddedDate()) : null);
            ps.setString(++count, wishlist.getPriority() != null ? wishlist.getPriority().name() : null);
            ps.setString(++count, wishlist.getId());
            ps.setString(++count, wishlist.getMovieId());
            ps.setString(++count, wishlist.getCustomerId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteWishlistFromDB(String customerID, String movieID) {
        String sql = "DELETE FROM Wishlists WHERE customer_id = ? movie_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, customerID);
            ps.setString(2, movieID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Wishlist> getAllWishlists() {
        String sql = "SELECT * FROM Wishlists";
        List<Wishlist> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Wishlist wishlist = new Wishlist(
                        rs.getString("customer_id"),
                        rs.getString("movie_id"),
                        rs.getDate("added_date") != null ? rs.getDate("added_date").toLocalDate() : null,
                        rs.getString("priority") != null ? WishlistPriority.valueOf(rs.getString("priority")) : null
                );
                list.add(wishlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<Wishlist> getUserWishlist(String customerId) {
        String query = "SELECT movie_id, added_date, priority FROM Wishlists WHERE customer_id = ?";

        List<Wishlist> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Wishlist wishlist = new Wishlist(
                        customerId,
                        rs.getString("movie_id"),
                        rs.getDate("added_date").toLocalDate(),
                        WishlistPriority.valueOf(rs.getString("priority"))
                );
                list.add(wishlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
}
