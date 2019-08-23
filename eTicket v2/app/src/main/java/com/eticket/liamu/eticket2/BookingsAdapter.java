package com.eticket.liamu.eticket2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by liamu on 17/03/2018.
 *
 *
 *
 *
 * Adapter to list bookings that the user has made
 */


public class BookingsAdapter extends ArrayAdapter {

    String[] bookingIDs;
    String[] arrayTitles;
    String[] arrayTimes;
    String[] arrayDates;
    String[] arrayVenues;

    Context mContext;



    int i =0;
    Integer x =0;

    private DatabaseReference mBookingsDatabase;

    public BookingsAdapter(Context context, String[] bookingIDs, String[] arrayTitles, String[] arrayTimes, String[] arrayDates, String[] arrayVenues) {
        super(context, R.layout.single_booking_layout);
        this.bookingIDs = bookingIDs;
        this.mContext = context;
        this.arrayDates = arrayDates;
        this.arrayTitles = arrayTitles;
        this.arrayTimes = arrayTimes;
        this.arrayVenues = arrayVenues;

    }
    @Override
    public int getCount() {
        return bookingIDs.length;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.single_booking_layout, parent, false);

            mViewHolder.mTitle = (TextView) convertView.findViewById(R.id.single_booking_title);
            mViewHolder.mDate = (TextView) convertView.findViewById(R.id.single_booking_date);
            mViewHolder.mTime = (TextView) convertView.findViewById(R.id.single_booking_time);
            mViewHolder.mVenue = (TextView) convertView.findViewById(R.id.single_booking_vemue);
            mViewHolder.mRedeem = convertView.findViewById(R.id.singleBooking_redeem);



            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mTitle.setText(arrayTitles[position]);
        mViewHolder.mDate.setText(arrayDates[position]);
        mViewHolder.mTime.setText(arrayTimes[position]);
        mViewHolder.mVenue.setText(arrayVenues[position]);



        mViewHolder.mRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(mContext, TicketRedeem.class);
                editIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                editIntent.putExtra("bookingID", bookingIDs[position]);
                mContext.startActivity(editIntent);
                ((Activity)mContext).finish();

                Log.i("Edit", "Edit Button Click");
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView mTitle;
        TextView mDate;
        TextView mTime;
        TextView mVenue;
        Button mRedeem;

    }
}
