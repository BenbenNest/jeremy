package com.jeremy.lychee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.customview.news.ChannelFirstChooseView;
import com.jeremy.lychee.db.NewsChannel;
import com.jeremy.lychee.db.NewsChannelDao;
import com.jeremy.lychee.fragment.LunboFragment;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.news.NewsChannelMode;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.CommonPreferenceUtil;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.hitLog.HitLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChannelFirstChooseActivity extends FragmentActivity implements LunboFragment.OnLunboFragmentClickListener {

    @Bind(com.jeremy.lychee.R.id.view)
    ChannelFirstChooseView firstChooseView;

    private List<NewsChannel> newsChannelList = new ArrayList<>();
    private String md5;
    private String cidString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.jeremy.lychee.R.layout.activity_channel_first_choose);
        ButterKnife.bind(this);
        OldRetroAdapter.getService().getNewChannel()
                .subscribeOn(Schedulers.io())
                .map(ModelBase::getData)
                .doOnNext(model -> md5 = model.getKey())
                .map(NewsChannelMode::getList)
                .doOnNext(this::saveForbidDelCids)
                .map(this::convert)
                .doOnNext(list -> newsChannelList = list)
                .map(this::filterList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(firstChooseView::setData, Throwable::printStackTrace);


    }


    @OnClick(com.jeremy.lychee.R.id.confirm_btn)
    public void confirmNewsChannel() {
        CommonPreferenceUtil.SaveNewsChannelMd5(md5);
        CommonPreferenceUtil.SetIsFirstOpen(false);
        Observable.just(newsChannelList)
                .subscribeOn(Schedulers.io())
                .doOnNext(this::saveCids)
                .doOnNext(this::saveDb)
                .doOnNext(this::syncChannel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getClickObserver());

        //打点 4003
        List<String> list = new ArrayList<>();
        for(NewsChannel it : newsChannelList){
            if(it.getIsShow() && it.getInit().equals("0")){
                list.add(it.getCid());
            }
        }
        HitLog.hitLogFirstChooseChannels(list);
    }



    public void saveForbidDelCids(List<NewsChannel> list) {
        String cids = "";
        for (NewsChannel newsChannel : list) {
            if ("1".equals(newsChannel.getInit())) {
                cids += newsChannel.getCid() + "|";
            }
        }
        CommonPreferenceUtil.SetForbitDelCids(cids);
    }

    private List<NewsChannel> filterList(List<NewsChannel> newsChannelList) {
        List<NewsChannel> list = new ArrayList<>();
        for (Iterator<NewsChannel> iterator = newsChannelList.iterator(); iterator.hasNext(); ) {
            NewsChannel channel = iterator.next();
            if ("1".equals(channel.getIs_start())) {
                list.add(channel);
            }
        }
        return list;
    }

    private List<NewsChannel> convert(List<NewsChannel> newsChannelList) {
        for (NewsChannel newsChannel : newsChannelList) {
            newsChannel.setIsShow("1".equals(newsChannel.getInit()));
        }
        return newsChannelList;
    }


    public void saveCids(List<NewsChannel> list) {
        for (NewsChannel newsChannel : list) {
            if ("1".equals(newsChannel.getInit()) || (newsChannel.getIsShow() != null && newsChannel.getIsShow())) {
                cidString += newsChannel.getCid() + "|";
            }
        }
        new NewsChannelPreference().saveNewsChannelCids(cidString);
    }

    private void saveDb(List<NewsChannel> newsChannels) {
//        Schedulers.io().createWorker().schedule(() -> {
//
//        });
        NewsChannelDao dao = ContentApplication.getDaoSession().getNewsChannelDao();
        dao.insertOrReplaceInTx(newsChannels);
    }

    private Observer<List<NewsChannel>> getClickObserver() {
        return new Observer<List<NewsChannel>>() {
            @Override
            public void onCompleted() {
                startMainActivity();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                startMainActivity();
            }

            @Override
            public void onNext(List<NewsChannel> newsChannel) {

            }
        };
    }

    private void startMainActivity() {
        Intent intent = new Intent(ChannelFirstChooseActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }

    private void syncChannel(List<NewsChannel> newsChannelList) {
        List<String> cidList = new ArrayList<>();
        for (NewsChannel newsChannel : newsChannelList) {
            if ("1".equals(newsChannel.getInit()) || (newsChannel.getIsShow() != null && newsChannel.getIsShow())) {
                cidList.add(newsChannel.getCid());
            }
        }
        Collections.sort(cidList);
        String cids = "";
        for (String cid : cidList) {
            cids += cid;
            cids += "|";
        }
        OldRetroAdapter.getService().newsChannelAsyn(cids, AppUtil.getSyChannelSign(cids))
                .subscribeOn(Schedulers.io())
                .subscribe(data->{
                }, Throwable::printStackTrace);
    }

    @Override
    public void onLunboFragmentClick() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(com.jeremy.lychee.R.id.lunbo_fragment);
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(fragment);
        ft.commitAllowingStateLoss();
    }
}
