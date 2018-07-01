package com.jeremy.lycheeserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by changqing on 2018/6/29.
 */

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onCreate();
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Constant.TAG, "MyService onCreate");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            Log.i(Constant.TAG, "MyService onStartCommand接收到的数据是:" + intent.getStringExtra("data"));
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(Constant.TAG, "MyService onDestroy");
    }


}