package com.example.pollingsystem.data.model;

import java.util.List;

public class Role {
    private int id;
    private String roleName;
    private List<User> users;

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<User> getUsers() {
        return users;
    }
}
