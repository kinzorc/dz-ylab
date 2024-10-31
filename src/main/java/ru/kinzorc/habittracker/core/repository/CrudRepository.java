package ru.kinzorc.habittracker.core.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Базовый интерфейс CRUD для работы с репозиториями.
 * <p>Содержит основные методы для добавления, обновления, удаления и поиска сущностей.</p>
 *
 * @param <T>  тип сущности
 * @param <ID> тип идентификатора сущности
 */
public interface CrudRepository<T, ID> {

    /**
     * Сохраняет новую сущность в репозитории.
     *
     * @param entity объект сущности для сохранения
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void save(T entity) throws SQLException;

    /**
     * Обновляет существующую сущность в репозитории.
     *
     * @param entity объект сущности с обновленными данными
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void update(T entity) throws SQLException;

    /**
     * Удаляет все сущности в репозитории
     *
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void deleteAll() throws SQLException;

    /**
     * Удаляет сущность по ее идентификатору.
     *
     * @param id идентификатор сущности
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void deleteById(ID id) throws SQLException;

    /**
     * Возвращает список всех сущностей в репозитории.
     *
     * @return список всех сущностей
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    List<T> findAll() throws SQLException;

    /**
     * Ищет сущность по ее идентификатору.
     *
     * @param id идентификатор сущности
     * @return {@code Optional} с объектом сущности, если найдена, иначе пустой {@code Optional}
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    Optional<T> findById(ID id) throws SQLException;
}