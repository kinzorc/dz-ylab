package ru.kinzorc.habittracker.model;

public class User {
    private String name;
    private String password;
    private String email;
    private boolean isAdmin;
    private boolean isLogin;
    private boolean isBlocked;

    public User(String name, String email, String password, boolean isAdmin, boolean isLogin, boolean isBlocked) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isLogin = isLogin;
        this.isBlocked = isBlocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
