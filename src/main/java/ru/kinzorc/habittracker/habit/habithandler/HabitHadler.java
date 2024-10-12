package ru.kinzorc.habittracker.habit.habithandler;

import ru.kinzorc.habittracker.common.util.FrequencyHabit;
import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.core.model.User;
import ru.kinzorc.habittracker.core.userhandler.UserHandler;
import ru.kinzorc.habittracker.habit.model.Habit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;


public class HabitHadler {
    static User currentUser = UserHandler.getCurrentUser();

    // Методы для управления привычками
    public static void addHabit() {
        String habitName = InputUtils.promptInput("Введите название новой привычки: ");
        String habitDescription = InputUtils.promptInput("Введите описание привычки: ");
        String habitFrequency = InputUtils.promptFrequencyValid("Частота выполнения (введите day или week): ", "Неверная частота, попробуйте еще раз.");

        currentUser.getHabits().put(new Habit(habitName, habitDescription, FrequencyHabit.valueOf(habitFrequency)), new ArrayList<>());

        System.out.println("Привычка \"" + habitName + "\" добавлена.");
    }

    public static void removeHabit() {
        String habitName = InputUtils.promptInput("Введите название привычки для удаления: ");
        Set<Habit> habitSet = currentUser.getHabits().keySet();
        boolean isDeleted = false;

        for (Habit habit : habitSet) {
            if (habit.getName().equalsIgnoreCase(habitName)) {
                currentUser.getHabits().remove(habit);
                isDeleted = true;
                break;
            }
        }

        System.out.println(isDeleted ? "Привычка \"" + habitName + "\" удалена." : "Не удалось найти или удалить привычку.");
    }

    // Вывести список привычек
    public static void listHabits() {
        if (currentUser.getHabits().isEmpty()) {
            System.out.println("Нет добавленных привычек.");
        } else {
            currentUser.getHabits().forEach(((habit, strings) -> System.out.println(habit.getName())));
        }
    }

    // Измнеить привычку
    public static void editHabit() {
        String habitName = InputUtils.promptInput("Введите название привычки: ");
        Set<Habit> habitSet = currentUser.getHabits().keySet();
        Habit habit = null;

        for (Habit findHabit : habitSet) {
            if (findHabit.getName().equalsIgnoreCase(habitName))
                habit = findHabit;
        }

        if (habit != null) {
            System.out.println("Изменить привычку: 1) Имя 2) Описание 3) Частоту выполнения");
            int option = InputUtils.promptMenuValidInput();

            if (option == 1) {
                String changeName = InputUtils.promptInput("Введите новое название привычки: ");
                habit.setName(changeName);
            } else if (option == 2) {
                String changeDescription = InputUtils.promptInput("Введите новое описание привычки: ");
                habit.setDescription(changeDescription);
            } else if (option == 3) {
                String changeFrequency = InputUtils.promptFrequencyValid("Введите новую частоту привычки: ", "Не удалось изменить частоту привычки!");
                habit.setFrequency(FrequencyHabit.valueOf(changeFrequency));
            }
        } else {
            System.out.println("Привычка не найдена!");
        }
    }

    // Отметка о выполнении привычки
    public static void markDoneHabit() {
        String habitName = InputUtils.promptInput("Введите название привычки: ");
        Set<Habit> habitSet = currentUser.getHabits().keySet();
        boolean isMark = false;

        for (Habit habit : habitSet) {
            if (habit.getName().equalsIgnoreCase(habitName)) {
                String addProgress = "Привычка: " + habit.getName() + ", Выполнена: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                currentUser.getHabits().get(habit).add(addProgress);
                isMark = true;
            }
        }

        System.out.println(isMark ? "Успешная отметка о выполнении привычки" : "Не удалось найти привычку!");
    }

    // Статискика выполениня привычки
    public static void statisticsHabit() {
        String habitName = InputUtils.promptInput("Введите название привычки: ");
        Set<Habit> habitSet = currentUser.getHabits().keySet();
        ArrayList<String> statistic = null;

        for (Habit habit : habitSet) {
            if (habit.getName().equalsIgnoreCase(habitName)) {
                statistic = currentUser.getHabits().get(habit);
                break;
            }
        }

        if (statistic != null) {
            statistic.forEach(System.out::println);
        } else {
            System.out.println("Статистика по привычке " + habitName + " - не найдена!");
        }
    }

}
