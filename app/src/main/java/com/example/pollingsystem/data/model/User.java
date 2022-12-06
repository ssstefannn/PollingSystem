package com.example.pollingsystem.data.model;

import java.util.List;

public class User {
    private int id;
    private String username;
    private String password;
    private List<Role> roles;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
