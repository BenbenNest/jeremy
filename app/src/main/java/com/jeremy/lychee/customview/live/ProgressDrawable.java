package com.jeremy.lychee.customview.live;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by houwenchang
 * 15/12/28.
 */
public class ProgressDrawable extends Drawable {
    private float progress;
    private Rect bound;
    private Rect clipBound;
    private int width;

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bound = bounds;
        clipBound = bounds;
        width = bounds.width();
    }

    @Override
    public void draw(Canvas canvas) {
        clipBound.right = (int) (width * progress);
        canvas.clipRect(clipBound);
        canvas.drawColor(Color.parseColor("#88999999"));
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
