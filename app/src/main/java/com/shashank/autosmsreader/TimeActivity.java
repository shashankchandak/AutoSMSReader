package com.shashank.autosmsreader;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeActivity extends AppCompatActivity {

    TimePickerDialog timePicker1;
    TimePickerDialog timePicker2;
    Calendar currentTime;
    String amPm;
    int fromHour,fromMinute,toHour,toMinute;
    ArrayList<TimeInterval> timeIntervals;
    RecyclerView rv;
    TimeAdapter timeAdapter;
    SharedPreferences shref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        setTitle(getString(R.string.time_activity_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        timeIntervals=new ArrayList<>();

        shref = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String response=shref.getString("times" , "");
        if(!response.isEmpty())
            timeIntervals = gson.fromJson(response, new TypeToken<ArrayList<TimeInterval>>(){}.getType());

        rv= (RecyclerView) findViewById(R.id.timerv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        timeAdapter =new TimeAdapter(this,timeIntervals);
        rv.setAdapter(timeAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentTime = Calendar.getInstance();
                int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                int currentMinute = currentTime.get(Calendar.MINUTE);
                timePicker1 = new TimePickerDialog(TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        fromHour=selectedHour;
                        fromMinute=selectedMinute;
                        showTimePicker2();

                    }
                }, currentHour, currentMinute, false);
                timePicker1.setTitle(getString(R.string.from_time));
                timePicker1.show();

            }
        });
    }



    public void showTimePicker2(){
        timePicker2 = new TimePickerDialog(TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                toHour=selectedHour;
                toMinute=selectedMinute;
                String from=giveTimeInterval(fromHour,fromMinute);
                String to=giveTimeInterval(toHour,toMinute);
                String timeinterval=from+" - " +to;
                TimeInterval timeInterval=new TimeInterval(fromHour,fromMinute,toHour,toMinute,timeinterval);
                timeIntervals.add(timeInterval);
                timeAdapter.notifyDataSetChanged();

                SharedPreferences.Editor editor;
                shref = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = gson.toJson(timeIntervals);
                editor = shref.edit();
                editor.remove("times").commit();
                editor.putString("times",json);
                editor.commit();



            }
        }, fromHour, fromMinute, false);
        timePicker2.setTitle(getString(R.string.to_time));
        timePicker2.show();


    }

    String giveTimeInterval(int hour,int minute){
        String interval;

        if(hour>=12){
            amPm=getString(R.string.pm);

            if(hour>12)
                hour=hour-12;
        }
        else {
            if(hour==0)
                hour=hour+12;
            amPm=getString(R.string.am);
        }
         interval=String.format("%02d:%02d",hour,minute)+amPm;
        return  interval;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
        }
        return  true;
    }

}
