package ru.kinzorc.habittracker.common.util;

import java.util.InputMismatchException;
import java.util.Scanner;


public class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtils() {
    }

    // Проверка имени
    private static boolean isValidUsername(String name) {
        // Имя должно содержать от 3 до 20 символов и начинаться с буквы
        String usernameRegex = "^[a-zA-Zа-яА-Я0-9][a-zA-Zа-яА-Я0-9-]{2,19}$";
        return name != null && name.matches(usernameRegex);
    }

    // Проверка адреса почты
    private static boolean isValidEmail(String email) {
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
    private static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    // Проверка на корректность кода из почты
    public static boolean isValidResetCode(String codeUser, String codeFromMail) {
        return codeUser != null && codeUser.equals(codeFromMail);
    }

    public static String promptInput(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    // Метод на проверку ввода данных пользователем (name, email, password)
    public static String promptValidInputUserData(DataOfUser param, String message, String errorMessage) {
        while (true) {
            String data = promptInput(message);
            switch (param) {
                case NAME -> {
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

    // Метод для проверки пользователем пунктов меню, проверяеся, что вводится число, а не любое другое значение
    public static int promptMenuValidInput() {
        StringBuilder value = new StringBuilder();

        while (true) {
            try {
                System.out.print("Введите число: ");
                value.append(scanner.nextInt());
                scanner.nextLine(); // Сброс буфера после ввода числа
                break;
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите целое число.");
                scanner.nextLine(); // Очистка некорректного ввода из буфера
            }
        }

        int option = Integer.parseInt(value.toString());
        value.setLength(0);

        return option;
    }

    public static String promptFrequencyValid(String message, String errorMessage) {
        while (true) {
            System.out.print(message);
            String data = scanner.nextLine();
            switch (data) {
                case "day" -> {
                    if ("day".equalsIgnoreCase(FrequencyHabit.DAY.toString()))
                        return "DAY";
                }
                case "week" -> {
                    if ("week".equalsIgnoreCase(FrequencyHabit.WEEK.toString()))
                        return "WEEK";
                }
            }
            System.out.println(errorMessage);
        }
    }
}
