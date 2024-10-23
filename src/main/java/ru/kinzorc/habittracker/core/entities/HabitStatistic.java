package ru.kinzorc.habittracker.core.entities;

import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Класс представляет статистику выполнения привычки.
 * <p>
 * Включает данные о дате начала и окончания выполнения привычки, периоде выполнения,
 * проценте выполнения, а также содержит информацию о выполнениях привычки по дням и истории выполнения.
 * </p>
 */
public class HabitStatistic {

    /**
     * Коллекция для временного хранения стрика по датам,
     * количество дат в коллекции указыает на количество стриков выполнения.
     * При пропуске выполнения данная (пропуск нескольких дней или недель) коллекция очищается и добавляется единственная запись с текущей датой выполнения,
     * т.е. стрик равен 1
     * <p>
     * Хранит даты, каждая идущая дата подряд от дня или недели, равняется одному стрику.
     * </p>
     */
    private final Set<LocalDateTime> streaks;
    /**
     * Список дат, когда привычка была выполнена.
     */
    private final List<LocalDateTime> executions;
    /**
     * Дата начала выполнения привычки.
     */
    private LocalDateTime startDate;
    /**
     * Дата окончания выполнения привычки.
     */
    private LocalDateTime endDate;
    /**
     * Период выполнения привычки (например, месяц или год).
     * <p>
     * Определяется значениями перечисления {@link HabitExecutionPeriod}.
     * </p>
     */
    private HabitExecutionPeriod executionPeriod;
    /**
     * Процент выполнения привычки за определенный период.
     */
    private int executionPercentage;

    /**
     * Конструктор для создания объекта статистики привычки.
     * <p>
     * При создании объекта автоматически рассчитывается дата окончания выполнения привычки на основе
     * даты начала и периода выполнения привычки.
     * </p>
     *
     * @param startDate       дата начала выполнения привычки
     * @param executionPeriod период выполнения привычки (например, месяц или год)
     */
    public HabitStatistic(LocalDateTime startDate, HabitExecutionPeriod executionPeriod) {
        this.startDate = startDate;
        this.executionPeriod = executionPeriod;
        this.streaks = new TreeSet<>();
        this.executions = new ArrayList<>();

        calculateEndDate(startDate, executionPeriod);
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
     * Рассчитывает и устанавливает дату окончания привычки в зависимости от периода выполнения.
     *
     * @param date                 дата начала выполнения
     * @param habitExecutionPeriod период выполнения (месяц или год)
     */
    private void calculateEndDate(LocalDateTime date, HabitExecutionPeriod habitExecutionPeriod) {
        switch (habitExecutionPeriod) {
            case MONTH -> endDate = date.plusMonths(1);
            case YEAR -> endDate = date.plusYears(1);
        }
    }

    /**
     * Возвращает период выполнения привычки (месяц или год).
     *
     * @return период выполнения привычки
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
     * Возвращает коллекцию выполнений привычки по датам.
     * <p>
     * Каждая дата в коллекции представляет день, когда привычка была успешно выполнена.
     * </p>
     *
     * @return коллекция выполнений привычки
     */
    public Set<LocalDateTime> getStreaks() {
        return streaks;
    }

    /**
     * Сбрасывает информацию о выполнениях привычки.
     * <p>
     * Удаляет все даты из коллекции выполнений.
     * </p>
     */
    public void resetStreaks() {
        this.streaks.clear();
    }

    /**
     * Возвращает список дат, когда привычка была выполнена.
     * <p>
     * Каждая дата в списке представляет день, когда привычка была выполнена.
     * </p>
     *
     * @return список выполнений привычки
     */
    public List<LocalDateTime> getExecutions() {
        return executions;
    }

    /**
     * Сбрасывает список выполнений привычки.
     * <p>
     * Очищает список всех дат выполнения привычки.
     * </p>
     */
    public void resetExecutions() {
        this.executions.clear();
    }

    /**
     * Возвращает процент выполнения привычки за определенный период.
     *
     * @return процент выполнения привычки
     */
    public int getExecutionPercentage() {
        return executionPercentage;
    }

    /**
     * Устанавливает процент выполнения привычки.
     *
     * @param percentage новый процент выполнения привычки
     */
    public void setExecutionPercentage(int percentage) {
        this.executionPercentage = percentage;
    }
}
