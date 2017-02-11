package com.jeremy.lychee.fragment.live;

/**
 * Created by chengyajun on 2016/1/13.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.adapter.live.LiveDiscoveryColumnAdapter2;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.channel.QHState;
import com.jeremy.lychee.db.LiveHotItem;
import com.jeremy.lychee.db.LiveHotItemDao;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.widget.LiveHotUltimateRecyclerView;
import com.jeremy.lychee.widget.shareWindow.ShareManager;
import com.jeremy.lychee.widget.toptoast.TopToastUtil;

import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.hitLog.HitLogHelper;
import com.jeremy.lychee.widget.MySwipeRefreshLayout;
import com.qihoo.sdk.report.QHStatAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LiveDiscoveryColumnFragment2 extends BaseFragment {

    @Bind(com.jeremy.lychee.R.id.recyclerView)
    LiveHotUltimateRecyclerView recyclerView;
    @Bind(com.jeremy.lychee.R.id.pull_refresh_view)
    MySwipeRefreshLayout swipeLayout;
    @Bind(com.jeremy.lychee.R.id.loading_layout)
    View loading_layout;
    @Bind(com.jeremy.lychee.R.id.error_layout)
    View error_layout;

    View rootView;
    LiveDiscoveryColumnAdapter2 mAdapter;
    //    private int startNum = 0;
    //for saving hitlog info
    ArrayList<String> columnlist;
    LinearLayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(com.jeremy.lychee.R.layout.layout_discovery_column, container, false);
        columnlist = new ArrayList<String>();
        ButterKnife.bind(this, rootView);
        initView();
        initCache();
        queryDataFromNet();
        initEvent();
        QEventBus.getEventBus().register(this);
        return rootView;
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {
        error_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryDataFromNet();
                loading_layout.setVisibility(View.VISIBLE);
                error_layout.setVisibility(View.GONE);
            }
        });


        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setRecylerViewBackgroundColor(getResources().getColor(com.jeremy.lychee.R.color.live_hot_bg));


        mAdapter = new LiveDiscoveryColumnAdapter2(this.getActivity(), null);

        recyclerView.setAdapter(mAdapter);
        recyclerView.enableLoadmore();
        mAdapter.setCustomLoadMoreView(LayoutInflater.from(this.getContext()).inflate(com.jeremy.lychee.R.layout.custom_bottom_bar, null));

        recyclerView.setOnLoadMoreListener(new LiveHotUltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                insertMore();
            }
        });
        mAdapter.setErrorListener(v -> {
            mAdapter.showLoadingDataView();
            insertMore();
        });
        mAdapter.setNoDataListener(v -> {
            //没有数据刷新网络
            swipeLayout.setRefreshing(true);
            layoutManager.smoothScrollToPosition(recyclerView.mRecyclerView, null, 0);
            goRefresh();
        });

        swipeLayout.setEnabled(true);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新就上传数据
                HitLogHelper.getHitLogHelper().postLogToNet();
                goRefresh();
            }
        });

    }


    private void initCache() {
        //先从本地获取数据显示，如果本地无数据则网络获取
        loading_layout.setVisibility(View.VISIBLE);
        Observable
                .create(new Observable.OnSubscribe<List<LiveHotItem>>() {
                    @Override
                    public void call(Subscriber<? super List<LiveHotItem>> subscriber) {
                        subscriber.onNext(getCacheData(0));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LiveHotItem>>() {
                    @Override
                    public void onCompleted() {
                        loading_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loading_layout != null) {
                            loading_layout.setVisibility(View.GONE);
                        }
                        if (error_layout != null) {
                            error_layout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNext(List<LiveHotItem> listResult) {
                        if (listResult != null && listResult.size() > 0) {

                            if (loading_layout != null) {
                                loading_layout.setVisibility(View.GONE);
                            }
                            mAdapter.setData(listResult);
                            mAdapter.notifyDataSetChanged();
                            recyclerView.scrollVerticallyToPosition(0);
                        }//本地无数据

                    }
                });
    }

    private List<LiveHotItem> getCacheData(int start) {
        try {
            LiveHotItemDao dao = ContentApplication.getDaoSession().getLiveHotItemDao();

            List<LiveHotItem> listData = dao.queryBuilder().limit(9).orderDesc(LiveHotItemDao.Properties.Time).offset(start).build().list();
            if (listData != null) {
                return listData;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void cacheData(Result<List<LiveHotItem>> listModelBase) {
        //下拉刷新存数据
        if (listModelBase != null && listModelBase.getData().size() > 0) {
            try {
                LiveHotItemDao dao = ContentApplication.getDaoSession().getLiveHotItemDao();
                List<LiveHotItem> list = listModelBase.getData();
                for (int i = list.size() - 1; i >= 0; i--) {
                    long time = System.currentTimeMillis();
                    list.get(i).setTime(time);
                    dao.insertOrReplaceInTx(list.get(i));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void queryDataFromNet() {


        OldRetroAdapter.getService().getLiveHotDataList()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(data -> cacheData(data))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setViewAdapter, throwable -> {
                    throwable.printStackTrace();
                    swipeLayout.setRefreshing(false);
                    recyclerView.scrollVerticallyToPosition(0);
                    if (loading_layout != null) {
                        loading_layout.setVisibility(View.GONE);
                    }

                    if (mAdapter.getmList() != null && mAdapter.getmList().size() == 0) {
                        error_layout.setVisibility(View.VISIBLE);
                    } else {
                        TopToastUtil.showTopToast(
                                recyclerView, String.format("网络不给力，请检查网络刷新重试"));
                    }
                });
    }

    private void setViewAdapter(Result<List<LiveHotItem>> listResult) {
        swipeLayout.setRefreshing(false);
        recyclerView.scrollVerticallyToPosition(0);
        if (listResult != null && listResult.getData().size() > 0) {//网络有数据
            List<LiveHotItem> list = listResult.getData();
            if (loading_layout != null) {
                loading_layout.setVisibility(View.GONE);
            }
            mAdapter.insert(list, 0, true);
            mAdapter.notifyDataSetChanged();
            TopToastUtil.showTopToast(
                    recyclerView, String.format("又有新的内容啦"));
        } else {//网络无数据
            if (mAdapter.getmList() != null && mAdapter.getmList().size() == 0) {//本地无数据
                mAdapter.showNoDataView();
                loading_layout.setVisibility(View.GONE);
                error_layout.setVisibility(View.GONE);
            } else {//本地有数据
                TopToastUtil.showTopToast(
                        recyclerView, String.format("暂时没有新的内容了…"));
            }
        }

    }

    public void goRefresh() {
        QHStatAgent.onEvent(getContext(), QHState.HOTVIDEO_REFRESH);
        queryDataFromNet();
    }

    public void insertMore() {
        Observable
                .create(new Observable.OnSubscribe<List<LiveHotItem>>() {
                    @Override
                    public void call(Subscriber<? super List<LiveHotItem>> subscriber) {
                        subscriber.onNext(getCacheData(mAdapter.getmList().size()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setLoaddingMoreViewAdapter, Throwable -> {
                    swipeLayout.setRefreshing(false);
                    if (loading_layout != null) {
                        loading_layout.setVisibility(View.GONE);
                    }
                    mAdapter.showErrorView();
                });
    }

    private void setLoaddingMoreViewAdapter(List<LiveHotItem> list) {
        swipeLayout.setRefreshing(false);


        if (list != null && list.size() > 0) {//数据库有数据
            mAdapter.insert(list, mAdapter.getmList().size(), false);
            mAdapter.notifyDataSetChanged();


        } else {//数据库无数据
            if (mAdapter.getmList() != null && mAdapter.getmList().size() == 0) {//列表无数据
                mAdapter.showNoDataView();
                loading_layout.setVisibility(View.GONE);
                error_layout.setVisibility(View.GONE);
            } else {//列表有数据
                mAdapter.showNoDataView();
            }
        }
    }

    public void setError_layoutShow() {
        error_layout.setVisibility(View.VISIBLE);
        loading_layout.setVisibility(View.GONE);
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Logger.i("LiveDiscoveryColumnFragment2   onCreateView");
//
//    }

    private void initEvent() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Observable.just(newState)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .filter(s -> s == RecyclerView.SCROLL_STATE_IDLE)
                        .doOnNext(s ->
                                filterHitlogData(getScreenData())
                                        .subscribe(s1 ->
                                                        HitLog.hitLogVideoShow(
                                                                HitLog.POSITION_LIVE_HOT, s1),
                                                Throwable::printStackTrace))

                        .subscribe(s -> {
                                    Logger.t("hitlog").d("视频列表显示打点");
                                },
                                Throwable::printStackTrace);
            }
        });


    }


    //获取屏幕中显示的数据
    private List<String> getScreenData() {
        //得到屏幕数据
        ArrayList<String> screenData = new ArrayList<>();
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        for (int i = first; i <= last && i < mAdapter.getmList().size(); i++) {//vid,news_from,news_type,is_focus
            screenData.add(mAdapter.getmList().get(i).getId() + "," + mAdapter.getmList().get(i).getNews_from() + "," + mAdapter.getmList().get(i).getNews_type() + "," + mAdapter.getmList().get(i).getIs_focus());
        }
        return screenData;
    }


    //筛选数据，去重
    private synchronized Observable<List<String>> filterHitlogData(List<String> data) {
        if (data == null || columnlist == null) return null;
        return Observable.just(data)
                .flatMap(Observable::from)
                .filter(s -> {
                    for (String str : columnlist) {
                        if (str.equals(s))
                            return false;
                    }
                    return true;
                })
                .toList()
                .filter(s -> s.size() > 0)
                .doOnNext(columnlist::addAll);
    }


    public void onEvent(LiveEvent.HotShareClick event) {
        if (event == null) {
            return;
        }
        if (!TextUtils.isEmpty(event.url)) {
            share_btn_Click(event.url, event.position);
        }

    }


    private void share_btn_Click(String url, int position) {

        LiveHotItem liveHotItem = (LiveHotItem) mAdapter.getmList().get(position);
        ShareInfo shareInfo = new ShareInfo(url, liveHotItem.getId(), liveHotItem.getVideo_name(), "", liveHotItem.getVideo_img(), null, ShareInfo.SHARECONTENT_LIVE);
        new ShareManager((Activity) getContext(), shareInfo, true,
                () -> HitLog.hitLogShare("null", "null", liveHotItem.getId(), liveHotItem.getNews_from())) //分享打点
                .show();
    }

    public void onEvent(LiveEvent.HotCommentUpdata event) {
        if (event != null) {
            //更新数据
            List<LiveHotItem> list = mAdapter.getmList();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(event.nid)) {
                    list.get(i).setComment("" + event.num);
                }
            }
            List<LiveHotItem> l = list;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
    }
}

