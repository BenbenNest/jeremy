package com.jeremy.lychee.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.adapter.user.WeMediaChannelReaderAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.base.BaseRecyclerViewAdapter;
import com.jeremy.lychee.customview.PullListRecyclerView;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangp on 16-1-13.
 */
@Deprecated
public class WeMediaChannelReadersFragment extends BaseFragment {

    @Bind(com.jeremy.lychee.R.id.recyclerView)
    PullListRecyclerView mRecyclerView;

    protected WeMediaChannelReaderAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.jeremy.lychee.R.layout.activity_wemedia_readers, null, false);
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
        mAdapter = new WeMediaChannelReaderAdapter(getContext(),
                ((WeMediaChannelActivity) getActivity()).getWeMediaInfo().getC_id());
        mRecyclerView.setAdapter(mAdapter, true);
        mAdapter.setDataState(BaseRecyclerViewAdapter.DataState.LOADING);
    }


}
