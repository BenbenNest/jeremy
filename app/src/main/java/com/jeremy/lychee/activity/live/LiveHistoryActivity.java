package com.jeremy.lychee.activity.live;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.jeremy.lychee.R;
import com.jeremy.lychee.fragment.live.LiveMineListFragment;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LiveHistoryActivity extends SlidingActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_live_history);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);
        showFragment(LiveMineListFragment.BROWSE_TAG);

        mToolBar.setTitle("播放记录");
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void showFragment(String tag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = LiveMineListFragment.newInstance(tag);
        ft.add(R.id.container, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
