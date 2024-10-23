package ru.kinzorc.habittracker.core.utils;

import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class HabitStreakCalculator {


    // Подсчет и установка текущего стирка в зависимости от периодичности
    public void calcStreak(Habit habit) {
        if (habit == null)
            throw new IllegalArgumentException("Объект привычки (habit) не может быть null.");

        if (completionDate == null)
            throw new IllegalArgumentException("Объект даты не может быть null.");

        List<LocalDate> completionHistory = habit.getExecutions();

        if (completionHistory.isEmpty()) {
            habit.resetStreaks(1);
            return;
        }

        HabitFrequency frequency = habit.getFrequency();
        LocalDate dateToCalculate = completionDate.minusDays(1);
        int streak = calculateStreak(habit, completionHistory, frequency, dateToCalculate);

        habit.resetStreaks(streak);
    }

    // Подсчет стрика в зависимости от частоты выполнения (frequency)
    private int calculateStreak(Habit habit, List<LocalDate> completionHistory, HabitFrequency frequency, LocalDate dateToCalculate) {
        if (completionHistory == null)
            throw new IllegalArgumentException("Объект списка completionHistory не может быть null.");

        if (frequency == null)
            throw new IllegalArgumentException("Объект частоты выполнения привычки (frequency) не может быть null");

        if (dateToCalculate == null)
            throw new IllegalArgumentException("Объект даты не может быть null.");

        // Итерация от текущей даты, уменьшая её в зависимости от частоты выполнения (frequency)
        return (int) Stream.iterate(dateToCalculate, completionHistory::contains,
                d -> HabitDatesUtils.decrementDateByFrequency(d, frequency, 1)).count();
    }

    // Метод для обновления процента успешного выполнения привычки
    public void updateCompletionPercentage(Habit habit) {
        LocalDate startDate = habit.getStartDate();
        LocalDate endDate = habit.getEndDate();
        List<LocalDate> completionHistory = habit.getExecutions();
        HabitFrequency frequency = habit.getFrequency();
        int successCompletionPercentage = habit.getExecutionPercentage();
        int percentageMultiplier = 100;
        int percent;

        if (completionHistory.isEmpty()) {
            return;
        }

        double count = (double) HabitDatesUtils.getFrequencyExecutionRangeFromPeriod(frequency, startDate, endDate);
        percent = (int) ((completionHistory.size() / count) * percentageMultiplier);

        if (percent > successCompletionPercentage) {
            habit.setExecutionPercentage(percent);
        }
    }

    // Метод для записи последнего усешного стрика, при его сбросе - если пользователь пропустил день или неделю
    public void updateStreakHistory(Habit habit, int streak) {
        List<LocalDate> tempDatesList = habit.getExecutions();

        if (tempDatesList.isEmpty()) {
            throw new IllegalStateException("История выполнения привычки пуста.");
        }

        int lastIndex = tempDatesList.size() - 1;

        // Удаление последнего элемента, если список не пуст
        boolean removed = tempDatesList.remove(tempDatesList.get(lastIndex));
        if (removed) {
            // Проверка, что streak не больше текущего количества дат
            if (streak > tempDatesList.size()) {
                throw new IllegalArgumentException("Streak превышает количество элементов в истории.");
            }

            LocalDate startDatePeriod = tempDatesList.get(tempDatesList.size() - streak);
            LocalDate endDatePeriod = tempDatesList.get(tempDatesList.size() - 1);

            habit.getStreakHistory().add(
                    String.format("Период: %s - %s, Количество стриков: %s",
                            startDatePeriod.toString(),
                            endDatePeriod.toString(),
                            streak
                    )
            );
        } else {
            throw new IllegalStateException("Не удалось удалить последний элемент истории.");
        }
    }
}
