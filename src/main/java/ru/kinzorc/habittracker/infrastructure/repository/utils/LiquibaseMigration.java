package ru.kinzorc.habittracker.infrastructure.repository.utils;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Класс для управления миграциями базы данных с использованием Liquibase.
 * <p>
 * Этот класс загружает параметры подключения из конфигурационного файла и выполняет миграции базы данных
 * на основе конфигурации файла изменений базы данных (changelog file). Используется JDBC для подключения к базе данных.
 * <p>
 * Пример использования:
 * <pre>
 *     LiquibaseMigration migration = new LiquibaseMigration();
 *     migration.startMigration();
 * </pre>
 * </p>
 */
public class LiquibaseMigration {

    private static final String CONFIG_FILE_PATH = "/liquibase.properties";
    private static final String URL_KEY = "url";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String DRIVER_KEY = "driver";
    private static final String CHANGELOG_FILE_KEY = "changeLogFile";

    private final Properties LIQUIBASE_PROPERTIES = new Properties();

    /**
     * Конструктор по умолчанию. Загружает параметры подключения из конфигурационного файла `liquibase.properties`.
     * <p>
     * Файл конфигурации должен содержать настройки базы данных, такие как URL, имя пользователя, пароль,
     * а также путь к файлу изменений Liquibase.
     * </p>
     *
     * @throws IOException если не удалось загрузить конфигурацию из файла `liquibase.properties`
     */
    public LiquibaseMigration() throws IOException {
        loadProperties();
    }

    /**
     * Выполняет миграцию базы данных, используя параметры, загруженные из конфигурационного файла.
     * <p>
     * Метод проверяет, что все необходимые параметры присутствуют, и, если они отсутствуют,
     * выбрасывается {@link IllegalArgumentException}. Подключается к базе данных, создает
     * экземпляр {@link Liquibase} и применяет миграции.
     * </p>
     *
     * @throws IllegalArgumentException если параметры подключения не указаны или некорректны
     * @throws SQLException             если возникает ошибка при подключении к базе данных
     * @throws DatabaseException        если не удалось создать экземпляр базы данных или произошла ошибка при подключении
     * @throws LiquibaseException       если произошла ошибка при применении миграций
     */
    public void startMigration() throws DatabaseException, SQLException, LiquibaseException {
        if (isConfigurationInvalid()) {
            throw new IllegalArgumentException("Некорректные параметры подключения в файле конфигурации.");
        }

        String url = LIQUIBASE_PROPERTIES.getProperty(URL_KEY);
        String username = LIQUIBASE_PROPERTIES.getProperty(USERNAME_KEY);
        String password = LIQUIBASE_PROPERTIES.getProperty(PASSWORD_KEY);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection == null) {
                throw new DatabaseException("Не удалось установить соединение с базой данных. " +
                        "Проверьте параметры подключения в конфигурационном файле.");
            }

            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS liquibase_schema");

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(LIQUIBASE_PROPERTIES.getProperty(CHANGELOG_FILE_KEY), new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (SQLException e) {
            throw new SQLException("Ошибка подключения к базе данных: " + e.getMessage(), e);
        }
    }

    /**
     * Загружает параметры подключения из конфигурационного файла `liquibase.properties`.
     * <p>
     * Конфигурационный файл должен находиться в корневом каталоге ресурсов и содержать свойства подключения,
     * такие как `url`, `username`, `password`, `driver`, и `changeLogFile`.
     * Если файл не найден или произошла ошибка при чтении, выбрасывается {@link IOException}.
     * </p>
     *
     * @throws IOException если файл конфигурации не найден или произошла ошибка при его чтении
     */
    private void loadProperties() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILE_PATH)) {
            if (inputStream != null) {
                LIQUIBASE_PROPERTIES.load(inputStream);
            } else {
                throw new IOException("Конфигурационный файл " + CONFIG_FILE_PATH + " не найден.");
            }
        }
    }

    /**
     * Проверяет, что все необходимые параметры конфигурации присутствуют в загруженном файле.
     *
     * @return true, если конфигурация неполная или некорректная, иначе false
     */
    private boolean isConfigurationInvalid() {
        return LIQUIBASE_PROPERTIES.getProperty(URL_KEY) == null ||
                LIQUIBASE_PROPERTIES.getProperty(USERNAME_KEY) == null ||
                LIQUIBASE_PROPERTIES.getProperty(PASSWORD_KEY) == null ||
                LIQUIBASE_PROPERTIES.getProperty(DRIVER_KEY) == null ||
                LIQUIBASE_PROPERTIES.getProperty(CHANGELOG_FILE_KEY) == null;
    }
}

