package com.mwongela.regionalsustainabilitynetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NoFutherQuestions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_futher_questions);
    }

    public void launchMainActivity(View view) {
        Intent exit = new Intent(this, ProjectActivity.class);
        startActivity(exit);
        finish();
    }
}