package ru.kinzorc.habittracker.infrastructure.repository.queries;

/**
 * Класс {@code UserSQLStatements} содержит SQL-запросы для операций с таблицами пользователей.
 * <p>
 * Этот класс предоставляет статические константы SQL-запросов для выполнения основных операций CRUD
 * и управления сессиями пользователей в базе данных. Запросы предназначены для взаимодействия с таблицами
 * базы данных, такими как {@code app_schema.users} и {@code service_schema.users_sessions}.
 * </p>
 */
public class UserSQLStatements {

    /**
     * SQL-запрос для создания нового пользователя.
     * <p>
     * Вставляет запись в таблицу {@code app_schema.users} с полями {@code username}, {@code password}, {@code email},
     * {@code role} и {@code status}.
     * </p>
     */
    public static final String CREATE_USER = "INSERT INTO app_schema.users (username, password, email, role, status) VALUES (?, ?, ?, ?, ?)";

    /**
     * SQL-запрос для обновления информации о существующем пользователе.
     * <p>
     * Обновляет поля {@code username}, {@code password}, {@code email}, {@code role} и {@code status} пользователя
     * в таблице {@code app_schema.users}, определённого по идентификатору {@code id}.
     * </p>
     */
    public static final String UPDATE_USER = "UPDATE app_schema.users SET username = ?, password = ?, email = ?, role = ?, status = ? WHERE id = ?";

    /**
     * SQL-запрос для удаления всех пользователей.
     * <p>
     * Удаляет все записи из таблицы {@code app_schema.users}.
     * </p>
     */
    public static final String DELETE_ALL_USERS = "DELETE FROM app_schema.users";

    /**
     * SQL-запрос для удаления пользователя по его идентификатору (ID).
     * <p>
     * Удаляет запись из таблицы {@code app_schema.users}, используя уникальный идентификатор {@code id}.
     * </p>
     */
    public static final String DELETE_USER = "DELETE FROM app_schema.users WHERE id = ?";

    /**
     * SQL-запрос для извлечения всех пользователей.
     * <p>
     * Извлекает все записи из таблицы {@code app_schema.users} без фильтрации.
     * </p>
     */
    public static final String FIND_ALL_USERS = "SELECT * FROM app_schema.users";

    /**
     * SQL-запрос для поиска пользователя по его идентификатору (ID).
     * <p>
     * Извлекает пользователя из таблицы {@code app_schema.users}, используя уникальный идентификатор {@code id}.
     * </p>
     */
    public static final String FIND_USER_BY_ID = "SELECT * FROM app_schema.users WHERE id = ?";

    /**
     * SQL-запрос для поиска пользователя по имени пользователя.
     * <p>
     * Извлекает пользователя из таблицы {@code app_schema.users}, используя значение {@code username}.
     * </p>
     */
    public static final String FIND_USER_BY_USERNAME = "SELECT * FROM app_schema.users WHERE username = ?";

    /**
     * SQL-запрос для поиска пользователя по email.
     * <p>
     * Извлекает пользователя из таблицы {@code app_schema.users}, используя значение {@code email}.
     * </p>
     */
    public static final String FIND_USER_BY_EMAIL = "SELECT * FROM app_schema.users WHERE email = ?";
}

