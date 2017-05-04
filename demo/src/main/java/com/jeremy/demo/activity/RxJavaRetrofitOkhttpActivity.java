package com.jeremy.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jeremy.demo.R;

public class RxJavaRetrofitOkhttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_retrofit_okhttp);


    }

    public void getGankAll(View view) {
        GankAllActivity.startActivity(this);


    }


}
