package ru.kinzorc.habittracker.core.utils;

import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HabitDatesUtils {

    public static LocalDate incrementDateByFrequency(LocalDate date, HabitFrequency frequency, int amount) {
        return switch (frequency) {
            case DAILY -> date.plusDays(amount);
            case WEEKLY -> date.plusWeeks(amount);
        };
    }

    public static LocalDate decrementDateByFrequency(LocalDate date, HabitFrequency frequency, int amount) {
        return switch (frequency) {
            case DAILY -> date.minusDays(amount);
            case WEEKLY -> date.minusWeeks(amount);
        };
    }

    // Метод для подсчета количества дней или недель в зависимости от частоты выполнения привычки, между датой начало выполнения привычки и датой окончания выполнения
    public static long getFrequencyExecutionRangeFromPeriod(HabitFrequency frequency, LocalDate startDateRange, LocalDate endDateRange) {
        switch (frequency) {
            case DAILY -> {
                return ChronoUnit.DAYS.between(startDateRange, endDateRange) + 1;
            }
            case WEEKLY -> {
                // Используем WeekFields для расчета количества недель
                WeekFields weekFields = WeekFields.of(Locale.getDefault()); // Можно передать любую локаль
                int startRangeWeek = startDateRange.get(weekFields.weekOfWeekBasedYear());
                int endRangeWeek = endDateRange.get(weekFields.weekOfWeekBasedYear());

                return endRangeWeek - startRangeWeek + 1;
            }
            default ->
                    throw new UnsupportedOperationException("Неподдерживаемая частота выполнения привычки: " + frequency);
        }
    }

    // Метод для расчета диапазона дат
    private List<LocalDate> getExecutionDateRangeFromPeriod(Habit habit, LocalDate startDateRange, LocalDate endDateRange) {
        if (habit == null) {
            throw new IllegalArgumentException("Объект привычки (habit) не может быть null");
        }

        if (startDateRange == null || endDateRange == null)
            throw new IllegalArgumentException("Дата начала периода или дата оканчания перода не может быть null");

        if (endDateRange.isBefore(startDateRange)) {
            throw new IllegalArgumentException("Дата окончания периода не может быть раньше даты начала периода");
        }

        try {
            // Расчет количества дней между startDateRange и endDateRange
            long numOfDays = ChronoUnit.DAYS.between(startDateRange, endDateRange) + 1;

            // Генерация диапазона дат
            return Stream.iterate(startDateRange, date -> date.plusDays(1))
                    .limit(numOfDays)
                    .collect(Collectors.toList());

        } catch (DateTimeException e) {
            throw new RuntimeException("Ошибка генерации диапазона дат: " + e.getMessage(), e);
        }
    }

}
