package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Payment;
import main.config.Database;

public class PaymentDAO {
    
    // Add a Payment record to the database
    public static boolean addPaymentToDB(Payment payment) {
        String sql = "INSERT INTO Payment (payment_id, payment_methods, rental_id) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, payment.getId());
            preparedStatement.setString(2, payment.getPaymentMethods());
            preparedStatement.setString(3, payment.getRentalId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Update a Payment record in the database
    public static boolean updatePaymentFromDB(Payment payment) {
        String sql = "UPDATE Payment SET payment_methods = ?, rental_id = ? WHERE payment_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, payment.getPaymentMethods());
            preparedStatement.setString(2, payment.getRentalId());
            preparedStatement.setString(3, payment.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Delete a Payment record from the database
    public static boolean deletePaymentFromDB(String paymentId) {
        String sql = "DELETE FROM Payment WHERE payment_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, paymentId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get all Payment records from the database
    public static List<Payment> getAllPayments() {
        String sql = "SELECT * FROM Payment";
        List<Payment> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Payment payment = new Payment(
                    resultSet.getString("payment_id"),
                    resultSet.getString("payment_methods"),
                    resultSet.getString("rental_id")
                );
                list.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
