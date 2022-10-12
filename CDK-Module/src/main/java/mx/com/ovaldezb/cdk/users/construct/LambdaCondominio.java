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

public class LambdaCondominio extends Construct {
    private final Function createCondominio;
    private final Function getAllCondominio;
    private final Function getUserById;
    private final Function updateUserCondo;
    private final Function deleteUserCondo;
    public LambdaCondominio(@NotNull Construct scope, @NotNull String id, Table userTable) {
        super(scope, id);
        Map<String, String> lambdaEnvMap = new HashMap<>();
        lambdaEnvMap.put("TABLE_NAME", "Condominio");
        lambdaEnvMap.put("PRIMARY_KEY","id");
        this.createCondominio = createFunction(lambdaEnvMap,Configuration.INSERT_USER_CONDO_HANDLER_NAME,Configuration.INSERT_USER_CONDO_FUNCTION_NAME);
        this.getAllCondominio = createFunction(lambdaEnvMap, Configuration.GETALL_USER_CONDO_HANDLER_NAME, Configuration.GETALL_USER_CONDO_FUNCTION_NAME);
        this.getUserById = createFunction(lambdaEnvMap, Configuration.GET_USERBYID_CONDO_HANDLER_NAME, Configuration.GET_USERBYID_CONDO_FUNCTION_NAME);
        this.updateUserCondo = createFunction(lambdaEnvMap, Configuration.UPDATE_USER_CONDO_HANDLER_NAME, Configuration.UPDATE_USER_CONDO_FUNCTION_NAME);
        this.deleteUserCondo = createFunction(lambdaEnvMap, Configuration.DELETE_USER_CONDO_HANDLER_NAME, Configuration.DELETE_USER_CONDO_FUNCTION_NAME);
        userTable.grantReadWriteData(createCondominio);
        userTable.grantReadWriteData(getAllCondominio);
        userTable.grantReadWriteData(getUserById);
        userTable.grantReadWriteData(updateUserCondo);
        userTable.grantReadWriteData(deleteUserCondo);
    }

    private Function createFunction(Map<String, String> lambdaEnvMap, String handler, String functionName){
        return Function.Builder
                .create(this,functionName)
                .timeout(Duration.seconds(20))
                .memorySize(512)
                .runtime(Runtime.JAVA_11)
                .code(Code.fromAsset("/Users/macbookpro/Documents/workspace-intellij/UsersCruds/UserDynamo/target/UserDynamo-1.0.0-jar-with-dependencies.jar"))
                .environment(lambdaEnvMap)
                .handler(handler)
                .functionName(functionName)
                .build();
    }

    public Function getCreateCondominio(){
        return createCondominio;
    }

    public Function getGetAllCondominio(){ return  getAllCondominio; }

    public Function getGetUserById(){ return  getUserById; }
    public Function getUpdateUserCondo(){ return updateUserCondo; }
    public Function getDeleteUserCondo(){ return  deleteUserCondo; }
}
