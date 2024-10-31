package ru.kinzorc.habittracker.core.repository;

import ru.kinzorc.habittracker.core.entities.User;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Интерфейс для работы с репозиторием пользователей.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Поиск пользователя по имени.
     * <p>
     * Возвращает объект {@link Optional} с пользователем, если пользователь с переданным именем найден.
     * В противном случае возвращается пустой {@code Optional}.
     * </p>
     *
     * @param userName значение для поиска
     * @return {@code Optional} с объектом {@link User}, если пользователь найден, иначе пустой {@code Optional}
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    Optional<User> findByUserName(String userName) throws SQLException;

    /**
     * Поиск пользователя по email.
     * <p>
     * Возвращает объект {@link Optional} с пользователем, если пользователь с переданным email найден.
     * В противном случае возвращается пустой {@code Optional}.
     * </p>
     *
     * @param userEmail значение для поиска
     * @return {@code Optional} с объектом {@link User}, если пользователь найден, иначе пустой {@code Optional}
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    Optional<User> findByEmail(String userEmail) throws SQLException;
}