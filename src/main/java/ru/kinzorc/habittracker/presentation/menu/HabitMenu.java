package ru.kinzorc.habittracker.presentation.menu;

import ru.kinzorc.habittracker.application.service.ApplicationService;
import ru.kinzorc.habittracker.common.config.HandlerConstants;
import ru.kinzorc.habittracker.core.repository.HabitHandler;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;

public class HabitMenu implements Menu {

    @Override
    public void showMenu(ApplicationService applicationService) {

        while (true) {
            System.out.println("\nМои привычки:\n");
            HabitHandler.printUserListHabits(HandlerConstants.CURRENT_USER);
            System.out.println("""

                    Управление: 1) Статистика по привычке 2) Отметить выполнение 3) Добавить привычку
                                4) Изменить привычку 5) Завершить привычку 6) Удалить привычку 7) Выход в личный кабинет""");
            int option = MenuUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> MenuNavigator.HABIT_METRICS_MENU.showMenu();
                case 2 -> HabitHandler.userMarkDoneHabit(HandlerConstants.CURRENT_USER);
                case 3 -> HabitHandler.addHabitForUser(HandlerConstants.CURRENT_USER);
                case 4 -> HabitHandler.editHabitForUser(HandlerConstants.CURRENT_USER);
                case 5 -> HabitHandler.removeHabitForUser(HandlerConstants.CURRENT_USER);
                case 6 -> HabitHandler.removeHabitForUser(HandlerConstants.CURRENT_USER);
                case 7 -> {
                    System.out.println("Возврат в личный кабинет");
                    return;
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }
}
