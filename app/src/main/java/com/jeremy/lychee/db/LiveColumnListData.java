package com.jeremy.lychee.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table LIVE_COLUMN_LIST_DATA.
 */
public class LiveColumnListData {

    /** Not-null value. */
    private String id;
    private String video_channel_name;
    private String video_name;
    private String video_column_name;
    private String video_desc;
    private String video_icon;
    private String video_img;
    private String video_islive;
    private String video_relate_id;
    private String video_play_url;
    private String tag;
    private String pdate;
    private String video_duration;
    private String video_type;
    private String video_publish_status;
    private String channel_cid;
    private String column_id;
    private Long time;
    private String news_type;
    private String news_from;

    public LiveColumnListData() {
    }

    public LiveColumnListData(String id) {
        this.id = id;
    }

    public LiveColumnListData(String id, String video_channel_name, String video_name, String video_column_name, String video_desc, String video_icon, String video_img, String video_islive, String video_relate_id, String video_play_url, String tag, String pdate, String video_duration, String video_type, String video_publish_status, String channel_cid, String column_id, Long time, String news_type, String news_from) {
        this.id = id;
        this.video_channel_name = video_channel_name;
        this.video_name = video_name;
        this.video_column_name = video_column_name;
        this.video_desc = video_desc;
        this.video_icon = video_icon;
        this.video_img = video_img;
        this.video_islive = video_islive;
        this.video_relate_id = video_relate_id;
        this.video_play_url = video_play_url;
        this.tag = tag;
        this.pdate = pdate;
        this.video_duration = video_duration;
        this.video_type = video_type;
        this.video_publish_status = video_publish_status;
        this.channel_cid = channel_cid;
        this.column_id = column_id;
        this.time = time;
        this.news_type = news_type;
        this.news_from = news_from;
    }

    /** Not-null value. */
    public String getId() {
        return id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_channel_name() {
        return video_channel_name;
    }

    public void setVideo_channel_name(String video_channel_name) {
        this.video_channel_name = video_channel_name;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_column_name() {
        return video_column_name;
    }

    public void setVideo_column_name(String video_column_name) {
        this.video_column_name = video_column_name;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public String getVideo_icon() {
        return video_icon;
    }

    public void setVideo_icon(String video_icon) {
        this.video_icon = video_icon;
    }

    public String getVideo_img() {
        return video_img;
    }

    public void setVideo_img(String video_img) {
        this.video_img = video_img;
    }

    public String getVideo_islive() {
        return video_islive;
    }

    public void setVideo_islive(String video_islive) {
        this.video_islive = video_islive;
    }

    public String getVideo_relate_id() {
        return video_relate_id;
    }

    public void setVideo_relate_id(String video_relate_id) {
        this.video_relate_id = video_relate_id;
    }

    public String getVideo_play_url() {
        return video_play_url;
    }

    public void setVideo_play_url(String video_play_url) {
        this.video_play_url = video_play_url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getVideo_publish_status() {
        return video_publish_status;
    }

    public void setVideo_publish_status(String video_publish_status) {
        this.video_publish_status = video_publish_status;
    }

    public String getChannel_cid() {
        return channel_cid;
    }

    public void setChannel_cid(String channel_cid) {
        this.channel_cid = channel_cid;
    }

    public String getColumn_id() {
        return column_id;
    }

    public void setColumn_id(String column_id) {
        this.column_id = column_id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getNews_type() {
        return news_type;
    }

    public void setNews_type(String news_type) {
        this.news_type = news_type;
    }

    public String getNews_from() {
        return news_from;
    }

    public void setNews_from(String news_from) {
        this.news_from = news_from;
    }

}
