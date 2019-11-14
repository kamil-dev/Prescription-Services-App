package model;

public class Issuer {
    private int issuerId;
    private String foreame;
    private String surname;
    private String phoneNumber;

    public int getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(int issuerId) {
        this.issuerId = issuerId;
    }

    public String getForeame() {
        return foreame;
    }

    public void setForeame(String foreame) {
        this.foreame = foreame;
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
}
