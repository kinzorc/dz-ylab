package ru.kinzorc.habittracker.core.repository;

import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.HabitStatistic;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.Habit.HabitData;
import ru.kinzorc.habittracker.core.exceptions.HabitAlreadyExistsException;
import ru.kinzorc.habittracker.core.exceptions.HabitNotFoundException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Интерфейс для управления привычками и их статистикой.
 * <p>
 * Определяет основные методы для добавления, редактирования, удаления, выполнения привычек и работы со статистикой.
 * </p>
 */
public interface HabitRepository {

    /**
     * Добавляет новую привычку для пользователя.
     *
     * @param user  объект {@link User}, к которому добавляется привычка
     * @param habit объект {@link Habit}, представляющий новую привычку
     * @throws HabitAlreadyExistsException если привычка с таким ID уже существует
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void addHabit(User user, Habit habit) throws HabitAlreadyExistsException, SQLException;

    /**
     * Удаляет привычку по её уникальному идентификатору.
     *
     * @param id уникальный идентификатор привычки
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void deleteHabit(long id) throws HabitNotFoundException, SQLException;

    /**
     * Обновляет данные о существующей привычке.
     *
     * @param habit объект {@link Habit} с обновлёнными данными
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void editHabit(Habit habit) throws HabitNotFoundException, SQLException;

    /**
     * Добавляет отметку о выполнении привычки на указанную дату.
     *
     * @param id            уникальный идентификатор привычки
     * @param executionDate дата выполнения привычки
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void markExecution(long id, LocalDateTime executionDate) throws HabitNotFoundException, SQLException;

    /**
     * Сбрасывает статистику выполнения привычки.
     * <p>
     * Метод позволяет сбросить выполненные действия или стрики привычки по её уникальному идентификатору.
     * </p>
     *
     * @param id              уникальный идентификатор привычки
     * @param resetExecutions если true, сбрасываются все выполненные действия
     * @param resetStreaks    если true, сбрасываются все стрики (серии)
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void resetStatistics(long id, boolean resetExecutions, boolean resetStreaks) throws HabitNotFoundException, SQLException;

    /**
     * Возвращает список выполнений привычки по уникальному идентификатору.
     *
     * @param id уникальный идентификатор привычки
     * @return список дат выполнений привычки
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    List<LocalDateTime> getExecutions(long id) throws HabitNotFoundException, SQLException;

    /**
     * Возвращает стрики привычки по её уникальному идентификатору.
     *
     * @param id уникальный идентификатор привычки
     * @return карта, где ключ — дата, а значение — количество последовательных выполнений (стриков)
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     * @throws SQLException           в случае возникновения ошибок при работе с базой данных
     */
    Map<LocalDateTime, Integer> getSteaks(long id) throws HabitNotFoundException, SQLException;

    /**
     * Сбрасывает все выполненные действия привычки.
     *
     * @param id уникальный идентификатор привычки
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     * @throws SQLException           в случае возникновения ошибок при работе с базой данных
     */
    void resetExecutions(long id) throws HabitNotFoundException, SQLException;

    /**
     * Сбрасывает все стрики привычки.
     *
     * @param id уникальный идентификатор привычки
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void resetStreaks(long id) throws HabitNotFoundException, SQLException;

    /**
     * Возвращает список всех привычек в системе.
     *
     * @return список всех объектов {@link Habit}
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    List<Habit> findAllHabits() throws SQLException;

    /**
     * Поиск привычки по указанным данным.
     *
     * @param habitData тип данных для поиска (например, имя привычки)
     * @param value     значение для поиска
     * @return объект {@code Optional} с привычкой, если найдена, иначе пустой {@code Optional}
     * @throws HabitNotFoundException если привычка с указанными данными не найдена
     * @throws SQLException           в случае возникновения ошибок при работе с базой данных
     */
    Optional<Habit> findHabit(HabitData habitData, String value) throws HabitNotFoundException, SQLException;

    /**
     * Возвращает статистику выполнения привычки за указанный период.
     *
     * @param id              уникальный идентификатор привычки
     * @param startPeriodDate начало периода
     * @param endPeriodDate   конец периода
     * @return объект {@link HabitStatistic} с данными за указанный период
     * @throws HabitNotFoundException если привычка с данным ID не найдена
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    HabitStatistic getStatisticByPeriod(long id, LocalDateTime startPeriodDate, LocalDateTime endPeriodDate) throws HabitNotFoundException, SQLException;

    /**
     * Рассчитывает процент выполнения привычки по её данным.
     *
     * @param habitId уникальный идентификатор привычки
     * @return процент выполнения привычки
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    int calculateExecutionPercentage(long habitId) throws SQLException;

    /**
     * Проверяет, существует ли привычка с указанным именем в системе.
     *
     * @param habitName имя привычки
     * @return {@code true}, если привычка существует, иначе {@code false}
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    boolean isHabitExist(String habitName) throws SQLException;
}
