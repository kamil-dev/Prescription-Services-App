package model;

import java.io.Serializable;
import java.util.Objects;

public class Medicine implements Serializable {
    private long medicineId;
    private String name;
    private String description;

    public Medicine(long medicineId, String name, String description) {
        this.medicineId = medicineId;
        this.name = name;
        this.description = description;
    }

    public Medicine() {
    }

    public long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(long medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return medicineId == medicine.medicineId &&
                Objects.equals(name, medicine.name) &&
                Objects.equals(description, medicine.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicineId, name, description);
    }
}
