package com.example.pollingsystem.data.model;

import java.util.List;
import java.util.UUID;

public class Choice extends BaseModel {
    private UUID questionId;
    private String name;
    private List<UserChoice> userChoices;

    public Choice(UUID questionId, String name){
        super();
        this.questionId = questionId;
        this.name = name;
    }

    public Choice(UUID id, UUID questionId, String name){
        super(id);
        this.questionId = questionId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public List<UserChoice> getUserChoices() {
        return userChoices;
    }

    public void setUserChoices(List<UserChoice> userChoices) {
        this.userChoices = userChoices;
    }
}
