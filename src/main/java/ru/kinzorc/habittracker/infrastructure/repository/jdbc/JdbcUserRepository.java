package ru.kinzorc.habittracker.infrastructure.repository.jdbc;

import ru.kinzorc.habittracker.application.dto.UserDTO;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.User.UserData;
import ru.kinzorc.habittracker.core.enums.User.UserRole;
import ru.kinzorc.habittracker.core.enums.User.UserStatusAccount;
import ru.kinzorc.habittracker.core.exceptions.UserAlreadyExistsException;
import ru.kinzorc.habittracker.core.exceptions.UserNotFoundException;
import ru.kinzorc.habittracker.core.repository.UserRepository;
import ru.kinzorc.habittracker.infrastructure.repository.utils.JdbcConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final JdbcConnector jdbcConnector;

    public JdbcUserRepository(JdbcConnector jdbcConnector) {
        this.jdbcConnector = jdbcConnector;
    }

    @Override
    public void addUser(User user) throws UserAlreadyExistsException, SQLException {
        String sql = "INSERT INTO app_schema.users (username, password, email, role_name, status_account) VALUES (?, ?, ?, ?, ?)";

        // Проверяем есть ли пользователь в базе данных
        if (isUserExist(UserData.EMAIL, user.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        // Создаем DTO для передачи в базу данных
        UserDTO newUser = new UserDTO(user);
        Object[] userArray = newUser.toSqlParams();

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i < userArray.length; i++) {
                statement.setObject(i, userArray[i]);
            }

            statement.executeUpdate();
        }
    }

    @Override
    public void deleteUser(long userId) throws UserNotFoundException, SQLException {
        String sql = "DELETE FROM app_schema.users WHERE id = ?";

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {


            statement.executeUpdate();
        }
    }

    @Override
    public void editUser(User user, UserData userData, String value) throws UserNotFoundException, SQLException {
        String sql = String.format("UPDATE app_schema.users SET %s = ? WHERE id = ?", userData.toString().toLowerCase());

        Optional<UserDTO> userDTO = findUser(UserData.ID, String.valueOf(user.getId()));

        if (userDTO.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден!");
        }

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            switch (userData) {
                case USERNAME -> {
                    userDTO.get().setUserName(value);
                    statement.setObject(1, userDTO.get().getUserName());
                    statement.setObject(2, userDTO.get().getId());
                }
                case PASSWORD -> {
                    userDTO.get().setPassword(value);
                    statement.setObject(1, userDTO.get().getPassword());
                    statement.setObject(2, userDTO.get().getId());
                }
                case EMAIL -> {
                    userDTO.get().setEmail(value);
                    statement.setObject(1, userDTO.get().getEmail());
                    statement.setObject(2, userDTO.get().getId());
                }
                default -> throw new IllegalStateException("Неверный параметр: " + userData);
            }

            statement.executeUpdate();
        }
    }

    @Override
    public void blockUser(long userId) throws UserNotFoundException, SQLException {
        String sql = "UPDATE app_schema.users SET status_account = ? WHERE id = ?";

        Optional<UserDTO> userDTO = findUser(UserData.ID, String.valueOf(userId));

        if (userDTO.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден!");
        }

        userDTO.get().setUserStatusAccount(UserStatusAccount.BLOCKED);

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userDTO.get().getUserStatusAccount().toString().toLowerCase());
            statement.setObject(2, userDTO.get().getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void updateUserPrivileges(long userId, UserRole userRole) throws UserNotFoundException, SQLException {
        String sql = "UPDATE app_schema.users SET role_name = ? WHERE id = ?";

        Optional<UserDTO> userDTO = findUser(UserData.ID, String.valueOf(userId));

        if (userDTO.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден!");
        }

        userDTO.get().setUserRole(userRole);

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, userDTO.get().getUserRole().toString().toLowerCase());
            statement.setObject(2, userDTO.get().getId());

            statement.executeUpdate();
        }
    }

    @Override
    public List<UserDTO> findAllUsers() throws SQLException {
        String sql = "SELECT * FROM app_schema.users";
        List<UserDTO> users = new ArrayList<>();

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                UserDTO userDTO = new UserDTO(rs);
                users.add(userDTO);
            }
        }

        return users;
    }

    @Override
    public Optional<UserDTO> findUser(UserData userData, String value) throws UserNotFoundException, SQLException {
        String sql = String.format("SELECT * FROM app_schema.users WHERE %s = ?", userData.toString().toLowerCase());
        UserDTO userDTO = null;

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            switch (userData) {
                case ID -> statement.setObject(1, Long.parseLong(value));
                case USERNAME -> statement.setObject(1, value);
                case EMAIL -> {
                    statement.setObject(1, value);
                }
                default -> throw new IllegalStateException("Неверный параметр: " + userData);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userDTO = new UserDTO(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Пользователь не найден!");
        }

        if (Optional.ofNullable(userDTO).isEmpty()) {
            throw new UserNotFoundException(String.format("Пользователь по реквизиту %s: %s - не найден.", userData, value));
        }

        return Optional.of(userDTO);
    }

    @Override
    public void addSession(long userId) throws SQLException {
        String sql = "INSERT INTO service_schema.users_sessions (username_id, login_time) VALUES (?, ?)";

        try (PreparedStatement statement = jdbcConnector.getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
        }
    }

    @Override
    public void removeSession(long userId) throws SQLException {
        String sql = "DELETE FROM service_schema.users_sessions WHERE user_id = ?";

        try (PreparedStatement statement = jdbcConnector.getConnection().prepareStatement(sql)) {
            statement.setLong(1, userId);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Сессия пользователя удалена.");
            } else {
                throw new SQLException("Сессия для данного пользователя не найдена.");
            }
        }
    }

    @Override
    public boolean isUserExist(UserData userData, String value) throws SQLException {
        String sql = String.format("SELECT email FROM app_schema.users WHERE %s = ?", userData.toString().toLowerCase());
        boolean result = false;

        try (Connection connection = jdbcConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            switch (userData) {
                case ID -> statement.setObject(1, Long.parseLong(value));
                case EMAIL -> statement.setObject(1, value);
                default -> throw new IllegalStateException("Неверный параметр: " + userData);
            }

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = true;
                }
            }
        }

        return result;
    }
}
