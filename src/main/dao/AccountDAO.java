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
    public static boolean addUserToDB(Account account) {
        String sql = "INSERT INTO Users (user_id, email, password, username, role, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, account.getId());  
            preparedStatement.setString(2, account.getEmail());  
            preparedStatement.setString(3, account.getPassword());  
            preparedStatement.setString(4, account.getAccountName());  
            preparedStatement.setString(5, account.getRole().name());  
            preparedStatement.setString(6, account.getStatus().name());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateUserFromDB(Account account) {
        String sql = "UPDATE Users SET email = ?, password = ?, username = ?, role = ?, status = ? WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, account.getEmail());  
            preparedStatement.setString(2, account.getPassword());  
            preparedStatement.setString(3, account.getAccountName());  
            preparedStatement.setString(4, account.getRole().name());  
            preparedStatement.setString(5, account.getStatus().name());  
            preparedStatement.setString(6, account.getId());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteUserFromDB(String userID) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Account> getAllUser() {
        String sql = "SELECT * FROM Users";
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
}
