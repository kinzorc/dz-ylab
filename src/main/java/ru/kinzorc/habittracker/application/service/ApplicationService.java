package ru.kinzorc.habittracker.application.service;

import ru.kinzorc.habittracker.application.dto.UserDTO;
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.HabitStatistic;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.Habit.HabitData;
import ru.kinzorc.habittracker.core.enums.Habit.HabitExecutionPeriod;
import ru.kinzorc.habittracker.core.enums.Habit.HabitFrequency;
import ru.kinzorc.habittracker.core.enums.User.UserData;
import ru.kinzorc.habittracker.core.enums.User.UserRole;
import ru.kinzorc.habittracker.core.exceptions.HabitAlreadyExistsException;
import ru.kinzorc.habittracker.core.exceptions.HabitNotFoundException;
import ru.kinzorc.habittracker.core.exceptions.UserAlreadyExistsException;
import ru.kinzorc.habittracker.core.exceptions.UserNotFoundException;
import ru.kinzorc.habittracker.core.repository.HabitRepository;
import ru.kinzorc.habittracker.core.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ApplicationService {

    private final UserRepository userRepository;
    private final HabitRepository habitRepository;

    private User currentUser;

    public ApplicationService(UserRepository userRepository, HabitRepository habitRepository) {
        this.userRepository = userRepository;
        this.habitRepository = habitRepository;
    }

    public void registerUser(String name, String password, String email) {
        if (name != null && password != null && email != null) {
            try {
                User user = new User(name, password, email, UserRole.USER);
                userRepository.addUser(user);
            } catch (UserAlreadyExistsException e) {
                System.out.println("Пользователь с таким email уже зарегистрирован!");
            } catch (SQLException e) {
                System.err.println("Ошибка добавления пользователя в базу!");
            }
        } else {
            System.err.println("Некорректные или пустые данные: имя, пароль, email.");
        }
    }

    public boolean loginUser(String email, String password) {
        try {
            Optional<UserDTO> userDTO = userRepository.findUser(UserData.EMAIL, email);
            if (userDTO.isPresent()) {
                User user = userDTO.get().toUser();

                if (user.getPassword().equals(password)) {
                    currentUser = user;
                    userRepository.addSession(userDTO.get().getId());
                    return true;
                }
            }

            System.err.println("Неудачная попытка входа. Попробуйте еще раз.");
            return false;
        } catch (SQLException e) {
            System.err.println("Ошибка в запросе к базе данных");
            return false;
        } catch (UserNotFoundException e) {
            System.err.println("Пользователь не найден!");
            return false;
        }
    }

    public boolean logoutUser(User user) {
        try {
            currentUser = null;
            userRepository.removeSession(user.getId());
            System.out.println("Выход из аккаунта " + user.getUserName());
            return true;
        } catch (SQLException e) {
            System.err.println("Произошла ошибка! Попробуйте еще раз!");
            return false;
        }
    }

    // Изменение данных пользователя (имя, email, пароль)
    public void editUser(User user, UserData userData, String newValue) {
        if (currentUser == null) {
            System.err.println("Вы не авторизованы!");
            return;
        }

        try {
            userRepository.editUser(user, userData, newValue);
            System.out.println("Данные пользователя успешно обновлены.");
        } catch (SQLException e) {
            System.err.println("Ошибка обновления данных пользователя.");
        } catch (UserNotFoundException e) {
            System.err.println("Пользователь не найден!");
        }
    }

    // Изменение роли пользователя
    public void changeUserRole(User user, UserRole newRole) {
        if (currentUser == null || !currentUser.getUserRole().equals(UserRole.ADMIN)) {
            System.err.println("У вас недостаточно прав для изменения роли.");
            return;
        }

        try {
            userRepository.updateUserPrivileges(user.getId(), newRole);
            System.out.println("Роль пользователя успешно обновлена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении роли.");
        } catch (UserNotFoundException e) {
            System.err.println("Пользователь не найден!");
        }
    }

    // Блокировка пользователя с удалением сессии
    public void blockUser(long userId) {
        if (currentUser == null || !currentUser.getUserRole().equals(UserRole.ADMIN)) {
            System.err.println("У вас недостаточно прав для блокировки пользователя.");
            return;
        }

        try {
            userRepository.removeSession(userId);
            userRepository.blockUser(userId);
            System.out.println("Пользователь успешно заблокирован.");
        } catch (SQLException e) {
            System.err.println("Ошибка при блокировке пользователя.");
        } catch (UserNotFoundException e) {
            System.err.println("Пользователь не найден!");
        }
    }

    // Удаление пользователя с удалением сессии
    public void deleteUser(long userId) {
        if (currentUser == null || !currentUser.getUserRole().equals(UserRole.ADMIN)) {
            System.err.println("У вас недостаточно прав для удаления пользователя.");
            return;
        }

        try {
            userRepository.removeSession(userId);
            userRepository.deleteUser(userId);
            currentUser = null;
            System.out.println("Пользователь успешно удален.");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении пользователя.");
        } catch (UserNotFoundException e) {
            System.err.println("Пользователь не найден!");
        }
    }

    public boolean findUser(UserData userData, String value) {
        boolean result = false;

        try {
            Optional<UserDTO> userDTO = userRepository.findUser(userData, value);

            switch (userData) {
                case ID -> result = userDTO.map(dto -> dto.getId() == Long.parseLong(value)).orElse(false);
                case USERNAME -> result = userDTO.map(dto -> dto.getUserName().equals(value)).orElse(false);
                case EMAIL -> result = userDTO.map(dto -> dto.getEmail().equals(value)).orElse(false);
                default -> System.err.println("Указан неправильный параметр пользователя!");
            }
        } catch (UserNotFoundException e) {
            System.err.println("Пользователь не найден или некорректный email.");
        } catch (SQLException e) {
            System.err.println("Ошибка! Попробуйте еще раз" + e.getMessage());
        }

        return result;
    }


    public User getCurrentUser() {
        if (currentUser == null) {
            System.err.println("Вы не авторизованы в данной сессии!");
            return null;
        }

        return currentUser;
    }

    public List<UserDTO> findAllUsers() {
        try {
            return userRepository.findAllUsers();
        } catch (SQLException e) {
            System.err.println("Ошибка! Попробуйте еще раз" + e.getMessage());
            return null;
        }
    }

    /**
     * Добавляет новую привычку для пользователя.
     *
     * @param user            пользователь, которому добавляется привычка
     * @param habitName       имя привычки
     * @param description     описание привычки
     * @param frequency       частота выполнения привычки
     * @param executionPeriod период выполнения привычки
     * @param startDate       дата начала выполнения
     */
    public void addHabit(User user, String habitName, String description, HabitFrequency frequency,
                         LocalDateTime startDate, HabitExecutionPeriod executionPeriod) {
        try {
            Habit habit = new Habit(habitName, description, frequency, startDate, executionPeriod);
            habitRepository.addHabit(user, habit);
            System.out.println("Привычка добавлена успешно!");
        } catch (HabitAlreadyExistsException e) {
            System.err.println("Привычка с таким именем уже существует.");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении привычки в базу данных.");
        }
    }

    /**
     * Удаляет привычку по её ID.
     *
     * @param habitId ID привычки
     */
    public void deleteHabit(long habitId) {
        try {
            habitRepository.deleteHabit(habitId);
            System.out.println("Привычка удалена успешно.");
        } catch (HabitNotFoundException e) {
            System.err.println("Привычка не найдена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении привычки.");
        }
    }

    /**
     * Редактирует существующую привычку.
     *
     * @param habit обновленный объект привычки
     */
    public void editHabit(Habit habit) {
        try {
            habitRepository.editHabit(habit);
            System.out.println("Привычка успешно обновлена.");
        } catch (HabitNotFoundException e) {
            System.err.println("Привычка не найдена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении привычки.");
        }
    }

    /**
     * Добавляет отметку о выполнении привычки на указанную дату.
     *
     * @param habitId       ID привычки
     * @param executionDate дата выполнения
     */
    public void markExecution(long habitId, LocalDateTime executionDate) {
        try {
            habitRepository.markExecution(habitId, executionDate);
            System.out.println("Отметка о выполнении привычки добавлена.");
        } catch (HabitNotFoundException e) {
            System.err.println("Привычка не найдена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении отметки о выполнении.");
        }
    }

    /**
     * Сбрасывает статистику выполнения привычки.
     *
     * @param habitId         ID привычки
     * @param resetExecutions сброс выполненных действий
     * @param resetStreaks    сброс стриков
     */
    public void resetStatistics(long habitId, boolean resetExecutions, boolean resetStreaks) {
        try {
            habitRepository.resetStatistics(habitId, resetExecutions, resetStreaks);
            System.out.println("Статистика привычки успешно сброшена.");
        } catch (HabitNotFoundException e) {
            System.err.println("Привычка не найдена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при сбросе статистики.");
        }
    }

    /**
     * Возвращает список всех привычек.
     *
     * @return список всех привычек
     */
    public List<Habit> getAllHabits() {
        try {
            return habitRepository.findAllHabits();
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка привычек.");
            return List.of();
        }
    }

    /**
     * Возвращает привычку по её имени.
     *
     * @param habitName имя привычки
     * @return объект привычки
     */
    public Optional<Habit> findHabitByName(String habitName) {
        try {
            return habitRepository.findHabit(HabitData.NAME, habitName);
        } catch (HabitNotFoundException e) {
            System.err.println("Привычка не найдена.");
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске привычки.");
            return Optional.empty();
        }
    }

    /**
     * Возвращает статистику выполнения привычки за указанный период.
     *
     * @param habitId         ID привычки
     * @param startPeriodDate начало периода
     * @param endPeriodDate   конец периода
     * @return объект {@link HabitStatistic} с данными за указанный период
     */
    public HabitStatistic getHabitStatistic(long habitId, LocalDateTime startPeriodDate, LocalDateTime endPeriodDate) {
        try {
            return habitRepository.getStatisticByPeriod(habitId, startPeriodDate, endPeriodDate);
        } catch (HabitNotFoundException e) {
            System.err.println("Привычка не найдена.");
            return null;
        } catch (SQLException e) {
            System.err.println("Ошибка при получении статистики.");
            return null;
        }
    }

    /**
     * Рассчитывает процент выполнения привычки.
     *
     * @param habitId ID привычки
     * @return процент выполнения
     */
    public int calculateExecutionPercentage(long habitId) {
        try {
            return habitRepository.calculateExecutionPercentage(habitId);
        } catch (SQLException e) {
            System.err.println("Ошибка при расчете процента выполнения.");
            return 0;
        }
    }

    /**
     * Проверяет, существует ли привычка с указанным именем.
     *
     * @param habitName имя привычки
     * @return {@code true}, если привычка существует, иначе {@code false}
     */
    public boolean isHabitExist(String habitName) {
        try {
            return habitRepository.isHabitExist(habitName);
        } catch (SQLException e) {
            System.err.println("Ошибка при проверке существования привычки.");
            return false;
        }
    }

}
