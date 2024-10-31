package ru.kinzorc.habittracker.infrastructure.repository.jdbc;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kinzorc.habittracker.infrastructure.repository.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcSessionRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private JdbcSessionRepository sessionRepository;

    @BeforeAll
    public void setup() throws Exception {
        runLiquibaseMigrations();

        DatabaseConnector databaseConnector = new DatabaseConnector(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );
        sessionRepository = new JdbcSessionRepository(databaseConnector);
    }

    private void runLiquibaseMigrations() throws Exception {
        try (Connection connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        )) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
        }
    }

    @Test
    @DisplayName("Создание сессии пользователя")
    public void testCreateSession() throws SQLException {
        Long userId = 1L;
        String sessionId = "test-session-id";

        sessionRepository.createSession(userId, sessionId);

        assertThat(sessionRepository.sessionExists(sessionId)).isTrue();
    }

    @Test
    @DisplayName("Удаление сессии по sessionId")
    public void testDeleteSession() throws SQLException {
        Long userId = 1L;
        String sessionId = "session-to-delete";

        sessionRepository.createSession(userId, sessionId);
        sessionRepository.deleteSession(sessionId);

        assertThat(sessionRepository.sessionExists(sessionId)).isFalse();
    }

    @Test
    @DisplayName("Проверка существования сессии")
    public void testSessionExists() throws SQLException {
        Long userId = 2L;
        String sessionId = "existing-session-id";

        sessionRepository.createSession(userId, sessionId);

        assertThat(sessionRepository.sessionExists(sessionId)).isTrue();
        assertThat(sessionRepository.sessionExists("non-existing-id")).isFalse();
    }

    @Test
    @DisplayName("Поиск сессии по userId")
    public void testFindSessionByUserId() throws SQLException {
        Long userId = 1L;
        String sessionId = "user-session-id";

        sessionRepository.createSession(userId, sessionId);

        Optional<String> foundSession = sessionRepository.findSessionByUserId(userId);
        assertThat(foundSession).isPresent().contains(sessionId);
    }

    @Test
    @DisplayName("Поиск отсутствующей сессии по userId")
    public void testFindSessionByUserIdNotFound() throws SQLException {
        Optional<String> foundSession = sessionRepository.findSessionByUserId(-1L);
        assertThat(foundSession).isEmpty();
    }

    @Test
    @DisplayName("Обновление времени последней активности")
    public void testUpdateLastActivity() throws SQLException, InterruptedException {
        Long userId = 1L;
        String sessionId = "activity-session-id";

        sessionRepository.createSession(userId, sessionId);

        Optional<String> foundSession = sessionRepository.findSessionByUserId(userId);
        assertThat(foundSession).isPresent().contains(sessionId);

        Thread.sleep(1000);
        sessionRepository.updateLastActivity(sessionId);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        sessionRepository.deleteSession("test-session-id");
        sessionRepository.deleteSession("session-to-delete");
        sessionRepository.deleteSession("existing-session-id");
        sessionRepository.deleteSession("user-session-id");
        sessionRepository.deleteSession("activity-session-id");
    }

    @AfterAll
    public void stopContainer() {
        postgresContainer.stop();
    }
}
