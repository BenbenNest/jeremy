package com.jeremy.lychee.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Created by changqing on 2018/6/13.
 */

public interface PluginActivityInterface {

    public void attach(Activity proxyActivity);

    public void onCreate(Bundle savedInstanceState);

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public void onSaveInstanceState(Bundle outState);

    public boolean onTouchEvent(MotionEvent event);

    public void onBackPressed();

}
