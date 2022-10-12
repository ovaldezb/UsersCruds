package mx.com.ovaldezb.cdk.users;

import mx.com.ovaldezb.cdk.users.config.Configuration;
import mx.com.ovaldezb.cdk.users.construct.*;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.HashMap;
import java.util.Map;


public class CdkUserCrudStack extends Stack {
    public CdkUserCrudStack(final Construct scope, final String id) {
        super(scope, id);
        Map<String, Function> props = new HashMap<>();
        DatabaseUser dbUser = new DatabaseUser(this,"userTable");
        LambdaUser lambda = new LambdaUser(this,"UserLambda",dbUser.getUserTable());
        props.put(Configuration.CREATE_USER_FUNCTION_NAME,lambda.getCreateUserFunction());
        props.put(Configuration.GET_USER_FUNCTION_NAME,lambda.getGetUserFunction());
        props.put(Configuration.GET_USER_BYID_FUNCTION_NAME,lambda.getGetUserByIdFunction());
        props.put(Configuration.UPDATE_USER_FUNCTION_NAME,lambda.getUpdateUserFunction());
        props.put(Configuration.DELETE_USER_FUNCTION_NAME,lambda.getDeleteUserFunction());
        new GatewayUser(this,"UserGateway",props);
        DataBaseCondominio dataBaseCondominio = new DataBaseCondominio(this,"condominioTable");
        LambdaCondominio lambdaCondominio = new LambdaCondominio(this,"condominioLambda",dataBaseCondominio.getCondominioTable());
        props.put(Configuration.INSERT_USER_CONDO_FUNCTION_NAME,lambdaCondominio.getCreateCondominio());
        props.put(Configuration.GETALL_USER_CONDO_FUNCTION_NAME,lambdaCondominio.getGetAllCondominio());
        props.put(Configuration.GET_USERBYID_CONDO_FUNCTION_NAME,lambdaCondominio.getGetUserById());
        props.put(Configuration.UPDATE_USER_CONDO_FUNCTION_NAME,lambdaCondominio.getUpdateUserCondo());
        props.put(Configuration.DELETE_USER_FUNCTION_NAME,lambdaCondominio.getDeleteUserCondo());
        new GatewayCondominio(this,"CondoGateway",props);
    }


}
