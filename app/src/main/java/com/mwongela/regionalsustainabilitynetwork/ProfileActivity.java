package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {
    // Declare instances if the views
    private String mSpinnerLabel = "";
    private Uri stickyNote = null;
    private ArrayAdapter dataAdapterForSpinner, partnerAdapterForSpinner, countryAdapterForSpinner;
    private TextInputEditText lastName, firstName, designation;
    private ImageButton imageButton;
    private Button doneBtn;
    // Declare an instance of firebase authentication
    private FirebaseAuth mAuth;
    //Declare an Instance of the database reference  where we will be saving the profile photo and custom display name
    private DatabaseReference mDatabaseUser;
    //Declare an Instance of the Storage reference where we will upload the photo
    private StorageReference mStorageRef;
    // Declare an Instance of URI for getting the image from our phone, initialize it to null
    private Uri profileImageUri = null;
    //  since we want to get a result (getting and setting image) we will start the implicit intent using the method startActivityForResult()
    //startActivityForResult require two arguments the intent and the request code

    // Declare  and initialize  a private final static int  that will serve as our request code
    private final static int GALLERY_REQ = 1;
    private TextInputEditText organisation_input;
    private  TextInputEditText subject_input;
    private  TextInputEditText country_input;

    private  Spinner spinnerSubject;
    private  Spinner spinnerOrganisation;
    private  Spinner spinnerCountry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);


        spinnerSubject = (Spinner) findViewById(R.id.subject_spinner);
        subject_input = findViewById(R.id.input_subject);
        spinnerOrganisation = (Spinner) findViewById(R.id.spinner_organisation);
        organisation_input = findViewById(R.id.input_subject_organisation);
       spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
       country_input = findViewById(R.id.input_subject_country);



        //Initialize the  instances of the views
        lastName = findViewById(R.id.lName);
        firstName = findViewById(R.id.fName);
        designation = findViewById(R.id.designation);

        imageButton = findViewById(R.id.imagebutton);
        doneBtn = findViewById(R.id.doneBtn);
        //Initialize the instance of Firebase authentications
        mAuth = FirebaseAuth.getInstance();
        //We want to set the image and  on a specific user_ID we registered , hence get the user id of the current user and assign it to a string variable
        final String userID = mAuth.getCurrentUser().getUid();
        //Initialize the database reference where you have your registered users
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        //Initialize the firebase storage reference where you will store the profile  photo images
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
        //set on click listener on the image button so as to allow users to pick their  profile photo from their gallery
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an implicit intent for getting the images
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                //set the type to images only
                galleryIntent.setType("image/*");
                //since we need results, use the method  startActivityForResult() and pass the intent and request code you initialized
                startActivityForResult(galleryIntent, GALLERY_REQ);
            }
        });


        final String[] subjets = getResources().getStringArray(R.array.gender);

        dataAdapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjets);
        dataAdapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(dataAdapterForSpinner);

        subject_input.setKeyListener(null);

        subject_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerSubject.setVisibility(View.VISIBLE);
                spinnerSubject.performClick();
            }
        });

        subject_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    spinnerSubject.setVisibility(View.VISIBLE);
                    spinnerSubject.performClick();
                } else {
                    spinnerSubject.setVisibility(View.GONE);
                }
            }
        });

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject_input.setText(spinnerSubject.getSelectedItem().toString()); //this is taking the first value of the spinner by default.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                subject_input.setText(" ");
            }
        });



        final String[] organisations = getResources().getStringArray(R.array.partners);

        partnerAdapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, organisations);
        partnerAdapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrganisation.setAdapter(partnerAdapterForSpinner);

        organisation_input.setKeyListener(null);

        organisation_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerOrganisation.setVisibility(View.VISIBLE);
                spinnerOrganisation.performClick();
            }
        });

        organisation_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    spinnerOrganisation.setVisibility(View.VISIBLE);
                    spinnerOrganisation.performClick();
                } else {
                    spinnerOrganisation.setVisibility(View.GONE);
                }
            }
        });

        spinnerOrganisation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organisation_input.setText(spinnerOrganisation.getSelectedItem().toString()); //this is taking the first value of the spinner by default.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                organisation_input.setText("");
            }
        });


        //final Spinner spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
        //final TextInputEditText country_input = findViewById(R.id.input_subject_country);
        final String[] countries = getResources().getStringArray(R.array.countries);

        countryAdapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        countryAdapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapterForSpinner);

        country_input.setKeyListener(null);

        country_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerCountry.setVisibility(View.VISIBLE);
                spinnerCountry.performClick();
            }
        });

        country_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    spinnerCountry.setVisibility(View.VISIBLE);
                    spinnerCountry.performClick();
                } else {
                    spinnerCountry.setVisibility(View.GONE);
                }
            }
        });

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_input.setText(spinnerCountry.getSelectedItem().toString()); //this is taking the first value of the spinner by default.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                country_input.setText("");
            }
        });






        // on clicking the images we want to get the name and the profile photo, then later save this on a database reference for users
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the custom display name entered by the user
                validate();
            }
        });
    }



    public void validate() {
        boolean valid = true;


        final String lName = lastName.getText().toString().trim();
        final String fName = firstName.getText().toString().trim();
        final String uDesignation = designation.getText().toString().trim();
        // final String spinnerLabel=mSpinnerLabel.trim();
        final String organisationLabel = organisation_input.getText().toString().trim();
        final String genderLabel = subject_input.getText().toString().trim();
        final String countryLabel = country_input.getText().toString().trim();
        //validate to ensure that the fields and profile image are not null
        if (lName.isEmpty()) {
            lastName.setError("Required");
            valid = false;
        } else {
            lastName.setError(null);
        }
        if (fName.isEmpty()) {
            firstName.setError("Required");
            valid = false;
        } else {
            firstName.setError(null);
        }
        if (uDesignation.isEmpty()) {
            designation.setError("Required");
            valid = false;
        } else {
            designation.setError(null);
        }

        if (organisationLabel.isEmpty()) {
            organisation_input.setError("Required");
            valid = false;
        } else {
            organisation_input.setError(null);
        }
        if (genderLabel.isEmpty()) {
            subject_input.setError("Required");
            valid = false;
        } else {
            subject_input.setError(null);
        }
        if (countryLabel.isEmpty()) {
            country_input.setError("Required");
            valid = false;
        } else {
            country_input.setError(null);
        }

        if (valid) {



            if (profileImageUri != null) {

                //create Storage reference node, inside profile_image storage reference where you will save the profile image
                StorageReference profileImagePath = mStorageRef.child("profile_images").child(profileImageUri.getLastPathSegment());
                //call the putFile() method passing the profile image the user set on the storage reference where you are uploading the image
                //further call addOnSuccessListener on the reference to listen if the upload task was successful,and get a snapshot of the task
                profileImagePath.putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                                        final String profileImage = uri.toString();
                                        // call the method push() to add values on the database reference of  a specif user
                                        mDatabaseUser.push();
                                        //call the method addValueEventListener to publish the additions in  the database reference of a specific user
                                        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //add the profilePhoto and displayName for the current user
                                                mDatabaseUser.child("displayName").setValue(lName);
                                                mDatabaseUser.child("firstName").setValue(fName);
                                                mDatabaseUser.child("Designation").setValue(uDesignation);
                                                mDatabaseUser.child("gender").setValue(genderLabel);
                                                mDatabaseUser.child("organisation").setValue(organisationLabel);
                                                mDatabaseUser.child("Country").setValue(countryLabel);
                                                mDatabaseUser.child("profilePhoto").setValue(profileImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //show a toast to indicate the profile was updated
                                                           // Toast.makeText(ProfileActivity.this, " Profile Created", Toast.LENGTH_SHORT).show();
                                                            //launch the login activity
                                                            Intent login = new Intent(ProfileActivity.this, LoginActivity.class);
                                                            //login.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                            startActivity(login);
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
                Toast.makeText(ProfileActivity.this, "Please Add your profile photo", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    //override this method to get the  profile image and  set it in the image button view
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ && resultCode == RESULT_OK) {
            //get the image selected by the user
            profileImageUri = data.getData();
            //set in the image button view
            imageButton.setImageURI(profileImageUri);
        }
    }


}
