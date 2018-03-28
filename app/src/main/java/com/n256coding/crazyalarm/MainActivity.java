package com.n256coding.crazyalarm;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.n256coding.crazyalarm.adapters.CustomAdapter;
import com.n256coding.crazyalarm.callbacks.AlarmListChangeListener;
import com.n256coding.crazyalarm.helper.AlarmActivator;
import com.n256coding.crazyalarm.helper.DateEx;
import com.n256coding.crazyalarm.model.Alarm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AlarmListChangeListener{
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
//                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
//                        new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                                Alarm alarm = new Alarm(DateEx.getAlarmDateOf(i, i1), "", "");
//                                if(Alarm.addAlarm(getApplicationContext(), alarm)){
//                                    alarmListInserted(alarm, customAdapter.getItemCount());
//                                    Toast.makeText(MainActivity.this, "Alarm placed", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        },
//                        DateEx.getCurrentHourOfDay(),
//                        DateEx.getCurrentMinute(),
//                        false);
//                timePickerDialog.show();
            }
        });
    }

    @Override
    public void alarmListInserted(Alarm alarm) {
        customAdapter.addItemToList(alarm);
        customAdapter.notifyDataSetChanged();
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
