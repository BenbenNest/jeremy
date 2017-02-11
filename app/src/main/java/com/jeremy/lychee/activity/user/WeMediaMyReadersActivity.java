package com.jeremy.lychee.activity.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.jeremy.lychee.adapter.user.WeMediaChannelReaderAdapter;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.customview.PullListRecyclerView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeMediaMyReadersActivity extends SlidingActivity {

    public static final String CID = "CID";

    @Bind(com.jeremy.lychee.R.id.recyclerView)
    PullListRecyclerView mRecyclerView;

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar toolbar;

    protected WeMediaChannelReaderAdapter mAdapter;

    public static void startActivity(Context context, String cid) {
        Intent intent = new Intent(context, WeMediaMyReadersActivity.class);
        intent.putExtra(CID, cid);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_wemedia_readers);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        mAdapter = new WeMediaChannelReaderAdapter(this, getIntent().getStringExtra(CID));
        mRecyclerView.setAdapter(mAdapter, true);
        mAdapter.setDataState(BaseRecyclerViewAdapter.DataState.LOADING);
        initToolbar();
    }

    private void initToolbar(){
        if (toolbar != null) {
            toolbar.setTitle("读者");
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
