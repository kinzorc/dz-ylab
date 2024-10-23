package ru.kinzorc.habittracker.core.utils;

import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionStatus;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HabitExecutionManager {
    // Метод для отметки успешного выполнения привычки
    public HabitExecutionStatus markHabitCompletion(Habit habit, LocalDate date) {
        LocalDate startDate = habit.getStartDate();
        HabitFrequency frequency = habit.getFrequency();
        HabitStatus status = habit.getStatus();
        int currentStreak = habit.getStreaks();
        List<LocalDate> completionHistory = habit.getExecutions();

        // Проверяем статус привычки, если привычка имеет статус FINISHED возвращаем false, такой привычке нельзя отмечать выполнение
        if (status == HabitStatus.FINISHED) {
            return HabitExecutionStatus.HABIT_FINISHED;
        }

        // Проверяем, что старт (дата) начала выполнения привычки меньше, чем дата создания привычки,
        // т.е. пользователь хочет начать выполнять привычку задним числом
        if (date.isBefore(startDate)) {
            return HabitExecutionStatus.HABIT_START_DATE_NOT_REACHED;
        }

        // Проверка на выполнение привычки в зависимости от частоты
        if (frequency == HabitFrequency.DAILY) {
            // Проверка выполнена была ли выполнена привычка сегодня
            if (completionHistory.contains(date)) {
                return HabitExecutionStatus.HABIT_ALREADY_COMPLETED_TODAY;
            }
        } else if (frequency == HabitFrequency.WEEKLY) {
            if (isCompletedThisWeek(date, completionHistory)) {
                return HabitExecutionStatus.HABIT_ALREADY_COMPLETED_THIS_WEEK;
            }
        }

        // Добавляем выполнение привычки в историю
        habit.getExecutions().add(date);

        // Считаем новый стрик
        int newStreak = calculateStreak(habit);

        // Если стрик преравался обновляем историю предыдущих успешно выполененых стриков
        if (newStreak < currentStreak) {
            updateSuccessStreakHistory(habit);
        }

        // Устанавливаем новый стрик
        habit.resetStreaks(newStreak);

        // Обновляем процент успещного выполнения
        updateSuccessCompletionPercentage(habit);

        return HabitExecutionStatus.HABIT_SUCCESS;
    }

    // Метод для проверки, выполнена ли привычка на этой неделе
    private boolean isCompletedThisWeek(LocalDate date, ArrayList<LocalDate> completionHistory) {
        if (date == null)
            throw new IllegalArgumentException("Объект date не может быть null");

        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        // Определяем неделю и год для переданной даты
        int targetWeek = date.get(weekFields.weekOfWeekBasedYear());
        int targetYear = date.get(weekFields.weekBasedYear());

        // Проходим по всем датам в истории выполнения привычек
        for (LocalDate completionDate : completionHistory) {
            int completionWeek = completionDate.get(weekFields.weekOfWeekBasedYear());
            int completionYear = completionDate.get(weekFields.weekBasedYear());

            if (completionWeek == targetWeek && completionYear == targetYear) {
                return true;
            }
        }

        return false;
    }
}
