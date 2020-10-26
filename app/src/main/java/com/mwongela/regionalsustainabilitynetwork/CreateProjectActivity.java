package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Calendar;

public class CreateProjectActivity extends AppCompatActivity {
    private ProgressBar progress_bar;
    private FloatingActionButton fab;
    private View parent_view;
    private TextInputEditText project_date, project_Goals, project_name, project_activities, project_impact;

    private ArrayAdapter projectCountryAdapterForSpinner, convenerAdapterForSpinner;
    private TextInputEditText projectCountry_input;
    private TextInputEditText convener_input;

    private Spinner spinnerProjectCountry;
    private Spinner spinnerConvener;

    //Declare an Instance of the Storage reference where we will upload the post photo
    private StorageReference mStorageRef;
    //Declare an Instance of the database reference  where we will be saving the post details
    private DatabaseReference databaseRef;
    //Declare an Instance of firebase authentication
    private FirebaseAuth mAuth;
    //Declare an Instance of the database reference  where we have user details
    private DatabaseReference mDatabaseUsers;
    //Declare a Instance of currently logged in user
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        parent_view = findViewById(android.R.id.content);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        fab = (FloatingActionButton) findViewById(R.id.fabSubmitProject);


        project_date = (TextInputEditText) findViewById(R.id.projectStartDate);
        project_Goals = (TextInputEditText) findViewById(R.id.projectGoals);

        project_name = (TextInputEditText) findViewById(R.id.projectName);
        project_activities = (TextInputEditText) findViewById(R.id.projectActivities);
        project_impact = (TextInputEditText) findViewById(R.id.projectImpact);

        spinnerProjectCountry = (Spinner) findViewById(R.id.spinner_project_country);
        projectCountry_input = findViewById(R.id.input_subject_project_country);

        spinnerConvener = (Spinner) findViewById(R.id.spinner_project_convener);
        convener_input = findViewById(R.id.input_subject_project_convener);

        //Initialize the storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Initialize the database reference/node where you will be storing posts
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Projects");
        //Initialize an instance of  Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        // initialize the image button


        final String[] countries = getResources().getStringArray(R.array.countries);

        projectCountryAdapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        projectCountryAdapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProjectCountry.setAdapter(projectCountryAdapterForSpinner);

        projectCountry_input.setKeyListener(null);

        projectCountry_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerProjectCountry.setVisibility(View.VISIBLE);
                spinnerProjectCountry.performClick();
            }
        });

        projectCountry_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    spinnerProjectCountry.setVisibility(View.VISIBLE);
                    spinnerProjectCountry.performClick();
                } else {
                    spinnerProjectCountry.setVisibility(View.GONE);
                }
            }
        });

        spinnerProjectCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                projectCountry_input.setText(spinnerProjectCountry.getSelectedItem().toString()); //this is taking the first value of the spinner by default.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                projectCountry_input.setText("");
            }
        });


        //final Spinner spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
        //final TextInputEditText country_input = findViewById(R.id.input_subject_country);
        final String[] convener = getResources().getStringArray(R.array.partners);

        convenerAdapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, convener);
        convenerAdapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConvener.setAdapter(convenerAdapterForSpinner);

        convener_input.setKeyListener(null);

        convener_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerConvener.setVisibility(View.VISIBLE);
                spinnerConvener.performClick();
            }
        });

        convener_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    spinnerConvener.setVisibility(View.VISIBLE);
                    spinnerConvener.performClick();
                } else {
                    spinnerConvener.setVisibility(View.GONE);
                }
            }
        });

        spinnerConvener.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convener_input.setText(spinnerConvener.getSelectedItem().toString()); //this is taking the first value of the spinner by default.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                convener_input.setText("");
            }
        });



/*


        event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDatePickerLight(event_date);
            }
        });
*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                validate();
            }
        });
    }


    // Input Validation
    public void validate() {
        boolean valid = true;
        String projectDate = project_date.getText().toString();
        String projectName = project_name.getText().toString();
        String projectGoals = project_Goals.getText().toString();
        String projectActivities = project_activities.getText().toString();
        String projectConvener = convener_input.getText().toString();
        String projectCountry = projectCountry_input.getText().toString();
        String projectImpact = project_impact.getText().toString();

        if (projectImpact.isEmpty()) {
            project_impact.setError("Enter Project Impact");
            valid = false;
        } else {
            project_impact.setError(null);
        }

        if (projectDate.isEmpty()) {
            project_date.setError("Enter Project Dates");
            valid = false;
        } else {
            project_date.setError(null);
        }
        if (projectName.isEmpty()) {
            project_name.setError("Enter Project Name");
            valid = false;
        } else {
            project_name.setError(null);
        }
        if (projectGoals.isEmpty()) {
            project_Goals.setError("Enter Project Goals");
            valid = false;
        } else {
            project_Goals.setError(null);
        }
        if (projectActivities.isEmpty()) {
            project_activities.setError("Enter Project Activities ");
            valid = false;
        } else {
            project_activities.setError(null);
        }

        if (projectCountry.isEmpty()) {
            projectCountry_input.setError("Enter Project Country");
            valid = false;
        } else {
            projectCountry_input.setError(null);
        }
        if (projectConvener.isEmpty()) {
            convener_input.setError("Enter Project Convener");
            valid = false;
        } else {
            convener_input.setError(null);
        }

        if (valid) {
            final DatabaseReference newProject = databaseRef.push();
            //adding post contents to database reference
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newProject.child("projectName").setValue(projectName);
                    newProject.child("projectGoals").setValue(projectGoals);
                    newProject.child("projectDate").setValue(projectDate);
                    newProject.child("projectActivities").setValue(projectActivities);
                    newProject.child("projectConvener").setValue(projectConvener);
                    newProject.child("projectCountry").setValue(projectCountry);
                    newProject.child("projectImpact").setValue(projectImpact);
                    newProject.child("postedBy").setValue(mCurrentUser.getUid());
                    //get the profile photo and display name of the person

                    newProject.child("profilePhoto").setValue(dataSnapshot.child("profilePhoto").getValue());
                    newProject.child("displayName").setValue(dataSnapshot.child("displayName").getValue()).

                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //launch the main activity after posting

                                        Intent intent = new Intent(CreateProjectActivity.this, ProjectActivity.class);
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
}
