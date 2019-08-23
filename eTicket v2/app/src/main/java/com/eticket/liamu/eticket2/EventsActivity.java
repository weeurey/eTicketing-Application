package com.eticket.liamu.eticket2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;



public class EventsActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;
    String endDate;

    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Intent i = getIntent();
        final String type = i.getStringExtra("type");

        mProgressDialog = new ProgressDialog(EventsActivity.this);
        mProgressDialog.setTitle("Getting events");
        mProgressDialog.setMessage("Please wait will the list is populated...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mToolbar = findViewById(R.id.users_appbar);
        mUsersList = findViewById(R.id.users_list);

        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child(type);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    protected void onStart() {
        super.onStart();

        //Delete Expired Data



        Intent i = getIntent();
        final String type = i.getStringExtra("type");


        FirebaseDatabase.getInstance().getReference().child(type)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Events events = snapshot.getValue(Events.class);
                            // Log.i("DateRet", events.dateEnd);

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            String strDate = sdf.format(c.getTime());

                            Integer intCurDate = Integer.parseInt(strDate);
                            if (!type.equals("travel")) {
                                Integer intDateEnd = Integer.parseInt(events.dateEnd);

                                if (intCurDate < intDateEnd) {
                                    Log.i("delete", "No Delete");
                                } else {
                                    Log.i("delete", "Deleting...");
                                    //ADD DELETE CODE HERE

                                    mUsersDatabase.child(snapshot.getKey()).removeValue();


                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        FirebaseRecyclerAdapter<Events, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Events, UsersViewHolder>(
                Events.class,
                R.layout.events_single_layout,
                UsersViewHolder.class,
                mUsersDatabase

        ) {


            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, Events events, int position) {


                usersViewHolder.setEventName(events.getName());
                usersViewHolder.setEventVenue(events.getVenue());
                usersViewHolder.setEventImage(events.getImage(), getApplicationContext());


                final String event_id = getRef(position).getKey();
                mProgressDialog.dismiss();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(type.equals("travel")){
                            Intent profileIntent = new Intent(EventsActivity.this, TravelListingActivity.class);
                            profileIntent.putExtra("event_id", event_id);
                            profileIntent.putExtra("type", type);
                            startActivity(profileIntent);

                        }else {
                            Intent profileIntent = new Intent(EventsActivity.this, CinemaListingActivity.class);
                            profileIntent.putExtra("event_id", event_id);
                            profileIntent.putExtra("type", type);
                            startActivity(profileIntent);
                        }


                    }
                });

            }
        };

        mUsersList.setAdapter(firebaseRecyclerAdapter);

        }



    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }


        public void setEventName(String name){


            TextView mUserNameView = mView.findViewById(R.id.event_single_name);
            mUserNameView.setText(name);
        }

        public void setEventVenue(String status){


            TextView mUserStatusView = mView.findViewById(R.id.event_single_venue);
            mUserStatusView.setText(status);
        }


        public void setEventImage(final String thumb_image, final Context ctx){


            final CircleImageView userImageView = mView.findViewById(R.id.event_single_image);
            Picasso.with(ctx)
                    .load(thumb_image)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.defaultselfie)
                    .into(userImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx)
                            .load(thumb_image)
                            .placeholder(R.drawable.defaultselfie)
                            .into(userImageView);

                }
            });

        }


    }
}