package mx.com.ovaldezb.user.dynamo.handler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mx.com.ovaldezb.user.dynamo.entity.Condominio;
import mx.com.ovaldezb.user.dynamo.entity.GatewayResponse;

import java.util.Map;

public class CreateUserHandler implements RequestHandler<Map<String,Object>, GatewayResponse> {
    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger logger = context.getLogger();
        Gson gson = new Gson();
        logger.log("Entrando");
        String body = (String)input.get("body");
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        JsonElement element = JsonParser.parseString(body);
        JsonObject jsonObject = element.getAsJsonObject();
        Condominio condominio = new Condominio();
        condominio.setNombre(jsonObject.get("nombre").getAsString());
        condominio.setCodigoPostal(jsonObject.get("codigoPostal").getAsString());
        condominio.setDireccion(jsonObject.get("direccion").getAsString());
        condominio.setLugares(jsonObject.get("lugares").getAsInt());
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(condominio);
        return new GatewayResponse(gson.toJson(condominio,Condominio.class),200);
    }
}
