package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
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
    
    public static boolean chooseAndApplyDiscount(String customerId, String discountCode) {
        String sql = "UPDATE Discount_Account SET used_on = CURRENT_TIMESTAMP WHERE customer_id = ? AND discount_code = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ps.setString(2, discountCode);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return false;
    }
    
    public static boolean isDiscountUsed(String customerId, String discountCode) {
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
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return false;
    }
    
    public static List<Discount> getAvailableDiscounts(String customerId) {
        String query = "SELECT da.discount_code " +
                       "FROM Discount_Account da " +
                       "JOIN Discounts d ON da.discount_code = d.discount_code " +
                       "WHERE da.customer_id = ? " +
                       "AND da.used_on IS NULL " +
                       "AND d.is_active = TRUE " +
                       "AND d.end_date >= CURRENT_DATE";

        List<Discount> availableDiscounts = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

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
                availableDiscounts.add(discount);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return availableDiscounts;
    }
    
    public static boolean isDiscountAvailable(String movieId, String customerId) {
        String query = 
            "SELECT COUNT(*) AS discount_count"
            + "FROM Discounts d"
            + "LEFT JOIN Discount_Movie dm ON d.discount_code = dm.discount_code"
            + "LEFT JOIN Discount_Account da ON d.discount_code = da.discount_code"
            + "WHERE d.is_active = TRUE"
            +    "AND d.start_date <= ?"
            +    "AND d.end_date >= ?"
            +    "AND (d.apply_for_what = 'GLOBAL'" 
            +       "OR (d.apply_for_what = 'SPECIFIC_MOVIES' AND dm.movie_id = ?)"
            +       "OR (d.apply_for_what = 'CART_TOTAL') -- Extend logic for cart-level discounts"
            +       "OR (d.apply_for_what = 'GENRE') -- Extend logic for genre-based discounts"
            +  ")"
            +  "AND (d.apply_for_who = 'ALL_USERS'"
            +       "OR (d.apply_for_who = 'SPECIFIC_USERS' AND da.customer_id = ?)"
            +       "OR (d.apply_for_who = 'PREMIUM' AND EXISTS ("
            +            "SELECT 1 FROM Accounts a WHERE a.account_id = ? AND a.role = 'PREMIUM'"
            +       "))"
            +  ");"
        ;

        try (Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            LocalDate currentDate = LocalDate.now();

            // Set query parameters
            statement.setDate(1, Date.valueOf(currentDate));
            statement.setDate(2, Date.valueOf(currentDate));
            statement.setString(3, movieId);
            statement.setString(4, customerId);
            statement.setString(5, customerId);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("discount_count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public static String getMovieDiscountForUser(String movieId, String customerId) {
        String query = 
            "SELECT d.discount_code, d.discount_type, d.discount_value, d.description"
            +"FROM Discounts d"
            +"LEFT JOIN Discount_Movie dm ON d.discount_code = dm.discount_code"
            +"LEFT JOIN Discount_Account da ON d.discount_code = da.discount_code"
            +"WHERE d.is_active = TRUE"
              +"AND d.start_date <= ?"
              +"AND d.end_date >= ?"
              +"AND (d.apply_for_what = 'GLOBAL'" 
                   +"OR (d.apply_for_what = 'SPECIFIC_MOVIES' AND dm.movie_id = ?)"
                   +"OR (d.apply_for_what = 'CART_TOTAL') -- Extend logic for cart-level discounts"
                    +"OR (d.apply_for_what = 'GENRE') -- Extend logic for genre-based discounts"
            +")"
            +"AND (d.apply_for_who = 'ALL_USERS'"
            +    "OR (d.apply_for_who = 'SPECIFIC_USERS' AND da.customer_id = ?)"
               +   "OR (d.apply_for_who = 'PREMIUM' AND EXISTS ("
               +         "SELECT 1 FROM Accounts a WHERE a.account_id = ? AND a.role = 'PREMIUM'"
               +    "))"
              + ");"
           ;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            LocalDate currentDate = LocalDate.now();

            // Set query parameters
            statement.setDate(1, Date.valueOf(currentDate));
            statement.setDate(2, Date.valueOf(currentDate));
            statement.setString(3, movieId);
            statement.setString(4, customerId);
            statement.setString(5, customerId);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String discountCode = resultSet.getString("discount_code");

                return discountCode;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return null if no discount is available
        return null;
    }

}
