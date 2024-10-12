package ru.kinzorc.habittracker.service;

import ru.kinzorc.habittracker.core.model.User;

import java.util.HashMap;

public class UserManager {
    private HashMap<String, User> users = new HashMap<>();

    public void registerUser(String name, String email, String password) {
        if (users.containsKey(email) && users.get(email).getPassword().equals(password)) {
            System.out.printf("Пользователь с таким email - %s, уже существует", email);
            System.out.println();
            return;
        }

        users.put(email, new User(name, email, password, false, false, false));
        System.out.println("Вы успешно зарегистрировались!" + " Email: " + users.get(email).getEmail() + " Пароль: " + users.get(email).getPassword());
    }


    public User loginUser(String email, String password) {
        User authUser = users.get(email);

        if (authUser == null || !authUser.getPassword().equals(password)) {
            System.out.println("Неправильный email или пароль!");
            return null;
        }

        authUser.setLogin(true);
        System.out.println("Успешный вход! Привет " + authUser.getName() + "!");
        return authUser;
    }

    public HashMap<String, User> getUsers() {
        return this.users;
    }

}
