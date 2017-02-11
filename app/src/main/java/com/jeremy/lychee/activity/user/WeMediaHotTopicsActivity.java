package com.jeremy.lychee.activity.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jeremy.lychee.adapter.user.WeMediaTopicListViewAdapter;
import com.jeremy.lychee.db.WeMediaTopic;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeMediaHotTopicsActivity extends SlidingActivity {

    private WeMediaTopicListViewAdapter mAdapter;
    protected final static int REQUEST_SIZE = 10;//每页条数，默认为10

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar toolbar;

    @Bind(com.jeremy.lychee.R.id.hot_topic_list)
    ListView mListView;

    @Bind(com.jeremy.lychee.R.id.loading_layout)
    ViewGroup mLoadingView;

    @Bind(com.jeremy.lychee.R.id.error_refresh)
    ViewGroup mErrorRefresh;

    @Bind(com.jeremy.lychee.R.id.no_data)
    ViewGroup mNoData;

    @OnClick(com.jeremy.lychee.R.id.error_refresh)
    void onClickErrorLayout() {
        mAdapter.clearData();
        requestAndShowHotTopics(1);
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_hot_topic);
        ButterKnife.bind(this);
        QEventBus.getEventBus().register(this);
        initUI();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WeMediaHotTopicsActivity.class);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }


    @Override
    protected void onPostInflation() {
        super.onPostInflation();
    }

    private void requestAndShowHotTopics(int start) {
        getHotTopicList(start, REQUEST_SIZE)
                .subscribeOn(Schedulers.io())
                .doOnNext(s -> {
                    if (s.size() >= REQUEST_SIZE) {
                        //request continue
                        requestAndShowHotTopics(start + 1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> showLoadingView(false))
                .doOnNext(mAdapter::addAllDatas)
                .subscribe(s -> {
                            if (mAdapter.getData().size() == 0) {
                                showEmptyView(true);
                            } else {
                                mAdapter.notifyDataSetChanged();
                            }
                        },
                        e -> {
                            showErrorView(true);
                            e.printStackTrace();
                        });
    }

    private Observable<List<WeMediaTopic>> getHotTopicList(int start, int size) {
        return OldRetroAdapter.getService().getHotTopicList(start, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(respones -> Observable.just(respones.getData()));
    }

    private void initUI() {
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle("推荐话题");
        mAdapter = new WeMediaTopicListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        requestAndShowHotTopics(1);
    }

    private void showLoadingView(boolean val) {
        if (val && mLoadingView.getVisibility() != View.VISIBLE) {
            mLoadingView.setVisibility(View.VISIBLE);
        } else if (!val && mLoadingView.getVisibility() != View.GONE) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    private void showErrorView(boolean val) {
        if (val && !mErrorRefresh.isShown()) {
            mAdapter.clearData();
            showLoadingView(false);
            mErrorRefresh.setVisibility(View.VISIBLE);
        } else if (!val && mErrorRefresh.isShown()) {
            mErrorRefresh.setVisibility(View.GONE);
        }
    }

    private void showEmptyView(boolean val) {
        if (val && !mNoData.isShown()) {
            showLoadingView(false);
            showErrorView(false);
            mNoData.setVisibility(View.VISIBLE);
        } else if (!val && mNoData.isShown()) {
            mNoData.setVisibility(View.GONE);
        }
    }

    final public void onEventMainThread(Events.OnSubscribedTopicListUpdated event) {
        mAdapter.notifyDataSetChanged();
    }

    private WeMediaTopic findItemByChannelId(int id) {
        List<WeMediaTopic> list = mAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (id == mAdapter.getData().get(i).getId()) {
                return list.get(i);
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
    }

}
