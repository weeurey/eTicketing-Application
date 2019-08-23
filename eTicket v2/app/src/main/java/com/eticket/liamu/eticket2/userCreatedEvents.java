package com.eticket.liamu.eticket2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class userCreatedEvents extends AppCompatActivity {



    android.support.v7.widget.Toolbar mToolbar;
    private DatabaseReference mUsersDatabase;
    private ProgressDialog mProgressDialog;
    private FirebaseUser mCurrentUser;

    ArrayList<String> bookingIDs = new ArrayList<String>();
    ArrayList<String> TileList = new ArrayList<String>();
    ArrayList<String> DateList = new ArrayList<String>();
    ArrayList<String> TimeList = new ArrayList<String>();
    ArrayList<String> VenueList = new ArrayList<String>();
    ArrayList<String> TypeList = new ArrayList<String>();


    Button editButton;


    ListView mBookingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_created_events);

        Intent i = getIntent();
        final String type = i.getStringExtra("type");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child(type);
        mBookingsList = findViewById(R.id.lst_createdEvents);


        mToolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Your " + type + " Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(mToolbar);

        mProgressDialog = new ProgressDialog(userCreatedEvents.this);
        mProgressDialog.setTitle("Getting Your Events");
        mProgressDialog.setMessage("Please wait will the list is populated...");
        mProgressDialog.setCanceledOnTouchOutside(false);


    }

    protected void onStart() {
        super.onStart();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        final String current_uid =mCurrentUser.getUid();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent a = getIntent();
                final String type = a.getStringExtra("type");
                Integer i = 0;
                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    if(child.child("uid").getValue().toString().equals( current_uid)){
                        TileList.add(child.child("name").getValue().toString());
                        VenueList.add(child.child("venue").getValue().toString());
                        TypeList.add(type);

                        Log.i("Found Listing", "true");



                        bookingIDs.add(child.getKey().toString());
                        Log.i("bookingIDs", "added");




                        i++;
                    }
                }




                String [] arrayBookings = bookingIDs.toArray(new String[bookingIDs.size()]);
                final String [] arrayTitles = TileList.toArray(new String[TileList.size()]);
                String [] arrayDates = DateList.toArray(new String[DateList.size()]);
                String [] arrayVenues = VenueList.toArray(new String[VenueList.size()]);
                String[] arrayTypes = TypeList.toArray(new String[TypeList.size()]);



                mBookingsList = (ListView) findViewById(R.id.lst_createdEvents);
                CreatedEventsAdapter myAdapter = new CreatedEventsAdapter(userCreatedEvents.this, arrayBookings, arrayTitles, arrayVenues, arrayTypes);
                mBookingsList.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //Copy for each child from other class



    }
}
