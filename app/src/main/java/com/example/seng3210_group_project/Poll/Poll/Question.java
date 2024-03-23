package com.example.seng3210_group_project.Poll.Poll;

public class Question {

    private int questionId;
    private int questionIdCounter = 0;

    private String description;

    private String[] choices;

    Question(String description, String[] Choice){
        questionId = questionIdCounter;
        this.description = description;
        this.choices = choices;
        questionIdCounter++;
    }

    public int getQuestionId(){
        return questionId;
    }
    public String getDescription(){
        return description;
    }

    public  String[] getChoices(){
        return choices;
    }

    public int choiceLength(){
        return choices.length;
    }


}
