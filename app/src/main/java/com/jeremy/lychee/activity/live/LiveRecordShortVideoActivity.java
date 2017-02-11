package com.jeremy.lychee.activity.live;

import android.support.v4.app.Fragment;
import android.view.View;

import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.base.ImmersionActivity;
import com.jeremy.lychee.fragment.live.LiveShortVideoRecordingFragment;

import java.util.List;

public class LiveRecordShortVideoActivity extends ImmersionActivity {
    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_record_short_video);
    }

    @Override
    public View onGetFitWindowView() {
        return findViewById(com.jeremy.lychee.R.id.root);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();

        setFitsSystemWindows(true);
        setStatusBarColor(getResources().getColor(com.jeremy.lychee.R.color.statusbar_color));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(com.jeremy.lychee.R.id.root, LiveShortVideoRecordingFragment.instantiate(this, LiveShortVideoRecordingFragment.class.getName()))
                .commit();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (!fragment.isVisible()) {
                continue;
            }
            if(((BaseFragment)fragment).onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }
}
