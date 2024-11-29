package main.dto;

import main.base.Model;

public class Profile extends Model {
    
    private String fullName;
    private String phoneNumber;
    private String address;
    private String credit;

    public Profile(String accountID, String fullName, String phoneNumber, String address, String credit) {
        super(accountID);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.credit = credit;
    }


    public Profile(Profile other) {
        super(other.getId());
        this.fullName = other.fullName;
        this.phoneNumber = other.phoneNumber;
        this.address = other.address;
        this.credit = other.credit;
    }

    @Override
    public String toString() {
        return String.format("User: %s, %s, %s, %s, %s.",
                super.getId(),
                fullName,
                phoneNumber,
                address,
                credit);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            fullName,
            phoneNumber,
            address,
            credit
        };
    }

    public static String className() {
        return "User";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getphoneNumber() {
        return phoneNumber;
    }

    public void setphoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

}
