package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Account;
import main.config.Database;
import main.constants.AccRole;
import main.constants.AccStatus;

/**
 *
 * @author kiet
 */
public class AccountDAO {

    public static boolean addAccountToDB(Account account) {
        String sql = "INSERT INTO Accounts (account_id, email, password, username, role, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, account.getId());
            preparedStatement.setString(2, account.getEmail());
            preparedStatement.setString(3, account.getPassword());
            preparedStatement.setString(4, account.getUsername());
            preparedStatement.setString(5, account.getRole().name());
            preparedStatement.setString(6, account.getStatus().name());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateAccountInDB(Account account) {
        String sql = "UPDATE Accounts SET email = ?, password = ?, username = ?, role = ?, status = ? WHERE account_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, account.getEmail());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setString(3, account.getUsername());
            preparedStatement.setString(4, account.getRole().name());
            preparedStatement.setString(5, account.getStatus().name());
            preparedStatement.setString(6, account.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAccountFromDB(String userID) {
        String sql = "DELETE FROM Accounts WHERE account_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Account> getAllAccounts() {
        String sql = "SELECT * FROM Accounts";
        List<Account> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Account account = new Account(
                        resultSet.getString("account_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        AccRole.valueOf(resultSet.getString("role")),
                        AccStatus.valueOf(resultSet.getString("status"))
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
        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, accountID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
