package ru.kinzorc.habittracker.core.repository;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Интерфейс {@code SessionRepository} определяет методы для управления пользовательскими сессиями в базе данных.
 * <p>
 * Интерфейс поддерживает операции по созданию, удалению, обновлению и поиску пользовательских сессий, что позволяет
 * вести учет активных сессий и времени последней активности пользователей.
 * </p>
 */
public interface SessionRepository {

    /**
     * Создает новую сессию для пользователя.
     *
     * @param userId    идентификатор пользователя
     * @param sessionId уникальный идентификатор сессии
     * @throws SQLException если возникает ошибка при выполнении операции в базе данных
     */
    void createSession(Long userId, String sessionId) throws SQLException;

    /**
     * Удаляет сессию по её идентификатору.
     *
     * @param sessionId уникальный идентификатор сессии, которую необходимо удалить
     * @throws SQLException если возникает ошибка при выполнении операции в базе данных
     */
    void deleteSession(String sessionId) throws SQLException;

    boolean sessionExists(String sessionId) throws SQLException;

    /**
     * Находит сессию по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя, чью сессию требуется найти
     * @return объект {@code Optional} с идентификатором сессии, если найдена, или пустой {@code Optional}, если сессия отсутствует
     * @throws SQLException если возникает ошибка при выполнении операции в базе данных
     */
    Optional<String> findSessionByUserId(Long userId) throws SQLException;

    /**
     * Обновляет время последней активности для сессии.
     *
     * @param sessionId уникальный идентификатор сессии, для которой требуется обновить последнюю активность
     * @throws SQLException если возникает ошибка при выполнении операции в базе данных
     */
    void updateLastActivity(String sessionId) throws SQLException;
}
