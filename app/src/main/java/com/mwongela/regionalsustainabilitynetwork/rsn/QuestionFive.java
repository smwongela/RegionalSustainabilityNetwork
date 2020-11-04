package com.mwongela.regionalsustainabilitynetwork.rsn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.mwongela.regionalsustainabilitynetwork.CadimOne;
import com.mwongela.regionalsustainabilitynetwork.CealOne;
import com.mwongela.regionalsustainabilitynetwork.LoginActivity;
import com.mwongela.regionalsustainabilitynetwork.NoFutherQuestions;
import com.mwongela.regionalsustainabilitynetwork.R;
import com.mwongela.regionalsustainabilitynetwork.can.CanOne;

public class QuestionFive extends AppCompatActivity {
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
     private DatabaseReference surveyRef;

    private TextInputEditText eventChallenges;
    String post_key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_five);
        eventChallenges=findViewById(R.id.challenges);
        post_key = getIntent().getExtras().getString("PostKey");
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
        surveyRef = FirebaseDatabase.getInstance().getReference().child("Reports").child(post_key);
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
                    validate();

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
                    next.putExtra("PostKey",post_key);
                    startActivity(next);

                }
                if(organisation.equalsIgnoreCase("CEAL")) {
                    Intent next = new Intent(QuestionFive.this, CealOne.class);
                    next.putExtra("PostKey",post_key);
                    startActivity(next);


                }
                if(organisation.equalsIgnoreCase("CAN")) {
                    Intent next = new Intent(QuestionFive.this, CanOne.class);
                    next.putExtra("PostKey",post_key);
                    startActivity(next);


                }else {
                    Intent next = new Intent(QuestionFive.this, NoFutherQuestions.class);
                    next.putExtra("PostKey",post_key);
                    startActivity(next);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void validate() {
        boolean valid = true;
        String challenges =eventChallenges.getText().toString();
        String q5Label=getString(R.string.qn5Label);

        if (challenges.isEmpty()) {
            eventChallenges.setError("Required");
            valid = false;
        } else {
            eventChallenges.setError(null);
        }


        if (valid) {
            surveyRef.push();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    surveyRef.child("QuestionFive").child("Label").setValue(q5Label);
                    surveyRef.child("QuestionFive").child("Challenges").setValue(challenges).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            checkPartner();



                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(QuestionFive.this,"Please complete the benefits",Toast.LENGTH_SHORT).show();
        }

    }
}