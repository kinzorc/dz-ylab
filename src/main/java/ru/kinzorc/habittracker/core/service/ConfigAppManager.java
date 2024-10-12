package ru.kinzorc.habittracker.core.service;


import ru.kinzorc.habittracker.common.util.DataOfUser;
import ru.kinzorc.habittracker.common.util.EmailSender;
import ru.kinzorc.habittracker.common.util.InputUtils;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigAppManager {
    private static final Properties PROPERTIES = new Properties();
    private static final Properties MAIL_PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static void setParamApp() {
        System.out.println("Настройки приложения для отправки почтовых уведомлений для сброса пароля.\n");
        System.out.println("Отправка уведомлений реализована через отправку по SSL протоколу и тестировалась через почту gmail.com");

        System.out.println("Выберите параметр:\n1) Ввести данные 2) Выход в главное меню\n");
        int option = InputUtils.promptMenuValidInput();

        if (option == 1) {
            String smtpServer = InputUtils.promptInput("Введите адрес smtp сервера: ");
            String smtpPort = InputUtils.promptInput("Введите ssl порт: ");
            String email = InputUtils.promptValidInputUserData(DataOfUser.EMAIL, "Введите email: ", "Некорректный email");
            String password = InputUtils.promptInput("Введите пароль: ");

            setProperties(smtpServer, smtpPort, smtpPort, email, password);
        } else if (option == 2)
            System.out.println("Выход в главное меню");
    }

    // Метод для загрузки параметров для отправки email
    private static void loadProperties() {
        try (InputStream inputStream = EmailSender.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IOException("Файл config.properties не найден.");
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            System.err.println("Ошибка загрузки файла с параметрами: " + e.getMessage());
        }
    }

    private static void setProperties(String smtpServer, String smtpPort, String sFactoryPort,
                                      String username, String password) {
        MAIL_PROPERTIES.put("mail.smtp.host", smtpServer);
        MAIL_PROPERTIES.put("mail.smtp.port", smtpPort);
        MAIL_PROPERTIES.put("mail.smtp.auth", "true");
        MAIL_PROPERTIES.put("mail.smtp.socketFactory.port", sFactoryPort);
        MAIL_PROPERTIES.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        MAIL_PROPERTIES.put("mail.smtp.socketFactory.fallback", "false");
        MAIL_PROPERTIES.put("email.username", username);
        MAIL_PROPERTIES.put("email.password", password);
        MAIL_PROPERTIES.put("param.valid", "true");
        try (FileOutputStream output = new FileOutputStream("src/main/resources/config.properties")) {
            MAIL_PROPERTIES.store(output, null); // Сохраняем без комментариев
        } catch (Exception e) {
            System.out.println("Настройки установлены, но при сохранении в файл возника ошибка - " + e.getMessage());
        }
    }

    public static Properties getMailProperties() {
        return MAIL_PROPERTIES;
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }

}