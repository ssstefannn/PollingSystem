package com.example.pollingsystem.data.model;

import java.util.UUID;

public class BaseModel {
    private UUID id;

    public BaseModel(){
        id = UUID.randomUUID();
    }

    public BaseModel(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
