package ru.kinzorc.habittracker.navigation;

import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.habit.habithandler.HabitHadler;

public class HabitMenuNavigator {
    private HabitMenuNavigator() {}

    public static void habitMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nМеню привычек:\n1) Добавить привычку 2) Удалить привычку 3) Список привычек 4) Отметка о выполнении привычки 5) Выход");
            int option = InputUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> HabitHadler.addHabit();
                case 2 -> HabitHadler.removeHabit();
                case 3 -> ListHabitMenuNavigator.ProfileHabitsMenu();
                case 4 -> HabitHadler.markDoneHabit();
                case 5 -> {
                    System.out.println("Возврат в личный кабинет");
                    running = false;
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }
}
