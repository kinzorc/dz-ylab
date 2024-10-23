package ru.kinzorc.habittracker.core.exceptions;

/**
 * Исключение, выбрасываемое при попытке добавить привычку, которая уже существует.
 * <p>
 * Это проверяемое исключение (наследник {@link Exception}), которое может быть выброшено в случае,
 * если пользователь пытается создать новую привычку с уже существующим уникальным идентификатором или другой уникальной характеристикой.
 * </p>
 */
public class HabitAlreadyExistsException extends Exception {

    /**
     * Создает новое исключение {@code HabitAlreadyExistsException} без детализированного сообщения.
     */
    public HabitAlreadyExistsException() {
    }

    /**
     * Создает новое исключение {@code HabitAlreadyExistsException} с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public HabitAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение {@code HabitAlreadyExistsException} с указанным сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина исключения, которая может быть получена позднее с помощью {@link Throwable#getCause()}
     */
    public HabitAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение {@code HabitAlreadyExistsException} с указанной причиной.
     *
     * @param cause причина исключения, которая может быть получена позднее с помощью {@link Throwable#getCause()}
     */
    public HabitAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    /**
     * Создает новое исключение {@code HabitAlreadyExistsException} с указанным сообщением, причиной,
     * возможностью подавления исключения и управлением записи стека вызовов.
     *
     * @param message            сообщение об ошибке
     * @param cause              причина исключения
     * @param enableSuppression  флаг подавления исключения
     * @param writableStackTrace флаг, управляющий возможностью записи стека вызовов
     */
    public HabitAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}