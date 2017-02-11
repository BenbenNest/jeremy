package com.jeremy.lychee.fragment.live;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.widget.slidingtab.CustomSlidingTabLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LiveTabContainerFragment2 extends BaseFragment {
    @Bind(com.jeremy.lychee.R.id.viewpager)
    ViewPager viewPager;
    @Bind(com.jeremy.lychee.R.id.live_page_tab_layout)
    CustomSlidingTabLayout mTabLayout;

    private ViewPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.jeremy.lychee.R.layout.fragment_live_tab_container2, container, false);
        ButterKnife.bind(this, rootView);
        QEventBus.getEventBus().register(this);
        initUI();

        return rootView;
    }

    public void onEvent(LiveEvent.SubAlarm event) {
        String channelId = event.channelId;
        if (!TextUtils.isEmpty(channelId)){
            new Handler().postDelayed(() -> {
                if (viewPager!=null){
                    viewPager.setCurrentItem(1);
                }
            },200);

        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
    }

    private void initUI() {
        mAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(mAdapter.fragmentsInfo.length);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 1) {
                    QEventBus.getEventBus().post(new LiveChannelFragment2.StopVideoPlaying());
                }
              /*  if (mAdapter.getItem(position) instanceof LiveChannelFragment){

                }else {
                    if ( mAdapter!=null&&mAdapter.getItem(1)!=null){
                        mAdapter.getItem(1).onPause();
                    }

                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*viewPager.setCurrentItem(0);*/
        mTabLayout.setViewPager(viewPager);

        /*mAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            QEventBus.getEventBus().post(new LiveChannelFragment2.StopVideoPlaying());
        }

        try {
            Fragment fragment = mAdapter.fragments[viewPager.getCurrentItem()];
            if (fragment != null) {
                fragment.setUserVisibleHint(!hidden);
            }
        } catch (Throwable ignored) {
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final FragmentInfo fragmentsInfo[] = {
                new FragmentInfo("热点", LiveDiscoveryColumnFragment2.class),
                new FragmentInfo("频道", LiveChannelFragment2.class),
        };

        private Fragment fragments[] = new Fragment[fragmentsInfo.length];

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentsInfo.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position] == null) {
                try {
                    fragments[position] =
                            (Fragment) fragmentsInfo[position].fragmentClazz_.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return fragments[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsInfo[position].title_;
        }


        private class FragmentInfo {
            CharSequence title_;
            Class fragmentClazz_;

            FragmentInfo(CharSequence title, Class clazz) {
                title_ = title;
                fragmentClazz_ = clazz;
            }
        }
    }
}
