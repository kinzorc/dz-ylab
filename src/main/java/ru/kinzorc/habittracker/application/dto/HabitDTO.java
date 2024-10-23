package ru.kinzorc.habittracker.application.dto;

import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) для сущности Habit.
 * Используется для передачи данных между сервисами и репозиториями.
 */
public class HabitDTO {

    private long id;
    private String name;
    private String description;
    private HabitFrequency frequency;
    private LocalDateTime createdDate;
    private HabitStatus status;
    private HabitExecutionPeriod executionPeriod;

    // Конструктор на основе объекта Habit
    public HabitDTO(Habit habit) {
        this.id = habit.getId();
        this.name = habit.getName();
        this.description = habit.getDescription();
        this.frequency = habit.getFrequency();
        this.createdDate = habit.getHabitStatistic().getStartDate();
        this.status = habit.getStatus();
    }

    // Конструктор для создания из данных ResultSet (при запросе из БД)
    public HabitDTO(long id, String name, String description, HabitFrequency frequency, LocalDateTime createdDate, HabitStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.createdDate = createdDate;
        this.status = status;
    }

    // Преобразование HabitDTO обратно в сущность Habit
    public Habit toHabit() {
        Habit habit = new Habit(name, description, frequency, createdDate, executionPeriod);
        habit.setId(id);
        habit.setStatus(status);
        return habit;
    }

    // Геттеры и сеттеры

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

    public HabitExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(HabitExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    // Метод для преобразования объекта HabitDTO в массив параметров для SQL-запроса
    public Object[] toSqlParams() {
        return new Object[]{name, description, frequency.toString().toLowerCase(), createdDate, status.toString().toLowerCase(), startDate, endDate, executionPeriod.toString().toLowerCase()};
    }
}
}
