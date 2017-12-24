package com.jeremy.demo.activity.plugin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by changqing on 2017/12/24.
 */

public class HookActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ClipHelper.binder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EditText editText = new EditText(this);
        setContentView(editText);
    }

}
