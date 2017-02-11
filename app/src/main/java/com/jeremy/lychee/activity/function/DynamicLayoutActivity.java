package com.jeremy.lychee.activity.function;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jeremy.lychee.R;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.utils.DeviceUtil;
import com.jeremy.lychee.widget.CircleTextView;
import com.jeremy.lychee.widget.DynamicFloatLayout;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DynamicLayoutActivity extends AppCompatActivity {

    @Bind(R.id.flow_layout)
    DynamicFloatLayout flowLayout;
    @Bind(R.id.root)
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_layout);
        ButterKnife.bind(this);
        addCircles();

    }


    private void addCircles() {
        int width = DeviceUtil.getScreenWidth(this);
        int height = DeviceUtil.getScreenHeight(this);
        for (int i = 0; i < 20; i++) {
            CircleTextView circleTextView = new CircleTextView(this);
            int padding = DensityUtils.dip2px(this, 8);
            circleTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circleTextView.setPadding(padding, padding, padding, padding);
            circleTextView.setGravity(Gravity.CENTER);
            circleTextView.setTextColor(Color.WHITE);
            circleTextView.setBackgroundColor(Color.RED);
            if (i % 2 == 0) {
                circleTextView.setText("旅行");
            } else {
                circleTextView.setText("社会新闻");
            }
            int x = new Random(width).nextInt();
            int y = new Random(height).nextInt();
//            circleTextView.setPosition(x, y);
//            circleTextView.setPosition(x,y);
            flowLayout.addView(circleTextView);

        }

    }


}
