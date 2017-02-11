package com.jeremy.lychee.customview.live;

import android.view.animation.Interpolator;

/**
 * Created by houwenchang
 * 16/3/8.
 */
public class ReverseInterpolator implements Interpolator {

    private final Interpolator wrappedInterpolator;

    public ReverseInterpolator(Interpolator wrappedInterpolator) {
        this.wrappedInterpolator = wrappedInterpolator;
    }

    @Override
    public float getInterpolation(float paramFloat) {
        return Math.abs(wrappedInterpolator.getInterpolation(paramFloat) - 1f);
    }
}