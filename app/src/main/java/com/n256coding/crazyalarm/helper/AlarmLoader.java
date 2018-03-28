package com.n256coding.crazyalarm.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.text.ParseException;


public class AlarmLoader extends AsyncTask<Context, Void, Void> {
    private static final String TAG = "AlarmLoader_AsyncTask";

    @Override
    protected Void doInBackground(Context... contexts) {
        try {
            AlarmActivator.reloadAlarmOnReboot(contexts[0]);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date in reboot alarm update", e);
        }
        return null;
    }
}
