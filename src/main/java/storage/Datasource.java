package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Datasource {

    private static final String DB_NAME = "prescriptiondb";
    private static final String CONNECTION_STRING = "jdbc:postgresql:" + DB_NAME;
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private Connection conn;

    public static final String INSERT_PRESCRIPTION = "";
    public static final String INSERT_ISSUER = "";
    public static final String INSERT_MEDICINE = "";
    public static final String INSERT_PATIENT = "";

    private PreparedStatement insertIntoPrescriptions;
    private PreparedStatement insertIntoIssuers;
    private PreparedStatement insertIntoMedicines;
    private PreparedStatement insertIntoPatients;

    public boolean open(){
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
            insertIntoPrescriptions = conn.prepareStatement(INSERT_PRESCRIPTION);
            insertIntoIssuers = conn.prepareStatement(INSERT_ISSUER);
            insertIntoMedicines = conn.prepareStatement(INSERT_MEDICINE);
            insertIntoPatients = conn.prepareStatement(INSERT_PATIENT);
            return true;

        } catch (SQLException e) {
            System.err.println("The connection couldn't be established");
            return false;
        }
    }

    public void close() {
        try {
            if(insertIntoPrescriptions != null){
                insertIntoPrescriptions.close();
            }
            if (insertIntoIssuers != null){
                insertIntoIssuers.close();
            }
            if (insertIntoMedicines != null){
                insertIntoMedicines.close();
            }
            if (insertIntoPatients != null){
                insertIntoPatients.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Unable to close a connection" + e.getMessage());
        }
    }




}
