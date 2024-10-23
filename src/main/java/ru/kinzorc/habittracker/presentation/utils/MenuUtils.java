package ru.kinzorc.habittracker.presentation.utils;

import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.Habit.HabitStatus;
import ru.kinzorc.habittracker.core.enums.User.UserData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;


public class MenuUtils {

    // Проверка имени
    public boolean isValidUsername(String name) {
        // Имя должно содержать от 3 до 20 символов и начинаться с буквы
        String usernameRegex = "^[a-zA-Zа-яА-Я0-9][a-zA-Zа-яА-Я0-9-]{2,19}$";
        return name != null && name.matches(usernameRegex);
    }

    // Проверка адреса почты
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    // Проверка пароля
    // Пароль должен содержать:
    // - минимум 8 символов
    // - хотя бы одну цифру
    // - хотя бы одну строчную букву
    // - хотя бы одну заглавную букву
    // - хотя бы один специальный символ
    public boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    // Проверка на корректность кода из почты
    public boolean isValidResetCode(String codeUser, String codeFromMail) {
        return codeUser != null && codeUser.equals(codeFromMail);
    }

    public String promptInput(Scanner scanner, String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    // Метод на проверку ввода данных пользователем (name, email, password)
    public String promptValidInputUserData(Scanner scanner, UserData param, String message, String errorMessage) {
        while (true) {
            String data = promptInput(scanner, message);
            switch (param) {
                case USERNAME -> {
                    if (isValidUsername(data))
                        return data;
                }
                case EMAIL -> {
                    if (isValidEmail(data))
                        return data;
                }
                case PASSWORD -> {
                    if (isValidPassword(data))
                        return data;
                }
            }
            System.out.println(errorMessage);
        }
    }

    // Метод для проверки пользователем пунктов меню: проверяется, что вводится число, а не любое другое значение
    public int promptMenuValidInput(Scanner scanner) {
        int option;

        while (true) {
            System.out.print("Выберите пункт меню: ");
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine();
                return option;
            } else {
                System.out.println("Ошибка! Введите целое число.");
                scanner.nextLine();
            }
        }
    }

    public String promptHabitFrequencyValid(Scanner scanner, String message, String errorMessage) {
        while (true) {
            System.out.print(message);
            String data = scanner.nextLine();
            switch (data) {
                case "day" -> {
                    if (data.equalsIgnoreCase(HabitFrequency.DAILY.toString()))
                        return "DAY";
                }
                case "week" -> {
                    if (data.equalsIgnoreCase(HabitFrequency.WEEKLY.toString()))
                        return "WEEK";
                }
                case "0" -> {
                    return "0";
                }
                default -> {
                    System.out.println(errorMessage);
                    return null;
                }
            }
        }
    }

    public String promptHabitStatusValid(Scanner scanner, String message, String errorMessage) {
        while (true) {
            System.out.print(message);
            String data = scanner.nextLine();
            switch (data) {
                case "active" -> {
                    if (data.equalsIgnoreCase(HabitStatus.ACTIVE.toString()))
                        return data;
                }
                case "finished" -> {
                    if (data.equalsIgnoreCase(HabitStatus.FINISHED.toString()))
                        return data;
                }
                case "0" -> {
                    return "0";
                }
                default -> {
                    System.out.println(errorMessage);
                    return null;
                }
            }
        }
    }

    public String promptHabitExecutionPeriodValid(Scanner scanner, String message, String errorMessage) {
        while (true) {
            System.out.print(message);
            String data = scanner.nextLine();
            switch (data) {
                case "month" -> {
                    if (data.equalsIgnoreCase(HabitExecutionPeriod.MONTH.toString()))
                        return data;
                }
                case "year" -> {
                    if (data.equalsIgnoreCase(HabitExecutionPeriod.YEAR.toString()))
                        return data;
                }
                case "0" -> {
                    return "0";
                }
                default -> {
                    System.out.println(errorMessage);
                    return null;
                }
            }
        }
    }

    public LocalDateTime promptDateValid(Scanner scanner, String message, String errorMessage) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime date;

        while (true) {
            System.out.print(message);
            String data = scanner.nextLine();

            if (data.equalsIgnoreCase("0"))
                return null;

            // Парсим строку в дату
            try {
                date = LocalDateTime.parse(data, dateFormatter);
                break;
            } catch (Exception e) {
                System.out.println(errorMessage);
            }
        }

        return date;
    }

    public String generateResetCode() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

}
