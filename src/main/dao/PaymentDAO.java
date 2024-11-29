package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Payment;
import main.config.Database;
import main.constants.PaymentMethod;

public class PaymentDAO {
    
    public static boolean addPaymentToDB(Payment payment) {
        String sql = "INSERT INTO Payments (payment_id, payment_methods, rental_id) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, payment.getId());
            preparedStatement.setString(2, payment.getPaymentMethods().name());
            preparedStatement.setString(3, payment.getRentalId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updatePaymentInDB(Payment payment) {
        String sql = "UPDATE Payments SET payment_methods = ?, rental_id = ? WHERE payment_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, payment.getPaymentMethods().name());
            preparedStatement.setString(2, payment.getRentalId());
            preparedStatement.setString(3, payment.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deletePaymentFromDB(String paymentId) {
        String sql = "DELETE FROM Payments WHERE payment_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, paymentId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Payment> getAllPayments() {
        String sql = "SELECT * FROM Payments";
        List<Payment> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Payment payment = new Payment(
                    resultSet.getString("payment_id"),
                    PaymentMethod.valueOf(resultSet.getString("payment_methods")),
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
