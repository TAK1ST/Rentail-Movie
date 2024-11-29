package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.dto.Discount;
import main.config.Database;

/**
 * @author kiet
 */
public class DiscountDAO {

    // Method to add a new discount to the database
    public static boolean addDiscountToDB(Discount discount) {
        String sql = "INSERT INTO Discounts (discount_id, code, start_date, end_date, type, usage_available, is_active, value) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, discount.getId());  
            preparedStatement.setString(2, discount.getCode());  
            preparedStatement.setObject(3, discount.getStartDate());  
            preparedStatement.setObject(4, discount.getEndDate());  
            preparedStatement.setString(5, discount.getType());  
            preparedStatement.setInt(6, discount.getUsageAvailable());  
            preparedStatement.setBoolean(7, discount.isActive());  
            preparedStatement.setDouble(8, discount.getValue());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update an existing discount in the database
    public static boolean updateDiscountInDB(Discount discount) {
        String sql = "UPDATE Discounts SET code = ?, start_date = ?, end_date = ?, type = ?, usage_available = ?, is_active = ?, value = ? WHERE discount_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, discount.getCode());
            preparedStatement.setObject(2, discount.getStartDate());
            preparedStatement.setObject(3, discount.getEndDate());
            preparedStatement.setString(4, discount.getType());
            preparedStatement.setInt(5, discount.getUsageAvailable());
            preparedStatement.setBoolean(6, discount.isActive());
            preparedStatement.setDouble(7, discount.getValue());
            preparedStatement.setString(8, discount.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to delete a discount from the database by its ID
    public static boolean deleteDiscountFromDB(String discountID) {
        String sql = "DELETE FROM Discounts WHERE discount_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, discountID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to retrieve all discounts from the database
    public static List<Discount> getAllDiscounts() {
        String sql = "SELECT * FROM Discounts";
        List<Discount> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Discount discount = new Discount(
                    resultSet.getString("discount_id"),
                    resultSet.getString("code"),
                    resultSet.getObject("start_date", LocalDate.class),
                    resultSet.getObject("end_date", LocalDate.class),
                    resultSet.getString("type"),
                    resultSet.getInt("usage_available"),
                    resultSet.getBoolean("is_active"),
                    resultSet.getDouble("value")
                );
                list.add(discount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Method to get a discount by its code
    public static Discount getDiscountByCode(String code) {
        String sql = "SELECT * FROM Discounts WHERE code = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Discount(
                        resultSet.getString("discount_id"),
                        resultSet.getString("code"),
                        resultSet.getObject("start_date", LocalDate.class),
                        resultSet.getObject("end_date", LocalDate.class),
                        resultSet.getString("type"),
                        resultSet.getInt("usage_available"),
                        resultSet.getBoolean("is_active"),
                        resultSet.getDouble("value")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
