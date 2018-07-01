package com.jeremy.demo.client;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jeremy.demo.R;

public class IPCByBinderActivity extends AppCompatActivity {
    EditText etNum1;
    EditText etNum2;
    EditText etResult;
    IBinder mRemote;
    ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcby_binder);
        init();
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    add();
                }
            }
        });
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //这个service就是Binder驱动中创建的Binder对象
                mRemote = service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mRemote = null;
            }
        };
        connectServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private void init() {
        etNum1 = findViewById(R.id.et_num1);
        etNum2 = findViewById(R.id.et_num2);
        etResult = findViewById(R.id.et_result);
    }

    private boolean checkInput() {
        if (etNum1 == null || etNum2 == null || etResult == null) {
            return false;
        }
        return true;
    }

    private void connectServer() {
        if (mRemote == null) {
            //Android 5.0之后必须使用显式启动
            Intent intent = new Intent();
//        intent.setAction("com.jeremy.lycheeserver.MyService");
//        intent.setPackage("com.jeremy.lycheeserver");
//        Intent newIntent = IntentUtils.createExplicitFromImplicitIntent(this,intent);
            intent.setComponent(new ComponentName("com.jeremy.lycheeserver", "com.jeremy.lycheeserver.MyService"));

            //远程Service不知道为什么不能直接Bind，需要先startService之后才可以Bind，有人说需要打开关联启动权限，这里先不管了，直接启动先（也可以在服务端程序中启动），
            // 以后可以判断服务端服务是否启动再判断是否需要启动
            startService(intent);
            boolean bindResult = bindService(intent, connection, Service.BIND_AUTO_CREATE);
        }
    }

    public void add() {
        int code = 1;
        //向服务端发送的数据
        Parcel data = Parcel.obtain();
        //接收服务端返回的数据
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken("MyBinder");
        int a = Integer.valueOf(etNum1.getText().toString());
        int b = Integer.valueOf(etNum2.getText().toString());
        data.writeInt(a);
        data.writeInt(b);
        try {
            mRemote.transact(code, data, reply, 0);
            int i = reply.readInt();
            etResult.setText(i + "");
            Log.d("jeremy", "add: " + i);
            reply.recycle();
            data.recycle();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
