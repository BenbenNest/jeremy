package com.jeremy.library.recycler_view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.jeremy.library.R;


/**
 * Created by benbennest on 16/8/24.
 */
public class CommonRecyclerView extends FrameLayout {
    private static String TAG = "CommonRecyclerView";
    MySwipeRefreshLayout swipeRefreshLayout;
    LoadMoreRecyclerView mLoadMoreRecyclerView;
    LinearLayoutManager layoutManager;

    private int mActivePointerId;
    private static final int INVALID_POINTER = -1;
    private static final int INVALID_COORDINATE = -1;
    private float mLastY;
    private float mLastX;
    private int mTouchSlop;
    private boolean mRefreshEnabled = true;
    private boolean mLoadMoreEnabled = true;
    private float mRefreshTriggerOffset;
    private float mLoadMoreTriggerOffset;
    private float mRefreshFinalDragOffset;
    private float mLoadMoreFinalDragOffset;
    private View mHeaderView;

    private View mTargetView;
    private View mFooterView;
    private int mHeaderHeight;
    private int mFooterHeight;
    private boolean mHasHeaderView;
    private boolean mHasFooterView;
    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;
    private int mHeaderOffset;
    private int mTargetOffset;
    private int mFooterOffset;
    private float mInitDownY;
    private float mInitDownX;
    private static final float DEFAULT_DRAG_RATIO = 0.5f;
    private float mDragRatio = DEFAULT_DRAG_RATIO;
    OnRefreshLoadMoreListener listener;
    RecyclerOnScrollListener onScrollListener;

    public CommonRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public CommonRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_view_wrapper, this);
        swipeRefreshLayout = (MySwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mLoadMoreRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.load_more_recycler_view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public void setOnRefreshLoadMoreListener(final OnRefreshLoadMoreListener listener) {
        this.listener = listener;
//        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mLoadMoreRecyclerView.setAdapter(adapter);
    }

    public void setLoadingState(boolean loading) {
        onScrollListener.setLoadingState(loading);
    }

    public void disableRefresh() {
//        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    public interface OnRefreshLoadMoreListener {
        void onRefresh();

        void onLoadMore(int currentPage);
    }

}
