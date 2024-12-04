/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getPFM;
import static main.controllers.Managers.getPMM;
import main.dao.ProfileDAO;
import main.dto.Account;
import main.dto.Payment;
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
    
    public static boolean registorCredit(Account account) {
        Profile profile = (Profile)getPFM().searchById(account.getId());
        if (getPFM().checkNull(profile)) return false;
        
        double credit = getDouble("Amount to registor", 0, Double.MAX_VALUE, Double.MIN_VALUE);
        if (credit == Double.MIN_VALUE) return false;
        
        
        if (getPMM().add(new Payment(account.getId(), credit))) {
            profile.setCredit(profile.getCredit() + credit);
            return ProfileDAO.updateProfileInDB(profile);
        }
        return false;
    }
    
    public static boolean deleteAccount(Account account) {
        Profile profile = (Profile)getPFM().searchById(account.getId());
        if (getPFM().checkNull(profile)) return false;
        
        if (getPFM().delete(profile)) {
            return getACM().delete(account);
        }
        return false;
    }
    
}
