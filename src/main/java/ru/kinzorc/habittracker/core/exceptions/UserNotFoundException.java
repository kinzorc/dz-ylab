package ru.kinzorc.habittracker.core.exceptions;

/**
 * Исключение, выбрасываемое при попытке найти пользователя, который отсутствует в системе.
 * <p>
 * Это проверяемое исключение (наследник {@link Exception}), которое выбрасывается, когда пользователь не может быть найден
 * в системе по указанному идентификатору, имени или другому уникальному атрибуту.
 * </p>
 */
public class UserNotFoundException extends Exception {

    /**
     * Создает новое исключение {@code UserNotFoundException} без детализированного сообщения.
     */
    public UserNotFoundException() {
    }

    /**
     * Создает новое исключение {@code UserNotFoundException} с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение {@code UserNotFoundException} с указанным сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина исключения, которая может быть получена позднее с помощью {@link Throwable#getCause()}
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение {@code UserNotFoundException} с указанной причиной.
     *
     * @param cause причина исключения, которая может быть получена позднее с помощью {@link Throwable#getCause()}
     */
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Создает новое исключение {@code UserNotFoundException} с указанным сообщением, причиной,
     * возможностью подавления исключения и управлением записи стека вызовов.
     *
     * @param message            сообщение об ошибке
     * @param cause              причина исключения
     * @param enableSuppression  флаг подавления исключения
     * @param writableStackTrace флаг, управляющий возможностью записи стека вызовов
     */
    public UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
