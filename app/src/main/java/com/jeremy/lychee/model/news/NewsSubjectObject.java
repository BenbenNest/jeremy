package com.jeremy.lychee.model.news;

import com.jeremy.lychee.db.NewsListData;

import java.util.List;

/**
 * Created by chengyajun on 2016/3/16.
 */
public class NewsSubjectObject {
    private String id;
    private String title;
    private String summary;
    private String image;
    private String type;
    private String url;

    private NewsListData news;
    private List<Topic> topic;
    private List<SubjectColumnGroup> lanmu;
    private String share;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public NewsListData getNews() {
        return news;
    }

    public void setNews(NewsListData news) {
        this.news = news;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public List<Topic> getTopic() {
        return topic;
    }

    public void setTopic(List<Topic> topic) {
        this.topic = topic;
    }

    public List<SubjectColumnGroup> getLanmu() {
        return lanmu;
    }

    public void setLanmu(List<SubjectColumnGroup> lanmu) {
        this.lanmu = lanmu;
    }
}
