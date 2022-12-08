package com.example.pollingsystem.data.model;

public class Choice {
    private int id;
    private Question question;
    private String name;

    public Choice(Question question, String name){
        this.question = question;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
