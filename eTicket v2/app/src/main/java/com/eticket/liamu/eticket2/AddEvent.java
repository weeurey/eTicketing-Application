package com.eticket.liamu.eticket2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class AddEvent extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;

    private DatePickerDialog.OnDateSetListener mDateSetListner;
    private DatePickerDialog.OnDateSetListener mStartDateSetListner;

    private TimePickerDialog.OnTimeSetListener mTimeSetListner;
    private TimePickerDialog.OnTimeSetListener mWeekdayTimeSetListner;


    private TextView startDateLabel;
    private Button startDateButton;

    private TextView endDateLabel;
    private Button addEndDateButton;

    private TextView weekendTimesLabel;
    private TextView weekdayTimesLabel;


    private Button addWeekendTimeButton;
    private Button removeWeekendButton;

    private Button addWeekdayTimeButton;
    private Button removeWeekdayTimeButton;

    private Button submitEventBtn;

    private DatabaseReference mEventDatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;

    private static final int GALLERY_PICK=1;

    private boolean isCinema;

    private ProgressDialog  mProgressDialog;


    private TextInputEditText mEName, mEDesc, mEYT, mEVenue;
    String eName, eDisc, eYT, eVenue, eStartDate, eEndDate, eWeekendTimes, eWeekdayTimes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent i = getIntent();
        final String type = i.getStringExtra("type");


        mImageStorage = FirebaseStorage.getInstance().getReference();


        startDateLabel = findViewById(R.id.txtStartDate);
        startDateButton = findViewById(R.id.btn_startdate);

        endDateLabel = findViewById(R.id.txtEndDate);
        addEndDateButton = findViewById(R.id.btn_enddate);

        weekendTimesLabel = findViewById(R.id.txt_weekendTimes);
        weekdayTimesLabel = findViewById(R.id.txt_weekdayTimes);


        addWeekendTimeButton = findViewById(R.id.btn_addWeekendTime);
        removeWeekendButton = findViewById(R.id.btn_RemoveWeekendTime);

        addWeekdayTimeButton = findViewById(R.id.btn_addWeekdayTime);
        removeWeekdayTimeButton = findViewById(R.id.btn_removeWeekdayTime);

        submitEventBtn = findViewById(R.id.btn_submitEvent);

        mEName = findViewById(R.id.edit_eventName);
        mEDesc = findViewById(R.id.edit_eventDisc);
        mEYT = findViewById(R.id.edit_eventYT);
        mEVenue = findViewById(R.id.edit_Venue);

        //  mCinemaDatabase = FirebaseDatabase.getInstance().getReference().child("cinema");


        submitEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eName, eDisc, eYT, eVenue, eStartDate, eEndDate, eWeekendTimes, eWeekdayTimes;

                //Upload Image
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });



        addWeekendTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open time picker and select
                Calendar time = Calendar.getInstance();
                int hour = time.get(Calendar.HOUR_OF_DAY);
                int mins = time.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        AddEvent.this,
                        mTimeSetListner,
                        hour,mins,true
                        );

                dialog.show();
            }
        });

        addWeekdayTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open time picker and select
                Calendar time = Calendar.getInstance();
                int hour = time.get(Calendar.HOUR_OF_DAY);
                int mins = time.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        AddEvent.this,
                        mWeekdayTimeSetListner,
                        hour,mins,true
                );

                dialog.show();
            }
        });

        removeWeekendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open time picker and select
                String labelString = weekendTimesLabel.getText().toString();
                if (labelString.contains(",")) {
                    String[] splitSemiColon = labelString.split(":");
                    String[] splitTimes = splitSemiColon[1].split(",");

                    String rebuiltString = "";

                    int i = 0;
                    while (i != (splitTimes.length - 1)) {
                        rebuiltString = rebuiltString + splitTimes[i] + ",";
                        i++;
                    }

                    weekendTimesLabel.setText("Weekend Times:" + rebuiltString);

                }
            }
        });

        removeWeekdayTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open date picker and select
                String labelString = weekdayTimesLabel.getText().toString();
                if (labelString.contains(",")) {
                    String[] splitSemiColon = labelString.split(":");
                    String[] splitTimes = splitSemiColon[1].split(",");

                    String rebuiltString = "";

                    int i = 0;
                    while (i != (splitTimes.length - 1)) {
                        rebuiltString = rebuiltString + splitTimes[i] + ",";
                        i++;
                    }

                    weekdayTimesLabel.setText("Weekend Times:" + rebuiltString);

                }
            }
        });

        addEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open date picker and select

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListner,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();



            }
        });

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open date picker and select

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mStartDateSetListner,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();



            }
        });

        mDateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month ++;

                String stringMonth = String.valueOf(month);
                String stringDay = String.valueOf(dayOfMonth);

                if (month <10){
                    stringMonth = "0" + stringMonth;
                }

                if (dayOfMonth < 10){
                    stringDay = "0"+stringDay;
                }


                endDateLabel.setText("End Date:" + year+stringMonth+stringDay);
            }
        };


        mStartDateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month ++;
                String stringMonth = String.valueOf(month);
                String stringDay = String.valueOf(dayOfMonth);

                if (month <10){
                    stringMonth = "0" + stringMonth;
                }

                if (dayOfMonth < 10){
                    stringDay = "0"+stringDay;
                }


                startDateLabel.setText("End Date:" + year+stringMonth+stringDay);
            }
        };


        mTimeSetListner = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



                //Add leading zeros if time values are less than 10
                String stringHour = String.valueOf(hourOfDay);
                String stringMin = String.valueOf(minute);

                if (hourOfDay <10){
                    stringHour = "0" + stringHour;
                }

                if (minute < 10){
                    stringMin = "0"+stringMin;
                }


                String combinedStrings = stringHour + stringMin;
                weekendTimesLabel.setText(weekendTimesLabel.getText().toString()+ combinedStrings + ",");

            }
        };


        mWeekdayTimeSetListner = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                String stringHour = String.valueOf(hourOfDay);
                String stringMin = String.valueOf(minute);

                if (hourOfDay <10){
                    stringHour = "0" + stringHour;
                }

                if (minute < 10){
                    stringMin = "0"+stringMin;
                }


                String combinedStrings = stringHour + stringMin;

                weekdayTimesLabel.setText(weekdayTimesLabel.getText().toString()+ combinedStrings + ",");

            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Selecting image , crop and compressing before upload

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){



            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialog = new ProgressDialog(AddEvent.this);
                mProgressDialog.setTitle("Uploading Image");
                mProgressDialog.setMessage("Please wait will the image is uploaded and processed");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();


                File thumb_filePath = new File(resultUri.getPath());


                //Firebase Store Cropped
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();;

                //Compress
                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(60)
                        .compressToBitmap(thumb_filePath);

                //Set Vars

                eName = mEName.getText().toString();
                eDisc = mEDesc.getText().toString();
                eYT = mEYT.getText().toString();
                eVenue = mEVenue.getText().toString();

                String[] split;
                split = startDateLabel.getText().toString().split(":");
                eStartDate = split[1];
                split = endDateLabel.getText().toString().split(":");
                eEndDate = split[1];
                split = weekdayTimesLabel.getText().toString().split(":");
                eWeekdayTimes = split[1];
                split =  weekendTimesLabel.getText().toString().split(":");
                eWeekendTimes = split[1];

                //Get Random ID
                Random r = new Random();
                int a = r.nextInt((100000-10)+1)+10;

                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                String current_uid =mCurrentUser.getUid();

                //Build Event ID
               final String eventID = eName + "-" + eVenue + "-" + a +"-"+ current_uid;


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                StorageReference filepath = mImageStorage.child(eventID);
                final StorageReference thumb_filepath= mImageStorage.child("profile_images").child("thumbs").child(eventID+".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();


                                    if(thumb_task.isSuccessful()){

                                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = current_user.getUid();

                                        if(isCinema){
                                            mEventDatabase = FirebaseDatabase.getInstance().getReference().child("cinema").child(eventID);
                                        }if(isCinema == false){
                                            mEventDatabase = FirebaseDatabase.getInstance().getReference().child("music").child(eventID);
                                        }


                                        //Put data into firebase
                                        HashMap<String, String> eventMap = new HashMap<>();
                                        eventMap.put("name", eName);
                                        eventMap.put("desc", eDisc);
                                        eventMap.put("trailerlink", eYT);
                                        eventMap.put("venue", eVenue);
                                        eventMap.put("dateStart", eStartDate);
                                        eventMap.put("dateEnd", eEndDate);
                                        eventMap.put("weekday", eWeekdayTimes);
                                        eventMap.put("weekend", eWeekendTimes);
                                        eventMap.put("image", download_url);
                                        eventMap.put("uid", uid);
                                        eventMap.put("secure", "1"); // Set security as none


                                        mEventDatabase.setValue(eventMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(AddEvent.this, "Uploaded.", Toast.LENGTH_LONG).show();
                                                }else{

                                                    Toast.makeText(AddEvent.this, "Upload Error Encountered. Thumb", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                                    }else{

                                        Toast.makeText(AddEvent.this, "Upload Error Encountered.", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }else{
                            Toast.makeText(AddEvent.this, "Upload Error Encountered.", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    public void checkButton(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_cinema:
                if (checked)
                    isCinema = true;
                    break;
            case R.id.radio_music:
                if (checked)
                    isCinema = false;
                    break;
        }
    }


    //Image Logic



}

