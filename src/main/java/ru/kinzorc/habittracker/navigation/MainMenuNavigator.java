package ru.kinzorc.habittracker.navigation;

import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.core.userhandler.UserHandler;
import ru.kinzorc.habittracker.core.service.ConfigAppManager;


public class MainMenuNavigator {

    private MainMenuNavigator() {
    }

    // Главное меню
    public static void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nГлавное меню:\n1) Регистрация пользователя 2) Авторизация 3) Настройки приложения 4) Выход");
            int option = InputUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> UserHandler.handleRegisterNewUser();
                case 2 -> AuthMenuNavigator.authMenu();
                case 3 -> ConfigAppManager.setParamApp();
                case 4 -> {
                    System.out.println("Выход из приложения");
                    running = false; // Завершение цикла
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }





}