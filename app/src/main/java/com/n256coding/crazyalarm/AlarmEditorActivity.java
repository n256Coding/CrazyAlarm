package com.n256coding.crazyalarm;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.n256coding.crazyalarm.helper.DateEx;
import com.n256coding.crazyalarm.model.Alarm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmEditorActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemSelectedListener{
    private static final String TAG = "AlarmEditorActivity";
    TextView txtAlarmTime;
    Spinner spinnerSounds;
    Button btnAddAlarm;
    private Alarm alarm;
    private final List<String> spinnerItems = new ArrayList<>(2);
    private int oldAlarmId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_editor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtAlarmTime = findViewById(R.id.txtAlarmTime);
        spinnerSounds = findViewById(R.id.spinnerAlarmSounds);
        btnAddAlarm = findViewById(R.id.btnAddAlarm);

        txtAlarmTime.setOnClickListener(this);
        spinnerItems.add("Rooster");
        spinnerItems.add("Siren");

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(AlarmEditorActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerSounds.setAdapter(spinnerAdapter);
        spinnerSounds.setOnItemSelectedListener(this);

        if(getIntent().hasExtra("oldAlarmId")){
            oldAlarmId = getIntent().getIntExtra("oldAlarmId", -1);
            try {
                alarm = Alarm.getAlarmById(getApplicationContext(), oldAlarmId);
            } catch (ParseException e) {
                Log.e(TAG, "Error while parsing date from database", e);
            }
            switch (alarm.getAlarmSound()){
                case "Rooster":
                    spinnerSounds.setSelection(0);
                    break;
                case "Siren":
                    spinnerSounds.setSelection(1);
            }
            txtAlarmTime.setText(DateEx.getTimeString(alarm.getAlarmTime()));
        }else{
            alarm = new Alarm();
            alarm.setAlarmStatus(Alarm.ENABLED);
            txtAlarmTime.setText(DateEx.getTimeString(new Date()));
        }
    }

    @Override
    public void onClick(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmEditorActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        txtAlarmTime.setText(DateEx.getTimeString(DateEx.getAlarmDateOf(i, i1)));
                        alarm.setAlarmTime(DateEx.getAlarmDateOf(i, i1));
                    }
                }, DateEx.getCurrentHourOfDay(),
                DateEx.getCurrentMinute(),
                false);
        timePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        alarm.setAlarmSound(spinnerItems.get(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void commitAlarm(View view){
        if(this.alarm.getAlarmId() == -1){
            if(Alarm.addAlarm(getApplicationContext(), this.alarm)) {
                Toast.makeText(AlarmEditorActivity.this, "Alarm placed for "+DateEx.getDateTimeString(this.alarm.getAlarmTime()), Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else{
            this.alarm.setAlarmStatus(Alarm.ENABLED);
            if(Alarm.changeAlarm(getApplicationContext(), this.alarm, this.oldAlarmId)){
                Toast.makeText(AlarmEditorActivity.this, "Alarm placed for "+DateEx.getDateTimeString(this.alarm.getAlarmTime()), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
