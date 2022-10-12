package mx.com.ovaldez.lambda.user.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import mx.com.ovaldez.lambda.user.entity.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

public class UserGetHandler implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        try {
            OutputStreamWriter writer = new OutputStreamWriter(output);
            logger.log("Entrando a Get");
            Gson gson = new Gson();
            JSONParser parser = new JSONParser();
            DynamoDbClient dbClient = DynamoDbClient.create();
            String tableName = System.getenv("TABLE_NAME");
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(tableName)
                    .build();
            ScanResponse response = dbClient.scan(scanRequest);
            logger.log("Response: " + response.items());
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Map<String, AttributeValue> item : response.items()) {
                logger.log(item.toString());
                User u = new User();
                u.setUserId(item.get("userId").s());
                u.setNombre(item.get("nombre").s());
                u.setApellido(item.get("apellido").s());
                u.setCondominio(item.get("condominio").s());
                u.setAntiguedad(Integer.parseInt(item.get("antiguedad").n()));
                sb.append(parser.parse(gson.toJson(u, User.class)));
                //arraySalida.add(gson.toJson(u, User.class));
            }
            logger.log("Termino el for");
            sb.append("]");
            JSONObject responseObject = new JSONObject();
            responseObject.put("body", sb.toString());
            responseObject.put("statusCode", 200);
            responseObject.put("isBase64Encoded", "false");
            logger.log("Salio bien "+responseObject.toString());
            writer.write(responseObject.toString());
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
            context.getLogger().log("Error:"+e.getCause());
        }
    }
}
