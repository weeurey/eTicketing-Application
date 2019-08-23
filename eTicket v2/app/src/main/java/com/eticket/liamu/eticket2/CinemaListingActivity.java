package com.eticket.liamu.eticket2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CinemaListingActivity extends AppCompatActivity {


    private DatabaseReference mCinemaDatabase;

    private TextView mListingTitle, mListingDescr;
    private WebView mWebView;
   private ListView mListView;


    //ButtonDeclarations

    private Button timebtn1
    , timebtn2, timebtn3, timebtn4, timebtn5, timebtn6, timebtn7
      , timebtn8, timebtn9, timebtn10, timebtn11, timebtn12, timebtn13
     , timebtn14, timebtn15, timebtn16;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_listing);
        String event_id = getIntent().getStringExtra("event_id");

        Intent i = getIntent();
        final String type = i.getStringExtra("type");

        //Set Database Loc
        mCinemaDatabase = FirebaseDatabase.getInstance().getReference().child(type).child(event_id);

        mListingTitle = findViewById(R.id.travelListing_title);
        mListingDescr = findViewById(R.id.travelListing_descr);
        mWebView = findViewById(R.id.cinemaListing_webview);
        mListView = findViewById(R.id.cinemaListing_list);

        //All 16 Possible daily Buttons
        timebtn1 = findViewById(R.id.dateLine_time_btn1);
        timebtn2 = findViewById(R.id.dateLine_time_btn2);
        timebtn3 = findViewById(R.id.dateLine_time_btn3);
        timebtn4 = findViewById(R.id.dateLine_time_btn4);
        timebtn5 = findViewById(R.id.dateLine_time_btn5);
        timebtn6 = findViewById(R.id.dateLine_time_btn6);
        timebtn7 = findViewById(R.id.dateLine_time_btn7);
        timebtn8 = findViewById(R.id.dateLine_time_btn8);
        timebtn9 = findViewById(R.id.dateLine_time_btn9);
        timebtn10 = findViewById(R.id.dateLine_time_btn10);
        timebtn11 = findViewById(R.id.dateLine_time_btn11);
        timebtn12 = findViewById(R.id.dateLine_time_btn12);
        timebtn13 = findViewById(R.id.dateLine_time_btn13);
        timebtn14 = findViewById(R.id.dateLine_time_btn14);
        timebtn15 = findViewById(R.id.dateLine_time_btn15);
        timebtn16 = findViewById(R.id.dateLine_time_btn16);


        mCinemaDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Var Setup

                //Retreve data
                mListingTitle.setText(dataSnapshot.child("name").getValue().toString());
                mListingDescr.setText(dataSnapshot.child("desc").getValue().toString());
                String youtubeUrl = dataSnapshot.child("trailerlink").getValue().toString();
                String strEndDate = dataSnapshot.child("dateEnd").getValue().toString();
                String strStartDate = dataSnapshot.child("dateStart").getValue().toString();


                //Setup YouTube webview
                String[] split;



                if(youtubeUrl.contains("=")){
                    split = youtubeUrl.split("=");
                }else{
                    split = youtubeUrl.split("e/");
                }

                Log.i("Split", split[1]);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("https://www.youtube.com/embed/" + split[1]);

                //Get Weekday times
                String weekdayTimes = dataSnapshot.child("weekday").getValue().toString();
               String weekendTimes = dataSnapshot.child("weekend").getValue().toString();





                //Get Current Date
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String strCurDate = strStartDate;

                //Convert datecodes to string

                Integer intCurDate = Integer.parseInt(strCurDate);
                Integer intEndDate = Integer.parseInt(strEndDate);

                //Get Remaining Days
                Integer daysLeft = intEndDate - intCurDate;
                Integer toShow = 0;

                if(daysLeft >=7){
                    toShow = 7;
                }else{
                    toShow = daysLeft;
                }
                Integer arrayDateCodes[] = new Integer[toShow];
                String arrayTimes[] = new String[toShow];

                Integer i = 0;
                while (i != toShow) {
                    arrayDateCodes[i] = intCurDate + i;
                    arrayTimes[i] = weekdayTimes;
                    i++;
                }

                mListView = (ListView) findViewById(R.id.cinemaListing_list);
                CinemaDateAdapter myAdapter = new CinemaDateAdapter(CinemaListingActivity.this, arrayDateCodes, arrayTimes, weekendTimes, mCinemaDatabase.getKey(), type);
                mListView.setAdapter(myAdapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }
}