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

/**
 *
 * @author trann
 */
public class CustomerServices {
    
    public static void showMyProfile(Account account) {
        getACM().show(account, "My Profile");
    }
    
    public static boolean updateMyProfile(String accountID) {
        Profile myProfile = (Profile) getPFM().searchById(accountID);
        if (getPFM().checkNull(myProfile)) return false;
        
        return getPFM().updateProfile(myProfile);
    }
    
    public static boolean registorCredit(Account account) {
        Profile profile = (Profile)getPFM().searchById(account.getId());
        if (getPFM().checkNull(profile)) return false;
        
        Profile temp = new Profile(profile);
        double credit = getDouble("Amount to registor", 0, Double.MAX_VALUE, Double.MIN_VALUE);
        if (credit == Double.MIN_VALUE) return false;
        
        profile.setCredit(profile.getCredit()  + credit);
        
        return getPFM().update(profile, temp);
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
    
}
