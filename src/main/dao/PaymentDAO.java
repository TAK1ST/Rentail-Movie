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
        String sql = "INSERT INTO Payments (rental_id, payment_method) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, payment.getId());
            ps.setString(++count, payment.getMethod().name());
            ps.setString(++count, payment.getRentalId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updatePaymentInDB(Payment payment) {
        String sql = "UPDATE Payments SET payment_method = ? WHERE rental_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, payment.getMethod().name());
            ps.setString(++count, payment.getRentalId());
            ps.setString(++count, payment.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deletePaymentFromDB(String rentalId) {
        String sql = "DELETE FROM Payments WHERE rental_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, rentalId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Payment> getAllPayments() {
        String sql = "SELECT * FROM Payments";
        List<Payment> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Payment payment = new Payment(
                    resultSet.getString("rental_id"),
                    PaymentMethod.valueOf(resultSet.getString("payment_method"))
                );
                list.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
