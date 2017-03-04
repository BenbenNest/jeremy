package com.jeremy.demo.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jeremy.demo.R;

import java.lang.ref.WeakReference;

@SuppressLint("HandlerLeak")
public class ShakeActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "ShakeActivity";

    private static final int UPTATE_INTERVAL_TIME = 50;
    private static final int SPEED_SHRESHOLD = 50;//调节灵敏度
    private long lastUpdateTime;
    private float lastX;
    private float lastY;
    private float lastZ;

    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;
    private SoundPool mSoundPool;
    private boolean isShake = false;
    private LinearLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private ImageView mTopLine;
    private ImageView mBottomLine;
    private MyHandler mHandler;
    private int mWeiChatAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_shake);
        initView();
        mHandler = new MyHandler(this);
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mWeiChatAudio = mSoundPool.load(this, R.raw.weichat_audio, 1);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override
    protected void onPause() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    private void initView() {
        mTopLayout = (LinearLayout) findViewById(R.id.main_linear_top);
        mBottomLayout = ((LinearLayout) findViewById(R.id.main_linear_bottom));
        mTopLine = (ImageView) findViewById(R.id.main_shake_top_line);
        mBottomLine = (ImageView) findViewById(R.id.main_shake_bottom_line);
        mTopLine.setVisibility(View.GONE);
        mBottomLine.setVisibility(View.GONE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            long currentUpdateTime = System.currentTimeMillis();
            long timeInterval = currentUpdateTime - lastUpdateTime;
            if (timeInterval < UPTATE_INTERVAL_TIME) {
                return;
            }
            lastUpdateTime = currentUpdateTime;
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;
            lastX = x;
            lastY = y;
            lastZ = z;
            double speed = (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval) * 100;

//            if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math.abs(z) > 17) && !isShake) {
            if (speed >= SPEED_SHRESHOLD && !isShake) {
                isShake = true;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Log.d(TAG, "onSensorChanged");
                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(END_SHAKE).sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private static class MyHandler extends Handler {
        private WeakReference<ShakeActivity> mReference;
        private ShakeActivity mActivity;

        public MyHandler(ShakeActivity activity) {
            mReference = new WeakReference<ShakeActivity>(activity);
            if (mReference != null) {
                mActivity = mReference.get();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    mActivity.mVibrator.vibrate(300);
                    mActivity.mSoundPool.play(mActivity.mWeiChatAudio, 1, 1, 0, 0, 1);
                    mActivity.mTopLine.setVisibility(View.VISIBLE);
                    mActivity.mBottomLine.setVisibility(View.VISIBLE);
                    mActivity.startAnimation(false);
                    break;
                case AGAIN_SHAKE:
                    mActivity.mVibrator.vibrate(300);
                    break;
                case END_SHAKE:
                    mActivity.isShake = false;
                    mActivity.startAnimation(true);
                    break;
            }
        }
    }

    private void startAnimation(boolean isBack) {
        int type = Animation.RELATIVE_TO_SELF;
        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -0.5f;
            topToY = 0;
            bottomFromY = 0.5f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -0.5f;
            bottomFromY = 0;
            bottomToY = 0.5f;
        }

        TranslateAnimation topAnim = new TranslateAnimation(
                type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(200);
        topAnim.setFillAfter(true);

        TranslateAnimation bottomAnim = new TranslateAnimation(
                type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(200);
        bottomAnim.setFillAfter(true);

        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mTopLine.setVisibility(View.GONE);
                    mBottomLine.setVisibility(View.GONE);
                }
            });
        }
        mTopLayout.startAnimation(topAnim);
        mBottomLayout.startAnimation(bottomAnim);
    }

}
