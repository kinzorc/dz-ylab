package ru.kinzorc.habittracker.core.entities;

import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс представляет сущность привычки пользователя в системе отслеживания привычек.
 * <p>
 * Привычка содержит уникальный идентификатор, имя, описание, частоту выполнения, статус,
 * период выполнения и даты, связанные с жизненным циклом привычки (создания, начала и окончания).
 * Кроме того, хранится список дат выполнения для отслеживания прогресса пользователя.
 * </p>
 */
public class Habit {

    /**
     * Уникальный идентификатор привычки.
     */
    private long id;

    /**
     * Уникальный идентификатор пользователя.
     */
    private long userId;

    /**
     * Имя привычки.
     */
    private String name;

    /**
     * Описание привычки.
     */
    private String description;

    /**
     * Частота выполнения привычки, задается значением {@link HabitFrequency}.
     */
    private HabitFrequency frequency;

    /**
     * Дата создания привычки, задается при инициализации.
     */
    private LocalDate createdDate;

    /**
     * Дата начала выполнения привычки.
     */
    private LocalDate startDate;

    /**
     * Дата окончания выполнения привычки, рассчитанная на основе периода выполнения.
     */
    private LocalDate endDate;

    /**
     * Период выполнения привычки, задается значением {@link HabitExecutionPeriod}.
     */
    private HabitExecutionPeriod executionPeriod;

    /**
     * Статус привычки (активна или завершена), задается значением {@link HabitStatus}.
     */
    private HabitStatus status;

    /**
     * Стрик (серия подряд выполненных дней).
     */
    private int streak;

    /**
     * Процент выполнения привычки.
     */
    private int executionPercentage;

    /**
     * Конструктор для создания новой привычки.
     * <p>
     * Поля даты создания, статуса и списка выполнений инициализируются по умолчанию.
     * Дата окончания рассчитывается на основе даты начала и периода выполнения.
     * </p>
     *
     * @param userId         уникальный идентификатор пользователя
     * @param name            имя привычки
     * @param description     описание привычки
     * @param frequency       частота выполнения (ежедневно, еженедельно), определяется перечислением {@link HabitFrequency}
     * @param startDate       дата начала выполнения привычки
     * @param executionPeriod период выполнения привычки (например, месяц или год), определяется перечислением {@link HabitExecutionPeriod}
     */
    public Habit(long userId, String name, String description, HabitFrequency frequency, LocalDate startDate, HabitExecutionPeriod executionPeriod) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.createdDate = LocalDate.now();
        this.startDate = startDate;
        this.executionPeriod = executionPeriod;
        this.status = HabitStatus.ACTIVE;
        this.streak = 0;
        this.executionPercentage = 0;

        calculateEndDate(startDate, executionPeriod);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        calculateEndDate(this.startDate, this.executionPeriod);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Рассчитывает дату окончания выполнения привычки на основе начальной даты и периода выполнения.
     * <p>
     * Использует значения {@link HabitExecutionPeriod#MONTH} и {@link HabitExecutionPeriod#YEAR}
     * для добавления одного месяца или года к дате начала.
     * </p>
     *
     * @param date                 начальная дата
     * @param habitExecutionPeriod период выполнения привычки
     */
    public void calculateEndDate(LocalDate date, HabitExecutionPeriod habitExecutionPeriod) {
        switch (habitExecutionPeriod) {
            case MONTH -> this.endDate = date.plusMonths(1);
            case YEAR -> this.endDate = date.plusYears(1);
        }
    }

    public HabitExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(HabitExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
        calculateEndDate(this.startDate, this.executionPeriod);
    }

    public HabitStatus getStatus() {
        return status;
    }

    public void setStatus(HabitStatus status) {
        this.status = status;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public int getExecutionPercentage() {
        return executionPercentage;
    }

    public void setExecutionPercentage(int execution_percentage) {
        this.executionPercentage = execution_percentage;
    }

    /**
     * Переопределяет метод {@code hashCode()} для генерации хеш-кода на основе идентификатора привычки.
     *
     * @return хеш-код привычки
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Переопределяет метод {@code equals()} для сравнения объектов по идентификатору.
     * <p>
     * Два объекта {@code Habit} считаются равными, если у них одинаковые идентификаторы.
     * </p>
     *
     * @param obj объект для сравнения
     * @return {@code true}, если объекты равны по идентификатору, иначе {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Habit habit = (Habit) obj;
        return id == habit.id;
    }

    /**
     * Возвращает строковое представление объекта привычки.
     *
     * @return строковое представление привычки
     */
    @Override
    public String toString() {
        return String.format("Привычка: %s, Описание: %s, Частота: %s, Дата создания: %s, Дата начала: %s, Период: %s, Дата окончания: %s, Статус: %s",
                name, description, frequency, createdDate, startDate, executionPeriod, endDate, status);
    }
}
