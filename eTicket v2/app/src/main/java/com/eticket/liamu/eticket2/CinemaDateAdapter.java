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

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liamu on 09/03/2018.
 *
 * Adapter that loads each button for each date seen on the time listings
 */

public class CinemaDateAdapter extends ArrayAdapter {

    Integer[] timeSettingArray;

    Integer[] dates;
    String[] times;
    String timesWeekend;
    Context mContext;
    String myOutputtedDate;
    String justDay;
    String key;
    String type;

    //Declare Buttons
    private Button timebtn1
            , timebtn2, timebtn3, timebtn4, timebtn5, timebtn6, timebtn7
            , timebtn8, timebtn9, timebtn10, timebtn11, timebtn12, timebtn13
            , timebtn14, timebtn15, timebtn16;




    public CinemaDateAdapter(Context context, Integer[] dates, String[] times, String timesWeekend, String key, String type) {
        super(context, R.layout.single_date_layout);
        this.dates = dates;
        this.times = times;
        this.mContext = context;
        this.timesWeekend = timesWeekend;
        this.key = key;
        this.type = type;
    }

    @Override
    public int getCount() {
        return dates.length;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }



    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        Log.i("eventID", key);

        ViewHolder mViewHolder = new ViewHolder();
       // if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.single_date_layout, parent, false);


            //Deal With date logic
            String testDate = dates[position].toString();
            SimpleDateFormat newPattern = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            SimpleDateFormat justDayPattern = new SimpleDateFormat("EEEE");
            DateFormat input = new SimpleDateFormat("yyyyMMdd");

            //Parse dates into needed formats
            try {
                Date inDate = input.parse(testDate);
                justDay = justDayPattern.format(inDate);
                myOutputtedDate = newPattern.format(inDate);
                Log.i("Parse","Parsed! " + justDay);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("Parse","Not Parsed! ");

            }

            //Init Buttons
            timebtn1= convertView.findViewById(R.id.dateLine_time_btn1);
            timebtn1 = convertView.findViewById(R.id.dateLine_time_btn1);
        timebtn2 = convertView.findViewById(R.id.dateLine_time_btn2);
        timebtn3 = convertView.findViewById(R.id.dateLine_time_btn3);
        timebtn4 = convertView.findViewById(R.id.dateLine_time_btn4);
        timebtn5 = convertView.findViewById(R.id.dateLine_time_btn5);
        timebtn6 = convertView.findViewById(R.id.dateLine_time_btn6);
        timebtn7 = convertView.findViewById(R.id.dateLine_time_btn7);
        timebtn8 = convertView.findViewById(R.id.dateLine_time_btn8);
        timebtn9 = convertView.findViewById(R.id.dateLine_time_btn9);
        timebtn10 = convertView.findViewById(R.id.dateLine_time_btn10);
        timebtn11 = convertView.findViewById(R.id.dateLine_time_btn11);
        timebtn12 = convertView.findViewById(R.id.dateLine_time_btn12);
        timebtn13 = convertView.findViewById(R.id.dateLine_time_btn13);
        timebtn14 = convertView.findViewById(R.id.dateLine_time_btn14);
        timebtn15 = convertView.findViewById(R.id.dateLine_time_btn15);
        timebtn16 = convertView.findViewById(R.id.dateLine_time_btn16);

        //make all buttons invisable
            timebtn1.setVisibility(convertView.GONE);
            timebtn2.setVisibility(convertView.GONE);
            timebtn3.setVisibility(convertView.GONE);
            timebtn4.setVisibility(convertView.GONE);
            timebtn5.setVisibility(convertView.GONE);
            timebtn6.setVisibility(convertView.GONE);
            timebtn7.setVisibility(convertView.GONE);
            timebtn8.setVisibility(convertView.GONE);
            timebtn9.setVisibility(convertView.GONE);
            timebtn10.setVisibility(convertView.GONE);
            timebtn11.setVisibility(convertView.GONE);
            timebtn12.setVisibility(convertView.GONE);
            timebtn13.setVisibility(convertView.GONE);
            timebtn14.setVisibility(convertView.GONE);
            timebtn15.setVisibility(convertView.GONE);
            timebtn16.setVisibility(convertView.GONE);


            // Split and convert times
            String[] timesSplitString = times[position].split(",");
            Integer[] timesSplitInt = new Integer[timesSplitString.length];

            String[] weekendTimesSplitString = timesWeekend.split(",");
            Log.i("times", timesWeekend);
            Integer[] weekendTimesSplitInt = new Integer[weekendTimesSplitString.length];


            //convert weekday times
            Integer i = 0;
            while (i != timesSplitString.length) {

                timesSplitInt[i] = Integer.parseInt(timesSplitString[i]);
                i++;
            }

            //convert weekend times
            i = 0;
            while (i != weekendTimesSplitString.length) {

                weekendTimesSplitInt[i] = Integer.parseInt(weekendTimesSplitString[i]);
                i++;
            }


            //Select weekdayrow or weekendrow
            Integer numberOfTimes;
            if(justDay.equals("Saturday")  ||  justDay.equals("Sunday")){
                Log.i("Day", "Weekend" + justDay);
                numberOfTimes = weekendTimesSplitInt.length;
                Log.i("numberoftimes", ""+weekendTimesSplitInt.length);
                timeSettingArray = weekendTimesSplitInt;
            }else{
                Log.i("Day", "Weekday" );
                Log.i("numberoftimes", "EnteredElse");

                numberOfTimes = timesSplitInt.length;
                timeSettingArray = timesSplitInt;
            }
            


            //turn on buttons based on number of times
        if(numberOfTimes >0){
            timebtn1.setVisibility(convertView.VISIBLE);
            timebtn1.setText(timeSettingArray[0].toString());

            //Check how many leading zeroes are needed
            if (timebtn1.getText().toString().length() == 2){
                timebtn1.setText("0" + timebtn1.getText().toString());
            }
            if (timebtn1.getText().toString().length() == 3){
                timebtn1.setText("0" + timebtn1.getText().toString());
            }

            timebtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                    intent.putExtra("key",key);
                    intent.putExtra("date",justDay);
                    intent.putExtra("ldate",dates[position].toString());
                    intent.putExtra("type", type);

                    mContext.startActivity(intent);




                }
            });

        }
            if(numberOfTimes >1){
                timebtn2.setVisibility(convertView.VISIBLE);
                timebtn2.setText(timeSettingArray[1].toString());

                //Check how many leading zeroes are needed
                if (timebtn2.getText().toString().length() == 2){
                    timebtn2.setText("0" + timebtn2.getText().toString());
                }
                if (timebtn2.getText().toString().length() == 3){
                    timebtn2.setText("0" + timebtn2.getText().toString());
                }
                timebtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                        intent.putExtra("key",key);
                        intent.putExtra("date",justDay);
                        intent.putExtra("ldate",dates[position].toString());
                        intent.putExtra("type", type);


                        mContext.startActivity(intent);




                    }
                });

            }
                if (numberOfTimes >2){
                    timebtn3.setVisibility(convertView.VISIBLE);
                    timebtn3.setText(timeSettingArray[2].toString());
                    //Check how many leading zeroes are needed
                    if (timebtn3.getText().toString().length() == 2){
                        timebtn3.setText("0" + timebtn3.getText().toString());
                    }
                    if (timebtn3.getText().toString().length() == 3){
                        timebtn3.setText("0" + timebtn3.getText().toString());
                    }
                    timebtn3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >3){
                    timebtn4.setVisibility(convertView.VISIBLE);
                    timebtn4.setText(timeSettingArray[3].toString());
                    //Check how many leading zeroes are needed
                    if (timebtn4.getText().toString().length() == 2){
                        timebtn4.setText("0" + timebtn4.getText().toString());
                    }
                    if (timebtn4.getText().toString().length() == 3){
                        timebtn4.setText("0" + timebtn4.getText().toString());
                    }
                    timebtn4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >4){
                    timebtn5.setVisibility(convertView.VISIBLE);
                    timebtn5.setText(timeSettingArray[4].toString());
                    //Check how many leading zeroes are needed
                    if (timebtn5.getText().toString().length() == 2){
                        timebtn5.setText("0" + timebtn5.getText().toString());
                    }
                    if (timebtn5.getText().toString().length() == 3){
                        timebtn5.setText("0" + timebtn5.getText().toString());
                    }
                    timebtn5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >5){
                    timebtn6.setVisibility(convertView.VISIBLE);
                    timebtn6.setText(timeSettingArray[5].toString());
                    //Check how many leading zeroes are needed
                    if (timebtn6.getText().toString().length() == 2){
                        timebtn6.setText("0" + timebtn6.getText().toString());
                    }
                    if (timebtn6.getText().toString().length() == 3){
                        timebtn6.setText("0" + timebtn6.getText().toString());
                    }
                    timebtn6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >6){
                    timebtn7.setVisibility(convertView.VISIBLE);
                    timebtn7.setText(timeSettingArray[6].toString());
                    //Check how many leading zeroes are needed
                    if (timebtn7.getText().toString().length() == 2){
                        timebtn7.setText("0" + timebtn7.getText().toString());
                    }
                    if (timebtn7.getText().toString().length() == 3){
                        timebtn7.setText("0" + timebtn7.getText().toString());
                    }
                    timebtn7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >7){
                    timebtn8.setVisibility(convertView.VISIBLE);
                    timebtn8.setText(timeSettingArray[7].toString());
                    //Check how many leading zeroes are needed
                    if (timebtn8.getText().toString().length() == 2){
                        timebtn8.setText("0" + timebtn8.getText().toString());
                    }
                    if (timebtn8.getText().toString().length() == 3){
                        timebtn8.setText("0" + timebtn8.getText().toString());
                    }
                    timebtn8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >8){
                    timebtn9.setVisibility(convertView.VISIBLE);
                    timebtn9.setText(timeSettingArray[8].toString());
                    //Check how many leading zeroes are needed
                    if (timebtn9.getText().toString().length() == 2){
                        timebtn9.setText("0" + timebtn9.getText().toString());
                    }
                    if (timebtn9.getText().toString().length() == 3){
                        timebtn9.setText("0" + timebtn9.getText().toString());
                    }
                    timebtn9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >9){
                    timebtn10.setVisibility(convertView.VISIBLE);
                    timebtn10.setText(timeSettingArray[9].toString());
                    if (timebtn10.getText().toString().length() == 2){
                        timebtn10.setText("0" + timebtn10.getText().toString());
                    }
                    if (timebtn10.getText().toString().length() == 3){
                        timebtn10.setText("0" + timebtn10.getText().toString());
                    }
                    timebtn10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >110){
                    timebtn11.setVisibility(convertView.VISIBLE);
                    timebtn11.setText(timeSettingArray[10].toString());
                    if (timebtn11.getText().toString().length() == 2){
                        timebtn11.setText("0" + timebtn11.getText().toString());
                    }
                    if (timebtn11.getText().toString().length() == 3){
                        timebtn11.setText("0" + timebtn11.getText().toString());
                    }
                    timebtn11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >11){
                    timebtn12.setVisibility(convertView.VISIBLE);
                    timebtn12.setText(timeSettingArray[11].toString());
                    if (timebtn12.getText().toString().length() == 2){
                        timebtn12.setText("0" + timebtn12.getText().toString());
                    }
                    if (timebtn12.getText().toString().length() == 3){
                        timebtn12.setText("0" + timebtn12.getText().toString());
                    }
                    timebtn12.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >12){
                    timebtn13.setVisibility(convertView.VISIBLE);
                    timebtn13.setText(timeSettingArray[12].toString());
                    if (timebtn13.getText().toString().length() == 2){
                        timebtn13.setText("0" + timebtn13.getText().toString());
                    }
                    if (timebtn13.getText().toString().length() == 3){
                        timebtn13.setText("0" + timebtn13.getText().toString());
                    }
                    timebtn13.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >13){
                    timebtn14.setVisibility(convertView.VISIBLE);
                    timebtn14.setText(timeSettingArray[13].toString());
                    if (timebtn14.getText().toString().length() == 2){
                        timebtn14.setText("0" + timebtn14.getText().toString());
                    }
                    if (timebtn14.getText().toString().length() == 3){
                        timebtn14.setText("0" + timebtn14.getText().toString());
                    }
                    timebtn14.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >14){
                    timebtn15.setVisibility(convertView.VISIBLE);
                    timebtn15.setText(timeSettingArray[14].toString());
                    if (timebtn15.getText().toString().length() == 2){
                        timebtn15.setText("0" + timebtn15.getText().toString());
                    }
                    if (timebtn15.getText().toString().length() == 3){
                        timebtn15.setText("0" + timebtn15.getText().toString());
                    }
                    timebtn15.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }
                if (numberOfTimes >15){
                    timebtn16.setVisibility(convertView.VISIBLE);
                    timebtn16.setText(timeSettingArray[15].toString());
                    if (timebtn16.getText().toString().length() == 2){
                        timebtn16.setText("0" + timebtn16.getText().toString());
                    }
                    if (timebtn16.getText().toString().length() == 3){
                        timebtn16.setText("0" + timebtn16.getText().toString());
                    }
                    timebtn16.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BookingOptionsActivity.class);
                            intent.putExtra("key",key);
                            intent.putExtra("date",justDay);
                            intent.putExtra("ldate",dates[position].toString());
                            intent.putExtra("type", type);


                            mContext.startActivity(intent);




                        }
                    });

            }



            mViewHolder.mDate = convertView.findViewById(R.id.dateLine_date);
           // mViewHolder.mTime = convertView.findViewById(R.id.textView);
            convertView.setTag(mViewHolder);
       // } else {
           // mViewHolder = (ViewHolder) convertView.getTag();
       // }
        //Set date on row
        mViewHolder.mDate.setText(myOutputtedDate);
       // mViewHolder.mTime.setText(times[position]);

        return convertView;
    }

    static class ViewHolder {
        TextView mDate;
        TextView mTime;
    }
}
