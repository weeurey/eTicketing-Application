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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by liamu on 17/03/2018.
 *
 * Adapter to return a list of bookings made by the logged in user
 */


public class CreatedEventsAdapter extends ArrayAdapter {

    String[] bookingIDs;
    String[] arrayTitles;
    String[] arrayVenues;
    String[] arrayTypes;

    Context mContext;



    int i =0;
    Integer x =0;

    Button editButton;



    private DatabaseReference mBookingsDatabase;

    public CreatedEventsAdapter(Context context, String[] bookingIDs, String[] arrayTitles, String[] arrayVenues, String[] arrayTypes) {
        super(context, R.layout.single_created_event_layout);
        this.arrayTitles = arrayTitles;
        this.bookingIDs = bookingIDs;
        this.mContext = context;
        this.arrayVenues = arrayVenues;
        this.arrayTypes = arrayTypes;

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
            convertView = mInflater.inflate(R.layout.single_created_event_layout, parent, false);

            mViewHolder.mTitle = (TextView) convertView.findViewById(R.id.single_booking_title);
            mViewHolder.mVenue = (TextView) convertView.findViewById(R.id.single_booking_vemue);
            mViewHolder.mType = convertView.findViewById(R.id.single_booking_type);





            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mTitle.setText(arrayTitles[position]);
        mViewHolder.mVenue.setText(arrayVenues[position]);
        mViewHolder.mType.setText(arrayTypes[position]);
        editButton = convertView.findViewById(R.id.btn_EditEvent);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(mContext, EditingEventActivity.class);
                editIntent.putExtra("eventID", bookingIDs[position]);
                editIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                editIntent.putExtra("type", arrayTypes[position]);
                mContext.startActivity(editIntent);
                ((Activity)mContext).finish();

                Log.i("Edit", "Edit Button Click");
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView mTitle;
        TextView mVenue;
        TextView mType;

    }


}
