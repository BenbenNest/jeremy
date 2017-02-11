package com.jeremy.lychee.widget.BookReader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jeremy.lychee.bean.DeviceUtils;
import com.jeremy.lychee.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by benbennest on 16/6/7.
 */
public class TextReader extends View {
    /**
     * 要显示的文字
     */
    public String text = "Google在2010年展示了第一个Chromebook原型，第二年则将第一批商用笔记本电脑上市。五年后，Chromebook设法占领了一部分在美国的市场份额，其中学校现在“比起所有其他的设备加起来，购买了更多的Chromebook”，并且在2016年Q1中“Chromebook超越了Mac”，这都是Google引用了IDC的话。其中一个解释Chromebook被大众接受很慢的原因是Chromebook曾经不能运行原生的Windows应用并且缺少离线运行特定应用的支持。我们值得提到的是，企业们曾经有机会通过Receiver for Chrome运行遗留的Windows应用，这是Citrix公司的一个桌面虚拟化解决方案。\n" +
            "\n" +
            "为了弥补缺失应用的缺陷，Google决定将整个Play商店和它超过1.5兆的应用都带到Chrome OS中。在最近的I/O 2016中，他们发布了这些Chromebook。在九月份M53可以使用后，Chromebox和Chromebase都能够访问Play商店。用户将能够使用微软为安卓开发的应用，包括Word、Excel、PowerPoint、Outlook和Skype。并且，他们将能够选择离线运行它们，就像Google Play Music一样。";

    private Context mContext;
    private TextPaint mTextPaint;
    private String mText;
    public static int PADDING_TOP = 10;// 页面顶部间距
    public static int PADDING_LEFT = 20;// 页面左侧间距
    public static int PADDING_RIGHT = 10;// 页面右侧间距
    public static int PADDING_BOTTOM = 10;// 页面底部间距

    private int firstLineIndex;
    private int firstWordIndex;
    private int lastLineIndex;
    private int lastWordIndex; // last paragraph's last word's index

    public TextReader(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public TextReader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mText = "Google在2010年展示了第一个Chromebook原型Google在2010年展示了第一个Chromebook原型";
        mText = text;
        // Default font size and color.
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DeviceUtils.dp2px(mContext, 15));
        mTextPaint.setColor(0xFF000000);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setAntiAlias(true);
//        StaticLayout
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mTextPaint.setLetterSpacing(0.5f);
//        }

        mTextPaint.density = mContext.getResources().getDisplayMetrics().density;
//        mTextPaint.set
    }

    /**
     * Sets the text to display in this widget.
     *
     * @param text The text to display.
     */
    public void setText(String text) {
        mText = text;
        requestLayout();
        invalidate();
    }

    /**
     * Sets the text size for this widget.
     *
     * @param size Font size.
     */
    public void setTextSize(int size) {
        mTextPaint.setTextSize(size);
        requestLayout();
        invalidate();
    }

    /**
     * Sets the text color for this widget.
     *
     * @param color ARGB value for the text.
     */
    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    /**
     * 一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。
     * 一个MeasureSpec由大小和模式组成
     * 它有三种模式：UNSPECIFIED(未指定),父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小;
     * EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；
     * AT_MOST(至多)，子元素至多达到指定大小的值。
     * <p>
     * 　　它常用的三个函数：
     * 1.static int getMode(int measureSpec):根据提供的测量值(格式)提取模式(上述三个模式之一)
     * 2.static int getSize(int measureSpec):根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小)
     * 3.static int makeMeasureSpec(int size,int mode):根据提供的大小值和模式创建一个测量值(格式)
     * 如果你的自定义View的尺寸是根据父控件行为一致，就不需要重写onMeasure()方法
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
        }
        return result;
    }

    /**
     * Render the text
     *
     * @see View#onDraw(Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        oldDrawMethod(canvas);
    }

    private void oldDrawMethod(Canvas canvas) {
        List<int[]> lines = new ArrayList<int[]>();
        int pos = 0;
        int maxWidth = getWidth() - PADDING_LEFT - PADDING_RIGHT;
        int width = 0;
        int start = 0;
        int w = 0, w2 = 0;
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top;
        int lineSpaceing = 10;
//        canvas.clipRect(PADDING_LEFT, 0, PADDING_LEFT + maxWidth, getHeight());
//        mTextPaint.breakText()
        while (pos < mText.length()) {
            String ch = String.valueOf(mText.charAt(pos));
            if (ch.equals("\r") && String.valueOf(mText.charAt(pos + 1)).equals("\n")) {
                lines.add(new int[]{start, pos});
                start = pos + 2;
                pos = start + 1;
                w = 0;
                w2 = 0;
                width = 0;
            } else {
                float charWidth = mTextPaint.measureText(String.valueOf(mText.charAt(pos)));
                Rect rect = new Rect();
                mTextPaint.getTextBounds(String.valueOf(mText.charAt(pos)), 0, 1, rect);
                float cWidth = rect.width();
                w += cWidth;
                w2 += charWidth;
                String str = mText.substring(start, pos + 1);
                float temp = mTextPaint.measureText(str);
                mTextPaint.getTextBounds(str, 0, str.length(), rect);
                width = rect.width();
                float[] arr = new float[1];
                mTextPaint.getTextWidths(ch, arr);
                int widthresult = (width + w2 + w) / 3;
                float result = mTextPaint.measureText(str);
                if (result * 95 / 100 > maxWidth) {
                    lines.add(new int[]{start, pos - 1});
                    start = pos - 1;
                    Log.v("width", "charRect=" + w + "---" + "measureText=" + w2 + "----" + "textRect=" + width);
                    width = 0;
                    w = 0;
                    w2 = 0;
                }
                if (pos == mText.length() - 1) {
                    lines.add(new int[]{start, pos});
                }
                pos++;
            }
        }
        int startY = PADDING_TOP + ScreenUtil.getStatusBarHeight(mContext);
        for (int i = 0; i < lines.size(); i++) {
            int[] arr = lines.get(i);
            if (i > 180) {
                arr = lines.get(i);
            }
            String line = mText.substring(arr[0], arr[1]);
            Log.v("test", line + " 序号:" + arr[0] + "--" + arr[1]);
//            canvas.clipRect(getLeft() + getPaddingLeft(), startY, getRight(), startY + 200);
            Rect rect = new Rect();
            mTextPaint.getTextBounds(line, 0, line.length(), rect);
            float scale = (float) maxWidth / rect.width();
//            mTextPaint.setTextScaleX(scale);
            canvas.drawText(line, PADDING_LEFT, startY, mTextPaint);
            startY += height + lineSpaceing;
        }
    }

}
