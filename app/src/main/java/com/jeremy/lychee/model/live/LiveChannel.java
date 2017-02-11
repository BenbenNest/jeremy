package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

public class LiveChannel implements Parcelable {

    /**
     * 增加一个type
     * id : 73
     * video_channel_name :
     * video_name : 互联网第一现场
     * video_column_name : 玩物 2015年青葱新品发布会
     * video_desc :
     * video_icon : http://image.huajiao.com/30e736dd6e6b735abd065e3224102403-200_200.jpg
     * video_img : http://image.huajiao.com/bbe3da475e160d2bf2668e9f95adc0e8.jpg
     * video_islive : 0
     * video_relate_id : 2274562
     * video_play_url : http://10.117.83.51:8001/Video/play?id=73&type_id=2&relate_id=2274562
     * tag : 互联网第一现场
     */

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
    private String watches;
    private String news_type;
    private String news_from;

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

    public String getWatches() {
        return watches;
    }

    public void setWatches(String watches) {
        this.watches = watches;
    }

    public String getVideo_column_id() {
        return video_column_id;
    }

    public void setVideo_column_id(String video_column_id) {
        this.video_column_id = video_column_id;
    }

    private String video_column_id;

    public String getChannel_cid() {
        return channel_cid;
    }

    public void setChannel_cid(String channel_cid) {
        this.channel_cid = channel_cid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVideo_channel_name(String video_channel_name) {
        this.video_channel_name = video_channel_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public void setVideo_column_name(String video_column_name) {
        this.video_column_name = video_column_name;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public void setVideo_icon(String video_icon) {
        this.video_icon = video_icon;
    }

    public void setVideo_img(String video_img) {
        this.video_img = video_img;
    }

    public void setVideo_islive(String video_islive) {
        this.video_islive = video_islive;
    }

    public void setVideo_relate_id(String video_relate_id) {
        this.video_relate_id = video_relate_id;
    }

    public void setVideo_play_url(String video_play_url) {
        this.video_play_url = video_play_url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public String getVideo_channel_name() {
        return video_channel_name;
    }

    public String getVideo_name() {
        return video_name;
    }

    public String getVideo_column_name() {
        return video_column_name;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public String getVideo_icon() {
        return video_icon;
    }

    public String getVideo_img() {
        return video_img;
    }

    public String getVideo_islive() {
        return video_islive;
    }

    public String getVideo_relate_id() {
        return video_relate_id;
    }

    public String getVideo_play_url() {
        return video_play_url;
    }

    public String getTag() {
        return tag;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.video_channel_name);
        dest.writeString(this.video_name);
        dest.writeString(this.video_column_name);
        dest.writeString(this.video_desc);
        dest.writeString(this.video_icon);
        dest.writeString(this.video_img);
        dest.writeString(this.video_islive);
        dest.writeString(this.video_relate_id);
        dest.writeString(this.video_play_url);
        dest.writeString(this.tag);
        dest.writeString(this.pdate);
        dest.writeString(this.video_duration);
        dest.writeString(this.video_type);
        dest.writeString(this.video_publish_status);
        dest.writeString(this.channel_cid);
        dest.writeString(this.news_type);
        dest.writeString(this.news_from);
    }

    public LiveChannel() {
    }

    protected LiveChannel(Parcel in) {
        this.id = in.readString();
        this.video_channel_name = in.readString();
        this.video_name = in.readString();
        this.video_column_name = in.readString();
        this.video_desc = in.readString();
        this.video_icon = in.readString();
        this.video_img = in.readString();
        this.video_islive = in.readString();
        this.video_relate_id = in.readString();
        this.video_play_url = in.readString();
        this.tag = in.readString();
        this.pdate = in.readString();
        this.video_duration = in.readString();
        this.video_type = in.readString();
        this.video_publish_status = in.readString();
        this.channel_cid = in.readString();
        this.news_type = in.readString();
        this.news_from = in.readString();
    }

    public static final Parcelable.Creator<LiveChannel> CREATOR = new Parcelable.Creator<LiveChannel>() {
        public LiveChannel createFromParcel(Parcel source) {
            return new LiveChannel(source);
        }

        public LiveChannel[] newArray(int size) {
            return new LiveChannel[size];
        }
    };
}
