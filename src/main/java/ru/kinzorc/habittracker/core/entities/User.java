package ru.kinzorc.habittracker.core.entities;

import ru.kinzorc.habittracker.core.enums.User.UserRole;
import ru.kinzorc.habittracker.core.enums.User.UserStatusAccount;

import java.util.HashMap;
import java.util.Objects;

/**
 * Класс представляет сущность пользователя в системе.
 * <p>
 * Пользователь обладает уникальным идентификатором (id), именем, паролем, email,
 * а также ролями и статусами учетной записи и активности.
 * </p>
 * <p>
 * Пользователь может иметь несколько привычек, каждая из которых хранится в коллекции {@code habits}.
 * Отношение между классами {@link User} и {@link Habit} — композиция, то есть объекты класса {@link Habit}
 * создаются и управляются только пользователем и не могут существовать отдельно.
 * </p>
 */
public class User {

    /**
     * Коллекция привычек пользователя.
     * <p>
     * Хранит пары ключ-значение, где ключ — это уникальный идентификатор привычки, а значение — объект {@link Habit}.
     * </p>
     */
    private final HashMap<Long, Habit> habits;

    /**
     * Идентификатор пользователя (id).
     */
    private long id;

    /**
     * Имя пользователя (username).
     */
    private String username;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Электронная почта пользователя.
     */
    private String email;

    /**
     * Роль пользователя в системе.
     * <p>
     * Определяется значениями перечисления {@link UserRole}, которые обозначают роль пользователя (например, администратор или обычный пользователь).
     * </p>
     */
    private UserRole userRole;

    /**
     * Статус учетной записи пользователя.
     * <p>
     * Может принимать значения из перечисления {@link UserStatusAccount}, указывая на состояние аккаунта (например, активен или заблокирован).
     * </p>
     */
    private UserStatusAccount userStatusAccount;

    /**
     * Конструктор для создания нового пользователя.
     * <p>
     * При создании пользователя автоматически генерируется уникальный идентификатор, устанавливается статус действия аккаунта {@code ACTIVE},
     * а также инициализируется коллекция для хранения привычек пользователя.
     * </p>
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @param email    электронная почта пользователя
     * @param userRole роль пользователя, определяется перечислением {@link UserRole}
     */
    public User(String username, String password, String email, UserRole userRole) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
        this.userStatusAccount = UserStatusAccount.ACTIVE;
        this.habits = new HashMap<>();
    }

    /**
     * Возвращает идентификатор пользователя (id).
     *
     * @return идентификатор пользователя
     */
    public long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор пользователя (id).
     *
     * @param id новый идентификатор пользователя
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return имя пользователя
     */
    public String getUserName() {
        return username;
    }

    /**
     * Устанавливает имя пользователя.
     *
     * @param username новое имя пользователя
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль пользователя
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает новый пароль пользователя.
     *
     * @param password новый пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Возвращает электронную почту пользователя.
     *
     * @return электронная почта пользователя
     */
    public String getEmail() {
        return email;
    }

    /**
     * Устанавливает новую электронную почту пользователя.
     *
     * @param email новая электронная почта пользователя
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Возвращает роль пользователя в системе.
     *
     * @return роль пользователя
     */
    public UserRole getUserRole() {
        return userRole;
    }

    /**
     * Устанавливает новую роль пользователя.
     *
     * @param userRole новая роль пользователя
     */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    /**
     * Возвращает статус учетной записи пользователя.
     *
     * @return статус учетной записи пользователя (active/blocked)
     */
    public UserStatusAccount getUserStatusAccount() {
        return userStatusAccount;
    }

    /**
     * Устанавливает новый статус учетной записи пользователя.
     *
     * @param userStatusAccount новый статус учетной записи (active/blocked)
     */
    public void setUserStatusAccount(UserStatusAccount userStatusAccount) {
        this.userStatusAccount = userStatusAccount;
    }

    /**
     * Возвращает коллекцию привычек пользователя.
     *
     * @return коллекция привычек пользователя
     */
    public HashMap<Long, Habit> getHabits() {
        return habits;
    }

    /**
     * Переопределяет метод {@code equals()} для сравнения пользователей по уникальному идентификатору.
     * <p>
     * Два пользователя считаются равными, если их уникальные идентификаторы {@code id} совпадают.
     * </p>
     *
     * @param o объект для сравнения с текущим пользователем
     * @return {@code true}, если объекты равны, иначе {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    /**
     * Возвращает хэш-код пользователя на основе уникального идентификатора.
     *
     * @return хэш-код пользователя
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Возвращает строковое представление объекта пользователя.
     * <p>
     * Включает информацию об имени, email, роли и статусе учетной записи пользователя.
     * </p>
     *
     * @return строковое представление пользователя
     */
    @Override
    public String toString() {
        return String.format("Пользователь (id - %s): имя - %s, email - %s, роль - %s, статус аккаунта - %s", id, username, email, userRole, userStatusAccount);
    }
}
