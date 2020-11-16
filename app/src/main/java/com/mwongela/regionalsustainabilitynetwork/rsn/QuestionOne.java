package com.mwongela.regionalsustainabilitynetwork.rsn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
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
import com.mwongela.regionalsustainabilitynetwork.AppConstants;
import com.mwongela.regionalsustainabilitynetwork.FetchAddressTask;
import com.mwongela.regionalsustainabilitynetwork.LoginActivity;
import com.mwongela.regionalsustainabilitynetwork.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionOne extends AppCompatActivity  implements FetchAddressTask.onTaskCompleted {

    //Views
    private TextView mLocationTextView;


    //location classes
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mTrackingLocation;
    private LocationCallback mLocationCallBack;
    // Constants
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TRACKING_LOCATION_KEY = "tracking location";

    private FirebaseAuth mAuth;
    private TextInputEditText numberOfMen, numberOfWomen, numberOfYouth;
    private TextView eventTitle;
    String event_name = null;
    String event_id = null;
    private ImageButton imageButton;
    private Uri event_image_uri = null;
    private final static int GALLERY_REQ = 1;
    private StorageReference mStorageRef;
    private FirebaseUser mCurrentUser;
    private DatabaseReference surveyRef,mDatabaseUsers;
    private ProgressBar progressBar;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_one);
        mAuth = FirebaseAuth.getInstance();

        event_id = getIntent().getExtras().getString("EventID");
        event_name = getIntent().getExtras().getString("EventName");
        numberOfMen = findViewById(R.id.number_of_men);
        numberOfWomen = findViewById(R.id.number_of_women);

        numberOfYouth = findViewById(R.id.number_of_youth);
        eventTitle = findViewById(R.id.event_title);
        imageButton = findViewById(R.id.imagebuttonEvent);
        mLocationTextView=findViewById(R.id.location_textView);
        layout = findViewById(R.id.display);
        progressBar = new ProgressBar(QuestionOne.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(QuestionOne.this, LoginActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        mStorageRef = FirebaseStorage.getInstance().getReference().child("event_images");

        surveyRef = FirebaseDatabase.getInstance().getReference().child("Reports");

        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                //set the type to images only
                galleryIntent.setType("image/*");
                //since we need results, use the method  startActivityForResult() and pass the intent and request code you initialized
                startActivityForResult(galleryIntent, GALLERY_REQ);
            }
        });

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(QuestionOne.this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        QuestionOne.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });

    }

    public void validate() {
        boolean valid = true;
        String men = numberOfMen.getText().toString();
        String women = numberOfWomen.getText().toString();

        String youth = numberOfYouth.getText().toString();
        final String mLocation=mLocationTextView.getText().toString().trim();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy ");
        final String stringDate =dateFormat.format(date);

        Date imageDate = new Date();

        SimpleDateFormat dateFor = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

        String postRandomName = dateFor.format(imageDate);


        if (men.isEmpty()) {
            numberOfMen.setError("Required");
            valid = false;
        } else {
            numberOfMen.setError(null);
        }
        if (women.isEmpty()) {
            numberOfWomen.setError("Required");
            valid = false;
        } else {
            numberOfWomen.setError(null);
        }

        if (youth.isEmpty()) {
            numberOfYouth.setError("Required");
            valid = false;
        } else {
            numberOfYouth.setError(null);
        }


        if (valid) {


            if (event_image_uri != null) {

                //create Storage reference node, inside profile_image storage reference where you will save the profile image
                StorageReference eventImagePath = mStorageRef.child("event_images").child(event_image_uri.getLastPathSegment()+ postRandomName+".jpg");
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), event_image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                assert bmp != null;
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask2 = eventImagePath.putBytes(data);




                               uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //if the upload of the profile image was successful get the download url
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                //get the download url from your storage use the methods getStorage() and getDownloadUrl()
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                //call the method addOnSuccessListener to determine if we got the download url
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //convert the uri to a string on success
                                        final String eventImage = uri.toString();
                                        // call the method push() to add values on the database reference of  a specif user
                                        final DatabaseReference newSurvey = surveyRef.push();

                                        //call the method addValueEventListener to publish the additions in  the database reference of a specific user
                                        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //add the profilePhoto and displayName for the current user
                                                newSurvey.child("QuestionOne").child("men").setValue(men);
                                                newSurvey.child("QuestionOne").child("women").setValue(women);
                                                newSurvey.child("QuestionOne").child("youth").setValue(youth);
                                                newSurvey.child("eventName").setValue(event_name);
                                                newSurvey.child("eventID").setValue(event_id);
                                                newSurvey.child("eventPhoto").setValue(eventImage);
                                                newSurvey.child("location").setValue(mLocation);
                                                newSurvey.child("date").setValue(stringDate);
                                                newSurvey.child("UID").setValue(mCurrentUser.getUid());
                                                newSurvey.child("organisation").setValue(dataSnapshot.child("organisation").getValue());
                                                newSurvey.child("country").setValue(dataSnapshot.child("Country").getValue());
                                                newSurvey.child("profilePhoto").setValue(dataSnapshot.child("profilePhoto").getValue());
                                                newSurvey.child("filled by").setValue(dataSnapshot.child("displayName").getValue())
                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //show a toast to indicate the profile was updated
                                                            // Toast.makeText(ProfileActivity.this, " Profile Created", Toast.LENGTH_SHORT).show();
                                                            //launch the login activity
                                                            final String postKey=newSurvey.getKey();

                                                            progressBar.setVisibility(View.GONE);
                                                            Intent next = new Intent(QuestionOne.this, QuestionThree.class);
                                                            next.putExtra("EventID",event_id);
                                                            next.putExtra("PostKey",postKey);
                                                            startActivity(next);
                                                            // finish();
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                });
                            }
                        }

                    }
                });

            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(QuestionOne.this, "Please Add event photo", Toast.LENGTH_SHORT).show();
            }

        }
                }


    @Override
    public void onStart() {
        super.onStart();

        eventTitle.setText(event_name);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabQ1);
        if (currentUser  !=null) {


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    validate();

                }
            });

        }
    }

    private void getLocation() {
        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d("TAG", "getLocation: permissions granted");
        }*/

        if (ActivityCompat.checkSelfPermission( QuestionOne.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(QuestionOne.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QuestionOne.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Start the reverse geocode AsyncTask
                        new FetchAddressTask(QuestionOne.this,
                                QuestionOne.this).execute(location);
                    }
                });
        mLocationTextView.setText(getString(R.string.address_text,
                getString(R.string.loading),
                System.currentTimeMillis()));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this,
                            "location permission denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        //update ui
        // mLocationTextView.setVisibility(true);
        mLocationTextView.setText(getString(R.string.address_text,
                result, System.currentTimeMillis()));


    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ && resultCode == RESULT_OK) {
            //get the image selected by the user
            event_image_uri = data.getData();
            //set in the image button view
            imageButton.setImageURI(event_image_uri);
        }
    }
}