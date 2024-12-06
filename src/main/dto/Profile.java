package main.dto;

import java.time.LocalDate;
import main.base.Model;

public class Profile extends Model {
    
    private String fullName;
    private String phoneNumber;
    private String address;
    private double credit;
    private LocalDate birthday;
    
    public Profile() {
    }
    
    public Profile(String accountID) {
        super(accountID);
    }
    
    public Profile(String accountID, String fullName, String phoneNumber, String address, double credit, LocalDate birthday) {
        super(accountID);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.credit = credit;
        this.birthday = birthday;
    }


    public Profile(Profile other) {
        super(other.getAccountId());
        this.fullName = other.fullName;
        this.phoneNumber = other.phoneNumber;
        this.address = other.address;
        this.credit = other.credit;
        this.birthday = other.birthday;
    }

    @Override
    public String toString() {
        String[] attr = getAttributes();
        int count = 0;
        return String.format(
                "\n[%s]:\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %.2f,\n"
                + "%s: %s.",
                className(),
                attr[count++], this.getAccountId(),
                attr[count++], fullName,
                attr[count++], phoneNumber,
                attr[count++], address,
                attr[count++], credit,
                attr[count++], birthday
        );
    }

    public static String className() {
        return "Profile";
    }
    
    public static String[] getAttributes() {
        return new String[] {"Id", "Full name", "Phone number", "Address", "Credit", "Birthday"};
    }

//    @Override
//    public String getId() {
//        try {
//            throw new MethodNotFound("Profile only has accountId instead of id");
//        } catch (MethodNotFound e) {
//            errorLog("Exception caught: " + e.getMessage());
//            return null;
//        }
//    }
//    
//    @Override
//    public void setId(String id) {
//        try {
//            throw new MethodNotFound("Profile only has accountId instead of id");
//        } catch (MethodNotFound e) {
//            errorLog("Exception caught: " + e.getMessage());
//        }
//    }
    
    public String getAccountId() {
        return super.getId();
    }
    
    public void setAccountId(String id) {
        super.setId(id);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
}
