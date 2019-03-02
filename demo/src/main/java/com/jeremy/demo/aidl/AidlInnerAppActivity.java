package com.jeremy.demo.aidl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityManagerCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.demo.R;
import com.jeremy.demo.model.Person;

public class AidlInnerAppActivity extends Activity {
    private TextView mTestTV;
    private TextView mTest2TV;
    private AidlInnerApp mRemoteService;
    private Person person;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_inner_app);
        person = new Person();
        person.setName("Jeremy");
        mTestTV =(TextView)findViewById(R.id.test) ;
        mTest2TV = findViewById(R.id.test2);
        bindService(new Intent(this,AidlInnerAppService.class),mServiceConnection,BIND_AUTO_CREATE);
        mTestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRemoteService!=null){
                    try {
                        //client和Server不在一个进程的情况下，sayHello调用是通过AidlInnerApp.Stub.Proxy.sayHello方法实现的，
                        // proxy会调用mRemote.transact(Stub.TRANSACTION_sayHello, _data, _reply, 0);mRemote是可以跨进程传输的Binder对象
                        //AidlInnerApp.Stub的onTransact方法会接收到transact方法传过来的参数
                        //Proxy的sayHello方法会调用IBinder的transact接口，他的实现在Binder类中，
                        //Binder类的transact方法又会调用onTransact方法，也就是AidlInnerApp.Stub的onTransact方法
                        String result = mRemoteService.sayHello(person);
                        Toast.makeText(AidlInnerAppActivity.this, result, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    ActivityManagerService
                }
            }
        });
        mTest2TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mRemoteService.updateName(person);
                    Toast.makeText(AidlInnerAppActivity.this, person.getName(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){

                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    //asInterface的作用是根据调用是否属于同进程而返回不同的实例对象，决定返回何种对象的关键在obj.queryLocalInterface(DESCRIPTOR)的返回结果。
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //这里得到的service对象其实就是AidlInnerApp对象，他是实现了AidlInnerApp.Stub具体接口的IBinder
            //asInterface会调用queryLocalInterface，如果client和server在同一个进程，则返回本地的AidlInnerApp.Stub对象，如果client和server不在同一个进程，则返回AidlInnerApp.Stub的内部代理类Proxy
            mRemoteService = AidlInnerApp.Stub.asInterface(iBinder);
            Toast.makeText(AidlInnerAppActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


}
