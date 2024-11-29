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

/**
 *
 * @author trann
 */
public class RentalDAO {
    
    public static boolean addRentalToDB(Rental rental) {
        String sql = "INSERT INTO Rental (rental_id, user_id, movie_id, staff_id, rental_date, return_date, late_fee, due_date, total_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, rental.getId());
            preparedStatement.setString(2, rental.getUserID());
            preparedStatement.setString(3, rental.getMovieID());
            preparedStatement.setString(4, rental.getStaffID());
            preparedStatement.setDate(5, Date.valueOf(rental.getRentalDate()));
            preparedStatement.setDate(6, Date.valueOf(rental.getReturnDate()));
            preparedStatement.setDouble(7, rental.getLate_fee());
            preparedStatement.setDouble(8, rental.getDue_date());
            preparedStatement.setDouble(9, rental.getTotal_amount());
            preparedStatement.setString(10, rental.getStatus());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateRentalFromDB(Rental rental) {
        String sql = "UPDATE Rental SET user_id = ?, movie_id = ?, staff_id = ?, rental_date = ?, return_date = ?, late_fee = ?, due_date = ?, total_amount = ?, status = ? WHERE rental_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, rental.getUserID());
            preparedStatement.setString(2, rental.getMovieID());
            preparedStatement.setString(3, rental.getStaffID());
            preparedStatement.setDate(4, Date.valueOf(rental.getRentalDate()));
            preparedStatement.setDate(5, Date.valueOf(rental.getReturnDate()));
            preparedStatement.setDouble(6, rental.getLate_fee());
            preparedStatement.setDouble(7, rental.getDue_date());
            preparedStatement.setDouble(8, rental.getTotal_amount());
            preparedStatement.setString(9, rental.getStatus());
            preparedStatement.setString(10, rental.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteRentalFromDB(String rentalID) {
        String sql = "DELETE FROM Rental WHERE rental_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, rentalID);
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Rental> getAllRental() {
        String sql = "SELECT * FROM Rental";
        List<Rental> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Rental rental = new Rental(
                    resultSet.getString("rental_id"),
                    resultSet.getString("user_id"),
                    resultSet.getString("movie_id"),
                    resultSet.getString("staff_id"),
                    resultSet.getDate("rental_date").toLocalDate(),
                    resultSet.getDate("return_date").toLocalDate(),
                    resultSet.getDouble("late_fee"),
                    resultSet.getDouble("due_date"),
                    resultSet.getDouble("total_amount"),
                    resultSet.getString("status")
                );
                list.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
