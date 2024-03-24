package com.example.seng3210_group_project.ui.dashboard;

import static java.lang.Integer.max;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DatabaseUtils;
import android.os.Bundle;
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
import com.example.seng3210_group_project.R;
import com.example.seng3210_group_project.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    Button buttonLoad;
    Button buttonSubmit;
    Button buttonCancel;
    Button buttonNext;

    TextView textPollName;
    TextView textPollDesc;

    LinearLayout questionBox;

    Poll poll;


    private FragmentDashboardBinding binding;



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


        buttonLoad.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                EditText textEditPollId = new EditText(getActivity());
                List<String> pollIdList = new ArrayList<>();

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Input Poll Id")
                        .setView(textEditPollId)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String stringPollId = (textEditPollId.getText().toString());
                                if (pollIdList.contains("pollId" +  stringPollId)) {
                                    poll = new Poll();
                                    poll.setPoll_id(Integer.valueOf(stringPollId));

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
                                                        String choice = choicesSnapshot.getValue().toString();
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

                                            textPollName.setText(poll.getPollName());
                                            textPollDesc.setText(poll.getDescription());
                                        }
                                    });

                                }

                            }

                        })
                        .show();

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

    }

}


