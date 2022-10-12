package mx.com.ovaldezb.cdk.users.construct;

import mx.com.ovaldezb.cdk.users.config.Configuration;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

import java.util.Map;

public class GatewayUser extends Construct {
    public GatewayUser(@NotNull Construct scope, @NotNull String id, Map<String, Function> lambdas) {
        super(scope, id);
        RestApi apiGateway = new RestApi(this,"User Api GW",
                RestApiProps.builder()
                        .restApiName("UserService")
                        .build());
        IResource users = apiGateway.getRoot().addResource("users");
        IResource userByIdResource = users.addResource("{userId}");
        Integration userByIdFunc = new LambdaIntegration((Function)lambdas.get(Configuration.GET_USER_BYID_FUNCTION_NAME));
        userByIdResource.addMethod("GET",userByIdFunc);
        Integration createUserIntegration = new LambdaIntegration((Function) lambdas.get(Configuration.CREATE_USER_FUNCTION_NAME));
        users.addMethod("POST",createUserIntegration);

        Integration getUserIntegration = new LambdaIntegration((Function) lambdas.get(Configuration.GET_USER_FUNCTION_NAME));
        users.addMethod("GET",getUserIntegration);

        Integration updateUserIntegration = new LambdaIntegration((Function) lambdas.get(Configuration.UPDATE_USER_FUNCTION_NAME));
        userByIdResource.addMethod("PUT",updateUserIntegration);

        Integration deleteUserIntegration = new LambdaIntegration((Function)lambdas.get(Configuration.DELETE_USER_FUNCTION_NAME) );
        userByIdResource.addMethod("DELETE",deleteUserIntegration);
    }


}
