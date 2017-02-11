package com.jeremy.lychee.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.jeremy.lychee.adapter.user.LoadViewAction;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.utils.ScrollSmoothLineaerLayoutManager;
import com.jeremy.lychee.widget.MySwipeRefreshLayout;
import com.qihoo.livecloud.tools.NetUtil;

/**
 * @author：leikang on 15-11-22 13:50
 * @mail：leikang@360.cn
 *
 */
public class PullListRecyclerView extends FrameLayout implements View.OnClickListener,
        ViewGroup.OnHierarchyChangeListener, LoadViewAction {

    private Context mContext;

    public boolean isLoading = false;

    // 是否可以下拉刷新
    private boolean mEnable = true;

    protected RecyclerView mRecyclerView;

    private MySwipeRefreshLayout headerView;

    protected BaseRecyclerViewAdapter recylerViewAdapter;

    private FrameLayout emptyView;

    protected boolean scrollEnable = true;

    private SparseArray<View> stateViews = new SparseArray<View>();

    protected LoadViewAction footLoadAction;

    public PullListRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullListRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getContentViewId(), this);

        headerView = (MySwipeRefreshLayout) view.findViewById(com.jeremy.lychee.R.id.recyclerview_header);
        headerView.setColorSchemeResources(com.jeremy.lychee.R.color.base_red_color);
        //emptyView = (FrameLayout)view.findViewById(R.id.emptyview);

        //TODO:设置SwipeRefreshBar 的顶部缩进
        TypedArray a = context.obtainStyledAttributes(attrs, com.jeremy.lychee.R.styleable.PullListRecyclerView);
        headerView.setRefreshBarOffset(
                (int) a.getDimension(com.jeremy.lychee.R.styleable.PullListRecyclerView_refershBarStartOffset, 0));
        a.recycle();

        emptyView = new FrameLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(emptyView, layoutParams);

        mRecyclerView = (RecyclerView)view.findViewById(com.jeremy.lychee.R.id.recyclerview);
        mRecyclerView.setLayoutManager(new ScrollSmoothLineaerLayoutManager(context, LinearLayoutManager.VERTICAL, false, 1000));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && scrollEnable && !isLoading) {
                    ScrollSmoothLineaerLayoutManager manager = (ScrollSmoothLineaerLayoutManager) recyclerView.getLayoutManager();
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1) && recylerViewAdapter != null) {

                        if (!NetUtil.isConnected(context)) {
                            recylerViewAdapter.setDataState(BaseRecyclerViewAdapter.DataState.NET_ERROR);
                            return;
                        }

                        if (recylerViewAdapter.isBackwardLoadEnable()) {
                            footLoadAction.onLoading();
                            recylerViewAdapter.load(true);

                            Logger.d("base adapter:", "滑到底部开始加载");

                        } else {
                            footLoadAction.onNormal(false);
                        }
                    }
                }

            }

        });
        footLoadAction = this;
        setOnHierarchyChangeListener(this);
        //add maskview for recycleriew enable control
        maskView = new MaskView(getContext());
        ((FrameLayout)findViewById(com.jeremy.lychee.R.id.container)).addView(maskView);
        setRecyclerViewEnabled(true);
    }

    private MaskView maskView;
    public void setRecyclerViewEnabled(boolean enabled){
        maskView.setVisibility(enabled?GONE:VISIBLE);
    }

    private class MaskView extends RelativeLayout{

        public MaskView(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }

    }
    public void setItemAnimator(RecyclerView.ItemAnimator animator){
        mRecyclerView.setItemAnimator(animator);
    }

    protected int getContentViewId() {
        return com.jeremy.lychee.R.layout.base_recycler_view_layout;
    }

    private RecyclerView.AdapterDataObserver dataSetObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            onDataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            onDataChanged();
        }
    };

    @Override
    public void onClick(View v) {
        if (recylerViewAdapter == null) {
            return;
        }

        if (recylerViewAdapter.isBackwardLoadEnable()) {

        }
    }

    public void enablePullToRefresh(boolean enable) {
        this.mEnable = enable;
        headerView.setEnabled(mEnable);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {

    }


    @Override
    public void onLoading() {
        scrollEnable = false;
        isLoading = true;
        recylerViewAdapter.onLoading();
    }

    @Override
    public boolean onNormal(boolean hasMore) {
        isLoading = false;
        recylerViewAdapter.onNormal(hasMore);
        return false;
    }

    private void invalidateFooterView() {
        if (recylerViewAdapter == null) {
            return;
        }

        if (footLoadAction != null) {
            boolean removable =  footLoadAction.onNormal(recylerViewAdapter.isBackwardLoadEnable());
            isLoading = false;

        }
    }

    /***
     * 设置listview 中的数据适配器
     */
    public void setAdapter(BaseRecyclerViewAdapter adapter, boolean load) {

        if (adapter != null) {
            recylerViewAdapter = adapter;
            recylerViewAdapter.registerAdapterDataObserver(dataSetObserver);
            mRecyclerView.setAdapter(recylerViewAdapter);
            recylerViewAdapter.setHeaderView(headerView);
            recylerViewAdapter.setDataState(BaseRecyclerViewAdapter.DataState.LOADING);
            if (load) {
                loadData();
            }
            adapter.setPullListRecyclerView(this);
        }
    }

    public void loadData() {
        recylerViewAdapter.load(false);
    }

    public void setEnable(boolean enable) {
        mEnable = enable;
        invalidate();
    }


    /**
     * 设置下拉刷新view的展现样式
     */
    public void onDataChanged() {
        scrollEnable = true;
        int currentEmptyViewType = recylerViewAdapter.getEmptyViewType();
        if (currentEmptyViewType >= 0) {

            View stateView = stateViews.get(currentEmptyViewType);
            if (stateView != null && emptyView.getChildCount() > 0) {
                if (stateView != emptyView.getChildAt(0)) {
                    emptyView.removeAllViews();
                } else {
                    mRecyclerView.setVisibility(GONE);
                    emptyView.setVisibility(VISIBLE);
                    return;
                }
            } else {
                emptyView.removeAllViews();
            }

            if (recylerViewAdapter != null) {
                //加载模块自己定义的空页面
                View child = recylerViewAdapter.getEmptyView(emptyView, stateView, recylerViewAdapter.getCurrentState());
                if (child == emptyView) {
                    if (emptyView.getChildCount() != 1) {
                        throw new RuntimeException("emptyView 的子元素只能为1");
                    } else {
                        child = emptyView.getChildAt(0);
                    }
                } else if (child != null) {
                    if (child.getParent() != null) {
                        ViewGroup viewGroup = (ViewGroup) child.getParent();
                        viewGroup.removeView(child);
                    }
                    LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    emptyView.addView(child, layoutParams);
                }
                stateViews.put(currentEmptyViewType, child);
            }
            mRecyclerView.setVisibility(GONE);
            emptyView.setVisibility(VISIBLE);
        } else {
            invalidateFooterView();
            mRecyclerView.setVisibility(VISIBLE);
            emptyView.setVisibility(GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (recylerViewAdapter == null || recylerViewAdapter.getEmptyViewType() >= 0) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        if (mRecyclerView!=null) {
            mRecyclerView.addItemDecoration(decor);
        }
    }

    public RecyclerView getInternalRecyclerView() {
        return mRecyclerView;
    }
}
