package com.eticket.liamu.eticket2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

//Class deals with reedeeming of ticket, it generates a random code and waits for the code string to be changed to the
// word "accepted" when this happend the screen goes green to indicate that verification worked.
public class TicketRedeem extends AppCompatActivity {

    private DatabaseReference mCurrentDatabase;
    private DatabaseReference mCurrentDatabase2;
    private DatabaseReference mCurrentDatabase3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_redeem);

        final String passedID;

        final Intent intent = getIntent();

        final TextView mTitle, mTime, mDate, mVenue, mCode, mStatus, mIsRedeemed;


        mStatus = findViewById(R.id.txt_redeemAccepted);
        mTitle = findViewById(R.id.txt_RedeemEventName);
        mTime = findViewById(R.id.txt_redeemTime);
        mDate = findViewById(R.id.txt_RedeemDate);
        mVenue = findViewById(R.id.txt_redeemVenue);
        mCode = findViewById(R.id.txt_redeemSecurityCode);
        mIsRedeemed = findViewById(R.id.txt_isRedeem);

        passedID = intent.getStringExtra("bookingID");
        mCurrentDatabase = FirebaseDatabase.getInstance().getReference().child("bookings");

        Random r = new Random();
        int Low = 1;
        int High = 9999;
        final int randResult = r.nextInt(High - Low) + Low;

        mCurrentDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().toString().equals(passedID)) {

                        String title = child.child(("title")).getValue().toString();
                        String time = child.child(("time")).getValue().toString();
                        String date = child.child(("date")).getValue().toString();
                        String venue = child.child(("venue")).getValue().toString();
                        String secure = child.child(("secure")).getValue().toString();

                        mTitle.setText(title);
                        mTime.setText(time);
                        mDate.setText(date);
                        if(!date.equals("")){//lising is not a travel ticket{
                            mVenue.setText(venue);

                        }

                        if(child.child("isRedeemed").exists()){

                            mIsRedeemed.setText("REDEEMED");

                        }
                        if (secure.equals("1")) {
                            String finalCode = Integer.toString(randResult);
                            mCode.setText(finalCode.toString());
                        }

                    }
                }

                mCurrentDatabase2 = FirebaseDatabase.getInstance().getReference().child("bookings").child(passedID);
                mCurrentDatabase2.child("code").setValue(mCode.getText().toString());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        mCurrentDatabase3 = FirebaseDatabase.getInstance().getReference().child("bookings").child(passedID);
        mCurrentDatabase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String a = dataSnapshot.child("code").getValue().toString();

                if(a.equals("ACCEPTED")){

                    Log.i("ACCEPTED DETECTED", "true");

                    View view = findViewById(R.id.lay_redeemLayout);
                    view.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    mStatus.setText("Accepted");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 5000);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        mCurrentDatabase3.child("code").setValue("0");


    }
}