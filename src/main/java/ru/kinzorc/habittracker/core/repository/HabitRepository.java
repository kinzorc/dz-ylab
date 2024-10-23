package ru.kinzorc.habittracker.core.repository;

import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.HabitStatistic;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.Habit.HabitData;
import ru.kinzorc.habittracker.core.exceptions.HabitAlreadyExistsException;
import ru.kinzorc.habittracker.core.exceptions.HabitNotFoundException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Интерфейс для управления привычками и их статистикой.
 */
public interface HabitRepository {

    /**
     * Добавляет новую привычку в систему.
     *
     * @param habit объект Habit, представляющий новую привычку
     * @throws HabitAlreadyExistsException если привычка с таким ID уже существует
     */
    void addHabit(User user, Habit habit) throws HabitAlreadyExistsException, SQLException;

    /**
     * Удаляет привычку по её идентификатору.
     *
     * @param id уникальный идентификатор привычки
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     */
    void deleteHabit(long id) throws HabitNotFoundException;

    /**
     * Обновляет данные о существующей привычке.
     *
     * @param habit объект Habit с новыми данными
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     */
    void editHabit(Habit habit) throws HabitNotFoundException;

    /**
     * Добавляет отметку о выполнении привычки на указанную дату.
     *
     * @param id            уникальный идентификатор привычки
     * @param executionDate дата выполнения привычки
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     */
    void markExecution(long id, LocalDate executionDate) throws HabitNotFoundException, SQLException;

    /**
     * Сбрасывает статистику привычки.
     *
     * @param id              уникальный идентификатор привычки
     * @param resetExecutions если true, сбрасываются все выполненные действия
     * @param resetStreaks    если true, сбрасываются все стрики (серии)
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     */
    void resetStatistics(long id, boolean resetExecutions, boolean resetStreaks) throws HabitNotFoundException, SQLException;

    /**
     * Возвращает список всех привычек.
     *
     * @return список всех объектов Habit
     */
    List<Habit> findAllHabits() throws SQLException;


    Optional<Habit> findHabit(HabitData habitData, String value) throws HabitNotFoundException, SQLException;


    /**
     * Возвращает статистику выполнения привычки за указанный период.
     *
     * @param id              уникальный идентификатор привычки
     * @param startPeriodDate начало периода
     * @param endPeriodDate   конец периода
     * @return объект HabitStatistics с данными за указанный период
     */
    HabitStatistic getStatisticByPeriod(long id, LocalDateTime startPeriodDate, LocalDateTime endPeriodDate) throws HabitNotFoundException, SQLException;

    /**
     * Рассчитывает процент выполнения привычки на основании её данных.
     *
     * @param id уникальный идентификатор привычки
     * @return процент выполнения привычки
     */
    int calculateExecutionPercentage(long id) throws SQLException;

    boolean isHabitExist(String habitName) throws SQLException;
}