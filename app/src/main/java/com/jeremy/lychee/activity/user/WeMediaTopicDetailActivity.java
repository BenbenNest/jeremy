package com.jeremy.lychee.activity.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.R;
import com.jeremy.lychee.adapter.user.WeMediaChannelTopicArticleFeedListAdapter;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.customview.PullListRecyclerView;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WeMediaTopicDetailActivity extends SlidingActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    @Bind(R.id.article_recycler_view)
    PullListRecyclerView mRecyclerView;

    private int mTopicId;
    private String mTopicName;
    private static final String TOPIC_ID = "TOPIC_ID";
    private static final String TOPIC_NAME = "TOPIC_NAME";
    private boolean mIsSub = false;

    private WeMediaChannelTopicArticleFeedListAdapter mAdapter;

    public static void startActivity(Context context, int id, String name) {
        Intent intent = new Intent(context, WeMediaTopicDetailActivity.class);
        intent.putExtra(TOPIC_ID, id);
        intent.putExtra(TOPIC_NAME, name);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }

    private void getExtraParameters() {
        if (getIntent() != null) {
            mTopicId = getIntent().getIntExtra(TOPIC_ID, 0);
            mTopicName = getIntent().getStringExtra(TOPIC_NAME);
        } else {
            finish();
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_wemedia_topic_detail);
        ButterKnife.bind(this);
        getExtraParameters();
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        mToolBar.setTitle(mTopicName);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        initToolbar();

        if (mRecyclerView != null) {
            mAdapter = new WeMediaChannelTopicArticleFeedListAdapter(this, mTopicId);
            mRecyclerView.setAdapter(mAdapter, true);
        }
    }

    private boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    private void initToolbar() {
        if (isLogin()) {
            mToolBar.inflateMenu(R.menu.menu_wemedia_topic_detail);
            findViewById(R.id.menu_text).setOnClickListener(v -> {
                //订阅按钮
                subscribeWeMediaTopic(mTopicId, !mIsSub);
            });
            requestIsSub(mTopicId, 2)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (s.get(0).equals("1")) {//订阅
                            mIsSub = true;
                            ((TextView) findViewById(R.id.menu_text)).setText(R.string.channel_booked);
                        } else {//未订阅
                            mIsSub = false;
                            ((TextView) findViewById(R.id.menu_text)).setText(R.string.channel_unbook);
                        }
                    }, Throwable::printStackTrace);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void subscribeWeMediaTopic(int cId, boolean sub) {
        if (sub) {
            OldRetroAdapter.getService().subscribeColumn(cId, 2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if(s.getErrno() == 0){
                            mIsSub = true;
                            ((TextView) findViewById(R.id.menu_text)).setText(R.string.channel_booked);
                            QEventBus.getEventBus().post(new Events.OnSubscribedTopicListUpdated());
                        }else{
                            ToastHelper.getInstance(this).toast("订阅失败");
                        }
                    }, e -> {
                        ToastHelper.getInstance(this).toast("订阅失败");
                        e.printStackTrace();
                    });
        } else {
            OldRetroAdapter.getService().unSubscribeColumn(cId, 2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        if (s.getErrno() == 0) {
                            mIsSub = false;
                            ((TextView) findViewById(R.id.menu_text)).setText(R.string.channel_unbook);
                            QEventBus.getEventBus().post(new Events.OnSubscribedTopicListUpdated());
                        } else {
                            ToastHelper.getInstance(this).toast("取消订阅失败");
                        }
                    }, e -> {
                        ToastHelper.getInstance(this).toast("取消订阅失败");
                        e.printStackTrace();
                    });
        }
    }

    private Observable<List<String>> requestIsSub(int cid, int type){
        return OldRetroAdapter.getService()
                .getIsSub(cid, type)
                .filter(s -> s.getErrno() == 0)
                .map(ModelBase::getData)
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle());
    }

}
