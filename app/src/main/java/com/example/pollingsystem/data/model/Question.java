package com.example.pollingsystem.data.model;

import androidx.annotation.Nullable;

import java.util.List;

public class Question extends BaseModel{
    private Integer pollId;
    private String name;
    private List<Choice> choices;

    public Question(@Nullable Integer id, Integer pollId, String name){
        super(id);
        this.pollId = pollId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
