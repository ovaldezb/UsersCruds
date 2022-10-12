package mx.com.ovaldez.lambda.user.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import mx.com.ovaldez.lambda.user.entity.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserGetByIdHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        System.out.println("Entrando");
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        LambdaLogger logger = context.getLogger();
        Gson gson = new Gson();
        JSONParser parser = new JSONParser();
        JSONObject responseObject = new JSONObject();
        try {
            String tableName= System.getenv("TABLE_NAME");
            String primaryKey = System.getenv("PRIMARY_KEY");
            DynamoDbClient dbClient = DynamoDbClient.create();
            JSONObject reqObject = (JSONObject) parser.parse(reader);
            //System.out.println("pathParameters"+reqObject.get("pathParameters"));
            if(reqObject.get("pathParameters") != null) {
                System.out.println("Si hay pathParam");
                JSONObject pps = (JSONObject) reqObject.get("pathParameters");
                if(pps.get("userId")!= null) {
                    String  id = (String)pps.get("userId");
                    Map<String, AttributeValue> tableKey = new HashMap<>();
                    tableKey.put(primaryKey, AttributeValue.builder().s(id).build());
                    GetItemRequest req = GetItemRequest.builder().key(tableKey).tableName(tableName).build();
                    GetItemResponse response = dbClient.getItem(req);
                    if(response != null) {
                        Map<String, AttributeValue> item = response.item();
                        StringBuilder sb = new StringBuilder();
                        User u = new User();
                        u.setUserId(item.get("userId").s());
                        u.setNombre(item.get("nombre").s());
                        u.setApellido(item.get("apellido").s());
                        u.setCondominio(item.get("condominio").s());
                        u.setAntiguedad(Integer.parseInt(item.get("antiguedad").n()));
                        sb.append(parser.parse(gson.toJson(u, User.class)));
                        responseObject.put("body", sb.toString());
                        responseObject.put("statusCode", 200);
                    }else{
                        responseObject.put("body", "No se encontró el userId "+id);
                        responseObject.put("statusCode", 404);
                    }
                }else {
                    //User u = new User("123132","Omar","Valdez","Sauces",3);
                    responseObject.put("body","Error, no existe userId");
                    responseObject.put("statusCode",400);
                }
            }
            responseObject.put("isBase64Encoded","false");
            logger.log("Salida:"+responseObject.toString());
            writer.write(responseObject.toString());
            writer.close();
            reader.close();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
