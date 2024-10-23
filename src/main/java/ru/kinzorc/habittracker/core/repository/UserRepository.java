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
 * Реализует операции по управлению пользователями, такими как блокировка, обновление привилегий и управление сессиями.
 * </p>
 */
public interface UserRepository {

    /**
     * Добавление нового пользователя в систему.
     * <p>
     * Метод добавляет объект {@link User} в репозиторий. Если пользователь с таким уникальным идентификатором уже существует,
     * выбрасывается исключение {@link UserAlreadyExistsException}.
     * </p>
     *
     * @param user объект {@link User} для добавления
     * @throws UserAlreadyExistsException если пользователь с таким ID уже существует
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void addUser(User user) throws UserAlreadyExistsException, SQLException;

    /**
     * Удаление пользователя по его идентификатору (id).
     * <p>
     * Если пользователь с переданным идентификатором не найден, выбрасывается исключение {@link UserNotFoundException}.
     * </p>
     *
     * @param userId уникальный идентификатор пользователя
     * @throws UserNotFoundException если пользователь с данным ID не найден
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void deleteUser(long userId) throws UserNotFoundException, SQLException;

    /**
     * Редактирование информации о пользователе.
     * <p>
     * Обновляет данные о пользователе на основе переданного параметра {@link UserData} и значения.
     * Если пользователь с таким идентификатором не найден, выбрасывается исключение {@link UserNotFoundException}.
     * </p>
     *
     * @param user     объект {@link User} с обновлёнными данными
     * @param userData тип данных пользователя, который будет обновлен (например, имя, email)
     * @param value    новое значение для указанных данных
     * @throws UserNotFoundException если пользователь с данным ID не найден
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
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
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void blockUser(long userId) throws UserNotFoundException, SQLException;

    /**
     * Обновление привилегий пользователя.
     * <p>
     * Обновляет роль пользователя (например, роль админа или обычного пользователя).
     * Если пользователь с таким идентификатором не найден, выбрасывается исключение {@link UserNotFoundException}.
     * </p>
     *
     * @param id       уникальный идентификатор пользователя
     * @param userRole новая роль для пользователя (user или admin)
     * @throws UserNotFoundException если пользователь с данным ID не найден
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void updateUserPrivileges(long id, UserRole userRole) throws UserNotFoundException, SQLException;

    /**
     * Поиск всех пользователей в системе.
     * <p>
     * Возвращает список всех зарегистрированных пользователей.
     * </p>
     *
     * @return список всех пользователей в системе
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    List<UserDTO> findAllUsers() throws SQLException;

    /**
     * Поиск пользователя по данным (id, username, email).
     * <p>
     * Возвращает объект {@link Optional} с пользователем, если пользователь с переданными данными найден.
     * В противном случае возвращается пустой {@code Optional}.
     * </p>
     *
     * @param userData тип данных для поиска (уникальный идентификатор, имя или email)
     * @param value    значение для поиска
     * @return {@code Optional} с объектом {@link UserDTO}, если пользователь найден, иначе пустой {@code Optional}
     * @throws UserNotFoundException если пользователь с указанными данными не найден
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    Optional<UserDTO> findUser(UserData userData, String value) throws UserNotFoundException, SQLException;

    /**
     * Добавление сессии для пользователя.
     * <p>
     * Метод добавляет сессию для пользователя с указанным идентификатором.
     * </p>
     *
     * @param userId уникальный идентификатор пользователя
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void addSession(long userId) throws SQLException;

    /**
     * Удаление сессии для пользователя.
     * <p>
     * Метод удаляет сессию для пользователя с указанным идентификатором.
     * </p>
     *
     * @param userId уникальный идентификатор пользователя
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    void removeSession(long userId) throws SQLException;

    /**
     * Проверка существования пользователя по данным.
     * <p>
     * Метод проверяет, существует ли пользователь с указанным идентификатором, именем или email в системе.
     * </p>
     *
     * @param userData тип данных для поиска (id, username, email)
     * @param value    значение для поиска
     * @return {@code true}, если пользователь существует, иначе {@code false}
     * @throws SQLException в случае возникновения ошибок при работе с базой данных
     */
    boolean isUserExist(UserData userData, String value) throws SQLException;
}
