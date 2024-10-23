package ru.kinzorc.habittracker.application.service;


import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;
import ru.kinzorc.habittracker.presentation.utils.PrintUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;

public class HabitService {
    public HabitService() {
    }

    public void addHabit(User user, Habit habit) {
        user.getHabits().put(habit.getName(), habit);
    }

    public void markHabit(Habit habit) {
        habit.markCompletion(LocalDate.now());
    }

    public boolean removeHabit(User user, String habitName) {
        if (user == null || habitName == null)
            return false;

        return user.getHabits().remove(habitName) != null;
    }

    public void editDataHabit(User user, Habit habit, String param, String value) {

        if (!user.getHabits().containsKey(habit.getName())) {
            return;
        }

        switch (param) {
            case "NAME" -> habit.setName(value);
            case "DESCRIPTION" -> habit.setDescription(value);
            case "FREQUENCY" -> {
                habit.setCompletionHistory(new HashSet<>());
                habit.setFrequency(HabitFrequency.valueOf(value));
            }
            case "DATA_START" -> {
                habit.setCompletionHistory(new HashSet<>());
                habit.setStartDate(LocalDate.parse(value));
            }
            case "ACTIVE" -> habit.setStatus(HabitStatus.ACTIVE);
            case "FINISHED" -> habit.setStatus(HabitStatus.FINISHED);
        }
    }

    public void clearCompletionHistory(Habit habit) {
        habit.getExecutions().clear();
    }

    // Метод для вывода списка привычек пользователя
    public void printListHabits(User user) {
        PrintUtils.printListHabits(user.getHabits());
    }


    // Метод для вывоад истории выполнения привычки
    public void printCompletionHistory(Habit habit) {
        habit.getExecutions().forEach(date -> System.out.println("Выполнена: " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }

    public Habit getHabit(User user, String habitName) {
        return user.getHabits().get(habitName);
    }

    public HashMap<String, Habit> getHabits(User user) {
        return user.getHabits();
    }
}
