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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Map;

public class GetUserByIdHandler implements RequestHandler<Map<String,Object>, GatewayResponse> {
    @Override
    public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger logger = context.getLogger();
        Map<String, Object> pathParameters = (Map<String, Object>)input.get("pathParameters");
        String id=(String)pathParameters.get("id");
        logger.log("Entrando a GET: "+id);
        Gson gson = new Gson();
        JSONParser parser = new JSONParser();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Condominio condominio = new Condominio();
        condominio.setId(id);
        DynamoDBQueryExpression<Condominio> queryExpression = new DynamoDBQueryExpression<Condominio>().withHashKeyValues(condominio);
        List<Condominio> itemList = mapper.query(Condominio.class, queryExpression);
        StringBuilder sb = new StringBuilder();
        try {
            for(Condominio condo : itemList){
                    sb.append(parser.parse(gson.toJson(condo, Condominio.class)));
                }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new GatewayResponse(sb.toString(),200);
    }
}
