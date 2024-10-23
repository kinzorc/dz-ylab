package ru.kinzorc.habittracker.core.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.kinzorc.habittracker.application.service.HabitService;
import ru.kinzorc.habittracker.common.config.HandlerConstants;
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.presentation.utils.MenuUtils;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Тесты класса HabitHandler")
class HabitHandlerTest {

    private User user;

    @BeforeEach
    void setUp() {
        // Инициализация пользователя
        user = new User("testUser", "test@example.com", "password", false, false, false);
        // Мокируем HandlerConstants.HABIT_SERVICE
        HandlerConstants.HABIT_SERVICE = Mockito.mock(HabitService.class);
    }

    @Test
    @DisplayName("Тест на создание привычки")
    void testAddHabitForUser() {
        try (var mockedInputUtils = mockStatic(MenuUtils.class)) {
            // Мокируем ввод данных
            mockedInputUtils.when(() -> MenuUtils.promptInput(anyString()))
                    .thenReturn("Morning Run", "Run every morning");

            mockedInputUtils.when(() -> MenuUtils.promptHabitFrequencyValid(anyString(), anyString()))
                    .thenReturn("DAY");  // Возвращаем "DAY" для частоты выполнения

            mockedInputUtils.when(() -> MenuUtils.promptDateValid(anyString(), anyString()))
                    .thenReturn(LocalDate.now().plusDays(1));  // Дата начала в будущем

            // Мокирование вызова сервиса
            doNothing().when(HandlerConstants.HABIT_SERVICE).addHabit(any(User.class), any(Habit.class));

            // Запуск тестируемого метода
            HabitHandler.addHabitForUser(user);

            // Проверка, что привычка была добавлена через сервис
            verify(HandlerConstants.HABIT_SERVICE).addHabit(any(User.class), any(Habit.class));
        }
    }

    @Test
    @DisplayName("Тест на удаления привычки")
    void testRemoveHabitForUser() {
        try (var mockedInputUtils = mockStatic(MenuUtils.class)) {
            // Мокируем ввод названия привычки
            mockedInputUtils.when(() -> MenuUtils.promptInput(anyString()))
                    .thenReturn("Morning Run");

            // Мокирование вызова удаления привычки
            when(HandlerConstants.HABIT_SERVICE.removeHabit(any(User.class), anyString()))
                    .thenReturn(true);  // Предположим, что привычка была удалена

            // Запуск тестируемого метода
            HabitHandler.removeHabitForUser(user);

            // Проверка, что привычка была удалена через сервис
            verify(HandlerConstants.HABIT_SERVICE).removeHabit(any(User.class), anyString());
        }
    }

    @Test
    @DisplayName("Тест на изменение информации о привычке")
    void testEditHabitForUser() {
        Habit mockHabit = new Habit("Morning Run", "Run every morning", HabitFrequency.DAILY, LocalDate.now());

        try (var mockedInputUtils = mockStatic(MenuUtils.class)) {
            // Мокируем ввод названия привычки
            mockedInputUtils.when(() -> MenuUtils.promptInput(anyString()))
                    .thenReturn("Morning Run", "Evening Run");

            // Мокируем получение привычки
            when(HandlerConstants.HABIT_SERVICE.getHabit(any(User.class), anyString()))
                    .thenReturn(mockHabit);

            // Запуск тестируемого метода
            HabitHandler.editHabitForUser(user);

            // Проверка, что привычка была получена и изменена
            verify(HandlerConstants.HABIT_SERVICE).getHabit(any(User.class), anyString());
        }
    }


    @Test
    @DisplayName("Тесты об отметке выполнения найденой привычки")
    void testUserMarkDoneHabitHabitFound() {
        try (var mockedInputUtils = mockStatic(MenuUtils.class)) {
            Habit mockHabit = new Habit("Morning Run", "Run every morning", HabitFrequency.DAILY, LocalDate.now().minusDays(5));

            // Мокируем ввод названия привычки
            mockedInputUtils.when(() -> MenuUtils.promptInput(anyString()))
                    .thenReturn("Morning Run");

            // Мокируем получение привычки
            when(HandlerConstants.HABIT_SERVICE.getHabit(any(User.class), anyString()))
                    .thenReturn(mockHabit);

            // Мокируем вызов метода для отметки привычки
            doNothing().when(HandlerConstants.HABIT_SERVICE).markHabit(mockHabit);

            // Запуск тестируемого метода
            HabitHandler.userMarkDoneHabit(user);

            // Проверка, что привычка была получена и выполнена
            verify(HandlerConstants.HABIT_SERVICE).getHabit(any(User.class), anyString());
            verify(HandlerConstants.HABIT_SERVICE).markHabit(mockHabit);
        }
    }

    @Test
    @DisplayName("Тест на отметку выполнения не найденой привычки")
    void testUserMarkDoneHabitHabitNotFound() {
        try (var mockedInputUtils = mockStatic(MenuUtils.class)) {
            // Мокируем ввод названия привычки
            mockedInputUtils.when(() -> MenuUtils.promptInput(anyString()))
                    .thenReturn("Evening Walk");

            // Мокируем отсутствие привычки
            when(HandlerConstants.HABIT_SERVICE.getHabit(any(User.class), anyString()))
                    .thenReturn(null);

            // Запуск тестируемого метода
            HabitHandler.userMarkDoneHabit(user);

            // Проверка, что привычка была запрошена, но не найдена
            verify(HandlerConstants.HABIT_SERVICE).getHabit(any(User.class), anyString());
        }
    }
}