package com.jeremy.lychee.base;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.customview.PullListRecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.functions.Action0;

/**
 * @author：leikang on 15-11-20 21:13
 * @mail：leikang@360.cn
 */
public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    protected final Context mContext;

    protected List<T> mItems = new ArrayList<T>();

    public abstract boolean isBackwardLoadEnable();

    public abstract void load(boolean isLoadMore);

    public boolean hasFooterView = true;

    public SwipeRefreshLayout swipeRefreshLayout;

    private ProgressBar mFooterLoading;
    private TextView mFooterLabel;
    private View mFooterView;
    private FrameLayout mCustomFooter;

    private PullListRecyclerView pullListRecyclerView;

    public Context getContext() {
        return mContext;
    }

    public void setHeaderView(SwipeRefreshLayout headerView) {
        swipeRefreshLayout = headerView;
        swipeRefreshLayout.setOnRefreshListener(() -> load(false));
    }

    /**
     * 当前数据集的状态
     */
    public interface DataState {
        int LOADING = 0;            //加载中
        int ERROR = 1;              //加载错误
        int EMPTY = 2;              //加载数据为空
        int NORMAL = 3;             //已经加载到数据
        int NET_ERROR = 4;          //网络连接错误
    }

    /**
     * 这里normal 和 footer 设置大值，区别与子adapter中的item_types
     */
    protected class VIEW_TYPES {
        public static final int NORMAL = 1000;
        public static final int FOOTER = 999;
    }

    private int currentState = DataState.LOADING;

    public void setDataState(int state) {

        switch (state) {
            case DataState.NET_ERROR:
                Logger.d("set net error");
                break;
            case DataState.EMPTY:
                Logger.d("set empty");
                break;
            case DataState.ERROR:
                Logger.d("set error");
                break;
            case DataState.NORMAL:
                Logger.d("set normal");
                break;
        }

        currentState = state;
        if (currentState != DataState.LOADING) {
            swipeRefreshLayout.setRefreshing(false);
        }
        notifyDataSetChanged();
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setHasFooterView(boolean has) {
        hasFooterView = has;
        notifyDataSetChanged();
    }

    public void refreshCurrentState() {
        if (mItems.size() > 0) {
            currentState = DataState.NORMAL;
        } else {
            currentState = DataState.EMPTY;
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    public int getEmptyViewType() {
        boolean isEmpty = getItemCount() == 0;
        if (isEmpty) {
            if (currentState == DataState.LOADING
                    || currentState == DataState.EMPTY
                    || currentState == DataState.ERROR
                    || currentState == DataState.NET_ERROR
                    ) {
                return currentState;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public BaseRecyclerViewAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public View getEmptyView(ViewGroup parent, View convertView, int currentState) {
        //默认空页面、错误、加载页面
        if (convertView == null) {
            switch (currentState) {
                case DataState.EMPTY: {
                    convertView = View.inflate(mContext, com.jeremy.lychee.R.layout.common_nodata_layout, parent);
                    break;
                }
                //当前处理error为网络错误，后续根据不同情况override
                case DataState.NET_ERROR:
                case DataState.ERROR: {
                    convertView = View.inflate(mContext, com.jeremy.lychee.R.layout.common_error_layout, parent);
                    convertView.findViewById(com.jeremy.lychee.R.id.net_error_panel).setOnClickListener(v -> {
                        //TODO 默认这里为点击重新获取数据
                        setDataState(DataState.LOADING);
                        load(false);
                    });
                    break;
                }
                case DataState.LOADING: {
                    convertView = View.inflate(mContext, com.jeremy.lychee.R.layout.common_loading_layout, parent);
                    break;
                }
                default:
                    return null;
            }
        }
        return convertView;
    }

    private int mCustomFooterRes = 0;
    public void setCustomFooterView(int res){
        mCustomFooterRes = res;
    }

    private Action0 mOnCustomFooterClickLinstener;
    public void setOnCustomFooterClickLinstener(Action0 listener){
        mOnCustomFooterClickLinstener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPES.FOOTER) {
            mFooterView = LayoutInflater.from(parent.getContext()).inflate(com.jeremy.lychee.R.layout.common_load_more, parent, false);
            FooterViewHolder viewHolder = new FooterViewHolder(mFooterView, true);
            mFooterLabel = (TextView) mFooterView.findViewById(com.jeremy.lychee.R.id.load_label);
            mFooterLoading = (ProgressBar) mFooterView.findViewById(com.jeremy.lychee.R.id.load_progressBar);
            mCustomFooter = (FrameLayout)mFooterView.findViewById(com.jeremy.lychee.R.id.custom_container);
            mFooterLabel.setOnClickListener(v -> {
                onLoading();
                load(true);
            });

            if (getItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return (VH) viewHolder;
        } else {
            return onCreateCustomViewHolder(parent, viewType);
        }
    }

    protected int getFooterViewId() {
        return com.jeremy.lychee.R.layout.common_load_more;
    }

    public void onLoading() {

        Logger.d("on loading");
        //mFooterView.setVisibility(VISIBLE);
        if (hasFooterView && mFooterView != null) {
            mFooterLabel.setText(com.jeremy.lychee.R.string.doing_update);
            mFooterLoading.setVisibility(View.VISIBLE);
            mCustomFooter.setVisibility(View.GONE);
        }
    }

    public void onNormal(boolean hasMore) {

        Logger.d("on onnormal");
        //footerView.setVisibility(VISIBLE);
        if (!hasFooterView || mFooterView == null)
            return;

        if (hasMore) {
            mFooterLabel.setText(com.jeremy.lychee.R.string.common_load_more);
            mFooterLoading.setVisibility(View.GONE);
        } else {
            if(mCustomFooterRes != 0){
                if (mCustomFooter.getChildCount() == 0) {
                    View custom = LayoutInflater.from(getContext()).inflate(mCustomFooterRes, null);
                    mCustomFooter.addView(custom);
                    if (mOnCustomFooterClickLinstener != null) {
                        custom.setOnClickListener(v -> mOnCustomFooterClickLinstener.call());
                    }
                }
                mCustomFooter.setVisibility(View.VISIBLE);
            } else {
                mFooterLabel.setText(com.jeremy.lychee.R.string.common_no_data);
                mFooterLoading.setVisibility(View.GONE);
                mCustomFooter.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (hasFooterView && position == getItemCount() - 1) {
            return VIEW_TYPES.FOOTER;
        } else
            return VIEW_TYPES.NORMAL;
    }


    public abstract VH onCreateCustomViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindCustomViewHolder(VH holder, int position);

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        ProgressBar loadProgressBar;
        TextView loadLabel;

        public FooterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            loadProgressBar = (ProgressBar) itemView.findViewById(com.jeremy.lychee.R.id.load_progressBar);
            loadLabel = (TextView) itemView.findViewById(com.jeremy.lychee.R.id.load_label);
        }

    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (hasFooterView && position == getItemCount() - 1) {

        } else {
            onBindCustomViewHolder(holder, position);
        }

    }

    @Override
    public int getItemCount() {

        if (mItems.size() == 0) {
            return 0;
        }

        return hasFooterView ? mItems.size() + 1 : mItems.size();
    }

    public int size() {
        if (mItems == null) {
            return 0;
        } else {
            return mItems.size();
        }
    }

    public final void insert(int index, T item) {

        Logger.d("insert one to" + index);

        if (item == null) {
            return;
        }

        synchronized (this) {
            mItems.add(index, item);
        }

        refreshCurrentState();
        notifyDataSetChanged();
    }

    public final void insert(int index, Collection<? extends T> items) {

        Logger.d("insert list to" + index);

        synchronized (this) {
            if (items != null && !items.isEmpty()) {
                mItems.addAll(index, items);
            }
        }

        refreshCurrentState();
        notifyDataSetChanged();
    }

    public void append(Collection<? extends T> items) {

        Logger.d("append list" );

        if (items != null && !items.isEmpty()) {
            synchronized (this) {
                mItems.addAll(items);
            }
        }

        refreshCurrentState();
        notifyDataSetChanged();
    }

    public void append(T item) {

        Logger.d("append one item");

        if (item == null) {
            return;
        }

        synchronized (this) {
            mItems.add(item);
        }

        refreshCurrentState();
        notifyDataSetChanged();
    }

    public void clearWithOutNotifyDataSetChanged() {
        synchronized (this) {
            mItems.clear();
        }
    }

    public void clear() {
        synchronized (this) {
            mItems.clear();
            currentState = DataState.EMPTY;
            notifyDataSetChanged();
        }
    }

    public int indexOf(T item) {
        refreshCurrentState();
        return mItems.indexOf(item);
    }

    public final List<T> getItems() {
        return mItems;
    }

    public T getItem(int position) {
        if (position >= 0 && position < mItems.size()) {
            return mItems.get(position);
        }
        return null;
    }

    public void setPullListRecyclerView(PullListRecyclerView pullListRecyclerView) {
        this.pullListRecyclerView = pullListRecyclerView;
    }

    public void setRecyclerViewEnabled(boolean enalbed){
        pullListRecyclerView.setRecyclerViewEnabled(enalbed);
    }
}
