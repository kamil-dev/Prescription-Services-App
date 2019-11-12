import controller.PrescriptionController;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

import static fi.iki.elonen.NanoHTTPD.Response.Status.NOT_FOUND;

public class RequestUrlMapper {
    private final static String ADD_NEW_PRESCRIPTION_URL = "/prescription/add" ;
    private final static String GET_PRESCRIPTION_URL = "/prescription/get";
    private final static String GET_AND_REALIZE_PRESCRIPTION_URL = "/prescription/realize";
    private PrescriptionController prescriptionController = new PrescriptionController();

    public Response serveResponse(NanoHTTPD.IHTTPSession session){
        if (session.getMethod().equals(NanoHTTPD.Method.POST) && session.getUri().equals(ADD_NEW_PRESCRIPTION_URL)){
            return prescriptionController.serveAddPrescription(session);
        }
        else if (session.getMethod().equals(NanoHTTPD.Method.GET) && session.getUri().equals(GET_PRESCRIPTION_URL)) {
            return prescriptionController.serveGetPrescription(session);
        }
        else if (session.getMethod().equals(NanoHTTPD.Method.GET) && session.getUri().equals(GET_AND_REALIZE_PRESCRIPTION_URL)){
            return prescriptionController.serveRealizePrescription(session);
        }
        return NanoHTTPD.newFixedLengthResponse(NOT_FOUND, "text/plain", "Not Found");
    }


}
