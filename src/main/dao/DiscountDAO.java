package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Discount;
import main.config.Database;
import main.constants.DiscountType;

public class DiscountDAO {

    public static boolean addDiscountToDB(Discount discount) {
        String sql = "INSERT INTO Discounts ("
                + "discount_code, "
                + "customer_id, "
                + "start_date, "
                + "end_date, "
                + "discount_type, "
                + "quantity, "
                + "is_active, "
                + "discount_value"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, discount.getCode());
            ps.setString(++count, discount.getCustomerID());
            ps.setDate(++count, Date.valueOf(discount.getStartDate()));
            ps.setDate(++count, Date.valueOf(discount.getEndDate()));
            ps.setString(++count, discount.getType().name());
            ps.setInt(++count, discount.getQuantity());
            ps.setBoolean(++count, discount.isActive());
            ps.setDouble(++count, discount.getValue());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return false;
    }

    public static boolean updateDiscountInDB(Discount discount) {
        String sql = "UPDATE Discounts SET "
                + "customer_id = ?, "
                + "start_date = ?, "
                + "end_date = ?, "
                + "discount_type = ?, "
                + "quantity = ?, "
                + "is_active = ?, "
                + "discount_value = ? "
                + "WHERE discount_code = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, discount.getCustomerID());
            ps.setDate(++count, Date.valueOf(discount.getStartDate()));
            ps.setDate(++count, Date.valueOf(discount.getEndDate()));
            ps.setString(++count, discount.getType().name());
            ps.setInt(++count, discount.getQuantity());
            ps.setBoolean(++count, discount.isActive());
            ps.setDouble(++count, discount.getValue());
            ps.setString(++count, discount.getCode());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return false;
    }

    public static boolean deleteDiscountFromDB(String discountID) {
        String sql = "DELETE FROM Discounts WHERE discount_code = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, discountID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return false;
    }

    public static List<Discount> getAllDiscounts() {
        String sql = "SELECT * FROM Discounts";
        List<Discount> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Discount discount = new Discount(
                        resultSet.getString("discount_code"),
                        resultSet.getString("customer_id"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        DiscountType.valueOf(resultSet.getString("discount_type")),
                        resultSet.getInt("quantity"),
                        resultSet.getBoolean("is_active"),
                        resultSet.getDouble("discount_value")
                );
                list.add(discount);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return list;
    }
}
