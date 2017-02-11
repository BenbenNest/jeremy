package com.jeremy.lychee.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.adapter.user.WeMediaChannelArticleFeedListAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.customview.PullListRecyclerView;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangp on 16-1-13.
 */
public class WeMediaChannelArticlesFragment extends BaseFragment {

    @Bind(com.jeremy.lychee.R.id.article_recycler_view)
    PullListRecyclerView mRecyclerView;

    protected WeMediaChannelArticleFeedListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.jeremy.lychee.R.layout.fragment_wemedia_channel_article, null, false);
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
        WeMediaChannel info = ((WeMediaChannelActivity) getActivity()).getWeMediaInfo();
        if(info != null){
            mAdapter = new WeMediaChannelArticleFeedListAdapter(
                    getContext(),
                    info.getC_id());
            mRecyclerView.setAdapter(mAdapter, true);
            mAdapter.setDataState(BaseRecyclerViewAdapter.DataState.LOADING);
        }

    }

}
