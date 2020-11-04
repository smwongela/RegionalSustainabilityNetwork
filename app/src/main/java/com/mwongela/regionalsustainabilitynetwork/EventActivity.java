package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.mwongela.regionalsustainabilitynetwork.utils.Tools;


import java.util.Calendar;
import java.util.GregorianCalendar;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progress_bar;
    private FloatingActionButton fab;
    private View parent_view;
    private TextInputEditText event_date, participants, event_name, event_venue;
    private int mYear, mMonth, mDay;

    private ArrayAdapter eventTypeAdapterForSpinner, convenerAdapterForSpinner;
    private TextInputEditText eventType_input;
    private TextInputEditText convener_input;

    private Spinner spinnerEventType;
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
        setContentView(R.layout.activity_event);
        parent_view = findViewById(android.R.id.content);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        event_date = (TextInputEditText) findViewById(R.id.event_date);
        participants = (TextInputEditText) findViewById(R.id.numberOfParticipants);

        event_name = (TextInputEditText) findViewById(R.id.eventName);
        event_venue = (TextInputEditText) findViewById(R.id.venue);

        spinnerEventType = (Spinner) findViewById(R.id.spinner_event_type);
        eventType_input = findViewById(R.id.input_subject_event_type);
        spinnerConvener = (Spinner) findViewById(R.id.spinner_convener);
        convener_input = findViewById(R.id.input_subject_convener);

        //Initialize the storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Initialize the database reference/node where you will be storing posts
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Events");
        //Initialize an instance of  Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        // initialize the image button


        final String[] types = getResources().getStringArray(R.array.events);

        eventTypeAdapterForSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        eventTypeAdapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventType.setAdapter(eventTypeAdapterForSpinner);

        eventType_input.setKeyListener(null);

        eventType_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerEventType.setVisibility(View.VISIBLE);
                spinnerEventType.performClick();
            }
        });

        eventType_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    spinnerEventType.setVisibility(View.VISIBLE);
                    spinnerEventType.performClick();
                } else {
                    spinnerEventType.setVisibility(View.GONE);
                }
            }
        });

        spinnerEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventType_input.setText(spinnerEventType.getSelectedItem().toString()); //this is taking the first value of the spinner by default.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                eventType_input.setText("");
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


        event_date.setOnClickListener(this);
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


/*


    private void dialogDatePickerLight(final TextInputEditText bt) {
        Calendar cur_calender = Calendar.getInstance();

        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    final long today = System.currentTimeMillis() - 1000;

                    long date_ship_millis = calendar.getTimeInMillis();
                    event_date.setText(Tools.getFormattedDateSimple(date_ship_millis));
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)

        );
        //set dark light

        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));

       datePicker.setMinDate(new GregorianCalendar().getTimeInMillis());
        datePicker.show(getSupportFragmentManager(), "Datepickerdialog");
    }
*/

    // Input Validation
    public void validate() {
        boolean valid = true;
        String date = event_date.getText().toString();
        String eventName = event_name.getText().toString();
        String eventVenue = event_venue.getText().toString();
        String eventParticipants = participants.getText().toString();
        String eventConvener = convener_input.getText().toString();
        String eventType = eventType_input.getText().toString();

        if (date.isEmpty()) {
            event_date.setError("Required");
            valid = false;
        } else {
            event_date.setError(null);
        }
        if (eventName.isEmpty()) {
            event_name.setError("Enter Event Name");
            valid = false;
        } else {
            event_name.setError(null);
        }
        if (eventVenue.isEmpty()) {
            event_venue.setError("Enter Venue");
            valid = false;
        } else {
            event_venue.setError(null);
        }
        if (eventParticipants.isEmpty()) {
            participants.setError("Enter expected number of participants ");
            valid = false;
        } else {
            participants.setError(null);
        }

        if (eventType.isEmpty()) {
            eventType_input.setError("Enter event type");
            valid = false;
        } else {
            eventType_input.setError(null);
        }
        if (eventConvener.isEmpty()) {
            convener_input.setError("Enter event Convener");
            valid = false;
        } else {
            convener_input.setError(null);
        }

        if (valid) {
            Toast.makeText(EventActivity.this, R.string.processing, Toast.LENGTH_LONG).show();
            final DatabaseReference newEvent = databaseRef.push();
            //adding post contents to database reference
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newEvent.child("eventName").setValue(eventName);
                    newEvent.child("eventVenue").setValue(eventVenue);
                    newEvent.child("eventDate").setValue(date);
                    newEvent.child("postedBy").setValue(mCurrentUser.getUid());
                    newEvent.child("eventParticipants").setValue(eventParticipants);
                    newEvent.child("eventConvener").setValue(eventConvener);
                    newEvent.child("eventType").setValue(eventType);
                    //get the profile photo and display name of the person
                    newEvent.child("organisation").setValue(dataSnapshot.child("organisation").getValue());
                    newEvent.child("country").setValue(dataSnapshot.child("Country").getValue());
                    newEvent.child("profilePhoto").setValue(dataSnapshot.child("profilePhoto").getValue());
                    newEvent.child("displayName").setValue(dataSnapshot.child("displayName").getValue()).

                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //launch the main activity after posting

                                        Intent intent = new Intent(EventActivity.this, MainActivity.class);
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
    public void onClick(View v) {
        if (v == event_date) {
            //6.2 Declare a calender to get current selected  date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            //6.3 use the date picker dialog, declare a new datapicker dialog
            android.app.DatePickerDialog datePickerdialog = new android.app.DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //6.4 Set the date to the edit text

                    event_date.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                }
            }, mYear, mMonth, mDay);
            //6.5 Show the date picker dialog
            datePickerdialog.show();
        }
    }
}
