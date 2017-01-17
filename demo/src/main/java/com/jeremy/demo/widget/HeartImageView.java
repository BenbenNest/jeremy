package com.jeremy.demo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.jeremy.demo.R;
import com.jeremy.library.util.ScreenUtil;

import java.util.Random;

/**
 * Created by benbennest on 16/12/12.
 */

public class HeartImageView extends ImageView {
    private Context context;
    private Bitmap bitmap_heart;
    int width, height;
    Random random;
    private OnAnimEnd onAnimEnd;

    public HeartImageView(Context context) {
        this(context, null);
    }

    public HeartImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        bitmap_heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        random = new Random();
        width = ScreenUtil.getScreenWidth(context);
        height = ScreenUtil.getScreenHeight(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(bitmap_heart.getWidth(), bitmap_heart.getHeight());
    }

    public void setColor(int color) {
        setImageBitmap(createColor(color));
    }

    private Bitmap createColor(int color) {
        int heartWidth = bitmap_heart.getWidth();
        int heartHeight = bitmap_heart.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(heartWidth, heartHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap_heart, 0, 0, paint);
        canvas.drawColor(color, PorterDuff.Mode.SRC_ATOP);
        canvas.setBitmap(null);
        return newBitmap;
    }

    public void setListener(OnAnimEnd onAnimEnd) {
        this.onAnimEnd = onAnimEnd;
    }

    public void startAnim() {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this, View.SCALE_X, 0.5f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.5f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, 0.5f, 1f);
        AnimatorSet enterAnimatorSet = new AnimatorSet();
        enterAnimatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
        enterAnimatorSet.setDuration(1000);
        enterAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                HeartImageView.this.setVisibility(View.VISIBLE);
            }
        });
        enterAnimatorSet.start();

        PointF start = new PointF();
        PointF end = new PointF();
        PointF p1 = new PointF();
        PointF p2 = new PointF();

        if (random.nextInt(2) == 0) {
            p1.x = random.nextInt(width / 2);
            p1.y = random.nextInt(height / 2) + height / 2 - 50;
            p2.x = random.nextInt(width / 2);
            p2.y = random.nextInt(height / 2);
        } else {
            p1.x = random.nextInt(width / 2) + width / 2;
            p1.y = random.nextInt(height / 2) + height / 2 - 50;
            p2.x = random.nextInt(width / 2) + width / 2;
            p2.y = random.nextInt(height / 2);
        }

        start.x = (width - bitmap_heart.getWidth()) / 2;
        start.y = height - bitmap_heart.getHeight();
        end.x = random.nextInt(width);
        end.y = 0;


        ValueAnimator translateAnimator = ValueAnimator.ofObject(new HeartEvaluator(p1, p2), start, end);
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                HeartImageView.this.setX(pointF.x);
                HeartImageView.this.setY(pointF.y);
            }
        });
        translateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                onAnimEnd.onAnimEnd(HeartImageView.this);
            }
        });
        translateAnimator.setDuration(3000);
        TimeInterpolator[] timeInterpolator = {new LinearInterpolator(), new AccelerateDecelerateInterpolator(), new DecelerateInterpolator(), new AccelerateInterpolator()};
        translateAnimator.setInterpolator(timeInterpolator[random.nextInt(timeInterpolator.length)]);
        ObjectAnimator translateAlphaAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f);
        translateAlphaAnimator.setInterpolator(new DecelerateInterpolator());
        AnimatorSet translateAnimatorSet = new AnimatorSet();
        translateAnimatorSet.playTogether(translateAnimator, translateAlphaAnimator);
        translateAnimatorSet.setDuration(5000);
        translateAnimatorSet.setStartDelay(1000);
        translateAnimatorSet.start();

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
            Log.d("BeiSaier", "start" + startValue.toString());

            Log.d("BeiSaier", newPointF.toString());

            return newPointF;
        }
    }

    public interface OnAnimEnd {
        public void onAnimEnd(HeartImageView imageView);
    }

}