package ru.kinzorc.habittracker.infrastructure.repository.jdbc;

import ru.kinzorc.habittracker.application.dto.HabitDTO;
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.Habit.HabitData;
import ru.kinzorc.habittracker.core.exceptions.HabitAlreadyExistsException;
import ru.kinzorc.habittracker.core.exceptions.HabitNotFoundException;
import ru.kinzorc.habittracker.core.exceptions.UserNotFoundException;
import ru.kinzorc.habittracker.core.repository.HabitRepository;
import ru.kinzorc.habittracker.infrastructure.repository.utils.JdbcConnector;

import java.sql.*;
import java.util.Optional;

public class JdbcHabitRepository implements HabitRepository {

    private final JdbcConnector jdbcConnector;

    public JdbcHabitRepository(JdbcConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }

    @Override
    public void addHabit(User user, Habit habit) throws HabitAlreadyExistsException, SQLException {
        String sql = "INSERT INTO app_schema.habits (user_id, habit_name, description, frequency, created_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, user.getId());
            statement.setString(2, habit.getName());
            statement.setString(3, habit.getDescription());
            statement.setString(4, habit.getFrequency().toString().toLowerCase());
            statement.setTimestamp(5, Timestamp.valueOf(habit.getHabitStatistic().getStartDate()));
            statement.setString(6, habit.getStatus().toString().toLowerCase());

            int addRows = statement.executeUpdate();

            if (addRows == 0) {
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
    public Optional<Habit> findHabit(HabitData habitData, String value) throws HabitNotFoundException, SQLException {
        String sql = String.format("SELECT * FROM app_schema.habits WHERE %s = ?", habitData.toString().toLowerCase());
        Habit habit = null;

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            switch (habitData) {
                case ID -> statement.setObject(1, Long.parseLong(value));
                case NAME -> statement.setObject(1, value);
                default -> throw new IllegalStateException("Неверный параметр: " + habitData);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    habit = new HabitDTO(resultSet).toHabit();
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Пользователь не найден!");
        }

        if (Optional.ofNullable(userDTO).isEmpty()) {
            throw new UserNotFoundException(String.format("Пользователь по реквизиту %s: %s - не найден.", userData.toString(), value));
        }

        return Optional.of();
    }
}
