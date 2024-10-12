package ru.kinzorc.habittracker.common.util;

import ru.kinzorc.habittracker.core.service.ConfigAppManager;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private static final Properties PROPERTIES = ConfigAppManager.getMailProperties();
    private static final String paramIsValid = PROPERTIES.getProperty("param.valid");

    // Метод для отправки почты
    public static void sendEmail(String recipient, String subject, String content) throws Exception {
        final String username = PROPERTIES.getProperty("email.username");
        final String password = PROPERTIES.getProperty("email.password");

        // Проверяем наличие критических параметров
        if (username == null || password == null) {
            throw new IllegalArgumentException("Отсутствуют параметры для email аутентификации.");
        }

        Session session = Session.getInstance(PROPERTIES, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
        } catch (MessagingException e) {
            // Логируем ошибку и выбрасываем исключение дальше
            System.err.println("Ошибка отправки email: " + e.getMessage());
            throw e;
        }
    }

    public static boolean paramIsValid() {
        return paramIsValid.equalsIgnoreCase("true");
    }
}