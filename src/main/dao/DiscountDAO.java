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

/**
 * @author kiet
 */
public class DiscountDAO {

    public static boolean addDiscountToDB(Discount discount) {
        String sql = "INSERT INTO Discounts (code, start_date, end_date, type, usage_available, is_active, value) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
  
            preparedStatement.setString(1, discount.getCode());  
            preparedStatement.setString(2, discount.getCustomerID());
            preparedStatement.setDate(3, Date.valueOf(discount.getStartDate()));  
            preparedStatement.setDate(4, Date.valueOf(discount.getEndDate()));  
            preparedStatement.setString(5, discount.getType().name());  
            preparedStatement.setInt(6, discount.getUsageAvailable());  
            preparedStatement.setBoolean(7, discount.isActive());  
            preparedStatement.setDouble(8, discount.getValue());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateDiscountInDB(Discount discount) {
        String sql = "UPDATE Discounts SET account_id = ?, start_date = ?, end_date = ?, type = ?, usage_available = ?, is_active = ?, value = ? WHERE code = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, discount.getCustomerID());
            preparedStatement.setDate(2, Date.valueOf(discount.getStartDate()));
            preparedStatement.setDate(3, Date.valueOf(discount.getEndDate()));
            preparedStatement.setString(4, discount.getType().name());
            preparedStatement.setInt(5, discount.getUsageAvailable());
            preparedStatement.setBoolean(6, discount.isActive());
            preparedStatement.setDouble(7, discount.getValue());
            preparedStatement.setString(8, discount.getCode());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteDiscountFromDB(String discountID) {
        String sql = "DELETE FROM Discounts WHERE code = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, discountID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Discount> getAllDiscounts() {
        String sql = "SELECT * FROM Discounts";
        List<Discount> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Discount discount = new Discount(
                    resultSet.getString("code"),
                    resultSet.getString("account_id"),
                    resultSet.getDate("start_date").toLocalDate(),
                    resultSet.getDate("end_date").toLocalDate(),
                    DiscountType.valueOf(resultSet.getString("type")),
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
   
}
