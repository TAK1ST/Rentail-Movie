package main.dto;

import main.base.Model;

public class Profile extends Model {

    private String user_id;
    private String fullName;
    private String phone_number;
    private String address;
    private String credit;

    //Constructors
    public Profile(String id, String user_id, String fullName, String phone_number, String address, String credit) {
        super(id);
        this.user_id = user_id;
        this.fullName = fullName;
        this.phone_number = phone_number;
        this.address = address;
        this.credit = credit;
    }


    public Profile(Profile other) {
        super(other.getId());
        this.user_id = other.user_id;
        this.fullName = other.fullName;
        this.phone_number = other.phone_number;
        this.address = other.address;
        this.credit = other.credit;
    }

    //Methods
    @Override
    public String toString() {
        return String.format("User: %s, %s, %s, %s, %s.",
                super.getId(),
                fullName,
                phone_number,
                address,
                credit);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            fullName,
            phone_number,
            address,
            credit
        };
    }

    public static String className() {
        return "User";
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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
