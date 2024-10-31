package ru.kinzorc.habittracker.infrastructure.repository.jdbc;

import ru.kinzorc.habittracker.application.mappers.HabitMapper;
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.repository.HabitRepository;
import ru.kinzorc.habittracker.infrastructure.repository.queries.HabitSQLStatements;
import ru.kinzorc.habittracker.infrastructure.repository.utils.DatabaseConnector;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Реализация интерфейса {@link HabitRepository} для работы с привычками через JDBC.
 * <p>
 * Этот класс взаимодействует с базой данных с помощью JDBC и предоставляет CRUD-операции
 * для привычек и управления их выполнениями, стриками и статистикой. В классе реализованы методы
 * для добавления, обновления, удаления, поиска привычек, а также для расчёта и сброса их статистики.
 * </p>
 */
public class JdbcHabitRepository implements HabitRepository {

    private final DatabaseConnector databaseConnector;

    /**
     * Конструктор для создания экземпляра {@code JdbcHabitRepository}.
     *
     * @param databaseConnector объект {@link DatabaseConnector} для управления соединениями с базой данных
     */
    public JdbcHabitRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Habit habit) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.ADD_HABIT)) {

            statement.setLong(1, habit.getUserId());
            statement.setString(2, habit.getName());
            statement.setString(3, habit.getDescription());
            statement.setString(4, habit.getFrequency().toString().toLowerCase());
            statement.setDate(5, Date.valueOf(LocalDate.now()));
            statement.setDate(6, Date.valueOf(habit.getStartDate()));
            statement.setDate(7, Date.valueOf(habit.getEndDate()));
            statement.setString(8, habit.getExecutionPeriod().toString().toLowerCase());
            statement.setString(9, habit.getStatus().toString().toLowerCase());
            statement.setInt(10, 0);
            statement.setInt(11, 0);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при создании привычки: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Habit habit) throws SQLException {

        Optional<Habit> existingHabit = findByName(habit.getName());
        boolean needsResetStreak = false;
        int currentStreak = 0;

        if (existingHabit.isPresent()) {
            needsResetStreak = !existingHabit.get().getStartDate().isEqual(habit.getStartDate())
                    || !(existingHabit.get().getStatus() == habit.getStatus())
                    || !(existingHabit.get().getExecutionPeriod() == habit.getExecutionPeriod());

            currentStreak = existingHabit.get().getStreak();
            habit.setId(existingHabit.get().getId());
        }

        int executionPercentage = calculateExecutionPercentageByPeriod(habit, habit.getStartDate(), habit.getEndDate());

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.UPDATE_HABIT)) {

            statement.setString(1, habit.getName());
            statement.setString(2, habit.getDescription());
            statement.setString(3, habit.getFrequency().toString().toLowerCase());
            statement.setDate(4, Date.valueOf(habit.getStartDate()));
            statement.setDate(5, Date.valueOf(habit.getEndDate()));
            statement.setString(6, habit.getExecutionPeriod().toString().toLowerCase());
            statement.setString(7, habit.getStatus().toString().toLowerCase());

            statement.setInt(8, !needsResetStreak ? currentStreak : 0);
            statement.setInt(9, executionPercentage);
            statement.setLong(10, habit.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Ошибка при обновлении привычки " + habit.getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.DELETE_ALL_HABIT)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при удалении всех привычек: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long habitId) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.DELETE_HABIT)) {

            statement.setLong(1, habitId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Ошибка при удалении привычки с ID " + habitId + ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllForUser(User user) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.DELETE_ALL_HABITS_FOR_USER)) {

            statement.setLong(1, user.getId());
            statement.executeUpdate();

            resetAllExecutionsForUser(user);
        } catch (SQLException e) {
            throw new SQLException("Ошибка при удалении привычек для пользователя " + user.getUserName() + ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markExecution(Habit habit, LocalDate executionDate) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.HABIT_MARK_EXECUTION)) {

            statement.setLong(1, habit.getId());
            statement.setDate(2, Date.valueOf(executionDate));

            statement.executeUpdate();

            updateStreak(habit, executionDate);
            calculateExecutionPercentageByPeriod(habit, habit.getStartDate(), habit.getEndDate());
        } catch (SQLException e) {
            throw new SQLException("Ошибка отметки выполнения привычки: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalDate> getExecutions(long habitId) throws SQLException {

        List<LocalDate> executions = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.QUERY_GET_EXECUTIONS)) {

            statement.setLong(1, habitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    executions.add(resultSet.getDate("date").toLocalDate());
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Ошибка получения списка выполнения привычек: " + e.getMessage());
        }

        return executions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetExecutions(long habitId) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.RESET_EXECUTIONS_FOR_HABIT)) {

            statement.setLong(1, habitId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Ошибка при сборе выполнений привычек: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetAllExecutionsForUser(User user) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.RESET_ALL_EXECUTIONS_FOR_USER)) {

            statement.setLong(1, user.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Ошибка при сбросе всех выполнений всех привычек для пользователя " + user.getUserName() + ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetAllExecutions() throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.RESET_EXECUTIONS_ALL_HABITS)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при сборе выполнения всех привычек в базе данных: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<LocalDate, Integer> getStatisticByPeriod(Habit habit, LocalDate startPeriodDate, LocalDate endPeriodDate)
            throws SQLException {

        Map<LocalDate, Integer> statistics = new HashMap<>();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.GET_STATISTIC_BY_PERIOD)) {

            statement.setLong(1, habit.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDate executionDate = resultSet.getDate("execution_date").toLocalDate();
                    statistics.put(executionDate, calculateExecutionPercentageByPeriod(habit,
                            habit.getStartDate(), executionDate));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при получении статистики за переданный период: " + e.getMessage());
        }

        return statistics;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetStatistics(long habitId, boolean resetExecutions, boolean resetStreaks) throws SQLException {
        try {
            if (resetExecutions)
                resetExecutions(habitId);

            if (resetStreaks)
                resetStreaks(habitId);
        } catch (SQLException e) {
            throw new SQLException("Ошибка при выполнении запросов на сброс стрика или выполнения привычки: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int calculateExecutionPercentageByPeriod(Habit habit, LocalDate startPeriodDate, LocalDate endPeriodDate) throws SQLException {

        if (startPeriodDate.isBefore(habit.getStartDate()))
            startPeriodDate = habit.getStartDate();

        if (endPeriodDate.isAfter(habit.getEndDate()))
            endPeriodDate = habit.getEndDate();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.QUERY_CALCULATE_EXECUTION_PERCENTAGE)) {

            statement.setLong(1, habit.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    long period = 0L;

                    String executionPeriod = resultSet.getString("execution_period");
                    String frequency = resultSet.getString("frequency");

                    if (executionPeriod.equals("month") || executionPeriod.equals("year")) {
                        if (frequency.equals("daily")) {
                            period = ChronoUnit.DAYS.between(startPeriodDate, endPeriodDate) + 1;
                        } else if (frequency.equals("weekly")) {
                            period = ChronoUnit.WEEKS.between(startPeriodDate, endPeriodDate) + 1;
                        }
                    }

                    if (period > 0) {
                        int executions = resultSet.getInt("execution_count");

                        return (int) ((executions / (double) period) * 100);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при подсчете процента успешного выполнения привычки: " + e.getMessage());
        }

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateStreak(Habit habit, LocalDate newExecutionDate) throws SQLException {

        int newStreak = 0;

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statementLastExecution = connection.prepareStatement(HabitSQLStatements.QUERY_LAST_EXECUTION);
             PreparedStatement statementCalculateStreak = connection.prepareStatement(HabitSQLStatements.QUERY_UPDATE_STREAK)) {

            statementLastExecution.setLong(1, habit.getId());

            try (ResultSet resultSet = statementLastExecution.executeQuery()) {
                if (resultSet.next()) {
                    LocalDate lastExecutionDate = resultSet.getDate("last_execution_date").toLocalDate();

                    long difference;
                    if (habit.getFrequency().toString().equalsIgnoreCase("daily")) {
                        difference = ChronoUnit.DAYS.between(lastExecutionDate, newExecutionDate);
                    } else if (habit.getFrequency().toString().equalsIgnoreCase("weekly")) {
                        difference = ChronoUnit.WEEKS.between(lastExecutionDate, newExecutionDate);
                    } else {
                        throw new IllegalArgumentException("Неверная частота выполнения: " + habit.getFrequency());
                    }

                    if (difference == 1) {
                        int currentStreak = getCurrentStreak(habit.getId());
                        newStreak = currentStreak + 1;

                        statementCalculateStreak.setInt(1, newStreak);
                    } else {
                        newStreak = 1;
                        statementCalculateStreak.setInt(1, 1);
                    }

                    statementCalculateStreak.setLong(2, habit.getId());
                    statementCalculateStreak.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при получении данных из базы данных: " + e.getMessage());
        }

        return newStreak;
    }

    /**
     * Получает текущее значение стрика для указанной привычки.
     * <p>
     * Выполняет запрос к базе данных для получения значения стрика (серии успешных выполнений) привычки с указанным идентификатором.
     * Если стрик не найден, возвращает значение по умолчанию 0.
     * </p>
     *
     * @param habitId уникальный идентификатор привычки, для которой требуется получить значение стрика
     * @return текущее значение стрика для привычки, или 0, если запись стрика отсутствует
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    private int getCurrentStreak(long habitId) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.QUERY_CURRENT_STREAK)) {
            statement.setLong(1, habitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("streak");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при получении текущего стрика привычки: " + e.getMessage());
        }

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Habit> findAll() throws SQLException {

        List<Habit> habits = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.FIND_ALL_HABITS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                habits.add(HabitMapper.INSTANCE.toEntity(HabitMapper.INSTANCE.fromResultSetToDTO(resultSet)));
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка получения информации по привычкам: " + e.getMessage());
        }

        return habits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Habit> findById(Long habitId) throws SQLException {

        Habit habit = null;

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.FIND_HABIT_BY_ID)) {

            statement.setLong(1, habitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    habit = HabitMapper.INSTANCE.toEntity(HabitMapper.INSTANCE.fromResultSetToDTO(resultSet));
                    habit.setId(resultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при получении привычки из базы данных, возможна привычка не найдена: " + e.getMessage());
        }

        return Optional.ofNullable(habit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Habit> findByName(String habitName) throws SQLException {

        Habit habit = null;

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.FIND_HABIT_BY_NAME)) {

            statement.setString(1, habitName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    habit = HabitMapper.INSTANCE.toEntity(HabitMapper.INSTANCE.fromResultSetToDTO(resultSet));
                    habit.setId(resultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при получении привычки из базы данных, возможна привычка не найдена: " + e.getMessage());
        }

        return Optional.ofNullable(habit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Habit> findHabitsByUser(User user) throws SQLException {

        List<Habit> habits = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.FIND_HABITS_BY_USER)) {

            statement.setLong(1, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    habits.add(HabitMapper.INSTANCE.toEntity(HabitMapper.INSTANCE.fromResultSetToDTO(resultSet)));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при запросе привычек пользователя: " + e.getMessage());
        }

        return habits;
    }

    /**
     * Проверяет существование привычки с заданным именем для указанного пользователя.
     * <p>
     * Выполняет запрос к базе данных для определения, существует ли привычка с указанным именем у пользователя с заданным идентификатором.
     * Возвращает {@code true}, если привычка существует, иначе — {@code false}.
     * </p>
     *
     * @param userId    уникальный идентификатор пользователя
     * @param habitName имя привычки, которую нужно проверить
     * @return {@code true}, если привычка с данным именем уже существует у пользователя, иначе {@code false}
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    public boolean isHabitExistForUser(long userId, String habitName) throws SQLException {
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.QUERY_IS_HABIT_FOR_EXIST)) {

            statement.setLong(1, userId);
            statement.setString(2, habitName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Пользователь ID " + userId + " и именем привычки " + habitName + " не найдены: " + e.getMessage());
        }

        return false;
    }

    /**
     * Сбрасывает значение стрика для привычки.
     * <p>
     * Выполняет запрос к базе данных, устанавливая значение стрика привычки на 0. Используется для обновления привычки при изменении условий отслеживания.
     * </p>
     *
     * @param id уникальный идентификатор привычки, для которой необходимо сбросить стрик
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных
     */
    private void resetStreaks(long id) throws SQLException {
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(HabitSQLStatements.QUERY_RESET_STEAK)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при сбросе стрика привычки с ID " + id + ": " + e.getMessage());
        }
    }
}