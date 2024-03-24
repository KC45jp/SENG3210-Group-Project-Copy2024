package com.example.seng3210_group_project.ui.home;


import static java.lang.Integer.max;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.seng3210_group_project.Poll.Poll.Poll;
import com.example.seng3210_group_project.Poll.Poll.Question;
import com.example.seng3210_group_project.R;
import com.example.seng3210_group_project.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Poll poll;
    Question question;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReference;


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

        firebaseDatabase = FirebaseDatabase.getInstance("https://seng3210-group-project-default-rtdb.firebaseio.com/");
        dbReference = firebaseDatabase.getReference("Polls");



        buttonCreatePoll = (Button) getActivity().findViewById(R.id.buttonSubmitCreate);
        buttonCancel = (Button) getActivity().findViewById(R.id.buttonCancelCreate);
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

        //DatabaseCheck, Renew Poll id;
        dbReference.addValueEventListener(new ValueEventListener() {
            Poll dbReferencePoll;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int dbPollIDMAX = 0;
                for (DataSnapshot dataSnapshot:
                        snapshot.getChildren()) {
                    if(dataSnapshot.child("pollId").exists()){
                        dbPollIDMAX = max(dbPollIDMAX, Integer.valueOf(dataSnapshot.child("pollId").getValue().toString()));
                    }


                }
                pollId = dbPollIDMAX+1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Connection Error")
                        .setMessage("DB not Found")
                        .setPositiveButton("OK",null)
                        .show();
            }
        });

        buttonCreatePoll.setOnClickListener(new View.OnClickListener() {

            //I feel this questionCOunter should be somewhere else but I could not find better way
            int questionCounter = 0;
            @Override
            public void onClick(View v) {


                if(isPollFilled() && poll == null){

                    poll = new Poll(pollId, pollName.getText().toString(), pollDesc.getText().toString());
                    questionCounter = addQuestion(questionCounter);
                    String pollId = "pollId" + questionCounter;

                    dbReference.child(pollId).setValue(poll.getPollId());
                    dbReference.child(pollId).child("pollName").setValue(poll.getPollName());
                    dbReference.child(pollId).child("pollDesc").setValue(poll.getDescription());

                   int questionCounter = 0;
                    for (Question question:
                         poll.getQuestions()) {

                        dbReference.child(pollId).child("Questions").child("questionId"+questionCounter).setValue(question.getQuestionId());
                        dbReference.child(pollId).child("Questions").child("questionId" + questionCounter).child("questionDesc").setValue(question.getDescription());
                        int choiceCounter = 0;

                        for (String choice:
                             question.getChoices()) {
                            dbReference.child(pollId).child("Questions").child("questionId"+questionCounter).child("choices").child(String.valueOf(choiceCounter)).setValue(choice);
                            choiceCounter++;
                        }
                        questionCounter++;

                    }
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
        poll = null;//Clear out List
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
        return questionCounter+1;
    }


}