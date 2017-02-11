package com.jeremy.lychee.customview;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.jeremy.lychee.R;
import com.jeremy.lychee.base.BaseLayout;
import com.jeremy.lychee.channel.QHState;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.fragment.FragmentHandler;
import com.jeremy.lychee.fragment.live.LiveChannelFragment3;
import com.jeremy.lychee.fragment.news.NewsFragment;
import com.jeremy.lychee.fragment.user.UserFragment;
import com.jeremy.lychee.fragment.user.UserMessageFragment;
import com.qihoo.sdk.report.QHStatAgent;

public class ContentLayout extends BaseLayout {

    FragmentHandler fragmentHandler;

    public ContentLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onInflate(Context context) {
        LayoutInflater.from(context).inflate(R.layout.content_layout, this);
    }

    @Override
    protected void initUI(Activity activity) {

        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            fragmentHandler = new FragmentHandler(fragmentManager);
        } else {
            throw new RuntimeException("activity show be fragment activity");
        }
        registerFragment();
        showNewTab();
    }

    private void registerFragment() {
        fragmentHandler.registerFragment(NewsFragment.class, R.id.contentLayout);
       /* fragmentHandler.registerFragment(LiveTabContainerFragment2.class, R.id.contentLayout);*/
        fragmentHandler.registerFragment(LiveChannelFragment3.class, R.id.contentLayout);
        fragmentHandler.registerFragment(UserFragment.class, R.id.contentLayout);
    }

    public void showNewTab() {
        fragmentHandler.switchToFragment(NewsFragment.class, false);
        QEventBus.getEventBus().post(new LiveChannelFragment3.StopVideoPlaying());
    }

    public void showLiveTab() {
        QHStatAgent.onEvent(getContext(), QHState.LIVE_TAB_PV);
        fragmentHandler.switchToFragment(LiveChannelFragment3.class, false);
    }

    public void showUserTab() {
        fragmentHandler.switchToFragment(UserFragment.class, false);
        QHStatAgent.onEvent(getContext(), QHState.USER_TAB_PV);
        QEventBus.getEventBus().post(new LiveChannelFragment3.StopVideoPlaying());
        UserMessageFragment.checkUnreadMessage(aBoolean -> {
            if(aBoolean){
                QEventBus.getEventBus().post(new Events.OnMessageReceived());
            }
        });
    }

    public boolean onBackPressed() {
        return false;
    }
    public Fragment getCurrentFragment(){
        return fragmentHandler.getCurrentFragment();
    }


}
