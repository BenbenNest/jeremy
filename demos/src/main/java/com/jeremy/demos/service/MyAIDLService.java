package com.jeremy.demos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.jeremy.demos.IMyAIDLInterface;

/**
 * Created by benbennest on 16/9/14.
 */
public class MyAIDLService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IMyAIDLInterface.Stub() {

            @Override
            public int add(int value1, int value2) throws RemoteException {
                return value1 + value2;
            }

            @Override
            public int minus(int val1, int val2) throws RemoteException {
                return val1 - val2;
            }
        };

    }

}
