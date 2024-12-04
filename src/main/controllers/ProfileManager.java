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
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
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

    public boolean add(Profile profile) { 
        if (checkNull(profile) || checkNull(list)) return false;
        
        list.add(profile);
        return ProfileDAO.addProfileToDB(profile);
    }

    public boolean update(Profile profile) {
        if (checkNull(profile) || checkNull(list)) return false;

        Profile newProfile = getInputs(new boolean[] {true, true, true, true}, profile);
        if (newProfile != null)
            profile = newProfile;
        else 
            return false;
        return ProfileDAO.updateProfileInDB(newProfile);
    }

    public boolean delete(Profile profile) { 
        if (checkNull(profile) || checkNull(list)) return false;     

        if (!list.remove(profile)) {
            errorLog("Profile not found");
            return false;
        }
        return ProfileDAO.deleteProfileFromDB(profile.getId());
    }
    
    @Override
    public Profile getInputs(boolean[] options, Profile oldData) {
        if (options == null) {
            options = new boolean[] {true, true, true, true};
        }
        if (options.length < 4) {
            errorLog("Not enough option length");
            return null;
        }
        
        Account foundAccount = (Account) getACM().searchById(oldData.getId());
        if (getACM().checkNull(foundAccount)) return null;
        
        String name = null, phoneNumber = null, address = null;
        LocalDate birthday = null;
        
        if (oldData != null) {
            name = oldData.getFullName();
            phoneNumber = oldData.getPhoneNumber();
            address = oldData.getAddress();
            birthday = oldData.getBirthday();
        }
        
        if (options[0]) {
            name = getName("Enter full name", name);
            if (name == null) return null;
        }
        if (options[1]) {
            phoneNumber = getPhoneNumber("Enter your phone number", phoneNumber);
            if (phoneNumber == null) return null;
        }
        if (options[2]) {
            address = getString("Enter your address", address);
            if (address == null) return null;
        }
        if (options[3]) {
            birthday = getDate("Enter your birthday", birthday);
            if (birthday == null) return null;
        }
        
        return new Profile(
                oldData.getId(), 
                name, 
                phoneNumber, 
                address,
                0,
                birthday
        );
    }
    
    @Override
    public List<Profile> searchBy(String propety) {
        List<Profile> result = new ArrayList<>();
        for (Profile item : list) {
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
    public List<Profile> sortList(List<Profile> tempList, String property) {
        if (checkNull(tempList)) return null;
        
        if (property == null) return tempList;
        
        String[] options = Profile.getAttributes();
        List<Profile> result = new ArrayList<>(tempList);

        if (property.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Profile::getAccountId));
        } else if (property.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Profile::getFullName));
        } else if (property.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Profile::getBirthday));
        } else if (property.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Profile::getAddress));
        } else if (property.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Profile::getPhoneNumber));
        } else if (property.equalsIgnoreCase(options[5])) {
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
