package mx.com.ovaldezb.cdk.users.construct;

import mx.com.ovaldezb.cdk.users.config.Configuration;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

import java.util.Map;

public class GatewayCondominio extends Construct {
    public GatewayCondominio(@NotNull Construct scope, @NotNull String id, Map<String, Function> lambdas) {
        super(scope, id);
        RestApi apiGateway = new RestApi(this,"Condominio Api GW",
                RestApiProps.builder()
                        .restApiName("CondominioApiGW")
                        .build());
        IResource condominio = apiGateway.getRoot().addResource("condominio");
        IResource userByIdResource = condominio.addResource("{id}");
        Integration postCondominio = new LambdaIntegration((Function) lambdas.get(Configuration.INSERT_USER_CONDO_FUNCTION_NAME));
        condominio.addMethod("POST",postCondominio);
        Integration  getAllCondomino = new LambdaIntegration((Function)lambdas.get(Configuration.GETALL_USER_CONDO_FUNCTION_NAME));
        condominio.addMethod("GET",getAllCondomino);
        Integration getUserById = new LambdaIntegration((Function)lambdas.get(Configuration.GET_USERBYID_CONDO_FUNCTION_NAME));
        userByIdResource.addMethod("GET",getUserById);
        Integration updateUserCondo = new LambdaIntegration((Function) lambdas.get(Configuration.UPDATE_USER_CONDO_FUNCTION_NAME));
        userByIdResource.addMethod("PUT",updateUserCondo);
        Integration deleteUserCondo = new LambdaIntegration((Function) lambdas.get(Configuration.DELETE_USER_FUNCTION_NAME));
        userByIdResource.addMethod("DELETE",deleteUserCondo);
    }
}
