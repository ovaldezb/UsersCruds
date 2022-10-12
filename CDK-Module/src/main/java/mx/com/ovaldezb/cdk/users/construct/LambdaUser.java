package mx.com.ovaldezb.cdk.users.construct;

import mx.com.ovaldezb.cdk.users.config.Configuration;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import java.util.HashMap;
import java.util.Map;

public class LambdaUser extends Construct {

    private final Function createUserFunction;
    private final Function getUserFunction;
    private final Function getUserByIdFunction;
    private final Function updateUserFunction;
    private final Function deleteUserFunction;

    public LambdaUser(@NotNull Construct scope, @NotNull String id, Table userTable) { //, Table itemTable
        super(scope,id);
        Map<String, String> lambdaEnvMap = new HashMap<>();
        lambdaEnvMap.put("TABLE_NAME", Configuration.TABLE_USER);
        lambdaEnvMap.put("PRIMARY_KEY",Configuration.PRIMARY_KEY);
        this.createUserFunction = createFunction(lambdaEnvMap,Configuration.CREATE_USER_FUNCTION_HANDLER, Configuration.CREATE_USER_FUNCTION_NAME);
        this.getUserFunction = createFunction(lambdaEnvMap,Configuration.GET_USER_FUNCTION_HANDLER,Configuration.GET_USER_FUNCTION_NAME);
        this.getUserByIdFunction = createFunction(lambdaEnvMap,Configuration.GET_USER_BYID_FUNCTION_HANDLER,Configuration.GET_USER_BYID_FUNCTION_NAME);
        this.updateUserFunction = createFunction(lambdaEnvMap,Configuration.UPDATE_USER_FUNCTION_HANDLER, Configuration.UPDATE_USER_FUNCTION_NAME);
        this.deleteUserFunction = createFunction(lambdaEnvMap, Configuration.DELETE_USER_FUNCTION_HANDLER, Configuration.DELETE_USER_FUNCTION_NAME);
        userTable.grantReadWriteData(createUserFunction);
        userTable.grantReadWriteData(getUserFunction);
        userTable.grantReadWriteData(getUserByIdFunction);
        userTable.grantReadWriteData(updateUserFunction);
        userTable.grantReadWriteData(deleteUserFunction);
    }

    private Function createFunction(Map<String, String> lambdaEnvMap, String handler, String functionName){
        return Function.Builder
                .create(this,functionName)
                .timeout(Duration.seconds(20))
                .memorySize(512)
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("/Users/macbookpro/Documents/workspace-intellij/UsersCruds/UserLambdaCrud/target/UserLambdaCrud-1.0.0-jar-with-dependencies.jar"))
                .environment(lambdaEnvMap)
                .handler(handler)
                .functionName(functionName)
                .build();
    }

    public Function getCreateUserFunction(){
        return createUserFunction;
    }

    public Function getGetUserFunction(){
        return getUserFunction;
    }

    public Function getGetUserByIdFunction(){ return getUserByIdFunction; }

    public Function getUpdateUserFunction(){ return updateUserFunction; }

    public Function getDeleteUserFunction(){ return  deleteUserFunction; }
}
