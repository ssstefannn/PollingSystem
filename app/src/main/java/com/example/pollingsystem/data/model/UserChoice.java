package com.example.pollingsystem.data.model;

import android.location.Location;

import androidx.annotation.Nullable;

import java.util.Date;

public class UserChoice extends BaseModel{
    private Integer userId;
    private Integer choiceId;
    private Date submittedOn;
    private Location submittedIn;

    public UserChoice(@Nullable Integer id, Integer userId, Integer choiceId, Date submittedOn, Location submittedIn){
        super(id);
        this.userId = userId;
        this.choiceId = choiceId;
        this.submittedOn = submittedOn;
        this.submittedIn = submittedIn;
    }

    public Date getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(Date submittedOn) {
        this.submittedOn = submittedOn;
    }

    public Location getSubmittedIn() {
        return submittedIn;
    }

    public void setSubmittedIn(Location submittedIn) {
        this.submittedIn = submittedIn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Integer choiceId) {
        this.choiceId = choiceId;
    }
}
