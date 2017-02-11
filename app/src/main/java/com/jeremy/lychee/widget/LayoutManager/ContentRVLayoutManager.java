package com.jeremy.lychee.widget.LayoutManager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.jeremy.lychee.adapter.news.ContentRVAdapter;

public class ContentRVLayoutManager extends GridLayoutManager {

    private ContentRVAdapter adapter;

    public ContentRVLayoutManager(Context context, int spanCount, ContentRVAdapter adapter) {
        super(context, spanCount);
        this.adapter = adapter;
        setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                int itemViewType = adapter.getItemViewType(position);
                if (adapter.isFooterType(itemViewType)) {
                    return getSpanCount();
                } else if (adapter.isContentType(itemViewType)) {
                    int type = adapter.getContentItemViewType(position);
                    switch (type) {
                        case ContentRVAdapter.SEARCH_TYPE:
                        case ContentRVAdapter.FOCUS_NEWS_TYPE:
                        case ContentRVAdapter.IMAGE_NEWS_TYPE1:
                        case ContentRVAdapter.IMAGE_NEWS_TYPE2:
                        case ContentRVAdapter.LAST_REFRESH_TAG:
                        case ContentRVAdapter.EX_FOCUS_NEWS_TAG:
                            return getSpanCount();
                        default:
                            return 1;
                    }
                }
                return 1;
            }
        });
    }

}
