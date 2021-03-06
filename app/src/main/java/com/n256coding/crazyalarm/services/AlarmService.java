package com.n256coding.crazyalarm.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.n256coding.crazyalarm.AlarmViewerActivity;
import com.n256coding.crazyalarm.R;
import com.n256coding.crazyalarm.model.Alarm;

import java.io.IOException;

public class AlarmService extends Service {
    private static final String TAG = "AlarmService";

    private final AlarmServiceBinder mBinder = new AlarmServiceBinder();
    protected MediaPlayer player;
    private NotificationManagerCompat notificationManager;

    public class AlarmServiceBinder extends Binder {
        public AlarmService getService() {
            return AlarmService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player = new MediaPlayer();

        Alarm alarm = new Alarm();
        if(intent.hasExtra("alarm"))
            alarm = (Alarm) intent.getExtras().get("alarm");

        if (Build.VERSION.SDK_INT < 26) {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
        } else {
            player.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .build()
            );
        }

        try {
            switch (alarm.getAlarmSound()) {
                case "Rooster":
                    player.setDataSource(getApplicationContext(),
                            Uri.parse("android.resource://" +
                                    getApplicationContext().getPackageName() +
                                    "/" + R.raw.rooster));
                    break;
                case "Siren":
                    player.setDataSource(getApplicationContext(),
                            Uri.parse("android.resource://" +
                                    getApplicationContext().getPackageName() +
                                    "/" + R.raw.siren));
            }
        } catch (IOException e) {
            Log.e(TAG, "Error parsing alarm sound uri", e);
        }

        Intent viewerIntent = new Intent(AlarmService.this, AlarmViewerActivity.class);
        PendingIntent alarmUiShowIntent = PendingIntent.getActivity(AlarmService.this,
                0, viewerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Its Time to Wake up!")
                .setContentText("Sleeping time is over. Wake up and get ready.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setContentIntent(alarmUiShowIntent)
                .build();

        notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(0, notification);
        Log.i(TAG, "Alarm Service Started");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAlarmSound();
        removeNotification();
        player.release();
        player = null;
        Log.i(TAG, "Service destroyed");
    }

    public void startAlarmSound() throws IOException {
        if (!player.isPlaying()) {
            player.setLooping(true);
            player.prepare();
            player.start();
        }
    }

    public void stopAlarmSound() {
        if (player.isPlaying()) {
            player.stop();
        }
    }

    public void removeNotification() {
        notificationManager.cancel(0);
    }


}
