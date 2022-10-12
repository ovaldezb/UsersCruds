package mx.com.ovaldezb.user.dynamo.handler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import mx.com.ovaldezb.user.dynamo.entity.Condominio;
import mx.com.ovaldezb.user.dynamo.entity.GatewayResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Map;

public class UpdateUserHandler implements RequestHandler<Map<String,Object>, GatewayResponse> {
    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger logger = context.getLogger();
        GatewayResponse gwres = null;
        Map<String, Object> pathParameters = (Map<String, Object>)input.get("pathParameters");
        String id=(String)pathParameters.get("id");
        String body = (String)input.get("body");
        JSONParser parser = new JSONParser();
        try {
            JSONObject bodyObject = (JSONObject) parser.parse(body);
            Gson gson = new Gson();
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
            DynamoDBMapper mapper = new DynamoDBMapper(client);
            Condominio condominio = new Condominio();
            condominio.setId(id);
            DynamoDBQueryExpression<Condominio> queryExpression = new DynamoDBQueryExpression<Condominio>().withHashKeyValues(condominio);
            List<Condominio> itemList = mapper.query(Condominio.class, queryExpression);
            logger.log("Size:" + itemList.size());
            if (itemList.size() != 1) {
                gwres = new GatewayResponse("Error, no existe ese ID " + id, 400);
            } else {
                condominio = itemList.get(0);
                condominio.setNombre(bodyObject.get("nombre").toString()) ;
                condominio.setDireccion(bodyObject.get("direccion").toString());
                condominio.setCodigoPostal(bodyObject.get("codigoPostal").toString());
                condominio.setLugares(Integer.parseInt(bodyObject.get("lugares").toString()));
                mapper.save(condominio);
                gwres = new GatewayResponse(gson.toJson(condominio,Condominio.class), 200);
            }
        }catch (ParseException e){
            logger.log(e.getMessage());
        }
        return gwres;
    }
}
