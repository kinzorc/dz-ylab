package ru.kinzorc.habittracker.infrastructure.repository.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnector {

    private final Properties DB_PROPERTIES = new Properties();

    public JdbcConnector() {
        loadProperties();
    }

    // Метод для загрузки конфигурационного файла
    private void loadProperties() {
        try (InputStream inputStream = getClass().getResourceAsStream("/application.properties")) {
            if (inputStream != null) {
                DB_PROPERTIES.load(inputStream);
            } else {
                System.err.println("Файл application.properties не найден.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения конфигурационного файла: " + e.getMessage());
        }

    }

    // Метод для получения соединения с базой данных
    public Connection getConnection() throws SQLException {
        if (DB_PROPERTIES.getProperty("postgres_db.url") == null ||
                DB_PROPERTIES.getProperty("postgres_db.username") == null ||
                DB_PROPERTIES.getProperty("postgres_db.password") == null) {
            throw new IllegalArgumentException("В файле конфигурации указаны некорректные параметры подключения.");
        }

        String url = DB_PROPERTIES.getProperty("postgres_db.url");
        String username = DB_PROPERTIES.getProperty("postgres_db.username");
        String password = DB_PROPERTIES.getProperty("postgres_db.password");

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
            if (connection == null) {
                System.err.println("Не удалось подключиться к базе данных.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            throw e;
        }

        return connection;
    }

}
