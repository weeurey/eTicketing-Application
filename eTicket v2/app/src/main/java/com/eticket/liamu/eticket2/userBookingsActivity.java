package com.eticket.liamu.eticket2;

import android.app.ProgressDialog;
import android.service.quicksettings.Tile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class userBookingsActivity extends AppCompatActivity {

android.support.v7.widget.Toolbar mToolbar;
    private DatabaseReference mUsersDatabase;
    private ProgressDialog mProgressDialog;
    private FirebaseUser mCurrentUser;
    ArrayList<String> bookingIDs = new ArrayList<String>();
    ArrayList<String> TileList = new ArrayList<String>();
    ArrayList<String> DateList = new ArrayList<String>();
    ArrayList<String> TimeList = new ArrayList<String>();
    ArrayList<String> VenueList = new ArrayList<String>();


    ListView mBookingsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookings);




        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("bookings");
        mBookingsList = findViewById(R.id.user_bookings_list);

        mToolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(mToolbar);

        mProgressDialog = new ProgressDialog(userBookingsActivity.this);
        mProgressDialog.setTitle("Getting Bookings");
        mProgressDialog.setMessage("Please wait will the list is populated...");
        mProgressDialog.setCanceledOnTouchOutside(false);
      //  mProgressDialog.show();







    }

    protected void onStart() {
        super.onStart();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        final String current_uid =mCurrentUser.getUid();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer i = 0;
                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    if(child.child("userid").getValue().toString().equals( current_uid)){
                        TileList.add(child.child("title").getValue().toString());
                        DateList.add(child.child("date").getValue().toString());
                        TimeList.add(child.child("time").getValue().toString());
                        VenueList.add(child.child("venue").getValue().toString());



                        bookingIDs.add(child.getKey().toString());
                        Log.i("bookingIDs", "added");




                        i++;
                    }
                }

                String [] arrayBookings = bookingIDs.toArray(new String[bookingIDs.size()]);
                String [] arrayTitles = TileList.toArray(new String[TileList.size()]);
                String [] arrayTimes = TimeList.toArray(new String[TimeList.size()]);
                String [] arrayDates = DateList.toArray(new String[DateList.size()]);
                String [] arrayVenues = VenueList.toArray(new String[VenueList.size()]);



                mBookingsList = (ListView) findViewById(R.id.user_bookings_list);
                BookingsAdapter myAdapter = new BookingsAdapter(userBookingsActivity.this, arrayBookings, arrayTitles, arrayTimes, arrayDates, arrayVenues);
                mBookingsList.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //Copy for each child from other class



    }
}
