/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.CRUD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Rental;
import main.utils.DatabaseUtil;

/**
 *
 * @author trann
 */
public class RentalCRUD {
    
    public static boolean addRentalToDB(Rental rental) {
        String sql = "INSERT INTO Rental (rental_id, user_id, movie_id, retal_date, return_date, charges, overdue_fines) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, rental.getId());
            preparedStatement.setString(2, rental.getUserId());
            preparedStatement.setString(3, rental.getMovieId());
            preparedStatement.setDate(4, Date.valueOf(rental.getRentalDate()));
            preparedStatement.setDate(5, Date.valueOf(rental.getReturnDate()));
            preparedStatement.setDouble(6, rental.getCharges());
            preparedStatement.setDouble(7, rental.getOverdueFines());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateRentalFromDB(Rental rental) {
        String sql = "UPDATE Rental SET userId = ?, movieId = ?, rentalDate = ?, returnDate = ?, charges = ?, overdueFines = ? WHERE rentalId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, rental.getUserId());
            preparedStatement.setString(2, rental.getMovieId());
            preparedStatement.setDate(3, Date.valueOf(rental.getRentalDate()));
            preparedStatement.setDate(4, Date.valueOf(rental.getReturnDate()));
            preparedStatement.setDouble(5, rental.getCharges());
            preparedStatement.setDouble(6, rental.getOverdueFines());
            preparedStatement.setString(7, rental.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteRentalFromDB(String rentalID) {
        String sql = "DELETE FROM Rental WHERE rentalId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
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
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Rental rental = new Rental(
                    resultSet.getString("rentalId"),
                    resultSet.getString("userId"),
                    resultSet.getString("movieId"),
                    resultSet.getDate("rentalDate").toLocalDate(),
                    resultSet.getDate("returnDate").toLocalDate(),
                    resultSet.getDouble("charges"),
                    resultSet.getDouble("overdueFines")
                );
                list.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
