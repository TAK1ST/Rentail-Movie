package main.controllers;

import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
        copy(ProfileDAO.getAllProfiles()); 
    }
    
    public boolean addProfile(String accountID) {
        if (accountID == null) 
            accountID = getString("Enter account's id", null);
        if (accountID == null) return false;
        
        Account account = (Account) getACM().searchById(accountID);
        if (getACM().checkNull(account)) return false;
        
        String name = getName("Enter name", null);
        if (name == null) return false;
        
        String phoneNumber = getString("Enter phone number", null);
        if (phoneNumber == null) return false;
        
        String address = getString("Enter address", null);
        if (address == null) return false;
        
        LocalDate birthday = getDate("Enter birthday", null);
        if (birthday == null) return false;
        
        Profile profile = new Profile(
                accountID, 
                name, 
                phoneNumber,
                address,
                0f,
                birthday
        );
        return add(profile);
    }
    
    public boolean updateProfile(Profile profile) {
        if (checkNull(list)) return false;
        
        if (profile == null)
            profile = (Profile) getById("Enter profile's id");
        if (checkNull(profile)) return false;
        
        Profile temp = new Profile();
        temp.setFullName(getName("Enter full name", profile.getFullName()));
        temp.setPhoneNumber(getPhoneNumber("Enter phone number", profile.getPhoneNumber()));
        temp.setAddress(getString("Enter address", profile.getAddress()));
        temp.setBirthday(getDate("Enter birthday", profile.getBirthday()));
        temp.setCredit(getDouble("Enter credit", 0f, Double.MAX_VALUE, profile.getCredit()));
        
        return update(profile, temp);
    }
    
    public boolean deleteProfile(Profile profile) {
        if (checkNull(list)) return false;
        if (profile == null) 
            profile = (Profile) getById("Enter profile's id");
        if (checkNull(profile)) return false;
        return delete(profile);
    }
    
    public boolean add(Profile profile) {
        if (profile == null) return false;
        return ProfileDAO.addProfileToDB(profile) && list.add(profile);
    }

    public boolean update(Profile oldProfile, Profile newProfile) {
        if (newProfile == null || checkNull(list)) return false;
        if (ProfileDAO.updateProfileInDB(newProfile))
            oldProfile = newProfile;
        return true;
    }
    
    public boolean delete(Profile profile) {
        if (profile == null) return false;     
        return ProfileDAO.deleteProfileFromDB(profile.getId()) && list.remove(profile);
    }
    
    @Override
    public List<Profile> searchBy(List<Profile> tempList, String propety) {
        if (checkNull(tempList)) return null;
        
        List<Profile> result = new ArrayList<>();
        for (Profile item : tempList) {
            if (item == null)
                continue;
            if ((item.getId() != null && item.getId().equals(propety))
                    || (item.getFullName()      != null && propety.trim().toLowerCase().contains(item.getFullName().trim().toLowerCase())) 
                    || (item.getPhoneNumber()   != null && item.getPhoneNumber().equals(propety))
                    || (item.getAddress()       != null && item.getAddress().trim().toLowerCase().contains(propety))
                    || (item.getBirthday()      != null && item.getBirthday().format(Validator.DATE).contains(propety.trim()))
                    || String.valueOf(item.getCredit()).equals(propety)) 
            {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Profile> sortList(List<Profile> tempList, String propety) {
        if (checkNull(tempList)) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Profile.getAttributes();
        List<Profile> result = new ArrayList<>(tempList);

        if (propety.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Profile::getAccountId));
        } else if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Profile::getFullName));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Profile::getBirthday));
        } else if (propety.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Profile::getAddress));
        } else if (propety.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Profile::getPhoneNumber));
        } else if (propety.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Profile::getCredit));
        } else {
            result.sort(Comparator.comparing(Profile::getAccountId)); // Default case
        }
        return result;
    }
    
    @Override
    public void show(List<Profile> tempList) {
        if (checkNull(tempList)) return;
        
        InfosTable.getTitle(Profile.getAttributes());
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getAccountId(), 
                        item.getFullName(),
                        item.getPhoneNumber(),
                        item.getAddress(),
                        item.getCredit(),
                        formatDate(item.getBirthday(), Validator.DATE)
                );
            }
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                        item.getAccountId(), 
                        item.getFullName(),
                        item.getPhoneNumber(),
                        item.getAddress(),
                        item.getCredit(),
                        formatDate(item.getBirthday(), Validator.DATE)
                );
            }
        );
        InfosTable.showFooter();
    }
}
