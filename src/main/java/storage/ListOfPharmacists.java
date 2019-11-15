package storage;

import model.Pharmacist;

import java.util.ArrayList;
import java.util.List;

public class ListOfPharmacists {
    public static List<Pharmacist> pharmacists = new ArrayList<>();

    public static Pharmacist getPharmacist(int pharmacistId, String password){
        for (Pharmacist p : pharmacists){
            if (p.getPassword().equals(password) && p.getPharmacistId() == pharmacistId) return p;
        }
        return null;
    }

    public static void addRealizedPrescription(int pharmacistId, String password, int prescriptionId){
        for (Pharmacist p : pharmacists){
            if (p.getPassword().equals(password) && p.getPharmacistId() == pharmacistId)
                p.addRealizedPrescriptionId(prescriptionId);
        }
    }

    public static void addPharmacist(Pharmacist p){
        pharmacists.add(p);
    }

    public static void loadList(List<Pharmacist> loadedPharmacists){
        pharmacists = loadedPharmacists;
    }
}
