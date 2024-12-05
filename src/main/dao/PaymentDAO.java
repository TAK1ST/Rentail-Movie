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
                + "status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, payment.getId());
            ps.setString(++count, payment.getCustomerID());
            ps.setDouble(++count, payment.getAmount());
            ps.setString(++count, payment.getMethod().name());
            if (payment.getTransactionTime() != null) {
                ps.setTimestamp(++count, Timestamp.valueOf(payment.getTransactionTime()));
            } else {
                ps.setNull(++count, java.sql.Types.TIMESTAMP);
            }
            ps.setString(++count, payment.getStatus().name());
            
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
            ps.setString(++count, payment.getMethod().name());
            if (payment.getTransactionTime() != null) {
                ps.setTimestamp(++count, Timestamp.valueOf(payment.getTransactionTime()));
            } else {
                ps.setNull(++count, java.sql.Types.TIMESTAMP);
            }
            ps.setString(++count, payment.getStatus().name());

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
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Payment payment = new Payment(
                    resultSet.getString("payment_id"),
                    resultSet.getString("customer_id"),
                    resultSet.getDouble("amount"),
                    PaymentMethod.valueOf(resultSet.getString("payment_method")),
                    resultSet.getTimestamp("transaction_time") != null ? resultSet.getTimestamp("transaction_time").toLocalDateTime() : null,
                    PaymentStatus.valueOf(resultSet.getString("status"))     
                );
                list.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<String> getUserPayments(String customerId) throws SQLException {
        String query = "SELECT payment_id, amount, payment_method, transaction_time, status FROM Payments WHERE customer_id = ?";

        List<String> payments = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String paymentId = rs.getString("payment_id");
                double amount = rs.getDouble("amount");
                String paymentMethod = rs.getString("payment_method");
                Timestamp transactionTime = rs.getTimestamp("transaction_time");
                String status = rs.getString("status");

                payments.add("Payment ID: " + paymentId + ", Amount: " + amount + ", Payment Method: " + paymentMethod + ", Transaction Time: " + transactionTime + ", Status: " + status);
            }
        }
        return payments;
    }
    
}
