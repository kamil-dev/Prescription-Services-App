package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class Prescription implements Serializable {
    private long prescriptionId;
    private Patient patient;
    private Medicine medicine;
    private Prescriptor prescriptor;
    private Date issueDate;
    private String comment;
    private int quantity; // quantity of medicine
    private double payment; //level of repayment in %'s
    private boolean isRealized;
    private Pharmacist pharmacist;

    public Pharmacist getPharmacist() {
        return pharmacist;
    }

    public void setPharmacist(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
    }

    public long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Prescriptor getPrescriptor() {
        return prescriptor;
    }

    public void setPrescriptor(Prescriptor prescriptor) {
        this.prescriptor = prescriptor;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public boolean isRealized() {
        return isRealized;
    }

    public void setRealized(boolean realized) {
        isRealized = realized;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return prescriptionId == that.prescriptionId &&
                quantity == that.quantity &&
                payment == that.payment &&
                isRealized == that.isRealized &&
                Objects.equals(medicine, that.medicine) &&
                Objects.equals(prescriptor, that.prescriptor) &&
                Objects.equals(issueDate, that.issueDate) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prescriptionId, medicine, prescriptor, issueDate, comment, quantity, payment, isRealized);
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", patient=" + patient +
                ", medicine=" + medicine +
                ", prescriptor=" + prescriptor +
                ", issueDate=" + issueDate +
                ", comment='" + comment + '\'' +
                ", quantity=" + quantity +
                ", payment=" + payment +
                ", isRealized=" + isRealized +
                '}';
    }
}
