import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class PrescriptionServicesApp extends NanoHTTPD {
    RequestUrlMapper requestUrlMapper = new RequestUrlMapper();


    public PrescriptionServicesApp(int port) throws IOException {
        super(port);
        start(5000, false);
        System.out.println("Server has started");
    }

    @Override
    public Response serve(IHTTPSession session) {
        return requestUrlMapper.serveResponse(session);
    }
}
