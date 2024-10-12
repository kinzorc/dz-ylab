package ru.kinzorc.habittracker.core.service;


import ru.kinzorc.habittracker.common.util.InputUtils;


import java.util.Properties;
import java.util.Scanner;

public class AppManager {
    private static final Properties PROPERTIES = new Properties();
    private static final String CONFIG_FILE = "config.properties";
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void setParamApp() {
        System.out.println("Настройки приложения для отправки почтовых уведомлений для сброса пароля.\n");
        System.out.println("Отправка уведомлений реализована через отправку по SSL протоколу и тестировалась через почту gmail.com");

        System.out.println("Выберите параметр:\n1) Ввести данные 2) Выход в главное меню\n");
        int option = InputUtils.promptMenuValidInput();

        if (option == 1) {
            String smtpServer = InputUtils.promptInput("Введите адрес smtp сервера");
            String sslSmtpPort = InputUtils.promptInput("Введите ssl порт");
            String email =
        } else if (option == 2)
            System.out.println("Выход в главное меню");
    }

    private static void setProperties(String key, String value) {

    }

}