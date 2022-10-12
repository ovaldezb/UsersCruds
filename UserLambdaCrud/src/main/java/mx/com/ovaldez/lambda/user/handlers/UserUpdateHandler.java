package mx.com.ovaldez.lambda.user.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mx.com.ovaldez.lambda.user.entity.GatewayResponse;
import mx.com.ovaldez.lambda.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserUpdateHandler implements RequestHandler<Map<String,Object>, GatewayResponse> {
    private final Logger logger = LoggerFactory.getLogger(UserUpdateHandler.class);
    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> pathParameters = (Map<String, Object>)input.get("pathParameters");
        String id=(String)pathParameters.get("userId");
        String body = (String)input.get("body");
        String output = updateData(id, body);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return new GatewayResponse(output, headers, 200);
    }

    private String updateData(String id, String body){
        Gson gson = new Gson();
        DynamoDbClient ddb = DynamoDbClient.create();
        String tableName= System.getenv("TABLE_NAME");
        String primaryKey = System.getenv("PRIMARY_KEY");
        Map<String, AttributeValue> tableKey = new HashMap<>();
        tableKey.put(primaryKey, AttributeValue.builder().s(id).build());
        Map<String, AttributeValueUpdate> item = new HashMap<>();
        JsonElement element = JsonParser.parseString(body);
        JsonObject jsonObject = element.getAsJsonObject();
        Set<String> keys = jsonObject.keySet();
        for (String key: keys) {
            if(key.equals("antiguedad")){
                item.put(key, AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().n(jsonObject.get(key).getAsString()).build())
                        .action(AttributeAction.PUT)
                        .build()
                );
            }else {
                item.put(key, AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(jsonObject.get(key).getAsString()).build())
                        .action(AttributeAction.PUT)
                        .build()
                );
            }
        }
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .key(tableKey)
                .tableName(tableName)
                .attributeUpdates(item)
                .returnValues(ReturnValue.ALL_NEW)
                .build();
        UpdateItemResponse response = ddb.updateItem(updateItemRequest);
        Map<String, AttributeValue> itemUpdated = response.attributes();
        //System.out.println("Item Updated:"+itemUpdated);
        StringBuilder sb = new StringBuilder();
        User u = new User();
        u.setUserId(itemUpdated.get("userId").s());
        u.setNombre(itemUpdated.get("nombre").s());
        u.setApellido(itemUpdated.get("apellido").s());
        u.setCondominio(itemUpdated.get("condominio").s());
        //System.out.println("antiguedad:"+ itemUpdated.get("antiguedad").s());
        u.setAntiguedad(Integer.parseInt(itemUpdated.get("antiguedad").n()));
        sb.append(JsonParser.parseString(gson.toJson(u, User.class)));
        return sb.toString();
    }
}
