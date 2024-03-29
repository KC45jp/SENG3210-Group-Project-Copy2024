package com.example.seng3210_group_project.Poll.Poll;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;



public class Poll {

    private int pollId;

    private String pollName;
    private String description;

    private String password;
    private Question question;


    //For Future use with user authentication
    //private String manager;

    ArrayList<Question> questionList;


    public Poll(){
        questionList = new ArrayList<>();
    }
    //Constructor
    public Poll(int pollId, String pollName, String description){
        this.pollId = pollId;
        this.pollName = pollName;
        this.description = description;
        questionList = new ArrayList<>();
    }


    public void setPoll_id(int pollId) {
        this.pollId = pollId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPollName(String pollName){
        this.pollName = pollName;
    }


    public int getPollId() {
        return pollId;
    }

    public String getDescription() {
        return description;
    }

    public String getPollName(){ return pollName;};

    //public String getManager(){return  manager;}


    public void setPassword(String password) {

        //Seriously no Hash!? Yeah I know... it is better but sometime I cannot
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    //Add question to the poll
    public void addQuestion(Question question){
        questionList.add(question);
    }

    public Question getQuestion(int questionID) {
        for (Question question:
             questionList) {
            if(question.getQuestionId() == questionID){
                return question;
            }
        }
        Log.d("Question Class", "ERROR cannot find message");
        return null;
    }


    public List<Question> getQuestions(){
        return questionList;
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

    public String idToString(){
        return String.valueOf(pollId);
    }


}
