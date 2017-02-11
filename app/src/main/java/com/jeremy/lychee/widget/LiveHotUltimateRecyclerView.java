/*
 * Copyright(c) 2015 Marshal Chen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeremy.lychee.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.jeremy.lychee.adapter.LiveHotUltimateViewAdapter;
import com.jeremy.lychee.adapter.live.LiveDiscoveryColumnAdapter2;
import com.jeremy.lychee.listener.ObservableScrollViewCallbacks;
import com.jeremy.lychee.listener.Scrollable;
import com.jeremy.lychee.widget.RecyclerViewDecoration.DividerItemDecoration;
import com.jeremy.lychee.listener.ObservableScrollState;
import com.jeremy.lychee.utils.SavedStateScrolling;

/**
 * UltimateRecyclerView is a recyclerview which contains many features like  swipe to dismiss,animations,drag drop etc.
 */
public class LiveHotUltimateRecyclerView extends FrameLayout implements Scrollable {
    public RecyclerView mRecyclerView;
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
    private LiveHotUltimateViewAdapter mAdapter;


    // Fields that should be saved onSaveInstanceState
    private int mPrevFirstVisiblePosition;
    private int mPrevFirstVisibleChildHeight = -1;
    private int mPrevScrolledChildrenHeight;
    private int mPrevScrollY;
    private int mScrollY;
    private SparseIntArray mChildrenHeights = new SparseIntArray();

    // Fields that don't need to be saved onSaveInstanceState
    private ObservableScrollState mObservableScrollState;
    private ObservableScrollViewCallbacks mCallbacks;
    //private ScrollState mScrollState;
    private boolean mFirstScroll;
    private boolean mDragging;
    private boolean mIntercepted;
    private boolean mIsLoadMoreWidgetEnabled;
    private MotionEvent mPrevMoveEvent;
    private ViewGroup mTouchInterceptionViewGroup;


    protected ViewStub mEmpty;
    protected View mEmptyView;
    protected int mEmptyId;
    protected int[] defaultSwipeToDismissColors = null;
    public int showLoadMoreItemNum = 3;

    // added by Sevan Joe to support scrollbars
    private static final int SCROLLBARS_NONE = 0;
    private static final int SCROLLBARS_VERTICAL = 1;
    private static final int SCROLLBARS_HORIZONTAL = 2;
    private int mScrollbarsStyle;


    private int mVisibleItemCount = 0;
    private int mTotalItemCount = 0;
    private int previousTotal = 0;
    private int mFirstVisibleItem;

    public LiveHotUltimateRecyclerView(Context context) {
        super(context);
        initViews();
    }

    public LiveHotUltimateRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initViews();
    }

    public LiveHotUltimateRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initViews();
    }

    public void setRecylerViewBackgroundColor(@ColorInt int color) {
        mRecyclerView.setBackgroundColor(color);
    }

    protected void initViews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(com.jeremy.lychee.R.layout.ultimate_recycler_view_layout, this);
        mRecyclerView = (RecyclerView) view.findViewById(com.jeremy.lychee.R.id.ultimate_list);

        if (mRecyclerView != null) {

            mRecyclerView.setClipToPadding(mClipToPadding);
            if (mPadding != -1.1f) {
                mRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
        }

        setDefaultScrollListener();

        mEmpty = (ViewStub) view.findViewById(com.jeremy.lychee.R.id.emptyview);

        mEmpty.setLayoutResource(mEmptyId);

        if (mEmptyId != 0)
            mEmptyView = mEmpty.inflate();
        mEmpty.setVisibility(View.GONE);

    }

    /**
     * retrieve the empty view from the core
     *
     * @return the view item
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * Set custom empty view.The view will be shown if the adapter is null or the size of the adapter is zero.
     * You can customize it as loading view.
     *
     * @param emptyResourceId the Resource Id from the empty view
     */
    public void setEmptyView(@LayoutRes int emptyResourceId) {
        mEmptyId = emptyResourceId;

        mEmpty.setLayoutResource(mEmptyId);
        if (mEmptyId != 0)
            mEmptyView = mEmpty.inflate();
        mEmpty.setVisibility(View.GONE);
    }

    /**
     * Show the custom or default empty view.
     * You can customize it as loading view.
     */
    public void showEmptyView() {
        if (mEmptyId != 0)
            mEmpty.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the custom or default empty view.
     */
    public void hideEmptyView() {
        if (mEmptyId != 0)
            mEmpty.setVisibility(View.GONE);
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
    public void enableLoadmore() {
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
                    if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                        int lastPosition = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        boolean mLastItemVisible = (mAdapter.getItemCount() > 0) && lastPosition >= mAdapter.getItemCount() - 1;

//                        if (mAdapter instanceof GridLayoutRVAdapter) {
//                            if (mLastItemVisible && !((GridLayoutRVAdapter) mAdapter).isLoadNodata()) {
//
//                                if (!isLoadingMore && (mTotalItemCount - mVisibleItemCount) <= mFirstVisibleItem) {
//                                    //todo: there are some bugs needs to be adjusted for admob adapter
//                                    onLoadMoreListener.loadMore(mRecyclerView.getAdapter().getItemCount(), lastVisibleItemPosition);
//                                    isLoadingMore = true;
//                                    previousTotal = mTotalItemCount;
//                                }
//                            }
//                        }
//
//                        if (mAdapter instanceof LiveDiscoveryColumnAdapter) {
//                            if (mLastItemVisible && !((LiveDiscoveryColumnAdapter) mAdapter).isLoadNodata()) {
//                                if (!isLoadingMore && (mTotalItemCount - mVisibleItemCount) <= mFirstVisibleItem) {
//                                    //todo: there are some bugs needs to be adjusted for admob adapter
//                                    onLoadMoreListener.loadMore(mRecyclerView.getAdapter().getItemCount(), lastVisibleItemPosition);
//                                    isLoadingMore = true;
//                                    previousTotal = mTotalItemCount;
//                                }
//                            }
//                        }
//                        if (mAdapter instanceof LiveColumnRecommendAdapter) {
//                            if (mLastItemVisible && !((LiveColumnRecommendAdapter) mAdapter).isLoadNodata()) {
//                                if (!isLoadingMore && (mTotalItemCount - mVisibleItemCount) <= mFirstVisibleItem) {
//                                    //todo: there are some bugs needs to be adjusted for admob adapter
//                                    onLoadMoreListener.loadMore(mRecyclerView.getAdapter().getItemCount(), lastVisibleItemPosition);
//                                    isLoadingMore = true;
//                                    previousTotal = mTotalItemCount;
//                                }
//                            }
//                        }


                    }
                    if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        int lastPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        boolean mLastItemVisible = (mAdapter.getItemCount() > 0) && lastPosition >= mAdapter.getItemCount() - 1;
//                        if (mAdapter instanceof MyReaderAdapter) {
//                            if (mLastItemVisible && !((MyReaderAdapter) mAdapter).isLoadNodata()) {
//                                if (!isLoadingMore && (mTotalItemCount - mVisibleItemCount) <= mFirstVisibleItem) {
//                                    //todo: there are some bugs needs to be adjusted for admob adapter
//                                    onLoadMoreListener.loadMore(mRecyclerView.getAdapter().getItemCount(), lastVisibleItemPosition);
//                                    isLoadingMore = true;
//                                    previousTotal = mTotalItemCount;
//
//                                }
//                            }
//                        }
                        if (mAdapter instanceof LiveDiscoveryColumnAdapter2) {
                            if (mLastItemVisible && !((LiveDiscoveryColumnAdapter2) mAdapter).isLoadNodata()) {
                                if (!isLoadingMore && (mTotalItemCount - mVisibleItemCount) <= mFirstVisibleItem) {
                                    //todo: there are some bugs needs to be adjusted for admob adapter
                                    onLoadMoreListener.loadMore(mRecyclerView.getAdapter().getItemCount(), lastVisibleItemPosition);
                                    isLoadingMore = true;
                                    previousTotal = mTotalItemCount;
                                }
                            }
                        }
                    }

                }
            }
        };
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        if (mAdapter != null && mAdapter.getCustomLoadMoreView() == null)
            mAdapter.setCustomLoadMoreView(LayoutInflater.from(getContext())
                    .inflate(com.jeremy.lychee.R.layout.bottom_progressbar, null));

        mIsLoadMoreWidgetEnabled = true;

    }

    /**
     * If you have used {@link #disableLoadmore()} and want to enable loading more again,you can use this method.
     */
    public void reenableLoadmore() {
        enableLoadmore();
        if (mAdapter != null) {
            mAdapter.setCustomLoadMoreView(LayoutInflater.from(getContext())
                    .inflate(com.jeremy.lychee.R.layout.bottom_progressbar, null));
            mAdapter.isLoadMoreChanged = false;
        }
        mIsLoadMoreWidgetEnabled = true;
    }

    /**
     * If you have used {@link #disableLoadmore()} and want to enable loading more again,you can use this method.
     */
    public void reenableLoadmore(View customLoadingMoreView) {
        enableLoadmore();
        if (mAdapter != null) {
            mAdapter.setCustomLoadMoreView(customLoadingMoreView);
            mAdapter.isLoadMoreChanged = false;
        }
        mIsLoadMoreWidgetEnabled = true;
    }

    public boolean isLoadMoreEnabled() {
        return mIsLoadMoreWidgetEnabled;
    }

    /**
     * Remove loading more scroll listener
     */
    public void disableLoadmore() {
        setDefaultScrollListener();
        if (mAdapter != null)
            mAdapter.swipeCustomLoadMoreView(LayoutInflater.from(getContext())
                    .inflate(com.jeremy.lychee.R.layout.empty_progressbar, null));
        mIsLoadMoreWidgetEnabled = false;
    }


    /**
     * Set a listener that will be notified of any changes in scroll state or position.
     *
     * @param customOnScrollListener to set or null to clear
     * @deprecated Use {@link #addOnScrollListener(RecyclerView.OnScrollListener)} and
     * {@link #removeOnScrollListener(RecyclerView.OnScrollListener)}
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener customOnScrollListener) {
        mRecyclerView.setOnScrollListener(customOnScrollListener);
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
     * {@link #setAdapter(LiveHotUltimateViewAdapter)} but assumes existing adapter and the new adapter uses the same
     * ViewHolder and does not clear the RecycledViewPool.
     * <p>
     * Note that it still calls onAdapterChanged callbacks.
     *
     * @param adapter                       The new adapter to set, or null to set no adapter.
     * @param removeAndRecycleExistingViews If set to true, RecyclerView will recycle all existing Views. If adapters have stable ids and/or you want to animate the disappearing views, you may prefer to set this to false.
     */
    public void swapAdapter(LiveHotUltimateViewAdapter adapter, boolean removeAndRecycleExistingViews) {
        mRecyclerView.swapAdapter(adapter, removeAndRecycleExistingViews);
    }

    /**
     * Add an {@link RecyclerView.ItemDecoration} to this RecyclerView. Item decorations can affect both measurement and drawing of individual item views. Item decorations are ordered. Decorations placed earlier in the list will be run/queried/drawn first for their effects on item views. Padding added to views will be nested; a padding added by an earlier decoration will mean further item decorations in the list will be asked to draw/pad within the previous decoration's given area.
     *
     * @param itemDecoration Decoration to add
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * Add an {@link RecyclerView.ItemDecoration} to this RecyclerView. Item decorations can affect both measurement and drawing of individual item views.
     * <p>Item decorations are ordered. Decorations placed earlier in the list will be run/queried/drawn first for their effects on item views. Padding added to views will be nested; a padding added by an earlier decoration will mean further item decorations in the list will be asked to draw/pad within the previous decoration's given area.</p>
     *
     * @param itemDecoration Decoration to add
     * @param index          Position in the decoration chain to insert this decoration at. If this value is negative the decoration will be added at the end.
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecyclerView.addItemDecoration(itemDecoration, index);
    }

    /**
     * Sets the {@link RecyclerView.ItemAnimator} that will handle animations involving changes
     * to the items in this RecyclerView. By default, RecyclerView instantiates and
     * uses an instance of {@link android.support.v7.widget.DefaultItemAnimator}. Whether item animations are enabled for the RecyclerView depends on the ItemAnimator and whether
     * the LayoutManager {@link RecyclerView.LayoutManager#supportsPredictiveItemAnimations()
     * supports item animations}.
     *
     * @param animator The ItemAnimator being set. If null, no animations will occur
     *                 when changes occur to the items in this RecyclerView.
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    /**
     * Gets the current ItemAnimator for this RecyclerView. A null return value
     * indicates that there is no animator and that item changes will happen without
     * any animations. By default, RecyclerView instantiates and
     * uses an instance of {@link android.support.v7.widget.DefaultItemAnimator}.
     *
     * @return ItemAnimator The current ItemAnimator. If null, no animations will occur
     * when changes occur to the items in this RecyclerView.
     */
    public RecyclerView.ItemAnimator getItemAnimator() {
        return mRecyclerView.getItemAnimator();
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
    public void setAdapter(LiveHotUltimateViewAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter != null) {
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
        if ((adapter == null || mAdapter.getAdapterItemCount() == 0) && mEmptyId != 0) {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void updateHelperDisplays() {
        isLoadingMore = false;
        if (mAdapter == null)
            return;
        /**
         * fixed by jjHesk
         * + empty layout is NONE
         * + getItemCount is zero
         */
        if (mAdapter.getAdapterItemCount() == 0) {
            mEmpty.setVisibility(mEmptyId != 0 ? View.VISIBLE : View.GONE);
        } else if (mEmptyId != 0) {
            mEmpty.setVisibility(View.GONE);
        }

        if (mAdapter.getCustomLoadMoreView() == null) return;
        if (mAdapter.getAdapterItemCount() >= showLoadMoreItemNum && mAdapter.getCustomLoadMoreView().getVisibility() == View.GONE) {
            mAdapter.getCustomLoadMoreView().setVisibility(View.VISIBLE);
        }
        if (mAdapter.getAdapterItemCount() < showLoadMoreItemNum) {
            mAdapter.getCustomLoadMoreView().setVisibility(View.GONE);
        }

    }

    /**
     * @param adapter input param
     * @deprecated Short for some ui effects
     */
    @Deprecated
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                update();
            }

            @Override
            public void onChanged() {
                super.onChanged();
                update();
            }

            private void update() {
                isLoadingMore = false;
//
            }

        });
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }


    public interface OnLoadMoreListener {
        void loadMore(int itemsCount, final int maxLastVisiblePosition);
    }

    public static enum LAYOUT_MANAGER_TYPE {
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
//    public void setParallaxHeader(@LayoutRes int mLayout, int resId) {
//        View h_layout = LayoutInflater.from(getContext()).inflate(mLayout, null);
//        setParallaxHeader(h_layout, resId);
//    }

    /**
     * Set the parallax header of the recyclerview
     *
     * @param header the view
     */
    public void setParallaxHeader(View header, int resId) {
        mHeader = new CustomRelativeWrapper(header.getContext());
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeader.addView(header, header.getLayoutParams());
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
//    public void setNormalHeader(View header) {
//        setParallaxHeader(header, 0);
//        isParallaxHeader = false;
//    }

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

    /**
     * the observable scroll view call backs
     *
     * @param listener listener to set
     */
    public void setScrollViewCallbacks(ObservableScrollViewCallbacks listener) {
        mCallbacks = listener;
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstScroll = mDragging = true;
                    mCallbacks.onDownMotionEvent();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mIntercepted = false;
                    mDragging = false;
                    mCallbacks.onUpOrCancelMotionEvent(mObservableScrollState);
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);

//        if (mCallbacks != null) {
//            URLogs.d("ev---"+ev.getActionMasked());
//            switch (ev.getActionMasked()) {
//                case MotionEvent.ACTION_DOWN:
//                    // Whether or not motion events are consumed by children,
//                    // flag initializations which are related to ACTION_DOWN events should be executed.
//                    // Because if the ACTION_DOWN is consumed by children and only ACTION_MOVEs are
//                    // passed to parent (this view), the flags will be invalid.
//                    // Also, applications might implement initialization codes to onDownMotionEvent,
//                    // so call it here.
//                    mFirstScroll = mDragging = true;
//                    mCallbacks.onDownMotionEvent();
//                    break;
//            }
//        }
//        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void setTouchInterceptionViewGroup(ViewGroup viewGroup) {
        mTouchInterceptionViewGroup = viewGroup;
        setObserableScrollListener();
    }

    @Override
    public void scrollVerticallyTo(int y) {
        View firstVisibleChild = getChildAt(0);
        if (firstVisibleChild != null) {
            int baseHeight = firstVisibleChild.getHeight();
            int position = y / baseHeight;
            scrollVerticallyToPosition(position);
        }
    }

    public void scrollVerticallyToPosition(int position) {
        RecyclerView.LayoutManager lm = getLayoutManager();

        if (lm != null && lm instanceof LinearLayoutManager) {
            ((LinearLayoutManager) lm).scrollToPositionWithOffset(position, 0);
        } else {
            lm.scrollToPosition(position);
        }
    }

    @Override
    public int getCurrentScrollY() {
        return mScrollY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {

            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mIntercepted = false;
                    mDragging = false;
                    mCallbacks.onUpOrCancelMotionEvent(mObservableScrollState);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mPrevMoveEvent == null) {
                        mPrevMoveEvent = ev;
                    }
                    float diffY = ev.getY() - mPrevMoveEvent.getY();
                    mPrevMoveEvent = MotionEvent.obtainNoHistory(ev);
                    if (getCurrentScrollY() - diffY <= 0) {
                        // Can't scroll anymore.

                        if (mIntercepted) {
                            // Already dispatched ACTION_DOWN event to parents, so stop here.
                            return false;
                        }

                        // Apps can set the interception target other than the direct parent.
                        final ViewGroup parent;
                        if (mTouchInterceptionViewGroup == null) {
                            parent = (ViewGroup) getParent();
                        } else {
                            parent = mTouchInterceptionViewGroup;
                        }

                        // Get offset to parents. If the parent is not the direct parent,
                        // we should aggregate offsets from all of the parents.
                        float offsetX = 0;
                        float offsetY = 0;
                        for (View v = this; v != null && v != parent; v = (View) v.getParent()) {
                            offsetX += v.getLeft() - v.getScrollX();
                            offsetY += v.getTop() - v.getScrollY();
                        }
                        final MotionEvent event = MotionEvent.obtainNoHistory(ev);
                        event.offsetLocation(offsetX, offsetY);

                        if (parent.onInterceptTouchEvent(event)) {
                            mIntercepted = true;

                            // If the parent wants to intercept ACTION_MOVE events,
                            // we pass ACTION_DOWN event to the parent
                            // as if these touch events just have began now.
                            event.setAction(MotionEvent.ACTION_DOWN);

                            // Return this onTouchEvent() first and set ACTION_DOWN event for parent
                            // to the queue, to keep events sequence.
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    parent.dispatchTouchEvent(event);
                                }
                            });
                            return false;
                        }
                        // Even when this can't be scrolled anymore,
                        // simply returning false here may cause subView's click,
                        // so delegate it to super.
                        return super.onTouchEvent(ev);
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
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



}
