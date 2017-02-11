package com.jeremy.lychee.activity.function;

import android.os.Bundle;

import com.jeremy.lychee.R;
import com.jeremy.lychee.core.BaseToolbarActivity;

import butterknife.ButterKnife;

/**
 * Created by benbennest on 16/4/24.
 */
public class CurtainActivity extends BaseToolbarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle("窗帘效果");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_curtain;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }
}
