package main.controllers;


import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static main.controllers.Managers.getACM;
import main.dao.ProfileDAO;
import main.dto.Account;
import main.dto.Profile;
import main.utils.InfosTable;
import static main.utils.Input.getDouble;
import static main.utils.Input.getString;
import static main.utils.Utility.formatDate;
import main.utils.Validator;
import static main.utils.Validator.getDate;
import static main.utils.Validator.getName;
import static main.utils.Validator.getPhoneNumber;


public class ProfileManager extends ListManager<Profile> {
      
    public ProfileManager() {
        super(Profile.className(), Profile.getAttributes());
        list = ProfileDAO.getAllProfiles();
    }

    public boolean addProfile(String accountID) { 
        
        Account foundAccount = (Account) getACM().searchById(accountID);
        if (getACM().checkNull(foundAccount)) return false;
        
        String name = getName("Enter full name", false);
        if (name.isEmpty()) return false;
        
        String phoneNumber = getPhoneNumber("Enter your phone number", false);
        if (phoneNumber.isEmpty()) return false;
        
        String address = getString("Enter your address", false);
        if (address.isEmpty()) return false;
        
        LocalDate birthday = getDate("Enter your birthday", false);
        if (birthday == null) return false;
        
        list.add(new Profile(
                accountID, 
                name, 
                phoneNumber, 
                address,
                0,
                birthday
        ));
        return ProfileDAO.addProfileToDB(list.getLast());
    }

    public boolean updateProfile(String userID) {
        if (checkNull(list)) return false;

        Profile foundProfile;
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

    public boolean deleteProfile() { 
        if (checkNull(list)) return false;

        Profile foundProfile = (Profile)getById("Enter user's id");
        if (checkNull(foundProfile)) return false;

        list.remove(foundProfile);
        return ProfileDAO.deleteProfileFromDB(foundProfile.getId());
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

    @Override
    public List<Profile> sortList(List<Profile> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }
        String[] options = Profile.getAttributes();
        List<Profile> result = new ArrayList<>(tempList);

        if (property.equals(options[0])) {
            result.sort(Comparator.comparing(Profile::getAccountId));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Profile::getFullName));
        } else if (property.equals(options[2])) {
            result.sort(Comparator.comparing(Profile::getBirthday));
        } else if (property.equals(options[3])) {
            result.sort(Comparator.comparing(Profile::getAddress));
        } else if (property.equals(options[4])) {
            result.sort(Comparator.comparing(Profile::getPhoneNumber));
        } else if (property.equals(options[5])) {
            result.sort(Comparator.comparing(Profile::getCredit));
        } else {
            result.sort(Comparator.comparing(Profile::getAccountId)); // Default case
        }
        return result;
    }
    
    @Override
    public void show(List<Profile> tempList) {
        if (checkNull(tempList)) {
            return;
        } 
        
        InfosTable.getTitle(Profile.getAttributes());
        tempList.forEach(item -> 
                InfosTable.calcLayout(
                        item.getAccountId(), 
                        item.getFullName(),
                        item.getPhoneNumber(),
                        item.getAddress(),
                        item.getCredit(),
                        formatDate(item.getBirthday(), Validator.DATE)
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
                InfosTable.displayByLine(
                        item.getAccountId(), 
                        item.getFullName(),
                        item.getPhoneNumber(),
                        item.getAddress(),
                        item.getCredit(),
                        formatDate(item.getBirthday(), Validator.DATE)
                )
        );
        InfosTable.showFooter();
    }
}
