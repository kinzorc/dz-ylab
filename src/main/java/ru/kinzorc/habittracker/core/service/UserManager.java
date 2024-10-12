package ru.kinzorc.habittracker.core.service;

import ru.kinzorc.habittracker.core.model.User;


import java.util.HashMap;


public class UserManager {
    private static final HashMap<String, User> users = new HashMap<>();
    private static UserManager INSTANCE;


    private UserManager() {}

    // Паттерн Singleton - Ленивая инициализация
    public static UserManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserManager();
        }

        return INSTANCE;
    }

    // Метод для регистрации нового пользователя  с добавлению в мапу users
    public boolean registerUser(String name, String email, String password) {
        if (users.containsKey(email) && users.get(email).getPassword().equals(password)) {
            return false;
        }

        users.put(email, new User(name, email, password, false, false, false));
        return true;
    }

    // Метод для авторизации пользователя с проверкой
    public User loginUser(String email, String password) {
        User user = users.get(email);

        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }

        user.setLogin(true);
        return user;
    }

    // Метод для удаления пользователя с проверкой пароля
    public boolean deleteUser(User currentUser, String password) {
        if (currentUser == null || !currentUser.getPassword().equals(password)) {
            return false;
        }

        users.remove(currentUser.getEmail());
        return true;
    }


    // Метод для получения пользователей
    public HashMap<String, User> getUsers() {
        return users;
    }

    // Метод для получения пользователя по email
    public User getUser(String email) {
        return getUsers().get(email);
    }

}
