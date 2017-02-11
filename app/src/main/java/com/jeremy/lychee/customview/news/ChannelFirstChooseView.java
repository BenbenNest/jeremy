package com.jeremy.lychee.customview.news;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.db.NewsChannel;
import com.jeremy.lychee.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class ChannelFirstChooseView extends LinearLayout {

    static UIItem[] uiItems = {new UIItem(com.jeremy.lychee.R.color.red_txt_color_selector, com.jeremy.lychee.R.drawable.red_bg_selector),
            new UIItem(com.jeremy.lychee.R.color.orange_txt_color_selector, com.jeremy.lychee.R.drawable.orange_bg_selector),
            new UIItem(com.jeremy.lychee.R.color.purple_txt_color_selector, com.jeremy.lychee.R.drawable.purple_bg_selector),
            new UIItem(com.jeremy.lychee.R.color.blue_txt_color_selector, com.jeremy.lychee.R.drawable.blue_bg_selector),
            new UIItem(com.jeremy.lychee.R.color.green_txt_color_selector, com.jeremy.lychee.R.drawable.green_bg_selector)};
    static int[] rowNum = {3, 2, 3, 3, 4, 3, 2, 3, 3, 4};
    private static List<NewsChannel> mData = new ArrayList<>();

    public ChannelFirstChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void setData(List<NewsChannel> data) {
        ChannelFirstChooseView.mData = data;
        int i = 0;
        int rowIndex = 0;
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DensityUtils.dip2px(getContext(), 15), 0, 0);
        while (i < mData.size()) {
            Row row = new Row(getContext());
            List<NewsChannel> list = new ArrayList<>();
            for (int j = 0; j < rowNum[rowIndex]; j++) {
                if (i < mData.size()) {
                    list.add(mData.get(i++));
                }
            }
            row.setRowData(list);
            if (rowIndex == 0) {
                addView(row);
            } else {
                addView(row, params);
            }
            rowIndex++;
        }
    }

    public static class Row extends LinearLayout {
        private List<NewsChannel> rowData = new ArrayList<>();

        public Row(Context context) {
            super(context);
            setOrientation(HORIZONTAL);
            setGravity(Gravity.CENTER);
        }

        public void setRowData(List<NewsChannel> rowData) {
            this.rowData = rowData;
            initItem();
        }

        private void initItem() {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(DensityUtils.dip2px(getContext(), 10), 0, 0, 0);
            int i = 0;
            for (NewsChannel channel : rowData) {
                Item item = new Item(getContext());
                item.init(getUIItem(channel), channel);
                if (i == 0) {
                    addView(item);
                } else {
                    addView(item, params);
                }

                i++;
            }
        }
    }


    public static class Item extends TextView {
        private NewsChannel newsChannel;

        public Item(Context context) {
            super(context);
            setGravity(Gravity.CENTER);
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            setClickable(true);
            setPadding(DensityUtils.dip2px(context, 10), DensityUtils.dip2px(context, 5),
                    DensityUtils.dip2px(context, 10), DensityUtils.dip2px(context, 5));
            setOnClickListener(view -> {
                if (newsChannel == null) {
                    return;
                }
                newsChannel.setIsShow(!newsChannel.getIsShow());
                setSelected(newsChannel.getIsShow());
            });
        }

        public void init(UIItem uiItem, NewsChannel newsChannel) {
            this.newsChannel = newsChannel;
            if (uiItem != null) {
                setTextColor(ContextCompat.getColorStateList(getContext(), uiItem.txtColorResId));
                setBackgroundResource(uiItem.bgResId);
            }
            setText(newsChannel.getTagname());
        }

    }

    public static class UIItem {
        int txtColorResId;
        int bgResId;

        public UIItem(int txtColorResId, int bgResId) {
            this.txtColorResId = txtColorResId;
            this.bgResId = bgResId;
        }
    }

    public static UIItem getUIItem(NewsChannel channel) {
        int indexOf = mData.indexOf(channel);
        if (indexOf == -1) {
            return uiItems[0];
        }
        indexOf++;
        int mo = mData.size() / uiItems.length;
        int yu = mData.size() % uiItems.length;
        int i = (indexOf / mo) + (((indexOf % mo - yu) > 0) ? 1 : 0);
        i--;
        if (i >= uiItems.length || i < 0) {
            return uiItems[0];
        }
        return uiItems[i];
    }

}
