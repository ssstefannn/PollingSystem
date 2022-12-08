package com.example.pollingsystem.data.model;

import android.location.Location;

import java.util.Date;

public class UserChoice {
    private int id;
    private User user;
    private Choice choice;
    private Date submittedOn;
    private Location submittedIn;

    public UserChoice(User user, Choice choice, Date submittedOn, Location submittedIn){
        this.user = user;
        this.choice = choice;
        this.submittedOn = submittedOn;
        this.submittedIn = submittedIn;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
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
}
