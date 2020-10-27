package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class QuestionFive extends AppCompatActivity {
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_five);
        //Initialize the instance of the firebase user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(QuestionFive.this, LoginActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
    }

    @Override
    public void onStart() {
        super.onStart();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabQ5);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPartner();
                }
            });
        }
    }

    private void checkPartner() {
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String organisation = (String) dataSnapshot.child("organisation").getValue();
                if(organisation.equalsIgnoreCase("CADIM")) {
                    Intent next = new Intent(QuestionFive.this, CadimOne.class);
                    startActivity(next);

                }
                if(organisation.equalsIgnoreCase("CEAL")) {
                    Intent next = new Intent(QuestionFive.this, CealOne.class);
                    startActivity(next);


                }
                if(organisation.equalsIgnoreCase("CAN")) {
                    Intent next = new Intent(QuestionFive.this, CanOne.class);
                    startActivity(next);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}