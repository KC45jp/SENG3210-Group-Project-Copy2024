package com.example.seng3210_group_project;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.seng3210_group_project.Poll.Poll.Poll;
import com.example.seng3210_group_project.Poll.Poll.Question;

import java.util.ArrayList;
import java.util.List;


public class UnitTestPollClass {

    Question question1 = new Question(1, "Q1");
    Question question2 = new Question(2, "Q2");


    @Test
    public void testPoll_pollID(){
        int pollId = 1;
        String pollName = "Poll Name";
        String pollDesc = "Poll Description";

        Poll poll = new Poll(pollId,pollName, pollDesc);

        assertEquals(pollId, poll.getPollId());

    }

    @Test
    public void testPoll_pollName(){
        int pollId = 1;
        String pollName = "Poll Name";
        String pollDesc = "Poll Description";

        Poll poll = new Poll(pollId,pollName, pollDesc);

        assertEquals(pollName, poll.getPollName());
    }

    @Test
    public void testPoll_pollDesc(){
        int pollId = 1;
        String pollName = "Poll Name";
        String pollDesc = "Poll Description";

        Poll poll = new Poll(pollId,pollName, pollDesc);

        assertEquals(pollDesc, poll.getDescription());
    }

    //check it makes empty Question List
    @Test
    public void testPoll_QuestionListCreation(){
        int pollId = 1;
        String pollName = "Poll Name";
        String pollDesc = "Poll Description";

        Poll poll = new Poll(pollId,pollName, pollDesc);

        assertTrue(poll.getQuestions().isEmpty());
    }

    //check it makes empty Question List
    @Test
    public void testPoll_QuestionListCreation_NoParameterConstructor(){
        int pollId = 1;
        String pollName = "Poll Name";
        String pollDesc = "Poll Description";

        Poll poll = new Poll();

        assertTrue(poll.getQuestions().isEmpty());
    }

    @Test
    public void testPollAddQuestion_questionLengthTest(){
        int pollId = 1;
        String pollName = "Poll Name";
        String pollDesc = "Poll Description";
        Poll poll = new Poll(pollId,pollName, pollDesc);
        poll.addQuestion(question1);
        poll.addQuestion(question2);
        assertEquals(2, poll.questionLength());
    }

    @Test
    public void testPollAddQuestion_getQuestionsTest(){
        int pollId = 1;
        String pollName = "Poll Name";
        String pollDesc = "Poll Description";

        Poll poll = new Poll(pollId,pollName, pollDesc);
        poll.addQuestion(question1);
        poll.addQuestion(question2);

        //create same List
        List<Question> questionList = new ArrayList<>();
        questionList.add(question1);
        questionList.add(question2);

        assertArrayEquals(new List[]{questionList}, new List[]{poll.getQuestions()});
    }

    @Test
    public void testPollAddQuestion_clearQuestionsTest(){
        int pollId = 1;
        String pollName = "Poll Name";
        String pollDesc = "Poll Description";

        Poll poll = new Poll(pollId,pollName, pollDesc);
        poll.addQuestion(question1);
        poll.addQuestion(question2);


        //catch error if it is not propery added
        assertEquals(2, poll.questionLength());
        //clear it
        poll.clearQuestion();

        //then empty
        assertTrue(poll.getQuestions().isEmpty());
    }

    @Test
    public void testToString(){
        int pollId = 1;
        String pollName = "Poll Name";
        String pollDesc = "Poll Description";

        Poll poll = new Poll(pollId,pollName, pollDesc);

        assertEquals(pollDesc, poll.toString());
    }




}
