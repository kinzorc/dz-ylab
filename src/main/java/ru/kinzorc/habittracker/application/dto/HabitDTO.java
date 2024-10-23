package ru.kinzorc.habittracker.application.dto;

import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.HabitStatistic;
import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class HabitDTO {

    private long id;
    private String name;
    private String description;
    private HabitFrequency frequency;
    private LocalDateTime createdDate;
    private HabitStatus status;
    private HabitExecutionPeriod executionPeriod;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private HabitStatistic habitStatistic;

    // Конструктор для создания DTO на основе данных из Habit
    public HabitDTO(Habit habit) {
        this.id = habit.getId();
        this.name = habit.getName();
        this.description = habit.getDescription();
        this.frequency = habit.getFrequency();
        this.createdDate = habit.getCreatedDate();
        this.status = habit.getStatus();
        this.executionPeriod = habit.getExecutionPeriod();
        this.startDate = habit.getStartDate();
        this.endDate = habit.getEndDate();
    }

    // Конструктор для создания DTO на основе данных из ResultSet
    public HabitDTO(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.name = resultSet.getString("habit_name");
        this.description = resultSet.getString("description");
        this.frequency = HabitFrequency.valueOf(resultSet.getString("frequency").toUpperCase());
        this.createdDate = resultSet.getTimestamp("created_date").toLocalDateTime();
        this.status = HabitStatus.valueOf(resultSet.getString("status").toUpperCase());
        this.executionPeriod = HabitExecutionPeriod.valueOf(resultSet.getString("execution_period").toUpperCase());
        this.startDate = resultSet.getTimestamp("start_date").toLocalDateTime();
        this.endDate = resultSet.getTimestamp("end_date").toLocalDateTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HabitFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(HabitFrequency frequency) {
        this.frequency = frequency;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public HabitStatus getStatus() {
        return status;
    }

    public void setStatus(HabitStatus status) {
        this.status = status;
    }

    public HabitExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(HabitExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public HabitStatistic getHabitStatistic() {
        return habitStatistic;
    }

    public void setHabitStatistic(HabitStatistic habitStatistic) {
        this.habitStatistic = habitStatistic;
    }

    // Преобразование DTO обратно в сущность Habit
    public Habit toHabit() {
        Habit habit = new Habit(name, description, frequency, startDate, executionPeriod);
        habit.setId(id);
        habit.setStatus(status);
        return habit;
    }

    // Метод для передачи данных в SQL-запрос
    public Object[] toSqlParams() {
        return new Object[]{name, description, frequency.toString().toLowerCase(), startDate, endDate,
                executionPeriod.toString().toLowerCase(), createdDate, status.toString().toLowerCase()};
    }
}