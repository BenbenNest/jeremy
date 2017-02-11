package com.jeremy.lychee.adapter.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchVHTemplate extends ContentVHTemplate<Void> {

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.rv_content_search_view, parent, false);
        return new SearchViewVH(view);
    }

    @Override
    public void onBindContentItemViewHolder(RecyclerView.ViewHolder viewHolder, Void data) {
        Calendar calendar = Calendar.getInstance();
        String date = String.format(Locale.CHINA, "%02d月%02d日 %s", calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH), getWeekDay(calendar));
        SearchViewVH searchViewVH = (SearchViewVH) viewHolder;
        searchViewVH.dateTxt.setText(date);
    }


    public static class SearchViewVH extends RecyclerView.ViewHolder {
        @Bind(com.jeremy.lychee.R.id.date_txt)
        TextView dateTxt;

        public SearchViewVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static String getWeekDay(Calendar c) {
        String week = "星期";
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            week += "六";
        }
        return week;
    }

}
