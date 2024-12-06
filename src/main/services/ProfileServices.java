/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import main.config.Database;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getPFM;
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
        myProfile = (Profile) getPFM().searchById(id);
    }
    
    public static void showMyProfile(Account account) {
        getACM().show(account, "My Profile");
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
        
        if (updateUserCredit(account.getId(), credit)) {
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
    
    private static boolean updateUserCredit(String customerId, double creditAmount) {
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
    
}
