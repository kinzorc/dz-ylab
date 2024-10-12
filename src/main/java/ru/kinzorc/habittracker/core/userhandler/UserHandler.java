package ru.kinzorc.habittracker.core.handlerUser;

import ru.kinzorc.habittracker.common.util.DataOfUser;
import ru.kinzorc.habittracker.common.util.EmailSender;
import ru.kinzorc.habittracker.common.util.InputUtils;
import ru.kinzorc.habittracker.common.util.OutputUtils;
import ru.kinzorc.habittracker.core.model.User;
import ru.kinzorc.habittracker.core.service.ConfigAppManager;
import ru.kinzorc.habittracker.core.service.UserManager;


public class UserHandler {
    private static User currentUser = null;
    private static final UserManager userManager = UserManager.getInstance();


    // Метод для регистарции пользователя
    public static void handleRegisterNewUser() {
        String name = InputUtils.promptValidInputUserData(DataOfUser.NAME, "Введите имя: ",
                "Имя должно содержать от 3 до 20 символов и начинаться с буквы"); // Ввод имени
        String email = InputUtils.promptValidInputUserData(DataOfUser.EMAIL, "Введите email: ", "Некорректный email"); // Ввод email
        String password = InputUtils.promptValidInputUserData(DataOfUser.PASSWORD, "Введите пароль: ", """
                        Пароль должен содержать:
                        - минимум 8 символов
                        - хотя бы одну цифру
                        - хотя бы одну строчную букву
                        - хотя бы одну заглавную букву
                        - хотя бы один специальный символ"""); // ввод пароля

        boolean isRegistered = userManager.registerUser(name, email, password);

        System.out.println(isRegistered ? "Вы успешно зарегистрировались!" : "Пользователь с таким email уже существует");
    }

    // Метод для авторизации пользователя
    public static boolean handlerLoginUser() {
        System.out.println("Вход в личный кабинет:");
        String email = InputUtils.promptInput("  Введите email: ");
        String password = InputUtils.promptInput("  Введите пароль: ");

        currentUser = userManager.loginUser(email, password);

        if (!currentUser.isLogin()) {
            System.out.println("Неудачная попытка входа!");
            return false;
        }

        System.out.println("Успешный вход! Привет " + currentUser.getName() + "!");
        return true;
    }

    // Метод для удаления пользователя с проверкой пароля
    public static boolean handleDeleteUserFromAccount(User currentUser) {
        String password = InputUtils.promptInput("Введите пароль для удаления аккаунта: ");

        boolean isDeleted = UserManager.getInstance().deleteUser(currentUser, password);

        if (!isDeleted) {
            System.out.println("Пароль неверный. Удаление не удалось. Возврат в меню профиля.");
            return false;
        }

        System.out.println("Ваш аккаунт был успешно удален. Возврат в главное меню.");
        return true;
    }

    // Метод для сброса пароля
    public static boolean resetPassword() {
        if (ConfigAppManager.getMailProperties() != null) {
            System.out.println("Не настроена учетная запись для отправки почтовых уведомлений!");
            return false;
        }

        User user = userManager.getUser(InputUtils.promptInput("Введите email для сброса пароля: "));

        if (user == null) {
            System.out.println("Пользователь с таким email не зарегистрирован!");
            return false;
        }

        if (EmailSender.paramIsValid()) {
            String resetCode = OutputUtils.generateResetCode();
            try {
                EmailSender.sendEmail(user.getEmail(), "Код для сброса пароля", "Ваш код: " + resetCode);
                System.out.println("Код для сброса отправлен на email!");
            } catch (Exception e) {
                System.out.println("Ошибка отправки письма: " + e.getMessage());
                return false;
            }

            String userCode = InputUtils.promptInput("Введите код: ");
            if (!InputUtils.isValidResetCode(userCode, resetCode)) {
                System.out.println("Неверный код!");
                return false;
            }

            String newPassword = InputUtils.promptValidInputUserData(DataOfUser.PASSWORD, "Введите новый пароль: ",
                    """
                    Пароль должен содержать:
                    - минимум 8 символов
                    - хотя бы одну цифру
                    - хотя бы одну строчную букву
                    - хотя бы одну заглавную букву
                    - хотя бы один специальный символ
                    """);
            user.setPassword(newPassword);
            System.out.println("Пароль успешно изменен!");
            return true;
        }

        return false;
    }

    // Метод для смены имени пользователя
    public static void setNameForUser() {
        currentUser.setName(InputUtils.promptValidInputUserData(DataOfUser.NAME,
                "Введите имя: ", "Имя должно содержать от 3 до 20 символов и начинаться с буквы"));
    }

    // Метод для смены email пользователя
    public static void setEmailForUser() {
        currentUser.setEmail(InputUtils.promptValidInputUserData(DataOfUser.EMAIL, "Введите email: ", "Некорректный email")); // Ввод email
    }

    public static void setPasswordForUser() {
        currentUser.setPassword(InputUtils.promptValidInputUserData(DataOfUser.PASSWORD, "Введите пароль: ", """
                        Пароль должен содержать:
                        - минимум 8 символов
                        - хотя бы одну цифру
                        - хотя бы одну строчную букву
                        - хотя бы одну заглавную букву
                        - хотя бы один специальный символ""")); // ввод пароля
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}