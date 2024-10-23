package ru.kinzorc.habittracker.presentation.menu;

import ru.kinzorc.habittracker.application.service.ApplicationService;
import ru.kinzorc.habittracker.common.config.HandlerConstants;
import ru.kinzorc.habittracker.core.repository.UserHandler;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;

import java.util.Scanner;

public class UserProfileMenu implements Menu {

    @Override
    public void showMenu(ApplicationService applicationService, MenuUtils menuUtils) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nПрофиль пользователя:\n "
                    + "\n Имя: " + applicationService.getCurrentUser().getUserName()
                    + "\n Email: " + applicationService.getCurrentUser().getEmail()
                    + "\n Роль в системе: " + applicationService.getCurrentUser().getUserRole() + "\n");

            System.out.println("Меню профиля: 1) Изменить имя 2) Изменить email 3) Изменить пароль 4) Выход");

            int option = menuUtils.promptMenuValidInput(scanner);

            switch (option) {
                case 1 -> UserHandler.setNameUser(HandlerConstants.CURRENT_USER);
                case 2 -> UserHandler.setEmailUser(HandlerConstants.CURRENT_USER);
                case 3 -> UserHandler.setPasswordUser(HandlerConstants.CURRENT_USER);
                case 4 -> {
                    System.out.println("Возврат в личный кабинет");
                    return;
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }
}
