package com.example.pollingsystem.data.model;

public class Question {
    private int id;
    private Poll poll;
    private String name;

    public int getId() {
        return id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
