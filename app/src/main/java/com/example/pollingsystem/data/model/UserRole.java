package com.example.pollingsystem.data.model;

import androidx.annotation.Nullable;

import java.util.UUID;

public class UserRole extends BaseModel{
    private UUID userId;
    private UUID roleId;

    public UserRole(UUID userId, UUID roleId){
        super();
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserRole(@Nullable UUID id, UUID userId, UUID roleId){
        super(id);
        this.userId = userId;
        this.roleId = roleId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
}
