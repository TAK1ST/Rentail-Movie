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
import main.constants.RentalStatus;

/**
 *
 * @author trann
 */
public class RentalDAO {
    
    public static boolean addRentalToDB(Rental rental) {
        String sql = "INSERT INTO Rentals (rental_id, customer_id, movie_id, staff_id, rental_date, return_date, due_date, late_fee, total_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, rental.getId());
            preparedStatement.setString(2, rental.getCustomerID());
            preparedStatement.setString(3, rental.getMovieID());
            preparedStatement.setString(4, rental.getStaffID());
            preparedStatement.setDate(5, Date.valueOf(rental.getRentalDate()));
            preparedStatement.setDate(6, Date.valueOf(rental.getReturnDate()));
            preparedStatement.setDate(8, Date.valueOf(rental.getDueDate()));
            preparedStatement.setDouble(7, rental.getLateFee());
            preparedStatement.setDouble(9, rental.getTotalAmount());
            preparedStatement.setString(10, rental.getStatus().name());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateRentalInDB(Rental rental) {
        String sql = "UPDATE Rentals SET customer_id = ?, movie_id = ?, staff_id = ?, rental_date = ?, return_date = ?, late_fee = ?, due_date = ?, total_amount = ?, status = ? WHERE rental_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, rental.getCustomerID());
            preparedStatement.setString(2, rental.getMovieID());
            preparedStatement.setString(3, rental.getStaffID());
            preparedStatement.setDate(4, Date.valueOf(rental.getRentalDate()));
            preparedStatement.setDate(5, Date.valueOf(rental.getReturnDate()));
            preparedStatement.setDate(6, Date.valueOf(rental.getDueDate()));
            preparedStatement.setDouble(7, rental.getLateFee());
            preparedStatement.setDouble(8, rental.getTotalAmount());
            preparedStatement.setString(9, rental.getStatus().name());
            preparedStatement.setString(10, rental.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteRentalFromDB(String rentalID) {
        String sql = "DELETE FROM Rentals WHERE rental_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, rentalID);
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Rental> getAllRentals() {
        String sql = "SELECT * FROM Rentals";
        List<Rental> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

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
}
