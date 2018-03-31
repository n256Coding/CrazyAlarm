package com.n256coding.crazyalarm.helper;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.n256coding.crazyalarm.model.Alarm;

import java.text.ParseException;
import java.util.List;

public class AlarmActivator {

    public static void activateAlarm(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.n256Coding.crazyalarm.ALARM_SIGNAL");
        intent.putExtra("alarm", alarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getAlarmTime().getTime(), pendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getAlarmTime().getTime(), pendingIntent);
        }
    }

    public static void cancelAlarm(Context context, int alarmId) {
        Intent intent = new Intent("com.n256Coding.crazyalarm.ALARM_SIGNAL");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void changeAlarm(Context context, Alarm newAlarm, int oldAlarmId) {
        Intent intent = new Intent("com.n256Coding.crazyalarm.ALARM_SIGNAL");
        intent.putExtra("alarm", newAlarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, oldAlarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, newAlarm.getAlarmTime().getTime(), pendingIntent);
    }

    public static void reloadAlarmOnReboot(Context context) throws ParseException {
        List<Alarm> alarms = Alarm.getAllAlarms(context);
        PendingIntent pendingIntent;
        Intent intent = new Intent("com.n256Coding.crazyalarm.ALARM_SIGNAL");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (Alarm alarm : alarms){
            if(alarm.getAlarmStatus().equals(Alarm.ENABLED)){
                intent.putExtra("alarm", alarm);
                pendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getAlarmTime().getTime(), pendingIntent);
                }else{
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getAlarmTime().getTime(), pendingIntent);
                }
            }
        }
    }
}
