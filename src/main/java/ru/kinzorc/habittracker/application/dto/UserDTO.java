package ru.kinzorc.habittracker.application.dto;

import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.User.UserRole;
import ru.kinzorc.habittracker.core.enums.User.UserStatusAccount;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private UserRole userRole;
    private UserStatusAccount userStatusAccount;


    // Конструктор для создания DTO на основе данных введенных пользователем
    public UserDTO(String username, String password, String email) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = UserRole.USER;
        this.userStatusAccount = UserStatusAccount.ACTIVE;
    }

    // Конструктор для создания DTO на основе сущности User
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUserName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.userRole = user.getUserRole();
        this.userStatusAccount = user.getUserStatusAccount();
    }

    // Конструктор для создания DTO на основе данных из ResultSet
    public UserDTO(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.username = resultSet.getString("username");
        this.email = resultSet.getString("email");
        this.password = resultSet.getString("password");
        this.userRole = UserRole.valueOf(resultSet.getString("role_name").toUpperCase());
        this.userStatusAccount = UserStatusAccount.valueOf(resultSet.getString("status_account").toUpperCase());
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public UserStatusAccount getUserStatusAccount() {
        return userStatusAccount;
    }

    public void setUserStatusAccount(UserStatusAccount userStatusAccount) {
        this.userStatusAccount = userStatusAccount;
    }

    // Метод для преобразования полей DTO в массив для SQL запроса
    public Object[] toSqlParams() {
        return new Object[]{username, password, email, userRole.toString().toLowerCase(), userStatusAccount.toString().toLowerCase()};
    }

    public User toUser() {
        User user = new User(username, password, email, userRole);
        user.setId(id);

        return user;
    }
}