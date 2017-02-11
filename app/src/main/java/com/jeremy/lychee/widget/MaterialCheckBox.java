package com.jeremy.lychee.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;


/**
 * 看点3.0版本的checkbox
 *
 * @author wangxunlong
 */
public class MaterialCheckBox extends CustomView implements Checkable {

    /**
     * 被选中时小球的颜色
     */
    private int ballCheckedColor = getContext().getResources().getColor(com.jeremy.lychee.R.color.setting_checkbox_checked_ball_color);
    /**
     * 被选中时线条的颜色
     */
    private int checkedLineColor = getContext().getResources().getColor(com.jeremy.lychee.R.color.setting_checkbox_checked_line_color);
    /**
     * 未选中时线的颜色
     */
    private int uncheckedLineColor = getContext().getResources().getColor(com.jeremy.lychee.R.color.setting_checkbox_disable_color);
    private Ball ball;
    private boolean check = false;
    private volatile boolean eventCheck = false;
    private OnCheckListener onCheckListener;
    private Bitmap bitmap;
    private boolean suddenly;
    boolean placedBall = false;
    private Canvas temp;

    public MaterialCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!isEnabled()) {
                    return;
                }
                if (check) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
            }
        });
    }

    /**
     * 第一次进来放置小球
     */
    private void placeBall() {
        ball.setX(getHeight() / 4 - ball.getWidth() / 2);
        ball.xIni = ball.getX();
        ball.xFin = getWidth() - getHeight() / 4 - ball.getWidth() / 2;
        ball.xCen = getWidth() / 2 - ball.getWidth() / 2;
        placedBall = true;
        ball.animate(suddenly);
    }

    protected void setAttributes(AttributeSet attrs) {
        setBackgroundResource(com.jeremy.lychee.R.drawable.background_transparent);
        setMinimumHeight(dip2px(getContext(), 48f));
        setMinimumWidth(dip2px(getContext(), 60));
        int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML, "background", -1);
        if (bacgroundColor != -1) {
            setBackgroundColor(getResources().getColor(bacgroundColor));
        } else {
            int background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            if (background != -1) {
                setBackgroundColor(background);
            }
        }
        check = attrs.getAttributeBooleanValue(MATERIALDESIGNXML, "check", false);
        eventCheck = check;
        ball = new Ball(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dip2px(getContext(), 20f), dip2px(
                getContext(), 20f));
        params.addRule(CENTER_VERTICAL, TRUE);
        ball.setLayoutParams(params);
        addView(ball);

    }

    /**
     * 设置checkbox是否被选中
     * @param check 是否被选中
     * @param suddenly true的话没有过渡动画效果，一般用于初始化时，false有动画效果
     */
    public void setChecked(boolean check, boolean suddenly) {
        this.suddenly = suddenly;
        invalidate();
        this.check = check;
        this.eventCheck = check;
        ball.animate(suddenly);
    }
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 给一个默认的选中方法，有动画效果
     * @param check
     */
    public void setChecked(boolean check) {
        setChecked(check, false);
    }


    @Override
    public void setEnabled(boolean enabled) {
        if (ball != null) {
            ball.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!placedBall) {
            placeBall();
            placedBall = true;
        }
        if (null == bitmap) {
            bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            temp = new Canvas(bitmap);
        }
        // 这里的代码是画背景的那条线
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor((eventCheck && isEnabled()) ? checkedLineColor : isEnabled()?uncheckedLineColor:uncheckedLineColor);
        paint.setStrokeWidth(dip2px(getContext(), 2f));
        temp.drawLine(getHeight() / 4, getHeight() / 2, getWidth() - getHeight() / 4, getHeight() / 2, paint);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
        super.onDraw(canvas);

    }

    @Override
    public void setBackgroundColor(int color) {
        ballCheckedColor = color;
        if (isEnabled()) {
            beforeBackground = ballCheckedColor;
        }

    }

    class Ball extends View {

        float xIni, xFin, xCen;
        private ObjectAnimator objectAnimator;

        public Ball(Context context) {
            super(context);
            setBackgroundResource(com.jeremy.lychee.R.drawable.background_switch_ball_uncheck);
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            // 当不可操作的时候要求改变小球的颜色
            changeBackground();
        }

        /**
         * 根据选中状态改变小球的颜色
         */
        public void changeBackground() {
            if (eventCheck && isEnabled()) {
                setBackgroundResource(com.jeremy.lychee.R.drawable.background_checkbox);
                // 获取背景layer 改变小球的填充色
                LayerDrawable layer = (LayerDrawable) getBackground();
                GradientDrawable shape = (GradientDrawable) layer.findDrawableByLayerId(com.jeremy.lychee.R.id.shape_bacground);
                shape.setColor(ballCheckedColor);
            } else if (!isEnabled()) {
                /**
                 * 设置小球 enable状态
                 */
                setBackgroundResource(com.jeremy.lychee.R.drawable.background_switch_ball_disable);
            } else {
                /**
                 * 设置小球未选中状态
                 */
                setBackgroundResource(com.jeremy.lychee.R.drawable.background_switch_ball_uncheck);
            }
        }

        /**
         * 根据是否需要动画执行方法
         * @param suddenly true为不需要动画
         */
        public void animate(boolean suddenly) {
            if (suddenly) {
                animateCheck(0);
            } else {
                animateCheck(300);
            }
        }

        /**
         * 根据给定的时间执行过度动画
         * @param time 时间
         */
        private void animateCheck(int time) {
            changeBackground();
            if (suddenly) {
                // 根据选中状态设置小球的x坐标
                if (eventCheck) {
                    setX(ball.xFin);
                } else {
                    setX(ball.xIni);
                }
                //非动画状态时都不是用户行为，不需要回调
//                if (onCheckListener != null) {
//                    onCheckListener.onCheck(eventCheck, suddenly);
//                }
                return;
            }
            // 根据选中状态初始化动画 这里的动画是修改小球的x坐标
            if (eventCheck) {
                objectAnimator = ObjectAnimator.ofFloat(this, "x", ball.xFin);
            } else {
                objectAnimator = ObjectAnimator.ofFloat(this, "x", ball.xIni);
            }
            objectAnimator.setDuration(time);
            objectAnimator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator arg0) {

                }

                @Override
                public void onAnimationRepeat(Animator arg0) {

                }

                @Override
                public void onAnimationEnd(Animator arg0) {
                    // 动画执行完成之后回调onCheck
                    if (onCheckListener != null) {
                        onCheckListener.onCheck(eventCheck, suddenly);
                    }
                }

                @Override
                public void onAnimationCancel(Animator arg0) {

                }
            });
            objectAnimator.start();
        }

    }

    /**
     * 设置回调
     * @param onCheckListener
     */
    public void setOncheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {
        /**
         * checkbox回调
         * @param check 是否选中
         * @param isSuddenly 是否是没有动画回调的
         */
        public void onCheck(boolean check, boolean isSuddenly);
    }

    @Override
    public boolean isChecked() {
        return check;
    }

    @Override
    public void toggle() {
        setChecked(!check);
    }

}
