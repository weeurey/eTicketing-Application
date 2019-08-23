package com.eticket.liamu.eticket2;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class BookingOptionsActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler  {
    BillingProcessor bp;

    Toolbar mToolbar;
    String date;
    String justDay="";
    Button mButton;
    String myDateCode="";


    Spinner mTimeSpinner;

    private DatabaseReference mBookingOptionsDatabase;
    private DatabaseReference mPlaceBookingRef;

    private DatabaseReference mEventRef;


    TextView mTitle, mDate, mVenue;

    FirebaseUser mCurrentUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_options);

        Intent i = getIntent();
        final String type = i.getStringExtra("type");

        //Google Licence id for in app billing
        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnReHYmyhJow2FgEI38skUFDy3WZE7dJF3J2" +
                "gU3G024OVaeNjoUleHyz9yvlGS/IVKbq89en4V7pZnamEbXlUq2ortRfTlq2lVaSx1fwC8MrUsmZzgOOQdEopwECINZLQd0SVQV6+9s9" +
                "XgwcDl3PYndWqxYOX1AAqjPIXOlEAFM6f6iZvOaRBG7OSopZyjhJjD21s9jKgrmPwM9mCutIQY5nSKrHR0V0aeBwU0gYzNfRl2YA+9ehDNiM" +
                "a0OqELrz1FZZuEK1Iwd+84IHaS8pMD4yEtipXA4Qp73LZUectQ9j6HGut6NDZg0VJ9SiR/DCHfp2speQXC5sEtbwKEivNSQIDAQAB"
                , this);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        final String current_uid =mCurrentUser.getUid();

        mDate = findViewById(R.id.BookingOptions_date_txt);
        mTitle = findViewById(R.id.BookingOptions_title_txt);
        mVenue = findViewById(R.id.BookingOptions_venue_txt);
        mButton = findViewById(R.id.BookingOptions_place_btn);


        mTimeSpinner = findViewById(R.id.BookingOptions_Time_spnr);

        final ArrayList<String> spinnerArrayTimes = new ArrayList<String>();
        //spinnerArrayTimes.add("Hello");

        Intent intent = getIntent();
          final String key = intent.getStringExtra("key").toString();
          String ldate="";

          if(!type.equals("travel")){
              date = intent.getStringExtra("date").toString();
              ldate = intent.getStringExtra("ldate").toString();
          }


        DateFormat input = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat justDayPattern = new SimpleDateFormat("EEEE");
        SimpleDateFormat newPattern = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        SimpleDateFormat dateCodeFormat = new SimpleDateFormat("yyyyMMdd");


        String myOutputtedDate="";

        if (!type.equals("travel")) {

        try {
                Date inDate = input.parse(ldate);
                justDay = justDayPattern.format(inDate);
                Log.i("Parse2", "Parsed! " + justDay);
                myOutputtedDate = newPattern.format(inDate);
                myDateCode = dateCodeFormat.format(inDate);


            } catch(ParseException e){
                e.printStackTrace();

            }

            mDate.setText(myOutputtedDate);

        }

        spinnerArrayTimes.add("Please Select a Time");


        mBookingOptionsDatabase = FirebaseDatabase.getInstance().getReference().child(type).child(key);
        mBookingOptionsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!type.equals("travel")){


                String weekdayTimes = dataSnapshot.child("weekday").getValue().toString();
                String weekendTimes = dataSnapshot.child("weekend").getValue().toString();
                String venue = dataSnapshot.child("venue").getValue().toString();

                mVenue.setText(venue);


                    String[] timesArray;

                    if(justDay.equals( "Saturday")  ||  justDay.equals("Sunday")) {

                        timesArray = weekendTimes.split(",");
                        Log.i("WEEKTYPE", "weekend"+date);
                    }else{

                        timesArray = weekdayTimes.split(",");
                        Log.i("WEEKTYPE", "weekday"+date);
                    }

                    Integer i = 0;

                    while(i != timesArray.length){
                        spinnerArrayTimes.add(timesArray[i].toString());
                        i++;
                    }

                }

                String title = dataSnapshot.child("name").getValue().toString();
                final String security = dataSnapshot.child("secure").getValue().toString();
                mTitle.setText(title);

                if(type.equals("travel")){

                    mTimeSpinner.setVisibility(View.INVISIBLE);

                }

                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                        if(mTimeSpinner.getSelectedItem().toString().length() == 4) {
                            bp.consumePurchase("android.test.purchased");
                            bp.purchase(BookingOptionsActivity.this, "android.test.purchased");
                        }

                        if(type.equals("travel")){
                            bp.consumePurchase("android.test.purchased");
                            bp.purchase(BookingOptionsActivity.this, "android.test.purchased");
                        }
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayTimes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner.setAdapter(dataAdapter);


        mToolbar = (Toolbar) findViewById(R.id.bookingOptions_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Booking Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    // IBillingHandler implementation

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details ) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Booking Placed")
                    .setMessage("The booking has been placed!")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(BookingOptionsActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                    }).create();

            Random r = new Random();
            int a = r.nextInt((100000 - 10) + 1) + 10;

            Intent intent = getIntent();
            String key = intent.getStringExtra("key").toString();

            final String current_uid = mCurrentUser.getUid();

            String bookID = key + "-" + myDateCode + "-" + mTimeSpinner.getSelectedItem().toString() + "-" + a;

            mPlaceBookingRef = FirebaseDatabase.getInstance().getReference().child("bookings").child(bookID);

            Intent i = getIntent();
            final String type = i.getStringExtra("type");

            mEventRef = FirebaseDatabase.getInstance().getReference().child(type).child(key);


        Date currentTime = Calendar.getInstance().getTime();

        HashMap<String, String> userMap = new HashMap<>();

            userMap.put("date", myDateCode);

            if(type.equals("travel")){
                userMap.put("time", currentTime.toString());

            }else{
                userMap.put("time", mTimeSpinner.getSelectedItem().toString());

            }
            userMap.put("title", mTitle.getText().toString());
            userMap.put("userid", current_uid.toString());
            userMap.put("eventid", key);
            userMap.put("venue", mVenue.getText().toString());
            userMap.put("code", "unset");
            userMap.put("secure", "1");



            mPlaceBookingRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        builder.show();



                    }
                }
            });

        }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         *
         * Note - this includes handling the case where the user canceled the buy dialog:
         * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
         */
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
