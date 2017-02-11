package com.jeremy.lychee.customview.live;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
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
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

import com.jeremy.lychee.activity.live.LiveRecordShortVideoActivity;
import com.jeremy.lychee.activity.live.LiveRecordingActivity;
import com.jeremy.lychee.activity.user.CreateArticleActivity;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.activity.user.TransArticleActivity;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.preference.CommonPreferenceUtil;
import com.jeremy.lychee.preference.UserPreference;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.utils.AppUtil;

import rx.functions.Action0;


public class VerticalMenu extends ViewGroup implements OnClickListener {


    private float bottomSpace;
    private float rightSpace;
    private Context mContext;
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
     * 点击子菜单项的回调接口
     */
    public interface OnMenuItemClickListener {
        void onClick(View view, int pos);
    }

    public void setOnMenuItemClickListener(
            OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }

    public VerticalMenu(Context context) {
        this(context, null);
    }

    public VerticalMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.bottomSpace = dp(10);
        this.rightSpace = dp(10);
    }

    public VerticalMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init(){
        this.setOnMenuItemClickListener((view, pos) -> {
            if (Session.isUserInfoEmpty()) {
//                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            if (view.getTag().equals("live")) {
                checkNetStatus(getContext(), new Action0() {
                    @Override
                    public void call() {
                        Intent intent = new Intent(mContext,
                                LiveRecordingActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            } else if (view.getTag().equals("radio")) {
                checkNetStatus(getContext(), new Action0() {
                    @Override
                    public void call() {
                        Intent intent = new Intent(mContext,
                                LiveRecordShortVideoActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            } else if (view.getTag().equals("recommend")) {
                Intent intent = new Intent(mContext,
                        TransArticleActivity.class);
                mContext.startActivity(intent);
            } else if (view.getTag().equals("write")) {
                Intent intent = new Intent(mContext,
                        CreateArticleActivity.class);
                mContext.startActivity(intent);
            }
        });
    }


    private static void checkNetStatus(Context context, Action0 cb) {
        if (AppUtil.isNetTypeMobile(context) &&
                !UserPreference.getInstance().getUseMobileNetEnabled()) {
            DialogUtil.showConfirmDialog(context, context.getString(com.jeremy.lychee.R.string.dialog_use_mobile_net_record),
                    context.getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                        cb.call();
                        UserPreference.getInstance().setUseMobileNetEnabled(true);
                        dialog.dismiss();
                    }, context.getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss
            );
            return;
        }
        cb.call();
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
    protected void onLayout(boolean changed, int ll, int tt, int rr, int bb) {
        if (changed) {
            layoutCButton();
            int count = getChildCount();
            View center = getChildAt(count - 1);
            for (int i = 0; i < count - 1; i++) {
                View child = getChildAt(i);

                if (!isInEditMode()) {
                    child.setVisibility(View.GONE);
                }

                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();


                int gap = 0;
                if (child instanceof ViewGroup) {
                    gap = Math.abs(center.getMeasuredWidth() - ((ViewGroup) child).getChildAt(1).getMeasuredWidth()) / 2;
                }
                int l = (int) (getMeasuredWidth() - width - rightSpace - gap);
                int t = getTargetTop(child, i);
                child.layout(l, t, l + width, t + height);
            }
        }
        if (!isInEditMode()) {
            //首次进入直播发现
            if (CommonPreferenceUtil.GetIntroLievDiscoveryFragment()) {
                CommonPreferenceUtil.setIntroLievDiscoveryFragment(false);
//            mArcMenu.autoOpen();
//                autoOpen();
            }
        }
    }


    private int getTargetTop(View child, int index) {

        int height = child.getMeasuredHeight();
        return (int) (-dp(26) + getMeasuredHeight() - (height + dp(9)) * (index + 2)); //26dp 起始距离,9是间距
    }


    /**
     * 定位主菜单按钮
     */
    private void layoutCButton() {
        mCButton = getChildAt(getChildCount() - 1);
        mCButton.setOnClickListener(this);


        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();

        int l = (int) (getMeasuredWidth() - width - rightSpace);
        int t = (int) (getMeasuredHeight() - height - bottomSpace);
        mCButton.layout(l, t, l + width, t + width);
    }

    @Override
    public void onClick(View v) {

        View iv_plus = v.findViewById(com.jeremy.lychee.R.id.center_menu_plus);
        Animation rotateAnimation = new RotateAnimation(0f, 45 * 5, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        if (mCurrentStatus == Status.OPEN) {
            rotateAnimation.setInterpolator(new ReverseInterpolator(rotateAnimation.getInterpolator()));
        }
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(250);
        iv_plus.startAnimation(rotateAnimation);


        if (mCurrentStatus == Status.CLOSE) {//toopen
            fadeIn();
        } else {//toclose
            fadeOut();
        }

        toggleMenu(250);

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

            AnimationSet animset = new AnimationSet(true);
            Animation tranAnim;

            int[] childLocation = new int[2];
            childView.getLocationOnScreen(childLocation);

            int[] triggerLocation = new int[2];
            mCButton.getLocationOnScreen(triggerLocation);

            int gap = triggerLocation[1] - childLocation[1];

            // to open
            if (mCurrentStatus == Status.CLOSE) {
                tranAnim = new TranslateAnimation(0, 0, gap, 0);
                animset.setInterpolator(new OvershootInterpolator(1.0f));
                childView.setClickable(true);
                childView.setFocusable(true);

            } else
            // to close
            {
                tranAnim = new TranslateAnimation(0, 0, 0, gap);
                animset.setInterpolator(new AnticipateInterpolator(1.0f));
                childView.setClickable(false);
                childView.setFocusable(false);

            }
            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration + i * 20);
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
            Animation anim;
            if (i == pos) {
                anim = scaleBigAnim(100);
                anim.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (mMenuItemClickListener != null) {
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

        }
        fadeOut();


        View iv_plus = mCButton.findViewById(com.jeremy.lychee.R.id.center_menu_plus);
        Animation animation = iv_plus.getAnimation();
        if (animation != null) {
            animation.setInterpolator(new ReverseInterpolator(animation.getInterpolator()));
            iv_plus.clearAnimation();
            iv_plus.startAnimation(animation);
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
            toggleMenu(250);

            fadeOut();
            View iv_plus = mCButton.findViewById(com.jeremy.lychee.R.id.center_menu_plus);
            Animation animation = iv_plus.getAnimation();
            if (animation != null) {
                animation.setInterpolator(new ReverseInterpolator(animation.getInterpolator()));
                iv_plus.clearAnimation();
                iv_plus.startAnimation(animation);
            }
            return true;
        }
        return false;
    }

//    public void autoOpen() {
//        mCButton.setVisibility(View.VISIBLE);
//        View iv_plus = mCButton.findViewById(R.id.center_menu_plus);
//        Animation rotateAnimation = new RotateAnimation(0f, 45 * 5, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setInterpolator(new LinearInterpolator());
//        rotateAnimation.setFillAfter(true);
//        rotateAnimation.setDuration(250);
//        iv_plus.startAnimation(rotateAnimation);
//
//        if (mCurrentStatus == Status.CLOSE) {//toopen
//            fadeIn();
//        } else {//toclose
//
//            fadeOut();
//        }
////        alphaAnimation.setFillAfter(true);
////        alphaAnimation.setDuration(250);
////        VerticalMenu.this.setAnimation(alphaAnimation);
////        alphaAnimation.setAnimationListener(new AnimationListener() {
////            @Override
////            public void onAnimationStart(Animation animation) {
////
////            }
////
////            @Override
////            public void onAnimationEnd(Animation animation) {
////                QEventBus.getEventBus().post(new LiveEvent.showIntroLiveDiscoveryOpen());
////            }
////
////            @Override
////            public void onAnimationRepeat(Animation animation) {
////
////            }
////        });
//
//        toggleMenu(250);
//    }

    public void show(boolean anim) {
        if (Session.isUserInfoEmpty()) {
            return;
        }
        if (this.getVisibility() != VISIBLE || !mCButton.isEnabled()) {
            int duration = anim ? 250 : 0;
            ScaleAnimation scaleAnim = new ScaleAnimation(0, 1.0f, 0, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            scaleAnim.setFillAfter(true);
            scaleAnim.setDuration(duration);
            setVisibility(VISIBLE);
            mCButton.setEnabled(true);
            mCButton.startAnimation(scaleAnim);
        }
    }

    public void hide(boolean anim) {
        if (this.getVisibility() == VISIBLE) {
            int duration = anim ? 250 : 0;
            ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0f, 1.0f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            scaleAnim.setFillAfter(true);
            scaleAnim.setDuration(duration);
            scaleAnim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mCButton.setEnabled(false);
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

    private void fadeIn() {
        Animation alphaAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                VerticalMenu.this.setBackgroundColor(AppUtil.transferAlpha(getResources().getColor(com.jeremy.lychee.R.color.translucent_white), interpolatedTime));
            }
        };
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(250);
        clearAnimation();
        startAnimation(alphaAnimation);
    }

    private void fadeOut() {
        Animation alphaAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                VerticalMenu.this.setBackgroundColor(AppUtil.transferAlpha(getResources().getColor(com.jeremy.lychee.R.color.translucent_white), interpolatedTime));
            }
        };
        alphaAnimation.setInterpolator(new ReverseInterpolator(new LinearInterpolator()));
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(250);
        clearAnimation();
        startAnimation(alphaAnimation);

    }

    public boolean isShown() {
        return getVisibility() != GONE;
    }

    private float dp(float dp) {
        return AppUtil.dip2px(getContext(), dp);
    }
}