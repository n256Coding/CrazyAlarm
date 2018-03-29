package com.n256coding.crazyalarm.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.n256coding.crazyalarm.AlarmViewerActivity;
import com.n256coding.crazyalarm.helper.AlarmLoader;
import com.n256coding.crazyalarm.model.Alarm;
import com.n256coding.crazyalarm.services.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Receiver received alarms");

        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            AlarmLoader alarmLoader = new AlarmLoader();
            alarmLoader.execute(context);
            return;
        }
        else if(!intent.getAction().equals("com.n256Coding.crazyalarm.ALARM_SIGNAL")){
            return;
        }

        Intent alarmUiIntent = new Intent(context, AlarmViewerActivity.class);
        Alarm alarm = (Alarm) intent.getExtras().get("alarm");

        alarmUiIntent.putExtra("alarm", alarm);
        alarmUiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent alarmServiceIntent = new Intent(context, AlarmService.class);
        alarmServiceIntent.putExtra("alarm", alarm);

        context.getApplicationContext().startActivity(alarmUiIntent);
        context.getApplicationContext().startService(alarmServiceIntent);
    }



}
