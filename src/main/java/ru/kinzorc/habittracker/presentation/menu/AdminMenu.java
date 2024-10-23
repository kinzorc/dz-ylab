package ru.kinzorc.habittracker.presentation.menu;

import ru.kinzorc.habittracker.application.dto.UserDTO;
import ru.kinzorc.habittracker.application.service.ApplicationService;
import ru.kinzorc.habittracker.core.enums.User.UserData;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;
import ru.kinzorc.habittracker.presentation.utils.PrintUtils;

import java.util.List;
import java.util.Scanner;


public class AdminMenu implements Menu {

    @Override
    public void showMenu(ApplicationService applicationService, MenuUtils menuUtils) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""

                    Администрирование:
                    1) Список пользователей 2) Список привычек пользователей 3) Заблокировать пользователя 4) Удалить пользователя 5) Выход в личный кабинет""");

            int option = menuUtils.promptMenuValidInput(scanner);

            switch (option) {
                case 1 -> {
                    List<UserDTO> users = applicationService.findAllUsers();

                    if (users.isEmpty()) {
                        System.out.println("Произошла ошибка, попробуйте еще раз.");
                        return;
                    }

                    PrintUtils.pintAllUsers(users);
                    menuUtils.promptInput(scanner, "Введите enter для выхода...");
                }
                case 2 -> {
                    System.out.println("Список привычек пользователей:\n");
                    PrintUtils.printListHabits(applicationService.getAllHabits());
                }
                case 3 -> {
                    String value = menuUtils.promptInput(scanner, "Введите id пользователя: ");

                    boolean isFind = applicationService.findUser(UserData.ID, value);

                    if (isFind) {
                        long userId = Long.parseLong(value);

                        if (applicationService.getCurrentUser().getId() == userId) {
                            System.out.println("Вы не можете заблокировать себя!");
                            return;
                        }

                        applicationService.blockUser(userId);
                    }
                }
                case 4 -> {
                    String value = menuUtils.promptInput(scanner, "Введите id пользователя: ");

                    boolean isFind = applicationService.findUser(UserData.ID, value);

                    if (isFind) {
                        long userId = Long.parseLong(value);

                        if (applicationService.getCurrentUser().getId() == userId) {
                            System.out.println("Вы не можете удалить свой аккаунт через меню администратора, воспользуетесь личным кабинетом");
                            return;
                        }

                        applicationService.deleteUser(userId);
                    }
                }
                case 5 -> {
                    System.out.println("Выход в главное меню.");
                    return;
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }
}
