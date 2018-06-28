package com.jeremy.demo.activity.plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;

import com.jeremy.demo.R;
import com.jeremy.lychee.plugin.PluginActivityInterface;

import java.lang.reflect.Constructor;

public class ProxyActivity extends Activity {
    private String className;
    PluginActivityInterface pluginInterfaceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_test);
        className = getIntent().getStringExtra("className");
        try {
            Class activityClass = getClassLoader().loadClass(className);
            Constructor constructor = activityClass.getConstructor(new Class[]{});
            Object instance= constructor.newInstance(new Object[]{});
            pluginInterfaceActivity = (PluginActivityInterface) instance;
            pluginInterfaceActivity.attach(this);
            Bundle bundle = new Bundle();
            pluginInterfaceActivity.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        String className1=intent.getStringExtra("className");
        Intent intent1 = new Intent(this, ProxyActivity.class);
        intent1.putExtra("className", className1);
        super.startActivity(intent1);
    }

//    @Override
//    public ComponentName startService(Intent service) {
//        String serviceName = service.getStringExtra("serviceName");
//        Intent intent1 = new Intent(this, ProxyService.class);
//        intent1.putExtra("serviceName", serviceName);
//        return super.startService(intent1);
//    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
//
        IntentFilter newInterFilter = new IntentFilter();
        for (int i=0;i<filter.countActions();i++) {
            newInterFilter.addAction(filter.getAction(i));
        }
//        return super.registerReceiver(new ProxyBroadCast(receiver.getClass().getName(),this),newInterFilter);
        return null;
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }


    @Override
    protected void onStart() {
        super.onStart();
//        payInterfaceActivity.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        payInterfaceActivity.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        payInterfaceActivity.onPause();
    }



}
