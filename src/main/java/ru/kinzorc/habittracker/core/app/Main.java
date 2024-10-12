package ru.kinzorc.habittracker.app;

import ru.kinzorc.habittracker.service.UserManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();

        mainMenu(scanner, userManager);
    }

    public static void mainMenu(Scanner scanner, UserManager userManager) {
        StringBuilder data = new StringBuilder(); // для прохождение циклов ввода данных (имя, пароль, почта) в меню

        while (true) {
            System.out.println("Главное меню:\n1) Регистрация пользователя 2) Личный кабинет 3) Выход");
            System.out.print("Введите: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                String name;
                String email;
                String password;

                // Вводим имя с проверкой на корректность - не пустая строка
                System.out.print("Введите имя: ");

                while (true) {
                    data.append(scanner.nextLine());

                    if (data.toString().isEmpty()) {
                        System.out.println("Имя пользователя не может быть пустым!");
                        data.setLength(0);
                        System.out.print("Введите имя: ");
                    } else {
                        name = data.toString();
                        data.setLength(0);
                        break;
                    }
                }

                // Вводим email с проверкой на корректность
                System.out.print("Введите email: ");
                String regexOfEmail = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)";

                while (true) {
                    data.append(scanner.nextLine());
                    if (!data.toString().matches(regexOfEmail) || data.toString().isEmpty()) {
                        System.out.println("Некорректный email");
                        data.setLength(0);
                        System.out.print("Введите email: ");
                    } else {
                        email = data.toString();
                        data.setLength(0);
                        break;
                    }
                }

                // Вводим пароль
                System.out.print("Введите пароль: ");

                while (true) {
                    data.append(scanner.nextLine());
                    if (data.toString().isEmpty()) {
                        System.out.println("Пароль не может быть пустым");
                        data.setLength(0);
                        System.out.print("Введите пароль: ");
                    } else {
                        password = data.toString();
                        data.setLength(0);
                        break;
                    }
                }

                userManager.registerUser(name, email, password);
            } else if (option == 2) {
                System.out.print("Enter email: ");
                String email = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();


                if (userManager.loginUser(email, password)) {
                    boolean isExitCabinetMenu = false;
                    do {
                        System.out.println("Выберите пункт меню: 1) Создать привычку 2) Удалить привычку 3) Мои привычки 4) Выход");
                        int optionCabinetMenu = scanner.nextInt();
                        scanner.nextLine();

                        if (optionCabinetMenu == 4) {
                            isExitCabinetMenu = true;
                            System.out.println("Выход в главное меню");
                        }

                    } while (!isExitCabinetMenu);
                }
            } else if (option == 3) {
                System.out.println("Exiting...");
                return;
            } else {
                System.out.println("Ни один пункт меню не выбран!");
            }


        }
    }

    public static void loginMenu(Scanner scanner, UserManager userManager) {
        StringBuilder data = new StringBuilder();

        while (true) {
            System.out.println("Личный кабинет:\n1) Вход 2) Выход в главное меню");
            System.out.print("Введите: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                String email;
                String password;

                // Вводим email с проверкой на корректность
                System.out.print("Введите email: ");
                String regexOfEmail = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)";

                while (true) {
                    data.append(scanner.nextLine());
                    if (!data.toString().matches(regexOfEmail) || data.toString().isEmpty()) {
                        System.out.println("Некорректный email");
                        data.setLength(0);
                        System.out.print("Введите email: ");
                    } else {
                        email = data.toString();
                        data.setLength(0);
                        break;
                    }
                }

                // Вводим пароль
                System.out.print("Введите пароль: ");

                while (true) {
                    data.append(scanner.nextLine());
                    if (data.toString().isEmpty()) {
                        System.out.println("Пароль не может быть пустым");
                        data.setLength(0);
                        System.out.print("Введите пароль: ");
                    } else {
                        password = data.toString();
                        data.setLength(0);
                        break;
                    }
                }

                userManager.loginUser(email, password);
                accountMenu(scanner, userManager);
            }
        }
    }

    public static void accountMenu(Scanner scanner, UserManager userManager) {

    }
}
