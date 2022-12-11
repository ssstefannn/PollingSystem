package com.example.pollingsystem.data.model;

import androidx.annotation.Nullable;

import java.util.UUID;

public class Role extends BaseModel{
    private String name;

    public Role(String name){
        super();
        this.name = name;
    }

    public Role(UUID id,String name){
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
