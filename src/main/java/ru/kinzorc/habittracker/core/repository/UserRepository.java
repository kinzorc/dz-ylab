package ru.kinzorc.habittracker.core.repository;

import ru.kinzorc.habittracker.application.dto.UserDTO;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.User.UserData;
import ru.kinzorc.habittracker.core.enums.User.UserRole;
import ru.kinzorc.habittracker.core.exceptions.UserAlreadyExistsException;
import ru.kinzorc.habittracker.core.exceptions.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с репозиторием пользователей.
 * <p>
 * Определяет основные методы для добавления, редактирования, удаления и поиска пользователей в системе.
 * </p>
 */
public interface UserRepository {

    /**
     * Добавление нового пользователя в систему.
     * <p>
     * Метод добавляет объект {@link User} в репозиторий. Если пользователь с таким уникальным идентификатором уже существует,
     * будет выброшено исключение {@link UserAlreadyExistsException}.
     * </p>
     *
     * @param user объект {@link User} для добавления
     * @throws UserAlreadyExistsException если пользователь с таким ID уже существует
     */
    void addUser(User user) throws UserAlreadyExistsException, SQLException;

    /**
     * Удаление пользователя по его идентификатору (id).
     * <p>
     * Если пользователь с переданным идентификатором не найден, будет выброшено исключение {@link UserNotFoundException}.
     * </p>
     *
     * @param userId уникальный идентификатор пользователя
     * @throws UserNotFoundException если пользователь с данным ID не найден
     */
    void deleteUser(long userId) throws UserNotFoundException, SQLException;

    /**
     * Редактирование информации о пользователе.
     * <p>
     * Обновляет данные о пользователе на основе переданного объекта {@link User}.
     * Если пользователь с таким идентификатором не найден, выбрасывается исключение {@link UserNotFoundException}.
     * </p>
     *
     * @param user объект {@link User} с обновлёнными данными
     * @throws UserNotFoundException если пользователь с данным ID не найден
     */
    void editUser(User user, UserData userData, String value) throws UserNotFoundException, SQLException;

    /**
     * Блокировка пользователя по его идентификатору (id).
     * <p>
     * Метод блокирует пользователя, делая его учетную запись недоступной для использования.
     * Если пользователь с таким идентификатором не найден, выбрасывается исключение {@link UserNotFoundException}.
     * </p>
     *
     * @param userId уникальный идентификатор пользователя
     * @throws UserNotFoundException если пользователь с данным ID не найден
     */
    void blockUser(long userId) throws UserNotFoundException, SQLException;

    /**
     * Обновление привилегий пользователя.
     * <p>
     * Обновляет роли и права доступа пользователя. Если пользователь с таким идентификатором не найден,
     * выбрасывается исключение {@link UserNotFoundException}.
     * </p>
     *
     * @param id       уникальный идентификатор пользователя
     * @param userRole новая роль для пользователя (user или admin)
     * @throws UserNotFoundException если пользователь с данным ID не найден
     */
    void updateUserPrivileges(long id, UserRole userRole) throws UserNotFoundException, SQLException;

    /**
     * Поиск всех пользователей в системе.
     * <p>
     * Возвращает список всех зарегистрированных пользователей.
     * </p>
     *
     * @return список всех пользователей в системе
     */
    List<UserDTO> findAllUsers() throws SQLException;

    /**
     * Поиск пользователя по данным (id, username, email).
     * <p>
     * Возвращает объект {@link Optional} с пользователем, если пользователь с переданными. В противном случае возвращается пустой {@code Optional}.
     * </p>
     *
     * @param userData тип реквизита по которому осуществлять поиск - уникальный идентификатор пользователя, имя, email
     * @param value    значение по которому осуществлять поиск
     * @return {@code Optional} с объектом {@link User}, если пользователь найден, иначе пустой {@code Optional}
     */
    Optional<UserDTO> findUser(UserData userData, String value) throws UserNotFoundException, SQLException;

    void addSession(long userId) throws SQLException;

    void removeSession(long userId) throws SQLException;

    boolean isUserExist(UserData userData, String value) throws SQLException;

}