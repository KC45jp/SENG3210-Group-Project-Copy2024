package com.example.seng3210_group_project.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.example.seng3210_group_project.Poll.Poll.Poll;
import com.example.seng3210_group_project.Poll.Poll.Question;
import com.example.seng3210_group_project.R;
import com.example.seng3210_group_project.databinding.FragmentNotificationsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationsFragment extends Fragment {

    //variable
    String pollId;

    //UI
    Button buttonLoad;
    LinearLayout boxResult;

    //FireBase
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    //


    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {

        super.onStart();

        buttonLoad = getActivity().findViewById(R.id.buttonLoadResult);
        boxResult = getActivity().findViewById(R.id.boxResult);

        //FireBase
        database = FirebaseDatabase.getInstance("https://seng3210-group-project-default-rtdb.firebaseio.com/");

        boxResult.removeAllViews();


        //Hit Load Button
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pollId != ""){
                    loadResultDialog();
                }
            }
        });
    }


    private void loadResultDialog(){

        EditText textPollId = new EditText(getActivity());

        AlertDialog loadDialog = new AlertDialog.Builder(getActivity())
                .setTitle("LoadDialog")
                .setMessage("Enter Poll Id")
                .setView(textPollId)
                .setPositiveButton("Load", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        pollId = textPollId.getText().toString();
                        loadResult();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

        int i =0;
    }

    private void loadResult(){

        databaseReference = database.getReference("Polls/"+ "pollId"+pollId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = getPollInfo(snapshot);
                createNewTexiView(s);
                String t = getQuestionInfo(snapshot.child("Questions"));
                createNewTexiView(t);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DataBase", "Load Fail");

            }
        });
    }

    String getPollInfo(DataSnapshot snapshot){
        String s = snapshot.child("pollName").getValue().toString() + "\n" + snapshot.child("pollDesc").getValue().toString() + "\n";
        return s;
    }

    String getQuestionInfo(DataSnapshot snapshot){

        String totalQuestionInfo = "";
        for (DataSnapshot questionSnapShot:
                snapshot.getChildren()) {
            String quesitonInfo = questionSnapShot.child("questionDesc").getValue().toString() + "\n";
            totalQuestionInfo = totalQuestionInfo + quesitonInfo + getChoiceInfo(questionSnapShot.child("choices"));
        }
        return totalQuestionInfo;
    }

    String getChoiceInfo(DataSnapshot choicesSnapshot){
        String totalChoice = "";
        for(DataSnapshot choiceInfo:
        choicesSnapshot.getChildren()){
            String choice = choiceInfo.getKey().toString() + ": " + choiceInfo.getValue() +"\n";
            totalChoice = totalChoice + choice;
        }
        return  totalChoice;
    }

    void createNewTexiView(String s){
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(s);
        boxResult.addView(textView);

    }
}