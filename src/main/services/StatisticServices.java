package main.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import main.config.Database;

public class StatisticServices {

    public static Map<String, Double> getTop5RevenueGeneratingMovies() {
        String sql
                = "SELECT m.title,"
                + " SUM(r.total_amount + COALESCE(r.late_fee, 0)) as total_revenue "
                + "FROM Movies m "
                + "JOIN Rentals r ON m.movie_id = r.movie_id "
                + "WHERE r.status = 'APPROVED'"
                + "GROUP BY m.movie_id, m.title "
                + "ORDER BY total_revenue DESC; ";

        Map<String, Double> results = new LinkedHashMap<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.put(rs.getString("title"), rs.getDouble("total_revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static Map<String, Double> getTop5HighestRatedMovies() {
        String sql = " SELECT m.title, "
                + "    m.avg_rating,"
                + " COUNT(r.review_id) as review_count"
                + " FROM Movies m "
                + " LEFT JOIN Reviews r ON m.movie_id = r.movie_id "
                + " GROUP BY m.movie_id, m.title, m.avg_rating"
                + " HAVING review_count >= 5"
                + " ORDER BY m.avg_rating DESC "
                + " LIMIT 5";

        Map<String, Double> results = new LinkedHashMap<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.put(rs.getString("title"), rs.getDouble("avg_rating"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static Map<String, Integer> getTop5MostWishlistedMovies() {
        String sql
                = "SELECT m.title, "
                + "       COUNT(w.wishlist_id) as wishlist_count,"
                + "       COUNT(CASE WHEN w.priority = 'HIGH' THEN 1 END) as high_priority_count"
                + " FROM Movies m "
                + " JOIN Wishlists w ON m.movie_id = w.movie_id "
                + " GROUP BY m.movie_id, m.title "
                + " ORDER BY wishlist_count DESC, high_priority_count DESC "
                + " LIMIT 5";

        Map<String, Integer> results = new LinkedHashMap<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.put(rs.getString("title"), rs.getInt("wishlist_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static Map<String, Double> getTop5PremiumCustomersBySpending() {
        String sql =
                " SELECT p.full_name,"
                + "     SUM(r.total_amount + COALESCE(r.late_fee, 0)) as total_spent "
                + "FROM Accounts a "
                + "JOIN Profiles p ON a.account_id = p.account_id "
                + "JOIN Rentals r ON a.account_id = r.customer_id "
                + "WHERE a.role = 'PREMIUM' "
                + "AND r.status = 'APPROVED'"
                + "GROUP BY a.account_id, p.full_name "
                + "ORDER BY total_spent DESC "
                + "LIMIT 5";

        Map<String, Double> results = new LinkedHashMap<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.put(rs.getString("full_name"), rs.getDouble("total_spent"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static Map<String, Integer> getTop5MostActiveGenres() {
        String sql = 
            " SELECT g.genre_name,"
            + "       COUNT(r.rental_id) as rental_count "
            + "FROM Genres g "
            + "JOIN Movie_Genre mg ON g.genre_name = mg.genre_name "
            + "JOIN Movies m ON mg.movie_id = m.movie_id "
            + "JOIN Rentals r ON m.movie_id = r.movie_id "
            + "WHERE r.status = 'APPROVED'"
            + "GROUP BY g.genre_name "
            + "ORDER BY rental_count DESC "
            + "LIMIT 5";

        Map<String, Integer> results = new LinkedHashMap<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.put(rs.getString("genre_name"), rs.getInt("rental_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
