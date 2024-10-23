package ru.kinzorc.habittracker.core.repository;

import ru.kinzorc.habittracker.common.config.HandlerConstants;
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.Habit.HabitData;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;


public class HabitHandler {

    public HabitHandler() {
    }

    // Методы для управления привычками
    // Добавить привычку
    public static void addHabitForUser(User user) {
        String habitName = MenuUtils.promptInput("Введите название новой привычки: ");
        String habitDescription = MenuUtils.promptInput("Введите описание привычки: ");
        String habitFrequency = MenuUtils.promptHabitFrequencyValid("Частота выполнения (day/week): ", "Неверное значение, попробуйте еще раз.");
        LocalDate startDate = MenuUtils.promptDateValid("Дата начала выполнения (формат - dd.mm.yyyy): ", "Неверно указана дата");

        while (startDate != null && startDate.isBefore(LocalDate.now())) {
            System.out.println("Дата начала выполнения привычки не может быть меньше текущей даты, введите корректную дату!");
            startDate = MenuUtils.promptDateValid("Дата начала выполнения (формат - dd.mm.yyyy): ", "Неверно указана дата");
        }

        HandlerConstants.HABIT_SERVICE.addHabit(user, new Habit(habitName, habitDescription, HabitFrequency.valueOf(habitFrequency), startDate));

        System.out.println("Привычка \"" + habitName + "\" добавлена.");
    }

    public static void removeHabitForUser(User user) {
        String habitName = MenuUtils.promptInput("Введите название привычки для удаления: ");

        boolean isDeleted = HandlerConstants.HABIT_SERVICE.removeHabit(user, habitName);

        System.out.println(isDeleted ? "Привычка \"" + habitName + "\" удалена." : "Привычка не найдена.");
    }

    // Метод для изменения привычки
    public static void editHabitForUser(User user) {
        String habitName = MenuUtils.promptInput("Введите название привычки для изменения: ");
        Habit editHabit = HandlerConstants.HABIT_SERVICE.getHabit(user, habitName);

        if (editHabit == null) {
            System.out.println("Привычка не найдена!");
            return;
        }


        System.out.println("Изменить: 1) Имя 2) Описание 3) Частоту выполнения 4) Дата начала выполнения 5) Изменить статус 6) Очистить историю 7) Выход в меню привычек");
        int option = MenuUtils.promptMenuValidInput();
        String value;

        switch (option) {
            case 1 -> {
                value = MenuUtils.promptInput("Введите новое название привычки: ");
                HandlerConstants.HABIT_SERVICE.getHabits(HandlerConstants.CURRENT_USER).remove(editHabit.getName());
                editHabit.setName(value);
                HandlerConstants.HABIT_SERVICE.getHabits(HandlerConstants.CURRENT_USER).put(editHabit.getName(), editHabit);
            }
            case 2 -> {
                value = MenuUtils.promptInput("Введите новое описание привычки: ");
                HandlerConstants.HABIT_SERVICE.editDataHabit(HandlerConstants.CURRENT_USER, editHabit, HabitData.DESCRIPTION.toString(), value);
            }
            case 3 -> {
                System.out.println("При изменении частоты привычки, вся история о выполнении сбросится!");
                value = MenuUtils.promptHabitFrequencyValid("Введите новую частоту привычки (day/week/0 для выхода): ",
                        "Не удалось изменить частоту привычки!");

                if (value == null || value.equalsIgnoreCase("0")) {
                    return;
                }

                HandlerConstants.HABIT_SERVICE.editDataHabit(HandlerConstants.CURRENT_USER, editHabit, HabitData.FREQUENCY.toString(), value.toUpperCase());
            }
            case 4 -> {
                System.out.println("При изменении даты начала выполнения привычки, вся история о выполнении сбросится!");
                LocalDate date = MenuUtils.promptDateValid("Введите новую дату (формат - dd.MM.yyyy/0 для выхода): ",
                        "Неверный формат даты. Попробуйте снова.");

                if (date == null) {
                    return;
                }

                if (date.isBefore(editHabit.getStartDate())) {
                    System.out.println("Вы пытаетесь изменить дату начала выполнения привычки за прошедшее время");
                    return;
                }

                HandlerConstants.HABIT_SERVICE.editDataHabit(HandlerConstants.CURRENT_USER, editHabit, HabitData.START_DATE.toString(), date.toString());
            }
            case 5 -> {
                System.out.println("Изменение статуса привычки:");
                value = MenuUtils.promptHabitStatusValid("Введите ACTIVE/FINISHED: ", "Не правильно указан статус!");

                if (value == null || value.equalsIgnoreCase("0"))
                    return;

                if (value.equalsIgnoreCase("ACTIVE"))
                    HandlerConstants.HABIT_SERVICE.editDataHabit(HandlerConstants.CURRENT_USER, editHabit, HabitStatus.ACTIVE.toString(), value);
                else
                    HandlerConstants.HABIT_SERVICE.editDataHabit(HandlerConstants.CURRENT_USER, editHabit, HabitStatus.FINISHED.toString(), value);
            }
            case 6 -> {
                HandlerConstants.HABIT_SERVICE.clearCompletionHistory(editHabit);
                System.out.println("История выполнения пивычки очищена");
            }
            case 7 -> System.out.println("Выход в меню привычек");
        }
    }

    // Вывести список привычек
    public static void printUserListHabits(User user) {
        if (user == null) {
            System.out.println("Пользователь не найден");
            return;
        }

        HashMap<String, Habit> userHabits = HandlerConstants.HABIT_SERVICE.getHabits(user);

        if (userHabits.isEmpty()) {
            System.out.println("Пользователь " + user.getName() + " - привычки отсуствуют");
            return;
        }

        HandlerConstants.HABIT_SERVICE.printListHabits(user);
    }

    // Отметка о выполнении привычки
    public static void userMarkDoneHabit(User user) {
        String habitName = MenuUtils.promptInput("Введите название привычки: ");
        Habit markHabit = HandlerConstants.HABIT_SERVICE.getHabit(user, habitName);

        if (markHabit == null) {
            System.out.println("Привычка не найдена");
            return;
        }

        HandlerConstants.HABIT_SERVICE.markHabit(markHabit);

    }

    // Вывод информации о привычке
    public static Habit printHabitInfo(User user) {
        String habitName = MenuUtils.promptInput("Введите название привычки: ");
        Habit habit = HandlerConstants.HABIT_SERVICE.getHabit(user, habitName);

        if (habit == null) {
            System.out.println("Привычка не найдена!");
            return null;
        }

        System.out.println("\nПривычка:\n");
        System.out.printf("Название: %s%n Описание: %s%n Дата создания: %s%n Дата начала: %s%n Переодичность: %S%n",
                habit.getName(), habit.getDescription(), habit.getCreatedDate(), habit.getStartDate(), habit.getFrequency());

        return habit;
    }

    // Вывод истории выполнения
    public static void printHabitCompletionHistory(Habit habit) {
        System.out.println("История выполнения:\n");

        if (habit.getExecutions().isEmpty()) {
            System.out.println("История выполнения пуста");
            return;
        }

        HandlerConstants.HABIT_SERVICE.printCompletionHistory(habit);
    }

    // ВЫвод метриков привычки
    public static void printHabitMetrics(Habit habit) {

        if (habit.getExecutions().isEmpty()) {
            System.out.println("Отсутствует прогресс выполнения привычки \"" + habit.getName() + "\" - не было отметок о выполнении.");
            return;
        }

        System.out.println("\nПрогресс выполнения привычки: " + habit.getName());
        System.out.println("Статус: " + habit.getStatus());
        System.out.println("Периодичность: " + habit.getFrequency());
        System.out.println("Последняя дата выполнения: " + Collections.max(habit.getExecutions()));
        System.out.println("Текущий стрик выполнения: " + habit.calculateStreak());

        // Ожидание от пользователя нажатия Enter перед возвратом в меню
        System.out.println("\nНажмите Enter для возврата в меню...");
        MenuUtils.promptInput("");
    }

}
