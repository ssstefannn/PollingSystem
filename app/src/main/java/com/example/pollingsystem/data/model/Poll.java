package com.example.pollingsystem.data.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Poll extends BaseModel {
    private UUID createdByUserId;
    private String name;
    private Date startDate;
    private int durationInMinutes;
    private List<Question> questions;

    public Poll(UUID createdByUserId, String name, Date startDate, int durationInMinutes){
        super();
        this.createdByUserId = createdByUserId;
        this.name = name;
        this.startDate = startDate;
        this.durationInMinutes = durationInMinutes;
    }

    public Poll(UUID id, UUID createdByUserId, String name, Date startDate, int durationInMinutes){
        super(id);
        this.createdByUserId = createdByUserId;
        this.name = name;
        this.startDate = startDate;
        this.durationInMinutes = durationInMinutes;
    }

    public String getName() {
        return name;
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

    public UUID getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(UUID createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
