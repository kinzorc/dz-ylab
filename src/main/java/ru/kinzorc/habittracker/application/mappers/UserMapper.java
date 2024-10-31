package ru.kinzorc.habittracker.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.kinzorc.habittracker.application.dto.UserDTO;
import ru.kinzorc.habittracker.core.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Интерфейс {@code UserMapper} предоставляет методы для преобразования между объектами {@link User} и {@link UserDTO}.
 * <p>
 * Этот маппер также включает метод для создания объекта {@link UserDTO} из данных {@link ResultSet}.
 * </p>
 */
@Mapper
public interface UserMapper {

    /**
     * Экземпляр маппера для вызова методов преобразования
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Преобразует объект {@link User} в {@link UserDTO}.
     *
     * @param user объект {@link User} для преобразования
     * @return объект {@link UserDTO}
     */
    UserDTO toDTO(User user);

    /**
     * Преобразует объект {@link UserDTO} в {@link User}.
     *
     * @param dto объект {@link UserDTO} для преобразования
     * @return объект {@link User}
     */
    User toEntity(UserDTO dto);

    /**
     * Создает объект {@link UserDTO} из {@link ResultSet}.
     *
     * @param resultSet объект {@link ResultSet} с данными из базы данных
     * @return объект {@link UserDTO}
     * @throws SQLException если возникла ошибка при чтении данных из {@link ResultSet}
     */
    @Named("fromResultSetToDTO")
    default UserDTO fromResultSetToDTO(ResultSet resultSet) throws SQLException {
        UserDTO dto = new UserDTO();

        dto.id = resultSet.getLong("id");
        dto.username = resultSet.getString("username");
        dto.password = resultSet.getString("password");
        dto.email = resultSet.getString("email");
        dto.userRole = resultSet.getString("role").toUpperCase();
        dto.userStatusAccount = resultSet.getString("status").toUpperCase();

        return dto;
    }
}