package com.jeremy.demos.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.demos.IMyAIDLInterface;
import com.jeremy.demos.R;
import com.jeremy.demos.service.MyAIDLService;
import com.jeremy.lychee.IMyAidlInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AIDLActivity extends AppCompatActivity {
    IMyAIDLInterface iMyAIDLInterface;
    IMyAidlInterface iMyAidlInterface;
    AIDLServiceConnection connection;
    AIDLServiceConnectionOut outConnection;

    @BindView(R.id.tv_aidl_inner)
    TextView tvAidlInner;
    @BindView(R.id.tv_aidl_outer)
    TextView tvAidlOuter;
    @BindView(R.id.et_val1)
    EditText etVal1;
    @BindView(R.id.et_val2)
    EditText etVal2;
    @BindView(R.id.tv_equals)
    TextView tvEquals;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.layout_inner)
    LinearLayout layoutInner;
    @BindView(R.id.et_val1_outer)
    EditText etVal1Outer;
    @BindView(R.id.et_val2_outer)
    EditText etVal2Outer;
    @BindView(R.id.tv_equals_outer)
    TextView tvEqualsOuter;
    @BindView(R.id.tv_result_outer)
    TextView tvResultOuter;
    @BindView(R.id.layout_outer)
    LinearLayout layoutOuter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unbindService(outConnection);
        connection = null;
        outConnection = null;
    }

    private void init() {
        initService();
        tvResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int val1 = Integer.valueOf(etVal1.getText().toString().trim());
                    int val2 = Integer.valueOf(etVal2.getText().toString().trim());
                    int result = iMyAIDLInterface.add(val1, val2);
                    tvResult.setText(String.valueOf(result));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        tvResultOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iMyAidlInterface == null) {
                    Toast.makeText(AIDLActivity.this, "Service not started", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    int val1 = Integer.valueOf(etVal1Outer.getText().toString().trim());
                    int val2 = Integer.valueOf(etVal2Outer.getText().toString().trim());
                    int result = iMyAidlInterface.minus(val1, val2);
                    tvResultOuter.setText(String.valueOf(result));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initService() {
        connection = new AIDLServiceConnection();
        Intent i = new Intent(this, MyAIDLService.class);
//        i.setClassName("com.jeremy.lychee.service", MyAIDLService.class.getName());
//        bindService(new Intent("cjq.hcn.aidl.DownloadService"), conn, BIND_AUTO_CREATE);
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);

        outConnection = new AIDLServiceConnectionOut();
        ret = bindService(new Intent("MinusService"), outConnection, Service.BIND_AUTO_CREATE);

//        Intent intent = new Intent(this, MyAIDLServiceOuter.class);
//        ret = bindService(intent, outConnection, Context.BIND_AUTO_CREATE);

    }

    class AIDLServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAIDLInterface = IMyAIDLInterface.Stub.asInterface(service);
            Toast.makeText(AIDLActivity.this, "Service connected", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iMyAIDLInterface = null;
            Toast.makeText(AIDLActivity.this, "Service disconnected", Toast.LENGTH_LONG).show();
        }
    }

    class AIDLServiceConnectionOut implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            Toast.makeText(AIDLActivity.this, "Service connected", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iMyAidlInterface = null;
            Toast.makeText(AIDLActivity.this, "Service disconnected", Toast.LENGTH_LONG).show();
        }
    }


}
