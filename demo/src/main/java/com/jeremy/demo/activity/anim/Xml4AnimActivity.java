package com.jeremy.demo.activity.anim;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.jeremy.demo.R;

public class Xml4AnimActivity extends AppCompatActivity {
    private final String TAG = "Xml4AnimActivity";
    private ImageView mIvAnim;
    private ImageView mIvBlueBall;
    private float mScreenHeight;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, Xml4AnimActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml4_anim);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        mIvAnim = (ImageView) findViewById(R.id.iv_anim);
        mIvBlueBall = (ImageView) findViewById(R.id.iv_blue_ball);
    }

    public void scaleX(View view) {
        // 加载动画
        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.scalex);
        anim.setTarget(mIvAnim);
        anim.start();
    }

    public void scaleXandScaleY(View view) {
        // 加载动画
        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.scale);
        mIvAnim.setPivotX(0);
        mIvAnim.setPivotY(0);
        //显示的调用invalidate
//        mIvAnim.invalidate();
        anim.setTarget(mIvAnim);
        anim.start();
    }

    public void viewAnim(View view) {
        // need API12
        mIvBlueBall.animate()//
                .alpha(0)//
                .y(mScreenHeight).setDuration(1000)
                // need API 12
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "START");
                    }
                    // need API 16
                }).withEndAction(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "END");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIvBlueBall.setY(0);
                        mIvBlueBall.setAlpha(1.0f);
                    }
                });
            }
        }).start();
    }

    public void propertyValuesHolder(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(1000).start();
    }

    /**
     * 自由落体
     *
     * @param view
     */
    public void verticalRun(View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, mScreenHeight
                - mIvBlueBall.getHeight());
        animator.setTarget(mIvBlueBall);
        animator.setDuration(1000).start();
        // animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIvBlueBall.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }

    /**
     * 抛物线
     *
     * @param view
     */
    public void paowuxian(View view) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                Log.e(TAG, fraction * 3 + "");
                // x方向200px/s ，则y方向0.5 * g * t (g = 100px / s*s)
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 100 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                mIvBlueBall.setX(point.x);
                mIvBlueBall.setY(point.y);
            }
        });
    }

    public void fadeOut(View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mIvBlueBall, "alpha", 0.5f);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e(TAG, "onAnimationEnd");
                ViewGroup parent = (ViewGroup) mIvBlueBall.getParent();
//                if (parent != null)
//                    parent.removeView(mIvBlueBall);
            }
        });

        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.e(TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onAnimationRepeat");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e(TAG, "onAnimationEnd");
                ViewGroup parent = (ViewGroup) mIvBlueBall.getParent();
//                if (parent != null)
//                    parent.removeView(mIvBlueBall);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onAnimationCancel");
            }
        });
        anim.start();
    }

    public void rotateyAnimRun(final View view) {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(view, "zcq", 1.0F, 0.2F)//
                .setDuration(500);//
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });
    }

}
