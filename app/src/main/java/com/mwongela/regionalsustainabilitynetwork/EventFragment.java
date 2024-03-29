package com.mwongela.regionalsustainabilitynetwork;

import android.app.Activity;
import android.content.Context;
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
import com.mwongela.regionalsustainabilitynetwork.rsn.QuestionOne;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference eventsRef;
    private FirebaseAuth mAuth;

    private EventModel eventModel;

    public EventFragment() {
        // Required empty public constructor
    }

    Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        //initialize recyclerview
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_events);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        //Reverse  the layout so as to display the most recent post at the top
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            Intent loginIntent = new Intent(context, RegisterActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }

        eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        String userID = mAuth.getCurrentUser().getUid();
        FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fabEvent);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createEvent = new Intent(context, EventActivity.class);
                    startActivity(createEvent);
                }
            });
        }
        FirebaseRecyclerOptions<EventModel> options =
                new FirebaseRecyclerOptions.Builder<EventModel>()
                        .setQuery(eventsRef, EventModel.class)
                        .build();

        FirebaseRecyclerAdapter<EventModel, EventViewHolder> adapter =
                new FirebaseRecyclerAdapter<EventModel, EventViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull EventModel model) {
                        final String post_key = getRef(position).getKey();

                        eventsRef.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {


                                    final String photo = snapshot.child("profilePhoto").getValue().toString();
                                    final String event = snapshot.child("eventName").getValue().toString();
                                    final String venue = snapshot.child("eventVenue").getValue().toString();
                                    final String country = snapshot.child("country").getValue().toString();
                                    final String convener = snapshot.child("organisation").getValue().toString();
                                    final String type = snapshot.child("eventType").getValue().toString();
                                    final String date = snapshot.child("eventDate").getValue().toString();
                                    final String postedBy = snapshot.child("postedBy").getValue().toString();
                                    if (userID.equals(postedBy)) {

                                        holder.fill_report.setVisibility(View.VISIBLE);
                                        holder.fill_report.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent reportIntent = new Intent(context, QuestionOne.class);
                                                reportIntent.putExtra("EventID", post_key);
                                                reportIntent.putExtra("EventName", event);
                                                startActivity(reportIntent);
                                            }
                                        });

                                    } else if (!userID.equals(postedBy)) {
                                        holder.fill_report.setVisibility(View.INVISIBLE);
                                    }
                                    holder.event_venue.setText(venue);
                                    Picasso.with(getContext()).load(photo).resize(200, 200)
                                            .transform(new CropCircleTransformation())
                                            .into(holder.profile_image);

                                    holder.event_name.setText(event);
                                    holder.event_country.setText(country);
                                    holder.event_convener.setText(convener);
                                    holder.event_type.setText(type);
                                    holder.event_date.setText(date);

                                }

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_item, parent, false);

                        return new EventViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view
        public final ImageView profile_image;
        public final TextView event_name;
        public final TextView event_venue;
        public final TextView event_country;
        public final TextView event_convener;
        public final TextView event_type;
        public final TextView event_date;
        public final TextView fill_report;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            profile_image = itemView.findViewById(R.id.userImage);
            event_name = itemView.findViewById(R.id.text_view_project_name);
            event_venue = itemView.findViewById(R.id.text_view_venue);
            event_country = itemView.findViewById(R.id.text_view_country);
            event_convener = itemView.findViewById(R.id.text_view_partner);
            event_type = itemView.findViewById(R.id.text_view_event_type);
            event_date = itemView.findViewById(R.id.text_view_date);

            fill_report = itemView.findViewById(R.id.fill_report);
            fill_report.setVisibility(View.INVISIBLE);

        }


    }

}