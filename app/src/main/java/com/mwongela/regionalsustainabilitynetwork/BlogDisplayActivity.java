package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.transition.Hold;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class BlogDisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference likesRef, postsRef;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;
    Boolean likeChecker =false;
    FirebaseRecyclerAdapter<Attic, BlogDisplayActivity.AtticViewHolder> adapter;
    String currentUserID =null;
    private ImageView profPic;
    //private  ProgressBar progressBar;
   // private RelativeLayout layout;
    private boolean isGPS = false;
   // private Boolean myGPS=false
   private boolean isFragmentDisplayed = false;
    static final String STATE_FRAGMENT ="state_of_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_display);
        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
/*
        layout = findViewById(R.id.blogDisplay);
        progressBar = new ProgressBar(BlogDisplayActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.VISIBLE);

 */



        profPic=findViewById(R.id.uImage);
        //initialize recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse  the layout so as to display the most recent post at the top
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);




        TextView postAdder = findViewById(R.id.articulate);
        //layout = findViewById(R.id.blogDisplay);


        //get the database reference where you will fetch posts
        // mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        //Initialize the database reference where you will store likes
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        //get an instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(BlogDisplayActivity.this, RegisterActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        //Initialize the instance of the firebase user
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {

                                                     String profPhoto = (String) dataSnapshot.child("profilePhoto").getValue();

                                                     Picasso.with(BlogDisplayActivity.this).load(profPhoto).resize(500,500)
                                                             .transform(new CropCircleTransformation())
                                                             .into(profPic);
                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                 }
                                             });

        postAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent articulate = new Intent(BlogDisplayActivity.this, PostActivity.class);
               // articulate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
              startActivity(articulate);
             // finish();


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //if user is logged in populate the Ui With card views
            loadData(currentUser);
            adapter.startListening();

        }


    }




    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            adapter.stopListening();

        }
    }





    public void loadData(final FirebaseUser currentUser) {
      /*  FirebaseRecyclerOptions<Attic> options =
                new FirebaseRecyclerOptions.Builder<Attic>()
                        .setQuery(postsRef, Attic.class)
                        .build();

       */
        Query query = FirebaseDatabase.getInstance().getReference().child("Posts");
        FirebaseRecyclerOptions<Attic> options = new FirebaseRecyclerOptions.Builder<Attic>().
                setQuery(query, new SnapshotParser<Attic>() {
                    @NonNull
                    @Override
                    //Create a snap shot of your model
                    public Attic parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Attic(snapshot.child("title").getValue().toString(),
                                snapshot.child("desc").getValue().toString(),
                                snapshot.child("displayName").getValue().toString(),
                                snapshot.child("profilePhoto").getValue().toString(),
                                snapshot.child("date").getValue().toString(),
                                snapshot.child("organisation").getValue().toString(),
                                snapshot.child("country").getValue().toString());

                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter<Attic, BlogDisplayActivity.AtticViewHolder>(options) {


            @NonNull
            @Override
            public AtticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);

                return new BlogDisplayActivity.AtticViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AtticViewHolder holder, int position, @NonNull Attic attic) {
                final String post_key = getRef(position).getKey();

                holder.setOrganisation(attic.getOrganisation());
                holder.setCountry(attic.getCountry());
                holder.setDate(attic.getDate());
                holder.setDisplayName(attic.getDisplayName());
                holder.setProfilePhoto(getApplicationContext(), attic.getProfilePhoto());
                holder.setTitle(attic.getTitle());
                holder.setDesc(attic.getDesc());
                holder.setLikeButtonStatus(post_key);

                holder.post_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //launch the screen single post activity on clicking a particular cardview item
                        //create this activity using the empty activity template
                        Intent singleActivity = new Intent(BlogDisplayActivity.this, SinglePostActivity.class);
                        singleActivity.putExtra("PostID", post_key);
                        startActivity(singleActivity);
                        ;
                    }
                });

                holder.commentPostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleActivity = new Intent(BlogDisplayActivity.this, SinglePostActivity.class);
                        singleActivity.putExtra("PostID", post_key);
                        startActivity(singleActivity);
                        //finish();

                    }
                });


                holder.likePostButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // initialize the like checker to true, we are using this boolean variable to determine if a post has been liked or dislike
                        // we declared this variable on to of our activity class
                        likeChecker = true;
                        //check the currently logged in user using his/her ID
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            currentUserID = user.getUid();
                        } else {
                            Toast.makeText(BlogDisplayActivity.this, R.string.please_login, Toast.LENGTH_SHORT).show();

                        }
                        //Listen to changes in the likes database reference
                        likesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (likeChecker.equals(true)) {
                                    // if the current post has a like, associated to the current logged and the user clicks on it again, remove the like
                                    //basically the user is disliking
                                    if (dataSnapshot.child(post_key).hasChild(currentUserID)) {
                                        likesRef.child(post_key).child(currentUserID).removeValue();
                                        likeChecker = false;
                                    } else {
                                        //here the user is liking, set value on the like
                                        likesRef.child(post_key).child(currentUserID).setValue(true);
                                        likeChecker = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

                postsRef.child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                                    /*

                                    String postTitle = (String) snapshot.child("title").getValue();
                                    String postDescription = (String) snapshot.child("desc").getValue();
                                    String displayName = (String) snapshot.child("displayName").getValue();
                                    String profilePhoto = (String) snapshot.child("profilePhoto").getValue();
                                    String date = (String) snapshot.child("date").getValue();
                                    String organisation = (String) snapshot.child("organisation").getValue();
                                    String country = (String) snapshot.child("country").getValue();
                                    holder.setLikeButtonStatus(post_key);
                                    holder.post_title.setText(postTitle);
                                    holder.post_desc.setText(postDescription);
                                    holder.userCountry.setText(country);
                                    holder.userOrganisation.setText(organisation);

                                     */


                            if (snapshot.hasChild("postImage")) {
                                String postImage = (String) snapshot.child("postImage").getValue();
                                holder.post_image.setVisibility(View.VISIBLE);
                                Picasso.with(BlogDisplayActivity.this).load(postImage).into(holder.post_image);

                            }

/*
                                    holder.postUserName.setText(displayName);

                                    Picasso.with(BlogDisplayActivity.this).load(profilePhoto).resize(500, 500)
                                            .transform(new CropCircleTransformation())
                                            .into(holder.user_image);

                                    holder.postDate.setText(date);

 */


                        }
                        holder.likePostButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // initialize the like checker to true, we are using this boolean variable to determine if a post has been liked or dislike
                                // we declared this variable on to of our activity class
                                likeChecker = true;
                                //check the currently logged in user using his/her ID
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    currentUserID = user.getUid();
                                } else {
                                    Toast.makeText(BlogDisplayActivity.this, R.string.please_login, Toast.LENGTH_SHORT).show();

                                }
                                //Listen to changes in the likes database reference
                                likesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (likeChecker.equals(true)) {
                                            // if the current post has a like, associated to the current logged and the user clicks on it again, remove the like
                                            //basically the user is disliking
                                            if (dataSnapshot.child(post_key).hasChild(currentUserID)) {
                                                likesRef.child(post_key).child(currentUserID).removeValue();
                                                likeChecker = false;
                                            } else {
                                                //here the user is liking, set value on the like
                                                likesRef.child(post_key).child(currentUserID).setValue(true);
                                                likeChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onDataChanged() {
                // Called each time there is a new data snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

              //  progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(DatabaseError e) {
                // Called when there is an error getting data. You may want to update
                // your UI to display an error message to the user.

                // ...
            }

            ;



        };
    }

    public class AtticViewHolder extends RecyclerView.ViewHolder{
        //Declare the view objects in the card view
        public final TextView post_title;
        public final TextView post_desc;
        public final ImageView post_image;
        public final TextView postUserName;
        public final ImageView user_image;
        public final TextView postTime;
        public final TextView postDate;
        public final LinearLayout post_layout;
        public final ImageButton likePostButton;
        public final ImageButton commentPostButton;
        public final TextView displayLikes;

        public final ProgressBar loadPhoto;
        public final RelativeLayout relativeLayout;
        public final TextView userOrganisation;
        public final TextView userCountry;


        //Declare an int variable to hold the count  of likes
        int countLikes;
        //Declare a string variable to hold  the user ID of currently logged in user
        String currentUserID;
        //Declare an instance of firebase authentication
        FirebaseAuth mAuth;
        //Declare a database reference where you are saving  the likes
        final DatabaseReference likesRef;
        //create constructor matching super
        public AtticViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            post_title = itemView.findViewById(R.id.post_title_txtview);
            post_desc = itemView.findViewById(R.id.post_desc_txtview);
            post_image = itemView.findViewById(R.id.post_image);
            postUserName = itemView.findViewById(R.id.post_user);
            user_image = itemView.findViewById(R.id.userImage);
            postTime = itemView.findViewById(R.id.time);

            postDate = itemView.findViewById(R.id.textView_date);
            userCountry=itemView.findViewById(R.id.textView_country);
            userOrganisation=itemView.findViewById(R.id.textView_organisation);




            post_layout = itemView.findViewById(R.id.linear_layout_post);
            relativeLayout=itemView.findViewById(R.id.progress);
            likePostButton = itemView.findViewById(R.id.like_button);
            commentPostButton = itemView.findViewById(R.id.comment);
            displayLikes = itemView.findViewById(R.id.likes_display);


            post_image.setVisibility(View.GONE);

            //Initialize a database reference where you will store  the likes
            likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            loadPhoto = new ProgressBar(BlogDisplayActivity.this, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
             params.addRule(RelativeLayout.CENTER_IN_PARENT);
             relativeLayout.addView(loadPhoto, params);
               loadPhoto.setVisibility(View.GONE);

        }
        public void setProfilePhoto(Context context, String profilePhoto) {

            Picasso.with(context).load(profilePhoto).resize(500, 500)
                    .transform(new CropCircleTransformation())
                    .into(user_image);
        }

        public void setDisplayName(String displayName) {

            postUserName.setText(displayName);

              }

        public void setTitle(String title) {

          post_title.setText(title);
        }

        public void setDesc(String desc) {

            post_desc.setText(desc);
        }
        public void setDate(String date) {

          postDate.setText(date);
        }
        public  void setCountry(String country) {
            userCountry.setText(country);
        }
        public  void setOrganisation(String organisation){
           userOrganisation.setText(organisation);
        }
       public void setPostImage(String postImage) {


       }
            public void setLikeButtonStatus(final String post_key){
            //we want to know who has like a particular post, so let's get the user using their user_ID
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                currentUserID = user.getUid();
            } else {
                Toast.makeText(BlogDisplayActivity.this,R.string.please_login,Toast.LENGTH_SHORT).show();
            }

            //mAuth = FirebaseAuth.getInstance();
            //  currentUserID=mAuth.getCurrentUser().getUid();

            // Listen to changes in the database reference of Likes
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //define post_key in the in the onBindViewHolder method
                    //check if a particular post has been liked
                    if(dataSnapshot.child(post_key).hasChild(currentUserID)){
                        //if liked get the number of likes
                        countLikes=(int) dataSnapshot.child(post_key).getChildrenCount();
                        //check the image from initiali sislike to like
                        likePostButton.setImageResource(R.drawable.like);
                        // count the like and display them in the textView for likes
                        displayLikes.setText(Integer.toString(countLikes));
                    }else {
                        //If disliked, get the current number of likes
                        countLikes=(int) dataSnapshot.child(post_key).getChildrenCount();
                        // set the image resource as disliked
                        likePostButton.setImageResource(R.drawable.dislike);
                        //display the current number of likes
                        displayLikes.setText(Integer.toString(countLikes));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
                  // on clicking log out, log the user out
       if (id == R.id.logout){
            mAuth.signOut();
            Intent logouIntent = new Intent(BlogDisplayActivity.this, LoginActivity.class);
            logouIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logouIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        // FirebaseUser currentUser;
                        boolean isContinue = true;
                       // Intent articulate = new Intent(BlogDisplayActivity.this, PostActivity.class);
                      //  startActivity(articulate);

                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        //Log.i(TAG, "onActivityResult: User rejected GPS request");
                        Toast.makeText(BlogDisplayActivity.this, R.string.please_turn_gps, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);

                        break;
                    default:
                        break;
                }
                break;
        }
    }
    @Override
    public void onBackPressed()
    {
        Intent myIntent=new Intent(BlogDisplayActivity.this, MainActivity.class);
        startActivity(myIntent);
        super.onBackPressed();  // optional depending on your needs

    }

}