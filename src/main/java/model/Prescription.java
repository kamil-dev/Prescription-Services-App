package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Prescription implements Serializable {
    private int prescriptionId;
    private Patient patient;
    private Medicine medicine;
    private Issuer issuer;
    private Date issueDate;
    private String comment;
    private int quantity; // quantity of medicine
    private int payment; //level of repayment in %'s
    private boolean isRealized;

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
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

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
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

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
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
                Objects.equals(issuer, that.issuer) &&
                Objects.equals(issueDate, that.issueDate) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prescriptionId, medicine, issuer, issueDate, comment, quantity, payment, isRealized);
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", patient=" + patient +
                ", medicine=" + medicine +
                ", issuer=" + issuer +
                ", issueDate=" + issueDate +
                ", comment='" + comment + '\'' +
                ", quantity=" + quantity +
                ", payment=" + payment +
                ", isRealized=" + isRealized +
                '}';
    }
}
