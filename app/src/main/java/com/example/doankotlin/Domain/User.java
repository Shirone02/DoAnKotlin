package com.example.doankotlin.Domain;

import androidx.annotation.NonNull;

public class User {
    private String Email;
    private String Password;
    private String isUser;
    private String isAdmin;

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "email='" + Email + '\'' +
                ", password='" + Password + '\'' +
                ", isUser='" + isUser + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                '}';
    }

    public User(String email, String password, String isUser, String isAdmin) {
        this.Email = email;
        this.Password = password;
        this.isUser = isUser;
        this.isAdmin = isAdmin;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public User() {}

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }
}
