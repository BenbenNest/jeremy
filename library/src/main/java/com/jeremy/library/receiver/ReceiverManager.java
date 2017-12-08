package com.jeremy.library.receiver;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * Created by changqing on 2017/12/8.
 */

public class ReceiverManager {

    NetWorkStateReceiver netWorkStateReceiver;

    protected void registerReceiver(Context context) {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(netWorkStateReceiver, filter);
        System.out.println("注册NetWorkStateReceiver");
    }

    protected void onPause(Context context) {
        context.unregisterReceiver(netWorkStateReceiver);
        System.out.println("注销NetWorkStateReceiver");
    }

    //静态注册

//    <receiver android:name=".NetWorkStateReceiver">
//    <intent-filter>
////检测网络变化的acton
//        <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
//        <category android:name="android.intent.category.DEFAULT" />
//    </intent-filter>
//    </receiver>


}
