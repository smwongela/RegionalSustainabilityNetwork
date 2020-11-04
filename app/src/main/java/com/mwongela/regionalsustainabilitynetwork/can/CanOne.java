package com.mwongela.regionalsustainabilitynetwork.can;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mwongela.regionalsustainabilitynetwork.LoginActivity;
import com.mwongela.regionalsustainabilitynetwork.R;
import com.mwongela.regionalsustainabilitynetwork.rsn.QuestionFour;
import com.mwongela.regionalsustainabilitynetwork.rsn.QuestionOne;
import com.mwongela.regionalsustainabilitynetwork.rsn.QuestionTwo;

public class CanOne extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_one);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(CanOne.this, LoginActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCan1);
        if (currentUser  !=null) {


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent next = new Intent(CanOne.this, CanTwo.class);
                    startActivity(next);
                }
            });

        }
    }
}