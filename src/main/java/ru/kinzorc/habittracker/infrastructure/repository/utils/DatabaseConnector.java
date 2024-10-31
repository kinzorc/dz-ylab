package ru.kinzorc.habittracker.infrastructure.repository.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Класс предоставляет функционал для установки соединения с базой данных с использованием JDBC.
 * <p>
 * Этот класс загружает параметры подключения из конфигурационного файла, настраивает и возвращает объект
 * {@link Connection} для подключения к базе данных. Поддерживает обработку ошибок, возникающих при
 * загрузке конфигурации и установлении соединения.
 * </p>
 */
public class DatabaseConnector {

    private static final String CONFIG_FILE_PATH = "/application.properties";
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DRIVER_KEY = "db.driver";
    /**
     * Свойства базы данных, загруженные из конфигурационного файла.
     */
    private final Properties DB_PROPERTIES = new Properties();
    private String url;
    private String username;
    private String password;

    /**
     * Конструктор по умолчанию, инициализирующий загрузку свойств базы данных из конфигурационного файла.
     * <p>
     * При создании экземпляра класса выполняется загрузка конфигурационных параметров,
     * необходимых для соединения с базой данных.
     * </p>
     */
    public DatabaseConnector() {
        loadProperties();
    }

    /**
     * Перегруженный конструктор для явной передачи параметров базы данных.
     *
     * @param url      URL базы данных
     * @param username имя пользователя базы данных
     * @param password пароль пользователя базы данных
     */
    public DatabaseConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Возвращает соединение с базой данных.
     * <p>
     * Использует параметры подключения, загруженные из файла конфигурации (URL, имя пользователя и пароль),
     * для создания соединения через {@link DriverManager#getConnection(String, String, String)}.
     * Если параметры подключения отсутствуют или некорректны, выбрасывается исключение
     * {@link IllegalArgumentException}. В случае проблем с установлением соединения — {@link SQLException}.
     * </p>
     *
     * @return объект {@link Connection} для соединения с базой данных
     * @throws SQLException             если возникает ошибка при установлении соединения с базой данных
     * @throws IllegalArgumentException если параметры подключения некорректны или отсутствуют в файле конфигурации
     */
    public Connection getConnection() throws SQLException {

        if (url == null || username == null || password == null) {

            if (isConfigurationInvalid()) {
                throw new IllegalArgumentException("В файле конфигурации указаны некорректные параметры подключения.");
            }

            url = DB_PROPERTIES.getProperty(URL_KEY);
            username = DB_PROPERTIES.getProperty(USERNAME_KEY);
            password = DB_PROPERTIES.getProperty(PASSWORD_KEY);
        }

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SQLException("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }

    /**
     * Загружает параметры базы данных из конфигурационного файла `application.properties`.
     * <p>
     * Файл должен находиться в корневом каталоге ресурсов и содержать такие параметры, как URL базы данных,
     * имя пользователя, пароль и имя драйвера. Если файл конфигурации не найден или произошла ошибка
     * при его чтении, выводится сообщение об ошибке.
     * </p>
     */
    private void loadProperties() {
        try (InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILE_PATH)) {
            if (inputStream != null) {
                DB_PROPERTIES.load(inputStream);
            } else {
                System.err.println("Конфигурационный файл не найден.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения конфигурационного файла: " + e.getMessage());
        }
    }

    /**
     * Проверяет корректность конфигурации, удостоверяясь, что все необходимые параметры присутствуют.
     *
     * @return true, если конфигурация некорректна (отсутствует один или несколько параметров), иначе false
     */
    private boolean isConfigurationInvalid() {
        return DB_PROPERTIES.getProperty(URL_KEY) == null ||
                DB_PROPERTIES.getProperty(USERNAME_KEY) == null ||
                DB_PROPERTIES.getProperty(PASSWORD_KEY) == null ||
                DB_PROPERTIES.getProperty(DRIVER_KEY) == null;
    }
}
