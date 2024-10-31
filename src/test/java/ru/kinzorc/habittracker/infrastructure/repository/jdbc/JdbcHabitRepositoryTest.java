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
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;
import ru.kinzorc.habittracker.infrastructure.repository.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcHabitRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private JdbcHabitRepository habitRepository;

    @BeforeAll
    public void setup() throws Exception {
        runLiquibaseMigrations();

        DatabaseConnector databaseConnector = new DatabaseConnector(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );
        habitRepository = new JdbcHabitRepository(databaseConnector);

        User testUser = new User("test", "test", "test@test.com");
        testUser.setId(1L);
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
    @DisplayName("Сохранение привычки")
    public void testSaveHabit() throws SQLException {
        Habit habit = new Habit(1, "test habit", "test description", HabitFrequency.DAILY, LocalDate.now(), HabitExecutionPeriod.MONTH);
        habit.setEndDate(LocalDate.now().plusDays(30));
        habit.setStatus(HabitStatus.ACTIVE);

        habitRepository.save(habit);

        Optional<Habit> savedHabit = habitRepository.findByName("test habit");
        assertThat(savedHabit).isPresent();
        assertThat(savedHabit.get().getDescription()).isEqualTo("test description");
    }

    @Test
    @DisplayName("Найти привычку по ID")
    public void testFindById() throws SQLException {
        Habit habit = new Habit(1, "find habit", "habit for findById test", HabitFrequency.DAILY, LocalDate.now(), HabitExecutionPeriod.MONTH);
        habit.setEndDate(LocalDate.now().plusDays(30));
        habit.setStatus(HabitStatus.ACTIVE);

        habitRepository.save(habit);

        Optional<Habit> fetchedHabit = habitRepository.findByName("find habit");
        assertThat(fetchedHabit).isPresent();
        assertThat(fetchedHabit.get().getDescription()).isEqualTo("habit for findById test");
    }

    @Test
    @DisplayName("Обновление привычки")
    public void testUpdateHabit() throws SQLException {
        Habit habit = new Habit(1, "habit to update", "initial description", HabitFrequency.DAILY, LocalDate.now(), HabitExecutionPeriod.MONTH);
        habit.setEndDate(LocalDate.now().plusDays(30));
        habit.setStatus(HabitStatus.ACTIVE);

        habitRepository.save(habit);

        habit.setDescription("updated description");
        habitRepository.update(habit);

        Optional<Habit> updatedHabit = habitRepository.findByName(habit.getName());
        assertThat(updatedHabit).isPresent();
        assertThat(updatedHabit.get().getDescription()).isEqualTo("updated description");
    }

    @Test
    @DisplayName("Удаление привычки по ID")
    public void testDeleteHabitById() throws SQLException {
        Habit habit = new Habit(1, "habit to delete", "description for delete", HabitFrequency.DAILY, LocalDate.now(), HabitExecutionPeriod.MONTH);
        habit.setEndDate(LocalDate.now().plusDays(30));
        habit.setStatus(HabitStatus.ACTIVE);

        habitRepository.save(habit);
        habitRepository.deleteById(habit.getId());

        Optional<Habit> deletedHabit = habitRepository.findById(habit.getId());
        assertThat(deletedHabit).isEmpty();
    }

    @Test
    @DisplayName("Возврат пустого Optional при отсутствии привычки с указанным ID")
    public void testFindByIdThrowsExceptionWhenHabitNotFound() throws SQLException {
        Optional<Habit> habit = habitRepository.findById(-1L);
        assertThat(habit).isEmpty();
    }

    @Test
    @DisplayName("Получение списка выполнений привычки")
    public void testGetExecutions() throws SQLException {
        Habit habit = new Habit(1, "habit with executions", "test getExecutions method", HabitFrequency.DAILY, LocalDate.now(), HabitExecutionPeriod.MONTH);
        habit.setId(1);
        habit.setEndDate(LocalDate.now().plusDays(10));
        habit.setStatus(HabitStatus.ACTIVE);

        habitRepository.save(habit);
        habitRepository.resetExecutions(habit.getId());
        habitRepository.markExecution(habit, LocalDate.now());

        List<LocalDate> executions = habitRepository.getExecutions(habit.getId());
        assertThat(executions).hasSize(1).contains(LocalDate.now());
    }

    @Test
    @DisplayName("Сброс выполнений привычки")
    public void testResetExecutions() throws SQLException {
        Habit habit = new Habit(1, "habit with executions to reset", "test resetExecutions method", HabitFrequency.DAILY, LocalDate.now(), HabitExecutionPeriod.MONTH);
        habit.setEndDate(LocalDate.now().plusDays(10));
        habit.setStatus(HabitStatus.ACTIVE);

        habitRepository.save(habit);
        habitRepository.resetExecutions(habit.getId());

        List<LocalDate> executions = habitRepository.getExecutions(habit.getId());
        assertThat(executions).isEmpty();
    }

    @Test
    @DisplayName("Подсчет процента выполнения привычки за период")
    public void testCalculateExecutionPercentageByPeriod() throws SQLException {
        habitRepository.deleteAll();

        Habit habit = new Habit(1, "habit with percentage", "description habit with percentage", HabitFrequency.DAILY, LocalDate.now(), HabitExecutionPeriod.MONTH);
        habit.setEndDate(LocalDate.now().plusDays(4));
        habit.setStatus(HabitStatus.ACTIVE);

        habitRepository.save(habit);

        Optional<Habit> findHabit = habitRepository.findByName(habit.getName());
        findHabit.ifPresent(value -> habit.setId(value.getId()));

        habitRepository.markExecution(habit, LocalDate.now());
        habitRepository.markExecution(habit, LocalDate.now().plusDays(1));

        int percentage = habitRepository.calculateExecutionPercentageByPeriod(habit, habit.getStartDate(), habit.getEndDate());
        assertThat(percentage).isEqualTo(40);

        habitRepository.markExecution(habit, LocalDate.now().plusDays(2));
        habitRepository.markExecution(habit, LocalDate.now().plusDays(3));
        habitRepository.markExecution(habit, LocalDate.now().plusDays(4));

        percentage = habitRepository.calculateExecutionPercentageByPeriod(habit, habit.getStartDate(), habit.getEndDate());
        assertThat(percentage).isEqualTo(100);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        habitRepository.deleteAll();
        habitRepository.resetAllExecutions();   // Очистка выполнений
    }

    @AfterAll
    public void stopContainer() {
        postgresContainer.stop();
    }
}
