package com.brandon14.checkbook.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by brandon on 6/16/16.
 */
public class CheckbookApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);
    }
}
