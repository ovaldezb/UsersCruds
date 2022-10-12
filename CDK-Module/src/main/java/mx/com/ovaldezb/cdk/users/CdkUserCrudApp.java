package mx.com.ovaldezb.cdk.users;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class CdkUserCrudApp {
    public static void main(final String[] args) {
        App app = new App();

        new CdkUserCrudStack(app, "CdkUserCrudStack");

        app.synth();
    }
}

