// CustomerProfile.java
package main.models;

import base.Model;

public class UserInfo extends Model {
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;

    public UserInfo(String id, String fullName, String address, String phoneNumber, String email) {
        super(id);
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    public UserInfo(UserInfo other) {
        super(other.getId());
        this.fullName = other.fullName;
        this.address = other.address;
        this.phoneNumber = other.phoneNumber;
        this.email = other.email;
    }
    
    @Override
    public String toString() {
        return String.format("UserInfo: %s, %s, %s, %s, %s.", super.getId(), fullName, address, phoneNumber, email);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[] {
            super.getId(),
            fullName,
            address,
            phoneNumber,
            email,
        };
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
