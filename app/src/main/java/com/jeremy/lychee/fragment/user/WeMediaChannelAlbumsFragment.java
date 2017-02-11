package com.jeremy.lychee.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeremy.lychee.adapter.user.WeMediaChannelAlbumListAdapter;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.widget.LayoutManager.WrapContentLinearLayoutManager;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.activity.user.WeMediaCreateAlbumActivity;
import com.jeremy.lychee.net.OldRetroAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeMediaChannelAlbumsFragment extends BaseFragment {
    @Bind(com.jeremy.lychee.R.id.album_list)
    RecyclerView mRecyclerView;

    @Bind(com.jeremy.lychee.R.id.create_new_album_btn)
    ViewGroup mCreateNewAlbumBtn;

    @Bind(com.jeremy.lychee.R.id.txt_create_channel)
    View mCreateChannelTxt;

    @Bind(com.jeremy.lychee.R.id.loading_layout)
    ViewGroup mLoadingView;

    @Bind(com.jeremy.lychee.R.id.error_refresh)
    ViewGroup mErrorRefresh;

    @Bind(com.jeremy.lychee.R.id.empty_layout)
    ViewGroup mEmptyView;

    protected WeMediaChannelAlbumListAdapter mAdapter;
    protected final static int REQUEST_SIZE = 10;
    protected String mUserId = "";
    private boolean mIsMy = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.jeremy.lychee.R.layout.fragment_wemedia_channel_album, container, false);
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
        try {
            WeMediaChannel info = ((WeMediaChannelActivity) getActivity()).getWeMediaInfo();
            mIsMy = info.getIs_my();
            mUserId = info.getUid();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        initAddChannelBtn();
        mAdapter = new WeMediaChannelAlbumListAdapter(getContext());
        mRecyclerView.setLayoutManager(
                new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);

        requestAndShowChannelData();

    }

    @OnClick(com.jeremy.lychee.R.id.error_refresh)
    void onClickErrorLayout(){
        requestAndShowChannelData();
    }

    protected void showLoadingView(boolean val) {
        if (val &&  mLoadingView.getVisibility() != View.VISIBLE) {
            mLoadingView.setVisibility(View.VISIBLE);
        } else if(!val && mLoadingView.getVisibility() != View.GONE){
            mLoadingView.setVisibility(View.GONE);
        }
    }

    protected void showErrorView(boolean val) {
        if (val && mErrorRefresh.getVisibility() != View.VISIBLE) {
            mAdapter.clearData();
            showLoadingView(false);
            mErrorRefresh.setVisibility(View.VISIBLE);
        } else if(!val && mErrorRefresh.getVisibility() != View.GONE){
            mErrorRefresh.setVisibility(View.GONE);
        }
    }

    protected void showEmptyView(boolean val) {
        if (val && mEmptyView.getVisibility() != View.VISIBLE) {
            showLoadingView(false);
            mEmptyView.setVisibility(View.VISIBLE);
        } else if(!val && mEmptyView.getVisibility() != View.GONE){
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void initAddChannelBtn(){
        if(!mIsMy) return;
        mCreateNewAlbumBtn.setVisibility(View.VISIBLE);
        mCreateNewAlbumBtn.findViewById(com.jeremy.lychee.R.id.tv_create_new_album).setVisibility(View.VISIBLE);
        ImageView iv = (ImageView) mCreateNewAlbumBtn.findViewById(com.jeremy.lychee.R.id.album_icon);
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(com.jeremy.lychee.R.mipmap.ic_add_channel_btn);
        mCreateNewAlbumBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), WeMediaCreateAlbumActivity.class);
            startActivity(intent);
        });
    }

    public WeMediaChannelAlbumsFragment() {
        super();
        QEventBus.getEventBus().registerSticky(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
    }

    final public void onEventMainThread(Events.OnAlbumListUpdated event) {
        getView().postDelayed(() -> {//TODO:专辑内容变更后信息更新不实时，增加延时，此延时不安全，以后解决
            if (event.mData != null && event.mData instanceof Integer) {
                upadteAlbumInfo(Integer.toString((Integer) event.mData));
            } else {
                requestAndShowChannelData();
            }
        },200);

    }

    private void requestMyCreateMediaChannelList(final int start){
        getMyCreatedAlbumList(start)
                .observeOn(Schedulers.io())
                .doOnNext(s -> {
                    if (s.size() >= REQUEST_SIZE) {
                        //request continue
                        requestMyCreateMediaChannelList(start + 1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> {
                    if (s.size() > 0) {
                        mAdapter.notifyDataSetChanged();//notifyItemInserted之前必须调用一次
                        if (mIsMy) {
                            mCreateChannelTxt.setVisibility(View.VISIBLE);
                            showEmptyView(false);
                        }
                    } else if (mAdapter.getData().size() == 0) {
                        showEmptyView(true);
                    }
                })
                .flatMap(Observable::from)
                .subscribe(
                        s -> {
                            mAdapter.addData(s);
                            mAdapter.notifyItemInserted(mAdapter.getData().indexOf(s));
                            showEmptyView(false);
                            showLoadingView(false);

                        },
                        e -> {
                            e.printStackTrace();
                            showErrorView(true);
                        });
    }

    protected void requestAndShowChannelData(){
        showLoadingView(true);
        showErrorView(false);
        mCreateChannelTxt.setVisibility(View.GONE);
        //Data request
        if(mAdapter != null){
            mAdapter.clearData();
            requestMyCreateMediaChannelList(1);
        }

    }

    private void upadteAlbumInfo(String albumId){
        if(mAdapter != null){
            mAdapter.clearData();
        }
        requestAndRefreshAlbumInfo(1, albumId);
    }

    private int findItemIdexByAlbumId(String id) {
        if (mAdapter == null) return -1;
        List<WeMediaChannel> list = mAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private void requestAndRefreshAlbumInfo(int start, String albumId){
        getMyCreatedAlbumList(start)
                .observeOn(Schedulers.io())
                .doOnNext(s -> {
                    if (s.size() >= REQUEST_SIZE) {
                        //request continue
                        requestAndRefreshAlbumInfo(start + 1, albumId);
                    }
                    mAdapter.addAllDatas(s);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> {
                            if (s.size() < REQUEST_SIZE) {
                                int position = findItemIdexByAlbumId(albumId);
                                if (position != -1) {
                                    mAdapter.notifyItemChanged(position);
                                }
                            }
                        },
                        Throwable::printStackTrace);
    }

    private Observable<List<WeMediaChannel>> getMyCreatedAlbumList(int start) {
        return OldRetroAdapter.getService().getUserCreateMediaChannelList(start, REQUEST_SIZE, mUserId)
                .map(ModelBase::getData)
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle());
    }

}
