package com.mwongela.regionalsustainabilitynetwork.rsn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mwongela.regionalsustainabilitynetwork.LoginActivity;
import com.mwongela.regionalsustainabilitynetwork.R;

public class QuestionFour extends AppCompatActivity {
private FirebaseAuth mAuth;
    private DatabaseReference surveyRef,mDatabaseUsers;
    String post_key = null;
    String selectedRadio =null;
    int radioValue=0;
    private ProgressBar progressBar;
    private RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_four);
        mAuth = FirebaseAuth.getInstance();
        post_key = getIntent().getExtras().getString("PostKey");
        layout = findViewById(R.id.display);
        progressBar = new ProgressBar(QuestionFour.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(QuestionFour.this, LoginActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        surveyRef = FirebaseDatabase.getInstance().getReference().child("Reports").child(post_key);

        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
    }
    @Override
    public void onStart() {
        super.onStart();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabQ4);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                  validate();

                }
            });

        }
    }
    public void onRadioButtonClicked(View view) {
        //define what to do with checked radio button
        //create a boolean variable and use the method checked to determine if a radio button is checked
        boolean checked =((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.partial:
                if(checked)

                selectedRadio="Partial";
                radioValue=5;
                break;
            case R.id.high:
                if(checked)
                    selectedRadio="High";
                radioValue=15;

                break;
            case R.id.medium:
                if(checked)
                    selectedRadio="Medium";
                radioValue=10;

                break;
            default:
                //do something
                break;
        }


    }
    public void validate() {
        boolean valid = true;

        String selectedOption = String.valueOf(selectedRadio);
        String calculatedValue = String.valueOf(radioValue);
        String q4Label = getString(R.string.qn4Label);

        if (selectedOption.isEmpty()) {
            Toast.makeText(QuestionFour.this, "Please select a radio button", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (valid){

            surveyRef.push();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    surveyRef.child("QuestionThree").child("Label").setValue(q4Label);
                    surveyRef.child("QuestionThree").child("SelectedOption").setValue(selectedOption);
                    surveyRef.child("QuestionThree").child("Weight").setValue(calculatedValue)

                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    Intent next = new Intent(QuestionFour.this, QuestionFive.class);
                                    next.putExtra("PostKey",post_key);
                                    startActivity(next);

                                }
                            });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else{
            Toast.makeText(QuestionFour.this, "Please complete Filling this part", Toast.LENGTH_SHORT).show();
        }
    }
}
