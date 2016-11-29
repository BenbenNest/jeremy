package com.jeremy.demo.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jeremy.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThreadActivity extends AppCompatActivity {

    @BindView(R.id.tv_result)
    TextView tvResult;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        ButterKnife.bind(this);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                TextView tx = new TextView(ThreadActivity.this);
                tx.setText("子线程");
                WindowManager viewManager = ThreadActivity.this.getWindowManager();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                        200, 200, 200, 200, WindowManager.LayoutParams.FIRST_SUB_WINDOW,
                        WindowManager.LayoutParams.TYPE_TOAST, PixelFormat.OPAQUE);
                viewManager.addView(tx, layoutParams);
                Looper.loop();
            }
        });
    }


    @OnClick(R.id.tv_result)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_result:
                thread.start();
                break;
            default:

                break;
        }
    }


}
