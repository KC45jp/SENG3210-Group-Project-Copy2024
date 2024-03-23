package com.example.seng3210_group_project.Poll.Poll;

import java.util.ArrayList;

public class Poll {

    private int poll_id;
    private String description;
    private Question question;

    private String manager;

    ArrayList<Question> questionList;

    //Constructor
    Poll(int poll_id, String description, String manager){
        this.poll_id = poll_id;
        this.description = description;
        this.manager = manager;
        questionList = new ArrayList<>();
    }

    //getter and setter
    /*
    public void setPoll_id(int poll_id) {
        this.poll_id = poll_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
     */

    public int getPoll_id() {
        return poll_id;
    }

    public String getDescription() {
        return description;
    }

    public String getManager(){
        return  manager;
    }


    //Add question to the poll
    public void addQuestion(Question question){
        questionList.add(question);
    }

    public Question getQuestion() {
        return question;
    }

    public int questionLength(){
        return questionList.size();
    }



    //Return String
    @Override
    public String toString(){
        return description;
    }


}
