package ru.kinzorc.habittracker.navigation;

import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.habit.habithandler.HabitHadler;


public class ListHabitMenuNavigator {
    private ListHabitMenuNavigator() {}

    public static void ProfileHabitsMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nСписок привычек:");
            HabitHadler.listHabits();
            System.out.println("\n1) Статистика выполнения 2) Отметка о выполенении 3) Изменить привычку 4) Удалить привычку 5) Выйти в меню привычек");
            int option = InputUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> HabitHadler.statisticsHabit();
                case 2 -> HabitHadler.markDoneHabit();
                case 3 -> HabitHadler.editHabit();
                case 4 -> HabitHadler.removeHabit();
                case 5 -> {
                    System.out.println("Возврат в меню привычек");
                    running = false; // Завершение цикла
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }
}
