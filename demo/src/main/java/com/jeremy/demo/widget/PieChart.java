package com.jeremy.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by benbennest on 16/11/17.
 */

public class PieChart extends View {
    private int[] colors = new int[]{Color.parseColor("#b284f3"), Color.parseColor("#fe7c2e"), Color.parseColor("#7494d6"), Color.parseColor("#42c0fa")};
    private String[] titles = new String[]{"紫色区域", "橙色区域", "深蓝色区域", "浅蓝色区域"};
    private int[] startAngles = new int[]{0, 100, 210, 280, 360};
    private Context mContext;
    private int radius = 300;
    private Paint mPaint;
    private Paint mTextPaint;
    private Region[] regions = new Region[4];
    private int curRegion = -1;
    private int offset = 30;

    public PieChart(Context context) {
        super(context);
        init(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(colors[0]);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(40);
        mTextPaint.setStrokeWidth(1);
        for (int i = 0; i < 4; i++) {
            regions[i] = new Region();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(android.R.color.transparent));//设置背景透明
        for (int i = 0; i < 4; i++) {
            drawArc(canvas, i);
        }
    }

    private void drawArc(Canvas canvas, int pos) {
        Path path = new Path();
        float centerX = radius, centerY = radius;
        Region global = new Region(0, 0, radius * 2, radius * 2);
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        mPaint.setColor(colors[pos]);
        int middleAngle = startAngles[pos] + (startAngles[pos + 1] - startAngles[pos]) / 2;
        if (curRegion == pos) {
            centerX = radius + (float) (offset * Math.cos(middleAngle * Math.PI / 180));
            centerY = radius + (float) (offset * Math.sin(middleAngle * Math.PI / 180));
            rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
            canvas.drawArc(rectF, startAngles[pos], startAngles[pos + 1] - startAngles[pos], true, mPaint);
//            path.addArc(rectF, 0, 100);
            path.moveTo(centerX, centerY); //圆心
            path.lineTo((float) (centerX + radius * Math.cos(startAngles[pos] * Math.PI / 180)),
                    (float) (centerY + radius * Math.sin(startAngles[pos] * Math.PI / 180)));
            path.lineTo((float) (centerX + radius * Math.cos(startAngles[pos + 1] * Math.PI / 180)),
                    (float) (centerY + radius * Math.sin(startAngles[pos + 1] * Math.PI / 180)));
            path.close();
//            canvas.drawTextOnPath(titles[pos], path, offset, offset, mTextPaint);
//        canvas.drawPath(path, mPaint);
            regions[pos].setPath(path, global);

            Path path1 = new Path();
            path1.addArc(rectF, startAngles[pos], startAngles[pos + 1] = startAngles[pos]);
            canvas.drawTextOnPath(titles[pos], path1, offset, offset, mTextPaint);

            float textX1 = radius + (float) (radius / 2 * Math.cos(startAngles[pos] * Math.PI / 180));
            float textY1 = radius + (float) (radius / 2 * Math.cos(startAngles[pos] * Math.PI / 180));
            float textXCenter = radius + (float) (radius / 2 * Math.cos(middleAngle * Math.PI / 180));
            float textYCenter = radius + (float) (radius / 2 * Math.cos(middleAngle * Math.PI / 180));
            float textX2 = radius + (float) (radius / 2 * Math.cos(startAngles[pos + 1] * Math.PI / 180));
            float textY2 = radius + (float) (radius / 2 * Math.cos(startAngles[pos + 1] * Math.PI / 180));
            Path textPath = new Path();
            textPath.moveTo(textX1, textY1);
//            textPath.lineTo(textXCenter, textYCenter);
            textPath.lineTo(textX2, textY2);
            canvas.drawTextOnPath(titles[pos], textPath, offset, offset, mTextPaint);

        } else {
            canvas.drawArc(rectF, startAngles[pos], startAngles[pos + 1] - startAngles[pos], true, mPaint);

            RectF rectText = new RectF(radius * 0.3f, radius * 0.3f, radius * 1.7f, radius * 1.7f);

            path.addArc(rectText, startAngles[pos], startAngles[pos + 1] - startAngles[pos]);
//            path.moveTo(radius, radius); //圆心
//            path.lineTo((float) (radius + radius * Math.cos(startAngles[pos] * Math.PI / 180)),
//                    (float) (radius + radius * Math.sin(startAngles[pos] * Math.PI / 180)));
//            path.lineTo((float) (radius + radius * Math.cos(startAngles[pos + 1] * Math.PI / 180)),
//                    (float) (radius + radius * Math.sin(startAngles[pos + 1] * Math.PI / 180)));
//            path.close();
            canvas.drawPath(path, mPaint);
            canvas.drawTextOnPath(titles[pos], path, offset, 0, mTextPaint);
            regions[pos].setPath(path, global);

//            Path path1 = new Path();
//            path1.addArc(rectF, startAngles[pos], startAngles[pos + 1] = startAngles[pos]);
//            canvas.drawTextOnPath(titles[pos], path1, offset, offset, mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (int i = 0; i < regions.length; i++) {
            if (regions[i].contains((int) event.getRawX(), (int) event.getRawY())) {
                curRegion = i;
                Toast.makeText(mContext, titles[i], Toast.LENGTH_SHORT).show();
                break;
            }
            curRegion = -1;
        }
        curRegion = -1;
        invalidate();
        return super.onTouchEvent(event);
    }

    //    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        this.fontSize = scalaFonts((int) (hollowRadius / this.pieName.length() * 0.6f) * 2);
//
//        //画最下一层的圆圈
//        this.bgPaint.setColor(getResources().getColor(R.color.theme_main_blue));
//        this.bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        canvas.drawCircle(centerX, centerY, radius, bgPaint);
//
//        //画百分比圆弧
//        this.drawArc(canvas);
//
//        this.bgPaint.setColor(getResources().getColor(R.color.pageBgColor));
//        this.bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        canvas.drawCircle(centerX, centerY, hollowRadius, bgPaint);
//
//        this.bgPaint.setStyle(Paint.Style.STROKE);
//        this.bgPaint.setColor(getResources().getColor(R.color.black_overlay));
//        canvas.drawCircle(centerX, centerY, hollowRadius * 0.9f, bgPaint);
//
//        drawText(canvas);
//
//        canvas.restore();
//    }
//
//    public void drawText(Canvas canvas) {
//        int x = getWidth();
//        int y = getHeight() - lableHeight;
//
//        this.textPaint.setTextSize(this.fontSize);
//        float tX = (x - getFontlength(this.textPaint, this.pieName)) / 2;
//        float tY = (y - getFontHeight(this.textPaint)) / 2 + getFontLeading(this.textPaint);
//        canvas.drawText(this.pieName, tX, tY, this.textPaint);
//    }
//
//    public void drawArc(Canvas canvas) {
//        RectF rect = new RectF();
//        rect.left = centerX - radius;
//        rect.top = centerY - radius;
//        rect.right = centerX + radius;
//        rect.bottom = centerY + radius;
//
//
//        RectF rectText = new RectF();
//        rectText.left = centerX - (radius - (radius * 0.4f));
//        rectText.top = centerY - (radius - (radius * 0.4f));
//        rectText.right = centerX + (radius - (radius * 0.4f));
//        rectText.bottom = centerY + (radius - (radius * 0.4f));
//
//        if (this.data == null || this.data.isEmpty()) {
//            Log.w(TAG, "没有可以绘制的数据");
//            arcPaint.setColor(arcColos[0]);
//            canvas.drawArc(rect, //弧线所使用的矩形区域大小
//                    0,  //开始角度
//                    60, //扫过的角度
//                    true, //是否使用中心
//                    arcPaint);
//            return;
//        }
//        int total = 0;//总数
//        for (PieMember member : data) {
//            total += member.getNumber();
//        }
//        float angle = 0f;
//        int i = 0;
//        for (int pointer = 0; pointer < data.size(); pointer++) {
//            PieMember member = data.get(pointer);
//            if (member.getColor() == 0) {
//                if (i >= arcColos.length) {
//                    i = 0;
//                }
//                member.setColor(arcColos[i++]);
//            }
//            float d = 360f * ((float) member.getNumber() / (float) total);
//
//            arcPaint.setColor(member.getColor());
//            canvas.drawArc(rect, angle, d, true, arcPaint);  //根据进度画圆弧
//
//            Path path = new Path();
//            path.addArc(rectText, angle, d);
//            Paint citePaint = new Paint();
//            citePaint.setTextSize(fontSize * 0.6F);
//            citePaint.setStrokeWidth(1);
//            citePaint.setColor(getResources().getColor(R.color.black));
//            canvas.drawTextOnPath(member.getText(), path, 50, 0, citePaint);
//
//            //在底部写文字，此处我们不需要
//            float x = getWidth() / data.size() * pointer + margin;
//            float y = getHeight() - margin;
//            float h = getFontHeight(this.textPaint) * 0.6f;
//            canvas.drawText(member.getText() + ":" + member.getNumber(), x + h + (h * 0.3f), y, citePaint);
//            RectF r = new RectF(x, y - h, x + h, y);
//            canvas.drawRect(r, arcPaint)；
//            angle += d;
//        }
//
//    }
//
//    /**
//     * 根据屏幕系数比例获取文字大小
//     *
//     * @return
//     */
//    private static float scalaFonts(int size) {
//        //暂未实现
//        return size;
//    }
//
//    /**
//     * @return 返回指定笔和指定字符串的长度
//     */
//    public static float getFontlength(Paint paint, String str) {
//        return paint.measureText(str);
//    }
//
//    /**
//     * @return 返回指定笔的文字高度
//     */
//    public static float getFontHeight(Paint paint) {
//        Paint.FontMetrics fm = paint.getFontMetrics();
//        return fm.descent - fm.ascent;
//    }
//
//    /**
//     * @return 返回指定笔离文字顶部的基准距离
//     */
//    public static float getFontLeading(Paint paint) {
//        Paint.FontMetrics fm = paint.getFontMetrics();
//        return fm.leading - fm.ascent;
//    }
//
//    public void setData(List<PieMember> data) {
//        this.data = data;
//        postInvalidate();
//    }
//
//
//
//    public static class PieMember {
//        private String text = "";
//        private int number = 0;
//        private int color = 0;
//
//        public void augment(int n) {
//            this.number += n;
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public void setText(String text) {
//            this.text = text;
//        }
//
//        public int getNumber() {
//            return number;
//        }
//
//        public void setNumber(int number) {
//            this.number = number;
//        }
//
//        public int getColor() {
//            return color;
//        }
//
//        public void setColor(int color) {
//            this.color = color;
//        }
//    }

}
