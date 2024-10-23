package ru.kinzorc.habittracker.presentation.menu;

import ru.kinzorc.habittracker.application.service.ApplicationService;
import ru.kinzorc.habittracker.core.enums.User.UserData;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;

import java.util.Scanner;

public class AuthMenu implements Menu {

    @Override
    public void showMenu(ApplicationService applicationService, MenuUtils menuUtils) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nМеню авторизации:\n1) Вход 2) Сбросить пароль 3) Выход в главное меню");
        int option = menuUtils.promptMenuValidInput(scanner);

        switch (option) {
            case 1 -> {
                String email = menuUtils.promptInput(scanner, "  Email: ");
                String password = menuUtils.promptInput(scanner, "  Пароль: ");

                if (applicationService.loginUser(email, password)) {
                    MenuNavigator.ACCOUNT_MENU.showMenu(applicationService, menuUtils);
                }
            }
            case 2 -> {
                String email = menuUtils.promptInput(scanner, "Для сброса пароля введите email пользователя: ");
                if (applicationService.findUser(UserData.EMAIL, email)) {
                    MenuNavigator.ACCOUNT_MENU.showMenu(applicationService, menuUtils);
                } else {
                    MenuNavigator.MAIN_MENU.showMenu(applicationService, menuUtils);
                }
            }
            case 3 -> System.out.println("Возврат в главное меню.");
            default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");

        }
    }
}
