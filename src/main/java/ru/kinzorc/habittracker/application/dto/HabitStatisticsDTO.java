package ru.kinzorc.habittracker.application.dto;

import ru.kinzorc.habittracker.core.entities.HabitStatistic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class HabitStatisticsDTO {

    private final long habitId;
    private final Set<LocalDateTime> streaks;
    private final List<LocalDateTime> executions;


    // Конструктор для создания DTO на основе данных из HabitStatistic
    public HabitStatisticsDTO(HabitStatistic habitStatistic, long habitId) {
        this.habitId = habitId;
        this.streaks = habitStatistic.getStreaks();
        this.executions = habitStatistic.getExecutions();
    }

    public HabitStatisticsDTO(long habitId) {
        this.habitId = habitId;
        this.streaks = new TreeSet<>();
        this.executions = new ArrayList<>();
    }

    // Конструктор для создания DTO на основе данных из ResultSet
    public HabitStatisticsDTO(ResultSet resultSet, Connection connection) throws SQLException {
        this.habitId = resultSet.getLong("habit_id");

        // Инициализация стриков и выполнений из базы данных
        this.streaks = loadStreaksFromDB(connection, habitId);
        this.executions = loadExecutionsFromDB(connection, habitId);
    }

    // ЛМетод для загрузки стриков из таблицы habit_streaks
    private Set<LocalDateTime> loadStreaksFromDB(Connection connection, long habitId) throws SQLException {
        Set<LocalDateTime> streaks = new TreeSet<>();
        String streaksQuery = "SELECT date_execution FROM app_schema.habit_streaks WHERE habit_id = ?";

        try (PreparedStatement streaksStatement = connection.prepareStatement(streaksQuery)) {
            streaksStatement.setLong(1, habitId);
            try (ResultSet rs = streaksStatement.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime dateExecution = rs.getTimestamp("date_execution").toLocalDateTime();
                    streaks.add(dateExecution);
                }
            }
        }

        return streaks;
    }

    // Метод для загрузки выполнений из таблицы habit_executions
    private List<LocalDateTime> loadExecutionsFromDB(Connection connection, long habitId) throws SQLException {
        List<LocalDateTime> executions = new ArrayList<>();
        String executionsQuery = "SELECT date FROM app_schema.habit_executions WHERE habit_id = ?";

        try (PreparedStatement executionsStatement = connection.prepareStatement(executionsQuery)) {
            executionsStatement.setLong(1, habitId);
            try (ResultSet rs = executionsStatement.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime executionDate = rs.getTimestamp("date").toLocalDateTime();
                    executions.add(executionDate);
                }
            }
        }

        return executions;
    }

    // Преобразование DTO обратно в сущность HabitStatistic
    public HabitStatistic toHabitStatistic() {
        HabitStatistic habitStatistic = new HabitStatistic();
        habitStatistic.setHabitId(this.habitId);
        habitStatistic.getStreaks().addAll(this.streaks);
        habitStatistic.getExecutions().addAll(this.executions);
        return habitStatistic;
    }
}