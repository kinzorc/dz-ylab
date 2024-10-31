package ru.kinzorc.habittracker.application.exceptions;

/**
 * Исключение, выбрасываемое при неправильном вводе пароля при аутентификации.
 * <p>
 * Это проверяемое исключение (наследник {@link Exception}), которое может быть выброшено в случае,
 * если пользователь ввел неправильный пароль от учетной записи.
 * </p>
 */
public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}