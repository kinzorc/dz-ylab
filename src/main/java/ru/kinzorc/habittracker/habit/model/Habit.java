package ru.kinzorc.habittracker.habit.model;


import ru.kinzorc.habittracker.common.util.FrequencyHabit;

import java.time.LocalDate;
import java.util.Objects;

public class Habit {
    private String name;
    private String description;
    private FrequencyHabit frequency;
    private final LocalDate createDateHabit;

    public Habit(String name, String description, FrequencyHabit frequencyHabit) {
        this.name = name;
        this.description = description;
        this.frequency = frequencyHabit;
        this.createDateHabit = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FrequencyHabit getFrequency() {
        return frequency;
    }

    public void setFrequency(FrequencyHabit frequency) {
        this.frequency = frequency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, frequency, createDateHabit);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;

        Habit habit = (Habit) obj;

        return Objects.equals(name, habit.name)
                && Objects.equals(description, habit.description)
                && Objects.equals(frequency, habit.frequency)
                && Objects.equals(createDateHabit, habit.createDateHabit);
    }


    @Override
    public String toString() {
        return "Привычка: " + name + ", Описание привычки: " + description + ", Частота выполнения: " + frequency;
    }
}
