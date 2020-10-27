package com.mwongela.regionalsustainabilitynetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {
    Activity context;
    private FirebaseAuth mAuth;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(context, RegisterActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fabSurvey);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
           fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createEvent = new Intent(context, QuestionOne.class);
                    startActivity(createEvent);
                }
            });

        }



    }
}
