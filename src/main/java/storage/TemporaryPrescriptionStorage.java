package storage;

import model.Pharmacist;
import model.Prescription;

import java.util.ArrayList;
import java.util.List;

public class TemporaryPrescriptionStorage {
    public static final List<Prescription> PRESCRIPTIONS = new ArrayList<>();

    public static void addNewPrescription(Prescription p){
        PRESCRIPTIONS.add(p);
    }

    public static Prescription getPrescription(int prescriptionId){
        for (Prescription p : PRESCRIPTIONS){
            if (p.getPrescriptionId() == prescriptionId) return p;
        }
        return null;
    }

    public static void loadList(List<Prescription> loadedPrescriptions){
        PRESCRIPTIONS.addAll(loadedPrescriptions);
    }


}
