package com.example.seng3210_group_project.Poll.Poll;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private int questionId;

    private String description;

    private List<String> choices;

    public Question(){
        choices = new ArrayList<>();

    }
    public Question(int questionId, String description){
        this.questionId = questionId;
        this.description = description;
        choices = new ArrayList<>();
    }

    public int getQuestionId(){
        return questionId;
    }
    public String getDescription(){
        return description;
    }

    public void addChoices(String choiceDesc){
        choices.add(choiceDesc);
    }

    public  List<String> getChoices(){
        return choices;
    }


    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int choiceLength(){
        return choices.size();
    }


}
