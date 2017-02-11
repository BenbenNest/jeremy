package com.jeremy.lychee.activity.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.user.WeMediaTransmitedChannelListAdapter;
import com.jeremy.lychee.model.news.TransmitedChannel;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.widget.UltimateRecyclerView;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeMediaTransmitedChannelListActivity extends SlidingActivity {
    private WeMediaTransmitedChannelListAdapter mAdapter;
    private List list;
    private boolean isLoadMore = false;
    private String nid = "";
    private String url = "";
    private final int limit = 10;//每页条数，默认为10
    private boolean isLoading = false;

    @Bind(R.id.recyclerView)
    UltimateRecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_transmited_channel_list);
        list = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            nid = intent.getStringExtra("nid");
            url = intent.getStringExtra("url");
        }
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);
        initView();
        insertTop();
    }

    private void requestAndShowData(long start_time) {
        isLoading = true;
        OldRetroAdapter.getService().getTransmitedChannelList(nid, url, start_time, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(e -> Observable.from(e.getData()))
                .doOnNext(e -> isLoading = false)
                .subscribe(this::updateViewAdapter,
                        Throwable::printStackTrace);
    }

    private void updateViewAdapter(TransmitedChannel channel) {
        if (isLoadMore) {
            mAdapter.insert(channel, mAdapter.getAdapterItemCount());
        } else {
            mAdapter.insert(channel, 0);
            recyclerView.scrollVerticallyToPosition(0);
        }

    }

    public void insertMore() {
        isLoadMore = true;
        int position = mAdapter.getAdapterItemCount() - 1;
        if (position >= 0) {
            requestAndShowData(mAdapter.getmList().get(position).getTimestamp());
        }
    }

    public void insertTop() {
        isLoadMore = false;
        requestAndShowData(System.currentTimeMillis()/1000L);
    }

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_white_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle("转推过的直播号");
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        //设置adapter
        mAdapter = new WeMediaTransmitedChannelListAdapter(this, list);
        recyclerView.setAdapter(mAdapter);

//        ItemTouchListenerAdapter itemTouchListenerAdapter = new ItemTouchListenerAdapter(recyclerView.mRecyclerView,
//                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
//                    @Override
//                    public void onItemClick(RecyclerView parent, View clickedView, int position) {
//                        WeMediaChannel channel = mAdapter.getmList().get(position).getChannelInfo();
//                        WeMediaChannelActivity.startActivity(
//                                WeMediaTransmitedChannelListActivity.this,
//                                channel.getId());
//                    }
//
//                    @Override
//                    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
//
//                    }
//                });
//        recyclerView.mRecyclerView.addOnItemTouchListener(itemTouchListenerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1) && mAdapter != null) {
                        //load more
                        insertMore();
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public static void startActivity(Context context, String nid, String url) {
        Intent intent = new Intent(context, WeMediaTransmitedChannelListActivity.class);
        intent.putExtra("nid", nid);
        intent.putExtra("url", url);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }
}
