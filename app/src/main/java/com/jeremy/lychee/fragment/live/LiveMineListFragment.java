package com.jeremy.lychee.fragment.live;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremy.lychee.adapter.live.LiveVideoHistoryListAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.live.LiveBrowseHistoryManager;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.utils.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LiveMineListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_TAG = "ARG_TAG";
    //浏览历史
    public static final String BROWSE_TAG = "BROWSE";
    //上传历史
    public static final String UPLOAD_TAG = "UPLOAD";
    //直播历史
    public static final String RECORD_TAG = "RECORD";

    private String tag;

    @Bind(com.jeremy.lychee.R.id.list)
    RecyclerView mRecyclerView;

    @Bind(com.jeremy.lychee.R.id.pull_refresh_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(com.jeremy.lychee.R.id.loading_layout)
    View loading_layout;

    @Bind(com.jeremy.lychee.R.id.error_layout)
    View error_layout;

    @Bind(com.jeremy.lychee.R.id.nodata_lay)
    View nodata_lay;

    @Bind(com.jeremy.lychee.R.id.nodata_tv)
    TextView nodata_tv;

    public static LiveMineListFragment newInstance(String arg) {
        LiveMineListFragment fragment = new LiveMineListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAG, arg);
        fragment.setArguments(args);
        return fragment;
    }

    public LiveMineListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag = getArguments().getString(ARG_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.jeremy.lychee.R.layout.fragment_video_item_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(com.jeremy.lychee.R.color.swipe_refresh_color);
        nodata_lay.setOnClickListener(v -> {
            nodata_lay.setVisibility(View.GONE);
            error_layout.setVisibility(View.GONE);
             getData();
        });

        nodata_lay.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
        loading_layout.setVisibility(View.VISIBLE);
        getData();

        error_layout.setOnClickListener(v -> {
            getData();
            loading_layout.setVisibility(View.VISIBLE);
            error_layout.setVisibility(View.GONE);
            nodata_lay.setVisibility(View.GONE);
        });

        int txtRes = com.jeremy.lychee.R.string.nodata_browse;
        switch (tag){
            case BROWSE_TAG:
                //浏览历史本地获取
                txtRes = com.jeremy.lychee.R.string.nodata_browse;
                break;
            case UPLOAD_TAG:
                txtRes = com.jeremy.lychee.R.string.nodata_upload;
                break;
            case RECORD_TAG:
                txtRes = com.jeremy.lychee.R.string.nodata_record;
                break;
        }
        nodata_tv.setText(txtRes);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void getData(){
        switch (tag){
            case BROWSE_TAG:
                //浏览历史本地获取
                getBrowseHistoryLives();
                break;
            default:
                if(mSwipeRefreshLayout!=null){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                break;
        }
    }

    public void onEvent(Events.Logout event) {
        //退出登录
        Logger.e("LiveMineListFragment onEvent Logout");
    }

    public void onEvent(Events.LoginOk event) {
        Logger.e("LiveMineListFragment onEvent LoginOk");
        //登录成功 刷新数据
        nodata_lay.setVisibility(View.GONE);
        error_layout.setVisibility(View.GONE);
        loading_layout.setVisibility(View.VISIBLE);
        getData();
    }

    /**
     * 获取浏览历史list数据
     */
    private void getBrowseHistoryLives(){
        loading_layout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        List<LiveVideoInfo> list = LiveBrowseHistoryManager.getInstance().getBrowseVideoList();
        mRecyclerView.setAdapter(new LiveVideoHistoryListAdapter(getContext(), list, tag));
        if (list == null||list.size()==0) {
            //TODO 数据为空
            nodata_lay.setVisibility(View.VISIBLE);
        } else {
            nodata_lay.setVisibility(View.GONE);
        }
    }

    /**
     * SwipeRefreshLayout onRefresh
     */
    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Logger.e("LiveMineListFragment setUserVisibleHint isVisibleToUser:" + isVisibleToUser + "    tag: " + tag);
        if(isVisibleToUser){
            getData();
        }
    }



}
