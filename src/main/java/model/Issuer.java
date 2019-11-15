package model;

import java.io.Serializable;

public class Issuer implements Serializable {
    private int issuerId;
    private String forename;
    private String surname;
    private String phoneNumber;

    public int getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(int issuerId) {
        this.issuerId = issuerId;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Issuer{" +
                "issuerId=" + issuerId +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
