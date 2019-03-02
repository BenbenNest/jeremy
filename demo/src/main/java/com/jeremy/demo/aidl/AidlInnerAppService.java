package com.jeremy.demo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.jeremy.demo.model.Person;

import static com.jeremy.demo.aidl.AidlInnerApp.*;

public class AidlInnerAppService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    //mServiceBinder是一个Stub对象，Stub extends android.os.Binder implements com.jeremy.demo.AidlInnerApp，在跟客户端建立连接后，会把这个Binder传递给客户端
    private final Stub mServiceBinder = new Stub() {

        @Override
        public String sayHello(com.jeremy.demo.model.Person person) throws RemoteException {
            return "Hello,"+person.getName()+"!";
        }

        @Override
        public void updateName(Person person) throws RemoteException {
            person.setName("benben");
        }

    };


}
