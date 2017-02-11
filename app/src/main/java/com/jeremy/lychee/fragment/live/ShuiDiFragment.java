package com.jeremy.lychee.fragment.live;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.lychee.adapter.live.LiveShuiDiAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.live.ShuiDiVideo;
import com.jeremy.lychee.widget.toptoast.TopToastUtil;
import com.jeremy.lychee.model.live.LanMu;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.MySwipeRefreshLayout;
import com.jeremy.lychee.widget.RecyclerViewDecoration.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daibangwen-xy on 16/3/8.
 */
public class ShuiDiFragment extends BaseFragment {
    private View mContentView;
    private View errorLayout;
    private View loadingLayout;
    private RecyclerView recyclerView;
    private LiveShuiDiAdapter mdapter;
    private int page = 1;
    private int limit = 10;
    private List<ShuiDiVideo> shuiDiVideoList;
    private MySwipeRefreshLayout swipeLayout;
    private String channelId = "";
    private View player_layout;
    private List<LanMu> lanMus;
    private HorizontalScrollView lanmuscroll;
    private LinearLayout textparent;
    private String current_lid="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView != null) {
            ViewParent mParent = mContentView.getParent();
            if (mParent != null && mParent instanceof ViewGroup) {
                ((ViewGroup) mParent).removeView(mContentView);
            }
            return mContentView;
        }
        mContentView = inflater.inflate(com.jeremy.lychee.R.layout.fragment_shuidi_layout, container, false);
        lanmuscroll = (HorizontalScrollView) mContentView.findViewById(com.jeremy.lychee.R.id.lanmuscroll);
        textparent = (LinearLayout) mContentView.findViewById(com.jeremy.lychee.R.id.textparent);
        loadingLayout = mContentView.findViewById(com.jeremy.lychee.R.id.loading_layout);
        errorLayout = mContentView.findViewById(com.jeremy.lychee.R.id.error_layout);
        errorLayout.setOnClickListener(v1 -> {
            page = 1;
            getShuidiData(page, limit,current_lid, 0);
        });
        shuiDiVideoList = new ArrayList<>();
        recyclerView = (RecyclerView) mContentView.findViewById(com.jeremy.lychee.R.id.recyclerView);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, AppUtil.dip2px(getContext(), 2), AppUtil.dip2px(getContext(), 2), true));
        swipeLayout = (MySwipeRefreshLayout) mContentView.findViewById(com.jeremy.lychee.R.id.pull_refresh_view);
        swipeLayout.setEnabled(false);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShuidiData( 1, limit,current_lid, 1);
            }
        });
        View footer = View.inflate(getContext(), com.jeremy.lychee.R.layout.custom_bottom_bar, null);
        mdapter = new LiveShuiDiAdapter(getContext(), false, true, null, footer);
        QEventBus.getEventBus().register(mdapter);
        mdapter.setShuidiVideos(shuiDiVideoList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mdapter.getShuidiVideos().size()) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        View loadover = footer.findViewById(com.jeremy.lychee.R.id.load_no_data);
        loadover.setOnClickListener(v -> {
            layoutManager.scrollToPosition(0);
        });
        View loaderror = footer.findViewById(com.jeremy.lychee.R.id.load_error);
        loaderror.setOnClickListener(v -> {
            mdapter.showLoadMore();
            loadMore();
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        int lastPosition = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1));
                        boolean mLastItemVisible = (mdapter.getItemCount() > 0) && lastPosition >= mdapter.getItemCount() - 1;
                        if (mLastItemVisible && !mdapter.isLoadOver()) {
                            loadMore();
                        }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //determinMiniPlayer();
            }
        });
        QEventBus.getEventBus().register(this);
        return mContentView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        channelId = (String) getArguments().get("channel_id");
        lanMus = (List<LanMu>) getArguments().get("lanmu");
        if (lanMus!=null){
            for (int i = 0; i < lanMus.size(); i++) {
                View parentView = View.inflate(getContext(), com.jeremy.lychee.R.layout.shuidi_lanmuitem_layout, null);
                TextView textView = (TextView) parentView.findViewById(com.jeremy.lychee.R.id.name);
                textView.setText("" + lanMus.get(i).getName());
                if (i == 0) {
                    textView.setTextColor(getResources().getColor(com.jeremy.lychee.R.color.livechannel_name_color));
                    current_lid= lanMus.get(0).getId();
                }
                textView.setTag(lanMus.get(i).getId());
                textView.setOnClickListener(listener);
                textparent.addView(parentView);
            }
        }
        getShuidiData( page, limit,current_lid, 0);
        mdapter.channelId = channelId;
    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < textparent.getChildCount(); i++) {
                TextView textview = (TextView) textparent.getChildAt(i).findViewById(com.jeremy.lychee.R.id.name);
                textview.setTextColor(getResources().getColor(com.jeremy.lychee.R.color.shuidi_lanmu_unclick));
            }
            TextView textView = (TextView) v;
            current_lid = (String) textView.getTag();
            textView.setTextColor(getResources().getColor(com.jeremy.lychee.R.color.livechannel_name_color));
            getShuidiData(1, limit, current_lid, 0);

        }
    };

    //type 0 第一次加载   1 下拉刷新   2 上拉加载
    private void getShuidiData( int pageNum, int limit, String lid,int type) {
        if (type == 0) {
            shuiDiVideoList.clear();
            loadingLayout.setVisibility(View.VISIBLE);
        }
        errorLayout.setVisibility(View.GONE);
        OldRetroAdapter.getService().getShuiDiList( pageNum, limit,lid)
                .subscribeOn(Schedulers.io())
               /* .observeOn(Schedulers.io())*/
              /*  .doOnNext(data -> cacheData(data))*/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase<List<ShuiDiVideo>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (type == 0) {
                            loadingLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.VISIBLE);
                        } else if (type == 1) {
                            swipeLayout.setRefreshing(false);
                            TopToastUtil.showTopToast(
                                    recyclerView, String.format("网络不给力，请检查网络刷新重试"));
                            loadingLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.GONE);
                        } else {
                            mdapter.showLoadError();
                        }
                    }

                    @Override
                    public void onNext(ModelBase<List<ShuiDiVideo>> listModelBase) {
                        if (type == 0) {
                            if (listModelBase.getErrno() == 0 && listModelBase.getData() != null && listModelBase.getData().size() > 0) {
                                page++;
                                shuiDiVideoList.addAll(listModelBase.getData());
                                mdapter.setShuidiVideos(shuiDiVideoList);
                                mdapter.notifyDataSetChanged();
                                if (mdapter.getShuidiVideos().size()<limit){
                                    mdapter.showLoadOver();
                                }
                                loadingLayout.setVisibility(View.GONE);
                                errorLayout.setVisibility(View.GONE);
                            } else {
                                loadingLayout.setVisibility(View.GONE);
                                errorLayout.setVisibility(View.VISIBLE);
                            }
                        } else if (type == 1) {//下拉刷新
                            if (listModelBase.getErrno() == 0 && listModelBase.getData() != null) {
                                swipeLayout.setRefreshing(false);
                                //1.去除重复的
                                int count = mdapter.getShuidiVideos().size() > 10 ? 11 : mdapter.getShuidiVideos().size();
                                for (int i = 0; i < listModelBase.getData().size(); i++) {
                                    for (int j = 0; j < count; j++) {
                                        if (listModelBase.getData().get(i).getId().equals(mdapter.getShuidiVideos().get(j).getId())) {
                                            listModelBase.getData().remove(i);
                                        }
                                    }
                                }
                                //2 插入顶部
                                if (listModelBase.getData().size() > 0) {
                                    shuiDiVideoList.addAll(0, listModelBase.getData());
                                    mdapter.notifyDataSetChanged();
                                } else {
                                    TopToastUtil.showTopToast(recyclerView, String.format("暂时没有更新，请等会儿再试试"));
                                }

                                loadingLayout.setVisibility(View.GONE);
                                errorLayout.setVisibility(View.GONE);
                            } else {
                                TopToastUtil.showTopToast(recyclerView, String.format("网络不给力，请检查网络刷新重试"));
                            }
                        } else { //上拉加载
                            if (listModelBase.getErrno() == 0 && listModelBase.getData() != null) {
                                page++;
                                if (listModelBase.getData().size() > 0) {
                                    shuiDiVideoList.addAll(listModelBase.getData());
                                    mdapter.notifyDataSetChanged();
                                    loadingLayout.setVisibility(View.GONE);
                                    errorLayout.setVisibility(View.GONE);
                                } else {
                                    mdapter.showLoadOver();
                                }
                            } else {
                                mdapter.showLoadError();
                            }
                        }
                    }
                });
    }

    //刷新
    private void reflash() {
        page = 1;
        getShuidiData( page, limit, current_lid,1);
    }

    //加载
    private void loadMore() {
        getShuidiData( page, limit,current_lid, 2);
    }

    @Override
    public void onDestroy() {
        QEventBus.getEventBus().unregister(this);
        if (mdapter != null) {
            QEventBus.getEventBus().unregister(mdapter);
        }
        super.onDestroy();
    }
}
