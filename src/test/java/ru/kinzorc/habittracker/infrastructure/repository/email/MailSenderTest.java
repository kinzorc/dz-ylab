package ru.kinzorc.habittracker.infrastructure.repository.email;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MailSenderTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_SUBJECT = "Test Subject";
    private static final String TEST_TEXT = "This is a test email.";
    private static MailSender mailSender;
    private static Session mockSession;

    @BeforeAll
    public static void setup() {
        Properties properties = new Properties();
        mockSession = Session.getInstance(properties);
        mailSender = new MailSender(mockSession);
    }

    @Test
    @DisplayName("Отправка email с корректными параметрами")
    public void testSendEmailSuccess() throws Exception {
        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {
            mailSender.sendEmail(TEST_EMAIL, TEST_SUBJECT, TEST_TEXT);

            transportMock.verify(() -> Transport.send(any(Message.class)), times(1));
        }
    }

    @Test
    @DisplayName("Исключение при ошибке отправки email")
    public void testSendEmailThrowsMessagingException() throws Exception {
        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class)))
                    .thenThrow(new MessagingException("Ошибка отправки email"));

            assertThatThrownBy(() -> mailSender.sendEmail(TEST_EMAIL, TEST_SUBJECT, TEST_TEXT))
                    .isInstanceOf(MessagingException.class)
                    .hasMessageContaining("Ошибка отправки email");
        }
    }

}
