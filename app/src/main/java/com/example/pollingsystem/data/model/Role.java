package com.example.pollingsystem.data.model;

import java.util.List;

public class Role {
    public int id;
    public String name;

    public Role(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
