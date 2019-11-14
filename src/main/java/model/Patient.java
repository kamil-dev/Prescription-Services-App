package model;

public class Patient {
    private int parientId;
    private String foreame;
    private String surname;

    public int getParientId() {
        return parientId;
    }

    public void setParientId(int parientId) {
        this.parientId = parientId;
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
}
