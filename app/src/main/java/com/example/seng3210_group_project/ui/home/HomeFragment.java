package com.example.seng3210_group_project.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.seng3210_group_project.Poll.Poll.Poll;
import com.example.seng3210_group_project.Poll.Poll.Question;
import com.example.seng3210_group_project.R;
import com.example.seng3210_group_project.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Poll poll;
    Question question;

    int pollId = 0;

    Boolean isCreationMode = false;

    Button buttonCreatePoll;
    Button buttonCancel;

    EditText pollName;
    EditText pollDesc;
    EditText questionDesc;

    //View choices;
    List<EditText> choiceList = new ArrayList<>();




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onStart(){
        super.onStart();
        buttonCreatePoll = (Button) getActivity().findViewById(R.id.buttonSubmit);
        buttonCancel = (Button) getActivity().findViewById(R.id.buttonCancel);
        pollName = (EditText) getActivity().findViewById(R.id.editTexPollName);
        pollDesc = getActivity().findViewById(R.id.editPollDesc);
        questionDesc = getActivity().findViewById(R.id.editTextQuestion);
        //choices = getActivity().findViewById(R.id.choiceList);

        //Comment by Keishi
        //I wanted to make it loop to seach in Liner View but we do not have time for that unfortunatelly.
        EditText choice1 = getActivity().findViewById(R.id.editTextChoise1);
        EditText choice2 = getActivity().findViewById(R.id.editTextChoice2);
        EditText choice3 = getActivity().findViewById(R.id.editTextChoice3);
        EditText choice4 = getActivity().findViewById(R.id.editTextChoice4);

        //I hate this code but time is money now.
        choiceList.add(choice1);
        choiceList.add(choice2);
        choiceList.add(choice3);
        choiceList.add(choice4);

        //Initialize
        buttonCancel.setEnabled(false);

        buttonCreatePoll.setOnClickListener(new View.OnClickListener() {

            //I feel this questionCOunter should be somewhere else but I could not find better way
            int questionCounter = 0;
            @Override
            public void onClick(View v) {

                if(isPollFilled() && poll == null){
                    poll = new Poll(pollId, pollName.getText().toString(), pollDesc.getText().toString());
                    questionCounter = addQuestion(questionCounter);
                    setIsCreationMode(false);
                }
                else if (poll != null) {
                    //if User Pushed Next but did not want to add new Question
                    setIsCreationMode(false);
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Oops")
                            .setMessage("Please Fill Content")
                            .setPositiveButton("OK",null)
                            .show();
                }

            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsCreationMode(true);//Creation Mode Disactive. Poll would cleaout
            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //User FUnctions

    //Check the mode of the function
    void setIsCreationMode(boolean bool){
        isCreationMode = bool;
        poll.clear();//Clear out List
    }

    //Check if user input data or return false
    private Boolean isPollFilled(){
        int i = 0;

        if(pollName.getText().toString().length() != 0 &&
        pollDesc.getText().toString().length() != 0 &&
        questionDesc.getText().toString().length() != 0){
            for (EditText text:
                 choiceList) {
                if(text.getText().toString().length() != 0){
                    return true;
                }
            }
        }
        return false;
    }

    //Brilliant function that add question to poll + add question counter
    int addQuestion(int questionCounter){

        question = new Question(questionCounter, questionDesc.getText().toString());
        //find out
        for (EditText choice: choiceList) {
            if(choice.getText().toString().length() != 0){
                question.addChoices(choice.getText().toString());
            }
        }
        poll.addQuestion(question);
        return questionCounter++;
    }


}