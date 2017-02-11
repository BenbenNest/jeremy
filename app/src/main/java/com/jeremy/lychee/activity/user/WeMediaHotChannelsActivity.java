package com.jeremy.lychee.activity.user;

import android.support.v7.widget.Toolbar;

import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.user.WeMediaHotChannelListAdapter;
import com.jeremy.lychee.customview.PullGridRecyclerView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

@Deprecated
public class WeMediaHotChannelsActivity extends SlidingActivity {

    @Bind(R.id.hot_channel_recycler_view)
    PullGridRecyclerView hotChannelRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private WeMediaHotChannelListAdapter mWeMediaHotChannelListAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_wemedia_hot_channels);
        ButterKnife.bind(this);
        initToolbar();
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        if (mWeMediaHotChannelListAdapter == null) {
            mWeMediaHotChannelListAdapter = new WeMediaHotChannelListAdapter(this);
            hotChannelRecyclerView.setAdapter(mWeMediaHotChannelListAdapter, false);
            hotChannelRecyclerView.loadData();
        }
    }

    private void initToolbar(){
        if (toolbar != null) {
            toolbar.setTitle("热门直播号");
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

    }
}
