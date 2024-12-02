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
import static main.utils.Input.getDouble;
import static main.utils.Input.getString;
import main.utils.Validator;
import static main.utils.Validator.getDate;
import static main.utils.Validator.getName;
import static main.utils.Validator.getPhoneNumber;


public class ProfileManager extends ListManager<Profile> {
      
    public ProfileManager() {
        super(Profile.className());
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

        List<Profile> result = new ArrayList<>(tempList);
        switch (property) {
            case "accountId":
                result.sort(Comparator.comparing(Profile::getAccountId));
                break;
            case "fullName":
                result.sort(Comparator.comparing(Profile::getFullName));
                break;
            case "birthday":
                result.sort(Comparator.comparing(Profile::getBirthday));
                break;
            case "address":
                result.sort(Comparator.comparing(Profile::getAddress));
                break;
            case "phoneNumber":
                result.sort(Comparator.comparing(Profile::getPhoneNumber));
                break;
            case "credit":
                result.sort(Comparator.comparing(Profile::getCredit));
                break;
            default:
                result.sort(Comparator.comparing(Profile::getAccountId)); // Default to accountId
                break;
        }
        return result;
    }
    
    @Override
    public void display(List<Profile> tempList) {
        if (checkNull(tempList)) {
            return;
        } 
        
        int nameL = "Full name".length();
        int addressL = "Address".length();
        for (Profile item : list) {
            nameL = Math.max(nameL, item.getFullName().length());
            addressL = Math.max(addressL, item.getAddress().length());
        }
        
        int widthLength = 8 + nameL + 10 + addressL + 10 + 4 + 19;
         for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-" + nameL + "s | %-10s | %-" + addressL + "s | %-10s | %-6s |",
                "ID", "Full Name", "Birthday" , "Address" , "PhoneNumber" , "Credit");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Profile item : tempList) {
        System.out.printf("\n| %-8s | %-" + nameL + "s | %-10s | %-" + addressL + "s | %-10s | %-4.2f |",
                    item.getId(),
                    item.getFullName(),
                    item.getBirthday(),
                    item.getAddress(),
                    item.getPhoneNumber(),
                    item.getCredit());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
}
