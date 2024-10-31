package ru.kinzorc.habittracker.application.services;

import ru.kinzorc.habittracker.application.dto.HabitDTO;
import ru.kinzorc.habittracker.application.exceptions.HabitAlreadyExistsException;
import ru.kinzorc.habittracker.application.exceptions.HabitNotFoundException;
import ru.kinzorc.habittracker.application.mappers.HabitMapper;
import ru.kinzorc.habittracker.core.entities.Habit;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.repository.HabitRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс {@code HabitService} представляет сервисный слой для управления привычками пользователей.
 * <p>
 * Этот сервис предоставляет функционал для создания, обновления, удаления, отметок выполнения и сброса статистики
 * привычек. Также он поддерживает вычисление стриков и процента выполнения привычек за определённые периоды.
 * </p>
 */
public class HabitService {

    private final HabitRepository habitRepository;

    /**
     * Конструктор для создания экземпляра {@code HabitService} с указанным репозиторием.
     *
     * @param habitRepository репозиторий для выполнения операций с привычками
     */
    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    /**
     * Создает новую привычку для пользователя.
     *
     * @param habitDTO DTO привычки
     * @throws HabitAlreadyExistsException если привычка с таким именем уже существует
     */
    public void createHabit(HabitDTO habitDTO) throws HabitAlreadyExistsException {
        try {
            if (habitRepository.findByName(HabitMapper.INSTANCE.toEntity(habitDTO).getName()).isPresent()) {
                throw new HabitAlreadyExistsException("Привычка с таким именем уже существует");
            }
            Habit habit = HabitMapper.INSTANCE.toEntity(habitDTO);
            habitRepository.save(habit);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании привычки: " + e.getMessage(), e);
        }
    }

    /**
     * Обновляет существующую привычку.
     *
     * @param habitDTO обновленная DTO привычки
     * @throws HabitNotFoundException если привычка не найдена
     */
    public void updateHabit(HabitDTO habitDTO) throws HabitNotFoundException {
        try {
            Optional<Habit> existingHabit = habitRepository.findById(habitDTO.id);
            if (existingHabit.isEmpty()) {
                throw new HabitNotFoundException("Привычка не найдена");
            }
            habitRepository.update(HabitMapper.INSTANCE.toEntity(habitDTO));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении привычки: " + e.getMessage(), e);
        }
    }

    /**
     * Удаляет привычку по идентификатору.
     *
     * @param habitId идентификатор привычки
     * @throws HabitNotFoundException если привычка не найдена
     */
    public void deleteHabit(Long habitId) throws HabitNotFoundException {
        try {
            Optional<Habit> habit = habitRepository.findById(habitId);
            if (habit.isEmpty()) {
                throw new HabitNotFoundException("Привычка не найдена");
            }
            habitRepository.deleteById(habitId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении привычки: " + e.getMessage(), e);
        }
    }

    /**
     * Возвращает список всех привычек.
     *
     * @return список привычек
     */
    public List<HabitDTO> getAllHabits() {
        try {
            return habitRepository.findAll().stream()
                    .map(HabitMapper.INSTANCE::toDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка привычек: " + e.getMessage(), e);
        }
    }

    /**
     * Отмечает выполнение привычки на указанную дату.
     *
     * @param habitId       идентификатор привычки
     * @param executionDate дата выполнения
     * @throws HabitNotFoundException если привычка не найдена
     */
    public void markHabitExecution(Long habitId, LocalDate executionDate) throws HabitNotFoundException {
        try {
            Habit habit = habitRepository.findById(habitId)
                    .orElseThrow(() -> new HabitNotFoundException("Привычка не найдена"));
            habitRepository.markExecution(habit, executionDate);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при отметке выполнения привычки: " + e.getMessage(), e);
        }
    }

    /**
     * Получает список выполнений привычки.
     *
     * @param habitId идентификатор привычки
     * @return список выполнений привычки
     * @throws HabitNotFoundException если привычка не найдена
     */
    public List<LocalDate> getHabitExecutions(Long habitId) throws HabitNotFoundException {
        try {
            if (habitRepository.findById(habitId).isEmpty()) {
                throw new HabitNotFoundException("Привычка не найдена");
            }
            return habitRepository.getExecutions(habitId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении выполнений привычки: " + e.getMessage(), e);
        }
    }

    /**
     * Возвращает список всех привычек указанного пользователя.
     *
     * @param user пользователь, чьи привычки нужно получить
     * @return список привычек пользователя
     */
    public List<HabitDTO> getHabitsByUser(User user) {
        try {
            return habitRepository.findHabitsByUser(user).stream()
                    .map(HabitMapper.INSTANCE::toDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении привычек пользователя: " + e.getMessage(), e);
        }
    }

    /**
     * Сбрасывает выполнение и стрик привычки.
     *
     * @param habitId         идентификатор привычки
     * @param resetExecutions сброс выполнений
     * @param resetStreaks    сброс стрика
     * @throws HabitNotFoundException если привычка не найдена
     */
    public void resetHabitStatistics(Long habitId, boolean resetExecutions, boolean resetStreaks) throws HabitNotFoundException {
        try {
            if (habitRepository.findById(habitId).isEmpty()) {
                throw new HabitNotFoundException("Привычка не найдена");
            }
            habitRepository.resetStatistics(habitId, resetExecutions, resetStreaks);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сбросе статистики привычки: " + e.getMessage(), e);
        }
    }

    /**
     * Возвращает статистику выполнения привычки за указанный период.
     *
     * @param habitId         идентификатор привычки
     * @param startPeriodDate начало периода
     * @param endPeriodDate   конец периода
     * @return статистика выполнения за период
     * @throws HabitNotFoundException если привычка не найдена
     */
    public Map<LocalDate, Integer> getHabitStatistics(Long habitId, LocalDate startPeriodDate, LocalDate endPeriodDate) throws HabitNotFoundException {
        try {
            Habit habit = habitRepository.findById(habitId)
                    .orElseThrow(() -> new HabitNotFoundException("Привычка не найдена"));
            return habitRepository.getStatisticByPeriod(habit, startPeriodDate, endPeriodDate);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении статистики привычки: " + e.getMessage(), e);
        }
    }

    /**
     * Подсчитывает процент выполнения привычки за её период выполнения.
     *
     * @param habitId идентификатор привычки
     * @return процент выполнения
     * @throws HabitNotFoundException если привычка не найдена
     */
    public int calculateHabitExecutionPercentage(Long habitId) throws HabitNotFoundException {
        try {
            Habit habit = habitRepository.findById(habitId)
                    .orElseThrow(() -> new HabitNotFoundException("Привычка не найдена"));
            return habitRepository.calculateExecutionPercentageByPeriod(habit, habit.getStartDate(), habit.getEndDate());
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при расчете процента выполнения привычки: " + e.getMessage(), e);
        }
    }

    /**
     * Обновляет текущий стрик привычки.
     *
     * @param habitId          идентификатор привычки
     * @param newExecutionDate дата нового выполнения
     * @return обновленное значение стрика
     * @throws HabitNotFoundException если привычка не найдена
     */
    public int updateHabitStreak(Long habitId, LocalDate newExecutionDate) throws HabitNotFoundException {
        try {
            Habit habit = habitRepository.findById(habitId)
                    .orElseThrow(() -> new HabitNotFoundException("Привычка не найдена"));
            return habitRepository.updateStreak(habit, newExecutionDate);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении стрика привычки: " + e.getMessage(), e);
        }
    }
}