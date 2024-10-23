package ru.kinzorc.habittracker.infrastructure.repository.email;

import ru.kinzorc.habittracker.application.service.EmailService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JavaxMailSender implements EmailService {

    private static final Properties MAIL_PROPERTIES = new Properties();

    public JavaxMailSender() {
        loadProperties();
    }

    // Имплеминтируемый метод для отправки почты
    @Override
    public void sendEmail(String recipient, String subject, String text) throws MessagingException {
        final String username = MAIL_PROPERTIES.getProperty("email.username");
        final String password = MAIL_PROPERTIES.getProperty("email.password");

        // Проверяем наличие критических параметров
        if (username == null || password == null) {
            throw new IllegalArgumentException("Отсутствуют параметры для email аутентификации.");
        }

        Session session = Session.getInstance(MAIL_PROPERTIES, new Authenticator() {
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
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e) {
            // Логируем ошибку и выбрасываем исключение дальше
            System.err.println("Ошибка отправки email: " + e.getMessage());
            throw e;
        }
    }

    // Метод для загрузки конфигурационного файла
    private void loadProperties() {
        try (InputStream inputStream = JavaxMailSender.class.getResourceAsStream("/application.properties")) {
            MAIL_PROPERTIES.load(inputStream);
        } catch (IOException e) {
            System.err.println("Ошибка чтения конфигурационного файла: " + e.getMessage());
        }
    }

}