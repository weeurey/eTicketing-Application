package com.eticket.liamu.eticket2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TravelListingActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbar;


    private DatabaseReference mCinemaDatabase;

    private TextView mListingTitle, mListingDescr;
    private WebView mWebView;
    private ListView mListView;

    private Button mPurchase;

    //ButtonDeclarations


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_listing);
        final String event_id = getIntent().getStringExtra("event_id");

        Intent i = getIntent();
        final String type = i.getStringExtra("type");
        mToolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Travel Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(mToolbar);


        //Set Database Loc
        mCinemaDatabase = FirebaseDatabase.getInstance().getReference().child(type).child(event_id);

        mListingTitle = findViewById(R.id.travelListing_title);
        mListingDescr = findViewById(R.id.travelListing_descr);
        mWebView = findViewById(R.id.cinemaListing_webview);
        mListView = findViewById(R.id.cinemaListing_list);
        mPurchase = findViewById(R.id.btn_purchase);

        mPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelListingActivity.this, BookingOptionsActivity.class);
                intent.putExtra("key",event_id);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });



        mCinemaDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Var Setup

                //Retreve data
                mListingTitle.setText(dataSnapshot.child("name").getValue().toString());
                mListingDescr.setText(dataSnapshot.child("desc").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }
}