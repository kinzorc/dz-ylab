package ru.kinzorc.habittracker.navigation;

import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.core.userhandler.UserHandler;
import ru.kinzorc.habittracker.core.model.User;


public class AccountMenuNavigator {
    private AccountMenuNavigator() {}

    public static void accountMenu() {
        User currentUser = UserHandler.getCurrentUser();

        if (currentUser == null) {
            System.out.println("\nВы не авторизованы. Переход в главное меню.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("\nЛичный кабинет:\n1) Профиль 2) Мои привычки 3) Удалить аккаунт 4) Выход в главное меню");
            int option = InputUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> ProfileMenuNavigator.profileMenu(currentUser);
                case 2 -> HabitMenuNavigator.habitMenu();
                case 3 -> {
                    if (UserHandler.handleDeleteUserFromAccount(currentUser)) {
                        running = false; // возврат в главное меню
                    }
                }
                case 4 -> {
                    System.out.println("Выход в главное меню.");
                    running = false; // Завершение цикла
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }
}
