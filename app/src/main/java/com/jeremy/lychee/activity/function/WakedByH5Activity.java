package com.jeremy.lychee.activity.function;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jeremy.lychee.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WakedByH5Activity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waked_by_h5);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String data = intent.getDataString();
        tvTitle.setText("请求:data" + "\n打开Activity");
        System.out.println(data);

    }
}
