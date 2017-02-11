package com.jeremy.lychee.manager;

import android.text.TextUtils;

import com.jeremy.lychee.db.NewsChannelDao;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.NewsChannelPreference;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.db.NewsChannel;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.news.Config;
import com.jeremy.lychee.model.news.NewsChannelMode;
import com.jeremy.lychee.preference.CommonPreferenceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import rx.schedulers.Schedulers;

public class ConfigManager {
    private static volatile ConfigManager instance;

    private ConfigManager() {

    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public void updateConfig() {
        OldRetroAdapter.getService().getConfig()
                .subscribeOn(Schedulers.io())
                .map(ModelBase::getData)
                .subscribeOn(Schedulers.io())
                .subscribe(this::update, Throwable::printStackTrace);
    }

    private void update(Config config) {
        boolean flag = false;
        String channelMD5 = config.getChannel();
        NewsChannelDao dao = ContentApplication.getDaoSession().getNewsChannelDao();
        List<NewsChannel> channelList = dao.queryBuilder().build().list();
        if (channelList.size() == 0) {
            flag = true;
        }
        if (!TextUtils.isEmpty(channelMD5)) {
            if (!channelMD5.equals(CommonPreferenceUtil.GetNewsChannelMd5())) {
                flag = true;
            }
        }
//        if (flag) {
        updateNewsChannel();
//        }
    }

    public void updateNewsChannel() {
        OldRetroAdapter.getService().getNewChannel()
                .subscribeOn(Schedulers.io())
                .map(ModelBase::getData)
                .observeOn(Schedulers.io())
                .subscribe(newsChannelMode -> {
                    saveForbidDelCids(newsChannelMode.getList());
                    saveNewsChannelMd5(newsChannelMode);
                    saveNewsChannelCids(newsChannelMode);
                    saveToDb(newsChannelMode);
                    QEventBus.getEventBus().post(new Events.UpdateNewsChannelListEvent(-1));
                }, throwable -> {
                    throwable.printStackTrace();
                    if (getPreChannelListCount() == 0) {
                        QEventBus.getEventBus().post(new Events.UpdateNewsChannelError(throwable.getMessage()));
                    }
                });
    }

    private void saveToDb(NewsChannelMode newsChannelMode) {
        if (newsChannelMode == null || newsChannelMode.getList() == null) {
            return;
        }
        List<NewsChannel> newsChannels = newsChannelMode.getList();
        try {
            NewsChannelDao newsChannelDao = ContentApplication.getDaoSession().getNewsChannelDao();
            newsChannelDao.deleteAll();
            newsChannelDao.insertOrReplaceInTx(newsChannels);
        } catch (Throwable e) {
            e.printStackTrace();
        }
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

    private void saveNewsChannelMd5(NewsChannelMode newsChannelMode) {
        String md5 = newsChannelMode.getKey();
        if (!TextUtils.isEmpty(md5)) {
            CommonPreferenceUtil.SaveNewsChannelMd5(md5);
        }
    }

    private void saveNewsChannelCids(NewsChannelMode newsChannelMode) {
        List<NewsChannel> newsChannelList = newsChannelMode.getList();
        List<NewsChannel> prevChanelList = getPrevChanelList();
        List<NewsChannel> requiredChannelList = new ArrayList<>();
        List<NewsChannel> preRequiredChannelList = new ArrayList<>();
        for (NewsChannel newsChannel : newsChannelList) {
            if ("1".equals(newsChannel.getInit())) {
                requiredChannelList.add(newsChannel);
            }
        }
        for (NewsChannel newsChannel : prevChanelList) {
            if ("1".equals(newsChannel.getInit())) {
                preRequiredChannelList.add(newsChannel);
            }
        }
        List<String> newsCidList = getOrderedCidList();
        // 去掉已经订阅的实效的频道
        remove(newsChannelList, newsCidList, false);
        if (CommonPreferenceUtil.HasModifyNewsChannel()) {
            for (NewsChannel newsChannel : requiredChannelList) {
                if (!newsCidList.contains(newsChannel.getCid())) {
                    newsCidList.add(newsChannel.getCid());
                }
            }
        } else {
            remove(preRequiredChannelList, newsCidList, true);
            int postion = 0;
            for (NewsChannel newsChannel : requiredChannelList) {
                if ("1".equals(newsChannel.getInit())) {
                    newsCidList.add(postion++, newsChannel.getCid());
                }
            }
        }
        String cidString = "";
        for (String id : newsCidList) {
            if (TextUtils.isEmpty(id)) {
                continue;
            }
            cidString += id + "|";
        }
        new NewsChannelPreference().saveNewsChannelCids(cidString);
    }

    private void remove(List<NewsChannel> newsChannelList, List<String> newsCidList, boolean remove) {
        Iterator<String> iterator = newsCidList.iterator();
        while (iterator.hasNext()) {
            String cid = iterator.next();
            if (TextUtils.isEmpty(cid)) {
                iterator.remove();
                continue;
            }
            boolean flag = false;
            for (NewsChannel newsChannel : newsChannelList) {
                if (cid.equals(newsChannel.getCid())) {
                    flag = true;
                    break;
                }
            }
            if (flag == remove) {
                iterator.remove();
            }
        }
    }

    private List<NewsChannel> getPrevChanelList() {
        NewsChannelDao dao = ContentApplication.getDaoSession().getNewsChannelDao();
        List<NewsChannel> list = dao.queryBuilder().list();
        return list;
    }

    private long getPreChannelListCount() {
        NewsChannelDao dao = ContentApplication.getDaoSession().getNewsChannelDao();
        return dao.queryBuilder().count();
    }

    private List<String> getOrderedCidList() {
        NewsChannelPreference newsChannelPreference = new NewsChannelPreference();
        String cids = newsChannelPreference.getNewsChannelCids();
        String[] newsCids = cids.split("\\|");
        return new ArrayList<>(Arrays.asList(newsCids));

    }
}
