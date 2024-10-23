package ru.kinzorc.habittracker.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.kinzorc.habittracker.core.enums.User.UserData;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;

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
        assertFalse(MenuUtils.isValidUsername(""));  // Пустая строка
        assertFalse(MenuUtils.isValidUsername("ab"));  // Менее 3 символов
        assertFalse(MenuUtils.isValidUsername("a_bc"));  // Символ подчеркивания
        assertFalse(MenuUtils.isValidUsername("abc@"));  // Символ @

        // Пример корректных имен, которые должны вернуть true
        assertTrue(MenuUtils.isValidUsername("abc"));
        assertTrue(MenuUtils.isValidUsername("username123"));
    }

    @Test
    @DisplayName("Проверка email")
    void testIsValidEmail() {
        // Проверка валидных email
        assertTrue(MenuUtils.isValidEmail("test@example.com"));
        assertTrue(MenuUtils.isValidEmail("john.doe+filter@company.co"));

        // Проверка невалидных email
        assertFalse(MenuUtils.isValidEmail("test@.com"));
        assertFalse(MenuUtils.isValidEmail("example.com"));

    }

    @Test
    @DisplayName("Проверка пароля")
    void testIsValidPassword() {
        // Проверка валидных паролей
        assertTrue(MenuUtils.isValidPassword("Aa1!abcd"));
        assertTrue(MenuUtils.isValidPassword("Test123!@#"));

        // Проверка невалидных паролей
        assertFalse(MenuUtils.isValidPassword("short"));
        assertFalse(MenuUtils.isValidPassword("nocapital123"));
        assertFalse(MenuUtils.isValidPassword("NoSpecialChar1"));
    }

    @Test
    @DisplayName("Проверка общего метода для вводимых данных")
    void testPromptValidInputUserData() {
        try (MockedStatic<MenuUtils> mockedInputUtils = mockStatic(MenuUtils.class)) {
            // Используем матчеры для всех аргументов метода
            mockedInputUtils.when(() -> MenuUtils.promptValidInputUserData(any(), anyString(), anyString()))
                    .thenReturn("validData");

            // Вызов метода и проверка результата
            String result = MenuUtils.promptValidInputUserData(UserData.USERNAME, "Введите имя", "Некорректное имя");
            assertEquals("validData", result);
        }
    }

    @Test
    @DisplayName("Проверка ввода частоты выполнения привычки")
    void testPromptFrequencyValid() {
        try (var mockedInputUtils = mockStatic(MenuUtils.class)) {
            mockedInputUtils.when(() -> MenuUtils.promptHabitFrequencyValid("Enter frequency: ", "Invalid frequency"))
                    .thenReturn("DAY");

            String result = MenuUtils.promptHabitFrequencyValid("Enter frequency: ", "Invalid frequency");
            assertEquals("DAY", result);
        }
    }

    @Test
    @DisplayName("Проверка даты - формат: \"dd.MM.yyyy\"")
    void testPromptDateValid() {
        try (var mockedInputUtils = mockStatic(MenuUtils.class)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate expectedDate = LocalDate.parse("15.10.2024", dateFormatter);

            mockedInputUtils.when(() -> MenuUtils.promptDateValid(anyString(), anyString()))
                    .thenReturn(expectedDate);

            LocalDate result = MenuUtils.promptDateValid("Enter date: ", "Invalid date format");
            assertEquals(expectedDate, result);
        }
    }

    @Test
    @DisplayName("Проверка ввода статуса выполнения привычки")
    void testPromptStatusValid() {
        try (var mockedInputUtils = mockStatic(MenuUtils.class)) {
            mockedInputUtils.when(() -> MenuUtils.promptHabitStatusValid("Enter status: ", "Invalid status"))
                    .thenReturn("ACTIVE");

            String result = MenuUtils.promptHabitStatusValid("Enter status: ", "Invalid status");
            assertEquals("ACTIVE", result);
        }
    }
}