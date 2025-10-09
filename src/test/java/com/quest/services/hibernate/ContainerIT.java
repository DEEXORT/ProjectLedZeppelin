package com.quest.services.hibernate;

import com.quest.config.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.quest.config.ApplicationProperties.*;

@Testcontainers
public class ContainerIT {

    static SessionCreator sessionCreator;

    @Container
    static final JdbcDatabaseContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void setup() throws Exception {
        ApplicationProperties properties = ServiceLocator.getService(ApplicationProperties.class);
        properties.setProperty(DATABASE_CONNECTION_URL, postgreSQLContainer.getJdbcUrl());
        properties.setProperty(DATABASE_CONNECTION_USERNAME, postgreSQLContainer.getUsername());
        properties.setProperty(DATABASE_CONNECTION_PASSWORD, postgreSQLContainer.getPassword());

        sessionCreator = new SessionCreator(properties);

        MigrationDB migrationDB = new MigrationDB(properties);
        migrationDB.start();
    }

    @AfterAll
    static void clean() {
        sessionCreator.close();
    }
}
