package ru.kinzorc.habittracker.application.dto;

import java.time.LocalDate;

/**
 * Класс Data Transfer Object (DTO) для передачи данных привычки между слоями приложения.
 * <p>
 * Этот класс используется для представления привычки в виде простого объекта данных,
 * который может быть легко передан между слоями приложения (например, между сервисом и контроллером)
 * или при работе с базой данных.
 * </p>
 */
public class HabitDTO {

    /**
     * Уникальный идентификатор привычки.
     */
    public long id;

    /**
     * Идентификатор пользователя, к которому принадлежит привычка.
     */
    public long userId;

    /**
     * Имя привычки.
     */
    public String name;

    /**
     * Описание привычки.
     */
    public String description;

    /**
     * Частота выполнения привычки.
     * </p>
     */
    public String frequency;

    /**
     * Дата создания привычки.
     */
    public LocalDate createdDate;

    /**
     * Дата начала выполнения привычки.
     */
    public LocalDate startDate;

    /**
     * Дата окончания выполнения привычки.
     */
    public LocalDate endDate;

    /**
     * Период выполнения привычки.
     */
    public String executionPeriod;

    /**
     * Статус привычки.
     */
    public String status;

    /**
     * Текущий стрик (серия выполнений) привычки.
     */
    public int streak;

    /**
     * Процент выполнения привычки.
     */
    public int executionPercentage;

}
