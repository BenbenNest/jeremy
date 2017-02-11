package com.jeremy.lychee.activity.news;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.news.SubChannelListAdapter;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SubChannelListActivity extends SlidingActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.error_refresh)
    View errorLayout;
    @Bind(R.id.loading_layout)
    View loadingLayout;

    private String cid;
    private String title;
    private SubChannelListAdapter adapter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        QEventBus.getEventBus().unregister(this);
    }

    @Override
    protected void setContentView() {
        QEventBus.getEventBus().register(this);
        setContentView(R.layout.activity_sub_channel_list);
        ButterKnife.bind(this);
        cid = getIntent().getStringExtra("cid");
        title = getIntent().getStringExtra("title");
        initToolbar();
        getRecommendChannel();
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setTitle(title);

    }

    private void getRecommendChannel() {
        loadingLayout.setVisibility(View.VISIBLE);
        OldRetroAdapter.getService().getSubChannel(cid)
                .subscribeOn(Schedulers.io())
                .map(ModelBase::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsChannelList -> {
                    loadingLayout.setVisibility(View.GONE);
                    adapter = new SubChannelListAdapter(this, newsChannelList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                }, throwable -> {
                    errorLayout.setVisibility(View.VISIBLE);
                    loadingLayout.setVisibility(View.GONE);
                });
    }

    public void onEventMainThread(Events.UpdateNewsChannelListEvent event) {
        if (adapter == null) {
            return;
        }
        adapter.updateCids();
    }
}
