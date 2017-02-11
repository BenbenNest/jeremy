package com.jeremy.lychee.customview.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ViewServer;

/**
 * Created by wangp on 15-12-9.
 */
public class EditChannelIcon extends ImageView {

    private final static int CONNER_PADING =
            DensityUtils.dip2px(ContentApplication.getInstance().getBaseContext(), 0);
    private final static int CONNER_IMG_SIZE_H =
            DensityUtils.dip2px(ContentApplication.getInstance().getBaseContext(), 13);
    private final static int CONNER_IMG_SIZE_V =
            DensityUtils.dip2px(ContentApplication.getInstance().getBaseContext(), 10);

    public EditChannelIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 0, CONNER_PADING, CONNER_PADING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(0x6affffff);
        p.setStyle(Paint.Style.FILL);
        canvas.drawArc(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), 20, 140, false, p);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), com.jeremy.lychee.R.mipmap.zpd_icon_photo);
        canvas.drawBitmap(ViewServer.zoomBitmap(bitmap, CONNER_IMG_SIZE_H, CONNER_IMG_SIZE_V),
                (getWidth() - CONNER_IMG_SIZE_H) / 2,
                getHeight() - CONNER_IMG_SIZE_V - AppUtil.dpToPx(getContext(), 5),
                new Paint());

    }
}
