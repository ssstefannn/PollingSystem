package com.example.pollingsystem.data.model;

import java.util.List;
import java.util.UUID;

public class Question extends BaseModel{
    private UUID pollId;
    private String name;
    private List<Choice> choices;

    public Question(UUID pollId, String name){
        super();
        this.pollId = pollId;
        this.name = name;
    }

    public Question(UUID id, UUID pollId, String name){
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

    public UUID getPollId() {
        return pollId;
    }

    public void setPollId(UUID pollId) {
        this.pollId = pollId;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
