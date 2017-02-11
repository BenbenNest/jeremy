package com.jeremy.lychee.model.news;

import com.jeremy.lychee.db.NewsListData;

import java.util.List;

/**
 * Created by zhangying-pd on 2016/1/22.
 * 新闻相关数据中 频道数据
 */
public class RelatedMedia {
    private int id;
    private String icon;
    private String name;
    private String summary;
    private boolean issub;
    private boolean is_my = true;
    private List<NewsListData> news;

    public boolean is_my() {
        return is_my;
    }

    public void setIs_my(boolean is_my) {
        this.is_my = is_my;
    }

    public boolean issub() {
        return issub;
    }

    public void setIssub(boolean issub) {
        this.issub = issub;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<NewsListData> getNews() {
        return news;
    }

    public void setNews(List<NewsListData> news) {
        this.news = news;
    }
}
