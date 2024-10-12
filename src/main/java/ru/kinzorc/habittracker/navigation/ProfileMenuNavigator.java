package ru.kinzorc.habittracker.navigation;

import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.core.userhandler.UserHandler;
import ru.kinzorc.habittracker.core.model.User;

public class ProfileMenuNavigator {
    private ProfileMenuNavigator() {}

    public static void profileMenu(User currentUser) {
        boolean running = true;
        while (running) {
            System.out.println("Профиль пользователя: " + currentUser.getName()
                    + "\n Email: " + currentUser.getEmail()
                    + "\n Пароль: " + currentUser.getPassword());
            System.out.println();
            System.out.println("Меню профиля: 1) Изменить имя 2) Изменить email 3) Изменить пароль 4) Выход");

            int option = InputUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> UserHandler.setNameForUser();
                case 2 -> UserHandler.setEmailForUser();
                case 3 -> UserHandler.setPasswordForUser();
                case 4 -> {
                    System.out.println("Возврат в личный кабинет");
                    running = false; // Завершение цикла
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }
}
