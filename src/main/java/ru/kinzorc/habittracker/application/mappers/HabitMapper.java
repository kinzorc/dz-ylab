package ru.kinzorc.habittracker.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.kinzorc.habittracker.application.dto.HabitDTO;
import ru.kinzorc.habittracker.core.entities.Habit;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Интерфейс {@code HabitMapper} предоставляет методы для преобразования между объектами {@link Habit} и {@link HabitDTO}.
 * <p>
 * Этот маппер включает метод для создания объекта {@link HabitDTO} из данных {@link ResultSet}.
 * </p>
 */
@Mapper
public interface HabitMapper {

    /**
     * Экземпляр маппера для вызова методов преобразования
     */
    HabitMapper INSTANCE = Mappers.getMapper(HabitMapper.class);

    /**
     * Преобразует объект {@link Habit} в {@link HabitDTO}.
     *
     * @param habit объект {@link Habit} для преобразования
     * @return объект {@link HabitDTO}
     */
    HabitDTO toDTO(Habit habit);

    /**
     * Преобразует объект {@link HabitDTO} в {@link Habit}.
     *
     * @param dto объект {@link HabitDTO} для преобразования
     * @return объект {@link Habit}
     */
    Habit toEntity(HabitDTO dto);

    /**
     * Создает объект {@link HabitDTO} из {@link ResultSet}.
     * <p>
     * Этот метод используется для извлечения данных привычки из {@link ResultSet},
     * полученного при выполнении SQL-запроса к базе данных.
     * </p>
     *
     * @param resultSet объект {@link ResultSet}, содержащий данные из базы данных
     * @return объект {@link HabitDTO}, содержащий данные привычки
     * @throws SQLException если возникает ошибка при извлечении данных из {@link ResultSet}
     */
    @Named("fromResultSetToDTO")
    default HabitDTO fromResultSetToDTO(ResultSet resultSet) throws SQLException {
        HabitDTO dto = new HabitDTO();

        dto.id = resultSet.getLong("id");
        dto.userId = resultSet.getLong("user_id");
        dto.name = resultSet.getString("habit_name");
        dto.description = resultSet.getString("description");
        dto.frequency = resultSet.getString("frequency").toUpperCase();
        dto.createdDate = resultSet.getDate("created_date").toLocalDate();
        dto.startDate = resultSet.getDate("start_date").toLocalDate();
        dto.endDate = resultSet.getDate("end_date").toLocalDate();
        dto.executionPeriod = resultSet.getString("execution_period").toUpperCase();
        dto.status = resultSet.getString("status").toUpperCase();
        dto.streak = resultSet.getInt("streak");
        dto.executionPercentage = resultSet.getInt("execution_percentage");

        return dto;
    }

}