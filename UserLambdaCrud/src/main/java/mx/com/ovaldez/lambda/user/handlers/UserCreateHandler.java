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
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserCreateHandler implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();
        DynamoDbClient ddbClient = DynamoDbClient.create();
        StringBuilder sb = new StringBuilder();
        try {
            String tableName= System.getenv("TABLE_NAME");
            String primaryKey = System.getenv("PRIMARY_KEY");
            JSONObject reqObject = (JSONObject) parser.parse(reader);
            User newUser = gson.fromJson(reqObject.get("body").toString(),User.class);
            Map<String, AttributeValue> userItem = new HashMap<>();
            String userId = UUID.randomUUID().toString();
            userItem.put(primaryKey, AttributeValue.builder().s(userId).build());
            userItem.put("nombre", AttributeValue.builder().s(newUser.getNombre()).build());
            userItem.put("apellido", AttributeValue.builder().s(newUser.getApellido()).build());
            userItem.put("condominio", AttributeValue.builder().s(newUser.getCondominio()).build());
            userItem.put("antiguedad", AttributeValue.builder().n(String.valueOf(newUser.getAntiguedad())).build());
            PutItemRequest userRequest = PutItemRequest.builder().tableName(tableName).item(userItem).build();
            PutItemResponse itemResponse = ddbClient.putItem(userRequest);

            if(itemResponse != null) {
                newUser.setUserId(userId);
                sb.append(parser.parse(gson.toJson(newUser, User.class)));
                responseObject.put("statusCode", 200);
            }else{
                responseObject.put("body", "Error while saving user");
                responseObject.put("statusCode", 400);
            }
        } catch (Exception e) {
            logger.log("Error en el guardado "+e.getCause());
            responseObject.put("body", e.getMessage());
            responseObject.put("statusCode", 500);
        }
        responseObject.put("body", sb.toString());
        responseObject.put("isBase64Encoded","false");
        writer.write(responseObject.toString());
        reader.close();
        writer.close();
    }
}
