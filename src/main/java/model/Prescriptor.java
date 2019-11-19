package model;

import java.io.Serializable;

public class Prescriptor implements Serializable {
    private long prescriptorId;
    private String forename;
    private String surname;
    private String phoneNumber;

    public long getPrescriptorId() {
        return prescriptorId;
    }

    public void setPrescriptorId(long prescriptorId) {
        this.prescriptorId = prescriptorId;
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
        return "Prescriptor{" +
                "issuerId=" + prescriptorId +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
