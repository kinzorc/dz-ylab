package ru.kinzorc.habittracker.application.dto;

/**
 * Класс {@code UserDTO} представляет собой объект для передачи данных (DTO) пользователя
 * между слоями приложения, обеспечивая транспортировку данных без логики бизнес-уровня.
 * <p>
 * Используется для обмена данными о пользователе, такими как идентификатор, имя, пароль,
 * email, роль и статус учетной записи.
 * </p>
 */
public class UserDTO {

    /**
     * Уникальный идентификатор пользователя
     */
    public Long id;

    /**
     * Имя пользователя
     */
    public String username;

    /**
     * Пароль пользователя
     */
    public String password;

    /** Электронная почта пользователя */
    public String email;

    /** Роль пользователя в системе */
    public String userRole;

    /**
     * Статус учетной записи пользователя
     */
    public String userStatusAccount;
}
