package ru.kinzorc.habittracker.core.repository;

import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Интерфейс для управления привычками и их статистикой в системе.
 * <p>
 * Определяет методы для добавления, обновления, удаления, выполнения привычек, а также получения статистики
 * по выполнению и управления сериями (стриками) выполнения.
 * </p>
 */
public interface HabitRepository extends CrudRepository<Habit, Long> {

    /**
     * Удаляет все привычки, добавленные указанным пользователем.
     *
     * @param user объект {@link User}, для которого удаляются все привычки
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    void deleteAllForUser(User user) throws SQLException;

    /**
     * Находит привычку по её имени.
     *
     * @param habitName имя привычки
     * @return объект {@link Optional} с привычкой, если она найдена, иначе пустой {@code Optional}
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    Optional<Habit> findByName(String habitName) throws SQLException;

    /**
     * Находит все привычки пользователя.
     *
     * @param user объект {@link User}, представляющий пользователя для поиска привычек
     * @return список привычек пользователя
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    List<Habit> findHabitsByUser(User user) throws SQLException;

    /**
     * Добавляет отметку о выполнении привычки на определённую дату.
     *
     * @param habit         объект привычки, для которой создается отметка о выполнении
     * @param executionDate дата выполнения привычки
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    void markExecution(Habit habit, LocalDate executionDate) throws SQLException;

    /**
     * Возвращает список дат выполнения привычки.
     *
     * @param id уникальный идентификатор привычки
     * @return список дат выполнений привычки
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    List<LocalDate> getExecutions(long id) throws SQLException;

    /**
     * Сбрасывает выполнения всех привычек всех пользователей.
     *
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    void resetAllExecutions() throws SQLException;

    /**
     * Сбрасывает выполнения всех привычек для указанного пользователя.
     *
     * @param user объект {@link User}, для которого сбрасываются все выполнения привычек
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    void resetAllExecutionsForUser(User user) throws SQLException;

    /**
     * Сбрасывает все выполнения для указанной привычки.
     *
     * @param id уникальный идентификатор привычки
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    void resetExecutions(long id) throws SQLException;

    /**
     * Возвращает статистику выполнения привычки за указанный период.
     *
     * @param habit           объект {@link Habit} привычки
     * @param startPeriodDate дата начала периода
     * @param endPeriodDate   дата окончания периода
     * @return карта, где ключ — дата, а значение — количество выполнений в этот день
     * @throws SQLException           в случае ошибок при работе с базой данных
     */
    Map<LocalDate, Integer> getStatisticByPeriod(Habit habit, LocalDate startPeriodDate, LocalDate endPeriodDate) throws SQLException;

    /**
     * Сбрасывает всю статистику выполнения привычки, позволяя сбросить действия или стрики (серии).
     *
     * @param id              уникальный идентификатор привычки
     * @param resetExecutions если true, сбрасываются все выполненные действия
     * @param resetStreaks    если true, сбрасываются все стрики (серии)
     * @throws SQLException           в случае ошибок при работе с базой данных
     */
    void resetStatistics(long id, boolean resetExecutions, boolean resetStreaks) throws SQLException;

    /**
     * Рассчитывает и обновляет текущий стрик (серия выполнений) привычки.
     *
     * @param habit            объект {@link Habit} привычки
     * @param newExecutionDate дата нового выполнения для обновления стрика
     * @return обновленный стрик привычки
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    int updateStreak(Habit habit, LocalDate newExecutionDate) throws SQLException;

    /**
     * Рассчитывает и процент выполнения привычки за указанный период.
     *
     * @param habit           объект привычки
     * @param startPeriodDate дата начала периода
     * @param endPeriodDate   дата окончания периода
     * @return обновленный процент выполнения привычки
     * @throws SQLException в случае ошибок при работе с базой данных
     */
    int calculateExecutionPercentageByPeriod(Habit habit, LocalDate startPeriodDate, LocalDate endPeriodDate) throws SQLException;
}