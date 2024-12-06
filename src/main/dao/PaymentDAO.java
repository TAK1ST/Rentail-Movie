package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import main.dto.Payment;
import main.config.Database;
import main.constants.payment.PaymentMethod;
import main.constants.payment.PaymentStatus;

public class PaymentDAO {
    
    public static boolean addPaymentToDB(Payment payment) {
        String sql = "INSERT INTO Payments ("
                + "payment_id, "
                + "customer_id, "
                + "amount, "
                + "payment_method, "
                + "transaction_time, "
                + "status "
                + ") VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, payment.getId());
            ps.setString(++count, payment.getCustomerID());
            ps.setDouble(++count, payment.getAmount());
            ps.setString(++count, payment.getMethod() != null ? payment.getMethod().name() : null);
            ps.setTimestamp(++count, payment.getTransactionTime() != null ? Timestamp.valueOf(payment.getTransactionTime()) : null);
            ps.setString(++count, payment.getStatus() != null ? payment.getStatus().name() : null);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updatePaymentInDB(Payment payment) {
        String sql = "UPDATE Payments SET "
                + "customer_id = ? "
                + "amount = ? "
                + "payment_method = ? "
                + "transaction_time = ? "
                + "status = ? "
                + "WHERE payment_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, payment.getId());
            ps.setString(++count, payment.getCustomerID());
            ps.setDouble(++count, payment.getAmount());
            ps.setString(++count, payment.getMethod() != null ? payment.getMethod().name() : null);
            ps.setTimestamp(++count, payment.getTransactionTime() != null ? Timestamp.valueOf(payment.getTransactionTime()) : null);
            ps.setString(++count, payment.getStatus() != null ? payment.getStatus().name() : null);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deletePaymentFromDB(String paymentId) {
        String sql = "DELETE FROM Payments WHERE payment_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, paymentId);
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
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Payment payment = new Payment(
                    rs.getString("payment_id"),
                    rs.getString("customer_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_method") != null ? PaymentMethod.valueOf(rs.getString("payment_method")) : null,
                    rs.getTimestamp("transaction_time") != null ? rs.getTimestamp("transaction_time").toLocalDateTime() : null,
                    rs.getString("status") != null ? PaymentStatus.valueOf(rs.getString("status")) : null
                );
                list.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<Payment> getUserPayments(String customerId) throws SQLException {
        String query = "SELECT * FROM Payments WHERE customer_id = ?";

        List<Payment> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment(
                    rs.getString("payment_id"),
                    customerId,
                    rs.getDouble("amount"),
                    rs.getString("payment_method") != null ? PaymentMethod.valueOf(rs.getString("payment_method")) : null,
                    rs.getTimestamp("transaction_time") != null ? rs.getTimestamp("transaction_time").toLocalDateTime() : null,
                    rs.getString("status") != null ? PaymentStatus.valueOf(rs.getString("status")) : null
                );
                list.add(payment);
            }
        }
        return list;
    }
    
}
