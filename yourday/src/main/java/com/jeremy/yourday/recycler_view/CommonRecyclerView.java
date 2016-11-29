package com.jeremy.yourday.recycler_view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.jeremy.yourday.R;
import com.jeremy.yourday.widgit.MySwipeRefreshLayout;

/**
 * Created by benbennest on 16/8/4.
 */
public class CommonRecyclerView extends FrameLayout {
    public RecyclerView mRecyclerView;
    public MySwipeRefreshLayout mSwipeRefreshLayout;

    public CommonRecyclerView(Context context) {
        super(context);
        init();
    }

    public CommonRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_view_wrapper, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        CustomDecoration customDecoration = new CustomDecoration(getContext(), CustomDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(customDecoration);

//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(com.jeremy.lychee.R.layout.recycler_view_wrapper, this);
//        mRecyclerView = (RecyclerView) view.findViewById(com.jeremy.lychee.R.id.list);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(com.jeremy.lychee.R.id.swipe_refresh_layout);
//        errorViewStub = (ViewStub) view.findViewById(com.jeremy.lychee.R.id.error_view_stub);
//        mSwipeRefreshLayout.setEnabled(false);
//        mLoadingLayout = findViewById(com.jeremy.lychee.R.id.loading_layout);
//        if (mRecyclerView != null) {
//
//            mRecyclerView.setClipToPadding(mClipToPadding);
//            if (mPadding != -1.1f) {
//                mRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
//            } else {
//                mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
//            }
//        }
//        mSwipeRefreshLayout.setColorSchemeResources(com.jeremy.lychee.R.color.swipe_refresh_color);
//        setDefaultScrollListener();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }


}
