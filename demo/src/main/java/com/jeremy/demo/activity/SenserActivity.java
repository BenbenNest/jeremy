package com.jeremy.demo.activity;

import android.content.Context;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.jeremy.demo.R;
import com.jeremy.library.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SenserActivity extends AppCompatActivity implements SensorEventListener {

    @BindView(R.id.iv_launcher)
    ImageView ivLauncher;

    /**
     * 当前x方向加速度
     */
    private float x = 0.0F;

    /**
     * 当前Y方向加速度
     */
    private float y = 0.0F;

    /**
     * 当前Z方向加速度
     */
    private float z = 0.0F;

    private float startX, startY;

    private Handler mHandler;
    private SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senser);
        ButterKnife.bind(this);
        mHandler = new Handler();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor_accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor_accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        startX = ivLauncher.getX();
        startY = ivLauncher.getY();
        mHandler.postDelayed(animRunnable, 50L);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        mHandler.removeCallbacks(animRunnable);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[SensorManager.DATA_X];
            y = event.values[SensorManager.DATA_Y];
            z = event.values[SensorManager.DATA_Z];

            Log.v("SENSOR_TEST", "x=" + x + "---" + "y=" + y + "---" + "z=" + z);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    Runnable animRunnable = new Runnable() {
        @Override
        public void run() {
            float posX = startX - 20 * x;
            float posY = startY + 20 * y;
            if (posX < 0) {
                posX = 0;
            }
            if (posX > ScreenUtil.getScreenWidth(SenserActivity.this)) {
                posX = ScreenUtil.getScreenWidth(SenserActivity.this) - ivLauncher.getWidth();
            }
            if (posY < 0) {
                posY = 0;
            }
            if (posY > ScreenUtil.getScreenHeight(SenserActivity.this)) {
                posX = ScreenUtil.getScreenHeight(SenserActivity.this) - ivLauncher.getHeight();
            }
            Matrix matrix = new Matrix();
            matrix.postTranslate(posX, posY);
//            ivLauncher.setImageMatrix(matrix);
//            ivLauncher.invalidate();
            ivLauncher.setX(posX);
            ivLauncher.setY(posY);
            mHandler.postDelayed(animRunnable, 50L);
        }
    };

}
