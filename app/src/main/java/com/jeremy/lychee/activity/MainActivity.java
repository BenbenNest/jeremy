package com.jeremy.lychee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.fragment.news.NewsFragment;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.customview.BottomMenuBar;
import com.jeremy.lychee.customview.ContentLayout;
import com.jeremy.lychee.R;
import com.jeremy.lychee.base.BaseActivity;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.manager.QhLocationManager;
import com.jeremy.lychee.model.update.UpdateChecker;
import com.jeremy.lychee.preference.CommonPreferenceUtil;
import com.jeremy.lychee.videoplayer.VideoPlayerView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;


public class MainActivity extends SlidingActivity {

    private Subject<Void, Void> exitWatcher;
    private static final int EXIT_DBLCLICK_PERIOD = 2000;
    View introView;

    @Bind(R.id.contentLayout)
    ContentLayout contentLayout;

    @Bind(R.id.bottomBar)
    BottomMenuBar bottomMenuBar;
    @Bind(R.id.intro_view_stub)
    ViewStub introViewStub;
    @Bind(R.id.view_tab_news)
    View newsTab;
    @Bind(R.id.view_tab_live)
    View liveTab;
    @Bind(R.id.view_tab_user)
    View userTab;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        //启动打点：更新gps
        QhLocationManager.getInstance().updateLocation(
                HitLog::hitLogAppStart);
        //设备信息打点：
        HitLog.hitLogDeviceInfo();
        QEventBus.getEventBus().register(this);

    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);
        exitWatcher = new SerializedSubject<>(PublishSubject.create());
        exitWatcher.asObservable()
                .compose(bindToLifecycle())
                .throttleFirst(EXIT_DBLCLICK_PERIOD, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(it -> Toast.makeText(this, R.string.exit_text, Toast.LENGTH_SHORT).show(), Throwable::printStackTrace);
        exitWatcher.asObservable()
                .compose(bindToLifecycle())
                .timeInterval(AndroidSchedulers.mainThread())
                .skip(1)
                .filter(it -> it.getIntervalInMilliseconds() < EXIT_DBLCLICK_PERIOD)

                .subscribe(it -> {
                    //订阅频道打点
                    String newsChannelCids = new NewsChannelPreference().getNewsChannelCids();
                    HitLog.hitLogSubChannels(Arrays.asList(newsChannelCids.split("\\|")));
                    //退出打点：更新gps
                    sAppForgroundDuration += System.currentTimeMillis() / 1000L - sAppResumeTimeStamp;
                    HitLog.hitLogAppEnd(BaseActivity.sAppForgroundDuration);
                    finish();
                },Throwable::printStackTrace);

        new UpdateChecker(this).startCheck();
        //直播订阅跳转
        if (getIntent()!=null){
            if (getIntent().getAction()!=null&&getIntent().getAction().equals("com.qihoo.lianxian.fragment.live")){
                String tabType = getIntent().getStringExtra("tablive");
                String liveChannelId = getIntent().getStringExtra("channel_id");
                String tvtype = getIntent().getStringExtra("tvtype");
                new Handler().postDelayed(() -> {
                    Button btn = (Button) findViewById(R.id.view_tab_live);
                    if (bottomMenuBar!=null&&contentLayout!=null){
                        bottomMenuBar.setTabSelected(btn);
                        contentLayout.showLiveTab();
                    }

                }, 400);
                new Handler().postDelayed(() -> {
                    LiveEvent.SubAlarm event = new LiveEvent.SubAlarm();
                    event.channelId = liveChannelId;
                    event.tvtype = tvtype;
                    QEventBus.getEventBus().post(event);
                }, 800);
                /*findViewById(R.id.view_tab_live).performClick();*/
            }
        }

        if (CommonPreferenceUtil.GetIntroNewsFragment()) {
            showIntroView(R.drawable.intro_square, v -> {
                showIntroView(R.drawable.intro_channel, v1 -> {
                    v1.setVisibility(View.GONE);
                    CommonPreferenceUtil.setIntroNewsFragment(false);
                });
            });
        }
        setBottomMenuBarClickListner(newsTab);
        setBottomMenuBarClickListner(liveTab);
        setBottomMenuBarClickListner(userTab);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent!=null){
            if (intent.getAction()!=null&&intent.getAction().equals("com.qihoo.lianxian.fragment.live")){
                String tabType = getIntent().getStringExtra("tablive");
                String liveChannelId = intent.getStringExtra("channel_id");
                String tvtype = intent.getStringExtra("tvtype");
                new Handler().postDelayed(() -> {
                    Button btn = (Button) findViewById(R.id.view_tab_live);
                    if (bottomMenuBar!=null&&contentLayout!=null){
                        bottomMenuBar.setTabSelected(btn);
                        contentLayout.showLiveTab();
                    }
                }, 400);
                new Handler().postDelayed(() -> {
                    LiveEvent.SubAlarm event = new LiveEvent.SubAlarm();
                    event.channelId = liveChannelId;
                    event.tvtype = tvtype;
                    QEventBus.getEventBus().post(event);
                }, 800);
                /*findViewById(R.id.view_tab_live).performClick();*/
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /**
         * 崩溃重启时不保存Fragment的状态，随Activity重新初始化，
         * 避免异常状态
         */
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (exitWatcher!=null) {
            exitWatcher.onCompleted();
            exitWatcher = null;
        }
        QEventBus.getEventBus().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (contentLayout.onBackPressed()) {
            return;
        }

        if (VideoPlayerView.isFullScreen()) {
            VideoPlayerView.exitFullScreen();
            return;
        }

        if (exitWatcher!=null) {
            exitWatcher.onNext(null);
        }
    }
    private void setBottomMenuBarClickListner(View view) {
        RxView.clicks(view)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    bottomMenuBar.setTabSelected(view);
                    if (contentLayout == null) {
                        return;
                    }
                    if (view.getId() == R.id.view_tab_news) {
                        Fragment currentFragment = contentLayout.getCurrentFragment();
                        if (currentFragment != null && currentFragment.getClass() == NewsFragment.class) {
                            QEventBus.getEventBus().post(new Events.UpdateNewsEvent());
                        }else {
                            contentLayout.showNewTab();
                        }
                    } else if (view.getId() == R.id.view_tab_live) {
                        contentLayout.showLiveTab();

                    } else if (view.getId() == R.id.view_tab_user) {
                        contentLayout.showUserTab();
                        if (CommonPreferenceUtil.GetIntroWemediaFragment()) {
                            showIntroView(R.drawable.intro_wemedia, v -> {
                                v.setVisibility(View.GONE);
                                CommonPreferenceUtil.setIntroWemediaFragment(false);
                            });
                        }
                    }
                    QEventBus.getEventBus().post(new Events.HidePopWinEvent());
                },throwable -> throwable.printStackTrace());
    }

    public void showIntroView(int resId, View.OnClickListener listener) {
//        if (introView == null) {
//            introView = introViewStub.inflate();
//        }else {
//            introView.setVisibility(View.VISIBLE);
//        }
//        introView.setBackgroundResource(resId);
//        introView.setOnClickListener(listener);
    }

    final public void onEventMainThread(LiveEvent.showIntroLive event) {
        if (CommonPreferenceUtil.GetIntroLiveFragment()) {
                        showIntroView(R.drawable.intro_live,
                    v -> v.setVisibility(View.GONE));
                CommonPreferenceUtil.setIntroLiveFragment(false);
        }
    }
    final public void onEventMainThread(LiveEvent.showIntroLiveDiscoveryOpen event) {
        if (CommonPreferenceUtil.GetIntroLiveFragment()) {
            showIntroView(R.drawable.intro_live_discovery,
                    v -> {v.setVisibility(View.GONE);
                        QEventBus.getEventBus().post(new LiveEvent.showIntroLiveDiscoveryColse());});

            CommonPreferenceUtil.setIntroLiveFragment(false);
        }
    }
}
