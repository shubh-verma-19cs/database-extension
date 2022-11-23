package org.example;

import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.client.ClientContext;
import com.hivemq.extension.sdk.api.client.parameter.InitializerInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput;
import com.hivemq.extension.sdk.api.services.Services;
import com.hivemq.extension.sdk.api.services.intializer.ClientInitializer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseExtensionMain implements ExtensionMain {

    private @NotNull HikariDataSource ds;
    @Override
    public void extensionStart(@NotNull ExtensionStartInput extensionStartInput, final @NotNull ExtensionStartOutput extensionStartOutput) {
        ds = createDb();
        final DatabasePublishInterceptor interceptor = new DatabasePublishInterceptor(ds);
        Services.initializerRegistry().setClientInitializer(new ClientInitializer() {
            @Override
            public void initialize(final @NotNull InitializerInput initializerInput, final @NotNull ClientContext clientContext) {
                clientContext.addPublishInboundInterceptor(interceptor);
            }
        });
    }

    @Override
    public void extensionStop(@NotNull ExtensionStopInput extensionStopInput, final @NotNull ExtensionStopOutput extensionStopOutput) {
        ds.close();
    }

    private @NotNull HikariDataSource createDb(){
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/mqtt");
        config.setUsername("majorproject");
        config.setPassword("majorproject");
        config.setAutoCommit(true);
        return new HikariDataSource(config);
    }
}
