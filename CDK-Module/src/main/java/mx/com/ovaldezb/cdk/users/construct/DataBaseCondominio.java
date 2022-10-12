package mx.com.ovaldezb.cdk.users.construct;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.dynamodb.*;
import software.constructs.Construct;

public class DataBaseCondominio extends Construct {

    private final Table condominioTable;

    public DataBaseCondominio(@NotNull Construct scope, @NotNull String id) {
        super(scope, id);
        this.condominioTable = createTable();
    }

    private Table createTable(){
        return new Table(this,"condominio",
                TableProps.builder()
                        .tableName("Condominio")
                        .partitionKey(Attribute.builder().name("id").type(AttributeType.STRING).build())
                        .removalPolicy(RemovalPolicy.DESTROY)
                        .billingMode(BillingMode.PAY_PER_REQUEST)
                        .build()
                );
    }

    public Table getCondominioTable(){
        return  condominioTable;
    }
}
