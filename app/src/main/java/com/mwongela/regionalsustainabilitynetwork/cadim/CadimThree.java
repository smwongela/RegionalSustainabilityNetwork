package com.mwongela.regionalsustainabilitynetwork.cadim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
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

public class CadimThree extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference surveyRef,mDatabaseUsers;
    String post_key = null;
    String selectedRadio =null;
    int radioValue=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadim_three);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        post_key = getIntent().getExtras().getString("PostKey");
        if (currentUser == null) {

            Intent loginIntent = new Intent(CadimThree.this, LoginActivity.class);
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
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCadim3);
        if (currentUser  !=null) {


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            case R.id.s_agree:
                if(checked)
                    selectedRadio="Strongly Agree";
                radioValue=5;
                break;
            case R.id.agree:
                if(checked)
                    selectedRadio="Agree";
                radioValue=4;

                break;
            case R.id.neutral:
                if(checked)
                    selectedRadio="Neutral";
                radioValue=3;

                break;
            case R.id.disagree:
                if(checked)
                    selectedRadio="Disagree";
                radioValue=2;

                break;
            case R.id.s_disagree:
                if(checked)
                    selectedRadio="Strongly Disagree";
                radioValue=1;

                break;
            default:

                break;
        }


    }
    public void validate() {
        boolean valid = true;

        String selectedOption = String.valueOf(selectedRadio);
        String calculatedValue = String.valueOf(radioValue);
        String qnLabel = getString(R.string.cadim3Label);

        if (selectedOption.isEmpty()) {
            Toast.makeText(CadimThree.this, "Please select a radio button", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (valid){

            surveyRef.push();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    surveyRef.child("CadimThree").child("Label").setValue(qnLabel);
                    surveyRef.child("CadimThree").child("SelectedOption").setValue(selectedOption);
                    surveyRef.child("CadimThree").child("Weight").setValue(calculatedValue)



                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent next = new Intent(CadimThree.this, CadimFour.class);
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
            Toast.makeText(CadimThree.this, R.string.complete_question, Toast.LENGTH_SHORT).show();
        }
    }
}