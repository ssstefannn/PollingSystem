package com.example.pollingsystem.data.model;

import java.util.Date;

public class Poll {
    private int id;
    private User createdBy;
    private String name;
    private Date startDate;
    private int durationInMinutes;

    public Poll(User createdBy, String name, Date startDate, int durationInMinutes){
        this.createdBy = createdBy;
        this.name = name;
        this.startDate = startDate;
        this.durationInMinutes = durationInMinutes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }


}
