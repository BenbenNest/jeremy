package com.jeremy.lychee.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.activity.user.WeMediaSubscribedTopicsActivity;
import com.jeremy.lychee.adapter.user.UserSubscribeRecyclerViewAdapter2;
import com.jeremy.lychee.customview.PullListRecyclerView;
import com.jeremy.lychee.widget.toptoast.TopToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserSubscribeFragment2 extends BaseFragment {

    private long mLastTimeStamp = -1;
    private static final long AUTO_RELOAD_DURATION = 15 * 60 * 1000;

    static private String TAG = UserSubscribeFragment2.class.toString();
    @Bind(com.jeremy.lychee.R.id.user_subscribe_recycler_view)
    PullListRecyclerView userSubcribeRecyclerView;

    private ViewGroup rootView;
    private UserSubscribeRecyclerViewAdapter2 mSubscribeRecyclerViewAdapter;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        updateList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (null == rootView) {
            rootView = (ViewGroup)inflater.inflate(
                    com.jeremy.lychee.R.layout.fragment_user_subscribe_view, container, false);
            ButterKnife.bind(this, rootView);
            initUI();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI() {
        if (mSubscribeRecyclerViewAdapter == null) {
            mSubscribeRecyclerViewAdapter = new UserSubscribeRecyclerViewAdapter2(getContext(), this);
            userSubcribeRecyclerView.setAdapter(mSubscribeRecyclerViewAdapter, false);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        new Handler(Looper.getMainLooper()).post(() -> {
//            if (getUserVisibleHint() && !mIgnore.get()) {
//                updateList();
//            }
//        });
//    }

    private void updateList() {
        if (null != rootView) {
            if (userSubcribeRecyclerView == null) {
                //java.lang.NullPointerException: Attempt to read from field
                //'boolean PullListRecyclerView.isLoading' on a null object reference
                userSubcribeRecyclerView =
                        (PullListRecyclerView) rootView.findViewById(com.jeremy.lychee.R.id.user_subscribe_recycler_view);
            }
            if (userSubcribeRecyclerView != null
                    && !userSubcribeRecyclerView.isLoading) {
                mSubscribeRecyclerViewAdapter.load(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    private Events.IgnoreRefreshNextTime.IsIgnore mIgnore =
//            new Events.IgnoreRefreshNextTime.IsIgnore(false);//初始化true:避免onResume执行两次造成的多余load
//    final public void onEventMainThread(Events.IgnoreRefreshNextTime event) {
//        mIgnore = event.mData;
//    }

    final public void onEventMainThread(Events.ShowSubTopics event) {
        WeMediaSubscribedTopicsActivity.startActivity(getContext());
    }

    final public void onEventMainThread(Events.OnSubscribedTopicListUpdated event) {
        mSubscribeRecyclerViewAdapter.load(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QEventBus.getEventBus().register(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        long cur = System.currentTimeMillis();
        if (!hidden && mLastTimeStamp < 0 || cur - mLastTimeStamp > AUTO_RELOAD_DURATION) {
            mLastTimeStamp = cur;
            updateList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QEventBus.getEventBus().unregister(this);
    }

    public void showSnackBar() {
//        SnackBarUtil.showSnackBar(rootView, () -> mSubscribeRecyclerViewAdapter.load(true));
        TopToastUtil.showTopToast(rootView, getResources().getString(com.jeremy.lychee.R.string.top_toast_net_error));
    }


}
