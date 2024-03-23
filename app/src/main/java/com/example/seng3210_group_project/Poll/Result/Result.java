package com.example.seng3210_group_project.Poll.Result;

import com.example.seng3210_group_project.Poll.Poll.Poll;
import com.example.seng3210_group_project.Poll.Poll.Question;
import com.example.seng3210_group_project.Poll.Result.Answer;

import java.util.ArrayList;
import java.util.HashMap;

public class Result {

    private Poll poll;
    private Question question;
    private Answer answer;

    ArrayList<Answer> answerList;

    Result(Poll poll){
        this.poll = poll;
        answerList = new ArrayList<>();
    }


    public void addAnswer(Answer answer){
        answerList.add(answer);
    }

    public int getAnswerResult(int questionId, String choice){
        int sumAns = 0;



        for (Answer answer:answerList) {
            if(answer.getAnswerValue(questionId) == choice){
                sumAns++;
            }
        }
        return sumAns;
    }

    public HashMap<String, Integer> getQuestionResult(int questoionId){
        HashMap<String, Integer> questionResult = new HashMap<>();
        String[] choices = poll.getQuestion().getChoices();

        for (String choice: choices) {
            questionResult.put(choice, getAnswerResult(questoionId, choice));
        }
        return questionResult;
    }
}
