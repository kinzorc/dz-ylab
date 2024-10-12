package ru.kinzorc.habittracker.navigation;

import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.core.userhandler.UserHandler;

public class AuthMenuNavigator {
    private AuthMenuNavigator() {}

    public static void authMenu() {
        System.out.println("\nМеню авторизации:\n1) Вход 2) Сбросить пароль 3) Выход в главное меню");
        int option = InputUtils.promptMenuValidInput();

        switch (option) {
            case 1 -> {
                if (UserHandler.handlerLoginUser()) {
                    AccountMenuNavigator.accountMenu();
                }
            }
            case 2 -> {
                if (UserHandler.resetPassword()) {
                    AccountMenuNavigator.accountMenu();
                } else {
                    MainMenuNavigator.mainMenu();
                }
            }
            case 3 -> System.out.println("Возврат в главное меню.");
            default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
        }
    }
}
