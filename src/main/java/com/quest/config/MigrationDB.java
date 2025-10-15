package com.quest.config;

import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class MigrationDB {
    private final ApplicationProperties properties;

    public void start() throws Exception {
        log.info("Starting Liquibase...");

        Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
            CommandScope update = new CommandScope("update");

            update.addArgumentValue("changelogFile", "db/changelog/changelog-master.xml");
            update.addArgumentValue("url", properties.getProperty(ApplicationProperties.DATABASE_CONNECTION_URL));
            update.addArgumentValue("username", properties.getProperty(ApplicationProperties.DATABASE_CONNECTION_USERNAME));
            update.addArgumentValue("password", properties.getProperty(ApplicationProperties.DATABASE_CONNECTION_PASSWORD));
            update.addArgumentValue("driver", properties.getProperty(ApplicationProperties.DATABASE_CONNECTION_DRIVER));

            update.execute();
        });

        log.info("Liquibase started...DONE");
    }
}
