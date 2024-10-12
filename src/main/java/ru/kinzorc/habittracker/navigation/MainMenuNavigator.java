package ru.kinzorc.habittracker.navigation;

import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.core.handler.UserHandler;
import ru.kinzorc.habittracker.core.model.User;
import ru.kinzorc.habittracker.core.service.ConfigAppManager;


public class MenuNavigator {

    private MenuNavigator() {
    }

    // Главное меню
    public static void mainMenu() {
        while (true) {
            System.out.println("\nГлавное меню:\n1) Регистрация пользователя 2) Личный кабинет 3) Настройки приложения 4) Выход");
            int option = InputUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> UserHandler.handleRegisterNewUser();
                case 2 -> {
                    System.out.println("\nМеню авторизации:\n1) Вход 2) Сбросить пароль 3) Выход в главное меню");
                    int subOption = InputUtils.promptMenuValidInput();

                    if (subOption == 1) {
                        if (UserHandler.handlerLoginUser())
                            accountMenu();
                    } else if (subOption == 2) {
                        if (UserHandler.resetPassword())
                            accountMenu();
                        else
                            mainMenu();
                    } else if (subOption == 3) {
                        mainMenu();
                    }
                }
                case 3 -> ConfigAppManager.setParamApp();
                case 4 -> {
                    System.out.println("Выход из приложения");
                    System.exit(0); // Завершение программы
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }

    // Меню личного кабинета пользователя
    public static void accountMenu() {
        User currentUser = UserHandler.getCurrentUser();

        // Проверка, что текущий пользователь авторизован
        if (currentUser == null) {
            System.out.println("\nВы не авторизованы. Переход в главное меню.");
            return;
        }

        while (true) {
            System.out.println("\nЛичный кабинет:\n1) Профиль 2) Мои привычки 3) Удалить аккаунт 4) Выход в главное меню");
            int option = InputUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> profileMenu(currentUser);
                case 2 -> System.out.println("Список привычек пока не реализован.");
                case 3 -> {
                    if (UserHandler.handleDeleteUserFromAccount(currentUser)) {
                        mainMenu();
                    }
                }
                case 4 -> {
                    System.out.println("Выход в главное меню.");
                    System.out.println();
                    return; // Переход в главное меню
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }

    // Меню профиля пользователя
    public static void profileMenu(User currentUser) {

        while (true) {
            System.out.println("Профиль пользователя: " + currentUser.getName()
                    + "\n Email: " + currentUser.getEmail()
                    + "\n Пароль: " + currentUser.getPassword());
            System.out.println("\nМеню профиля: 1) Изменить имя 2) Изменить email 3) Изменить пароль 4) Выход");

            int option = InputUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> UserHandler.setNameForUser();
                case 2 -> UserHandler.setEmailForUser();
                case 3 -> UserHandler.setPasswordForUser();
                case 4 -> {
                    System.out.println("Возврат в личный кабинет");
                    running = false;
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }

    // Вспомогательный метод для безопасного ввода числа

}