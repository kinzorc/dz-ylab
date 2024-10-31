package ru.kinzorc.habittracker.infrastructure.repository.jdbc;

import ru.kinzorc.habittracker.application.mappers.UserMapper;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.repository.UserRepository;
import ru.kinzorc.habittracker.infrastructure.repository.queries.UserSQLStatements;
import ru.kinzorc.habittracker.infrastructure.repository.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса {@link UserRepository} для управления пользователями в базе данных с использованием JDBC.
 * <p>
 * Этот класс предоставляет методы для выполнения операций CRUD (создание, чтение, обновление, удаление) и управления
 * сессиями пользователей. Взаимодействие с базой данных происходит через JDBC, с использованием заранее определённых SQL-запросов.
 * Также используется объект {@link UserMapper} для преобразования данных между сущностями и DTO.
 * </p>
 */
public class JdbcUserRepository implements UserRepository {

    private final DatabaseConnector databaseConnector;

    /**
     * Конструктор для создания экземпляра репозитория пользователей.
     *
     * @param databaseConnector объект {@link DatabaseConnector}, обеспечивающий соединение с базой данных
     */
    public JdbcUserRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(User user) throws SQLException {

        try {
            Optional<User> findUser = findByUserName(user.getUserName());
            if (findUser.isPresent())
                throw new SQLException("Пользователь уже существует!");
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserSQLStatements.CREATE_USER)) {

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUserRole().toString().toLowerCase());
            statement.setString(5, user.getUserStatusAccount().toString().toLowerCase());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(User user) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserSQLStatements.UPDATE_USER)) {

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUserRole().toString());
            statement.setString(5, user.getUserStatusAccount().toString());
            statement.setLong(6, user.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Ошибка при обновлении пользователя с ID " + user.getId() + ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserSQLStatements.DELETE_ALL_USERS)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при получении списка пользователей: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long userId) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserSQLStatements.DELETE_USER)) {

            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при удалении пользователя с ID " + userId + ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findAll() throws SQLException {

        List<User> users = new ArrayList<>();

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserSQLStatements.FIND_ALL_USERS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = UserMapper.INSTANCE.toEntity(UserMapper.INSTANCE.fromResultSetToDTO(resultSet));
                users.add(user);
            }

        } catch (SQLException e) {
            throw new SQLException("Ошибка при получении списка пользователей: " + e.getMessage());
        }

        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findById(Long userId) throws SQLException {

        User user = null;

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserSQLStatements.FIND_USER_BY_ID)) {

            statement.setObject(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = UserMapper.INSTANCE.toEntity(UserMapper.INSTANCE.fromResultSetToDTO(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при запросе к базе данных: " + e.getMessage());
        }

        return Optional.ofNullable(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByUserName(String userName) throws SQLException {

        User user = null;

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserSQLStatements.FIND_USER_BY_USERNAME)) {

            statement.setObject(1, userName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = UserMapper.INSTANCE.toEntity(UserMapper.INSTANCE.fromResultSetToDTO(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при запросе к базе данных: " + e.getMessage());
        }

        return Optional.ofNullable(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByEmail(String userEmail) throws SQLException {

        User user;

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UserSQLStatements.FIND_USER_BY_EMAIL)) {

            statement.setObject(1, userEmail);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                user = UserMapper.INSTANCE.toEntity(UserMapper.INSTANCE.fromResultSetToDTO(resultSet));
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при запросе к базе данных: " + e.getMessage());
        }

        return Optional.of(user);
    }
}
