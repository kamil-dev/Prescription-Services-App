package storage;

import model.Prescription;

import java.util.ArrayList;
import java.util.List;

public class TemporaryPrescriptionStorage {
    private static final List<Prescription> PRESCRIPTIONS = new ArrayList<>();

    public static void addNewPrescription(Prescription p){
        PRESCRIPTIONS.add(p);
    }
}
