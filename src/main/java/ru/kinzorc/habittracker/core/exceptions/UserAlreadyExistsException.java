package ru.kinzorc.habittracker.core.exceptions;

/**
 * Исключение, выбрасываемое при попытке создания пользователя, который уже существует в системе.
 * <p>
 * Это проверяемое исключение (наследник {@link Exception}), которое может быть выброшено,
 * когда пользователь с таким же уникальным идентификатором, именем или электронной почтой уже существует в системе.
 * </p>
 */
public class UserAlreadyExistsException extends Exception {

    /**
     * Создает новое исключение {@code UserAlreadyExistsException} без детализированного сообщения.
     */
    public UserAlreadyExistsException() {
    }

    /**
     * Создает новое исключение {@code UserAlreadyExistsException} с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение {@code UserAlreadyExistsException} с указанным сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина исключения, которая может быть получена позднее с помощью {@link Throwable#getCause()}
     */
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение {@code UserAlreadyExistsException} с указанной причиной.
     *
     * @param cause причина исключения, которая может быть получена позднее с помощью {@link Throwable#getCause()}
     */
    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    /**
     * Создает новое исключение {@code UserAlreadyExistsException} с указанным сообщением, причиной,
     * возможностью подавления исключения и управлением записи стека вызовов.
     *
     * @param message            сообщение об ошибке
     * @param cause              причина исключения
     * @param enableSuppression  флаг подавления исключения
     * @param writableStackTrace флаг, управляющий возможностью записи стека вызовов
     */
    public UserAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
