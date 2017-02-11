/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeremy.lychee.widget.slidingtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * To be used with ViewPager to provide a tab indicator component which give constant feedback as to
 * the user's scroll progress.
 * <p>
 * To use the component, simply add it to your view hierarchy. Then in your
 * {@link android.app.Activity} or {@link android.support.v4.app.Fragment} call
 * {@link #setViewPager(ViewPager)} providing it the ViewPager this layout is being used for.
 * <p>
 * The colors can be customized in two ways. The first and simplest is to provide an array of colors
 * via {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)}. The
 * alternative is via the {@link TabColorizer} interface which provides you complete control over
 * which color is used for any individual position.
 * <p>
 * The views used as tabs can be customized by calling {@link #setCustomTabView(int, int)},
 * providing the layout ID of your custom layout.
 */
public class CustomSlidingTabLayout extends HorizontalScrollView {


    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 15;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 16;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final CustomSlidingTabStrip mTabStrip;

    private int mTabTextColor;
    private float mTabTextSize;
    private int mTabTextColorSelected;
    private float mTabTextSizeSelected;
    private float mTabTextPadding;
    private int mTabIndicator;

    public CustomSlidingTabLayout(Context context) {
        this(context, null);
    }

    public CustomSlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new CustomSlidingTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        TypedArray a = context.obtainStyledAttributes(attrs, com.jeremy.lychee.R.styleable.SlidingTabLayout);
        mTabTextColor = a.getColor(com.jeremy.lychee.R.styleable.SlidingTabLayout_tabTextDefaultColor,
                getResources().getColor(com.jeremy.lychee.R.color.user_tab_title_text));
        mTabTextSize = a.getDimension(com.jeremy.lychee.R.styleable.SlidingTabLayout_tabTextSize,
                TAB_VIEW_TEXT_SIZE_SP);
        mTabTextColorSelected = a.getColor(com.jeremy.lychee.R.styleable.SlidingTabLayout_tabTextSelectedColor,
                getResources().getColor(com.jeremy.lychee.R.color.user_tab_title_text_selected));
        mTabTextSizeSelected = a.getDimension(com.jeremy.lychee.R.styleable.SlidingTabLayout_tabTextSelectedSize,
                TAB_VIEW_TEXT_SIZE_SP);
        mTabTextPadding = a.getDimension(com.jeremy.lychee.R.styleable.SlidingTabLayout_tabTextSelectedPadding,
                TAB_VIEW_PADDING_DIPS);
        mTabIndicator = a.getColor(com.jeremy.lychee.R.styleable.SlidingTabLayout_tabLayoutIndicatorColor,
                getResources().getColor(com.jeremy.lychee.R.color.user_tab_title_text_selected));

        mTabStrip.setSelectedIndicatorColors(mTabIndicator);
        a.recycle();

//        setBackgroundResource(R.drawable.user_tab_layout_bg);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link CustomSlidingTabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTabTextSize);
        textView.setTypeface(Typeface.DEFAULT);
        textView.setTextColor(mTabTextColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                                                            outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }

        int padding = (int) (mTabTextPadding * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

//    private void populateTabStrip() {
//        final PagerAdapter adapter = mViewPager.getAdapter();
//        final View.OnClickListener tabClickListener = new TabClickListener();
//
//        for (int i = 0; i < adapter.getCount(); i++) {
//            View tabView = null;
//            TextView tabTitleView = null;
//
//            if (mTabViewLayoutId != 0) {
//                // If there is a custom tab view layout id set, try and inflate it
//                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
//                                                                           false);
//                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
//            }
//
//            if (tabView == null) {
//                tabView = createDefaultTabView(getContext());
//            }
//
//            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
//                tabTitleView = (TextView) tabView;
//            }
//
//            tabTitleView.setText(adapter.getPageTitle(i));
//            tabView.setOnClickListener(tabClickListener);
//
//            mTabStrip.addView(tabView);
//        }
//    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate
                // it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip, false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);

            //添加by Zhang Phil
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if(adapter.getCount() == 2){
                int margin = (int) (16 * getResources().getDisplayMetrics().density);
                if (i == 0) {
                    layoutParams.rightMargin = margin;
                } else if (i == 1) {
                    layoutParams.leftMargin = margin;
                }
            }
            tabView.setLayoutParams(layoutParams);

            tabView.setPadding(10,10,10,10);

            //添加by Zhang Phil

            mTabStrip.setGravity(Gravity.CENTER);
            mTabStrip.addView(tabView);
        }

        setTabSelected(0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                                      ? (int) (positionOffset * selectedTitle.getWidth())
                                      : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }

            setTabSelected(position);
        }


    }
    private void setTabSelected(int position){
        for(int i = 0 ;i< mTabStrip.getChildCount(); i++){
            TextView tv = (TextView) mTabStrip.getChildAt(i);
            tv.setTextColor(mTabTextColor);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTabTextSize);
        }
        TextView tv = (TextView) mTabStrip.getChildAt(position);
        tv.setTextColor(mTabTextColorSelected);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTabTextSizeSelected);
    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

}