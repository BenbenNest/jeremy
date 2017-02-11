
package com.jeremy.lychee.model.news;


import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.db.WeMediaTopic;
import com.jeremy.lychee.model.live.LiveVideoInfo;

import java.util.List;

/**
 * 新闻相关信息
 *
 * @author zhangying-pd
 */
public class NewsRelated {
    private String transmit_num;
    private List<TransmitData> transmit_data;
    /**
     * 相关新闻信息
     */
    private List<NewsListData> related;
    private List<NewsListData> hotNewsList;
    private RelatedMedia media;
    private List<LiveVideoInfo> related_video;
    private List<WeMediaTopic> topic;


    public String getTransmit_num() {
        return transmit_num;
    }

    public void setTransmit_num(String transmit_num) {
        this.transmit_num = transmit_num;
    }

    public List<TransmitData> getTransmit_data() {
        return transmit_data;
    }

    public void setTransmit_data(List<TransmitData> transmit_data) {
        this.transmit_data = transmit_data;
    }

    public List<NewsListData> getRelated() {
        return related;
    }

    public void setRelated(List<NewsListData> related) {
        this.related = related;
    }

    public List<NewsListData> getHotNewsList() {
        return hotNewsList;
    }

    public void setHotNewsList(List<NewsListData> hotNewsList) {
        this.hotNewsList = hotNewsList;
    }

    public RelatedMedia getMedia() {
        return media;
    }

    public void setMedia(RelatedMedia media) {
        this.media = media;
    }

    public List<LiveVideoInfo> getRelated_video() {
        return related_video;
    }

    public void setRelated_video(List<LiveVideoInfo> related_video) {
        this.related_video = related_video;
    }

    public List<WeMediaTopic> getTopic() {
        return topic;
    }

    public void setTopic(List<WeMediaTopic> topic) {
        this.topic = topic;
    }
}
