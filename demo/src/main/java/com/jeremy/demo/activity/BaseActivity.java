package com.jeremy.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jeremy.demo.jacoco.CoverageUtils;

/**
 * Created by changqing on 2018/7/4.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoverageUtils.creatCoverageFile(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoverageUtils.writeCoverageReport(this);
    }


}
