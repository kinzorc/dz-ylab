package ru.kinzorc.habittracker.presentation.menu;

import ru.kinzorc.habittracker.application.service.ApplicationService;
import ru.kinzorc.habittracker.core.enums.User.UserData;
import ru.kinzorc.habittracker.infrastructure.repository.email.MailSender;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;

import javax.mail.MessagingException;
import java.util.Scanner;

public class AuthMenu implements Menu {

    @Override
    public void showMenu(ApplicationService applicationService, MenuUtils menuUtils) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nМеню авторизации:\n1) Вход 2) Сбросить пароль 3) Выход в главное меню");
        int option = menuUtils.promptMenuValidInput(scanner);

        switch (option) {
            case 1 -> {
                String email = menuUtils.promptInput(scanner, "  Email: ");
                String password = menuUtils.promptInput(scanner, "  Пароль: ");

                if (applicationService.loginUser(email, password)) {
                    MenuNavigator.ACCOUNT_MENU.showMenu(applicationService, menuUtils);
                }
            }
            case 2 -> {
                String email = menuUtils.promptInput(scanner, "Для сброса пароля введите email пользователя: ");

                if (applicationService.findUser(UserData.EMAIL, email)) {
                    MailSender mailSender = new MailSender();
                    String resetCode = menuUtils.generateResetCode();

                    try {
                        mailSender.sendEmail(email, "Код для сброса пароля", "Ваш код для сброса пароля: " + resetCode);
                    } catch (MessagingException e) {
                        throw new RuntimeException("Неудачная попытка отправить сообщение на почту");
                    }

                    String input = menuUtils.promptInput(scanner, "Введите код для сброса пароля: ");

                    if (input.equals(resetCode)) {
                        MenuNavigator.ACCOUNT_MENU.showMenu(applicationService, menuUtils);
                    } else {
                        System.err.println("Неверный проверочный код, выход в главное меню.");
                        MenuNavigator.MAIN_MENU.showMenu(applicationService, menuUtils);
                    }
                }
            }
            case 3 -> System.out.println("Возврат в главное меню.");
            default -> System.out.println("Пожалуйста, выберите один из предложенных вариантов.");

        }
    }
}
