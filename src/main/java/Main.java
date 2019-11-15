import model.Pharmacist;
import model.Prescription;
import storage.ListOfPharmacists;
import storage.TemporaryPrescriptionStorage;

import java.io.*;
import java.util.List;

public class Main {
    private static final String PATH_TO_PRESCRIPTION_STORAGE = "src/main/java/storage/prescriptions.txt";
    private static final String PATH_TO_PHARMACISTS_STORAGE = "src/main/java/storage/pharmacists.txt";

    public static void main(String[] args) {

//        Pharmacist p = new Pharmacist(123,"qwerty","Anna","Nowak");
//        ListOfPharmacists.addPharmacist(p);
//
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH_TO_PHARMACISTS_STORAGE))) {
//            oos.writeObject(ListOfPharmacists.pharmacists);
//        } catch (FileNotFoundException e1){
//            System.err.println("File has not been found");
//            e1.printStackTrace();
//        } catch (IOException e2) {
//            System.err.println("Input & Output exception");
//            e2.printStackTrace();
//        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH_TO_PRESCRIPTION_STORAGE))) {
            TemporaryPrescriptionStorage.loadList((List<Prescription>)ois.readObject());
        } catch (FileNotFoundException e1) {
            System.err.println("File is not found");
        } catch (IOException e2) {
            System.err.println("IO exception");
        } catch (ClassNotFoundException e3) {
            System.err.println("Class could not have been found");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATH_TO_PHARMACISTS_STORAGE))) {
            ListOfPharmacists.loadList((List<Pharmacist>)ois.readObject());
        } catch (FileNotFoundException e1) {
            System.err.println("File is not found");
        } catch (IOException e2) {
            System.err.println("IO exception");
        } catch (ClassNotFoundException e3) {
            System.err.println("Class could not have been found");
        }


        try {
            new PrescriptionServicesApp(8081);
        } catch (IOException e) {
            System.err.println("Server can not start " + e);
            e.printStackTrace();
        }

    }
}
