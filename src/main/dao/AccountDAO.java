package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.dto.Account;
import main.config.Database;
import main.constants.AccRole;
import main.constants.AccStatus;
import main.utils.IDGenerator;

public class AccountDAO {

    public static boolean addAccountToDB(Account account) {
        String sql = "INSERT INTO Accounts ("
                + "account_id,"
                + "username,"
                + "password,"
                + "email,"
                + "role,"
                + "status,"
                + "online_at,"
                + "created_at,"
                + "updated_at, "
                + "creability"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, account.getId());
            ps.setString(++count, account.getUsername());
            ps.setString(++count, account.getPassword());
            ps.setString(++count, account.getEmail());
            ps.setString(++count, account.getRole().name());
            ps.setString(++count, account.getStatus().name());
            ps.setDate(++count, account.getOnlineAt() != null ? Date.valueOf(account.getOnlineAt()) : null);
            ps.setDate(++count, account.getCreateAt() != null ? Date.valueOf(account.getCreateAt()) : null);
            ps.setDate(++count, account.getUpdateAt() != null ? Date.valueOf(account.getUpdateAt()) : null);
            ps.setInt(++count, account.getCreability());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateAccountInDB(Account account) {
        String sql = "UPDATE Accounts SET "
                + "username = ?,"
                + "password = ?, "
                + "email = ?, "
                + "role = ?, "
                + "status = ?,"
                + "created_at = ?,"
                + "updated_at = ?,"
                + "online_at = ?, "
                + "creability = ?"
                + "WHERE account_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, account.getUsername());
            ps.setString(++count, account.getPassword());
            ps.setString(++count, account.getEmail());
            ps.setString(++count, account.getRole().name());
            ps.setString(++count, account.getStatus().name());
            ps.setDate(++count, account.getCreateAt() != null ? Date.valueOf(account.getCreateAt()) : null);
            ps.setDate(++count, account.getUpdateAt() != null ? Date.valueOf(account.getUpdateAt()) : null);
            ps.setDate(++count, account.getOnlineAt() != null ? Date.valueOf(account.getOnlineAt()) : null);
            ps.setInt(++count, account.getCreability());
            ps.setString(++count, account.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAccountFromDB(String accountID) {
        String sql = "DELETE FROM Accounts WHERE account_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, accountID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Account> getAllAccounts() {
        boolean haveDefaultAdmin = false;
        
        String sql = "SELECT * FROM Accounts";
        List<Account> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (rs.getString("account_id").equals(IDGenerator.DEFAULT_ADMIN_ID)
                        && AccRole.valueOf(rs.getString("role")) == AccRole.ADMIN) {
                    haveDefaultAdmin = true;
                }
                
                String username = rs.getString("account_id");
                if (rs.getString("username").equals("admin") 
                    && !rs.getString("account_id").equals(IDGenerator.DEFAULT_ADMIN_ID)) 
                {
                    username = IDGenerator.generateDiscountCode();
                    username = updateUsernameInDB(rs.getString("account_id"), username) ? username : rs.getString("username");
                }
                Account account = new Account(
                        rs.getString("account_id"),
                        username,
                        rs.getString("password"),
                        rs.getString("email"),
                        AccRole.valueOf(rs.getString("role")),
                        AccStatus.valueOf(rs.getString("status")),
                        rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null,
                        rs.getDate("updated_at") != null ? rs.getDate("updated_at").toLocalDate() : null,
                        rs.getDate("online_at") != null ? rs.getDate("online_at").toLocalDate() : null,
                        rs.getInt("creability")
                );
                list.add(account);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!haveDefaultAdmin) {
            list.add(new Account(
                        IDGenerator.DEFAULT_ADMIN_ID,
                        "admin",
                        "1",
                        "admin@gmail.com",
                        AccRole.ADMIN,
                        AccStatus.OFFLINE,
                        LocalDate.now(),
                        null,
                        LocalDate.now(),
                        0
                ));
            addAccountToDB (list.getLast());
        }
        return list;
    }

    public static boolean updatePasswordInDB(String accountID, String newPassword) {
        String sql = "UPDATE Accounts SET password = ? WHERE account_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, accountID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateUsernameInDB(String accountID, String username) {
        String sql = "UPDATE Accounts SET username = ? WHERE account_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, accountID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
