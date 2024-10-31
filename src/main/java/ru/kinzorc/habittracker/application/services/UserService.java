package ru.kinzorc.habittracker.application.services;

import ru.kinzorc.habittracker.application.exceptions.InvalidPasswordException;
import ru.kinzorc.habittracker.application.exceptions.UserAlreadyExistsException;
import ru.kinzorc.habittracker.application.exceptions.UserNotFoundException;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.repository.SessionRepository;
import ru.kinzorc.habittracker.core.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления пользователями и сессиями в системе.
 * <p>
 * Этот класс предоставляет методы для создания, обновления, удаления пользователей, а также управления
 * их сессиями (вход, выход и обновление данных).
 * </p>
 */
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    /**
     * Конструктор для инициализации сервиса с репозиториями пользователей и сессий.
     *
     * @param userRepository    репозиторий для выполнения операций с пользователями
     * @param sessionRepository репозиторий для выполнения операций с сессиями пользователей
     */
    public UserService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Создает нового пользователя в системе.
     *
     * @param user пользователь для создания
     * @throws UserAlreadyExistsException если пользователь с данным email уже существует
     */
    public void createUser(User user) throws UserAlreadyExistsException {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("Пользователь с таким email уже зарегистрирован");
            }
            userRepository.save(user);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании пользователя: " + e.getMessage(), e);
        }
    }

    /**
     * Авторизует пользователя, если учетные данные верны.
     *
     * @param email    email пользователя
     * @param password пароль пользователя
     * @return идентификатор сессии пользователя или сообщение об ошибке
     * @throws UserNotFoundException    если пользователь с данным email не найден
     * @throws InvalidPasswordException если пароль неверен
     */
    public String login(String email, String password) throws UserNotFoundException, InvalidPasswordException {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с таким email не найден"));

            if (!user.getPassword().equals(password)) {
                throw new InvalidPasswordException("Неверный пароль");
            }

            String sessionId = UUID.randomUUID().toString();
            sessionRepository.createSession(user.getId(), sessionId);

            return sessionId;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении входа: " + e.getMessage(), e);
        }
    }

    /**
     * Завершает сессию пользователя, удаляя её из базы данных.
     *
     * @param sessionId идентификатор текущей сессии пользователя
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public void logoutUser(String sessionId) throws SQLException {
        try {
            sessionRepository.deleteSession(sessionId);
            System.out.println("Сессия пользователя успешно завершена.");
        } catch (SQLException e) {
            throw new SQLException("Ошибка при завершении сессии пользователя: " + e.getMessage());
        }
    }

    /**
     * Обновляет данные пользователя в системе.
     *
     * @param user пользователь с обновленными данными
     * @throws UserNotFoundException если пользователь с указанным ID не найден
     */
    public void updateUser(User user) throws UserNotFoundException {
        try {
            if (userRepository.findById(user.getId()).isEmpty()) {
                throw new UserNotFoundException("Пользователь с ID " + user.getId() + " не найден");
            }
            userRepository.update(user);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении пользователя: " + e.getMessage(), e);
        }
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @throws UserNotFoundException если пользователь с указанным ID не найден
     */
    public void deleteUser(Long userId) throws UserNotFoundException {
        try {
            if (userRepository.findById(userId).isEmpty()) {
                throw new UserNotFoundException("Пользователь с ID " + userId + " не найден");
            }
            userRepository.deleteById(userId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя: " + e.getMessage(), e);
        }
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список пользователей
     */
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка пользователей: " + e.getMessage(), e);
        }
    }

    /**
     * Поиск пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return объект пользователя
     * @throws UserNotFoundException если пользователь с указанным ID не найден
     */
    public User getUserById(Long userId) throws UserNotFoundException {
        try {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + userId + " не найден"));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя по ID: " + e.getMessage(), e);
        }
    }

    /**
     * Поиск пользователя по email.
     *
     * @param email email пользователя
     * @return объект пользователя
     * @throws UserNotFoundException если пользователь с указанным email не найден
     */
    public User getUserByEmail(String email) throws UserNotFoundException {
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя по email: " + e.getMessage(), e);
        }
    }

    /**
     * Поиск пользователя по имени пользователя.
     *
     * @param userName имя пользователя
     * @return объект пользователя
     * @throws UserNotFoundException если пользователь с указанным именем не найден
     */
    public User getUserByUserName(String userName) throws UserNotFoundException {
        try {
            return userRepository.findByUserName(userName)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с именем " + userName + " не найден"));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя по имени: " + e.getMessage(), e);
        }
    }
}