package com.mwongela.regionalsustainabilitynetwork.rsn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mwongela.regionalsustainabilitynetwork.LoginActivity;
import com.mwongela.regionalsustainabilitynetwork.ProfileActivity;
import com.mwongela.regionalsustainabilitynetwork.R;

public class QuestionTwo extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference surveyRef,mDatabaseUsers;
    private ProgressBar progressBar;
    private RelativeLayout layout;


    private TextInputEditText benefits;
    String post_key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_two);
        benefits=findViewById(R.id.benefits);
        post_key = getIntent().getExtras().getString("PostKey");
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        layout = findViewById(R.id.display);
        progressBar = new ProgressBar(QuestionTwo.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);
        if (currentUser == null) {

            Intent loginIntent = new Intent(QuestionTwo.this, LoginActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }


        //Initialize the instance of the firebase user
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        surveyRef = FirebaseDatabase.getInstance().getReference().child("Reports").child(post_key);

        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabQ2);
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

    public void validate() {
        boolean valid = true;
        String eventBenefits = benefits.getText().toString();
        String q2Label=getString(R.string.label_q2);

        if (eventBenefits.isEmpty()) {
            benefits.setError("Required");
            valid = false;
        } else {
            benefits.setError(null);
        }


        if (valid) {
          surveyRef.push();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    surveyRef.child("QuestionTwo").child("Label").setValue(q2Label);
                    surveyRef.child("QuestionTwo").child("benefits").setValue(eventBenefits).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            Intent next = new Intent(QuestionTwo.this, QuestionThree.class);
                            next.putExtra("PostKey",post_key);
                            startActivity(next);

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(QuestionTwo.this,"Please complete the benefits",Toast.LENGTH_SHORT).show();
        }

    }


    }
