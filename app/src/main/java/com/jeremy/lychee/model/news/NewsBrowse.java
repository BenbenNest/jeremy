package com.jeremy.lychee.model.news;

import com.jeremy.lychee.db.NewsListData;

import java.util.List;

public class NewsBrowse {

    private float percent;
    private int slidingCnt;
    private String screenPos;
    private List<NewsListData> showedRelatedList;
    private List<NewsListData> showedHotList;
    private NewsListData clickNews;

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public void setSlidingCnt(int slidingCnt) {
        this.slidingCnt = slidingCnt;
    }

    public void setScreenPos(String screenPos) {
        this.screenPos = screenPos;
    }

    public void setShowedRelatedList(List<NewsListData> showedRelatedList) {
        this.showedRelatedList = showedRelatedList;
    }

    public void setShowedHotList(List<NewsListData> showedHotList) {
        this.showedHotList = showedHotList;
    }


    public float getPercent() {

        return percent;
    }

    public int getSlidingCnt() {
        return slidingCnt;
    }

    public String getScreenPos() {
        return screenPos;
    }

    public List<NewsListData> getShowedRelatedList() {
        return showedRelatedList;
    }

    public List<NewsListData> getShowedHotList() {
        return showedHotList;
    }

    public void setClickNews(NewsListData clickNews) {
        this.clickNews = clickNews;
    }

    public NewsListData getClickNews() {
        return clickNews;
    }
}
