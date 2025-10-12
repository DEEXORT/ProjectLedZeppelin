package com.quest.config;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static final String URL = "jdbc:mysql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "qwerty";
    private static final List<Connection> connections = new ArrayList<>();
    private static BlockingQueue<Connection> connectionQueue;

    public ConnectionPool(int sizePool) {
        connectionQueue = new ArrayBlockingQueue<>(sizePool);
        for (int i = 0; i < sizePool; i++) {
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                connections.add(connection);
                Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{Connection.class},
                        (proxy, method, args) -> {
                            if ("close".equals(method.getName())) {
                                connectionQueue.add((Connection) proxy);
                            }
                            return method.invoke(connection);
                        }
                );
            } catch (SQLException e) {
                throw new RuntimeException("Could not connect to database", e);
            }
        }
    }

    public Connection getConnection() {
        try {
            return connectionQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        connections.forEach(connection -> {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Could not close connection", e);
            }
        });
    }
}
