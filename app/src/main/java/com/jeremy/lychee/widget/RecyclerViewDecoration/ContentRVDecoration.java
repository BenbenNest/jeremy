package com.jeremy.lychee.widget.RecyclerViewDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeremy.lychee.adapter.news.ContentRVAdapter;
import com.jeremy.lychee.db.NewsListData;

import java.util.List;

public class ContentRVDecoration extends RecyclerView.ItemDecoration {

    private int offset;
    private ContentRVAdapter adapter;
    private int spanCount;

    public ContentRVDecoration(int spanCount, int offset, ContentRVAdapter adapter) {
        this.spanCount = spanCount;
        this.offset = offset / 2;
        this.adapter = adapter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        DecorationType decorationType = getDecorationType(position);
        switch (decorationType) {
            case left_edge:
                outRect.set(0, 2 * offset, offset, 0);
                break;
            case middle:
                outRect.set(0, 2 * offset, offset, 0);
                break;
            case right_edge:
                outRect.set(offset, 2 * offset, 0, 0);
                break;
            case one_line:
                outRect.set(0, 2 * offset, 0, 0);
                break;
            case one_line_no_space:
                outRect.set(0, 0, 0, 0);
                break;
            case one_line_margin_top:
                outRect.set(0, 2 * offset, 0, 0);
                break;
        }
    }


    public enum DecorationType {
        left_edge,
        middle,
        right_edge,
        one_line,
        one_line_no_space,
        one_line_margin_top
    }

    public DecorationType getDecorationType(int position) {
        int type = adapter.getContentItemViewType(position);
        if (spanCount == 1) {
            switch (type) {
                case ContentRVAdapter.FOCUS_NEWS_TYPE:
                case ContentRVAdapter.SEARCH_TYPE:
                    return DecorationType.one_line_no_space;
                default:
                    return DecorationType.one_line_margin_top;
            }
        }
        switch (type) {
            case ContentRVAdapter.FOCUS_NEWS_TYPE:
            case ContentRVAdapter.SEARCH_TYPE:
                return DecorationType.one_line_no_space;
            case ContentRVAdapter.LAST_REFRESH_TAG:
            case ContentRVAdapter.IMAGE_NEWS_TYPE1:
            case ContentRVAdapter.IMAGE_NEWS_TYPE2:
            case ContentRVAdapter.EX_FOCUS_NEWS_TAG:
                return DecorationType.one_line;
        }

        int listPosition = adapter.getListPosition(position);
        List<NewsListData> list = adapter.getList();
        if (list == null || listPosition >= list.size()) {
            return DecorationType.left_edge;
        }
        if (listPosition == 0) {
            return DecorationType.left_edge;
        }
        NewsListData data;
        int i = 1;
        while ((listPosition - i) >= 0) {
            data = list.get(listPosition - i);
            if ("4".equals(data.getModule()) || "5".equals(data.getModule()) || "3".equals(data.getModule())) {
                break;
            }
            i++;
        }
        i--;
        if (i % spanCount == 0) {
            return DecorationType.left_edge;
        } else if ((i + 1) % spanCount == 0) {
            return DecorationType.right_edge;
        } else {
            return DecorationType.middle;
        }

    }
}
