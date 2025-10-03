package com.quest.config;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class ApplicationProperties extends Properties {
    public static final String DATABASE_CONNECTION_URL = "hibernate.connection.url";
    public static final String DATABASE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String DATABASE_CONNECTION_PASSWORD = "hibernate.connection.password";
    public static final String DATABASE_CONNECTION_DRIVER = "hibernate.connection.driver_class";
    public static final String DATABASE_SHOW_SQL = "hibernate.show_sql";
    public static final String DATABASE_DIALECT = "hibernate.dialect";

    public ApplicationProperties() {
        try {
            this.load(new FileReader(CLASSES_ROOT + "/application.properties"));
            try {
                Class.forName(this.getProperty(DATABASE_CONNECTION_DRIVER));
            } catch (ClassNotFoundException e) {
                throw new ExceptionInInitializerError("Could not load database driver");
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load application.properties", e);
        }
    }

    public final static Path CLASSES_ROOT = Paths.get(URI.create(
            Objects.requireNonNull(
                    ApplicationProperties.class.getResource("/")
            ).toString()));
}
