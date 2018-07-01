package com.jeremy.lycheeserver;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by changqing on 2018/6/29.
 */

public class MyBinder extends Binder {
    private final static int ADD = 1;
    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case ADD:
                data.enforceInterface("MyBinder");
                int a = data.readInt();
                int b = data.readInt();
                int add = add(a, b);
                reply.writeInt(add);
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    public int add(int a, int b) {
        return a + b;
    }
}