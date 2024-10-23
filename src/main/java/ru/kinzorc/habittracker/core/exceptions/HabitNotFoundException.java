package ru.kinzorc.habittracker.core.exceptions;

/**
 * Исключение, выбрасываемое в случае, если привычка не найдена.
 * <p>
 * Это проверяемое исключение (наследник {@link Exception}), которое может быть выброшено,
 * когда запрашиваемая привычка отсутствует в системе или не может быть найдена по указанным параметрам.
 * </p>
 */
public class HabitNotFoundException extends Exception {

    /**
     * Создает новое исключение {@code HabitNotFoundException} без детализированного сообщения.
     */
    public HabitNotFoundException() {
    }

    /**
     * Создает новое исключение {@code HabitNotFoundException} с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public HabitNotFoundException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение {@code HabitNotFoundException} с указанным сообщением и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина исключения, которая может быть получена позднее с помощью {@link Throwable#getCause()}
     */
    public HabitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение {@code HabitNotFoundException} с указанной причиной.
     *
     * @param cause причина исключения, которая может быть получена позднее с помощью {@link Throwable#getCause()}
     */
    public HabitNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Создает новое исключение {@code HabitNotFoundException} с указанным сообщением, причиной,
     * возможностью подавления исключения и управлением записи стека вызовов.
     *
     * @param message            сообщение об ошибке
     * @param cause              причина исключения
     * @param enableSuppression  флаг подавления исключения
     * @param writableStackTrace флаг, управляющий возможностью записи стека вызовов
     */
    public HabitNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
