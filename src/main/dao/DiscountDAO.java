package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import main.dto.Discount;
import main.config.Database;
import main.constants.discount.ApplyForWhat;
import main.constants.discount.ApplyForWho;
import main.constants.discount.DiscountType;
import static main.dao.MiddleTableDAO.getSubIdsByMainId;

public class DiscountDAO {

    public static boolean addDiscountToDB(Discount discount) {
        String sql = "INSERT INTO Discounts ("
                + "discount_code, "
                + "start_date, "
                + "end_date, "
                + "discount_type, "
                + "quantity, "
                + "is_active, "
                + "discount_value, "
                + "apply_for_who, "
                + "apply_for_what "
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, discount.getCode());
            ps.setDate(++count, Date.valueOf(discount.getStartDate()));
            ps.setDate(++count, Date.valueOf(discount.getEndDate()));
            ps.setString(++count, discount.getType().name());
            ps.setInt(++count, discount.getQuantity());
            ps.setBoolean(++count, discount.isActive());
            ps.setDouble(++count, discount.getValue());
            ps.setString(++count, discount.getApplyForWho().name());
            ps.setString(++count, discount.getApplyForWhat().name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return false;
    }
    
    public static boolean updateDiscountInDB(Discount discount) {
        String sql = "UPDATE Discounts SET "
                + "start_date = ?, "
                + "end_date = ?, "
                + "discount_type = ?, "
                + "quantity = ?, "
                + "is_active = ?, "
                + "discount_value = ? "
                + "apply_for_who = ? "
                + "apply_for_what = ? "
                + "WHERE discount_code = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setDate(++count, Date.valueOf(discount.getStartDate()));
            ps.setDate(++count, Date.valueOf(discount.getEndDate()));
            ps.setString(++count, discount.getType().name());
            ps.setInt(++count, discount.getQuantity());
            ps.setBoolean(++count, discount.isActive());
            ps.setDouble(++count, discount.getValue());
            ps.setString(++count, discount.getCode());
            ps.setString(++count, discount.getApplyForWho().name());
            ps.setString(++count, discount.getApplyForWhat().name());

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
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Discount discount = new Discount(
                        rs.getString("discount_code"),
                        getSubIdsByMainId("Discount_Account", rs.getString("discount_code"), "discount_code", "customer_id"),
                        getSubIdsByMainId("Discount_Movie", rs.getString("discount_code"), "discount_code", "movie_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        DiscountType.valueOf(rs.getString("discount_type")),
                        rs.getInt("quantity"),
                        rs.getBoolean("is_active"),
                        rs.getDouble("discount_value"),
                        ApplyForWho.valueOf(rs.getString("apply_for_who")),
                        ApplyForWhat.valueOf(rs.getString("apply_for_what"))
                );
                list.add(discount);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return list;
    }
    
    public boolean chooseAndApplyDiscount(String customerId, String discountCode) throws SQLException {
        String sql = "UPDATE Discount_Account SET used_on = CURRENT_TIMESTAMP WHERE customer_id = ? AND discount_code = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ps.setString(2, discountCode);

            return ps.executeUpdate() > 0;
        }   catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return false;
    }
    
    public boolean isDiscountUsed(String customerId, String discountCode) throws SQLException {
        String query = "SELECT used_on FROM Discount_Account WHERE customer_id = ? AND discount_code = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, customerId);
            stmt.setString(2, discountCode);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Timestamp usedOn = rs.getTimestamp("used_on");
                return usedOn != null; 
            }
        }
        return false;
    }

}
