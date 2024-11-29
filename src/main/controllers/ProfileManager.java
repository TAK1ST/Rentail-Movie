
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.dao.ProfileDAO;
import main.constants.Constants;
import main.constants.AccRole;
import main.constants.AccStatus;
import main.dto.Profile;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.PassEncryptor.encryptPassword;
import main.utils.Validator;
import static main.utils.Validator.getDate;
import static main.utils.Validator.getFullName;
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

    public boolean addProfile(AccRole registorRole) throws IOException {   
        list.add(new Profile(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "U"), 
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

        String newProfilename = Validator.getProfilename("Enter new username", true, list);
        String newPassword = Validator.getPassword("Enter new password", true);
        
        AccRole newRole = AccRole.NONE;
        if (foundProfile.getRole() == AccRole.ADMIN)
            newRole = Validator.getRole("Enter new role", true);
        
        String newFullName = getString("Enter full name", true);
        String newAddress = getString("Enter your address", true); 
        String newPhoneNumber = Validator.getPhoneNumber("Enter your phone number", true);
        String newEmail = Validator.getEmail("Enter your email", true);

        if (!newProfilename.isEmpty()) foundProfile.setProfilename(newProfilename);
        if (!newPassword.isEmpty()) foundProfile.setPassword(encryptPassword(newPassword));
        if (newRole != AccRole.NONE) foundProfile.setRole(newRole);
        if (!newFullName.isEmpty()) foundProfile.setFullName(newFullName);
        if (!newAddress.isEmpty()) foundProfile.setAddress(newAddress);
        if (!newPhoneNumber.isEmpty()) foundProfile.setPhoneNumber(newPhoneNumber);
        if (!newEmail.isEmpty()) foundProfile.setEmail(newEmail);  

        ProfileDAO.updateProfileFromDB(foundProfile);
        return true;
    }

    public boolean deleteProfile() throws IOException { 
        if (checkEmpty(list)) return false;

        Profile foundProfile = (Profile)getById("Enter user's id");
        if (checkNull(foundProfile)) return false;

        list.remove(foundProfile);
        ProfileDAO.deleteProfileFromDB(foundProfile.getId());
        return true;
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
                    || String.valueOf(item.getRole()).equals(propety)
                    || (item.getProfilename()      != null && item.getProfilename().equals(propety)) 
                    || (item.getFullName()      != null && item.getFullName().equalsIgnoreCase(propety))
                    || (item.getPhoneNumber()   != null && item.getPhoneNumber().equals(propety))
                    || (item.getEmail()         != null && item.getEmail().equals(propety))
                    || (item.getAddress()       != null && item.getAddress().trim().toLowerCase().contains(propety))
            ) result.add(item);
        
        return result;
    }
    
    public void showMyProfile(String userID) {
        display(searchById(userID), "My Profile");
    }
    
    @Override
    public void display(List<Profile> users, String title) {
        if (checkEmpty(list)) return;
        
        System.out.println(title);
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s | %-20s | %-15s | %-20s |\n", 
                "Profile ID", "Profilename","Password", "Role", "Full Name", "Address", "Phone Number", "Email");
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        for (Profile user : users) {
            String role = user.getRole() == AccRole.ADMIN ? "Admin" : "Profile";
             System.out.printf("|%-15s | %-20s | %-20s | %-10s | %-20s | %-20s | %-15s | %-20s |\n", 
                    user.getId(),
                    user.getProfilename(),
                    user.getPassword(),
                    role,
                    user.getFullName() != null ? user.getFullName() : "N/A",
                    user.getAddress() != null ? user.getAddress() : "N/A",
                    user.getPhoneNumber() != null ? user.getPhoneNumber() : "N/A",
                    user.getEmail() != null ? user.getEmail() : "N/A"); 
        }
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
    }
    
}