package com.jeremy.lychee.fragment.user;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jeremy.lychee.activity.live.LiveHistoryActivity;
import com.jeremy.lychee.activity.user.MessageActivity;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.model.user.UserMessage;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.ProgramPreference;
import com.jeremy.lychee.widget.LoadingRecyclerViewFooter;
import com.jeremy.lychee.adapter.user.UserMessageAdapter;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.preference.PreferenceKey;
import com.jeremy.lychee.widget.recyclerview.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class UserMessageFragment extends BaseFragment {

    PublishSubject<Boolean> loadDataSubject;
    @Bind(com.jeremy.lychee.R.id.ultimate_recycler_view)
    RecyclerViewWrapper recyclerview;

    @Bind(com.jeremy.lychee.R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;


    private ArrayList<UserMessage> dataList;
    private UserMessageAdapter adapter;
    private ViewGroup rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        loadDataSubject = PublishSubject.create();
        if (null == rootView) {
            rootView = (ViewGroup) inflater.inflate(
                    com.jeremy.lychee.R.layout.fragment_message, container, false);
        }
        ButterKnife.bind(this, rootView);
        initUI();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void initUI() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataList = new ArrayList<>();
        adapter = new UserMessageAdapter(dataList);
        recyclerview.setAdapter(adapter);
        recyclerview.enableLoadMore();
        adapter.setFooterStatus(LoadingRecyclerViewFooter.FooterStatus.loading);
        adapter.getFooterView().setFullText("没有更多数据");
        View playRecord = View.inflate(getContext(), com.jeremy.lychee.R.layout.header_play_history, null);
        recyclerview.setNormalHeader(playRecord);
        recyclerview.setOnLoadMoreListener((itemsCount, maxLastVisiblePosition) -> loadDataSubject.onNext(true));
        swipe_refresh.setOnRefreshListener(() -> loadDataSubject.onNext(false));
        RxView.clicks(playRecord)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(v -> ((MessageActivity) getActivity()).openActivity(LiveHistoryActivity.class))
                .subscribe();
        loadDataSubject.asObservable()
                .doOnNext(this::getUserMessage)
                .subscribe();
        loadDataSubject.onNext(false);
    }


    private void getUserMessage(boolean append) {
        if (append) {
            adapter.setFooterStatus(LoadingRecyclerViewFooter.FooterStatus.loading);
        }
        long latest_message_id = ProgramPreference.getInstance().getLongValue(PreferenceKey.LATEST_MESSAGE_ID);

        OldRetroAdapter.getService().getUserMessage(append && dataList.size() > 0 ? String.valueOf(dataList.get(dataList.size() - 1).id) : null, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelBase<List<UserMessage>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        swipe_refresh.setRefreshing(false);
                        adapter.setFooterStatus(LoadingRecyclerViewFooter.FooterStatus.error);
                        adapter.getFooterView().setOnClickListener(v -> getUserMessage(true));
                    }

                    @Override
                    public void onNext(ModelBase<List<UserMessage>> listModelBase) {
                        swipe_refresh.setRefreshing(false);
                        if (listModelBase.getErrno() == 0) {
                            List<UserMessage> list = listModelBase.getData();

                            if (list.size() < 20) {
                                recyclerview.setIsFullData(true);
                            }else{
                                recyclerview.setIsFullData(false);
                            }
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    UserMessage message = list.get(i);
                                    message.isUnRead = message.id > latest_message_id;
                                }
                            }
                            if (!append) {
                                dataList.clear();
                                //save latest message id
                                if (list.size() > 0) {
                                    ProgramPreference.getInstance().saveLongValue(PreferenceKey.LATEST_MESSAGE_ID, list.get(0).id);
                                }
                            }
                            dataList.addAll(list);
                            adapter.notifyDataSetChanged();
                        } else if (listModelBase.getErrno() == 2001) {
                            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public static void checkUnreadMessage(Action1<Boolean> action1) {
        long latest_message_id = ProgramPreference.getInstance().getLongValue(PreferenceKey.LATEST_MESSAGE_ID);
        OldRetroAdapter.getService().getUserMessage(null, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelBase<List<UserMessage>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ModelBase<List<UserMessage>> listModelBase) {
                        if (listModelBase.getErrno() == 0) {
                            List<UserMessage> list = listModelBase.getData();
                            boolean hasUnreadMessage = list != null && list.size() > 0 && list.get(0).id > latest_message_id;
                            action1.call(hasUnreadMessage);
                        }
                    }
                });
    }
}
