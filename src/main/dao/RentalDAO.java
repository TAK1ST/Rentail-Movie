package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Rental;
import main.config.Database;
import main.constants.rental.RentalStatus;

public class RentalDAO {

    public static boolean addRentalToDB(Rental rental) {
        String sql = "INSERT INTO Rentals ("
                + "customer_id, "
                + "movie_id, "
                + "staff_id, "
                + "rental_date, "
                + "return_date, "
                + "due_date, "
                + "late_fee, "
                + "total_amount, "
                + "status "
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, rental.getCustomerID());
            ps.setString(++count, rental.getMovieID());
            ps.setString(++count, rental.getStaffID());
            ps.setDate(++count, rental.getRentalDate() != null ? Date.valueOf(rental.getRentalDate()) : null);
            ps.setDate(++count, rental.getReturnDate() != null ? Date.valueOf(rental.getReturnDate()) : null);
            ps.setDate(++count, rental.getDueDate() != null ? Date.valueOf(rental.getDueDate()) : null);
            ps.setDouble(++count, rental.getLateFee());
            ps.setDouble(++count, rental.getTotalAmount());
            ps.setString(++count, rental.getStatus() != null ? rental.getStatus().name() : null);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateRentalInDB(Rental rental) {
        String sql = "UPDATE Rentals SET "
                + "staff_id = ?, "
                + "rental_date = ?, "
                + "return_date = ?, "
                + "due_date = ?, "
                + "late_fee = ?, "
                + "total_amount = ?,"
                + "status = ? "
                + "WHERE "
                + "customer_id = ? AND "
                + "movie_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, rental.getStaffID());
            ps.setDate(++count, rental.getRentalDate() != null ? Date.valueOf(rental.getRentalDate()) : null);
            ps.setDate(++count, rental.getReturnDate() != null ? Date.valueOf(rental.getReturnDate()) : null);
            ps.setDate(++count, rental.getDueDate() != null ? Date.valueOf(rental.getDueDate()) : null);
            ps.setDouble(++count, rental.getLateFee());
            ps.setDouble(++count, rental.getTotalAmount());
            ps.setString(++count, rental.getStatus() != null ? rental.getStatus().name() : null);
            ps.setString(++count, rental.getCustomerID());
            ps.setString(++count, rental.getMovieID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteRentalFromDB(String customerID, String movieID) {
        String sql = "DELETE FROM Rentals WHERE customer_id = ? AND movie_id = ?";
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

    public static List<Rental> getAllRentals() {
        String sql = "SELECT * FROM Rentals";
        List<Rental> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rental rental = new Rental(
                        rs.getString("customer_id"),
                        rs.getString("movie_id"),
                        rs.getString("staff_id"),
                        rs.getDate("rental_date") != null ? rs.getDate("rental_date").toLocalDate() : null,
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                        rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null,
                        rs.getDouble("late_fee"),
                        rs.getDouble("total_amount"),
                        rs.getString("status") != null ? RentalStatus.valueOf(rs.getString("status")) : null
                );
                list.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<Rental> getUserRentals(String customerId) {
        String query = "SELECT movie_id, staff_id, rental_date, due_date, return_date, late_fee, total_amount, status FROM Rentals WHERE customer_id = ?";

        List<Rental> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Rental rental = new Rental(
                        customerId,
                        rs.getString("movie_id"),
                        rs.getString("staff_id"),
                        rs.getDate("rental_date") != null ? rs.getDate("rental_date").toLocalDate() : null,
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                        rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null,
                        rs.getDouble("late_fee"),
                        rs.getDouble("total_amount"),
                        rs.getString("status") != null ? RentalStatus.valueOf(rs.getString("status")) : null
                );
                list.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
