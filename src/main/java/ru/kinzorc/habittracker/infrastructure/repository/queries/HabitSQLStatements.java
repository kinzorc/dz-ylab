package ru.kinzorc.habittracker.infrastructure.repository.queries;

/**
 * Класс {@code HabitSQLStatements} содержит SQL-запросы для работы с таблицами привычек и их выполнений в базе данных.
 * Этот класс предоставляет набор констант с SQL-запросами для выполнения различных операций CRUD,
 * а также для управления выполнениями привычек, стриками и статистикой.
 * <p>
 * Основные функции, которые обеспечиваются данным классом:
 * </p>
 * <ul>
 *     <li>Создание и обновление привычек</li>
 *     <li>Удаление привычек (все, одна, все для конкретного пользователя)</li>
 *     <li>Добавление отметок о выполнении привычек и сброс выполнений</li>
 *     <li>Получение статистики по привычке за указанный период</li>
 *     <li>Расчет процента выполнения привычки</li>
 *     <li>Управление стриками (длина последовательности выполнений)</li>
 *     <li>Поиск привычек по различным параметрам, таким как ID, имя и пользователь</li>
 * </ul>
 * <p>
 * Этот класс организует запросы в формате, удобном для вызовов из слоя доступа к данным,
 * упрощая доступ к SQL-запросам и снижая вероятность ошибок.
 * </p>
 */
public class HabitSQLStatements {

    /**
     * Запрос для добавления новой привычки в таблицу {@code habits}
     */
    public static final String ADD_HABIT = "INSERT INTO app_schema.habits (user_id, habit_name, description, frequency, created_date, start_date, end_date, " +
            "execution_period, status, streak, execution_percentage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Запрос для получения всех записей о привычках из таблицы {@code habits}
     */
    public static final String FIND_ALL_HABITS = "SELECT * FROM app_schema.habits";

    /**
     * Запрос для поиска привычки по её ID в таблице {@code habits}
     */
    public static final String FIND_HABIT_BY_ID = "SELECT * FROM app_schema.habits WHERE id = ?";

    /**
     * Запрос для поиска привычки по имени в таблице {@code habits}
     */
    public static final String FIND_HABIT_BY_NAME = "SELECT * FROM app_schema.habits WHERE habit_name = ?";

    /**
     * Запрос для поиска всех привычек пользователя по его ID в таблице {@code habits}
     */
    public static final String FIND_HABIT_BY_USER_ID = "SELECT * FROM app_schema.habits WHERE user_id = ?";

    /**
     * Запрос для получения всех привычек пользователя из таблицы {@code habits}
     */
    public static final String FIND_HABITS_BY_USER = "SELECT * FROM app_schema.habits WHERE user_id = ?";

    /**
     * Запрос для обновления существующей привычки по ID в таблице {@code habits}
     */
    public static final String UPDATE_HABIT = "UPDATE app_schema.habits SET habit_name = ?, description = ?, frequency = ?, " +
            "start_date = ?, end_date = ?, execution_period = ?, status = ? , streak = ?, execution_percentage = ? WHERE id = ?";

    /**
     * Запрос для удаления всех привычек из таблицы {@code habits}
     */
    public static final String DELETE_ALL_HABIT = "DELETE FROM app_schema.habits";

    /**
     * Запрос для удаления привычки по её ID из таблицы {@code habits}
     */
    public static final String DELETE_HABIT = "DELETE FROM app_schema.habits WHERE id = ?";

    /**
     * Запрос для удаления всех привычек определенного пользователя из таблицы {@code habits}
     */
    public static final String DELETE_ALL_HABITS_FOR_USER = "DELETE FROM app_schema.habits WHERE user_id = ?";

    /**
     * Запрос для добавления записи о выполнении привычки в таблицу {@code habit_executions}
     */
    public static final String HABIT_MARK_EXECUTION = "INSERT INTO app_schema.habit_executions (habit_id, date) VALUES (?, ?)";

    /**
     * Запрос для получения всех дат выполнения привычки из таблицы {@code habit_executions}
     */
    public static final String QUERY_GET_EXECUTIONS = "SELECT date FROM app_schema.habit_executions WHERE habit_id = ?";

    /**
     * Запрос для удаления всех выполнений всех привычек в таблице {@code habit_executions}
     */
    public static final String RESET_EXECUTIONS_ALL_HABITS = "DELETE FROM app_schema.habit_executions";

    /**
     * Запрос для удаления всех выполнений для одной привычки по её ID в таблице {@code habit_executions}
     */
    public static final String RESET_EXECUTIONS_FOR_HABIT = "DELETE FROM app_schema.habit_executions WHERE habit_id = ?";

    /**
     * Запрос для удаления всех выполнений для всех привычек пользователя из таблицы {@code habit_executions}
     */
    public static final String RESET_ALL_EXECUTIONS_FOR_USER = "DELETE FROM app_schema.habit_executions WHERE user_id = ?";

    /**
     * Запрос для получения статистики по привычке за указанный период из таблицы {@code habit_executions}
     */
    public static final String GET_STATISTIC_BY_PERIOD = "SELECT DATE(date) as execution_date FROM app_schema.habit_executions WHERE habit_id = ?";

    /**
     * Запрос для вычисления процента выполнения привычки на основе данных из таблиц {@code habits} и {@code habit_executions}
     */
    public static final String QUERY_CALCULATE_EXECUTION_PERCENTAGE = "SELECT h.frequency, h.execution_period, COUNT(he.habit_id) AS execution_count "
            + "FROM app_schema.habits h LEFT JOIN app_schema.habit_executions he ON h.id = he.habit_id "
            + "WHERE h.id = ? "
            + "GROUP BY h.frequency, h.execution_period";

    /**
     * Запрос для получения последней даты выполнения привычки по её ID из таблицы {@code habit_executions}
     */
    public static final String QUERY_LAST_EXECUTION = "SELECT MAX(date) as last_execution_date FROM app_schema.habit_executions WHERE habit_id = ?";

    /**
     * Запрос для обновления стрика привычки по её ID в таблице {@code habits}
     */
    public static final String QUERY_UPDATE_STREAK = "UPDATE app_schema.habits SET streak = ? WHERE id = ?";

    /**
     * Запрос для проверки существования привычки у пользователя по его ID и имени привычки в таблице {@code habits}
     */
    public static final String QUERY_IS_HABIT_FOR_EXIST = "SELECT COUNT(*) FROM app_schema.habits WHERE user_id = ? AND habit_name = ?";

    /**
     * Запрос для сброса стрика для привычки по её ID в таблице {@code habits}
     */
    public static final String QUERY_RESET_STEAK = "UPDATE app_schema.habits SET streak = 0 WHERE id = ?";

    /**
     * Запрос для получения текущего стрика для привычки по её ID в таблице {@code habits}
     */
    public static final String QUERY_CURRENT_STREAK = "SELECT streak FROM app_schema.habits WHERE id = ?";
}


