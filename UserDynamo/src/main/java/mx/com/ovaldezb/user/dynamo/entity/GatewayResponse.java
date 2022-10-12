package mx.com.ovaldezb.user.dynamo.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GatewayResponse {
    private final String body;
    //private final Map<String, String> headers;
    private final int statusCode;

    public GatewayResponse(final String body,  final int statusCode) {
        super();
        this.body = body;
        //this.headers = Collections.unmodifiableMap(new HashMap<String, String>(headers));
        this.statusCode = statusCode;
    }
    public String getBody() {
        return body;
    }
    //public Map<String, String> getHeaders() {
    //    return headers;
    //}
    public int getStatusCode() {
        return statusCode;
    }
}
