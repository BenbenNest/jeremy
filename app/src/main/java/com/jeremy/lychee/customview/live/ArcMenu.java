package com.jeremy.lychee.customview.live;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.preference.CommonPreferenceUtil;
import com.jeremy.lychee.utils.AppUtil;


public class ArcMenu extends ViewGroup implements OnClickListener {
    private static final int POS_LEFT_TOP = 0;
    private static final int POS_LEFT_BOTTOM = 1;
    private static final int POS_RIGHT_TOP = 2;
    private static final int POS_RIGHT_BOTTOM = 3;

    private Position mPosition = Position.RIGHT_BOTTOM;
    private int mRadius;
    /**
     * 菜单的状态
     */
    private Status mCurrentStatus = Status.CLOSE;
    /**
     * 菜单的主按钮
     */
    private View mCButton;

    private OnMenuItemClickListener mMenuItemClickListener;

    public enum Status {
        OPEN, CLOSE
    }

    /**
     * 菜单的位置枚举类
     */
    public enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * 点击子菜单项的回调接口
     */
    public interface OnMenuItemClickListener {
        void onClick(View view, int pos);
    }

    public void setOnMenuItemClickListener(
            OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                100, getResources().getDisplayMetrics());

        // 获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                com.jeremy.lychee.R.styleable.ArcMenu, defStyle, 0);

        int pos = a.getInt(com.jeremy.lychee.R.styleable.ArcMenu_position, POS_RIGHT_BOTTOM);
        switch (pos) {
            case POS_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        mRadius = (int) a.getDimension(com.jeremy.lychee.R.styleable.ArcMenu_radius, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                        getResources().getDisplayMetrics()));


        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            // 测量child
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutCButton();

            int count = getChildCount();

            int j = 1;
            for (int i = 0; i < count - 1; i++) {
                View child = getChildAt(i);

                if (!isInEditMode()) {
                    child.setVisibility(View.GONE);
                }

                int cl = (int) (mRadius * Math.sin(Math.PI / 12 * (j)
                ));
                int ct = (int) (mRadius * Math.cos(Math.PI / 12 * (j)
                ));

                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();

                // 如果菜单位置在底部 左下，右下
                if (mPosition == Position.LEFT_BOTTOM
                        || mPosition == Position.RIGHT_BOTTOM) {
                    ct = getMeasuredHeight() - cHeight - ct - 36;
                }
                // 右上，右下
                if (mPosition == Position.RIGHT_TOP
                        || mPosition == Position.RIGHT_BOTTOM) {
                    cl = getMeasuredWidth() - cWidth - cl - 36;
                }
                child.layout(cl, ct, cl + cWidth, ct + cHeight);
                j = 5;
            }

        }
        if (!isInEditMode()) {
            //首次进入直播发现
            if (CommonPreferenceUtil.GetIntroLievDiscoveryFragment()) {
                CommonPreferenceUtil.setIntroLievDiscoveryFragment(false);
//            mArcMenu.autoOpen();
                autoOpen();
            }
        }

    }


    /**
     * 定位主菜单按钮
     */
    private void layoutCButton() {
        mCButton = getChildAt(2);
        mCButton.setOnClickListener(this);

        int l = 0;
        int t = 0;

        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();

        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width - 36;
                t = getMeasuredHeight() - height - 36;
                break;
        }
        mCButton.layout(l, t, l + width, t + width);
    }

    @Override
    public void onClick(View v) {
        // mCButton = findViewById(R.id.id_button);
        // if(mCButton == null)
        // {
        // mCButton = getChildAt(0);
        // }

//        rotateCButton(v, 0f, 360f, 300);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setInterpolator(new MyInterpolator());
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(400);

        mCButton.startAnimation(scaleAnim);

        Animation alphaAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                ArcMenu.this.setBackgroundColor(AppUtil.transferAlpha(Color.WHITE, interpolatedTime));
            }
        };
        if (mCurrentStatus == Status.CLOSE) {//toopen
            alphaAnimation.setInterpolator(new LinearInterpolator());
        } else {//toclose
            alphaAnimation.setInterpolator(new ReverseInterpolator(new LinearInterpolator()));
        }

        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(400);
        ArcMenu.this.setAnimation(alphaAnimation);

        toggleMenu(400);

    }

    class MyInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float x) {
            return -4 * (x - 0.5f) * (x - 0.5f) + 1;
        }
    }

    /**
     * 切换菜单
     */
    public void toggleMenu(int duration) {
        // 为menuItem添加平移动画和旋转动画
        int count = getChildCount();

        int j = 1;
        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i);
            childView.setVisibility(View.VISIBLE);

            // end 0 , 0
            // start
            int cl = (int) (mRadius * Math.sin(Math.PI / 12 * j));
            int ct = (int) (mRadius * Math.cos(Math.PI / 12 * j));

            int xflag = 1;
            int yflag = 1;

            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.LEFT_BOTTOM) {
                xflag = -1;
            }

            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.RIGHT_TOP) {
                yflag = -1;
            }

            AnimationSet animset = new AnimationSet(true);
            Animation tranAnim = null;

            // to open
            if (mCurrentStatus == Status.CLOSE) {
                tranAnim = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                animset.setInterpolator(new OvershootInterpolator());
                childView.setClickable(true);
                childView.setFocusable(true);
                ((ImageView) mCButton).setImageResource(com.jeremy.lychee.R.drawable.living_found_close);
//                this.setBackgroundColor(Color.WHITE);
//                this.setAlpha(0.88f);


            } else
            // to close
            {
                tranAnim = new TranslateAnimation(0, xflag * cl, 0, yflag * ct);
                animset.setInterpolator(new AnticipateInterpolator());
                childView.setClickable(false);
                childView.setFocusable(false);

            }
            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration);
            tranAnim.setStartOffset((i * 100) / count);

            tranAnim.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    mCButton.setClickable(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mCButton.setClickable(true);
                    if (mCurrentStatus == Status.CLOSE) {
                        childView.setVisibility(View.GONE);
                        ((ImageView) mCButton).setImageResource(com.jeremy.lychee.R.drawable.live_icon_found_movie);
                    } else {
                        if (CommonPreferenceUtil.GetIntroLievDiscoveryFragment()) {
                            CommonPreferenceUtil.setIntroLievDiscoveryFragment(false);
                        }
                    }
                }
            });

            animset.addAnimation(tranAnim);
            childView.startAnimation(animset);

            final int pos = i + 1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    menuItemAnim(pos - 1);
                    changeStatus();


                }
            });
            j = 5;
        }
        // 切换菜单状态
        changeStatus();

    }

    /**
     * 添加menuItem的点击动画
     *
     * @param
     */
    private void menuItemAnim(int pos) {
        for (int i = 0; i < getChildCount() - 1; i++) {

            View childView = getChildAt(i);
            Animation anim = null;
            if (i == pos) {
                anim = scaleBigAnim(100);
                anim.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (mMenuItemClickListener != null) {
                            ((ImageView) mCButton).setImageResource(com.jeremy.lychee.R.drawable.live_icon_found_movie);
                            mMenuItemClickListener.onClick(childView, pos);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            } else {

                anim = scaleSmallAnim(100);
            }

            childView.startAnimation(anim);
            childView.setClickable(false);
            childView.setFocusable(false);
            Animation alphaAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);
                    ArcMenu.this.setBackgroundColor(AppUtil.transferAlpha(Color.WHITE, interpolatedTime));
                }
            };

            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(100);
            ArcMenu.this.setAnimation(alphaAnimation);
        }


    }

    private Animation scaleSmallAnim(int duration) {

        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;

    }

    /**
     * 为当前点击的Item设置变大和透明度降低的动画
     *
     * @param duration
     * @return
     */
    private Animation scaleBigAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0.0f);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;


    }

    /**
     * 切换菜单状态
     */
    private void changeStatus() {
        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN
                : Status.CLOSE);
    }

    public boolean isOpen() {
        return mCurrentStatus == Status.OPEN;
    }

    private void rotateCButton(View v, float start, float end, int duration) {

        RotateAnimation anim = new RotateAnimation(start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true);
//        v.startAnimation(anim);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOpen()) {
            toggleMenu(400);

            ScaleAnimation alphaAnimation = null;

            alphaAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f) {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    ArcMenu.this.setBackgroundColor(AppUtil.transferAlpha(Color.WHITE, interpolatedTime));
                }
            };

            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(400);
            ArcMenu.this.setAnimation(alphaAnimation);
            return true;
        }
        return false;
    }

    public void autoOpen() {
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setInterpolator(new MyInterpolator());
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(400);

        mCButton.startAnimation(scaleAnim);


        ScaleAnimation alphaAnimation = null;
        if (mCurrentStatus == Status.CLOSE) {//toopen
            alphaAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f) {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);
                    ArcMenu.this.setBackgroundColor(AppUtil.transferAlpha(Color.WHITE, interpolatedTime));
                }
            };
        } else {//toclose
            alphaAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f) {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);

                    ArcMenu.this.setBackgroundColor(AppUtil.transferAlpha(Color.WHITE, interpolatedTime));

                }
            };
        }
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(400);
        ArcMenu.this.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                QEventBus.getEventBus().post(new LiveEvent.showIntroLiveDiscoveryOpen());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        toggleMenu(400);
    }

    public void show() {
        if (this.getVisibility() != VISIBLE) {
            ScaleAnimation scaleAnim = new ScaleAnimation(0, 1.0f, 0, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            scaleAnim.setFillAfter(true);
            scaleAnim.setDuration(300);
            setVisibility(VISIBLE);
            mCButton.startAnimation(scaleAnim);
        }
    }

    public void hide() {
        if (this.getVisibility() == VISIBLE) {
            ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0f, 1.0f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            scaleAnim.setFillAfter(true);
            scaleAnim.setDuration(300);
            scaleAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mCButton.startAnimation(scaleAnim);
        }
    }

    public boolean isShown() {
        return getVisibility() != GONE;
    }
}