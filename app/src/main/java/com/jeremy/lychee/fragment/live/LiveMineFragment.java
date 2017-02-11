package com.jeremy.lychee.fragment.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.utils.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Live about mine list
 */
@Deprecated
public class LiveMineFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    @Bind(com.jeremy.lychee.R.id.history_browse)
    RadioButton history_browse;

    @Bind(com.jeremy.lychee.R.id.history_upload)
    RadioButton history_upload;

    @Bind(com.jeremy.lychee.R.id.history_record)
    RadioButton history_record;

    @Bind(com.jeremy.lychee.R.id.login_lay)
    View login_lay;

    public LiveMineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.jeremy.lychee.R.layout.fragment_live_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        QEventBus.getEventBus().register(this);

        if(isLogin()){
            login_lay.setVisibility(View.GONE);
        } else {
            login_lay.setVisibility(View.VISIBLE);
        }

        history_browse.setOnCheckedChangeListener(this);
        history_upload.setOnCheckedChangeListener(this);
        history_record.setOnCheckedChangeListener(this);

        login_lay.setOnClickListener(v -> startActivity(new Intent(getContext(), LoginActivity.class)));

        String tag = LiveMineListFragment.BROWSE_TAG;
        showFragment(tag);
    }

    private void showFragment(String tag){
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(tag);
        if(fragment==null){
            fragment = LiveMineListFragment.newInstance(tag);
            ft.add(com.jeremy.lychee.R.id.fragment_container, fragment, tag);

        } else {
            ft.show(fragment);
            fragment.setUserVisibleHint(true);
        }
        ft.commitAllowingStateLoss();
    }
    private void hideFragment(String tag) {
        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if(fragment!=null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(fragment);
            ft.commitAllowingStateLoss();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
    }

    public void onEvent(Events.Logout event) {
        //退出登录
        login_lay.setVisibility(View.VISIBLE);
        Logger.e("LiveMineFragment onEvent Logout");
    }

    public void onEvent(Events.LoginOk event) {
        //登录成功
        login_lay.setVisibility(View.GONE);
        FragmentManager fm = getChildFragmentManager();
        List<Fragment> fragments =  fm.getFragments();
        if(fragments!=null){
            for(Fragment fragment:fragments){
                if(fragment!=null&&fragment instanceof  LiveMineListFragment){
                    LiveMineListFragment liveMineListFragment = (LiveMineListFragment) fragment;
                    liveMineListFragment.onEvent(event);
                }
            }
        }
        Logger.e("LiveMineFragment onEvent LoginOk");
    }

    private boolean isLogin(){
        return !Session.isUserInfoEmpty();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked){
            return;
        }
        int checkedId = buttonView.getId();
        switch (checkedId){
            case com.jeremy.lychee.R.id.history_browse:
                showFragment(LiveMineListFragment.BROWSE_TAG);

                hideFragment(LiveMineListFragment.UPLOAD_TAG);
                hideFragment(LiveMineListFragment.RECORD_TAG);
                break;
            case com.jeremy.lychee.R.id.history_upload:
                showFragment(LiveMineListFragment.UPLOAD_TAG);

                hideFragment(LiveMineListFragment.BROWSE_TAG);
                hideFragment(LiveMineListFragment.RECORD_TAG);
                break;
            case com.jeremy.lychee.R.id.history_record:
                showFragment(LiveMineListFragment.RECORD_TAG);

                hideFragment(LiveMineListFragment.BROWSE_TAG);
                hideFragment(LiveMineListFragment.UPLOAD_TAG);
                break;
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //TODO child fragment setUserVisibleHint
        Logger.e("LiveMineFragment setUserVisibleHint isVisibleToUser:" + isVisibleToUser);
        if(isVisibleToUser){
            FragmentManager fm = getChildFragmentManager();
            List<Fragment> fragments =  fm.getFragments();
            if(fragments!=null){
                for(Fragment fragment:fragments){
                    if(!fragment.isHidden()){
                        fragment.setUserVisibleHint(true);
                    }
                }
            }
        }
    }

}
