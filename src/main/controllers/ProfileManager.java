
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.dao.ProfileDAO;
import main.constants.AccRole;
import static main.constants.Constants.PROFILE_PREFIX;
import static main.controllers.Managers.getMVM;
import main.dto.Account;
import main.dto.Profile;
import static main.utils.Input.getDouble;
import static main.utils.Input.getString;
import main.utils.Validator;
import static main.utils.Validator.getDate;
import static main.utils.Validator.getName;
import static main.utils.Validator.getPhoneNumber;

/**
 *
 * @author trann
 */
public class ProfileManager extends ListManager<Profile> {
      
    public ProfileManager() throws IOException {
        super(Profile.className());
        list = ProfileDAO.getAllProfiles();
    }

    public boolean addProfile(String accountID) throws IOException {   
        list.add(new Profile(
                accountID, 
                getName("Enter username", false), 
                getPhoneNumber("Enter your phone number", false), 
                getString("Enter your address", false),
                0,
                getDate("Enter your birthday", false)
        ));
        return ProfileDAO.addProfileToDB(list.getLast());
    }

    public boolean updateProfile(String userID) {
        if (checkEmpty(list)) return false;

        Profile foundProfile = null;
        if (userID.isEmpty()) {
            foundProfile = (Profile)getById("Enter user's id");
        } else {
            foundProfile = (Profile)searchById(userID);
        }        
        if (checkNull(foundProfile)) return false;

        String newFullName = getName("Enter full name", true);
        String newAddress = getString("Enter address", true); 
        String newPhoneNumber = getPhoneNumber("Enter phone number", true);
        double newCredit = getDouble("Enter credit", 0, Double.MAX_VALUE, true);
        LocalDate newBirthday = getDate("Enter birthday", true);

        if (!newFullName.isEmpty()) foundProfile.setFullName(newFullName);
        if (!newAddress.isEmpty()) foundProfile.setAddress(newAddress);
        if (!newPhoneNumber.isEmpty()) foundProfile.setPhoneNumber(newPhoneNumber);
        if (newCredit > 0) foundProfile.setCredit(newCredit);
        if (newBirthday != null) foundProfile.setBirthday(newBirthday);
         

        
        return ProfileDAO.updateProfileInDB(foundProfile);
    }

    public boolean deleteProfile() throws IOException { 
        if (checkEmpty(list)) return false;

        Profile foundProfile = (Profile)getById("Enter user's id");
        if (checkNull(foundProfile)) return false;

        list.remove(foundProfile);
        return ProfileDAO.deleteProfileFromDB(foundProfile.getId());
    }

    public void searchProfile() {
        display(getProfileBy("Enter any user's propety"), "Search Results");
    }

    public List<Profile> getProfileBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<Profile> searchBy(String propety) {
        List<Profile> result = new ArrayList<>();
        for (Profile item : list) 
            if (item.getId().equals(propety)
                    || (item.getFullName()      != null && propety.trim().toLowerCase().contains(item.getFullName().trim().toLowerCase())) 
                    || (item.getPhoneNumber()   != null && item.getPhoneNumber().equals(propety))
                    || (item.getAddress()       != null && item.getAddress().trim().toLowerCase().contains(propety))
                    || (item.getBirthday()      != null && item.getBirthday().format(Validator.DATE).contains(propety.trim()))
                    || String.valueOf(item.getCredit()).equals(propety)
            ) result.add(item);
        
        return result;
    }
    
//    @Override
//    public void display(List<Profile> users, String title) {
//        if (checkEmpty(list)) return;
//        
//        System.out.println(title);
//        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
//        System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s | %-20s | %-15s | %-20s |\n", 
//                "Profile ID", "Profilename","Password", "Role", "Full Name", "Address", "Phone Number", "Email");
//        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
//        for (Profile user : users) {
//            String role = user.getRole() == AccRole.ADMIN ? "Admin" : "Profile";
//             System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s | %-20s | %-15s | %-20s |\n", 
//                    user.getId(),
//                    user.getProfilename(),
//                    user.getPassword(),
//                    role,
//                    user.getFullName() != null ? user.getFullName() : "N/A",
//                    user.getAddress() != null ? user.getAddress() : "N/A",
//                    user.getPhoneNumber() != null ? user.getPhoneNumber() : "N/A",
//                    user.getEmail() != null ? user.getEmail() : "N/A"); 
//        }
//        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
//    }
    
}