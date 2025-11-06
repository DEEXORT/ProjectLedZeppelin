package com.quest.services.hibernate;

import com.quest.config.ApplicationProperties;
import com.quest.config.ConfigApplication;
import com.quest.config.ServiceLocator;
import com.quest.config.SessionCreator;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.quest.config.ApplicationProperties.*;

@Testcontainers
@Slf4j
public class ContainerIT {

    static SessionCreator sessionCreator;
    private static final JdbcDatabaseContainer<?> POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:13.2")
                .withDatabaseName("test")
                .withUsername("postgres")
                .withPassword("postgres");
        POSTGRE_SQL_CONTAINER.start();
        setupGlobalDatabaseProperties();
    }

    private static void setupGlobalDatabaseProperties() {
        String urlWithP6spy = POSTGRE_SQL_CONTAINER.getJdbcUrl().replace("jdbc:postgresql", "jdbc:p6spy:postgresql");

        ApplicationProperties properties = ServiceLocator.getService(ApplicationProperties.class);
        properties.setProperty(DATABASE_CONNECTION_URL, urlWithP6spy);
        properties.setProperty(DATABASE_CONNECTION_USERNAME, POSTGRE_SQL_CONTAINER.getUsername());
        properties.setProperty(DATABASE_CONNECTION_PASSWORD, POSTGRE_SQL_CONTAINER.getPassword());

        sessionCreator = new SessionCreator(properties);

        ConfigApplication config = ServiceLocator.getService(ConfigApplication.class);
        config.initApplication();
        log.info("Application started");
    }

}
