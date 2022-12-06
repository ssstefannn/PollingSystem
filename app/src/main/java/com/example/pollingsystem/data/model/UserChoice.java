package com.example.pollingsystem.data.model;

import android.location.Location;

import java.util.Date;

public class UserChoice {
    private int id;
    private User user;
    private Choice choice;
    private Date submissionDate;
    private Location submissionLocation;

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

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Location getSubmissionLocation() {
        return submissionLocation;
    }

    public void setSubmissionLocation(Location submissionLocation) {
        this.submissionLocation = submissionLocation;
    }
}
