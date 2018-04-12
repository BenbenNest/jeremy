package com.jeremy.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.jeremy.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    private String TAG = "SplashActivity";
    private static final float START_SCALE = 1.0f;
    private static final float END_SCALE = 1.2f;
    private static final int DURATION = 1000;

    @BindView(R.id.start)
    ImageView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        init();
//        String deviceInfo = DeviceUtils.getDeviceInfo(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(TAG);
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
//        MobclickAgent.onPause(this);
    }

    private void init() {
//        MobclickAgent.onEvent(this, Constants.APP_START);
        start.setImageResource(R.drawable.splash);
        final ScaleAnimation scaleAnim = new ScaleAnimation(START_SCALE, END_SCALE, START_SCALE, END_SCALE,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(DURATION);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        start.startAnimation(scaleAnim);

    }


}
