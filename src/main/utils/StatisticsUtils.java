package main.utils;

import java.sql.*;

public class StatisticsUtils {

    // Hàm lấy kết nối cơ sở dữ liệu
    private static Connection getConnection() throws SQLException {
        return DatabaseUtil.getConnection();
    }

    // Thống kê số lượng rentals cho từng movie (5 bộ phim được thuê nhiều nhất)
    public static void printTopRentalMovies() {
        String query = "SELECT m.title, COUNT(r.rental_id) AS rental_count "
                + "FROM Rental r "
                + "JOIN Movie m ON r.movie_id = m.movie_id "
                + "GROUP BY m.movie_id "
                + "ORDER BY rental_count DESC "
                + "LIMIT 5";

        try (Connection connection = getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Top 5 films rented most:");
            while (rs.next()) {
                String title = rs.getString("title");
                int rentalCount = rs.getInt("rental_count");
                System.out.println(title + ": " + rentalCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thống kê người dùng thuê nhiều nhất
    public static void printTopCustomer() {
        String query = "SELECT u.full_name, COUNT(r.rental_id) AS rental_count "
                + "FROM Rental r "
                + "JOIN Users u ON r.user_id = u.user_id "
                + "GROUP BY u.user_id "
                + "ORDER BY rental_count DESC "
                + "LIMIT 1";

        try (Connection connection = getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String customerName = rs.getString("full_name");
                int rentalCount = rs.getInt("rental_count");
                System.out.println("Person of the most rent: " + customerName + " with " + rentalCount + " rent.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tính tổng doanh thu từ rentals
    public static void printTotalRevenue() {
        String query = "SELECT SUM(r.charges) AS total_revenue "
                + "FROM Rental r";

        try (Connection connection = getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                double totalRevenue = rs.getDouble("total_revenue");
                System.out.println("Total revenue from rentals: " + totalRevenue); //revenue: doanh thu
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // total overdue fees and penalties
    public static void printTotalFinesAndCharges() {
        String query = "SELECT SUM(r.overdue_fines) + SUM(r.charges) AS total_fines_and_charges "
                + "FROM Rental r";

        try (Connection connection = getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                double totalFinesAndCharges = rs.getDouble("total_fines_and_charges");
                System.out.println("Tổng phí và phạt quá hạn: " + totalFinesAndCharges);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // The number of copies of the film remaining in stock
    public static void printAvailableCopies() {
        String query = "SELECT title, available_copies FROM Movie";

        try (Connection connection = getConnection(); 
             Statement stmt = connection.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Số lượng bản sao còn lại của các bộ phim:");
            while (rs.next()) {
                String title = rs.getString("title");
                int availableCopies = rs.getInt("available_copies");
                System.out.println(title + ": " + availableCopies + " bản sao còn lại");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
