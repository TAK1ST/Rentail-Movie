package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Account;
import main.config.Database;
import main.constants.AccRole;
import main.constants.AccStatus;

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
                + "updated_at"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        String sql = "SELECT * FROM Accounts";
        List<Account> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Account account = new Account(
                        resultSet.getString("account_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        AccRole.valueOf(resultSet.getString("role")),
                        AccStatus.valueOf(resultSet.getString("status")),
                        resultSet.getDate("created_at") != null ? resultSet.getDate("created_at").toLocalDate() : null,
                        resultSet.getDate("updated_at") != null ? resultSet.getDate("updated_at").toLocalDate() : null,
                        resultSet.getDate("online_at") != null ? resultSet.getDate("online_at").toLocalDate() : null

                );
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
}
