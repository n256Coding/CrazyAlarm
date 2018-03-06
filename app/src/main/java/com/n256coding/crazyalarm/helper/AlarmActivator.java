package com.n256coding.crazyalarm.helper;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.n256coding.crazyalarm.model.Alarm;

public class AlarmActivator {
    public void activeAlarm(Context context, Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //TODO Set Pending intent and replace null values
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getAlarmTime().getTime(), null);
    }
}
