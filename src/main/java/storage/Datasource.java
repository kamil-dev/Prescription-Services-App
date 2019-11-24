package storage;

import model.*;

import java.sql.*;

public class Datasource {

    static {
        try {
        Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
        System.err.println("Server couldn't find postgres Driver class: \n" + e);
    }
    }

    private static final String DB_NAME = "prescriptionsdb";
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String INSERT_PRESCRIPTION = "INSERT INTO prescriptions (prescriptionid, patientid, medicineid, prescriptorid, issuedate, comment, quantity, payment, isrealized, pharmacistid)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (prescriptionid) DO NOTHING;";
    private static final String INSERT_PRESCRIPTOR = "INSERT INTO prescriptors (prescriptorid, forename, surname, phonenumber) VALUES (?, ?, ?, ?) ON CONFLICT (prescriptorid) DO NOTHING;";
    private static final String INSERT_MEDICINE = "INSERT INTO medicines (medicineid, name, description) VALUES (?, ?, ?) ON CONFLICT (medicineid) DO NOTHING;";
    private static final String INSERT_PATIENT = "INSERT INTO patients (patientid, forename, surname) VALUES (?, ?, ?) ON CONFLICT (patientid) DO NOTHING;";
    private static final String INSERT_PHARMACIST = "INSERT INTO pharmacists (pharmacistid, password, forename, surname) VALUES (?, ?, ? ,?) ON CONFLICT (pharmacistid) DO NOTHING;";

    private static final String SELECT_PRESCRIPTION = "SELECT patients.patientid, medicines.medicineid, prescriptors.prescriptorid, prescriptions.issuedate, prescriptions.comment, prescriptions.quantity, prescriptions.payment, " +
            "prescriptions.isrealized, prescriptions.pharmacistid, patients.forename, patients.surname, medicines.name, medicines.description, prescriptors.forename as pforename, prescriptors.surname as psurname, prescriptors.phonenumber, prescriptions.pharmacistid FROM prescriptions " +
            "INNER JOIN patients ON patients.patientid = prescriptions.patientid INNER JOIN medicines ON prescriptions.medicineid = medicines.medicineid INNER JOIN prescriptors ON prescriptors.prescriptorid = prescriptions.prescriptorid WHERE prescriptions.prescriptionid = ? ;";

    private static final String SELECT_PHARMACIST = "SELECT * FROM pharmacists WHERE password = ? AND pharmacistid = ?;";

    private static final String UPDATE_PRESCRIPTION = "UPDATE prescriptions SET isrealized = ?, pharmacistid = ? WHERE prescriptionid = ?;";

    private Connection initializeDataBaseConnection(){
        try {
            return DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);

        } catch (SQLException e) {
            System.err.println("The connection couldn't be established");
        }
        return null;
    }

    private void closeDatabaseResources(Connection connection, Statement ... statement){

        try {
            for (Statement s : statement){
                if (s != null) s.close();
            }
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Error during closure of database resources: \n" + e);
        }
    }

    public void addPrescription(Prescription prescription) {

        Connection connection = initializeDataBaseConnection();

        PreparedStatement insertIntoPrescriptions = null;
        PreparedStatement insertIntoPrescribers = null;
        PreparedStatement insertIntoMedicines = null;
        PreparedStatement insertIntoPatients = null;

        try {
            insertIntoPrescribers = connection.prepareStatement(INSERT_PRESCRIPTOR);
            insertIntoPatients = connection.prepareStatement(INSERT_PATIENT);
            insertIntoMedicines = connection.prepareStatement(INSERT_MEDICINE);
            insertIntoPrescriptions = connection.prepareStatement(INSERT_PRESCRIPTION);

            Prescriptor prescriptor = prescription.getPrescriptor();
            Patient patient = prescription.getPatient();
            Medicine medicine = prescription.getMedicine();

            insertIntoPrescribers.setLong(1, prescriptor.getPrescriptorId());
            insertIntoPrescribers.setString(2, prescriptor.getForename());
            insertIntoPrescribers.setString(3, prescriptor.getSurname());
            insertIntoPrescribers.setString(4, prescriptor.getPhoneNumber());

            insertIntoPatients.setLong(1, patient.getPatientId());
            insertIntoPatients.setString(2, patient.getForename());
            insertIntoPatients.setString(3, patient.getSurname());

            insertIntoMedicines.setLong(1, medicine.getMedicineId());
            insertIntoMedicines.setString(2, medicine.getName());
            insertIntoMedicines.setString(3, medicine.getDescription());

            insertIntoPrescriptions.setLong(1, prescription.getPrescriptionId());
            insertIntoPrescriptions.setLong(2,prescription.getPatient().getPatientId());
            insertIntoPrescriptions.setLong(3,prescription.getMedicine().getMedicineId());
            insertIntoPrescriptions.setLong(4,prescription.getPrescriptor().getPrescriptorId());
            insertIntoPrescriptions.setDate(5,prescription.getIssueDate());
            insertIntoPrescriptions.setString(6,prescription.getComment());
            insertIntoPrescriptions.setInt(7,prescription.getQuantity());
            insertIntoPrescriptions.setDouble(8,prescription.getPayment());
            insertIntoPrescriptions.setBoolean(9, prescription.isRealized());
            insertIntoPrescriptions.setNull(10,Types.BIGINT);

            insertIntoMedicines.executeUpdate();
            insertIntoPatients.executeUpdate();
            insertIntoPrescribers.executeUpdate();
            insertIntoPrescriptions.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error during invoke of SQL query: " + e.getMessage());
            throw new RuntimeException("Error during invoke of SQL query");
        } finally {
            closeDatabaseResources(connection, insertIntoMedicines, insertIntoPatients, insertIntoPrescribers, insertIntoPrescriptions);
        }

    }

    public Prescription getPrescription(long prescriptionID){

        Connection connection = initializeDataBaseConnection();
        Prescription prescription = null;

        PreparedStatement selectPrescription = null;

        try {
            selectPrescription = connection.prepareStatement(SELECT_PRESCRIPTION);
            selectPrescription.setLong(1,prescriptionID);
            ResultSet resultSet = selectPrescription.executeQuery();

            Patient patient = null;
            Medicine medicine = null;
            Prescriptor prescriptor = null;

            while (resultSet.next()) {
                prescription = new Prescription();
                prescription.setPrescriptionId(prescriptionID);

                medicine = new Medicine();
                medicine.setDescription(resultSet.getString("description"));
                medicine.setMedicineId(resultSet.getLong("medicineid"));
                medicine.setName(resultSet.getString("name"));
                prescription.setMedicine(medicine);

                patient = new Patient();
                prescription.setPatient(patient);
                patient.setPatientId(resultSet.getLong("patientid"));
                patient.setForename(resultSet.getString("forename"));
                patient.setSurname(resultSet.getString("surname"));

                prescriptor = new Prescriptor();
                prescription.setPrescriptor(prescriptor);
                prescriptor.setPrescriptorId(resultSet.getLong("prescriptorid"));
                prescriptor.setForename(resultSet.getString("pforename"));
                prescriptor.setSurname(resultSet.getString("psurname"));
                prescriptor.setPhoneNumber(resultSet.getString("phonenumber"));

                prescription.setPrescriptionId(prescriptionID);
                prescription.setQuantity(resultSet.getInt("quantity"));
                prescription.setRealized(resultSet.getBoolean("isrealized"));
                prescription.setIssueDate(resultSet.getDate("issuedate"));
                prescription.setComment(resultSet.getString("comment"));
                prescription.setPayment(resultSet.getDouble("payment"));
            }

        } catch (SQLException e) {
            System.err.println("Error during invoke of SQL query: \n" + e.getMessage());
            throw new RuntimeException("Error during invoke of SQL query");

        } finally {
            closeDatabaseResources(connection, selectPrescription);
        }
        return prescription;
    }

    public Pharmacist getPharmacist(long pharmacistid, String password){

        Connection connection = initializeDataBaseConnection();
        Pharmacist pharmacist = null;

        PreparedStatement selectPharmacist = null;

        try {
            selectPharmacist = connection.prepareStatement(SELECT_PHARMACIST);
            selectPharmacist.setLong(2, pharmacistid);
            selectPharmacist.setString(1, password);
            ResultSet resultSet = selectPharmacist.executeQuery();

            if (resultSet.next()){
                pharmacist = new Pharmacist();
                pharmacist.setPharmacistId(pharmacistid);
                pharmacist.setPassword(password);
                pharmacist.setForename(resultSet.getString("forename"));
                pharmacist.setSurname(resultSet.getString("surname"));
            }

        } catch (SQLException e) {
            System.err.println("Error during invoke of SQL query: \n" + e.getMessage());
            throw new RuntimeException("Error during invoke of SQL query");
        } finally {
            closeDatabaseResources(connection, selectPharmacist);
        }

        return pharmacist;
    }

    public void updatePrescription(boolean isRealized, long pharmacistid, long prescriptionid){

        Connection connection = initializeDataBaseConnection();

        PreparedStatement updatePrescription = null;

        try {
            updatePrescription = connection.prepareStatement(UPDATE_PRESCRIPTION);
            updatePrescription.setBoolean(1, isRealized);
            updatePrescription.setLong(2,pharmacistid);
            updatePrescription.setLong(3, prescriptionid);

            updatePrescription.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error during invoke of SQL query: \n" + e.getMessage());
            throw new RuntimeException("Error during invoke of SQL query");
        } finally {
            closeDatabaseResources(connection, updatePrescription);
        }
    }










}
