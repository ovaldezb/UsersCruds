package mx.com.ovaldezb.cdk.users.construct;

import mx.com.ovaldezb.cdk.users.config.Configuration;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.dynamodb.*;
import software.constructs.Construct;

public class DatabaseUser extends Construct {
    private final Table usuariosTable;
    public DatabaseUser(@NotNull Construct scope, @NotNull String id) {
        super(scope, id);
        this.usuariosTable = createUserTable();
    }

    private Table createUserTable(){
        Table userTable = new Table(this, "users",TableProps.builder()
                .tableName(Configuration.TABLE_USER)
                .partitionKey(Attribute.builder().name(Configuration.PRIMARY_KEY).type(AttributeType.STRING).build())
                .removalPolicy(RemovalPolicy.DESTROY)
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build()
        );
        return userTable;
    }

    public Table getUserTable(){
        return this.usuariosTable;
    }
}
