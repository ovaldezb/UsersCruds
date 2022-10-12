package mx.com.ovaldez.lambda.user.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import mx.com.ovaldez.lambda.user.entity.GatewayResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteHandler implements RequestHandler<Map<String,Object>, GatewayResponse> {
    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String, Object> pathParameters = (Map<String, Object>)input.get("pathParameters");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String id=(String)pathParameters.get("userId");
        System.out.println("delete data for input parameter:"+id);
        DynamoDbClient ddb = DynamoDbClient.create();
        String tableName= System.getenv("TABLE_NAME");
        String primaryKey = System.getenv("PRIMARY_KEY");
        Map<String, AttributeValue> tableKey = new HashMap<>();
        tableKey.put(primaryKey, AttributeValue.builder().s(id).build());

        DeleteItemRequest getItemRequest= DeleteItemRequest.builder()
                .key(tableKey)
                .tableName(tableName)
                .build();
        DeleteItemResponse response = ddb.deleteItem(getItemRequest);
        System.out.println(response.attributes());
        return new GatewayResponse("Usuario eliminado con exito", headers, 200);
    }
}
