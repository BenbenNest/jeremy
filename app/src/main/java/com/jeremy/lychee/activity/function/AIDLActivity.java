package com.jeremy.lychee.activity.function;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.lychee.IMyAidlInterface;
import com.jeremy.lychee.R;
import com.jeremy.lychee.service.MyAIDLService;
import com.jeremy.lychee.utils.ToastHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AIDLActivity extends AppCompatActivity {
    IMyAidlInterface myAidlInterface;
    AdditionServiceConnection connection;
    @Bind(R.id.tv_add)
    TextView tvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        ButterKnife.bind(this);
        initService();
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int result = myAidlInterface.add(1, 2);
                    ToastHelper.getInstance(AIDLActivity.this).toast(result + "");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initService() {
        connection = new AdditionServiceConnection();
        Intent i = new Intent(this, MyAIDLService.class);
//        i.setClassName("com.jeremy.lychee.service", MyAIDLService.class.getName());
//        bindService(new Intent("cjq.hcn.aidl.DownloadService"), conn, BIND_AUTO_CREATE);

        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        if (ret) {
            ToastHelper.getInstance(this).toast("成功");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        connection = null;
    }

    class AdditionServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            myAidlInterface = IMyAidlInterface.Stub.asInterface((IBinder) boundService);
            Toast.makeText(AIDLActivity.this, "Service connected", Toast.LENGTH_LONG).show();
        }

        public void onServiceDisconnected(ComponentName name) {
            myAidlInterface = null;
            Toast.makeText(AIDLActivity.this, "Service disconnected", Toast.LENGTH_LONG).show();
        }
    }


}
