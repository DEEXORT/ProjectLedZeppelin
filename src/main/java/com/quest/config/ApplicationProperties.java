package com.quest.config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class ApplicationProperties extends Properties {
    public static final String DATABASE_CONNECTION_URL = "hibernate.connection.url";
    public static final String DATABASE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String DATABASE_CONNECTION_PASSWORD = "hibernate.connection.password";
    public static final String DATABASE_CONNECTION_DRIVER = "hibernate.connection.driver_class";
    public static final String DATABASE_SHOW_SQL = "hibernate.show_sql";
    public static final String DATABASE_DIALECT = "hibernate.dialect";
    public static final String ENV_EXPRESSION = "\\$\\{[A-Z_]*:.*}";

    public ApplicationProperties() {
        try {
            this.load(new FileReader(CLASSES_ROOT + "/application.properties"));
            log.info("Loaded Application Properties from path: {}", CLASSES_ROOT);
            scanEnvironments();

            try {
                // TODO: Вынести в статический блок для более ранней инициализации
                Class.forName(this.getProperty(DATABASE_CONNECTION_DRIVER));
            } catch (ClassNotFoundException e) {
                throw new ExceptionInInitializerError("Could not load database driver");
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load application.properties", e);
        }
    }

    private void scanEnvironments() {
        Map<String, String> environments = System.getenv();
        this.forEach((k, v) -> {
            if (v.toString().matches(ENV_EXPRESSION)) {
                String[] values = v.toString().replace("${", "")
                        .replace("}", "")
                        .split(":", 2);
                String envKey = values[0];
                String devVal = values[1];
                String actualVal = environments.getOrDefault(envKey, devVal);
                this.put(k, actualVal);
            }
            log.info("Found environment variable {}={}", k, get(k));
        });
    }

    public final static Path CLASSES_ROOT = Paths.get(URI.create(
            Objects.requireNonNull(
                    ApplicationProperties.class.getResource("/")
            ).toString()));
}
