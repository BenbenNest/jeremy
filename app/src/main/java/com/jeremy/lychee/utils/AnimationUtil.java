package com.jeremy.lychee.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.jeremy.lychee.widget.DecelerateAccelerateInterpolator;

public class AnimationUtil {
    public enum Direction {
        Left, Right
    }

    public static void expand(View v, Direction direction) {
        ScaleAnimation animation;
        if (direction == Direction.Left) {
            animation = new ScaleAnimation(0, 1, 1, 1, 0, 0);
        } else {
            animation = new ScaleAnimation(0, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 1);
        }
        animation.setFillAfter(true);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        v.startAnimation(animation);
    }

    public static void collapse(View v, Direction direction) {
        ScaleAnimation animation;
        if (direction == Direction.Left) {
            animation = new ScaleAnimation(1, 0, 1, 1, 0, 0);
        } else {
            animation = new ScaleAnimation(1, 0, 1, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 1);
        }
        animation.setFillAfter(true);
        animation.setDuration(300);
        animation.setInterpolator(new DecelerateInterpolator());
        v.startAnimation(animation);
    }

    /**
     * 创建平移动画
     */
    public static Animation createTranslateAnim(Context context, int fromX, int toX) {
        TranslateAnimation tlAnim = new TranslateAnimation(fromX, toX, 0, 0);
        //自动计算时间
        long duration = (long) (Math.abs(toX - fromX) * 1.0f / DeviceUtil.getScreenWidth(context) * 4000);
        tlAnim.setDuration(duration);
        tlAnim.setInterpolator(new DecelerateAccelerateInterpolator());
        tlAnim.setFillAfter(true);

        return tlAnim;
    }

}
