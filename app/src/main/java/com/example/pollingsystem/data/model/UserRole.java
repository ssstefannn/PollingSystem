package com.example.pollingsystem.data.model;

import androidx.annotation.Nullable;

public class UserRole extends BaseModel{
    private Integer userId;
    private Integer roleId;

    public UserRole(@Nullable Integer id, Integer userId, Integer roleId){
        super(id);
        this.userId = userId;
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
