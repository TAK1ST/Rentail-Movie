/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Users;
import main.utils.DatabaseUtil;
import static main.utils.PasswordEncryptor.encryptPassword;

/**
 *
 * @author trann
 */
public class UserDAO {
    public static boolean addUserToDB(Users user) {
        String sql = "INSERT INTO Users (user_id, username, password_hash, role, full_name, address, phone_number, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, encryptPassword(user.getPassword()));
            preparedStatement.setInt(4, user.getRole());
            preparedStatement.setString(5, user.getFullName());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getPhoneNumber());
            preparedStatement.setString(8, user.getEmail());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateUserFromDB(Users user) {
        String sql = "UPDATE Users SET username = ?, password = ?, role = ?, fullname = ?, address = ?, phoneNumber = ?, email = ? WHERE userId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, encryptPassword(user.getPassword()));
            preparedStatement.setInt(3, user.getRole());
            preparedStatement.setString(4, user.getFullName());
            preparedStatement.setString(5, user.getAddress());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setString(7, user.getEmail());
            preparedStatement.setString(8, user.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteUserFromDB(String userID) {
        String sql = "DELETE FROM User WHERE userId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Users> getAllUser() {
        String sql = "SELECT * FROM Users";
        List<Users> list = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Users user = new Users(
                    resultSet.getString("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password_hash"),
                    resultSet.getInt("role"),
                    resultSet.getString("full_name"),
                    resultSet.getString("address"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("email")
                );
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
