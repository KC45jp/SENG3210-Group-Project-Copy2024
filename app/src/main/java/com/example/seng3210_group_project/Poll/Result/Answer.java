package com.example.seng3210_group_project.Poll.Result;

import android.util.Log;

import com.example.seng3210_group_project.Poll.Poll.Poll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Answer {

    private int questionId;
    private int pollId;
    private String answerKey;
    private HashMap<Integer, String> answerHash;

    Answer(){
        answerHash = new HashMap<>();
    }

    Answer(int pollId){
        this.pollId = pollId;
        answerHash = new HashMap<>();
    }

    void addAnswer(int questionId, String answerKey){

        answerHash.put(questionId, answerKey);
    }

    String getAnswerValue(int questionId){
        if(answerHash.containsKey(questionId)){
            return answerHash.get(questionId);
        }
        else{
            //Error Handling
            Log.w("Answer Class", "QuestionId not found");
            return "";
        }
    }

}
