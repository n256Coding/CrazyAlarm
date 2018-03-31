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

    public Alarm getClone(){
        Alarm alarm = new Alarm();
        alarm.setAlarmId(this.alarmId);
        alarm.setAlarmTime(this.alarmTime);
        alarm.setAlarmStatus(this.alarmStatus);
        alarm.setAlarmSound(this.alarmSound);
        return alarm;
    }

    public static List<Alarm> getAllAlarms(Context context) throws ParseException {
        List<Alarm> alarmList = new ArrayList<>();
        DBHelper alarmDatabase = new DBHelper(context);
        Cursor alarms = alarmDatabase.getAllAlarms();
        while (alarms.moveToNext()) {
            alarmList.add(new Alarm(alarms.getInt(0),
                    DateEx.getDateOfDateTime(alarms.getString(1)),
                    alarms.getString(2), alarms.getString(3)));
        }
        alarmDatabase.close();
        return alarmList;
    }

    public static Alarm getAlarmById(Context context, int alarmId) throws ParseException {
        DBHelper alarmDatabase = new DBHelper(context);
        Cursor alarms = alarmDatabase.getAlarmById(alarmId);
        Alarm alarm = null;
        while (alarms.moveToNext()) {
            alarm = new Alarm(alarms.getInt(alarms.getColumnIndex("alarm_id")),
                    DateEx.getDateOfDateTime(alarms.getString(alarms.getColumnIndex("alarm_time"))),
                    alarms.getString(alarms.getColumnIndex("alarm_status")),
                    alarms.getString(alarms.getColumnIndex("alarm_sound")));
        }
        alarmDatabase.close();
        return alarm;
    }

    public static String getAlarmStatusById(Context context, int alarmId){
        DBHelper db = new DBHelper(context);
        Cursor results = db.getAlarmStatusById(alarmId);
        String status = "";
        while (results.moveToNext()){
            status = results.getString(results.getColumnIndex("alarm_status"));
        }
        db.close();
        return status;
    }

    /**
     * Modifies the alarm status
     * @param status could be "enabled" or "disabled"
     * @return true if updated, else false
     */
    public static boolean updateAlarmStatusById(Context context, String status, int alarmId){
        DBHelper db = new DBHelper(context);
        boolean result = db.updateAlarmStatusById(status, alarmId);
        db.close();
        return result;
    }

    public static boolean addAlarm(Context context, Alarm alarm) {
        DBHelper db = new DBHelper(context);
        alarm.setAlarmId((int) db.insert(alarm));
        db.close();
        if (alarm.getAlarmId() != -1) {
            AlarmActivator.activateAlarm(context, alarm);
            return true;
        }
        return false;
    }

    /**
     * Remove alarm from whole system including alarm manager tasks and database.
     * @param context context
     * @param alarmId id of the alarm to be removed
     * @return true if removed, else false
     */
    public static boolean removeAlarm(Context context, int alarmId) {
        DBHelper db = new DBHelper(context);
        if (db.delete(alarmId)) {
            db.close();
            AlarmActivator.cancelAlarm(context, alarmId);
            return true;
        }
        db.close();
        return false;
    }

    /**
     * Removes alarm from alarm manager's tasks but not from database.
     * But change the status of alarm as "disabled" in database.
     *
     * @param context
     * @param alarmId alarm id of the alarm which should be disabled
     * @return
     */
    public static boolean disableAlarm(Context context, int alarmId){
        DBHelper db = new DBHelper(context);
        if(db.updateAlarmStatusById(Alarm.DISABLED, alarmId)){
            db.close();
            AlarmActivator.cancelAlarm(context, alarmId);
            return true;
        }
        db.close();
        return false;
    }

    /**
     * Change the time of alarm and change it's status as "enabled".
     * @param context
     * @param newAlarm information of new alarm.
     * @param oldAlarmId alarm id that needs to be modified.
     * @return true if changed, else false
     */
    public static boolean changeAlarm(Context context, Alarm newAlarm, int oldAlarmId) {
        DBHelper db = new DBHelper(context);
        if (db.update(newAlarm, oldAlarmId)) {
            db.close();
            AlarmActivator.changeAlarm(context, newAlarm, oldAlarmId);
            return true;
        }
        db.close();
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
