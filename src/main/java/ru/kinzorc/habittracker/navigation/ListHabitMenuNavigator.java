package ru.kinzorc.habittracker.navigation;

import ru.kinzorc.habittracker.common.util.FrequencyHabit;
import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.habit.habithandler.HabitHadler;
import ru.kinzorc.habittracker.habit.model.Habit;

public class ProfileHabitMenuNavigator {
    private ProfileHabitMenuNavigator() {}

    public static void ProfileHabitsMenu() {
        boolean running = true;
        while (running) {
            HabitHadler.listHabits();
            System.out.println("\nСписок привычек:\n1) Выбрать привычку 2) Отметка о выполенении 3) Удалить привычку 4) Выход");
            int option = InputUtils.promptMenuValidInput();

            switch (option) {
                case 1 -> {
                    String habitName = InputUtils.promptInput("Введите название новой привычки: ");
                    String habitDescription = InputUtils.promptInput("Введите описание привычки: ");
                    String habitFrequency = InputUtils.promptFrequencyValid("Частота выполнения (введите day или week): ", "Неверная частота, попробуйте еще раз.");
                    HabitHadler.addHabit(new Habit(habitName, habitDescription, FrequencyHabit.valueOf(habitFrequency)));
                }
                case 2 -> {
                    String habitName = InputUtils.promptInput("Введите название привычки для удаления: ");
                    HabitHadler.removeHabit(habitName);
                }
                case 3 -> ;
                case 4 -> {
                    System.out.println("Возврат в личный кабинет");
                    running = false; // Завершение цикла
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }
}
