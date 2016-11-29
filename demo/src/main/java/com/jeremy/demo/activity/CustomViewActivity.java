package com.jeremy.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.jeremy.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomViewActivity extends Activity {


    @BindView(R.id.root)
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_view);

        ButterKnife.bind(this);

        RemoteControlMenu menu = new RemoteControlMenu(this);
        root.addView(menu);
    }


}
