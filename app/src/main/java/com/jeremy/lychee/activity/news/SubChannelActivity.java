package com.jeremy.lychee.activity.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.jeremy.lychee.R;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.db.NewsChannel;
import com.jeremy.lychee.db.NewsChannelDao;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.fragment.news.ContentFragment;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SubChannelActivity extends SlidingActivity {

    private static final String CID_KEY = "cid";
    private static final String CNAME_KEY = "cname";
    private static final String ICON_KEY = "icon";
    private static final String SHOW_SUBSCRIPTION = "show_subscription";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String lastCids;
    private boolean hasOrdered;
    //    private ChannelCategory.ChannelInfo.SubChannel subChannel;
    private String cid;
    private String cnamne;
    private String icon;
    private boolean showSubscription;
    private String orderedCids = "";

    public static void StartActivity(Context context, String cid, String cnamne, String icon) {
        StartActivity(context, cid, cnamne, icon, true);
    }

    public static void StartActivity(Context context, String cid, String cnamne, String icon, boolean showSubscription) {
        if (context instanceof SlidingActivity) {
            SlidingActivity activity = (SlidingActivity) context;
            Bundle bundle = new Bundle();
            bundle.putString(CID_KEY, cid);
            bundle.putString(CNAME_KEY, cnamne);
            bundle.putString(ICON_KEY, icon);
            bundle.putBoolean(SHOW_SUBSCRIPTION, showSubscription);
            activity.openActivity(SubChannelActivity.class, bundle, 0);
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_sub_channel);
        getDataFromIntent();
        ButterKnife.bind(this);
        initToolbar();
        getCids().subscribeOn(Schedulers.io())
                .map(s -> s.split("\\|"))
                .doOnNext(cidArray -> orderedCids = getOrderCids(cidArray))
                .filter(strings -> {
                    for (String s : strings) {
                        Logger.d(s);
                        if (!TextUtils.isEmpty(s) && s.equals(cid)) {
                            return true;
                        }
                    }
                    return false;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings1 -> setOrderOrNot(), Throwable::printStackTrace);
        initFragment();
    }

    @NonNull
    private Observable<String> getCids() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                lastCids = new NewsChannelPreference().getNewsChannelCids();
                subscriber.onNext(lastCids);
                subscriber.onCompleted();
            }
        });
    }

    private void getDataFromIntent() {
        cid = getIntent().getStringExtra(CID_KEY);
        cnamne = getIntent().getStringExtra(CNAME_KEY);
        icon = getIntent().getStringExtra(ICON_KEY);
        showSubscription = getIntent().getBooleanExtra(SHOW_SUBSCRIPTION, true);
        if (TextUtils.isEmpty(cid) || TextUtils.isEmpty(cnamne) || TextUtils.isEmpty(icon)) {
            throw new IllegalArgumentException();
        }
    }

    private void initFragment() {
        ContentFragment channelFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CNAME_KEY, cnamne);
        bundle.putString(CID_KEY, cid);
        bundle.putBoolean(ContentFragment.EXTRA_IS_NEED_SEARCH_VIEW, false);
        channelFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.content, channelFragment).commit();
        channelFragment.goRefresh();
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setTitle(cnamne);
        if (!TextUtils.isEmpty(cid) && showSubscription) {
            toolbar.inflateMenu(R.menu.menu_channel_order);

            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_order) {
                    Logger.t("menu").d("menu order");
                    setOrderOrNot();
                    return true;
                }
                return false;
            });
        }
    }

    private void setOrderOrNot() {
        hasOrdered = !hasOrdered;
        if (toolbar.getMenu() == null) {
            return;
        }
        if (toolbar.getMenu().getItem(0) == null) {
            return;
        }
        MenuItem menuItem = toolbar.getMenu().getItem(0);
        if (hasOrdered) {
            menuItem.setTitle("已订阅");
        } else {
            menuItem.setTitle("订阅");
        }
    }

    @Override
    public void finish() {
        if (hasOrdered) {
            addChannel(cid);
        } else {
            removeChannel(cid);
        }
        super.finish();
    }

    private void removeChannel(String cid) {
        String[] cids = lastCids.split("\\|");
        List<String> cidList = new ArrayList<>(Arrays.asList(cids));
        for (Iterator<String> iterator = cidList.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            if (next.equals(cid)) {
                iterator.remove();
            }
        }
        String temp = "";
        for (String string : cidList) {
            temp += string + "|";
        }
        if (!temp.equals(lastCids)) {
            new NewsChannelPreference().saveNewsChannelCids(temp);
            QEventBus.getEventBus().post(new Events.UpdateNewsChannelListEvent(-1));
        }
        syncCids(temp);
    }

    private void addChannel(String cid) {
        String[] cids = lastCids.split("\\|");
        List<String> cidList = Arrays.asList(cids);
        Boolean hasChannel = false;
        for (Iterator<String> iterator = cidList.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            if (next.equals(cid)) {
                hasChannel = true;
            }
        }
        String temp = lastCids;
        if (!hasChannel) {
            temp += cid + "|";
        }

        if (!temp.equals(lastCids)) {
            new NewsChannelPreference().saveNewsChannelCids(temp);
            QEventBus.getEventBus().post(new Events.UpdateNewsChannelListEvent(-1));
        }
        NewsChannelDao newsChannelDao = ContentApplication.getDaoSession().getNewsChannelDao();
        NewsChannel newsChannel = new NewsChannel();
        newsChannel.setCid(cid);
        newsChannel.setCname(cnamne);
        newsChannel.setIcon(icon);
        newsChannelDao.insertOrReplace(newsChannel);
        syncCids(temp);
    }

    private void syncCids(String temp) {
        Observable.just(temp)
                .map(s -> s.split("\\|"))
                .map(this::getOrderCids)
                .filter(s -> !s.equals(orderedCids))
                .subscribe(s -> {
                    OldRetroAdapter.getService().newsChannelAsyn(s, AppUtil.getSyChannelSign(s))
                            .subscribeOn(Schedulers.io())
                            .subscribe(data -> {
                            }, Throwable::printStackTrace);
                }, Throwable::printStackTrace);
    }

    private String getOrderCids(String[] cids) {
        String orderCids = "";
        Arrays.sort(cids);
        for (String cid : cids) {
            orderCids += cid + "|";
        }
        if (!orderCids.isEmpty()) {
            orderCids = orderCids.substring(0, orderCids.length() - 1);
        }
        return orderCids;
    }

}
