// CustomerProfile.java
package main.models;

import base.Model;

public class CustomerProfile extends Model {
    private Users userId;
    private String fullName;
    private String address;
    private int phoneNumber;

    public CustomerProfile(int profileId, Users userId, String fullName, String address, int phoneNumber) {
        super(profileId);
        this.userId = userId;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getProfileId() {
        return super.getId();
    }

    public void setProfileId(int profileId) {
        super.setId(profileId);
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[] {
                getId(),
                userId != null ? userId.getId() : null,
                fullName,
                address,
                phoneNumber
        };
    }

    @Override
    public String toString() {
        return String.format("CustomerProfile[profileId=%s, userId=%s, fullName=%s, address=%s, phoneNumber=%s]",
                getId(), userId != null ? userId.getId() : null, fullName, address, phoneNumber);
    }
}
