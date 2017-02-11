package com.jeremy.lychee.activity.news;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.jeremy.lychee.activity.MainActivity;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.manager.ConfigManager;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.adapter.news.ChannelManagerAdapter;
import com.jeremy.lychee.eventbus.news.Events;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChannelManagerActivity extends SlidingActivity {

    public static final String SOURCE_EXTRA = "source";
    public static final String FROM_FIRST_SOURCE = "from_first_source";

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar toolbar;
    @Bind(com.jeremy.lychee.R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(com.jeremy.lychee.R.id.error_refresh)
    View errorLayout;
    @Bind(com.jeremy.lychee.R.id.loading_layout)
    View loadingLayout;
    @Bind(com.jeremy.lychee.R.id.top_img)
    ImageView topImg;

    private ChannelManagerAdapter adapter;
    private String source;
    private boolean topImgVisible = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QEventBus.getEventBus().unregister(this);
    }

    @Override
    protected void setContentView() {
        QEventBus.getEventBus().register(this);
        setContentView(com.jeremy.lychee.R.layout.activity_channel_manager);
        ButterKnife.bind(this);
        source = getIntent().getStringExtra(SOURCE_EXTRA);
        initToolbar();
        initRecyclerView();
        String channelCids = new NewsChannelPreference().getNewsChannelCids();
        if (TextUtils.isEmpty(channelCids)) {
            ConfigManager.getInstance().updateNewsChannel();
        } else {
            getOrderedChannelNum();
        }
        getChannelCategory();

    }

    private void getOrderedChannelNum() {
        Observable.just(new NewsChannelPreference().getNewsChannelCids())
                .map(s -> {
                    String[] strings = s.split("\\|");
                    return strings.length;
                })
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::setChannelNum);
    }

    private void getChannelCategory() {
        loadingLayout.setVisibility(View.VISIBLE);
        OldRetroAdapter.getService().getChannelCategory()
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .map(ModelBase::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(channelCategory -> {
                    loadingLayout.setVisibility(View.GONE);
                    adapter.setIntro(channelCategory.getStart());
                    adapter.setChannelInfoList(channelCategory.getList());
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    errorLayout.setVisibility(View.VISIBLE);
                    loadingLayout.setVisibility(View.GONE);
                });
    }

    private void initRecyclerView() {
        adapter = new ChannelManagerAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (layoutManager.findFirstVisibleItemPosition() >= 2
                        && !topImgVisible) {
                    topImgVisible = true;
                    topImg.setVisibility(View.VISIBLE);
                } else if (layoutManager.findFirstVisibleItemPosition() < 2
                        && topImgVisible) {
                    topImgVisible = false;
                    topImg.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setTitle("频道管理");
    }

    @OnClick(com.jeremy.lychee.R.id.error_refresh)
    public void clickErrorLayout(View view) {
        getChannelCategory();
    }

    public void onEventMainThread(Events.UpdateNewsChannelListEvent event) {
        if (event == null) {
            return;
        }
        getOrderedChannelNum();
        if (adapter != null) {
            adapter.notifyChannelCategoryLayout();
        }
    }

    @Override
    public void finish() {
        if (FROM_FIRST_SOURCE.equals(source)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            super.finish();
        } else {
            super.finish();
        }
    }

    @OnClick(com.jeremy.lychee.R.id.top_img)
    public void goToTop() {
        recyclerView.smoothScrollToPosition(0);
    }
}
