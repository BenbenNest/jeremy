package com.jeremy.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.demo.R;
import com.jeremy.library.inject.InjectUtils;
import com.jeremy.library.inject.ViewInject;

public class ViewInjectActivity extends Activity {

    @ViewInject(R.id.tv_inject)
    private TextView tvInject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inject);
        InjectUtils.inject(this);
        tvInject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewInjectActivity.this, "运行时注解实现ButterKnife功能", Toast.LENGTH_LONG).show();
                tvInject.setText(tvInject.getText().toString() + " ViewId=" + InjectUtils.getViewId());
                tvInject.setEnabled(false);
            }
        });
    }


}
