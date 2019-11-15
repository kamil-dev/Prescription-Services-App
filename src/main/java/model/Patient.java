package model;

import java.io.Serializable;

public class Patient implements Serializable {
    private int patientId;
    private String forename;
    private String surname;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
