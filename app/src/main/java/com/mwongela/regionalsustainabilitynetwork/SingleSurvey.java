package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleSurvey extends AppCompatActivity {


    private ImageView singleSurveyPartnerImage, singleSurveyEventImage,imageSuccess;
    private TextView textViewNationalPolicies, singlePartnerName, singleLocation, singleSurveyDay, singleEventName, numberMen, numberWomen, numberYouth,challenges, policies, successLevel;
    private LinearLayout layoutPolicies, canLayout, cadimLayout,cealLayout;
    private ImageView canOneImage, canTwoImage, canThreeImage, canFourImage,cadimOneImage,cadimTwoImage,cadimThreeImage,cadimFourImage,cealOneImage,cealTwoImage,cealThreeImage,cealFourImage;
    String post_key = null;
    private DatabaseReference surveyRef, mDatabaseUsers;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
   // private ScrollView printSurvey;
    private Context context;
    Button btn_screenshot;
    ScrollView scrollView;
    LinearLayout ll_linear;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_survey);
        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        singleSurveyPartnerImage = findViewById(R.id.single_partner_image);
        singleSurveyEventImage = findViewById(R.id.single_survey_image);
        singlePartnerName = findViewById(R.id.single_survey_convener);
        singleLocation = findViewById(R.id.single_survey_location);
        singleEventName = findViewById(R.id.single_survey_name);
        numberMen = findViewById(R.id.numberMen);
        numberWomen = findViewById(R.id.numberWomen);
        numberYouth = findViewById(R.id.numberYouth);


        challenges = findViewById(R.id.singleChallenges);
        policies = findViewById(R.id.singlePolicies);
        successLevel = findViewById(R.id.successChoice);
        layoutPolicies = findViewById(R.id.layoutSinglePolicies);
        canLayout = findViewById(R.id.layoutCan);
        cadimLayout=findViewById(R.id.layoutCadim);
        cealLayout=findViewById(R.id.layoutCeal);
        imageSuccess=findViewById(R.id.image_success);
        singleSurveyDay=findViewById(R.id.single_survey_date);

        textViewNationalPolicies = findViewById(R.id.textView_national_policies);

        textViewNationalPolicies.setVisibility(View.INVISIBLE);

        layoutPolicies.setVisibility(View.INVISIBLE);


        canOneImage = findViewById(R.id.imageCanOne);
        canTwoImage = findViewById(R.id.imageCanTwo);
        canThreeImage = findViewById(R.id.imageCanThree);
        canFourImage = findViewById(R.id.imageCanFour);


        cadimOneImage = findViewById(R.id.imageCadimOne);
        cadimTwoImage = findViewById(R.id.imageCadimTwo);
        cadimThreeImage = findViewById(R.id.imageCadimThree);
        cadimFourImage = findViewById(R.id.imageCadimFour);

        cealOneImage = findViewById(R.id.imageCealOne);
        cealTwoImage= findViewById(R.id.imageCealTwo);
        cealThreeImage = findViewById(R.id.imageCealThree);
        cealFourImage = findViewById(R.id.imageCealFour);




    // printSurvey=findViewById(R.id.print_survey);

        post_key = getIntent().getExtras().getString("PostKey");
        //Initialize the database reference/node where you will be storing posts
        surveyRef = FirebaseDatabase.getInstance().getReference().child("Reports").child(post_key);
        //Initialize an instance of  Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = mAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        ll_linear = (LinearLayout) findViewById(R.id.linear_survey);
       // init();
       // fn_permission();

    }




    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }



    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //if user is logged in populate the Ui With card views
            getSurvey();

        }


    }

    public void getSurvey() {
        surveyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                    final String surveyName = snapshot.child("eventName").getValue().toString();
                    final String projectConvener = snapshot.child("organisation").getValue().toString();
                    final String surveyLocation = snapshot.child("location").getValue().toString();
                    final String surveyImage = snapshot.child("eventPhoto").getValue().toString();
                    final String countMen = snapshot.child("QuestionOne").child("men").getValue().toString();
                    final String countWomen = snapshot.child("QuestionOne").child("women").getValue().toString();
                    final String countYouth = snapshot.child("QuestionOne").child("youth").getValue().toString();
                    final String policyAchieved = snapshot.child("QuestionTwo").child("SelectedOption").getValue().toString();
                    final String initiativePolicies = snapshot.child("QuestionTwo").child("Policies").getValue().toString();
                    final String initiativeSuccess = snapshot.child("QuestionThree").child("SelectedOption").getValue().toString();
                    final String initiativeChallenges = snapshot.child("QuestionFour").child("Challenges").getValue().toString();
                    final String  surveyDay=snapshot.child("date").getValue().toString();



                    if (projectConvener.equalsIgnoreCase("Can")) {
                        if (snapshot.exists()) {
                            final String canOne = snapshot.child("CanOne").child("SelectedOption").getValue().toString();
                            final String canTwo = snapshot.child("CanTwo").child("SelectedOption").getValue().toString();
                            final String canThree = snapshot.child("CanThree").child("SelectedOption").getValue().toString();
                            final String canFour = snapshot.child("CanFour").child("SelectedOption").getValue().toString();


                            canLayout.setVisibility(View.VISIBLE);


                            if (canOne.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(canOneImage);
                            }
                            if (canOne.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(canOneImage);
                            }
                            if (canOne.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(canOneImage);
                            }
                            if (canOne.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(canOneImage);
                            }
                            if (canOne.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(canOneImage);
                            }


                            if (canTwo.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(canTwoImage);
                            }
                            if (canTwo.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(canTwoImage);
                            }
                            if (canTwo.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(canTwoImage);
                            }
                            if (canTwo.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(canTwoImage);
                            }
                            if (canTwo.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(canTwoImage);
                            }


                            if (canThree.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(canThreeImage);
                            }
                            if (canThree.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(canThreeImage);
                            }
                            if (canThree.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(canThreeImage);
                            }
                            if (canThree.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(canThreeImage);
                            }
                            if (canThree.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(canThreeImage);
                            }


                            if (canFour.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(canFourImage);
                            }
                            if (canFour.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(canFourImage);
                            }
                            if (canFour.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(canFourImage);
                            }
                            if (canFour.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(canFourImage);
                            }
                            if (canFour.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(canFourImage);
                            }
                        }

                    }else if (projectConvener.equalsIgnoreCase("CADIM")) {
                        if (snapshot.exists()) {
                            final String cadimOne = snapshot.child("CadimOne").child("SelectedOption").getValue().toString();
                            final String cadimTwo = snapshot.child("CadimTwo").child("SelectedOption").getValue().toString();
                            final String cadimThree = snapshot.child("CadimThree").child("SelectedOption").getValue().toString();
                            final String cadimFour= snapshot.child("CadimFour").child("SelectedOption").getValue().toString();


                            cadimLayout.setVisibility(View.VISIBLE);


                            if (cadimOne.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(cadimOneImage);
                            }
                            if (cadimOne.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(cadimOneImage);
                            }
                            if (cadimOne.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(cadimOneImage);
                            }
                            if (cadimOne.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(cadimOneImage);
                            }
                            if (cadimOne.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(cadimOneImage);
                            }


                            if (cadimTwo.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(cadimTwoImage);
                            }
                            if (cadimTwo.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(cadimTwoImage);
                            }
                            if (cadimTwo.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(cadimTwoImage);
                            }
                            if (cadimTwo.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(cadimTwoImage);
                            }
                            if (cadimTwo.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(cadimTwoImage);
                            }


                            if (cadimThree.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(cadimThreeImage);
                            }
                            if (cadimThree.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(cadimThreeImage);
                            }
                            if (cadimThree.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(cadimThreeImage);
                            }
                            if (cadimThree.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(cadimThreeImage);
                            }
                            if (cadimThree.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(cadimThreeImage);
                            }


                            if (cadimFour.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(cadimFourImage);
                            }
                            if (cadimFour.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(cadimFourImage);
                            }
                            if (cadimFour.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(cadimFourImage);
                            }
                            if (cadimFour.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(cadimFourImage);
                            }
                            if (cadimFour.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(cadimFourImage);
                            }
                        }
                    } else if (projectConvener.equalsIgnoreCase("CEAL")) {
                        if (snapshot.exists()) {
                            final String cealOne = snapshot.child("CealOne").child("SelectedOption").getValue().toString();
                            final String cealTwo = snapshot.child("CealTwo").child("SelectedOption").getValue().toString();
                            final String cealThree = snapshot.child("CealThree").child("SelectedOption").getValue().toString();
                            final String cealFour = snapshot.child("CealFour").child("SelectedOption").getValue().toString();

                            cealLayout.setVisibility(View.VISIBLE);

                            if (cealOne.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(cealOneImage);
                            }
                            if (cealOne.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(cealOneImage);
                            }
                            if (cealOne.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(cealOneImage);
                            }
                            if (cealOne.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(cealOneImage);
                            }
                            if (cealOne.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(cealOneImage);
                            }


                            if (cealTwo.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(cealTwoImage);
                            }
                            if (cealTwo.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(cealTwoImage);
                            }
                            if (cealTwo.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(cealTwoImage);
                            }
                            if (cealTwo.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(cealTwoImage);
                            }
                            if (cealTwo.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(cealTwoImage);
                            }


                            if (cealThree.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(cealThreeImage);
                            }
                            if (cealThree.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(cealThreeImage);
                            }
                            if (cealThree.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(cealThreeImage);
                            }
                            if (cealThree.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(cealThreeImage);
                            }
                            if (cealThree.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(cealThreeImage);
                            }


                            if (cealFour.equalsIgnoreCase("Strongly Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_agree).into(cealFourImage);
                            }
                            if (cealFour.equalsIgnoreCase("Agree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_agree).into(cealFourImage);
                            }
                            if (cealFour.equalsIgnoreCase("Neutral")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_neutral).into(cealFourImage);
                            }
                            if (cealFour.equalsIgnoreCase("Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_disagree).into(cealFourImage);
                            }
                            if (cealFour.equalsIgnoreCase("Strongly Disagree")) {
                                Picasso.with(SingleSurvey.this).load(R.drawable.line_s_disagree).into(cealFourImage);
                            }
                        }
                    }

                            Picasso.with(SingleSurvey.this).load(surveyImage).into(singleSurveyEventImage);




                    singleEventName.setText(surveyName);
                    singleLocation.setText(surveyLocation);
                    singlePartnerName.setText(projectConvener);
                    singleSurveyDay.setText(surveyDay);
                    numberMen.setText(countMen);
                    numberWomen.setText(countWomen);
                    numberYouth.setText(countYouth);




                    challenges.setText(initiativeChallenges);

                    successLevel.setText(initiativeSuccess);

                    if (policyAchieved.equalsIgnoreCase("YES")) {
                        layoutPolicies.setVisibility(View.VISIBLE);
                        textViewNationalPolicies.setVisibility(View.VISIBLE);
                        policies.setText(initiativePolicies);

                    }

                    if(initiativeSuccess.equalsIgnoreCase("Partial")){
                        Picasso.with(SingleSurvey.this).load(R.drawable.partial).into(imageSuccess);

                    }
                    if(initiativeSuccess.equalsIgnoreCase("High")){
                        Picasso.with(SingleSurvey.this).load(R.drawable.high).into(imageSuccess);

                    }
                    if(initiativeSuccess.equalsIgnoreCase("Medium")){
                        Picasso.with(SingleSurvey.this).load(R.drawable.medium).into(imageSuccess);

                    }




                    if (projectConvener.equalsIgnoreCase("Hanns Seidel")) {
                        Picasso.with(SingleSurvey.this).load(R.drawable.seidel).resize(600, 200).centerInside().into(singleSurveyPartnerImage);
                    }else if (projectConvener.equalsIgnoreCase("CADIM")) {
                        Picasso.with(SingleSurvey.this).load(R.drawable.cadim).resize(600, 200).centerInside().into(singleSurveyPartnerImage);
                    }else if (projectConvener.equalsIgnoreCase("EED")) {
                        Picasso.with(SingleSurvey.this).load(R.drawable.eed_logo).resize(600, 200).centerInside().into(singleSurveyPartnerImage);
                    }else if (projectConvener.equalsIgnoreCase("CAN")) {
                        Picasso.with(SingleSurvey.this).load(R.drawable.can).resize(600, 200).centerInside().into(singleSurveyPartnerImage);
                    }else if (projectConvener.equalsIgnoreCase("CEAL")) {
                        Picasso.with(SingleSurvey.this).load(R.drawable.ceal2).resize(600, 200).centerInside().into(singleSurveyPartnerImage);
                    } else if (projectConvener.equalsIgnoreCase("Urbis")) {
                        Picasso.with(SingleSurvey.this).load(R.drawable.urbis).resize(600, 200).centerInside().into(singleSurveyPartnerImage);
                    }else if (projectConvener.equalsIgnoreCase("Champions")) {
                        Picasso.with(SingleSurvey.this).load(R.drawable.champions_logo).resize(600, 200).centerInside().into(singleSurveyPartnerImage);
                    }else if (projectConvener.equalsIgnoreCase("DRFN")) {
                        Picasso.with(SingleSurvey.this).load(R.drawable.drfn_logo).resize(600, 200).centerInside().into(singleSurveyPartnerImage);
                    }else if (projectConvener.equalsIgnoreCase("SOS")) {
                        Picasso.with(SingleSurvey.this).load(R.drawable.sos_logo).resize(600, 200).centerInside().into(singleSurveyPartnerImage);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void printSurvey(View view)  {

        Bitmap bitmap1 = loadBitmapFromView(ll_linear, ll_linear.getWidth(), ll_linear.getHeight());
       PrintHelper photoPrinter = new PrintHelper(SingleSurvey.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("Survey", bitmap1);


    }



}



