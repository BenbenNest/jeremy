package com.jeremy.yourday.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by benbennest on 16/8/3.
 */
public class YourDayApplication extends Application {
    private static YourDayApplication mInstance;

    public static YourDayApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mInstance = this;
        Log.v("YourDayApplication", "onCreate");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mInstance = this;
        Log.v("YourDayApplication", "attachBaseContext");
    }
}
