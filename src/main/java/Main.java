import model.Pharmacist;
import model.Prescription;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            new PrescriptionServicesApp(8081);
        } catch (IOException e) {
            System.err.println("Server can not start " + e);
            e.printStackTrace();
        }

    }
}
