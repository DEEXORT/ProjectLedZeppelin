package com.quest.services.hibernate;

import com.quest.config.ApplicationProperties;
import com.quest.config.MigrationDB;
import com.quest.config.ServiceLocator;
import com.quest.config.SessionCreator;
import com.quest.entity.Achievement;
import com.quest.entity.User;
import com.quest.repository.RepositoryImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.quest.config.ApplicationProperties.*;

@Testcontainers
public class ContainerIT {

    @Container
    static final JdbcDatabaseContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("postgres");

    static SessionFactory sessionFactory;
    static SessionCreator sessionCreator;

    @BeforeAll
    static void setUp() throws Exception {
//        sessionFactory = new Configuration()
//                .configure("hibernate-test.cfg.xml")
//                .addAnnotatedClass(Achievement.class)
//                .addAnnotatedClass(User.class)
//                .buildSessionFactory();
        ApplicationProperties properties = ServiceLocator.getService(ApplicationProperties.class);
        properties.setProperty(DATABASE_CONNECTION_URL, postgreSQLContainer.getJdbcUrl());
        properties.setProperty(DATABASE_CONNECTION_USERNAME, postgreSQLContainer.getUsername());
        properties.setProperty(DATABASE_CONNECTION_PASSWORD, postgreSQLContainer.getPassword());
        sessionCreator = ServiceLocator.getService(SessionCreator.class);

        MigrationDB migrationDB = ServiceLocator.getService(MigrationDB.class);
        migrationDB.start();
    }
}
