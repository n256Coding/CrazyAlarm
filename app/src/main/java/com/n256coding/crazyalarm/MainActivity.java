package com.n256coding.crazyalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextClock;

import com.n256coding.crazyalarm.adapters.CustomAdapter;
import com.n256coding.crazyalarm.model.Alarm;
import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView lvAlarmList;
    FloatingActionButton fabAddAlarm;
    TextClock clock;
    CustomAnalogClock analogClock;
    private static final String TAG = "MainActivity";
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar() != null && getSupportActionBar().isShowing())
            getSupportActionBar().hide();

        lvAlarmList = findViewById(R.id.lv_alarmList);
        fabAddAlarm = findViewById(R.id.fab_addAlarm);
        clock = findViewById(R.id.textClock);
        analogClock = findViewById(R.id.analog_clock);

        switch (getResources().getConfiguration().orientation){
            case 1:
                clock.setFormat12Hour("h:mm:ss a");
                break;
            case 2:
                analogClock.setAutoUpdate(true);
        }

        List<Alarm> alarmList = new ArrayList<>();
        try {
            alarmList = Alarm.getAllAlarms(getApplicationContext());
        } catch (ParseException e) {
            Log.e(TAG, "Error while date parsing", e);
        }

        customAdapter = new CustomAdapter(alarmList);
        lvAlarmList.setAdapter(customAdapter);
        lvAlarmList.setLayoutManager(new LinearLayoutManager(MainActivity.this.getApplicationContext()));

        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlarmEditorActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            customAdapter.setAlarmList(Alarm.getAllAlarms(getApplicationContext()));
            customAdapter.notifyDataSetChanged();
        } catch (ParseException e) {
            Log.e(TAG, "Error while parsing time", e);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
