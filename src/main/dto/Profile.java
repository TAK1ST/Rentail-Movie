package main.dto;

import java.time.LocalDate;
import main.base.Model;
import main.utils.Validator;

public class Profile extends Model {
    
    private String fullName;
    private String phoneNumber;
    private String address;
    private double credit;
    private LocalDate birthday;

    public Profile(String accountID, String fullName, String phoneNumber, String address, double credit, LocalDate birthday) {
        super(accountID);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.credit = credit;
        this.birthday = birthday;
    }


    public Profile(Profile other) {
        super(other.getId());
        this.fullName = other.fullName;
        this.phoneNumber = other.phoneNumber;
        this.address = other.address;
        this.credit = other.credit;
        this.birthday = other.birthday;
    }

    @Override
    public String toString() {
        return String.format("User: %s, %s, %s, %s, %s, %s.",
                super.getId(),
                fullName,
                phoneNumber,
                address,
                credit,
                birthday.format(Validator.DATE));
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            fullName,
            phoneNumber,
            address,
            credit,
            birthday
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
