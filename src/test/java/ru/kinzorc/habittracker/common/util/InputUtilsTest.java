package ru.kinzorc.habittracker.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.kinzorc.habittracker.common.data.DataOfUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Тесты на корректность вводимых данных от пользователя")
class InputUtilsTest {

    @Test
    @DisplayName("Проверка имени")
    void testIsValidUsername() {
        // Пример некорректных имен, которые должны вернуть false
        assertFalse(InputUtils.isValidUsername(""));  // Пустая строка
        assertFalse(InputUtils.isValidUsername("ab"));  // Менее 3 символов
        assertFalse(InputUtils.isValidUsername("a_bc"));  // Символ подчеркивания
        assertFalse(InputUtils.isValidUsername("abc@"));  // Символ @

        // Пример корректных имен, которые должны вернуть true
        assertTrue(InputUtils.isValidUsername("abc"));
        assertTrue(InputUtils.isValidUsername("username123"));
    }

    @Test
    @DisplayName("Проверка email")
    void testIsValidEmail() {
        // Проверка валидных email
        assertTrue(InputUtils.isValidEmail("test@example.com"));
        assertTrue(InputUtils.isValidEmail("john.doe+filter@company.co"));

        // Проверка невалидных email
        assertFalse(InputUtils.isValidEmail("test@.com"));
        assertFalse(InputUtils.isValidEmail("example.com"));

    }

    @Test
    @DisplayName("Проверка пароля")
    void testIsValidPassword() {
        // Проверка валидных паролей
        assertTrue(InputUtils.isValidPassword("Aa1!abcd"));
        assertTrue(InputUtils.isValidPassword("Test123!@#"));

        // Проверка невалидных паролей
        assertFalse(InputUtils.isValidPassword("short"));
        assertFalse(InputUtils.isValidPassword("nocapital123"));
        assertFalse(InputUtils.isValidPassword("NoSpecialChar1"));
    }

    @Test
    @DisplayName("Проверка общего метода для вводимых данных")
    void testPromptValidInputUserData() {
        try (MockedStatic<InputUtils> mockedInputUtils = mockStatic(InputUtils.class)) {
            // Используем матчеры для всех аргументов метода
            mockedInputUtils.when(() -> InputUtils.promptValidInputUserData(any(), anyString(), anyString()))
                    .thenReturn("validData");

            // Вызов метода и проверка результата
            String result = InputUtils.promptValidInputUserData(DataOfUser.NAME, "Введите имя", "Некорректное имя");
            assertEquals("validData", result);
        }
    }

    @Test
    @DisplayName("Проверка ввода частоты выполнения привычки")
    void testPromptFrequencyValid() {
        try (var mockedInputUtils = mockStatic(InputUtils.class)) {
            mockedInputUtils.when(() -> InputUtils.promptFrequencyValid("Enter frequency: ", "Invalid frequency"))
                    .thenReturn("DAY");

            String result = InputUtils.promptFrequencyValid("Enter frequency: ", "Invalid frequency");
            assertEquals("DAY", result);
        }
    }

    @Test
    @DisplayName("Проверка даты - формат: \"dd.MM.yyyy\"")
    void testPromptDateValid() {
        try (var mockedInputUtils = mockStatic(InputUtils.class)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate expectedDate = LocalDate.parse("15.10.2024", dateFormatter);

            mockedInputUtils.when(() -> InputUtils.promptDateValid(anyString(), anyString()))
                    .thenReturn(expectedDate);

            LocalDate result = InputUtils.promptDateValid("Enter date: ", "Invalid date format");
            assertEquals(expectedDate, result);
        }
    }

    @Test
    @DisplayName("Проверка ввода статуса выполнения привычки")
    void testPromptStatusValid() {
        try (var mockedInputUtils = mockStatic(InputUtils.class)) {
            mockedInputUtils.when(() -> InputUtils.promptStatusValid("Enter status: ", "Invalid status"))
                    .thenReturn("ACTIVE");

            String result = InputUtils.promptStatusValid("Enter status: ", "Invalid status");
            assertEquals("ACTIVE", result);
        }
    }
}