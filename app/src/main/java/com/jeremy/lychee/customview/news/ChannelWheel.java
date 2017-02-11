package com.jeremy.lychee.customview.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.utils.DeviceUtil;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.eventbus.news.Events;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;


public class ChannelWheel extends HorizontalScrollView {

    private int currentPosition = 0;
    private LinearLayout container;
    private ViewPager pager;
    private int count = 0;
    private final PageListener pageListener = new PageListener();
    private int textSize = 16;
    private int selectedTextSize = 20;
    private int textColor = 0xff85888c;
    private int selectedTextColor = 0xff323232;
    private Typeface typeface = null;
    private boolean scrollable = false;

    /**
     * Max allowed duration for a "click", in milliseconds.
     */
    private static final int MAX_CLICK_DURATION = 1000;

    /**
     * Max allowed distance to move during a "click", in DP.
     */
    private static final int MAX_CLICK_DISTANCE = 15;

    private long pressStartTime;
    private float pressedX;
    private float pressedY;
    private Subscription subscribe;
    private double lastShownTime;

    public ChannelWheel(Context context) {
        this(context, null);

    }

    public ChannelWheel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChannelWheel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, HorizontalScrollView.LayoutParams.MATCH_PARENT));
        container.setPadding(DeviceUtil.getScreenWidth(getContext()) / 2 - DensityUtils.dip2px(getContext(), 20), 0,
                DeviceUtil.getScreenWidth(getContext()) / 2 - DensityUtils.dip2px(getContext(), 20), 0);
        addView(container);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (scrollable) {
            resetCollapseTimeout(true);
            return super.onTouchEvent(e);
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                pressStartTime = System.currentTimeMillis();
                pressedX = e.getX();
                pressedY = e.getY();
                return super.onTouchEvent(e);
            }
            case MotionEvent.ACTION_MOVE:
                if (distance(pressedX, pressedY, e.getX(), e.getY()) > 30) {
                    setScrollable(true);
                    return super.onTouchEvent(e);
                }
            case MotionEvent.ACTION_UP: {
                Logger.d("ACTION_UP:" + distance(pressedX, pressedY, e.getX(), e.getY()));
                long pressDuration = System.currentTimeMillis() - pressStartTime;
                if (pressDuration < MAX_CLICK_DURATION && distance(pressedX, pressedY, e.getX(), e.getY()) < MAX_CLICK_DISTANCE) {
                    Logger.d("changing");
                    setScrollable(true);
                    return super.onTouchEvent(e);
                }
            }
        }
        return false;

    }

    private float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return pxToDp(distanceInPx);
    }

    private float pxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!scrollable) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.addOnPageChangeListener(pageListener);
        notifyDataChanged();
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        resetCollapseTimeout(scrollable);
        if (this.scrollable == scrollable) {
            return;
        }
        this.scrollable = scrollable;
        if (!scrollable) {
            smoothScrollToChild(currentPosition);
        }
        QEventBus.getEventBus().post(new Events.ChannelCoverEvent(!scrollable));
    }

    private void resetCollapseTimeout(boolean scrollable) {
        if (!scrollable) {
            return;
        }
        lastShownTime = System.currentTimeMillis();
        subscribe = Observable.timer(3000, TimeUnit.MILLISECONDS)
                .compose(((RxAppCompatActivity) getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                .skipWhile(it -> System.currentTimeMillis() - lastShownTime < 2900)
                .subscribe(timer -> {
                    setScrollable(false);
                }, Throwable::printStackTrace);
    }

    private void notifyDataChanged() {
        container.removeAllViews();
        count = pager.getAdapter().getCount();
        for (int i = 0; i < count; i++) {
            addText(i, pager.getAdapter().getPageTitle(i).toString());
        }
        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });
    }

    private void addText(int i, String channel) {
        TextView textView = new TextView(getContext());
        textView.setText(channel);
        textView.setOnClickListener(v -> {
            QEventBus.getEventBus().post(new Events.HidePopWinEvent());
            if (i == currentPosition) {
                setScrollable(false);
            } else {
                pager.setCurrentItem(i);
            }
        });
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(DensityUtils.dip2px(getContext(), 9), 0, DensityUtils.dip2px(getContext(), 9), 0);
        container.addView(textView, i, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
    }

    public void smoothScrollToChild(int position) {
        if (count == 0) {
            return;
        }
        int newScrollX = (int) (container.getChildAt(position).getX()
                + container.getChildAt(position).getWidth() / 2) - getWidth() / 2;
        smoothScrollTo(newScrollX, 0);
    }

    private void scrollToChild(int position, int offset) {

        if (count == 0) {
            return;
        }

        int newScrollX = (int) (container.getChildAt(position).getX()
                + container.getChildAt(position).getWidth() / 2) - getWidth() / 2 + offset;
        scrollTo(newScrollX, 0);
    }

    private void updateTabStyles() {
        for (int i = 0; i < count; i++) {
            View view = container.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                textView.setTextColor(textColor);
                if (typeface != null) {
                    textView.setTypeface(typeface);
                }
                if (i == currentPosition) {
                    textView.setTextColor(selectedTextColor);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, selectedTextSize);
                }
            }
        }
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        updateTabStyles();
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPosition = position;
//            scrollToChild(position, (int) (positionOffset * container.getChildAt(position).getWidth()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            updateTabStyles();
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @SuppressWarnings("deprecation")
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout() {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    smoothScrollToChild(position);
                }
            });
            setScrollable(true);
        }
    }
}
