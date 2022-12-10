package com.example.pollingsystem.data.model;

import androidx.annotation.Nullable;

public class Role extends BaseModel{
    private String name;

    public Role(@Nullable Integer id, String name){
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
