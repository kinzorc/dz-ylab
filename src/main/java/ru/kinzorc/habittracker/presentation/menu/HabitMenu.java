package ru.kinzorc.habittracker.presentation.menu;

import ru.kinzorc.habittracker.application.service.ApplicationService;
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.HabitStatistic;
import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;
import ru.kinzorc.habittracker.presentation.utils.PrintUtils;

import java.time.LocalDateTime;
import java.util.Scanner;

public class HabitMenu implements Menu {

    @Override
    public void showMenu(ApplicationService applicationService, MenuUtils menuUtils) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nМои привычки:\n");
            System.out.println("""

                    Управление: 1) Статистика по привычке 2) Отметить выполнение 3) Добавить привычку
                                4) Изменить привычку 5) Завершить привычку 6) Удалить привычку 7) Выход в личный кабинет""");
            int option = menuUtils.promptMenuValidInput(scanner);
            int subOption;
            String habitName;
            Habit habit;

            switch (option) {
                case 1 -> {
                    habitName = menuUtils.promptInput(scanner, "Для отображения статистики введите название привычки: ");
                    if (applicationService.findHabitByName(habitName).isPresent()) {
                        habit = applicationService.findHabitByName(habitName).get();

                        System.out.println("Введите 1 - отображения всей статистики 2 - указать период: ");
                        subOption = menuUtils.promptMenuValidInput(scanner);

                        if (subOption == 1) {
                            HabitStatistic habitStatistic = applicationService.getHabitStatistic(habit.getId(), habit.getCreatedDate(), LocalDateTime.now());

                            if (habitStatistic != null) {
                                PrintUtils.printInfoForHabit(habit, habitStatistic);

                                menuUtils.promptInput(scanner, "Нажмите enter для выхода...");
                            }
                        } else if (subOption == 2) {
                            LocalDateTime startDate = menuUtils.promptDateValid(scanner,
                                    "Введите дату начала периода (формат dd-MM-yyyy): ", "Ошибка при вводе даты.");
                            LocalDateTime endDate = menuUtils.promptDateValid(scanner,
                                    "Введите дату окончания периода (формат dd-MM-yyyy): ", "Ошибка при вводе даты.");

                            HabitStatistic habitStatistic = applicationService.getHabitStatistic(habit.getId(), startDate, endDate);

                            if (habitStatistic != null) {
                                PrintUtils.printInfoForHabit(habit, habitStatistic);
                                habit = null;
                                menuUtils.promptInput(scanner, "Нажмите enter для выхода...");
                            }
                        }
                    } else {
                        return;
                    }
                }
                case 2 -> {
                    habitName = menuUtils.promptInput(scanner, "Введите название привычки: ");
                    if (applicationService.findHabitByName(habitName).isPresent()) {
                        habit = applicationService.findHabitByName(habitName).get();
                        applicationService.markExecution(habit.getId(), LocalDateTime.now());
                        habit = null;
                    }
                }
                case 3 -> {
                    habitName = menuUtils.promptInput(scanner, "Введите название новой привычки: ");
                    String habitDescription = menuUtils.promptInput(scanner, "Введите описание привычки: ");
                    String habitFrequency = menuUtils.promptHabitFrequencyValid(scanner,
                            "Частота выполнения (day/week): ",
                            "Неверное значение, попробуйте еще раз.");
                    LocalDateTime startDate = menuUtils.promptDateValid(scanner,
                            "Дата начала выполнения (формат - dd.mm.yyyy): ",
                            "Неверно указана дата");
                    String habitExecutionPeriod = menuUtils.promptHabitExecutionPeriodValid(scanner,
                            "Период выполнения привычки (month/year): ",
                            "Неверно указан период, попробуйте еще раз.");

                    while (startDate != null && startDate.isBefore(LocalDateTime.now())) {
                        System.out.println("Дата начала выполнения привычки не может быть меньше текущей даты, введите корректную дату!");
                        startDate = menuUtils.promptDateValid(scanner, "Дата начала выполнения (формат - dd.mm.yyyy): ", "Неверно указана дата");
                    }

                    applicationService.addHabit(applicationService.getCurrentUser(), habitName, habitDescription,
                            HabitFrequency.valueOf(habitFrequency), startDate, HabitExecutionPeriod.valueOf(habitExecutionPeriod));
                    habit = null;
                }
                case 4 -> {
                    habitName = menuUtils.promptInput(scanner, "Введите название привычки: ");

                    if (applicationService.findHabitByName(habitName).isEmpty())
                        return;

                    habit = applicationService.findHabitByName(habitName).get();

                    System.out.println("Изменить: 1) Имя 2) Описание 3) Частоту выполнения 4) Дата начала выполнения 5) Изменить статус 6) Очистить историю 7) Выход в меню привычек");
                    subOption = menuUtils.promptMenuValidInput(scanner);


                    switch (subOption) {
                        case 1 -> {
                            System.out.println("Изменение названия привычки");
                            habit.setName(menuUtils.promptInput(scanner, "Введите новое название: "));
                            applicationService.editHabit(habit);
                        }
                        case 2 -> {
                            System.out.println("Изменение описания привычки");
                            habit.setDescription(menuUtils.promptInput(scanner, "Введите новое описание: "));
                            applicationService.editHabit(habit);
                        }
                        case 3 -> {
                            System.out.println("При изменении частоты привычки, вся статистика сбрасывается");
                            habit.setFrequency(HabitFrequency.valueOf(menuUtils.promptHabitFrequencyValid(scanner,
                                    "Введите новую частоту выполнения (daily/weekly): ",
                                    "Неправильно введена частота выполнения")));
                            applicationService.resetStatistics(habit.getId(), true, true);
                            applicationService.editHabit(habit);
                        }
                        case 4 -> {
                            System.out.println("При изменении даты начала выполнения привычки, вся статистика сбрасывается");

                            LocalDateTime newDate = menuUtils.promptDateValid(scanner,
                                    "Введите новую дату начала (формат dd-MM-yyyy): ",
                                    "Неправильно введена дата");

                            if (newDate.isBefore(habit.getStartDate()) || newDate.isBefore(habit.getCreatedDate())) {
                                System.err.println("Дата не может быть меньше текущей даты начала выполнения или даты создания привычки!");
                                return;
                            }

                            habit.setStartDate(newDate);
                            applicationService.resetStatistics(habit.getId(), true, true);
                            applicationService.editHabit(habit);
                        }
                        case 5 -> {
                            System.out.println("Изменение статуса привычки");
                            habit.setStatus(HabitStatus.valueOf(menuUtils.promptHabitStatusValid(scanner,
                                    "Введите ACTIVE/FINISHED: ",
                                    "Не правильно указан статус")));
                            applicationService.editHabit(habit);
                        }
                        case 6 -> {
                            System.out.println("Очищение статистики привычки");
                            applicationService.resetStatistics(habit.getId(), true, true);
                        }
                        case 7 -> System.out.println("Выход в меню привычек");
                    }

                }
                case 5 -> {
                    habitName = menuUtils.promptInput(scanner, "Введите название привычки: ");
                    if (applicationService.findHabitByName(habitName).isEmpty())
                        return;

                    habit = applicationService.findHabitByName(habitName).get();
                    habit.setStatus(HabitStatus.FINISHED);
                    applicationService.editHabit(habit);

                    System.out.println("Выполнение привычки " + habit.getName() + " завершено");
                }
                case 6 -> {
                    habitName = menuUtils.promptInput(scanner, "Введите название привычки: ");
                    if (applicationService.findHabitByName(habitName).isEmpty())
                        return;

                    habit = applicationService.findHabitByName(habitName).get();

                    applicationService.deleteHabit(habit.getId());
                }
                case 7 -> {
                    System.out.println("Возврат в личный кабинет");
                    return;
                }
                default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");
            }
        }
    }
}
