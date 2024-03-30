package com.example.seng3210_group_project.ui.home;


import static java.lang.Integer.max;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.seng3210_group_project.Poll.Poll.Poll;
import com.example.seng3210_group_project.Poll.Poll.Question;
import com.example.seng3210_group_project.R;
import com.example.seng3210_group_project.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


    //int pollId = 0;

    Boolean isCreationMode = false;

    Button buttonCreatePoll;
    Button buttonCancel;


    FloatingActionButton buttonAddNewChoice;

    EditText pollName;
    EditText pollDesc;
    EditText questionDesc;

    ScrollView MainScrollView;


    LinearLayout choiceListBox;

    TextView pollIdText;
    TextView pollAddedIdText;

    EditText passwordText;




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



        MainScrollView = getActivity().findViewById(R.id.MainScrollView);
        buttonCreatePoll = (Button) getActivity().findViewById(R.id.buttonSubmitCreate);
        buttonCancel = (Button) getActivity().findViewById(R.id.buttonCancelCreate);
        buttonAddNewChoice = getActivity().findViewById(R.id.buttonAddChoiceCreate);

        pollName = (EditText) getActivity().findViewById(R.id.editTexPollName);
        pollDesc = getActivity().findViewById(R.id.editPollDesc);
        questionDesc = getActivity().findViewById(R.id.editTextQuestion);
        passwordText = getActivity().findViewById(R.id.editPassword);
        //choices = getActivity().findViewById(R.id.choiceList);
        pollIdText = new TextView(getActivity());
        pollAddedIdText = new TextView(getActivity());


        //Comment by Keishi
        //I wanted to make it loop to seach in Liner View but we do not have time for that unfortunatelly.
        EditText choice1 = getActivity().findViewById(R.id.editTextChoise1);
        EditText choice2 = getActivity().findViewById(R.id.editTextChoice2);

        choiceListBox = getActivity().findViewById(R.id.boxChoiceListCreate);

        //Initialize

        //DatabaseCheck, Renew Poll id;
        dbReference.addValueEventListener(new ValueEventListener() {
            Poll dbReferencePoll;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int dbPollIDMAX = 0;
                for (DataSnapshot dataSnapshot:
                        snapshot.getChildren()) {
                    if(dataSnapshot.exists()){
                        String pollId = dataSnapshot.getKey().toString();
                        //Take only number = pollId integer
                        pollId = pollId.replaceAll("[^0-9]", "");
                        dbPollIDMAX = max(dbPollIDMAX, Integer.valueOf(pollId));
                    }
                }
                pollIdText.setText(String.valueOf(dbPollIDMAX+1));
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

                //User fill UI but poll is null means User create poll with only one Question
                if(isPollFilled() && poll == null){
                    int pollIdint = Integer.valueOf(pollIdText.getText().toString());
                    poll = new Poll(pollIdint, pollName.getText().toString(), pollDesc.getText().toString());
                    questionCounter = addQuestion(questionCounter);
                    String pollId = "pollId" + pollIdint;
                    pollAddedIdText.setText(String .valueOf(pollIdint));

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
                            //dbReference.child(pollId).child("Questions").child("questionId"+questionCounter).child("choices").child(String.valueOf(choiceCounter)).setValue(choice);
                            dbReference.child(pollId).child("Questions").child("questionId"+questionCounter).child("choices").child(choice).setValue(0);
                            choiceCounter++;
                        }
                        questionCounter++;

                    }

                    //if there is password set password
                    if(passwordText.getText().toString().length() != 0){
                        dbReference.child(pollId).child("pollPassword").setValue(passwordText.getText().toString());
                    }

                    setIsCreationMode(false);
                    clearUI();
                    showDialogPollId();
                }
                else if (isPollFilled()) {
                    //if the user has multiple Questions and filled UI
                    setIsCreationMode(false);
                    clearUI();
                } else if (poll != null) {
                    //if User Pushed Next but did not want to add new Question
                    setIsCreationMode(false);
                    clearUI();
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

        buttonAddNewChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new Text box for new choice
                choiceListBox.addView(new androidx.appcompat.widget.AppCompatEditText(getActivity()){{ setHint("New Choice"); }});
                //Scroll to down
                MainScrollView.fullScroll(View.FOCUS_DOWN);

            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsCreationMode(true);//Creation Mode Disactive. Poll would cleaout
                clearUI();
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

    void clearUI(){
        //When mode change then clear below
        if(true){
            pollName.setText("");
            pollDesc.setText("");
            questionDesc.setText("");
            pollIdText.setText("");
            choiceListBox.removeAllViews();
            passwordText.setText("");
            choiceListBox.addView(new androidx.appcompat.widget.AppCompatEditText(getActivity()){{ setHint("choice1"); }});
        }
        poll = null;//Clear out List
    }

    //Check if user input data or return false
    private Boolean isPollFilled(){
        //int i = 0;

        if(pollName.getText().toString().length() != 0 &&
        pollDesc.getText().toString().length() != 0 &&
        questionDesc.getText().toString().length() != 0){

            for(int i = 0; i <choiceListBox.getChildCount(); i++){
                View childView = choiceListBox.getChildAt(i);
                if(childView instanceof  EditText){
                    if(((EditText) childView).getText().toString().length() != 0){
                        return true;
                    }
                }
            }

        }
        return false;
    }

    //Brilliant function that add question to poll + add question counter
    int addQuestion(int questionCounter){

        question = new Question(questionCounter, questionDesc.getText().toString());
        //find out

        for(int i = 0; i <choiceListBox.getChildCount(); i++){
            View childView = choiceListBox.getChildAt(i);
            if(childView instanceof  EditText){
                if(((EditText) childView).getText().toString().length() != 0){
                    question.addChoices(((EditText) childView).getText().toString());
                }
            }
        }



        poll.addQuestion(question);
        return questionCounter+1;
    }


    void showDialogPollId(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())

                .setTitle("Create Polls ID is " + pollAddedIdText.getText().toString())
                .setPositiveButton("OK", null)
                .setNeutralButton("Copy to Clipboard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                        //clip only if it successd
                        if(clipboardManager != null){
                            clipboardManager.setPrimaryClip(ClipData.newPlainText("", pollAddedIdText.getText().toString()));
                        }


                    }
                })
                .show();
    }


}