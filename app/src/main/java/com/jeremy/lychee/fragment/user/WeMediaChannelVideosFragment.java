package com.jeremy.lychee.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.adapter.user.WeMediaChannelVideoListAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.customview.PullListRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeMediaChannelVideosFragment extends BaseFragment {

    @Bind(com.jeremy.lychee.R.id.article_recycler_view)
    PullListRecyclerView mRecyclerView;

    protected WeMediaChannelVideoListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.jeremy.lychee.R.layout.fragment_wemedia_channel_video, null, false);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mAdapter = new WeMediaChannelVideoListAdapter(getContext(),
                ((WeMediaChannelActivity) getActivity()).getWeMediaInfo().getUid());
        mRecyclerView.setAdapter(mAdapter, true);
        mAdapter.setDataState(BaseRecyclerViewAdapter.DataState.LOADING);
    }


}
