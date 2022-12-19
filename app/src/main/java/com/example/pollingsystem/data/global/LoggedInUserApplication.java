package com.example.pollingsystem.data.global;

import android.app.Application;

import java.util.UUID;

public class LoggedInUserApplication extends Application {
    private String userName;
    private UUID userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}