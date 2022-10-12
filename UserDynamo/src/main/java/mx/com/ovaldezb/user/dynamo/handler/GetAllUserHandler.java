package mx.com.ovaldezb.user.dynamo.handler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import mx.com.ovaldezb.user.dynamo.entity.Condominio;
import mx.com.ovaldezb.user.dynamo.entity.GatewayResponse;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Map;

public class GetAllUserHandler implements RequestHandler<Map<String,Object>, GatewayResponse> {
    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger logger = context.getLogger();
        Gson gson = new Gson();
        JSONParser parser = new JSONParser();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Condominio> reply = mapper.scan(Condominio.class,scanExpression);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Condominio condominio : reply){
            try {
                sb.append(parser.parse(gson.toJson(condominio, Condominio.class))).append(",");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return new GatewayResponse(sb.toString(),200);
    }
}
