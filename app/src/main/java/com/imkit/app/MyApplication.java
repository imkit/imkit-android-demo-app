package com.imkit.app;

import android.app.Application;

import com.imkit.IMKIT;

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();

        IMKIT.init(getApplicationContext());
    }
}
