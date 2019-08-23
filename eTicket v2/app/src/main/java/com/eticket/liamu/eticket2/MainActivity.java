package com.eticket.liamu.eticket2;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;

    Button mCinemaBtn, mMusicBtn, mTravelBtn, mMyBookingsBtn;


    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Set Buttons
        mCinemaBtn = findViewById(R.id.btn_cinema);
        mMusicBtn = findViewById(R.id.btn_music);
        mTravelBtn = findViewById(R.id.btn_travel);
        mMyBookingsBtn = findViewById(R.id.btn_bookings);

        mCinemaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, EventsActivity.class);
                i.putExtra("type", "cinema");
                startActivity(i);

            }
        });

        mMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, EventsActivity.class);
                i.putExtra("type", "music");
                startActivity(i);

            }
        });

        mTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EventsActivity.class);
                i.putExtra("type", "travel");
                startActivity(i);
            }
        });

        mMyBookingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, userBookingsActivity.class);
                startActivity(i);
            }
        });

        //Toolbar
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("eTicket v2");

    }


    private void sendToStart() {

        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYmmdd");
        String strDate = sdf.format(c.getTime());

        Log.i("date", strDate.toString());


        if (currentUser == null) {

            sendToStart();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logout) {

            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
       if (item.getItemId() == R.id.main_add_event_btn) {
            Intent settingsIntent = new Intent(MainActivity.this, AddEvent.class);
            startActivity(settingsIntent);
        }

        if (item.getItemId() == R.id.main_editEvent) {
            Intent settingsIntent = new Intent(MainActivity.this, userCreatedEvents.class);
            settingsIntent.putExtra("type", "cinema");

            startActivity(settingsIntent);
        }

        if (item.getItemId() == R.id.menu_editMusic) {
            Intent settingsIntent = new Intent(MainActivity.this, userCreatedEvents.class);
            settingsIntent.putExtra("type", "music");
            startActivity(settingsIntent);
        }

        return true;
    }



}
