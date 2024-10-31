package ru.kinzorc.habittracker.infrastructure.repository.queries;

public class SessionsSQLStatements {

    public static final String CREATE_SESSION = "INSERT INTO app_schema.user_sessions (session_id, user_id) VALUES (?, ?)";
    public static final String DELETE_SESSION = "DELETE FROM app_schema.user_sessions WHERE session_id = ?";
    public static final String SESSION_EXISTS = "SELECT 1 FROM app_schema.user_sessions WHERE session_id = ?";
    public static final String FIND_SESSION_BY_USER_ID = "SELECT session_id FROM app_schema.user_sessions WHERE user_id = ?";
}
