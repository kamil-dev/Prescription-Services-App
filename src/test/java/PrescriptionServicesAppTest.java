
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

public class PrescriptionServicesAppTest {

    private static final String PRESCRIPTION_1 = "{\"medicine\" : {\"medicineId\" : 567122, \"name\" : \"Supremin\", \"description\" : \"200 ml\"}," +
            "\"patient\" : {\"patientId\" : 123456, \"forename\" : \"Andrzej\", \"surname\" : \"Nowak\"}," +
            "\"prescriptor\" : {\"prescriptorId\" : 7722345, \"forename\" : \"Anna\", \"surname\" : \"Kowalska\", \"phoneNumber\" : \"675234812\"}," +
            "\"comment\" : \"3 x 15 ml dziennie\", \"quantity\" : 2, \"payment\" : 100.00}";

    private static final String PRESCRIPTION_2 = "{\"medicine\" : {\"medicineId\" : 3456123, \"name\" : \"Ketonal\", \"description\" : \"30 tab.\"}," +
            "\"patient\" : {\"patientId\" : 456743, \"forename\" : \"Piotr\", \"surname\" : \"Mazowiecki\"}," +
            "\"prescriptor\" : {\"prescriptorId\" : 7722345, \"forename\" : \"Anna\", \"surname\" : \"Kowalska\", \"phoneNumber\" : \"675234812\"}," +
            "\"comment\" : \"3tab. dziennie\", \"quantity\" : 1, \"payment\" : 80.00}";

    private static final int APP_PORT = 8081;

    private PrescriptionServicesApp prescriptionServicesApp;

    @BeforeAll
    public static void beforeAll(){
        RestAssured.port = APP_PORT;
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        prescriptionServicesApp = new PrescriptionServicesApp(APP_PORT);
    }

    @AfterEach
    public void afterEach(){
        prescriptionServicesApp.stop();
    }

    @Test
    public void addMethod_correctBody_shouldReturnStatus200(){
        with().body(PRESCRIPTION_1).when().post("/prescription/add").then().statusCode(200).body(startsWith("Prescription has been added under id :"));
    }

    @Test
    public void addMethod_wrongInput_shouldReturnStatus500(){
        String prescriptionWithFieldTypeMismatch = "{\"medicine\" : {\"medicineId\" : \"text\", \"name\" : \"Ketonal\", \"description\" : \"30 tab.\"}," +
                "\"patient\" : {\"patientId\" : 456743, \"forename\" : \"Piotr\", \"surname\" : \"Mazowiecki\"}," +
                "\"prescriptor\" : {\"prescriptorId\" : 7722345, \"forename\" : \"Anna\", \"surname\" : \"Kowalska\", \"phoneNumber\" : \"675234812\"}," +
                "\"comment\" : \"3tab. dziennie\", \"quantity\" : 1, \"payment\" : 80.00}";

        with().body(prescriptionWithFieldTypeMismatch).when().post("/prescription/add").then().statusCode(500);
    }

    @Test
    public void addMethod_unexpectedField_shouldReturnStatus500(){
        with().body("{\"key\" : \"value\"}").when().post("/prescription/add").then().statusCode(500);
    }

    private long addPrescriptionAndGetId(String json){
        String response = with().body(json).when().post("/prescription/add").then().statusCode(200)
                .body(startsWith("Prescription has been added under id :")).extract().body().asString();

        return Long.parseLong(response.substring(response.indexOf(":") + 1).trim());
    }

    @Test
    public void getMethod_correctId_shouldReturnStatus200(){
        long prescriptionId = addPrescriptionAndGetId(PRESCRIPTION_2);

        with().param("prescriptionId", prescriptionId).when().get("/prescription/get").then().statusCode(200)
                .body("comment",equalTo("3tab. dziennie")).body("quantity", equalTo(1));
    }

    @Test
    public void getMethod_noIdgiven_shouldReturnStatus400(){
        when().get("/prescription/get").then().statusCode(400).body(equalTo("Wrong request parameters"));
    }

    @Test
    public void getMethod_wrongTypeOfId_shouldReturnStatus400(){
        with().param("prescriptionId","asap").when().get("/prescription/get")
                .then().statusCode(400).body(equalTo("Request param 'prescriptionId' has to be a number"));
    }

    @Test
    public void getMethod_noSuchIdWithinDatabase_shouldReturnStatus404(){
        with().param("prescriptionId",123456).when().get("/prescription/get")
                .then().statusCode(404);
    }

    @Test



}
