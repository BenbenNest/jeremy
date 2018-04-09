package com.jeremy.plugin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

/**
 * Created by changqing on 2018/4/9.
 */


public class TestActivity extends BaseProxyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(mProxyActivity);
        button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        button.setBackgroundColor(Color.YELLOW);
        button.setText("这是测试页面");
        setContentView(button);
    }

}