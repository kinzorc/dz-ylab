package ru.kinzorc.habittracker.presentation.utils;

import ru.kinzorc.habittracker.application.dto.UserDTO;
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.HabitStatistic;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PrintUtils {

    public static void pintAllUsers(List<UserDTO> users) {
        AtomicInteger increment = new AtomicInteger(1);
        System.out.println("\nСписок пользователей:\n");
        System.out.printf("%-3S %-6S %-20S %-30S %-12S %-12S%n", "№", "ID", "Имя", "Email", "Роль", "Статус аккаунта");
        System.out.println("------------------------------------------------------------------------");

        users.forEach(user -> System.out.printf("%-3s %-6S %-20s %-30s %-12S %-12S%n",
                increment.getAndIncrement(), user.getId(), user.getUserName(), user.getEmail(), user.getUserRole(), user.getUserStatusAccount()));
    }

    public static void printListHabits(List<Habit> habits) {
        AtomicInteger number = new AtomicInteger(1);
        System.out.println("\nСписок привычек:\n");
        System.out.printf("%-3s %-10s %-20s %-50s %-10S %-12s %-12s %-5S%n", "№", "ID", "Название", "Описание", "Статус", "Дата создания", "Дата начала", "Периодичность");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
        habits.forEach(habit -> System.out.printf("%-3s %-10s %-20s %-50s %-10S %-12s %-12s %-5S%n",
                number.getAndIncrement(), habit.getId(), habit.getName(), habit.getDescription(), habit.getStatus(), habit.getCreatedDate(), habit.getStartDate(), habit.getFrequency().name()));

    }

    public static void printInfoForHabit(Habit habit, HabitStatistic habitStatistic) {
        System.out.println("Название привычки: " + habit.getName());
        System.out.println("Дата создания: " + habit.getCreatedDate());
        System.out.println("Описание привычки: " + habit.getDescription());
        System.out.println("Период выполнения: " + habit.getExecutionPeriod());
        System.out.println("Дата начала выполнения: " + habit.getStartDate());
        System.out.println("Дата окончания выполнения: " + habit.getEndDate());

        System.out.println("Даты выполнения:\n");
        AtomicInteger number = new AtomicInteger(1);
        System.out.printf("%-3s %-20sn", "№", "Дата выполнения");
        System.out.println("-----------------------------------");
        habitStatistic.getExecutions().forEach(date -> System.out.printf("%-3s %-20sn",
                number.getAndIncrement(), date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:ss"))));

    }



}
