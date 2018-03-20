package com.n256coding.crazyalarm.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.n256coding.crazyalarm.database.DBHelper;
import com.n256coding.crazyalarm.helper.AlarmActivator;
import com.n256coding.crazyalarm.helper.DateEx;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alarm implements Parcelable {
    private int alarmId;
    private Date alarmTime;
    private String alarmText;
    private String alarmSound;

    public Alarm(Date alarmTime, String alarmText, String alarmSound) {
        this.alarmTime = alarmTime;
        this.alarmText = alarmText;
        this.alarmSound = alarmSound;
    }

    public Alarm(int alarmId, Date alarmTime, String alarmText, String alarmSound) {
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
        this.alarmText = alarmText;
        this.alarmSound = alarmSound;
    }

    protected Alarm(Parcel in) {
        alarmId = in.readInt();
        alarmText = in.readString();
        alarmSound = in.readString();
        alarmTime = new Date(in.readLong());
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
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

    public static List<Alarm> getAllAlarms(Context context) throws ParseException {
        List<Alarm> alarmList = new ArrayList<>();
        DBHelper alarmDatabase = new DBHelper(context);
        Cursor alarms = alarmDatabase.getAllAlarms();
        while (alarms.moveToNext()) {
            alarmList.add(new Alarm(alarms.getInt(0), DateEx.getDateOfDateTime(alarms.getString(1)),
                    alarms.getString(2), alarms.getString(3)));
        }
        alarmDatabase.close();
        return alarmList;
    }

    public static boolean addAlarm(Context context, Alarm alarm) {
        DBHelper db = new DBHelper(context);
        if (db.insert(alarm)) {
            AlarmActivator.activateAlarm(context, alarm);
            return true;
        }
        return false;
    }

    public static boolean removeAlarm(Context context, int alarmId) {
        DBHelper db = new DBHelper(context);
        if (db.delete(alarmId)) {
            AlarmActivator.cancelAlarm(context, alarmId);
            return true;
        }
        return false;
    }

    public static boolean changeAlarm(Context context, Alarm newAlarm, Alarm oldAlarm) {
        DBHelper db = new DBHelper(context);
        if (db.update(newAlarm, oldAlarm.getAlarmId())) {
            AlarmActivator.changeAlarm(context, newAlarm, oldAlarm);
            return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(alarmId);
        parcel.writeLong(alarmTime.getTime());
        parcel.writeString(alarmText);
        parcel.writeString(alarmSound);
    }
}
