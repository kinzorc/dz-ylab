package ru.kinzorc.habittracker.core.entities;

import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс представляет привычку пользователя в системе.
 * <p>
 * Привычка имеет уникальный идентификатор (UUID), имя, описание, частоту выполнения,
 * статус активности, а также включает статистику выполнения (через {@link HabitStatistic}),
 * которая хранит даты создания, начала и окончания привычки.
 * Привычка также может иметь активный или неактивный статус, который указывает на текущее состояние привычки.
 * </p>
 */
public class Habit {

    /**
     * Объект статистики привычки.
     * <p>
     * Включает информацию о периодах выполнения привычки, дате начала и окончания.
     * </p>
     */
    private final HabitStatistic habitStatistic;
    /**
     * Дата создания привычки.
     * <p>
     * Определяется автоматически при создании объекта привычки и не может быть изменена.
     * </p>
     */
    private final LocalDateTime createdDate;
    /**
     * Идентификатор привычки (id).
     */
    private long id;
    /**
     * Имя привычки.
     */
    private String name;
    /**
     * Описание привычки.
     */
    private String description;
    /**
     * Частота выполнения привычки.
     * <p>
     * Определяется значениями перечисления {@link HabitFrequency}, которые указывают,
     * как часто должна выполняться привычка (ежедневно, еженедельно и т.д.).
     * </p>
     */
    private HabitFrequency frequency;
    /**
     * Статус привычки.
     * <p>
     * Определяется значениями перечисления {@link HabitStatus}, которое указывает на текущий статус привычки
     * (например, активна или нет).
     * </p>
     */
    private HabitStatus status;

    /**
     * Конструктор для создания новой привычки.
     * <p>
     * При создании новой привычки автоматически генерируется уникальный идентификатор (UUID),
     * устанавливаются начальные значения для имени, описания, частоты и других атрибутов привычки.
     * Также создается объект статистики {@link HabitStatistic}, который рассчитывает дату окончания привычки
     * на основе даты начала и периода выполнения.
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
        this.habitStatistic = new HabitStatistic(startDate, executionPeriod);
    }

    /**
     * Возвращает идентификатор привычки (id).
     *
     * @return уникальный идентификатор привычки
     */
    public long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор привычки (id).
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
     * Возвращает объект статистики по привычке (выполнения привычки, стрики, процент выполнения и т.д.).
     *
     * @return объект статистики по привычке
     */
    public HabitStatistic getHabitStatistic() {
        return this.habitStatistic;
    }

    /**
     * Переопределение метода {@code hashCode()} для генерации хеш-кода на основе уникального идентификатора привычки.
     *
     * @return хеш-код для объекта привычки
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Переопределение метода {@code equals()} для сравнения объектов по уникальному идентификатору.
     * <p>
     * Два объекта {@code Habit} считаются равными, если у них одинаковые идентификаторы.
     * </p>
     *
     * @param obj объект для сравнения
     * @return {@code true}, если объекты равны, иначе {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Habit habit = (Habit) obj;
        return Objects.equals(id, habit.id);
    }

    /**
     * Возвращает строковое представление объекта привычки.
     *
     * @return строковое представление привычки
     */
    @Override
    public String toString() {
        return String.format("Привычка: %s, Описание: %s, Частота: %s, Дата создания: %s, Дата начала: %s, Период: %s, Дата окончания: %s, Статус: %s",
                name, description, frequency, createdDate, habitStatistic.getStartDate(), habitStatistic.getExecutionPeriod(), habitStatistic.getEndDate(), status);
    }
}
