package com.jeremy.lychee.adapter.news;

import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.model.news.Topic;

import java.util.List;

/**
 * Created by chengyajun on 2016/3/16.
 */
public class SubjectTopItem {
    private NewsListData news;
    private String title;
    private String summary;
    private List<Topic> topic;
    private String image;
    private String type;
    private String url;

    public SubjectTopItem(NewsListData news, String title, String summary, List<Topic> topic,String image,String type,String url) {
        this.news = news;
        this.title = title;
        this.summary = summary;
        this.topic = topic;
        this.image = image;
        this.type = type;
        this.url = url;

    }

    public NewsListData getNews() {
        return news;
    }

    public void setNews(NewsListData news) {
        this.news = news;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Topic> getTopic() {
        return topic;
    }

    public void setTopic(List<Topic> topic) {
        this.topic = topic;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
