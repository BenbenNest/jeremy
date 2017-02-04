package com.jeremy.demo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jeremy.demo.R;
import com.jeremy.demo.widget.HeartImageView;
import com.jeremy.library.utils.ScreenUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BubbleHeartActivity extends Activity implements HeartImageView.OnAnimEnd {

    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.iv_heart)
    ImageView ivHeart;

    private int[] colors = new int[]{Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bubble_heart);
        ButterKnife.bind(this);
//        init();
    }


    @OnClick({R.id.iv_heart})
    void onClick(View view) {
        HeartImageView heartImageView = new HeartImageView(this);
        heartImageView.setColor(colors[new Random().nextInt(4)]);
//        root.addView(heartImageView);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        root.addView(heartImageView, params);
        heartImageView.setListener(this);
        heartImageView.startAnim();
    }


    private void init() {
//        ivHeart.setColor(Color.RED);


        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ivHeart, View.SCALE_X, 0.5f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ivHeart, View.SCALE_Y, 0.5f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(ivHeart, View.ALPHA, 0.5f, 1f);
        AnimatorSet enterAnimatorSet = new AnimatorSet();
        enterAnimatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
        enterAnimatorSet.setDuration(1000);
        enterAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                ivHeart.setVisibility(View.VISIBLE);
            }
        });
        enterAnimatorSet.start();


        int width = ScreenUtil.getScreenWidth(this);
        int height = ScreenUtil.getScreenHeight(this);

        int[] randomArray = {0, 1};
        int point1x = 0;
        int point1y = 0;
        int point2x = 0;
        int point2y = 0;
        if (randomArray[new Random().nextInt(2)] == 0) {
            point1x = new Random().nextInt((width / 2 - ScreenUtil.dip2px(this, 50)));
        } else {
            point1x = new Random().nextInt((width / 2 - ScreenUtil.dip2px(this, 50))) + (width / 2 + ScreenUtil.dip2px(this, 50));
        }
        if (randomArray[new Random().nextInt(2)] == 0) {
            point2x = new Random().nextInt((width / 2 - ScreenUtil.dip2px(this, 50)));
        } else {
            point2x = new Random().nextInt((width / 2 - ScreenUtil.dip2px(this, 50))) + (width / 2 + ScreenUtil.dip2px(this, 50));
        }
        point1y = new Random().nextInt(height / 2 - ScreenUtil.dip2px(this, 50)) + (height / 2 + ScreenUtil.dip2px(this, 50));
        point2y = -new Random().nextInt(point1y) + point1y;

        point1x = width / 2;
        point2x = width / 4;
        point1y = height / 2;
        point2y = height / 4;

        int endX = new Random().nextInt(ScreenUtil.dip2px(this, 100)) + (width / 2 - ScreenUtil.dip2px(this, 100));
        int endY = -new Random().nextInt(point2y) + point2y;
        endX = 0;
        endY = 0;
        ValueAnimator translateAnimator = ValueAnimator.ofObject(new HeartEvaluator(new PointF(point1x, point1y), new PointF(point2x, point2y)), new PointF(width / 2 - ivHeart.getWidth() / 2, height - ivHeart.getHeight()), new PointF(endX, endY));
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                ivHeart.setX(pointF.x);
                ivHeart.setY(pointF.y);
            }
        });
        translateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                removeView(imageView);
            }
        });
        TimeInterpolator[] timeInterpolator = {new LinearInterpolator(), new AccelerateDecelerateInterpolator(), new DecelerateInterpolator(), new AccelerateInterpolator()};
        translateAnimator.setInterpolator(timeInterpolator[new Random().nextInt(timeInterpolator.length)]);
        ObjectAnimator translateAlphaAnimator = ObjectAnimator.ofFloat(ivHeart, View.ALPHA, 1f, 0f);
        translateAlphaAnimator.setInterpolator(new DecelerateInterpolator());
        AnimatorSet translateAnimatorSet = new AnimatorSet();
        translateAnimatorSet.playTogether(translateAnimator, translateAlphaAnimator);
        translateAnimatorSet.setDuration(5000);
        translateAnimatorSet.start();
    }

    @Override
    public void onAnimEnd(HeartImageView heartImageView) {
        root.removeView(heartImageView);
    }


    private class HeartEvaluator implements TypeEvaluator<PointF> {
        //贝塞尔曲线参考点1
        PointF f1;
        //贝塞尔曲线参考点2
        PointF f2;

        public HeartEvaluator(PointF f1, PointF f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float leftTime = 1f - fraction;
            PointF newPointF = new PointF();
            newPointF.x = startValue.x * leftTime * leftTime * leftTime
                    + f1.x * 3 * leftTime * leftTime * fraction
                    + f2.x * 3 * leftTime * fraction * fraction
                    + endValue.x * fraction * fraction * fraction;
            newPointF.y = startValue.y * leftTime * leftTime * leftTime
                    + f1.y * 3 * leftTime * leftTime * fraction
                    + f2.y * 3 * leftTime * fraction * fraction
                    + endValue.y * fraction * fraction * fraction;
            return newPointF;
        }
    }

}
