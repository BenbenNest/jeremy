package com.jeremy.lychee.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.jeremy.lychee.db.NewsListData;

/**
 * Created by zhaozuotong on 2015/11/27.
 */
public class NewsListDataWrapper implements Parcelable {
    private String nid;
    private String original_nid;
    private String channel;
    private Long time;
    private String id;
    private String md5;
    private String cid;
    private String album_pic;
    private String title;
    private String zm;
    private String url;
    private String comment;
    private String pdate;
    private String source;
    private String module;

    private String share;
    private String news_from;
    private String open_type;
    private String news_stick;
    private String news_stick_time;
    private String transmit_num;
    private String new_type;
    private String news_data;


    private String live_channel_id;
    private String live_topic_id;
    private String live_channel_tag;
    private String feed_id;



    public NewsListDataWrapper(NewsListData data){
        this.nid = data.getNid();
        this.channel = data.getChannel();
        this.time = data.getTime();
        this.id = data.getId();
        this.md5 = data.getMd5();
        this.cid = data.getCid();
        this.album_pic = data.getAlbum_pic();
        this.title = data.getTitle();
        this.zm = data.getZm();
        this.url = data.getUrl();
        this.comment = data.getComment();
        this.pdate = data.getPdate();
        this.source = data.getSource();
        this.module = data.getModule();
        this.share = data.getShare();
        this.news_from = data.getNews_from();
        this.open_type = data.getOpen_type();
        this.news_stick = data.getNews_stick();
        this.news_stick_time = data.getNews_stick_time();
        this.transmit_num = data.getTransmit_num();
        this.live_channel_id = data.getLive_channel_id();
        this.new_type = data.getNews_type();
        this.news_data = data.getNews_data();
        this.live_topic_id = data.getLive_topic_id();
        this.live_channel_tag = data.getLive_topic_id();
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getNid() {
        return nid;
    }

    public String getChannel() {
        return channel;
    }

    public Long getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public String getMd5() {
        return md5;
    }

    public String getCid() {
        return cid;
    }

    public String getAlbum_pic() {
        return album_pic;
    }

    public String getTitle() {
        return title;
    }

    public String getZm() {
        return zm;
    }

    public String getUrl() {
        return url;
    }

    public String getComment() {
        return comment;
    }

    public String getPdate() {
        return pdate;
    }

    public String getSource() {
        return source;
    }

    public String getModule() {
        return module;
    }

    public String getShare() {
        return share;
    }

    public String getTransmit_num() {
        return transmit_num;
    }

    public String getLive_channel_tag() {
        return live_channel_tag;
    }

    public String getLive_topic_id() {
        return live_topic_id;
    }

    public String getLive_channel_id() {
        return live_channel_id;
    }

    public String getNew_type() {
        return new_type;
    }

    public void setNew_type(String new_type) {
        this.new_type = new_type;
    }

    public String getNews_data() {
        return news_data;
    }

    public void setNews_data(String news_data) {
        this.news_data = news_data;
    }

    public String getNews_from() {
        return news_from;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nid);
        dest.writeString(this.original_nid);
        dest.writeString(this.channel);
        dest.writeValue(this.time);
        dest.writeString(this.id);
        dest.writeString(this.md5);
        dest.writeString(this.cid);
        dest.writeString(this.album_pic);
        dest.writeString(this.title);
        dest.writeString(this.zm);
        dest.writeString(this.url);
        dest.writeString(this.comment);
        dest.writeString(this.pdate);
        dest.writeString(this.source);
        dest.writeString(this.module);
        dest.writeString(this.share);
        dest.writeString(this.news_from);
        dest.writeString(this.open_type);
        dest.writeString(this.news_stick);
        dest.writeString(this.news_stick_time);
        dest.writeString(this.transmit_num);
        dest.writeString(this.new_type);
        dest.writeString(this.news_data);
        dest.writeString(this.live_channel_id);
        dest.writeString(this.live_topic_id);
        dest.writeString(this.live_channel_tag);
    }

    protected NewsListDataWrapper(Parcel in) {
        this.nid = in.readString();
        this.original_nid = in.readString();
        this.channel = in.readString();
        this.time = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readString();
        this.md5 = in.readString();
        this.cid = in.readString();
        this.album_pic = in.readString();
        this.title = in.readString();
        this.zm = in.readString();
        this.url = in.readString();
        this.comment = in.readString();
        this.pdate = in.readString();
        this.source = in.readString();
        this.module = in.readString();
        this.share = in.readString();
        this.news_from = in.readString();
        this.open_type = in.readString();
        this.news_stick = in.readString();
        this.news_stick_time = in.readString();
        this.transmit_num = in.readString();
        this.new_type = in.readString();
        this.news_data = in.readString();
        this.live_channel_id = in.readString();
        this.live_topic_id = in.readString();
        this.live_channel_tag = in.readString();
    }

    public static final Parcelable.Creator<NewsListDataWrapper> CREATOR = new Parcelable.Creator<NewsListDataWrapper>() {
        public NewsListDataWrapper createFromParcel(Parcel source) {
            return new NewsListDataWrapper(source);
        }

        public NewsListDataWrapper[] newArray(int size) {
            return new NewsListDataWrapper[size];
        }
    };
}
