package com.jeremy.lychee.manager;

import android.text.TextUtils;

import com.jeremy.lychee.db.NewsChannel;
import java.util.ArrayList;
import java.util.List;

public class NewsChannelManager {
    public static final String SCENE_CID = "scene";
    private volatile static NewsChannelManager instance;
    private List<NewsChannel> newsChannelList;
    private String sceneId = "";
    private String sceneName;

    private NewsChannelManager() {
        newsChannelList = new ArrayList<>();
    }

    public static NewsChannelManager getInstance() {
        if (instance == null) {
            synchronized (NewsChannelManager.class) {
                if (instance == null) {
                    instance = new NewsChannelManager();
                }
            }
        }
        return instance;
    }

    public void setNewsChannelList(List<NewsChannel> newsChannelList) {
        this.newsChannelList = newsChannelList;
    }

    public List<NewsChannel> getNewsChannelList() {
        return newsChannelList;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public boolean hasSceneChannel() {
        return !TextUtils.isEmpty(sceneId);
    }

    public int getIndexByCid(String cid) {
        int i = 0;
        for (NewsChannel newsChannel : newsChannelList) {
            if (newsChannel.getCid().equals(cid)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void reset(){
        newsChannelList.clear();
        resetScene();
    }
    public void resetScene(){
        sceneId = "";
        sceneName = "";
    }
}
