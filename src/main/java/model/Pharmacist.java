package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Pharmacist implements Serializable {
    private long pharmacistId;
    private String password;
    private String forename;
    private String surname;
    private Set<Integer> realizedPrescriptionIds;

    public Pharmacist(long pharmacistId, String password, String forename, String surname) {
        this.pharmacistId = pharmacistId;
        this.password = password;
        this.forename = forename;
        this.surname = surname;
        this.realizedPrescriptionIds = new HashSet<>();
    }

    public long getPharmacistId() {
        return pharmacistId;
    }

    public void setPharmacistId(int pharmacistId) {
        this.pharmacistId = pharmacistId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addRealizedPrescriptionId (int prescriptionId){
        realizedPrescriptionIds.add(prescriptionId);
    }


}
