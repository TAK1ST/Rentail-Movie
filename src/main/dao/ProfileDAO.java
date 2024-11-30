/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.config.Database;
import main.dto.Profile;

/**
 *
 * @author tak
 */
public class ProfileDAO {
    
    public static boolean addProfileToDB(Profile account) {
        String sql = "INSERT INTO Profiles (account_id, full_name, birth_day, address, phone_number, credit) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, account.getId());  
            preparedStatement.setString(2, account.getFullName());  
            preparedStatement.setDate(3, Date.valueOf(account.getBirthday()));
            preparedStatement.setString(4, account.getAddress());
            preparedStatement.setString(5, account.getPhoneNumber());  
            preparedStatement.setDouble(6, account.getCredit());  
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateProfileInDB(Profile account) {
        String sql = "UPDATE Profiles SET full_name = ?, phone_number = ?, address = ?, credit = ? birth_day = ? WHERE account_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
 
            preparedStatement.setString(1, account.getFullName());  
            preparedStatement.setString(2, account.getPhoneNumber());  
            preparedStatement.setString(3, account.getAddress());  
            preparedStatement.setDouble(4, account.getCredit());   
            preparedStatement.setDate(5, Date.valueOf(account.getBirthday()));  
            preparedStatement.setString(6, account.getId());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteProfileFromDB(String userID) {
        String sql = "DELETE FROM Profiles WHERE account_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Profile> getAllProfiles() {
        String sql = "SELECT * FROM Profiles";
        List<Profile> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Profile account = new Profile(
                    resultSet.getString("account_id"),  
                    resultSet.getString("full_name"),  
                    resultSet.getString("phone_number"),  
                    resultSet.getString("address"),  
                    resultSet.getDouble("credit"),
                    resultSet.getDate("birth_day").toLocalDate()
                );
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
