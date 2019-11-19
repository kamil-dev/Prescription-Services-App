package storage;

import model.*;

import java.sql.*;

public class Datasource {

    private static final String DB_NAME = "prescriptionsdb";
    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private Connection connection;

    private static final String INSERT_PRESCRIPTION = "INSERT INTO prescriptions (prescriptionid, patientid, medicineid, prescriptorid, issuedate, comment, quantity, payment, isrealized, pharmacistid)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String INSERT_PRESCRIPTOR = "INSERT INTO prescriptors (prescriptorid, forename, surname, phonenumber) VALUES (?, ?, ?, ?);";
    private static final String INSERT_MEDICINE = "INSERT INTO medicines (medicineid, name, description) VALUES (?, ?, ?);";
    private static final String INSERT_PATIENT = "INSERT INTO patients (patientid, forename, surname) VALUES (?, ?, ?)";
    private static final String INSERT_PHARMACIST = "INSERT INTO pharmacists (pharmacistid, password, forename, surname) VALUES (?, ?, ? ,?)";

    private static final String SELECT_PRESCRIPTION = "SELECT patients.patientid, medicines.medicineid, presciptors.prescriptorid, prescriptions.issuedate, prescriptions.comment, prescriptions.quantity, prescriptions.payment, " +
            "prescriptions.isrealized, prescriptions.pharmacistid, patients.forename, patients.surname, medicines.name, medicines.description, prescriptors.forename, prescriptors.surname, prescriptors.phonenumber FROM prescriptions " +
            "INNER JOIN patients ON patients.patientid = prescriptions.patientid INNER JOIN medicines ON prescriptions.medicineid = medicines.medicineid INNER JOIN prescribers ON prescribers.prescriberid = prescriptions.prescriberid WHERE prescriptions.prescriptionid = ? ;";

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
            ResultSet resultSet = selectPrescription.executeQuery();


            Patient patient = null;
            Medicine medicine = null;
            Prescriptor prescriptor = null;

            while (resultSet.next()) {
                prescription = new Prescription();
                prescription.setPrescriptionId(prescriptionID);

                prescription.setMedicine(medicine);
                medicine.setDescription(resultSet.getString("medicines.description"));
                medicine.setMedicineId(resultSet.getLong("medicines.medicineid"));
                medicine.setName(resultSet.getString("medicines.name"));

                prescription.setPatient(patient);
                patient.setPatientId(resultSet.getLong("patients.patientid"));
                patient.setForename(resultSet.getString("patients.forename"));
                patient.setSurname(resultSet.getString("patients.surname"));

                prescription.setPrescriptor(prescriptor);
                prescriptor.setPrescriptorId(resultSet.getLong("prescriptors.prescriptorid"));
                prescriptor.setForename(resultSet.getString("prescriptors.forename"));
                prescriptor.setSurname(resultSet.getString("prescriptors.surname"));
                prescriptor.setPhoneNumber(resultSet.getString("prescriptors.phonenumber"));

                prescription.setPharmacist(null); // to do
                prescription.setQuantity(resultSet.getInt("prescriptions.quantity"));
                prescription.setRealized(resultSet.getBoolean("prescriptions.isrealized"));
                prescription.setIssueDate(resultSet.getDate("prescriptions.issuedate"));
                prescription.setComment(resultSet.getString("prescriptions.comment"));
                prescription.setPayment(resultSet.getDouble("prescriptions.payment"));
            }



        } catch (SQLException e) {
            System.err.println("Error during invoke of SQL query: \n" + e.getMessage());
            throw new RuntimeException("Error during invoke of SQL query");

        } finally {
            closeDatabaseResources(connection, selectPrescription);
        }


        return prescription;

    }








}
