package com.example.seng3210_group_project.ui.dashboard;

import static java.lang.Integer.max;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.seng3210_group_project.MainActivity;
import com.example.seng3210_group_project.Poll.Poll.Poll;
import com.example.seng3210_group_project.Poll.Poll.Question;
import com.example.seng3210_group_project.Poll.Result.Answer;
import com.example.seng3210_group_project.Poll.Result.Result;
import com.example.seng3210_group_project.R;
import com.example.seng3210_group_project.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.resources.TextAppearance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    int pollId;

    int questionId = 0;//We only have one question for this deploy. question ID is always 0.

    String selectedChoice;
    Button buttonLoad;
    Button buttonSubmit;
    Button buttonCancel;
    Button buttonNext;

    TextView textPollName;
    TextView textPollDesc;

    LinearLayout questionBox;

    Poll poll = new Poll();;


    private FragmentDashboardBinding binding;

    //Communication Devise
    TextView textViewSelectedChoice;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onStart() {

        super.onStart();

        database = FirebaseDatabase.getInstance("https://seng3210-group-project-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("Polls");

        buttonLoad = getActivity().findViewById(R.id.buttonLoadAnswer);

        textPollName = getActivity().findViewById(R.id.textViewPollName);
        textPollDesc = getActivity().findViewById(R.id.textViewPollDescription);

        questionBox = getActivity().findViewById(R.id.verticalBoxAnswer);

        textViewSelectedChoice = new TextView(getActivity());

        buttonCancel = getActivity().findViewById(R.id.buttonCancelAnswer);
        buttonSubmit = getActivity().findViewById(R.id.buttonSubmitAnswer);

        setMode(false);



        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText textEditPollId = new EditText(getActivity());
                List<String> pollIdList = new ArrayList<>();

                setMode(false);

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Input Poll Id")
                        .setView(textEditPollId)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pollId = Integer.valueOf(textEditPollId.getText().toString());
                                LoadDB(textEditPollId, pollIdList);
                            }
                        })
                        .show();
                setMode(true);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot pollSnapshot:
                                snapshot.getChildren()) {
                            pollIdList.add(pollSnapshot.getKey().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

        //This textViewSelectedChoice changed means User Selected the Choice
        //So it would send DB to submit Result
        textViewSelectedChoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setMode(true);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Are you Sure?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference choicesDbReference = databaseReference.child("pollId" + pollId).child("Questions").child("questionId" + questionId).child("choices");
                                choicesDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChildren()){
                                            selectedChoice = textViewSelectedChoice.getText().toString();
                                            String voteNumber = snapshot.child(selectedChoice).getValue().toString();
                                            int voteInt = Integer.valueOf(voteNumber)+1;
                                            choicesDbReference.child(selectedChoice).setValue(voteInt);
                                        }
                                        else{
                                            Log.e("Warning", "Data Send Fail");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                setMode(false);

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(false);
            }
        });

    }

    private void LoadDB(EditText textEditPollId, List<String> pollIdList){
        String stringPollId = (textEditPollId.getText().toString());
        if (pollIdList.contains("pollId" +  stringPollId)) {
            poll = new Poll();
            poll.setPoll_id(Integer.valueOf(stringPollId));
            loadPoll(stringPollId);
            String s= poll.getDescription();
        }
        else {
            //Display poll id does not exist
        }
    }

    private void loadPoll(String stringPollId){
        databaseReference.child("pollId" + stringPollId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                String pollName = snapshot.child("pollName").getValue().toString();
                String pollDesc = snapshot.child("pollDesc").getValue().toString();

                poll.setPollName(pollName);
                poll.setDescription(pollDesc);
            }
        });
        loadQuestions(stringPollId);

    }

    private void loadQuestions(String stringPollId){
        databaseReference.child("pollId" + stringPollId).child("Questions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                int i = 0;
                Question question = new Question();

                for (DataSnapshot questionSnapShot:
                        snapshot.child("questionId" + i).getChildren()) {

                    DataSnapshot snapshot1 = questionSnapShot;

                    question.setQuestionId(i);

                    if(questionSnapShot.getKey().toString().equals("questionDesc")){
                        question.setDescription(questionSnapShot.getValue().toString());
                    }
                    if (questionSnapShot.getKey().toString().equals("choices")) {
                        for (DataSnapshot choicesSnapshot:
                                questionSnapShot.getChildren()) {
                            String choice = choicesSnapshot.getKey().toString();
                            question.addChoices(choice);
                        }
                    }

                    String f= question.getDescription();
                    int g = question.choiceLength();

                    if(question.getDescription() != null && question.choiceLength() != 0){
                        poll.addQuestion(question);
                        question = new Question();
                        i++;
                        if(!snapshot.child("questionId" + i).exists()){
                            break;
                        }
                    }
                }
                //Deal with UI
                updateUI();
            }
        });
    }

    void updateUI(){
        textPollName.setText(poll.getPollName());
        textPollDesc.setText(poll.getDescription());

        TextView textQuestionDesc = new TextView(getActivity());

        textQuestionDesc.setText(poll.getQuestion(0).getDescription());
        textQuestionDesc.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        int textApp = com.google.android.material.R.style.TextAppearance_AppCompat_Display1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textQuestionDesc.setTextAppearance(textApp);
        }
        questionBox.addView(textQuestionDesc);


        for (String choice:
             poll.getQuestion(0).getChoices()) {
            Button button = new Button(getActivity());
            button.setText(choice);
            button.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textViewSelectedChoice.setText(button.getText().toString());
                }
            });

            questionBox.addView(button);
        }


    }

    void setMode(Boolean mode){
        buttonSubmit.setEnabled(mode);
        buttonCancel.setEnabled(mode);

        if(!mode){
            textPollName.setText("");
            textPollDesc.setText("");
            questionBox.removeAllViews();
            poll = null;
        }

    }



}


