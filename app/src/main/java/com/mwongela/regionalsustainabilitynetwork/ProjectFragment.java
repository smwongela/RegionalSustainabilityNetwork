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
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference likesRef,projectsRef;
    private FirebaseAuth mAuth;
    // private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Boolean likeChecker = false;
    //  private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;
    private EventModel eventModel;
    public ProjectFragment() {
        // Required empty public constructor
    }

    Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        //initialize recyclerview
        View rootView=inflater.inflate(R.layout.fragment_project, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_projects);
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

        projectsRef= FirebaseDatabase.getInstance().getReference().child("Projects");
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
                        .setQuery(projectsRef, EventModel.class)
                        .build();

        FirebaseRecyclerAdapter<EventModel, ProjectFragment.ProjectViewHolder> adapter =
                new FirebaseRecyclerAdapter<EventModel, ProjectFragment.ProjectViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProjectFragment.ProjectViewHolder holder, int position, @NonNull EventModel model) {
                        final String post_key = getRef(position).getKey();
                        // final int position = getAdapterPosition();
                        // holder.setCard();
                        //eventsRef.child(post_key).addValueEventListener

                        projectsRef.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {


                                    final String projectName = snapshot.child("projectName").getValue().toString();
                                    final String projectConvener = snapshot.child("projectConvener").getValue().toString();
                                    final String projectCountry = snapshot.child("projectCountry").getValue().toString();
                                    final String projectDate = snapshot.child("projectDate").getValue().toString();


                                    /*Glide.with(getContext()).load(photo)
                                            .bitmapTransform(new BlurTransformation(context))
                                            .into((holder.profile_image);

                                                            */
                                    //Glide.with(getContext()).load(photo).into(holder.profile_image);
                                    holder.project_name.setText(projectName);
                                    holder.project_country.setText(projectCountry);
                                    holder.project_convener.setText(projectConvener);
                                    holder.project_date.setText(projectDate);




                                    if(projectConvener.equalsIgnoreCase("CADIM")) {
                                        Picasso.with(getContext()).load(R.drawable.ceal2).resize(200,200)

                                                .into(holder.partner_logo);

                                        //holder.changingLayout.setBackgroundResource(R.drawable.stickgreen);
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
                    public ProjectFragment.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card_item, parent, false);

                        return new ProjectFragment.ProjectViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder{
        //Declare the view objects in the card view
        public ImageView partner_logo;
        public TextView project_name;
         public TextView project_country;
        public TextView project_convener;
        public TextView project_date;
        public LinearLayout changingLayout;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            //profile_image=itemView.findViewById(R.id.userImage);
            project_name=itemView.findViewById(R.id.text_view_project_name);
            project_country=itemView.findViewById(R.id.text_view_country);
            project_convener=itemView.findViewById(R.id.text_view_partner);
            project_date=itemView.findViewById(R.id.text_view_date);
            partner_logo=itemView.findViewById(R.id.partnerLogo);

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
