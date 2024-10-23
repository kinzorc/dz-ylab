package ru.kinzorc.habittracker.presentation.utils;

import ru.kinzorc.habittracker.application.dto.UserDTO;

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
}
