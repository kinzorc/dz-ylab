package ru.kinzorc.habittracker.infrastructure.repository.jdbc;

import ru.kinzorc.habittracker.core.repository.SessionRepository;
import ru.kinzorc.habittracker.infrastructure.repository.queries.SessionsSQLStatements;
import ru.kinzorc.habittracker.infrastructure.repository.utils.DatabaseConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class JdbcSessionRepository implements SessionRepository {

    private final DatabaseConnector databaseConnector;

    public JdbcSessionRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void createSession(Long userId, String sessionId) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SessionsSQLStatements.CREATE_SESSION)) {

            statement.setString(1, sessionId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при создании сессии для пользователя с ID " + userId + ": " + e.getMessage());
        }
    }

    @Override
    public void deleteSession(String sessionId) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SessionsSQLStatements.DELETE_SESSION)) {

            statement.setString(1, sessionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при удалении сессии с ID " + sessionId, e);
        }
    }

    @Override
    public boolean sessionExists(String sessionId) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SessionsSQLStatements.SESSION_EXISTS)) {

            statement.setString(1, sessionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при проверке существования сессии с ID " + sessionId, e);
        }
    }

    @Override
    public Optional<String> findSessionByUserId(Long userId) throws SQLException {

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SessionsSQLStatements.FIND_SESSION_BY_USER_ID)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSet.getString("session_id"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка при поиске сессии для пользователя с ID " + userId, e);
        }

        return Optional.empty();
    }

    @Override
    public void updateLastActivity(String sessionId) throws SQLException {
        String sql = "UPDATE app_schema.user_sessions SET last_activity = ? WHERE session_id = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(2, sessionId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Ошибка при обновлении времени активности сессии с ID " + sessionId, e);
        }
    }
}
