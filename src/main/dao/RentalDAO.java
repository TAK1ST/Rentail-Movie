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
                + "rental_id, "
                + "customer_id, "
                + "movie_id, "
                + "staff_id, "
                + "rental_date, "
                + "return_date, "
                + "due_date, "
                + "late_fee, "
                + "total_amount, "
                + "status"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, rental.getId());
            ps.setString(++count, rental.getCustomerID());
            ps.setString(++count, rental.getMovieID());
            ps.setString(++count, rental.getStaffID());
            ps.setDate(++count, Date.valueOf(rental.getRentalDate()));
            ps.setDate(++count, Date.valueOf(rental.getReturnDate()));
            ps.setDate(++count, Date.valueOf(rental.getDueDate()));
            ps.setDouble(++count, rental.getLateFee());
            ps.setDouble(++count, rental.getTotalAmount());
            ps.setString(++count, rental.getStatus().name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateRentalInDB(Rental rental) {
        String sql = "UPDATE Rentals SET "
                + "customer_id = ?, "
                + "movie_id = ?, "
                + "staff_id = ?, "
                + "rental_date = ?, "
                + "return_date = ?, "
                + "late_fee = ?, "
                + "due_date = ?, "
                + "total_amount = ?, "
                + "status = ? "
                + "WHERE rental_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, rental.getCustomerID());
            ps.setString(++count, rental.getMovieID());
            ps.setString(++count, rental.getStaffID());
            ps.setDate(++count, Date.valueOf(rental.getRentalDate()));
            ps.setDate(++count, Date.valueOf(rental.getReturnDate()));
            ps.setDate(++count, Date.valueOf(rental.getDueDate()));
            ps.setDouble(++count, rental.getLateFee());
            ps.setDouble(++count, rental.getTotalAmount());
            ps.setString(++count, rental.getStatus().name());
            ps.setString(++count, rental.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteRentalFromDB(String rentalID) {
        String sql = "DELETE FROM Rentals WHERE rental_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, rentalID);
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
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Rental rental = new Rental(
                        resultSet.getString("rental_id"),
                        resultSet.getString("customer_id"),
                        resultSet.getString("movie_id"),
                        resultSet.getString("staff_id"),
                        resultSet.getDate("rental_date").toLocalDate(),
                        resultSet.getDate("return_date") != null ? resultSet.getDate("return_date").toLocalDate() : null,
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getDouble("late_fee"),
                        resultSet.getDouble("total_amount"),
                        RentalStatus.valueOf(resultSet.getString("status"))
                );
                list.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<String> getUserRentals(String customerId) throws SQLException {
        String query = "SELECT rental_id, movie_id, rental_date, due_date, return_date, status FROM Rentals WHERE customer_id = ?";

        List<String> rentals = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String rentalId = rs.getString("rental_id");
                String movieId = rs.getString("movie_id");
                Date rentalDate = rs.getDate("rental_date");
                Date dueDate = rs.getDate("due_date");
                Date returnDate = rs.getDate("return_date");
                String status = rs.getString("status");

                rentals.add("Rental ID: " + rentalId + ", Movie ID: " + movieId + ", Rental Date: " + rentalDate + ", Due Date: " + dueDate + ", Return Date: " + returnDate + ", Status: " + status);
            }
        }
        return rentals;
    }

}
