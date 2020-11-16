package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private Uri stickyNote = null;
    private TextInputEditText lastName, firstName, designation;
    private ImageButton imageButton;

    private DatabaseReference mDatabaseUser;

    private StorageReference mStorageRef;

    private Uri profileImageUri = null;

    private final static int GALLERY_REQ = 1;
    private TextInputEditText organisation_input;
    private  TextInputEditText subject_input;
    private  TextInputEditText country_input;

    private  Spinner spinnerSubject;
    private  Spinner spinnerOrganisation;
    private  Spinner spinnerCountry;

    private FirebaseAuth mAuth;
    private  ProgressBar progressBar;
    private RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        layout = findViewById(R.id.display);
        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);

      // progressBar = (ProgressBar) findViewById(R.id.progressbar);
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

        imageButton = findViewById(R.id.imageButton);

        progressBar = new ProgressBar(ProfileActivity.this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);

     mAuth = FirebaseAuth.getInstance();

        final String userID = mAuth.getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_images");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, GALLERY_REQ);
            }
        });


        final String[] subjets = getResources().getStringArray(R.array.gender);

        ArrayAdapter dataAdapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjets);
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

        ArrayAdapter partnerAdapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, organisations);
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

        ArrayAdapter countryAdapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
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

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Button doneBtn = findViewById(R.id.doneBtn);


        if (currentUser != null) {


            doneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //get the custom display name entered by the user
                    Toast.makeText(ProfileActivity.this,"Processing...",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.VISIBLE);
                    // fab.setAlpha(0f)
                    validate();

                }
            });

        }
    }






    public void validate()  {
        boolean valid = true;


        final String lName = lastName.getText().toString().trim();
        final String fName = firstName.getText().toString().trim();
        final String uDesignation = designation.getText().toString().trim();
        // final String spinnerLabel=mSpinnerLabel.trim();
        final String organisationLabel = organisation_input.getText().toString().trim();
        final String genderLabel = subject_input.getText().toString().trim();
        final String countryLabel = country_input.getText().toString().trim();

        Date imageDate = new Date();

        SimpleDateFormat dateFor = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

        String postRandomName = dateFor.format(imageDate);




        //validate to ensure that the fields and profile image are not null
        if (lName.isEmpty()) {
            lastName.setError("Required");
            progressBar.setVisibility(View.GONE);
            valid = false;
        } else {
            lastName.setError(null);
        }
        if (fName.isEmpty()) {
            firstName.setError("Required");
            progressBar.setVisibility(View.GONE);
            valid = false;
        } else {
            firstName.setError(null);
        }
        if (uDesignation.isEmpty()) {
            designation.setError("Required");
            progressBar.setVisibility(View.GONE);
            valid = false;
        } else {
            designation.setError(null);
        }

        if (organisationLabel.isEmpty()) {
            organisation_input.setError("Required");
            progressBar.setVisibility(View.GONE);
            valid = false;
        } else {
            organisation_input.setError(null);
        }
        if (genderLabel.isEmpty()) {
            subject_input.setError("Required");
            progressBar.setVisibility(View.GONE);
            valid = false;
        } else {
            subject_input.setError(null);
        }
        if (countryLabel.isEmpty()) {
            country_input.setError("Required");
            progressBar.setVisibility(View.GONE);
            valid = false;
        } else {
            country_input.setError(null);
        }

        if (valid) {


            if (profileImageUri != null) {


                StorageReference profileImagePath = mStorageRef.child("profile_images").child(profileImageUri.getLastPathSegment()+ postRandomName+".jpg");

                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), profileImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                assert bmp != null;
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask2 = profileImagePath.putBytes(data);

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
                                                            progressBar.setVisibility(View.GONE);

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
                progressBar.setVisibility(View.GONE);
            }

        }
    }

    @Override
    //override this method to get the  profile image and  set it in the image button view
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ && resultCode == RESULT_OK) {
            //get the image selected by the user
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(ProfileActivity.this);
        }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    profileImageUri = result.getUri();
                    imageButton.setImageURI(profileImageUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }


        }
    }



