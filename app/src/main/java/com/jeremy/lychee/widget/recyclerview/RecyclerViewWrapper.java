package com.jeremy.lychee.widget.recyclerview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.jeremy.lychee.adapter.news.ContentRVAdapter;
import com.jeremy.lychee.utils.SavedStateScrolling;
import com.jeremy.lychee.widget.LoadingRecyclerViewFooter;
import com.jeremy.lychee.widget.RecyclerViewDecoration.DividerItemDecoration;

/**
 * UltimateRecyclerView is a recyclerview which contains many features like  swipe to dismiss,animations,drag drop etc.
 */
public class RecyclerViewWrapper extends FrameLayout {
    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private OnLoadMoreListener onLoadMoreListener;
    private int lastVisibleItemPosition;
    protected RecyclerView.OnScrollListener mOnScrollListener;
    protected LAYOUT_MANAGER_TYPE layoutManagerType;
    private boolean isLoadingMore = false;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected boolean mClipToPadding;
    private BaseRVAdapter mAdapter;


    // Fields that should be saved onSaveInstanceState
    private int mPrevFirstVisiblePosition;
    private int mPrevFirstVisibleChildHeight = -1;
    private int mPrevScrolledChildrenHeight;
    private int mPrevScrollY;
    private int mScrollY;
    private SparseIntArray mChildrenHeights = new SparseIntArray();

    // Fields that don't need to be saved onSaveInstanceState
    private boolean mIsLoadMoreWidgetEnabled;
    private ViewStub errorViewStub;
    private View mErrorLayout;
    private View mLoadingLayout;
    private OnClickListener errorLayoutClickListener;


    protected int mEmptyId;
    protected int[] defaultSwipeToDismissColors = null;

    // added by Sevan Joe to support scrollbars
    private static final int SCROLLBARS_NONE = 0;
    private static final int SCROLLBARS_VERTICAL = 1;
    private static final int SCROLLBARS_HORIZONTAL = 2;
    private int mScrollbarsStyle;


    private int mVisibleItemCount = 0;
    private int mTotalItemCount = 0;
    private int previousTotal = 0;
    private int mFirstVisibleItem;
    private boolean isFullData = false;

    public RecyclerViewWrapper(Context context) {
        super(context);
        initViews();
    }

    public RecyclerViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initViews();
    }

    public RecyclerViewWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initViews();
    }

    public void setRecylerViewBackgroundColor(@ColorInt int color) {
        mRecyclerView.setBackgroundColor(color);
    }


    private int mBounceRange;
    private RecyclerView.OnScrollListener mBounceListener = new RecyclerView.OnScrollListener() {
        float dy_;
        boolean flg_ = false;
        ContentRVAdapter adapter_;
        volatile int start_ = -1;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                //searchbar不停留中间位置
                float y = mRecyclerView.computeVerticalScrollOffset();
                if (y < mBounceRange / 2) {
                    mRecyclerView.smoothScrollBy(0, (int) -y);
                } else if (y > (mBounceRange / 2) && y < mBounceRange) {
                    mRecyclerView.smoothScrollBy(0, (int) (mBounceRange - y));
                }

                //searchbar 二段下拉
                if (y == start_ && flg_) {
                    if (adapter_.getHeaderView() != null) {
                        adapter_.getHeaderView().getLayoutParams().height = mBounceRange;
                        ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(1, 0);
                        flg_ = false;
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (adapter_ == null) {//初始化
                try {
                    adapter_ = (ContentRVAdapter) mAdapter;
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
            if (start_ == -1 || start_ == 0) {//初始化
                start_ = mRecyclerView.computeVerticalScrollOffset();
            }
            dy_ = dy;
            if (mRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                float y = mRecyclerView.computeVerticalScrollOffset();
                if (dy_ > 0 && y >= mBounceRange && !flg_) {
                    if (adapter_.getHeaderView() != null) {
                        adapter_.getHeaderView().getLayoutParams().height = 0;
                        ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(1, 0);
                        flg_ = true;
                    }
                }
            }
        }
    };

    public void setTopItemBounceable(boolean val) {
        if (mBounceListener == null) return;
        if (val) {
            mRecyclerView.addOnScrollListener(mBounceListener);
        } else {
            mRecyclerView.removeOnScrollListener(mBounceListener);
        }
    }

    protected void initViews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(com.jeremy.lychee.R.layout.recycler_view_wrapper, this);
        mRecyclerView = (RecyclerView) view.findViewById(com.jeremy.lychee.R.id.list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(com.jeremy.lychee.R.id.swipe_refresh_layout);
        errorViewStub = (ViewStub) view.findViewById(com.jeremy.lychee.R.id.error_view_stub);
        mSwipeRefreshLayout.setEnabled(false);
        mLoadingLayout = findViewById(com.jeremy.lychee.R.id.loading_layout);
        if (mRecyclerView != null) {

            mRecyclerView.setClipToPadding(mClipToPadding);
            if (mPadding != -1.1f) {
                mRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
        }
        mSwipeRefreshLayout.setColorSchemeResources(com.jeremy.lychee.R.color.swipe_refresh_color);
        setDefaultScrollListener();

    }


    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, com.jeremy.lychee.R.styleable.UltimateRecyclerview);

        try {
            mPadding = (int) typedArray.getDimension(com.jeremy.lychee.R.styleable.UltimateRecyclerview_recyclerviewPadding, -1.1f);
            mPaddingTop = (int) typedArray.getDimension(com.jeremy.lychee.R.styleable.UltimateRecyclerview_recyclerviewPaddingTop, 0.0f);
            mPaddingBottom = (int) typedArray.getDimension(com.jeremy.lychee.R.styleable.UltimateRecyclerview_recyclerviewPaddingBottom, 0.0f);
            mPaddingLeft = (int) typedArray.getDimension(com.jeremy.lychee.R.styleable.UltimateRecyclerview_recyclerviewPaddingLeft, 0.0f);
            mPaddingRight = (int) typedArray.getDimension(com.jeremy.lychee.R.styleable.UltimateRecyclerview_recyclerviewPaddingRight, 0.0f);
            mClipToPadding = typedArray.getBoolean(com.jeremy.lychee.R.styleable.UltimateRecyclerview_recyclerviewClipToPadding, false);
            mEmptyId = typedArray.getResourceId(com.jeremy.lychee.R.styleable.UltimateRecyclerview_recyclerviewEmptyView, 0);
            mScrollbarsStyle = typedArray.getInt(com.jeremy.lychee.R.styleable.UltimateRecyclerview_recyclerviewScrollbars, SCROLLBARS_NONE);
            int colorList = typedArray.getResourceId(com.jeremy.lychee.R.styleable.UltimateRecyclerview_recyclerviewDefaultSwipeColor, 0);
            if (colorList != 0) {
                defaultSwipeToDismissColors = getResources().getIntArray(colorList);
            }
        } finally {
            typedArray.recycle();
        }
    }


    protected void setDefaultScrollListener() {
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHeader != null) {
                    mTotalYScrolled += dy;
                    if (isParallaxHeader)
                        translateHeader(mRecyclerView.computeVerticalScrollOffset());
                }
            }
        };

        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void setObserableScrollListener() {
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    /**
     * Enable loading more of the recyclerview
     */
    public void enableLoadMore() {
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            private int[] lastPositions;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHeader != null) {
                    mTotalYScrolled += dy;
                    if (isParallaxHeader)
                        translateHeader(mRecyclerView.computeVerticalScrollOffset());
                }
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                if (layoutManagerType == null) {
                    if (layoutManager instanceof GridLayoutManager) {
                        layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
                    } else if (layoutManager instanceof LinearLayoutManager) {
                        layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                    } else {
                        throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                    }
                }

                switch (layoutManagerType) {
                    case LINEAR:
                        mVisibleItemCount = layoutManager.getChildCount();
                        mTotalItemCount = layoutManager.getItemCount();
                    case GRID:
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        mFirstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        break;
                    case STAGGERED_GRID:
                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                        if (lastPositions == null)
                            lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                        lastVisibleItemPosition = findMax(lastPositions);

                        staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions);
                        mFirstVisibleItem = findMin(lastPositions);
                        break;
                }

                if (isLoadingMore) {
                    //todo: there are some bugs needs to be adjusted for admob adapter
                    if (mTotalItemCount > previousTotal) {
                        isLoadingMore = false;
                        previousTotal = mTotalItemCount;
                    }
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManagerType == null) {
                        if (layoutManager instanceof GridLayoutManager) {
                            layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
                        } else if (layoutManager instanceof LinearLayoutManager) {
                            layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                            layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                        } else {
                            throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                        }
                    }
                    int lastPosition = 0;
                    switch (layoutManagerType) {
                        case LINEAR:
                            lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                            break;
                        case GRID:
                            lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                            break;
                        case STAGGERED_GRID:
                            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                            if (lastPositions == null)
                                lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                            staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                            lastPosition = findMax(lastPositions);
                            break;
                    }
                    boolean mLastItemVisible = (mAdapter.getItemCount() > 0) && lastPosition >= mAdapter.getItemCount() - 1;
                    if (mLastItemVisible && !isFullData && !isLoadingMore && (mTotalItemCount - mVisibleItemCount) <= mFirstVisibleItem) {
                        //todo: there are some bugs needs to be adjusted for admob adapter
                        onLoadMoreListener.loadMore(mRecyclerView.getAdapter().getItemCount(), lastVisibleItemPosition);
                        isLoadingMore = true;
                        previousTotal = mTotalItemCount;
                    }

                }
            }
        };
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        if (mAdapter != null && mAdapter.getFooterView() == null) {
            LoadingRecyclerViewFooter footer = new LoadingRecyclerViewFooter(getContext());
            footer.setStatus(LoadingRecyclerViewFooter.FooterStatus.idle);
            mAdapter.setFooterView(footer);
        }


        mIsLoadMoreWidgetEnabled = true;

    }


    public void addOnScrollListener(RecyclerView.OnScrollListener customOnScrollListener) {
        mRecyclerView.addOnScrollListener(customOnScrollListener);
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener customOnScrollListener) {
        mRecyclerView.removeOnScrollListener(customOnScrollListener);
    }

    public void addItemDividerDecoration(Context context) {
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
    }


    /**
     * Swaps the current adapter with the provided one. It is similar to
     * ViewHolder and does not clear the RecycledViewPool.
     * <p>
     * Note that it still calls onAdapterChanged callbacks.
     *
     * @param adapter                       The new adapter to set, or null to set no adapter.
     * @param removeAndRecycleExistingViews If set to true, RecyclerView will recycle all existing Views.
     *                                      If adapters have stable ids and/or you want to animate the disappearing views,
     *                                      you may prefer to set this to false.
     */
    public void swapAdapter(BaseRVAdapter adapter, boolean removeAndRecycleExistingViews) {
        mRecyclerView.swapAdapter(adapter, removeAndRecycleExistingViews);
    }

    /**
     * Add an {@link RecyclerView.ItemDecoration} to this RecyclerView.
     * Item decorations can affect both measurement and drawing of individual item views.
     * Item decorations are ordered. Decorations placed earlier in the list will be
     * run/queried/drawn first for their effects on item views. Padding added to views will be nested;
     * a padding added by an earlier decoration will mean further item decorations in the list will be asked to draw/pad within the previous decoration's given area.
     *
     * @param itemDecoration Decoration to add
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void setDefaultOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            isFullData = false;
            listener.onRefresh();
        });
    }

    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setIsFullData(boolean isFullData) {
        this.isFullData = isFullData;
        mAdapter.setFooterStatus(LoadingRecyclerViewFooter.FooterStatus.full);
    }


    /**
     * Add an {@link RecyclerView.ItemDecoration} to this RecyclerView. Item decorations can affect both measurement and drawing of individual item views.
     * <p>Item decorations are ordered. Decorations placed earlier in the list will be run/queried/drawn first for their effects on item views. Padding added to views will be nested; a padding added by an earlier decoration will mean further item decorations in the list will be asked to draw/pad within the previous decoration's given area.</p>
     *
     * @param itemDecoration Decoration to add
     * @param index          Position in the decoration chain to insert this decoration at.
     *                       If this value is negative the decoration will be added at the end.
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecyclerView.addItemDecoration(itemDecoration, index);
    }

    public void setErrorViewClickListener(OnClickListener errorLayoutClickListener) {
        this.errorLayoutClickListener = errorLayoutClickListener;
        if (mErrorLayout != null) {
            mErrorLayout.setOnClickListener(errorLayoutClickListener);
        }
    }

    public void showLoadingView() {
        mLoadingLayout.setVisibility(VISIBLE);
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(GONE);
        }
    }

    public void hideLoadingView() {
        if (mLoadingLayout.getVisibility() != GONE) {
            mLoadingLayout.setVisibility(GONE);
        }
    }

    public void showErrorView() {
        if (mErrorLayout == null) {
            mErrorLayout = errorViewStub.inflate();
            if (errorLayoutClickListener != null) {
                mErrorLayout.setOnClickListener(errorLayoutClickListener);
            }
        }
        mErrorLayout.setVisibility(VISIBLE);
        mLoadingLayout.setVisibility(GONE);
    }

    public void hideErrorView() {
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(GONE);
        }
    }

    public void showLoadMoreError() {
        mAdapter.setFooterStatus(LoadingRecyclerViewFooter.FooterStatus.error);
    }

    /**
     * Set the load more listener of recyclerview
     *
     * @param onLoadMoreListener load listen
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * Set the layout manager to the recycler
     *
     * @param manager lm
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecyclerView.setLayoutManager(manager);
    }

    /**
     * Get the adapter of UltimateRecyclerview
     *
     * @return ad
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    /**
     * Set a UltimateViewAdapter or the subclass of UltimateViewAdapter to the recyclerview
     *
     * @param adapter the adapter in normal
     */
    public void setAdapter(BaseRVAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter != null) {
            mBounceRange = mAdapter.getTopBounceRange();
            setTopItemBounceable(mAdapter.canTopBound());
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    super.onItemRangeChanged(positionStart, itemCount);
                    updateHelperDisplays();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    updateHelperDisplays();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    updateHelperDisplays();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                    updateHelperDisplays();
                }

                @Override
                public void onChanged() {
                    super.onChanged();
                    updateHelperDisplays();
                }
            });
        }
    }

    private void updateHelperDisplays() {
        isLoadingMore = false;
        if (mAdapter == null || mAdapter.getFooterView() == null)
            return;
        if (isFullData) {
            mAdapter.setFooterStatus(LoadingRecyclerViewFooter.FooterStatus.full);
        } else if (mAdapter.getContentItemCount() > 0) {
            mAdapter.setFooterStatus(LoadingRecyclerViewFooter.FooterStatus.loading);
        } else {
            mAdapter.setFooterStatus(LoadingRecyclerViewFooter.FooterStatus.idle);
        }
    }

//    /**
//     * @param adapter input param
//     * @deprecated Short for some ui effects
//     */
//    @Deprecated
//    public void setAdapter(RecyclerView.Adapter adapter) {
//        mRecyclerView.setAdapter(adapter);
//        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeChanged(int positionStart, int itemCount) {
//                super.onItemRangeChanged(positionStart, itemCount);
//                update();
//            }
//
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                update();
//            }
//
//            @Override
//            public void onItemRangeRemoved(int positionStart, int itemCount) {
//                super.onItemRangeRemoved(positionStart, itemCount);
//                update();
//            }
//
//            @Override
//            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
//                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
//                update();
//            }
//
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                update();
//            }
//
//            private void update() {
//                isLoadingMore = false;
//            }
//
//        });
//    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }


    public interface OnLoadMoreListener {
        void loadMore(int itemsCount, final int maxLastVisiblePosition);
    }

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    private int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max)
                max = value;
        }
        return max;
    }

    private int findMin(int[] lastPositions) {
        int min = Integer.MAX_VALUE;
        for (int value : lastPositions) {
            if (value != RecyclerView.NO_POSITION && value < min)
                min = value;
        }
        return min;
    }


    private CustomRelativeWrapper mHeader;
    private int mTotalYScrolled;
    private final float SCROLL_MULTIPLIER = 0.5f;
    private OnParallaxScroll mParallaxScroll;
    private static boolean isParallaxHeader = false;

    /**
     * allow resource layout id to be introduced
     *
     * @param mLayout res id
     */
    public void setParallaxHeader(@LayoutRes int mLayout, int resId) {
        View h_layout = LayoutInflater.from(getContext()).inflate(mLayout, null);
        setParallaxHeader(h_layout, resId);
    }

    /**
     * Set the parallax header of the recyclerview
     *
     * @param header the view
     */
    public void setParallaxHeader(View header, int resId) {
        mHeader = new CustomRelativeWrapper(header.getContext());
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        mHeader.addView(header, header.getLayoutParams());
        mHeader.addView(header);
        mHeader.initStaticView(resId);
        if (mAdapter != null)
            mAdapter.setCustomHeaderView(mHeader);
        isParallaxHeader = true;
    }


    /**
     * Set the normal header of the recyclerview
     *
     * @param header
     */
    public void setNormalHeader(View header) {
        setParallaxHeader(header, 0);
        isParallaxHeader = false;
    }

    /**
     * Set the on scroll method of parallax header
     *
     * @param parallaxScroll
     */
    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        mParallaxScroll = parallaxScroll;
        mParallaxScroll.onParallaxScroll(0, 0, mHeader);
    }

    private void translateHeader(float of) {
        float ofCalculated = of * SCROLL_MULTIPLIER;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //Logs.d("ofCalculated    " + ofCalculated+"   "+mHeader.getHeight());
            mHeader.setTranslationY(ofCalculated);
            if (mHeader.getmStaticView() != null) {
                mHeader.getmStaticView().setTranslationY(-ofCalculated);
            }
        } else {
            TranslateAnimation anim = new TranslateAnimation(0, 0, ofCalculated, ofCalculated);
            anim.setFillAfter(true);
            anim.setDuration(0);
            mHeader.startAnimation(anim);
            if (mHeader.getmStaticView() != null) {
                TranslateAnimation anim1 = new TranslateAnimation(0, 0, -ofCalculated, -ofCalculated);
                anim1.setFillAfter(true);
                anim1.setDuration(0);
                mHeader.getmStaticView().startAnimation(anim1);
            }
        }
        mHeader.setClipY(Math.round(ofCalculated));
        if (mParallaxScroll != null) {
            float left = Math.min(1, ((ofCalculated) / (mHeader.getHeight() * SCROLL_MULTIPLIER)));
            mParallaxScroll.onParallaxScroll(left, of, mHeader);
        }
    }

    public interface OnParallaxScroll {
        void onParallaxScroll(float percentage, float offset, View parallax);
    }

    /**
     * Custom layout for the Parallax Header.
     */
    public static class CustomRelativeWrapper extends RelativeLayout {

        private int mOffset;
        private View mStaticView;

        public void initStaticView(int resId) {
            if (resId != 0) {
                mStaticView = findViewById(resId);
            }
        }

        public View getmStaticView() {
            return mStaticView;
        }

        public CustomRelativeWrapper(Context context) {
            super(context);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            if (isParallaxHeader) {
                canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            }
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }


    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedStateScrolling ss = (SavedStateScrolling) state;
        mPrevFirstVisiblePosition = ss.prevFirstVisiblePosition;
        mPrevFirstVisibleChildHeight = ss.prevFirstVisibleChildHeight;
        mPrevScrolledChildrenHeight = ss.prevScrolledChildrenHeight;
        mPrevScrollY = ss.prevScrollY;
        mScrollY = ss.scrollY;
        mChildrenHeights = ss.childrenHeights;
        super.onRestoreInstanceState(ss.getSuperState());
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedStateScrolling ss = new SavedStateScrolling(superState);
        ss.prevFirstVisiblePosition = mPrevFirstVisiblePosition;
        ss.prevFirstVisibleChildHeight = mPrevFirstVisibleChildHeight;
        ss.prevScrolledChildrenHeight = mPrevScrolledChildrenHeight;
        ss.prevScrollY = mPrevScrollY;
        ss.scrollY = mScrollY;
        ss.childrenHeights = mChildrenHeights;
        return ss;
    }

    public void scrollVerticallyToPosition(int position) {
        RecyclerView.LayoutManager lm = getLayoutManager();

        if (lm != null && lm instanceof LinearLayoutManager) {
            ((LinearLayoutManager) lm).scrollToPositionWithOffset(position, 0);
        } else {
            lm.scrollToPosition(position);
        }
    }


    public void removeItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRecyclerView.removeItemDecoration(decoration);
    }

    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.removeOnItemTouchListener(listener);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }


    /**
     * 返回可见item的范围
     *
     * @return
     */
    public Pair<Integer, Integer> getVisibleDataPosition() {
        return new Pair<>(
                mFirstVisibleItem, lastVisibleItemPosition);
    }
}
