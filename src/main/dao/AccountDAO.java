package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Account;
import main.config.Database;

/**
 *
 * @author kiet
 */
public class AccountDAO {
    public static boolean addAccountToDB(Account account) {
        String sql = "INSERT INTO Accounts (user_id, email, password, username, role, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, account.getId());  
            preparedStatement.setString(2, account.getEmail());  
            preparedStatement.setString(3, account.getPassword());  
            preparedStatement.setString(4, account.getAccountName());  
            preparedStatement.setInt(5, account.getRole());  
            preparedStatement.setInt(6, account.getStatus());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateAccountFromDB(Account account) {
        String sql = "UPDATE Accounts SET email = ?, password = ?, username = ?, role = ?, status = ? WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, account.getEmail());  
            preparedStatement.setString(2, account.getPassword());  
            preparedStatement.setString(3, account.getAccountName());  
            preparedStatement.setInt(4, account.getRole());  
            preparedStatement.setInt(5, account.getStatus());  
            preparedStatement.setString(6, account.getId());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteAccountFromDB(String userID) {
        String sql = "DELETE FROM Accounts WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Account> getAllAccount() {
        String sql = "SELECT * FROM Accounts";
        List<Account> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Account account = new Account(
                    resultSet.getString("user_id"),  
                    resultSet.getString("email"),  
                    resultSet.getString("password"),  
                    resultSet.getString("username"),  
                    resultSet.getInt("role"),  
                    resultSet.getInt("status")  
                );
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
