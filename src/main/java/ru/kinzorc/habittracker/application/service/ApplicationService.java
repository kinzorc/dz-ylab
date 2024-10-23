package ru.kinzorc.habittracker.application.service;

import ru.kinzorc.habittracker.application.dto.UserDTO;
import ru.kinzorc.habittracker.core.entities.User;
import ru.kinzorc.habittracker.core.enums.User.UserData;
import ru.kinzorc.habittracker.core.enums.User.UserRole;
import ru.kinzorc.habittracker.core.exceptions.UserAlreadyExistsException;
import ru.kinzorc.habittracker.core.exceptions.UserNotFoundException;
import ru.kinzorc.habittracker.core.repository.HabitRepository;
import ru.kinzorc.habittracker.core.repository.UserRepository;

import java.sql.SQLException;
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
    public void editUser(UserData userData, String newValue) {
        if (currentUser == null) {
            System.err.println("Вы не авторизованы!");
            return;
        }

        try {
            userRepository.editUser(currentUser, userData, newValue);
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
                case ID -> {
                    result = userDTO.map(dto -> dto.getId() == Long.parseLong(value)).orElse(false);
                }
                case USERNAME -> {
                    result = userDTO.map(dto -> dto.getUserName().equals(value)).orElse(false);
                }
                case EMAIL -> {
                    result = userDTO.map(dto -> dto.getEmail().equals(value)).orElse(false);
                }
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

}
