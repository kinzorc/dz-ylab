package ru.kinzorc.habittracker.core.entities;

import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс представляет привычку пользователя в системе.
 * <p>
 * Привычка включает уникальный идентификатор, имя, описание, частоту выполнения и статус активности.
 * Также содержит объект статистики {@link HabitStatistic}, который хранит информацию о выполнении привычки.
 * Привычка может иметь различные периоды выполнения (например, месяц или год) и поддерживает
 * отслеживание даты начала и окончания выполнения.
 * </p>
 */
public class Habit {

    /**
     * Объект статистики привычки.
     * <p>
     * Содержит данные о стриках, процентах выполнения и датах выполнений привычки.
     * </p>
     */
    private final HabitStatistic habitStatistic;
    /**
     * Дата создания привычки.
     * <p>
     * Определяется автоматически при создании привычки и не может быть изменена.
     * </p>
     */
    private final LocalDateTime createdDate;

    /**
     * Имя привычки.
     */
    private String name;

    /**
     * Описание привычки.
     */
    private String description;
    /**
     * Уникальный идентификатор привычки (ID).
     */
    private long id;
    /**
     * Частота выполнения привычки.
     * <p>
     * Определяется значениями перечисления {@link HabitFrequency}, которое указывает, как часто должна выполняться привычка.
     * </p>
     */
    private HabitFrequency frequency;
    /**
     * Статус привычки.
     * <p>
     * Указывает, активна привычка или нет. Определяется значениями перечисления {@link HabitStatus}.
     * </p>
     */
    private HabitStatus status;

    /**
     * Период выполнения привычки (например, месяц или год).
     */
    private HabitExecutionPeriod executionPeriod;

    /**
     * Дата начала выполнения привычки.
     */
    private LocalDateTime startDate;

    /**
     * Дата окончания выполнения привычки.
     */
    private LocalDateTime endDate;

    /**
     * Конструктор для создания новой привычки.
     * <p>
     * При создании новой привычки автоматически задаются идентификатор, статус, дата создания и создается объект статистики {@link HabitStatistic}.
     * Дата окончания выполнения рассчитывается на основе периода выполнения и даты начала.
     * </p>
     *
     * @param name            имя привычки
     * @param description     описание привычки
     * @param frequency       частота выполнения, определяется перечислением {@link HabitFrequency}
     * @param startDate       дата начала выполнения привычки
     * @param executionPeriod период выполнения привычки, определяется перечислением {@link HabitExecutionPeriod}
     */
    public Habit(String name, String description, HabitFrequency frequency, LocalDateTime startDate, HabitExecutionPeriod executionPeriod) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.createdDate = LocalDateTime.now();
        this.status = HabitStatus.ACTIVE;
        this.habitStatistic = new HabitStatistic();

        calculateEndDate(startDate, executionPeriod);
    }

    /**
     * Возвращает идентификатор привычки (ID).
     *
     * @return уникальный идентификатор привычки
     */
    public long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор привычки (ID).
     *
     * @param id новый идентификатор привычки
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Возвращает имя привычки.
     *
     * @return имя привычки
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает новое имя привычки.
     *
     * @param name новое имя привычки
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает описание привычки.
     *
     * @return описание привычки
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает новое описание привычки.
     *
     * @param description новое описание привычки
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Возвращает частоту выполнения привычки.
     *
     * @return частота выполнения привычки
     */
    public HabitFrequency getFrequency() {
        return frequency;
    }

    /**
     * Устанавливает новую частоту выполнения привычки.
     *
     * @param frequency новая частота выполнения привычки
     */
    public void setFrequency(HabitFrequency frequency) {
        this.frequency = frequency;
    }

    /**
     * Возвращает статус привычки (активна или нет).
     *
     * @return статус привычки
     */
    public HabitStatus getStatus() {
        return status;
    }

    /**
     * Устанавливает новый статус привычки.
     *
     * @param status новый статус привычки
     */
    public void setStatus(HabitStatus status) {
        this.status = status;
    }

    /**
     * Возвращает объект статистики привычки.
     *
     * @return объект статистики привычки {@link HabitStatistic}
     */
    public HabitStatistic getHabitStatistic() {
        return habitStatistic;
    }

    /**
     * Возвращает дату создания привычки.
     *
     * @return дата создания привычки
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Возвращает дату начала выполнения привычки.
     *
     * @return дата начала выполнения привычки
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Устанавливает новую дату начала выполнения привычки и пересчитывает дату окончания.
     *
     * @param startDate новая дата начала выполнения привычки
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        calculateEndDate(this.startDate, this.executionPeriod);
    }

    /**
     * Возвращает дату окончания выполнения привычки.
     *
     * @return дата окончания выполнения привычки
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Рассчитывает и устанавливает дату окончания выполнения привычки на основе периода выполнения.
     *
     * @param date                 дата начала выполнения
     * @param habitExecutionPeriod период выполнения (например, месяц или год)
     */
    private void calculateEndDate(LocalDateTime date, HabitExecutionPeriod habitExecutionPeriod) {
        switch (habitExecutionPeriod) {
            case MONTH -> endDate = date.plusMonths(1);
            case YEAR -> endDate = date.plusYears(1);
        }
    }

    /**
     * Возвращает период выполнения привычки.
     *
     * @return период выполнения привычки (например, месяц или год)
     */
    public HabitExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * Устанавливает новый период выполнения привычки и пересчитывает дату окончания.
     *
     * @param executionPeriod новый период выполнения привычки
     */
    public void setExecutionPeriod(HabitExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
        calculateEndDate(this.startDate, this.executionPeriod);
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
