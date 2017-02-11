package com.jeremy.lychee.activity.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jeremy.lychee.adapter.user.WeMediaChannelListViewAdapter;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.model.ModelBase;
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

public class WeMediaSubscribedChannelsActivity extends SlidingActivity {

    private WeMediaChannelListViewAdapter mAdapter;
    private static final String UID = "uid";
    protected final static int REQUEST_SIZE = 10;//每页条数，默认为10

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar toolbar;

    @Bind(com.jeremy.lychee.R.id.sub_channel_list)
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
        requestSubMediaChannelList(1, getIntent().getStringExtra(UID));
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_subscribed_media_channel);
        ButterKnife.bind(this);
        QEventBus.getEventBus().register(this);
        initUI();
    }

    public static void startActivity(Context context, String uid) {
        Intent intent = new Intent(context, WeMediaSubscribedChannelsActivity.class);
        intent.putExtra(UID, uid);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }


    @Override
    protected void onPostInflation() {
        super.onPostInflation();
    }

    private void requestSubMediaChannelList(final int start, String uid) {
        getMySubMediaChannelList(start, uid)
                .observeOn(Schedulers.io())
                .doOnNext(s -> {
                    if (s.size() >= REQUEST_SIZE) {
                        //request continue
                        requestSubMediaChannelList(start + 1, uid);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> showLoadingView(false))
                .doOnNext(s -> showEmptyView(true))
                .flatMap(Observable::from)
                .doOnNext(mAdapter::addData)
                .subscribe(s -> {
                            if (mAdapter.getData().size() > 0) {
                                showEmptyView(false);
                                mAdapter.notifyDataSetChanged();
                            }
                        },
                        e -> {
                            showErrorView(true);
                            e.printStackTrace();
                        });
    }

    private Observable<List<WeMediaChannel>> getMySubMediaChannelList(int start, String uid) {
        return OldRetroAdapter.getService().getUserSubMediaChannelList(start, REQUEST_SIZE, uid)
                .map(ModelBase::getData)
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle());
    }

    private void initUI() {
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle("订阅的直播号");
        mAdapter = new WeMediaChannelListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        requestSubMediaChannelList(1, getIntent().getStringExtra(UID));
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

    final public void onEventMainThread(Events.OnSubscribedChannelUpdated event) {
        WeMediaChannel item = findItemByChannelId(event.channelId);
        if (item != null) {
            item.setIs_sub(event.sub);
            mAdapter.notifyDataSetChanged();
        }
    }

    private WeMediaChannel findItemByChannelId(int id) {
        List<WeMediaChannel> list = mAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (id == Integer.parseInt(mAdapter.getData().get(i).getC_id())) {
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
