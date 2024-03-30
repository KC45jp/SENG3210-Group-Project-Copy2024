package com.example.seng3210_group_project;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.seng3210_group_project.Poll.Poll.Question;

import java.util.ArrayList;
import java.util.List;

public class UnitTestQuestionClass {
    @Test
    public void testQuestionId(){
        Question question1;
        question1 = new Question(1, "Quesion1");
        assertEquals(1, question1.getQuestionId());
    }

    @Test
    public void testQuestionDesc(){
        Question question1;
        question1 = new Question(1, "Question1");
        String s = question1.getDescription();
        assertEquals("Question1", s);
    }

    @Test
    public void testChoiceLength(){
        Question question1;
        int choiceNumber = 2;
        question1 = new Question(1, "Question1");
        question1.addChoices("Cat");
        question1.addChoices("Dog");

        assertEquals(choiceNumber, question1.choiceLength());
    }

    @Test
    public void testChoice(){
        Question question1;
        List<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Cat");
        stringArrayList.add("Dog");
        stringArrayList.add("deer");
        
        question1 = new Question(25, "Question2");

        for (String str:
             stringArrayList) {
            question1.addChoices(str);
        }

        List<String> stringArrayListReturned = new ArrayList<>();
        stringArrayListReturned = question1.getChoices();

        assertArrayEquals(new List[]{stringArrayList}, new List[]{stringArrayListReturned});
    }


    @Test
    public void testChoiceListConstructorWithParameter(){
        Question question1;
        question1 = new Question(1, "3345");

        List<String> strList = question1.getChoices();

        assertTrue(strList.isEmpty());
    }

    @Test
    public void testChoiceListConstructorWithoutParameter(){
        //Test String List choices are made or not withoutparameter
        Question question1 = new Question();

        List<String> strList = question1.getChoices();

        assertTrue(strList.isEmpty());
    }


}
