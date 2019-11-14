package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD;
import model.Prescription;
import storage.TemporaryPrescriptionStorage;

import static fi.iki.elonen.NanoHTTPD.Response.Status.INTERNAL_ERROR;
import static fi.iki.elonen.NanoHTTPD.Response.Status.OK;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

import java.io.IOException;

public class PrescriptionController {

    public NanoHTTPD.Response serveAddPrescription(NanoHTTPD.IHTTPSession session){
        ObjectMapper objectMapper = new ObjectMapper();
        int randomPrescriptionId;

        int contentLength = Integer.parseInt(session.getHeaders().get("content-length"));
        byte[] buffer = new byte[contentLength];

        try {
            session.getInputStream().read(buffer,0,contentLength);
            String requestBody = new String(buffer).trim();
            Prescription requestPrescription = objectMapper.readValue(requestBody, Prescription.class);
            randomPrescriptionId = requestPrescription.hashCode();
            requestPrescription.setPrescriptionId(randomPrescriptionId);

            TemporaryPrescriptionStorage.addNewPrescription(requestPrescription);
        } catch (IOException e) {
            System.err.println("Error during process request: " + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error prescription has not been added to the system");
        }

        return newFixedLengthResponse(OK, "text/plain","Prescription has been added under id : " + randomPrescriptionId);
    }

    public NanoHTTPD.Response serveGetPrescription(NanoHTTPD.IHTTPSession session){
        return null;
    }

    public NanoHTTPD.Response serveRealizePrescription(NanoHTTPD.IHTTPSession session){
        return null;
    }
}
