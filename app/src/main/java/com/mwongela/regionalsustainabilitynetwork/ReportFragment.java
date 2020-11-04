package com.mwongela.regionalsustainabilitynetwork;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference surveyRef;
    private FirebaseAuth mAuth;
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
        View rootView=inflater.inflate(R.layout.fragment_report, container, false);
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

        mAuth = FirebaseAuth.getInstance();
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


        FloatingActionButton fab= (FloatingActionButton) context.findViewById(R.id.createProject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createProject = new Intent(context, CreateProjectActivity.class);
                startActivity(createProject);
            }
        });

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

                        surveyRef.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {


                                    final String surveyName = snapshot.child("eventName").getValue().toString();
                                    final String projectConvener = snapshot.child("organisation").getValue().toString();
                                    final String surveyLocation = snapshot.child("location").getValue().toString();
                                    final String surveyImage = snapshot.child("eventPhoto").getValue().toString();



                                    Picasso.with(getContext()).load(surveyImage).into(holder.survey_image);
                                    holder.survey_name.setText(surveyName);
                                    holder.survey_location.setText(surveyLocation);
                                    holder.survey_convener.setText(projectConvener);

                                    /*Glide.with(getContext()).load(photo)
                                            .bitmapTransform(new BlurTransformation(context))
                                            .into((holder.profile_image);

                                                            */
                                    //Glide.with(getContext()).load(photo).into(holder.profile_image);





                                    if(projectConvener.equalsIgnoreCase("CADIM")) {
                                        Picasso.with(getContext()).load(R.drawable.cadim).into(holder.partner_logo);
                                    }
                                    if(projectConvener.equalsIgnoreCase("EED")) {
                                        Picasso.with(getContext()).load(R.drawable.eed_logo).into(holder.partner_logo);
                                    }
                                    if(projectConvener.equalsIgnoreCase("CAN")) {
                                        Picasso.with(getContext()).load(R.drawable.can).into(holder.partner_logo);
                                    }
                                    if(projectConvener.equalsIgnoreCase("CEAL")) {
                                        Picasso.with(getContext()).load(R.drawable.ceal2).into(holder.partner_logo);
                                    }
                                    if(projectConvener.equalsIgnoreCase("Urbis")) {
                                        Picasso.with(getContext()).load(R.drawable.urbis).into(holder.partner_logo);
                                    }
                                    if(projectConvener.equalsIgnoreCase("Champions")) {
                                        Picasso.with(getContext()).load(R.drawable.champions_logo).into(holder.partner_logo);
                                    }
                                    if(projectConvener.equalsIgnoreCase("DRFN")) {
                                        Picasso.with(getContext()).load(R.drawable.drfn_logo).into(holder.partner_logo);
                                    }
                                    if(projectConvener.equalsIgnoreCase("SOS")) {
                                        Picasso.with(getContext()).load(R.drawable.sos_logo).into(holder.partner_logo);
                                    }
                                    //<item>EED</item>
                                    //        <item>ECRC</item>
                                    //        <item>KCIC</item>
                                    //        <item>DRFN</item>
                                    //        <item>CEAL</item>
                                    //        <item>CADIM</item>
                                    //        <item>CAN</item>
                                    //        <item>Urbis</item>
                                    //        <item>Champions</item>
                                    //        <item>Hanns Seidel </item>
                                    /*


                                    }
                                    /*
                                    if(convener.equalsIgnoreCase("CAN")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.blue_sticky);
                                    }
                                    if(convener.equalsIgnoreCase("CEAL")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.purple_stickky);
                                    }
                                    if(convener.equalsIgnoreCase("Urbis")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.stick1);
                                    }
                                    if(convener.equalsIgnoreCase("Hanns Seidel")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.blue_sticky);
                                    }
                                    if(convener.equalsIgnoreCase("EED")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.purple_stickky);
                                    }
                                    if(convener.equalsIgnoreCase("KCIC")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.stickgreen);
                                    }
                                    if(convener.equalsIgnoreCase("Champions")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.stick1);
                                    }
                                    if(convener.equalsIgnoreCase("DRFN")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.stickgreen);
                                    }*/
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

                        return new ReportFragment.ProjectViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder{
        //Declare the view objects in the card view
        public ImageView partner_logo;
        public TextView survey_convener;
        public  TextView survey_location;
        public ImageView survey_image;
        public  TextView survey_name;

        public LinearLayout changingLayout;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            //profile_image=itemView.findViewById(R.id.userImage);
             partner_logo=itemView.findViewById(R.id.partnerLogo);
             survey_convener=itemView.findViewById(R.id.survey_convener);
             survey_location=itemView.findViewById(R.id.survey_location);
             survey_image=itemView.findViewById(R.id.event_survey_image);
             survey_name=itemView.findViewById(R.id.event_survey_name);

                                        /*
                                    }
                                    if(convener.equalsIgnoreCase("CEAL")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.purple_stickky);
                                    }
                                    if(convener.equalsIgnoreCase("Urbis")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.stick1);
                                    }
                                    if(convener.equalsIgnoreCase("Hanns Seidel")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.blue_sticky);
                                    }
                                    if(convener.equalsIgnoreCase("EED")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.purple_stickky);
                                    }
                                    if(convener.equalsIgnoreCase("KCIC")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.stickgreen);
                                    }
                                    if(convener.equalsIgnoreCase("Champions")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.stick1);
                                    }
                                    if(convener.equalsIgnoreCase("DRFN")){
                                        holder.changingLayout.setBackgroundResource(R.drawable.stickgreen);
                                    }*/

        }
        public void setCard() {
            //  size = adapter.getItemCount();
            final int position = getAdapterPosition();


            if (position == 0) {

                changingLayout.setBackgroundResource(R.drawable.greencard2);

            }
            if (position == 1) {
                changingLayout.setBackgroundResource(R.drawable.stickgreen);
            }

        }


    }
}
