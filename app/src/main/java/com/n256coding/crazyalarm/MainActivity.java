package com.n256coding.crazyalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.n256coding.crazyalarm.adapters.CustomAdapter;
import com.n256coding.crazyalarm.model.Alarm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView lvAlarmList;
    FloatingActionButton fabAddAlarm;
    private static final String TAG = "MainActivity";
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvAlarmList = findViewById(R.id.lv_alarmList);
        fabAddAlarm = findViewById(R.id.fab_addAlarm);

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
}
