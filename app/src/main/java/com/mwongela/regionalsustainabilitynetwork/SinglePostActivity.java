package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mwongela.regionalsustainabilitynetwork.rsn.QuestionOne;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SinglePostActivity extends AppCompatActivity {

    private TextView post_title, postComment;
    private TextView post_desc;
    private  ImageView post_image;
    private  TextView postUserName;
    private  ImageView user_image;
    private TextView postTime;
    private  TextView postDate;

    private  ImageButton likePostButton;
    private  ImageButton commentPostButton;
    private  TextView displayLikes;

    private  TextView userOrganisation;
    private  TextView userCountry;
    private  LinearLayout layoutComment;




    String post_key = null;
    private DatabaseReference mDatabase,commentRef,mDatabaseUsers;
    private TextView deleteBtn;
    private ImageView commentingUserImage;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    EditText makeComment;

    FirebaseRecyclerAdapter<CommentModel, SinglePostActivity.commentModelViewHolder> adapter;
    String currentUserID =null;

    //String currentUserID =null;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        commentingUserImage=findViewById(R.id.commentingUserImage);


        post_title = findViewById(R.id.post_title);
        post_desc = findViewById(R.id.post_desc);
        post_image = findViewById(R.id.poster_image);
        postUserName = findViewById(R.id.poster_user);
        user_image = findViewById(R.id.user);
       // postTime = findViewById(R.id.time);

        postDate = findViewById(R.id.tV_date);
        userCountry=findViewById(R.id.tV_country);
        userOrganisation=findViewById(R.id.tV_organisation);
        layoutComment=findViewById(R.id.layout_comment);



        layout = findViewById(R.id.display);
        progressBar = new ProgressBar(SinglePostActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);
        post_image.setVisibility(View.GONE);


        //initialize recyclerview
        recyclerView = findViewById(R.id.comment_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse  the layout so as to display the most recent post at the top
        // linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);



        postComment = findViewById(R.id.postComment);
        makeComment = findViewById(R.id.editTextcomment);

        post_key = getIntent().getExtras().getString("PostID");

        //Initialize the database reference/node where you will be storing posts
        commentRef = FirebaseDatabase.getInstance().getReference().child("PostComments").child(post_key);
        //Initialize an instance of  Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());



        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        deleteBtn = findViewById(R.id.deleteBtn);
        mAuth = FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();

                Intent mainintent = new Intent(SinglePostActivity.this, MainActivity.class);
                startActivity(mainintent);
            }
        });


        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String postTitle = (String) snapshot.child("title").getValue();
                    String postDescription = (String) snapshot.child("desc").getValue();

                    String displayName = (String) snapshot.child("displayName").getValue();
                    String profilePhoto = (String) snapshot.child("profilePhoto").getValue();
                    String time = (String) snapshot.child("time").getValue();
                    String date = (String) snapshot.child("date").getValue();
                    String organisation = (String) snapshot.child("organisation").getValue();
                    String country = (String) snapshot.child("country").getValue();
                    String post_uid = (String) snapshot.child("uid").getValue();


                    post_title.setText(postTitle);
                    post_desc.setText(postDescription);

                    postUserName.setText(displayName);
                    postDate.setText(date);
                    userCountry.setText(country);
                    userOrganisation.setText(organisation);

                    if (snapshot.hasChild("postImage")) {
                        String postImage = (String) snapshot.child("postImage").getValue();
                        post_image.setVisibility(View.VISIBLE);
                        Picasso.with(SinglePostActivity.this).load(postImage).into(post_image);

                    }

                    Picasso.with(SinglePostActivity.this).load(profilePhoto).resize(500, 500)
                            .transform(new CropCircleTransformation())
                            .into(user_image);


                    if (mAuth.getCurrentUser().getUid().equals(post_uid)) {

                        deleteBtn.setVisibility(View.VISIBLE);
                    }


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onStart() {
        //
        super.onStart();

        updateUI();

        FirebaseUser currentUser = mAuth.getCurrentUser();

            //if user is logged in populate the Ui With card views



        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String profPhoto = (String) dataSnapshot.child("profilePhoto").getValue();

                Picasso.with(SinglePostActivity.this).load(profPhoto).resize(500,500)
                        .transform(new CropCircleTransformation())
                        .into(commentingUserImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SinglePostActivity.this, "POSTING...", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
                validate();
            }
        });


    }
    public void  validate(){

        //get the comment from the edit texts
        final String comment = makeComment.getText().toString().trim();
        //get the date and time of the post

        Date date = new Date();

        SimpleDateFormat DateFor = new SimpleDateFormat("E, dd MMM yyyy ");
        final String stringDate = DateFor.format(date);
        // do a check for empty fields
        if (!TextUtils.isEmpty(comment)){

            final DatabaseReference newComment = commentRef.push();
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    newComment.child("comment").setValue(comment);
                    newComment.child("uid").setValue(mCurrentUser.getUid());
                    newComment.child("date").setValue(stringDate);
                    newComment.child("profilePhoto").setValue(dataSnapshot.child("profilePhoto").getValue());
                    newComment.child("displayName").setValue(dataSnapshot.child("displayName").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                           // layoutComment.setVisibility(View.VISIBLE);
                           // FirebaseUser currentUser = mAuth.getCurrentUser();
                           // updateUI(currentUser);

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }


    private void updateUI() {
        //create and initialize an instance of Query that retrieves all posts uploaded
        Query query = FirebaseDatabase.getInstance().getReference().child("PostComments").child(post_key);
        // Create and initialize and instance of Recycler Options passing in your model class and

        String userID = mAuth.getCurrentUser().getUid();
        FirebaseRecyclerOptions<CommentModel> options =
                new FirebaseRecyclerOptions.Builder<CommentModel>()
                        .setQuery(commentRef, CommentModel.class)
                        .build();
        /*
        FirebaseRecyclerOptions<CommentModel> options = new FirebaseRecyclerOptions.Builder<CommentModel>().
                setQuery(query, new SnapshotParser<CommentModel>() {
                    @NonNull
                    @Override
                    //Create a snap shot of your model
                    public CommentModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new CommentModel(snapshot.child("displayName").getValue().toString(),
                                snapshot.child("profilePhoto").getValue().toString(),
                                snapshot.child("comment").getValue().toString(),
                                snapshot.child("date").getValue().toString());

                    }
                })
                .build();

         */
        // crate a fire base adapter passing in the model, an a View holder
        // Create a  new ViewHolder as a public inner class that extends RecyclerView.Holder, outside the create , start and update the Ui methods.
        //Then implement the methods onCreateViewHolder and onBindViewHolder
        //Complete all the steps in the AtticViewHolder before proceeding to  the methods onCreateViewHolder, and onBindViewHolder
        adapter = new FirebaseRecyclerAdapter<CommentModel, SinglePostActivity.commentModelViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull SinglePostActivity.commentModelViewHolder holder, int i, @NonNull CommentModel model) {
                final String comment_key = getRef(i).getKey();
              //  holder.setUserName(model.getDisplayName());
            //    holder.setProfilePhoto(getApplicationContext(), model.getProfilePhoto());

              //  holder.setDate(model.getDate());
               // holder.setComment(model.getComment());

                commentRef.child(comment_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                             String displayName = snapshot.child("displayName").getValue().toString();
                            String profilePhoto =  snapshot.child("profilePhoto").getValue().toString();
                            String comment = snapshot.child("comment").getValue().toString();
                            String date = snapshot.child("date").getValue().toString();
                            String postedBy = snapshot.child("uid").getValue().toString();

                            if(userID.equals(postedBy)) {

                              holder.deleteComment.setVisibility(View.VISIBLE);
                              holder.deleteComment.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      commentRef.child(comment_key).removeValue();
                                  }
                              });
                            }

                           holder.commenterName.setText(displayName);

                            Picasso.with(SinglePostActivity.this).load(profilePhoto).resize(500,500)
                                    .transform(new CropCircleTransformation())
                                    .into(holder.commenterimage);

                            holder.commentDate.setText(date);
                           holder.the_comment .setText(comment);

                           // adapter.notifyDataSetChanged();

                        }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
/*

                if (comment_key != null) {
                    commentRef.child(comment_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String userID = snapshot.child("uid").getValue().toString();

                                if (mAuth.getCurrentUser().getUid().equals(userID)) {
                                    holder.deleteComment.setVisibility(View.VISIBLE);
                                }
                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }*/




            }


            @NonNull
            @Override
            public SinglePostActivity.commentModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //inflate the layout where you have the card view items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
                return new SinglePostActivity.commentModelViewHolder(view);
            }


        };
        adapter.notifyDataSetChanged();
         recyclerView.setAdapter(adapter);
        adapter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            adapter.stopListening();

        }

    }


    public static  class commentModelViewHolder extends RecyclerView.ViewHolder{
        //Declare the view objects in the card view

        public TextView commenterName;
        public ImageView commenterimage;
        public TextView commentTime;
        public TextView commentDate;
        public TextView  the_comment;
        private TextView deleteComment;

        //Declare a string variable to hold  the user ID of currently logged in user
        String currentUserID;
        //Declare an instance of firebase authentication
        FirebaseAuth mAuth;
        //Declare a database reference where you are saving  the likes
        DatabaseReference likesRef;
        //create constructor matching super
        public commentModelViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects

            commenterName = itemView.findViewById(R.id.commenterName);
            commenterimage = itemView.findViewById(R.id.commenterImage);

            commentDate = itemView.findViewById(R.id.commentDate);
            the_comment = itemView.findViewById(R.id.the_comment);
            deleteComment=itemView.findViewById(R.id.delete_comment);
            // deleteComment.setVisibility(View.INVISIBLE);

            commentDate.setVisibility(View.VISIBLE);

        }
        // create yos setters, you will use this setter in you onBindViewHolder method
        public void setComment(String comment){

            the_comment.setText(comment);
        }


        public void setUserName(String displayName){

            commenterName.setText(displayName);
        }
        public void  setProfilePhoto(Context context, String profilePhoto){

            Picasso.with(context).load(profilePhoto).resize(500,500)
                    .transform(new CropCircleTransformation())
                    .into(commenterimage);



        }

        public void setDate(String date) {


            commentDate.setText(date);
        }

    }
    @Override
    public void onBackPressed()
    {
        Intent myIntent=new Intent(SinglePostActivity.this, BlogDisplayActivity.class);
        startActivity(myIntent);
        super.onBackPressed();  // optional depending on your needs

    }
}