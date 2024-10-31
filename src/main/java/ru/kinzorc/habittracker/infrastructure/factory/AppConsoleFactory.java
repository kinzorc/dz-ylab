package ru.kinzorc.habittracker.infrastructure.factory;

import ru.kinzorc.habittracker.application.services.HabitService;
import ru.kinzorc.habittracker.application.services.UserService;
import ru.kinzorc.habittracker.core.repository.HabitRepository;
import ru.kinzorc.habittracker.core.repository.SessionRepository;
import ru.kinzorc.habittracker.core.repository.UserRepository;
import ru.kinzorc.habittracker.infrastructure.repository.jdbc.JdbcHabitRepository;
import ru.kinzorc.habittracker.infrastructure.repository.jdbc.JdbcSessionRepository;
import ru.kinzorc.habittracker.infrastructure.repository.jdbc.JdbcUserRepository;
import ru.kinzorc.habittracker.infrastructure.repository.utils.DatabaseConnector;
import ru.kinzorc.habittracker.infrastructure.repository.utils.LiquibaseMigration;

import java.io.IOException;

/**
 * Класс {@code AppConsoleFactory} реализует фабрику для создания и конфигурирования основных компонентов приложения.
 * <p>
 * Этот класс предназначен для создания экземпляров сервисов и репозиториев с готовыми зависимостями, необходимых
 * для работы консольного приложения. Использование данного класса позволяет централизованно управлять
 * созданием и инициализацией компонентов, необходимых для работы с базой данных и выполнения миграций.
 * </p>
 *
 * <p>
 * При инициализации фабрика создает подключение к базе данных и выполняет миграции,
 * используя {@link DatabaseConnector} и {@link LiquibaseMigration}.
 * </p>
 *
 * <p>Основные компоненты, создаваемые и предоставляемые фабрикой:</p>
 * <ul>
 *     <li>{@link UserRepository} и {@link HabitRepository} - для доступа к данным пользователей и привычек;</li>
 *     <li>{@link UserService} и {@link HabitService} - для бизнес-логики пользователей и привычек;</li>
 *     <li>{@link DatabaseConnector} и {@link LiquibaseMigration} - для управления соединением и миграциями базы данных.</li>
 * </ul>
 *
 * <p>Пример использования:</p>
 * <pre>{@code
 * AppConsoleFactory factory = new AppConsoleFactory();
 * UserService userService = factory.getUserService();
 * HabitService habitService = factory.getHabitService();
 * }</pre>
 */
public class AppConsoleFactory {

    private final DatabaseConnector databaseConnector;
    private final LiquibaseMigration liquibaseMigration;
    private final UserRepository userRepository;
    private final HabitRepository habitRepository;
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final HabitService habitService;

    /**
     * Конструктор по умолчанию, инициализирующий все основные компоненты фабрики.
     * <p>
     * Создает экземпляры {@link DatabaseConnector} и {@link LiquibaseMigration} для подключения и миграций базы данных,
     * а также создает репозитории и сервисы для управления пользователями и привычками.
     * </p>
     *
     * @throws IOException если возникает ошибка при загрузке конфигурации базы данных
     */
    public AppConsoleFactory() throws IOException {
        databaseConnector = new DatabaseConnector();
        liquibaseMigration = new LiquibaseMigration();
        userRepository = new JdbcUserRepository(databaseConnector);
        habitRepository = new JdbcHabitRepository(databaseConnector);
        sessionRepository = new JdbcSessionRepository(databaseConnector);
        userService = new UserService(userRepository, sessionRepository);
        habitService = new HabitService(habitRepository);
    }

    /**
     * Возвращает экземпляр {@link DatabaseConnector} для подключения к базе данных.
     *
     * @return экземпляр {@code DatabaseConnector}
     */
    public DatabaseConnector getJdbcConnector() {
        return databaseConnector;
    }

    /**
     * Возвращает экземпляр {@link LiquibaseMigration} для выполнения миграций базы данных.
     *
     * @return экземпляр {@code LiquibaseMigration}
     */
    public LiquibaseMigration getLiquibaseMigration() {
        return liquibaseMigration;
    }

    /**
     * Возвращает экземпляр {@link UserService} для управления бизнес-логикой пользователей.
     *
     * @return экземпляр {@code UserService}
     */
    public UserService getApplicationService() {
        return userService;
    }

    /**
     * Возвращает экземпляр {@link UserRepository} для доступа к данным пользователей.
     *
     * @return экземпляр {@code UserRepository}
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Возвращает экземпляр {@link HabitRepository} для доступа к данным привычек.
     *
     * @return экземпляр {@code HabitRepository}
     */
    public HabitRepository getHabitRepository() {
        return habitRepository;
    }

    /**
     * Возвращает экземпляр {@link UserService} для бизнес-логики пользователей.
     *
     * @return экземпляр {@code UserService}
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Возвращает экземпляр {@link HabitService} для управления бизнес-логикой привычек.
     *
     * @return экземпляр {@code HabitService}
     */
    public HabitService getHabitService() {
        return habitService;
    }
}