package com.imkit.app;

import android.app.Application;
import android.content.Context;

import com.imkit.IMKIT;

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();

        Context context = getApplicationContext();
        IMKIT.init(context, context.getString(R.string.IMKIT_URL), context.getString(R.string.IMKIT_CLIENT_KEY), context.getString(R.string.IMKIT_BUCKET_NAME), context.getPackageName() + ".fileProvider");
    }
}
