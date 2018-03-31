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
    private String alarmStatus;
    private String alarmSound;

    public static final String ENABLED = "enabled";
    public static final String DISABLED = "disabled";

    protected Alarm(Parcel in) {
        this.alarmId = in.readInt();
        this.alarmTime = new Date(in.readLong());
        this.alarmStatus = in.readString();
        this.alarmSound = in.readString();
    }

    public Alarm() {
        this.alarmId = -1;
        this.alarmTime = new Date();
        this.alarmStatus = "";
        this.alarmSound = "Rooster";
    }

    public Alarm(Date alarmTime, String alarmStatus, String alarmSound) {
        this.alarmTime = alarmTime;
        this.alarmStatus = alarmStatus;
        this.alarmSound = alarmSound;
    }

    public Alarm(int alarmId, Date alarmTime, String alarmStatus, String alarmSound) {
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
        this.alarmStatus = alarmStatus;
        this.alarmSound = alarmSound;
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

    public String getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
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

    public static Alarm getAlarmById(Context context, int alarmId) {
        DBHelper alarmDatabase = new DBHelper(context);
        Cursor alarms = alarmDatabase.getAlarmById(alarmId);
        Alarm alarm = null;
        while (alarms.moveToNext()) {
            alarm = new Alarm(alarms.getInt(alarms.getColumnIndex("alarm_id")),
                    new Date(alarms.getLong(alarms.getColumnIndex("alarm_date"))),
                    alarms.getString(alarms.getColumnIndex("alarm_status")),
                    alarms.getString(alarms.getColumnIndex("alarm_sound")));
        }
        return alarm;
    }

    public static String getAlarmStatusById(Context context, int alarmId){
        DBHelper db = new DBHelper(context);
        Cursor results = db.getAlarmStatusById(alarmId);
        String status = "";
        while (results.moveToNext()){
            status = results.getString(results.getColumnIndex("alarm_status"));
        }
        return status;
    }

    public static boolean updateAlarmStatusById(Context context, String status, int alarmId){
        DBHelper db = new DBHelper(context);
        return db.updateAlarmStatusById(status, alarmId);
    }

    public static boolean addAlarm(Context context, Alarm alarm) {
        DBHelper db = new DBHelper(context);
        alarm.setAlarmId((int) db.insert(alarm));
        if (alarm.getAlarmId() != -1) {
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

    public static boolean changeAlarm(Context context, Alarm newAlarm, int oldAlarmId) {
        DBHelper db = new DBHelper(context);
        if (db.update(newAlarm, oldAlarmId)) {
            AlarmActivator.changeAlarm(context, newAlarm, oldAlarmId);
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
        parcel.writeString(alarmStatus);
        parcel.writeString(alarmSound);
    }
}
