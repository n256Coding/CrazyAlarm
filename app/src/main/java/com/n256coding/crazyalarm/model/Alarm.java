package com.n256coding.crazyalarm.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.n256coding.crazyalarm.database.DBHelper;
import com.n256coding.crazyalarm.helper.DateEx;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alarm {
    private String alarmId;
    private Date alarmTime;
    private String alarmText;
    private String alarmSound;

    public Alarm(Date alarmTime, String alarmText, String alarmSound) {
        this.alarmTime = alarmTime;
        this.alarmText = alarmText;
        this.alarmSound = alarmSound;
    }

    public Alarm(String alarmId, Date alarmTime, String alarmText, String alarmSound) {
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
        this.alarmText = alarmText;
        this.alarmSound = alarmSound;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmText() {
        return alarmText;
    }

    public void setAlarmText(String alarmText) {
        this.alarmText = alarmText;
    }

    public String getAlarmSound() {
        return alarmSound;
    }

    public void setAlarmSound(String alarmSound) {
        this.alarmSound = alarmSound;
    }

    public List<Alarm> getAllAlarms(Context context) throws ParseException {
        List<Alarm> alarmList = new ArrayList<>();
        DBHelper alarmDatabase = new DBHelper(context);
        Cursor alarms = alarmDatabase.getAllAlarms();
        while (alarms.moveToNext()){
            alarmList.add(new Alarm(alarms.getString(0), DateEx.getDateOfDateTime(alarms.getString(1)),
                    alarms.getString(2), alarms.getString(3)));
        }
        return alarmList;
    }
}
