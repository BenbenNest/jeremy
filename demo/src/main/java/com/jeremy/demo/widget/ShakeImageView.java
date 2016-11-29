package com.jeremy.demo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jeremy.demo.R;

/**
 * Created by benbennest on 16/9/28.
 */
public class ShakeImageView extends ImageView {

    private float x, y;
    private Matrix matrix = new Matrix();
    private Bitmap bitmap = null;


    public ShakeImageView(Context context) {
        super(context);
        init(context);
    }

    public ShakeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShakeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        BitmapDrawable bd = (BitmapDrawable) this.getDrawable();
        if (bd != null) {
            bitmap = bd.getBitmap();
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.net_error_image);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        matrix.postTranslate(x, y);
//        canvas.translate(x,y);
        canvas.drawBitmap(bitmap, matrix, null);

    }


}
