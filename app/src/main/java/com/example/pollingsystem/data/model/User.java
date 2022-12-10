package com.example.pollingsystem.data.model;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public class User extends BaseModel{
    private String username;
    private String password;
    private List<Role> roles;

    public User(@Nullable Integer id, String username, String password){
        super(id);
        this.username = username;
        this.password = password;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }
}
