package com.mwongela.regionalsustainabilitynetwork.rsn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mwongela.regionalsustainabilitynetwork.LoginActivity;
import com.mwongela.regionalsustainabilitynetwork.R;

public class QuestionThree extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView textView_questionYes;
    private TextInputEditText input_policy;
    private TextInputLayout policy_layout;
    private DatabaseReference surveyRef,mDatabaseUsers;
    String post_key = null;
    String selectedRadio =null;
    int radioValue=1;
    private ProgressBar progressBar;
    private RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_three);
        mAuth = FirebaseAuth.getInstance();

        textView_questionYes=findViewById(R.id.if_yes);
        input_policy=findViewById(R.id.policies);
        policy_layout=findViewById(R.id.policies_layout);
        post_key = getIntent().getExtras().getString("PostKey");

        textView_questionYes.setVisibility(View.INVISIBLE);
        policy_layout.setVisibility(View.INVISIBLE);
        input_policy.setVisibility(View.INVISIBLE);
        layout = findViewById(R.id.display);
        progressBar = new ProgressBar(QuestionThree.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(QuestionThree.this, LoginActivity.class);
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabQ3);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    validate();
                }
            });

        }
    }
    //this method will handle checked radio buttons
    public void onRadioButtonClicked(View view) {
        //define what to do with checked radio button
        //create a boolean variable and use the method checked to determine if a radio button is checked
        boolean checked =((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.yes:
                if(checked)
                    textView_questionYes.setVisibility(View.VISIBLE);
                    policy_layout.setVisibility(View.VISIBLE);
                    input_policy.setVisibility(View.VISIBLE);
                     selectedRadio="Yes";
                     radioValue=1;
                break;
            case R.id.no:
                if(checked) textView_questionYes.setVisibility(View.INVISIBLE);
                input_policy.setVisibility(View.INVISIBLE);
                selectedRadio="No";
                radioValue=0;

                break;
            default:
                //do something
                break;
        }


    }
    public void validate() {
        boolean valid = true;
        String achievedPolicies = input_policy.getText().toString();
        String selectedOption = String.valueOf(selectedRadio);
        String calculatedValue = String.valueOf(radioValue);
        String q3Label = getString(R.string.qn3Label);

        if (selectedOption.isEmpty()) {
            Toast.makeText(QuestionThree.this, "Please select a radio button", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (valid){

            surveyRef.push();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    surveyRef.child("QuestionTwo").child("Label").setValue(q3Label);
                    surveyRef.child("QuestionTwo").child("SelectedOption").setValue(selectedOption);
                    surveyRef.child("QuestionTwo").child("Weight").setValue(calculatedValue);
                    surveyRef.child("QuestionTwo").child("Policies").setValue(achievedPolicies)


                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Intent next = new Intent(QuestionThree.this, QuestionFour.class);
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
            Toast.makeText(QuestionThree.this, "Please complete this question", Toast.LENGTH_SHORT).show();
        }
    }
    }
