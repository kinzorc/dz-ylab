package ru.kinzorc.habittracker.infrastructure.repository.jdbc;

import ru.kinzorc.habittracker.application.dto.HabitDTO;
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.HabitStatistic;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.Habit.HabitData;
import ru.kinzorc.habittracker.core.exceptions.HabitAlreadyExistsException;
import ru.kinzorc.habittracker.core.exceptions.HabitNotFoundException;
import ru.kinzorc.habittracker.core.repository.HabitRepository;
import ru.kinzorc.habittracker.infrastructure.repository.utils.JdbcConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class JdbcHabitRepository implements HabitRepository {

    private final JdbcConnector jdbcConnector;

    public JdbcHabitRepository(JdbcConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }

    @Override
    public void addHabit(User user, Habit habit) throws HabitAlreadyExistsException, SQLException {
        String sql = "INSERT INTO app_schema.habits (user_id, habit_name, description, frequency, start_date, end_date, execution_period, created_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (isHabitExist(habit.getName()))
                throw new HabitAlreadyExistsException("Уже есть такая привычка!");

            statement.setLong(1, user.getId());
            statement.setString(2, habit.getName());
            statement.setString(3, habit.getDescription());
            statement.setString(4, habit.getFrequency().toString().toLowerCase());
            statement.setTimestamp(5, Timestamp.valueOf(habit.getStartDate()));
            statement.setTimestamp(6, Timestamp.valueOf(habit.getEndDate()));
            statement.setString(7, habit.getExecutionPeriod().toString().toLowerCase());
            statement.setTimestamp(8, Timestamp.valueOf(habit.getCreatedDate()));
            statement.setString(6, habit.getStatus().toString().toLowerCase());

            int rowsInserted = statement.executeUpdate();


            if (rowsInserted == 0) {
                throw new SQLException("Создание привычки не удалось.");
            }

            // Получаем сгенерированный id привычки
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    habit.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Создание привычки не удалось, невозможно получить ID.");
                }
            }

        }
    }

    @Override
    public void deleteHabit(long id) throws HabitNotFoundException, SQLException {
        String sql = "DELETE FROM app_schema.habits WHERE id = ?";

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted == 0) {
                throw new HabitNotFoundException("Не найдена привычка с таким id: " + id);
            }
        }
    }

    @Override
    public void editHabit(Habit habit) throws HabitNotFoundException, SQLException {
        String sql = "UPDATE app_schema.habits SET habit_name = ?, description = ?, frequency = ?, start_date = ?, end_date = ? execution_period = ?, status = ? WHERE id = ?";

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, habit.getName());
            statement.setString(2, habit.getDescription());
            statement.setString(3, habit.getFrequency().toString().toLowerCase());
            statement.setTimestamp(4, Timestamp.valueOf(habit.getStartDate()));
            statement.setTimestamp(5, Timestamp.valueOf(habit.getEndDate()));
            statement.setString(6, habit.getExecutionPeriod().toString().toLowerCase());
            statement.setString(7, habit.getStatus().toString().toLowerCase());
            statement.setLong(8, habit.getId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new HabitNotFoundException("Не найдена привычка с таким id: " + habit.getId());
            }
        }
    }

    @Override
    public void markExecution(long id, LocalDateTime executionDate) throws SQLException {
        String sql = "INSERT INTO app_schema.habit_executions (habit_id, date) VALUES (?, ?)";

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setTimestamp(2, Timestamp.valueOf(executionDate));
            statement.executeUpdate();
        }
    }

    @Override
    public void resetStatistics(long id, boolean resetExecutions, boolean resetStreaks) throws HabitNotFoundException, SQLException {
        if (resetExecutions) {
            resetExecutions(id);
        }

        if (resetStreaks) {
            resetStreaks(id);
        }
    }

    @Override
    public List<LocalDateTime> getExecutions(long id) throws HabitNotFoundException, SQLException {
        String sql = "SELECT date FROM app_schema.habit_executions WHERE habit_id = ?";
        List<LocalDateTime> executions = new ArrayList<>();

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDateTime executionDate = resultSet.getTimestamp("date").toLocalDateTime();
                    executions.add(executionDate);
                }
            }
        }

        return executions;
    }

    @Override
    public Map<LocalDateTime, Integer> getSteaks(long id) throws HabitNotFoundException, SQLException {
        String sql = "SELECT date_execution, count FROM app_schema.habit_streaks WHERE habit_id = ?";
        Map<LocalDateTime, Integer> streaks = new HashMap<>();

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDateTime dateExecution = resultSet.getTimestamp("date_execution").toLocalDateTime();
                    int count = resultSet.getInt("count");
                    streaks.put(dateExecution, count);
                }
            }
        }

        return streaks;
    }

    @Override
    public void resetExecutions(long id) throws HabitNotFoundException, SQLException {
        String sql = "DELETE FROM app_schema.habit_executions WHERE habit_id = ?";

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void resetStreaks(long id) throws HabitNotFoundException, SQLException {
        String sql = "DELETE FROM app_schema.habit_streaks WHERE habit_id = ?";

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Habit> findAllHabits() throws SQLException {
        String sql = "SELECT * FROM app_schema.habits";
        List<Habit> habits = new ArrayList<>();

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                HabitDTO habitDTO = new HabitDTO(resultSet);
                habits.add(habitDTO.toHabit());
            }
        }

        return habits;
    }

    @Override
    public Optional<Habit> findHabit(HabitData habitData, String value) throws HabitNotFoundException, SQLException {
        String sql = String.format("SELECT * FROM app_schema.habits WHERE %s = ?", habitData.toString().toLowerCase());
        Habit habit = null;

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    HabitDTO habitDTO = new HabitDTO(resultSet);
                    habit = habitDTO.toHabit();
                }
            }
        }

        if (habit == null) {
            throw new HabitNotFoundException("Привычка не найдена.");
        }

        return Optional.of(habit);
    }

    @Override
    public HabitStatistic getStatisticByPeriod(long id, LocalDateTime startPeriodDate, LocalDateTime endPeriodDate) throws HabitNotFoundException, SQLException {
        String sql = "SELECT * FROM app_schema.habit_executions WHERE habit_id = ? AND date BETWEEN ? AND ?";
        HabitStatistic habitStatistic = new HabitStatistic();

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setTimestamp(2, Timestamp.valueOf(startPeriodDate));
            statement.setTimestamp(3, Timestamp.valueOf(endPeriodDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDateTime executionDate = resultSet.getTimestamp("date").toLocalDateTime();
                    habitStatistic.getExecutions().add(executionDate);
                }
            }
        }

        return habitStatistic;
    }

    @Override
    public int calculateExecutionPercentage(long habitId) throws SQLException {
        String sql_first = "SELECT execution_period AS period FROM app_schema.habits WHERE habit_id = ?";
        String sql_second = "SELECT COUNT(*) AS count FROM app_schema.habit_executions WHERE habit_id = ?";

        int percentage = 0;
        int period = getPeriod(habitId, sql_first);

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql_second)) {
            statement.setLong(1, habitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");

                    if (period > 0) {
                        percentage = (count * 100) / period;
                    } else {
                        throw new SQLException("Невозможно высчитать успешный процент выполнения, по причине: " +
                                "или привычка не выполнялась или указан неверный период выполнения");
                    }
                }
            }
        }

        return percentage;
    }

    private int getPeriod(long habitId, String sql_first) throws SQLException {
        int period = 0;

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql_first)) {
            statement.setLong(1, habitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String strPeriod = resultSet.getString("period");

                    if (strPeriod.equals("month")) {
                        period = 30;
                    } else {
                        period = 365;
                    }
                }
            }
        }
        return period;
    }

    @Override
    public boolean isHabitExist(String habitName) throws SQLException {
        String sql = "SELECT 1 FROM app_schema.habits WHERE habit_name = ?";

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, habitName);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
