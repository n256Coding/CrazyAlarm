package com.n256coding.crazyalarm.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.n256coding.crazyalarm.AlarmViewerActivity;
import com.n256coding.crazyalarm.services.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmUiIntent = new Intent(context, AlarmViewerActivity.class);
        Intent alarmServiceIntent = new Intent(context, AlarmService.class);
        alarmUiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.getApplicationContext().startActivity(alarmUiIntent);
        context.getApplicationContext().startService(alarmServiceIntent);
    }

}
