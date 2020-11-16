package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mwongela.regionalsustainabilitynetwork.rsn.QuestionOne;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PostActivity extends AppCompatActivity {
    //Views



    // Declare the view objects
    private ImageButton imageBtn;
    private EditText textTitle;
    private EditText textDesc;
    private Button postBtn;

    //Declare an Instance of the Storage reference where we will upload the post photo
    private StorageReference mStorageRef;
    //Declare an Instance of the database reference  where we will be saving the post details
    private DatabaseReference databaseRef;
    //Declare an Instance of the database reference  where we have user details
    private DatabaseReference mDatabaseUsers;
    //Declare a Instance of currently logged in user
    private FirebaseUser mCurrentUser;
    // Declare  and initialize  a private final static int  that will serve as our request code


    private static final int GALLERY_REQUEST_CODE = 2;
    // Declare an Instance of URI for getting the image from our phone, initialize it to null
    private Uri uri = null;

   private TextView mLocationTextView;
    private ProgressBar progressBar;
    private RelativeLayout layout;

    @Override
    protected void onStart() {

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                validate();



            }
        });

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mLocationTextView = findViewById(R.id.location);


        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // initializing  view objects
        postBtn = findViewById(R.id.postBtn);
        textDesc = findViewById(R.id.textDesc);
        textTitle = findViewById(R.id.textTitle);

        layout = findViewById(R.id.display);
        progressBar = new ProgressBar(PostActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);



        //Initialize the storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Initialize the database reference/node where you will be storing posts
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        //Initialize an instance of  Firebase Authentication
        //Declare an Instance of firebase authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        // initialize the image button
        imageBtn = findViewById(R.id.imgBtn);
        //picking image from gallery
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });









    }
        //getLocation();

        // posting to Firebase




    @Override
    // image from gallery result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            //get the image selected by the user
            uri = data.getData();
            //set the image
            imageBtn.setImageURI(uri);
        }

    }
    private void validate() {
        // mLocationTextView.setText(getString(R.string.time_post, System.currentTimeMillis()));
        //  Toast.makeText(PostActivity.this, "POSTING...", Toast.LENGTH_LONG).show();
        boolean valid = true;
        //get title and desc from the edit texts
        final String PostTitle = textTitle.getText().toString().trim();
        final String PostDesc = textDesc.getText().toString().trim();


        //long millis=System.currentTimeMillis();
        //java.util.Date date=new java.util.Date(millis);
        //Date date=java.util.Calendar.getInstance().getTime();

        Date date = new Date();

        SimpleDateFormat DateFor = new SimpleDateFormat("E, dd MMM yyyy ");
        final String stringDate = DateFor.format(date);

        Date imageDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");


        String postRandomName = dateFormat.format(imageDate);

        //final String mTime=mLocationTextView.getText().toString().trim();


        if (PostTitle.isEmpty()) {
            textTitle.setError("Required");
            progressBar.setVisibility(View.GONE);
            valid = false;
        } else {
            textTitle.setError(null);
        }
        if (PostDesc.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            textDesc.setError("Required");
            valid = false;
        } else {
            textDesc.setError(null);
        }


        //get the date and time of the post


        if (valid) {
            // do a check for empty fields
            final DatabaseReference newPost = databaseRef.push();
            String imageUrl = null;
            //create Storage reference node, inside pOST_image storage reference where you will save the post image
            if(uri!=null) {
                StorageReference filepath = mStorageRef.child("post_images").child(uri.getLastPathSegment() + postRandomName + ".jpg");


                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                assert bmp != null;
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask2 = filepath.putBytes(data);

                uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //if the upload of the post image was successful get the download url
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                //get the download url from your storage use the methods getStorage() and getDownloadUrl()
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                //call the method addOnSuccessListener to determine if we got the download url
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //convert the uri to a string on success
                                        String imageUrl = uri.toString();

                                        Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
                                        // call the method push() to add values on the database reference

                                        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                newPost.child("postImage").setValue(imageUrl);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }

                });
            }

            //adding post contents to database reference
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("title").setValue(PostTitle);
                    newPost.child("desc").setValue(PostDesc);
                    newPost.child("uid").setValue(mCurrentUser.getUid());
                    newPost.child("date").setValue(stringDate);
                    newPost.child("organisation").setValue(dataSnapshot.child("organisation").getValue());
                    newPost.child("country").setValue(dataSnapshot.child("Country").getValue());
                    //get the profile photo and display name of the person posting
                    newPost.child("profilePhoto").setValue(dataSnapshot.child("profilePhoto").getValue());
                    newPost.child("displayName").setValue(dataSnapshot.child("displayName").getValue()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //launch the main activity after posting
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(PostActivity.this, "Post created", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PostActivity.this, BlogDisplayActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override

    public void onBackPressed()
    {
        Intent myIntent=new Intent(PostActivity.this, BlogDisplayActivity.class);
        startActivity(myIntent);
        super.onBackPressed();  // optional depending on your needs

    }
}
