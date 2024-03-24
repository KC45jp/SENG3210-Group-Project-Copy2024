package com.example.seng3210_group_project.Poll.Poll;

import java.util.ArrayList;

public class Poll {

    private int poll_id;

    private String pollName;
    private String description;
    private Question question;

    //For Future use with user authentication
    //private String manager;

    ArrayList<Question> questionList;

    //Constructor
    public Poll(int poll_id, String pollName, String description){
        this.poll_id = poll_id;
        this.description = description;
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

    //public String getManager(){return  manager;}


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

    public void clear(){
        questionList.clear();
    }

    //Return String
    @Override
    public String toString(){
        return description;
    }


}
