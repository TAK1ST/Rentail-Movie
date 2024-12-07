/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import main.config.Database;
import main.constants.account.AccStatus;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getPFM;
import main.dao.ProfileDAO;
import main.dto.Account;
import main.dto.Profile;
import static main.utils.Input.getDouble;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.successLog;

/**
 *
 * @author trann
 */
public class ProfileServices {
    
    private static Profile myProfile;
    
    public static void initDataFor(String id) {
        myProfile = ProfileDAO.getProfile(id);
    }
    
    public static void showMyProfile(Account account) {
        Profile profile = getPFM().searchById(account.getId());
        if (getPFM().checkNull(profile)) return;
        
        getACM().show(account);
        getPFM().show(profile);
    }
    
    public static boolean updateMyProfile() {
        if (getPFM().checkNull(myProfile)) return false;
        
        return getPFM().updateProfile(myProfile);
    }
    
    public static boolean registorCredit(Account account) {
        if (getPFM().checkNull(myProfile)) return false;
        
        Profile temp = new Profile(myProfile);
        double credit = getDouble("Amount to registor", 0, Double.MAX_VALUE, Double.MIN_VALUE);
        if (credit == Double.MIN_VALUE) return false;
        
        if (updateCustomerCredit(account.getId(), credit)) {
            myProfile.setCredit(myProfile.getCredit()  + credit);
            return successLog("Successfully registor credit", true);
        }
        return errorLog("Fail to registor credit", false);
    }
    
    public static boolean adjustAccountCreability(Account account, int amount) {
        String sql = "UPDATE Accounts SET creability = ? WHERE account_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setInt(++count, account.getCreability() + amount);
            ps.setString(++count, account.getId());

            if (ps.executeUpdate() > 0)
                account.setCreability(account.getCreability() + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static boolean updateCustomerCredit(String customerId, double creditAmount) {
        String query = "UPDATE Profiles SET credit = credit + ? WHERE account_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, creditAmount); 
            stmt.setString(2, customerId);    

            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateAccountStatus(Account account, AccStatus newStatus) {
        String query = "UPDATE Accounts SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE account_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newStatus!= null ? newStatus.name() : null);
            preparedStatement.setString(2, account.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                account.setStatus(newStatus);
                return true;
            } 
            else 
                return false;
        } catch (SQLException e) {
            System.err.println("Error updating user status: " + e.getMessage());
            return false;
        }
    }
    
}
