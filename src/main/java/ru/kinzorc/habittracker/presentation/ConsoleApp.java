package ru.kinzorc.habittracker.presentation;

import liquibase.exception.LiquibaseException;
import ru.kinzorc.habittracker.infrastructure.factory.AppConsoleFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

/**
 * Класс представляет точку входа в консольное приложение Habit Tracker.
 * <p>
 * Приложение запускается с инициализацией базовых компонентов, таких как репозитории и сервисы,
 * и предоставляет пользовательский интерфейс через консоль.
 * </p>
 * <p>
 * При завершении работы приложения вызывается {@link Runtime#addShutdownHook(Thread)} для корректного
 * завершения сессий пользователя.
 * </p>
 */
public class ConsoleApp {

    /**
     * Метод main является точкой входа в консольное приложение Habit Tracker.
     * <p>
     * Приложение инициализирует соединение с базой данных, репозитории для работы с пользователями и привычками,
     * а также запускает главное меню для взаимодействия с пользователем через консоль.
     * В процессе завершения работы приложения, если активны сессии, они удаляются с использованием
     * механизма {@link ExecutorService} с таймаутом в 5 секунд.
     * </p>
     *
     * @param args аргументы командной строки, не используются
     */
    public static void main(String[] args) {

        AppConsoleFactory appConsoleFactory;

        try {
            appConsoleFactory = new AppConsoleFactory();
            appConsoleFactory.getLiquibaseMigration().startMigration();
        } catch (IOException | LiquibaseException | SQLException e) {
            System.err.println("При запуске приложения произошла ошибка! " + e.getMessage());
            System.exit(1);
        }


    }
}
