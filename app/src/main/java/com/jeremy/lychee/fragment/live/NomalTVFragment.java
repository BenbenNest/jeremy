package com.jeremy.lychee.fragment.live;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;

import com.jeremy.lychee.adapter.live.LiveNomalTVAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.db.LiveHotItem;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.model.live.IsPlayingLive;
import com.jeremy.lychee.videoplayer.VideoPlayerPanelBasic;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.videoplayer.VideoPlayerView;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.MySwipeRefreshLayout;
import com.jeremy.lychee.videoplayer.VideoPlayer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daibangwen-xy on 16/3/2.
 */
public class NomalTVFragment extends BaseFragment {
    private View mContentView;
    private RecyclerView recyclerView;
    private View errorLayout;
    private View loadingLayout;
    private LiveNomalTVAdapter mdapter;
    private IsPlayingLive isPlayingLive;
    private int count = 0;
    private int page = 1;
    private int limit = 10;
    private List<LiveHotItem> backwatchList;
    private String channelId = "";
    private MySwipeRefreshLayout swipeLayout;

    private VideoPlayerView playing_player_view;
    private GlideImageView playing_video_img;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView != null) {
            ViewParent mParent = mContentView.getParent();
            if (mParent != null && mParent instanceof ViewGroup) {
                ((ViewGroup) mParent).removeView(mContentView);
            }
            return mContentView;
        }
        mContentView = inflater.inflate(com.jeremy.lychee.R.layout.fragment_nomaltv_layout, container, false);
        recyclerView = (RecyclerView) mContentView.findViewById(com.jeremy.lychee.R.id.recyclerView);
        playing_player_view = (VideoPlayerView) mContentView.findViewById(com.jeremy.lychee.R.id.playing_player_view);
        playing_player_view.addControlPanel(new VideoPlayerPanelBasic());
        playing_video_img = (GlideImageView) mContentView.findViewById(com.jeremy.lychee.R.id.playing_video_img);
        playing_video_img.setOnClickListener(v -> {
            playing_player_view.setPlaceHolderDrawable(playing_video_img.getDrawable());
            playing_player_view.bindToVideoPlayer();
            VideoPlayer.getInstance().load((RxAppCompatActivity) getContext(), isPlayingLive.getId(), isPlayingLive.getVideo_type(), isPlayingLive.getTag());
        });
        swipeLayout = (MySwipeRefreshLayout) mContentView.findViewById(com.jeremy.lychee.R.id.pull_refresh_view);
        swipeLayout.setEnabled(false);
        loadingLayout = mContentView.findViewById(com.jeremy.lychee.R.id.loading_layout);
        errorLayout = mContentView.findViewById(com.jeremy.lychee.R.id.error_layout);
        errorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingLayout.setVisibility(View.VISIBLE);
                getFirstData(channelId);
            }
        });
        View footer = View.inflate(getContext(), com.jeremy.lychee.R.layout.custom_bottom_bar, null);

        mdapter = new LiveNomalTVAdapter(getContext(), false, true, null, footer);
        QEventBus.getEventBus().register(mdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
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
            }
        });

        QEventBus.getEventBus().register(this);
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getdata();
        page = 1;
        channelId = (String) getArguments().get("channel_id");
        getFirstData(channelId);
        mdapter.channelId = channelId;
    }

    public void loadMore() {
        getLoadMoreData(channelId);
    }

    public void getLoadMoreData(String channelId) {
        page++;
        OldRetroAdapter.getService().getchannelreplay(channelId, page + "", limit + "")
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase<List<LiveHotItem>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        page--;
                        mdapter.showLoadError();
                    }

                    @Override
                    public void onNext(ModelBase<List<LiveHotItem>> listModelBase) {
                        if (listModelBase.getErrno() == 0) {
                            if (listModelBase.getData() != null && listModelBase.getData().size() > 0) {
                                mdapter.addBackWatch(listModelBase.getData());
                                mdapter.notifyDataSetChanged();
                            } else if (listModelBase.getData() != null && listModelBase.getData().size() == 0) {
                                mdapter.showLoadOver();
                            } else {
                                mdapter.showLoadError();
                            }

                        } else {
                            mdapter.showLoadError();
                        }

                    }
                });
    }

    public void getFirstData(String channelId) {
        page = 1;
        errorLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        count = 0;
        Observable<Object> a = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                OldRetroAdapter.getService().getIsPlayingLive(channelId).subscribe(new Observer<ModelBase<IsPlayingLive>>() {
                    @Override
                    public void onCompleted() {
                        count++;
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(ModelBase<IsPlayingLive> isPlayingLiveModelBase) {
                        isPlayingLive = isPlayingLiveModelBase.getData();
                    }
                });
            }
        });
        Observable<Object> b = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                OldRetroAdapter.getService().getchannelreplay(channelId, 1 + "", limit + "").subscribe(new Subscriber<ModelBase<List<LiveHotItem>>>() {
                    @Override
                    public void onCompleted() {
                        count++;
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(ModelBase<List<LiveHotItem>> listModelBase) {
                        if (listModelBase.getErrno() == 0) {
                            backwatchList = listModelBase.getData();
                        }

                    }
                });
            }
        });
        Observable.merge(a.subscribeOn(Schedulers.io()), b.subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                errorLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
                List<IsPlayingLive> isPlayingLives = new ArrayList<IsPlayingLive>();
                isPlayingLives.add(isPlayingLive);
                playing_video_img.loadImage(isPlayingLive.getVideo_img());
                mdapter.setAllList(isPlayingLives, backwatchList);
                if (mdapter.getAllList().size() < 2) {
                    mdapter.showLoadOver();
                }
                mdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                errorLayout.setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNext(Object o) {

            }
        });


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
