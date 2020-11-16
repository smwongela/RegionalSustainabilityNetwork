package com.mwongela.regionalsustainabilitynetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference surveyRef;
    // private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Boolean likeChecker = false;
    //  private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;
    private EventModel eventModel;

    public ReportFragment() {
        // Required empty public constructor
    }

    Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        //initialize recyclerview
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_reports);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        //Reverse  the layout so as to display the most recent post at the top
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.setLayoutManager(linearLayoutManager);

        // Inflate the layout for this fragment
        //get an instance of firebase authentication

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(context, RegisterActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        surveyRef = FirebaseDatabase.getInstance().getReference().child("Reports");
        //reportsRef= FirebaseDatabase.getInstance().getReference().child("Projects");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<EventModel> options =
                new FirebaseRecyclerOptions.Builder<EventModel>()
                        .setQuery(surveyRef, EventModel.class)
                        .build();

        FirebaseRecyclerAdapter<EventModel, ReportFragment.ProjectViewHolder> adapter =
                new FirebaseRecyclerAdapter<EventModel, ReportFragment.ProjectViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReportFragment.ProjectViewHolder holder, int position, @NonNull EventModel model) {
                        final String post_key = getRef(position).getKey();
                        // final int position = getAdapterPosition();
                        // holder.setCard();
                        //eventsRef.child(post_key).addValueEventListener
                        holder.surveyLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent singleLayout = new Intent(context, SingleSurvey.class);
                                singleLayout.putExtra("PostKey", post_key);
                                startActivity(singleLayout);
                            }
                        });

                        surveyRef.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {


                                    final String surveyName = snapshot.child("eventName").getValue().toString();
                                    final String projectConvener = snapshot.child("organisation").getValue().toString();
                                    final String surveyLocation = snapshot.child("location").getValue().toString();
                                    final String surveyImage = snapshot.child("eventPhoto").getValue().toString();
                                   final String day= snapshot.child("date").getValue().toString();


                                    Picasso.with(getContext()).load(surveyImage).into(holder.survey_image);
                                    holder.survey_name.setText(surveyName);
                                    holder.survey_location.setText(surveyLocation);
                                    holder.survey_convener.setText(projectConvener);
                                    holder.surveyDate.setText(day);


                                    if (projectConvener.equalsIgnoreCase("Hanns Seidel")) {
                                        Picasso.with(getContext()).load(R.drawable.seidel).resize(600, 200).centerInside().into(holder.partner_logo);
                                    }

                                    if (projectConvener.equalsIgnoreCase("CADIM")) {
                                        Picasso.with(getContext()).load(R.drawable.cadim).resize(600, 200).centerInside().into(holder.partner_logo);
                                    }
                                    if (projectConvener.equalsIgnoreCase("EED")) {
                                        Picasso.with(getContext()).load(R.drawable.eed_logo).resize(600, 200).centerInside().into(holder.partner_logo);
                                    }
                                    if (projectConvener.equalsIgnoreCase("CAN")) {
                                        Picasso.with(getContext()).load(R.drawable.can).resize(600, 200).centerInside().into(holder.partner_logo);
                                    }
                                    if (projectConvener.equalsIgnoreCase("CEAL")) {
                                        Picasso.with(getContext()).load(R.drawable.ceal2).resize(600, 200).centerInside().into(holder.partner_logo);
                                    }
                                    if (projectConvener.equalsIgnoreCase("Urbis")) {
                                        Picasso.with(getContext()).load(R.drawable.urbis).resize(600, 200).centerInside().into(holder.partner_logo);
                                    }
                                    if (projectConvener.equalsIgnoreCase("Champions")) {
                                        Picasso.with(getContext()).load(R.drawable.champions_logo).resize(600, 200).centerInside().into(holder.partner_logo);
                                    }
                                    if (projectConvener.equalsIgnoreCase("DRFN")) {
                                        Picasso.with(getContext()).load(R.drawable.drfn_logo).resize(600, 200).centerInside().into(holder.partner_logo);
                                    }
                                    if (projectConvener.equalsIgnoreCase("SOS")) {
                                        Picasso.with(getContext()).load(R.drawable.sos_logo).resize(600, 200).centerInside().into(holder.partner_logo);
                                    }


                                }

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ReportFragment.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_card_item, parent, false);

                        return new ProjectViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view
        public final ImageView partner_logo;
        public final TextView survey_convener;
        public final TextView survey_location;
        public final ImageView survey_image;
        public final TextView survey_name;
        public final TextView surveyDate;



        public LinearLayout surveyLayout;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);

            partner_logo = itemView.findViewById(R.id.partnerLogo);
            survey_convener = itemView.findViewById(R.id.survey_convener);
            survey_location = itemView.findViewById(R.id.survey_location);
            survey_image = itemView.findViewById(R.id.event_survey_image);
            survey_name = itemView.findViewById(R.id.event_survey_name);
            surveyLayout = itemView.findViewById(R.id.surveyLayout);
            surveyDate=itemView.findViewById(R.id.survey_date);



        }


    }
}
