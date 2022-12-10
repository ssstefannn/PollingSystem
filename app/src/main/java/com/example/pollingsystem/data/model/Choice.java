package com.example.pollingsystem.data.model;

import androidx.annotation.Nullable;

public class Choice extends BaseModel {
    private Integer questionId;
    private String name;

    public Choice(@Nullable Integer id, Integer questionId, String name){
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

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
