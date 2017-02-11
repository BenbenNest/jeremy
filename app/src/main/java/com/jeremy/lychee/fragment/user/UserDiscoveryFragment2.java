package com.jeremy.lychee.fragment.user;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.adapter.news.ContentRVAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.widget.LayoutManager.ContentRVLayoutManager;
import com.jeremy.lychee.widget.LoadingRecyclerViewFooter;
import com.jeremy.lychee.widget.RecyclerViewDecoration.ContentRVDecoration;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.presenter.ChannelFragmentPresenter;
import com.jeremy.lychee.widget.recyclerview.RecyclerViewWrapper;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.widget.toptoast.TopToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserDiscoveryFragment2 extends BaseFragment {

    static private String TAG = UserDiscoveryFragment2.class.toString();
    @Bind(com.jeremy.lychee.R.id.article_recycler_view)
    RecyclerViewWrapper recyclerViewWrapper;

    private ViewGroup rootView;
    private ContentRVAdapter adapter;
    private ContentRVLayoutManager layoutManager;
    private int mCurrentMode = 0;//通栏样式
    private ContentRVDecoration contentRVDecoration;

    private static final int REQUEST_ARTICLES_SIZE = 10;
    private static final int REQUEST_HOT_ARTICLES_START = 1;
    private boolean mHasMoreArticles = false;
    private int mHotArticleStart = REQUEST_HOT_ARTICLES_START;
    private boolean isRefreshing = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (null == rootView) {
            rootView = (ViewGroup)inflater.inflate(
                    com.jeremy.lychee.R.layout.content_fragment_layout, container, false);
            ButterKnife.bind(this, rootView);
            initUI();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI() {
        recyclerViewWrapper.setBackgroundColor(Color.parseColor("#ffffff"));
        ContentRVAdapter.ContentRVOnItemClickListener listener = (view, position) -> {
            int itemViewType = adapter.getContentItemViewType(position);
            NewsListData data = null;
            switch (itemViewType) {
                case ContentRVAdapter.LAST_REFRESH_TAG:
                    recyclerViewWrapper.setRefreshing(true);
                    layoutManager.smoothScrollToPosition(recyclerViewWrapper.mRecyclerView, null, 0);
                    loadData(false);
                    return;
                case ContentRVAdapter.IMAGE_NEWS_TYPE1:
                case ContentRVAdapter.IMAGE_NEWS_TYPE2:
                case ContentRVAdapter.NORMAL_NEWS_TYPE:
                case ContentRVAdapter.NO_PIC_NEWS_TYPE:
                case ContentRVAdapter.EX_FOCUS_NEWS_TAG:
                default:
                    List<NewsListData> list = adapter.getList();
                    int listPosition = adapter.getListPosition(position);
                    if (listPosition >= 0 && listPosition < list.size()) {
                            data = list.get(listPosition);
                    }
                    break;

            }
            if (data == null) {
                return;
            }
            if (getActivity() instanceof SlidingActivity) {
                ChannelFragmentPresenter.OpenNewsDetailActivity(
                        (SlidingActivity) getActivity(), data, null, null);
            }

        };
        adapter = new ContentRVAdapter(getContext(), mCurrentMode, listener);
        adapter.setItemType(mCurrentMode);
        adapter.setHasSearchView(false);
        layoutManager = new ContentRVLayoutManager(getContext(), getSpanCount(), adapter);
        recyclerViewWrapper.setLayoutManager(layoutManager);
        recyclerViewWrapper.setDefaultOnRefreshListener(() -> loadData(false));
        recyclerViewWrapper.setAdapter(adapter);
        recyclerViewWrapper.enableLoadMore();
        contentRVDecoration = new ContentRVDecoration(getSpanCount(), getDecorationOffset(), adapter);
        recyclerViewWrapper.addItemDecoration(contentRVDecoration);

        recyclerViewWrapper.setOnLoadMoreListener((itemsCount, maxLastVisiblePosition) -> {
            if (mHasMoreArticles) {
                loadData(true);
            }
        });
        adapter.setFooterClickListener(new LoadingRecyclerViewFooter.FooterClickListener() {
            @Override
            public void onFullRefresh() {
                layoutManager.smoothScrollToPosition(recyclerViewWrapper.mRecyclerView, null, 0);
                if (mHasMoreArticles) {
                    loadData(true);
                }
            }

            @Override
            public void onErrorClick() {
                //nothing to do
            }
        });
        recyclerViewWrapper.setErrorViewClickListener(v -> {
            loadData(false);
            recyclerViewWrapper.showLoadingView();
        });
    }


    private int getSpanCount() {
        switch (mCurrentMode) {
            case 1:
                return 2;
            case 0:
            default:
                return 1;
        }
    }

    private int getDecorationOffset() {
        switch (mCurrentMode) {
            case 0:
                return DensityUtils.dip2px(getContext(), 12);
            case 1:
            default:
                return DensityUtils.dip2px(getContext(), 2f);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        new Handler().post(() -> {
            if (getUserVisibleHint() && !mIgnore.get()) {
//                updateList();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        loadData(false);
    }

    public void loadData(boolean loadMore) {
        if (null != rootView) {
            if (recyclerViewWrapper == null) {
                //java.lang.NullPointerException: Attempt to read from field
                //'boolean PullListRecyclerView.isLoading' on a null object reference
                recyclerViewWrapper =
                        (RecyclerViewWrapper) rootView.findViewById(com.jeremy.lychee.R.id.article_recycler_view);
            }
            if (isRefreshing) {
                return;
            }
            isRefreshing = true;
            requestAndShowHotArticles(loadMore);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * TODO(wangp)
     */
    private Events.IgnoreRefreshNextTime.IsIgnore mIgnore =
            new Events.IgnoreRefreshNextTime.IsIgnore(true);//初始化true:避免onResume执行两次造成的多余load
    final public void onEventMainThread(Events.IgnoreRefreshNextTime event) {
        mIgnore = event.mData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QEventBus.getEventBus().register(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !mIgnore.get()) {
            //TODO(wangp)
//            updateList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QEventBus.getEventBus().unregister(this);
    }


    private void requestAndShowHotArticles(boolean isLoadMore) {
        if (!isLoadMore) {
            mHotArticleStart = REQUEST_HOT_ARTICLES_START;
        }
        Observable.just(mHotArticleStart)
                .flatMap(this::getHotArticles)
                .doOnNext(it -> {
                    recyclerViewWrapper.hideLoadingView();
                    recyclerViewWrapper.setRefreshing(false);
                    if (!isLoadMore) {
                        adapter.clear();
                    }
                    if (it.size() > 0) {
                        mHasMoreArticles = it.size() == REQUEST_ARTICLES_SIZE;
                        mHotArticleStart++;
                    } else {
                        recyclerViewWrapper.setIsFullData(true);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    isRefreshing = false;
                    adapter.insertList(it);

                }, throwable -> {
                    TopToastUtil.showTopToast(recyclerViewWrapper, getResources().getString(com.jeremy.lychee.R.string.top_toast_net_error));
                    throwable.printStackTrace();
                    recyclerViewWrapper.setRefreshing(false);
                    if (adapter.getList() == null || adapter.getList().size() == 0) {
                        recyclerViewWrapper.showErrorView();
                    }
                    isRefreshing = false;
                });
    }

    private Observable<List<NewsListData>> getHotArticles(int start) {
        return OldRetroAdapter.getService().getUserHotArticle(start, REQUEST_ARTICLES_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(respones -> Observable.just(respones.getData()));
    }

        static public class HotChannelRecyclerView extends RecyclerView {
        static boolean s_isDragEnabled = false;
        static boolean s_isDragging = false;

        public HotChannelRecyclerView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

//        @Override
//        public boolean onTouchEvent(MotionEvent e) {
//            if (!custom) return false;
//            switch (e.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                case MotionEvent.ACTION_MOVE:
//                    QEventBus.getEventBus().post(new Events.OnDragEnabled(true));
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                case MotionEvent.ACTION_UP:
//                    QEventBus.getEventBus().post(new Events.OnDragEnabled(false));
//                    break;
//            }
//            return super.onTouchEvent(e);
//        }
//
//        private float xDistance, yDistance, lastX, lastY;
//        boolean custom = true;
//
//        @Override
//        public boolean onInterceptTouchEvent(MotionEvent ev) {
//            switch (ev.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    xDistance = yDistance = 0f;
//                    lastX = ev.getX();
//                    lastY = ev.getY();
//                    return false;
//                case MotionEvent.ACTION_MOVE:
//                    final float curX = ev.getX();
//                    final float curY = ev.getY();
//                    xDistance += Math.abs(curX - lastX);
//                    yDistance += Math.abs(curY - lastY);
//                    lastX = curX;
//                    lastY = curY;
//                    if (yDistance > xDistance) {
//                        custom = false;
//                    } else {
//                        custom = true;
//                    }
//                    return true;
//            }
//
//            return super.onInterceptTouchEvent(ev);
//        }

    }
}
