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
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.infrastructure.repository.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Контейнерные тесты для {@link JdbcUserRepository} с использованием AssertJ и тестами на обработку исключений.
 */
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcUserRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");


    private JdbcUserRepository userRepository;

    @BeforeAll
    public void setup() throws Exception {
        runLiquibaseMigrations();

        DatabaseConnector databaseConnector = new DatabaseConnector(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );
        userRepository = new JdbcUserRepository(databaseConnector);
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
    @DisplayName("Сохранение пользователя в базе данных")
    public void testSaveUser() throws SQLException {
        User user = new User("testuser", "password", "testuser@example.com");
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByUserName("testuser");
        assertThat(fetchedUser).isPresent();
        assertThat(fetchedUser.get().getEmail()).isEqualTo("testuser@example.com");
    }

    @Test
    @DisplayName("Поиск пользователя по ID")
    public void testFindById() throws SQLException {
        User user = new User("findbyiduser", "password", "findbyid@example.com");
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findById(user.getId());
        assertThat(fetchedUser).isPresent();
        assertThat(fetchedUser.get().getUserName()).isEqualTo("findbyiduser");
    }

    @Test
    @DisplayName("Поиск пользователя по email")
    public void testFindByEmail() throws SQLException {
        User user = new User("findbyemailuser", "password", "findbyemail@example.com");
        userRepository.save(user);

        Optional<User> fetchedUser = userRepository.findByEmail("findbyemail@example.com");
        assertThat(fetchedUser).isPresent();
        assertThat(fetchedUser.get().getUserName()).isEqualTo("findbyemailuser");
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    public void testFindAll() throws SQLException {
        userRepository.deleteAll();

        User user1 = new User("user1", "password1", "user1@example.com");
        User user2 = new User("user2", "password2", "user2@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    public void testUpdateUser() throws SQLException {
        User user = new User("updateuser", "password", "updateuser@example.com");
        userRepository.save(user);

        Optional<User> savedUser = userRepository.findByUserName("updateuser");
        assertThat(savedUser).isPresent().hasValueSatisfying(u -> assertThat(u.getEmail()).isEqualTo("updateuser@example.com"));

        user.setId(savedUser.get().getId());

        user.setUserName("updateduser");
        user.setEmail("updateduser@example.com");
        userRepository.update(user);

        Optional<User> updatedUser = userRepository.findById(user.getId());
        assertThat(updatedUser).isPresent().hasValueSatisfying(u -> {
            assertThat(u.getUserName()).isEqualTo("updateduser");
            assertThat(u.getEmail()).isEqualTo("updateduser@example.com");
        });
    }

    @Test
    @DisplayName("Удаление пользователя по ID")
    public void testDeleteUserById() throws SQLException {
        User user = new User("deleteuser", "password", "deleteuser@example.com");
        userRepository.save(user);

        userRepository.deleteById(user.getId());
        Optional<User> fetchedUser = userRepository.findById(user.getId());
        assertThat(fetchedUser).isNotPresent();
    }

    @Test
    @DisplayName("Возврат пустого Optional при отсутствии пользователя с указанным ID")
    public void testFindByIdReturnsEmptyOptionalWhenUserNotFound() throws SQLException {
        Optional<User> user = userRepository.findById(-1L);
        assertThat(user).isEmpty();
    }

    @Test
    @DisplayName("Исключение при отсутствии пользователя с указанным именем")
    public void testFindByUserNameThrowsExceptionWhenUserNotFound() {
        assertThatThrownBy(() -> userRepository.findByUserName("nonexistentuser"))
                .isInstanceOf(SQLException.class)
                .hasMessageContaining("Ошибка при запросе к базе данных");
    }

    @Test
    @DisplayName("Исключение при отсутствии пользователя с указанным email")
    public void testFindByEmailThrowsExceptionWhenUserNotFound() {
        assertThatThrownBy(() -> userRepository.findByEmail("nonexistent@example.com"))
                .isInstanceOf(SQLException.class)
                .hasMessageContaining("Ошибка при запросе к базе данных");
    }

    @Test
    @DisplayName("Исключение при попытке сохранить дубликат пользователя")
    public void testSaveUserThrowsExceptionForDuplicateUser() throws SQLException {
        User user = new User("duplicateuser", "password", "duplicate@example.com");
        userRepository.save(user);

        assertThatThrownBy(() -> userRepository.save(user))
                .isInstanceOf(SQLException.class)
                .hasMessageContaining("Пользователь уже существует!");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        userRepository.deleteAll();
    }
}