package com.mwongela.regionalsustainabilitynetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment {
    Activity context;
    public ProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        //initialize recyclerview
        View rootView=inflater.inflate(R.layout.fragment_project, container, false);

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        FloatingActionButton fabProj = (FloatingActionButton) context.findViewById(R.id.createProject);
        fabProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProject = new Intent(context, CreateProjectActivity.class);
                startActivity(createProject);
            }
        });

    }
}
