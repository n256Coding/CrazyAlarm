package com.n256coding.crazyalarm;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


public class LeakMonitor extends Application{
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context){
        LeakMonitor leakMonitor = (LeakMonitor) context.getApplicationContext();
        return leakMonitor.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
