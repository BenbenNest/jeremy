package com.jeremy.lychee.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.customview.user.UserView;


public class UserFragment extends BaseFragment {
    private UserView mUserView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        QEventBus.getEventBus().register(this);

        mUserView = new UserView(getContext(), this);
        QEventBus.getEventBus().register(mUserView);
        return mUserView;
    }

    public UserFragment() {
        super();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            mUserView.post(mUserView::onShown);
//        }
    }

    @Override
    public void onDestroy() {
        QEventBus.getEventBus().unregister(this);
        if (mUserView != null) {
            QEventBus.getEventBus().unregister(mUserView);
        }
        super.onDestroy();
    }

    public void onEvent(Events.LoginErr event) {
        mUserView.bindUserInfo(getContext());
    }

    public void onEvent(Events.LoginOk event) {
        mUserView.bindUserInfo(getContext());
    }

    public void onEvent(Events.Logout event) {
        mUserView.bindUserInfo(getContext());
    }

    final public void onEventMainThread(Events.OnWeMediaChannelInfoUpdated event) {
        mUserView.updateUserInfo();
    }
}
