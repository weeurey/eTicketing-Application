package com.example.liamu.eticketverifier;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    TextInputEditText mCode, mVenue;
    Button mVerify;

    private DatabaseReference mCurrentDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVerify = findViewById(R.id.btn_Verify);
        mCode = findViewById(R.id.inp_code);
        mVenue = findViewById(R.id.inp_venue);


        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String code = mCode.getText().toString();
                final String venue = mVenue.getText().toString();
                mCurrentDatabase = FirebaseDatabase.getInstance().getReference().child("bookings");

                mCurrentDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren() ){
                            if(child.child("venue").getValue().toString().equals(venue)){
                                if(child.child("code").getValue().toString().equals(code)){
                                    mCurrentDatabase = FirebaseDatabase.getInstance().getReference().child("bookings").child(child.getKey());
                                    mCurrentDatabase.child("code").setValue("ACCEPTED");
                                    mCurrentDatabase.child("isRedeemed").push().setValue("true");
                                    mCode.setText("");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });

    }
}
