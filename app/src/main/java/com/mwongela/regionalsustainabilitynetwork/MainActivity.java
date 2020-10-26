package com.mwongela.regionalsustainabilitynetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    CardView myProjects;
    CardView blog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myProjects = findViewById(R.id.cardViewPartner);
        blog=findViewById(R.id.cardViewBlog);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(MainActivity.this, RegisterActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);

        }
        myProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    Intent projectIntent = new Intent(MainActivity.this, ProjectActivity.class);
                    startActivity(projectIntent);
                }


            }
        });
        myProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    Intent projectIntent = new Intent(MainActivity.this, ProjectActivity.class);
                    startActivity(projectIntent);
                }


            }
        });
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    Intent projectIntent = new Intent(MainActivity.this, BlogDisplayActivity.class);
                    startActivity(projectIntent);
                }


            }
        });



    }
}
