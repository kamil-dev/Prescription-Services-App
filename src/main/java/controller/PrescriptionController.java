package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD;
import model.Pharmacist;
import model.Prescription;
import storage.Datasource;

import static fi.iki.elonen.NanoHTTPD.Response.Status.*;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

public class PrescriptionController {
    private final static String PRESCRIPTION_ID_PARAM_NAME = "prescriptionId";
    private final static String PHARMACIST_ID_PARAM_NAME = "pharmacistId";
    private final static String PHARMACIST_PASSWORD_PARAM_NAME = "pharmacistPassword";
    private Datasource database = new Datasource();

    private static final String PATH_TO_PRESCRIPTION_STORAGE = "src/main/java/storage/prescriptions.txt";
    private static final String PATH_TO_PHARMACISTS_STORAGE = "src/main/java/storage/pharmacists.txt";

    public NanoHTTPD.Response serveAddPrescription(NanoHTTPD.IHTTPSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        int randomPrescriptionId;

        int contentLength = Integer.parseInt(session.getHeaders().get("content-length"));
        byte[] buffer = new byte[contentLength];

        try {
            session.getInputStream().read(buffer, 0, contentLength);
            String requestBody = new String(buffer).trim();
            Prescription requestPrescription = objectMapper.readValue(requestBody, Prescription.class);
            randomPrescriptionId = requestPrescription.hashCode();
            requestPrescription.setPrescriptionId(randomPrescriptionId);

            requestPrescription.setIssueDate(new Date(System.currentTimeMillis()));
            requestPrescription.setRealized(false);
            requestPrescription.setPharmacist(null);

            database.addPrescription(requestPrescription);

        } catch (IOException e) {
            System.err.println("Error during process request: " + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error prescription has not been added to the system");
        } catch (RuntimeException e2) {
            System.err.println(e2.getMessage());
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error prescription has not been added to the system");
        }

        return newFixedLengthResponse(OK, "text/plain", "Prescription has been added under id : " + randomPrescriptionId);
    }

    public NanoHTTPD.Response serveGetPrescription(NanoHTTPD.IHTTPSession session) {
        Map<String, List<String>> requestParameters = session.getParameters();

        if (requestParameters.containsKey(PRESCRIPTION_ID_PARAM_NAME)) {
            List<String> prescriptionIdParams = requestParameters.get(PRESCRIPTION_ID_PARAM_NAME);
            String prescriptionIdParam = prescriptionIdParams.get(0);
            long prescriptionId = 0;

            try {
                prescriptionId = Long.parseLong(prescriptionIdParam);
            } catch (NumberFormatException e) {
                System.err.println("Error during convert request");
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request param 'prescriptionId' has to be a number");
            }

            Prescription prescription = database.getPrescription(prescriptionId);
            if (prescription != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String response = objectMapper.writeValueAsString(prescription);
                    return newFixedLengthResponse(OK, "application/json", response);
                } catch (JsonProcessingException e) {
                    System.err.println("Error during process request");
                    return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error - can not read a prescription");
                }
            }
            return newFixedLengthResponse(NOT_FOUND, "application/json", "");
        }
        return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Wrong request parameters");
    }

    public NanoHTTPD.Response serveRealizePrescription(NanoHTTPD.IHTTPSession session) {

        Map<String, List<String>> requestParameters = session.getParameters();

        if (requestParameters.containsKey(PRESCRIPTION_ID_PARAM_NAME) &&
                requestParameters.containsKey(PHARMACIST_ID_PARAM_NAME) &&
                requestParameters.containsKey(PHARMACIST_PASSWORD_PARAM_NAME)) {
            List<String> prescriptionIdParams = requestParameters.get(PRESCRIPTION_ID_PARAM_NAME);
            List<String> pharmacistIdParams = requestParameters.get(PHARMACIST_ID_PARAM_NAME);
            List<String> pharmacistPasswordParams = requestParameters.get(PHARMACIST_PASSWORD_PARAM_NAME);

            String prescriptionIdParam = prescriptionIdParams.get(0);
            String pharmacistIdParam = pharmacistIdParams.get(0);
            String pharmacistPassword = pharmacistPasswordParams.get(0);

            long prescriptionId = 0;
            long pharmacistId = 0;

            try {
                prescriptionId = Long.parseLong(prescriptionIdParam);
                pharmacistId = Long.parseLong(pharmacistIdParam);
            } catch (NumberFormatException e) {
                System.err.println("Error during convert request");
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request params 'prescriptionId' and" +
                        "pharmacistId have to be a number");
            }
            Pharmacist pharmacist = database.getPharmacist(pharmacistId, pharmacistPassword);
            if ( pharmacist == null) {
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "The pharmacist id and/or password is not valid");
            }

            Prescription prescription = database.getPrescription(prescriptionId);

            if (prescription != null) {
                if (prescription.isRealized() == true) {
                    return newFixedLengthResponse(OK, "text/plain", "This prescription has already been realized");
                }
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String response = objectMapper.writeValueAsString(prescription);
                    return newFixedLengthResponse(OK, "application/json", response);
                } catch (JsonProcessingException e) {
                    System.err.println("Error during process request");
                    return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error - can not read a prescription");
                } finally {
                    prescription.setRealized(true);
                    prescription.setPharmacist(pharmacist);
                    database.updatePrescription(true, pharmacist.getPharmacistId(), prescriptionId);
                }

            }
            return newFixedLengthResponse(NOT_FOUND, "application/json", "");

        }
        return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Wrong request params");
    }
}
