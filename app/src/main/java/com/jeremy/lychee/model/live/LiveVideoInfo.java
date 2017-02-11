package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

public class LiveVideoInfo implements Parcelable {

    /**
     * id : 1
     * video_channel_name : BTV北京卫视高清
     * video_column_id : 0
     * video_column_name : 《好梦剧场》38集剧：出生入死（30）
     * video_type : 1
     * video_desc : BTV北京卫视高清
     * video_name : 卫视
     * video_icon : http://p1.qhimg.com/dmfd/__70/t0111f629f9f65a7618.png?size=76x76&zoom_out=60
     * video_publish_status : 1
     * video_img : http://p7.qhimg.com/dmfd/__70/t01aa549a4a2778eaa4.jpg?size=640x480&zoom_out=60
     * video_islive : 2
     * video_play_url : http://10.117.83.51:8001//video/testplay?id=1&segid=0
     * tag :
     * is_seg : 0
     * stream_type : 5
     * video_key : lianxian_video_1
     * comment : 19
     * ding : 5
     * share : http://10.117.83.51:8001//detail/video?id=1&type_id=1
     */

    private String id;
    private String video_channel_name;
    private String video_column_id;
    private String video_column_name;
    private String video_type;
    private String video_desc;
    private String video_name;
    private String video_icon;
    private String video_duration;
    private String video_publish_status;
    private String video_img;
    private String video_islive;
    private String video_play_url;
    private long pdate;
    private String tag;
    private String is_seg;
    private int stream_type;
    private String video_key;
    private String comment;
    private String ding;
    private String share;
    private String news_type;
    private String news_from;

    public long getPdate() {
        return pdate;
    }

    public void setPdate(long pdate) {
        this.pdate = pdate;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public void setVideo_channel_name(String video_channel_name) {
        this.video_channel_name = video_channel_name;
    }

    public void setVideo_column_id(String video_column_id) {
        this.video_column_id = video_column_id;
    }

    public void setVideo_column_name(String video_column_name) {
        this.video_column_name = video_column_name;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public void setVideo_desc(String video_desc) {
        this.video_desc = video_desc;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public void setVideo_icon(String video_icon) {
        this.video_icon = video_icon;
    }

    public void setVideo_publish_status(String video_publish_status) {
        this.video_publish_status = video_publish_status;
    }

    public void setVideo_img(String video_img) {
        this.video_img = video_img;
    }

    public void setVideo_islive(String video_islive) {
        this.video_islive = video_islive;
    }

    public void setVideo_play_url(String video_play_url) {
        this.video_play_url = video_play_url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setIs_seg(String is_seg) {
        this.is_seg = is_seg;
    }

    public void setStream_type(int stream_type) {
        this.stream_type = stream_type;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDing(String ding) {
        this.ding = ding;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getId() {
        return id;
    }

    public String getVideo_channel_name() {
        return video_channel_name;
    }

    public String getVideo_column_id() {
        return video_column_id;
    }

    public String getVideo_column_name() {
        return video_column_name;
    }

    public String getVideo_type() {
        return video_type;
    }

    public String getVideo_desc() {
        return video_desc;
    }

    public String getVideo_name() {
        return video_name;
    }

    public String getVideo_icon() {
        return video_icon;
    }

    public String getVideo_publish_status() {
        return video_publish_status;
    }

    public String getVideo_img() {
        return video_img;
    }

    public String getVideo_islive() {
        return video_islive;
    }

    public String getVideo_play_url() {
        return video_play_url;
    }

    public String getTag() {
        return tag;
    }

    public String getIs_seg() {
        return is_seg;
    }

    public int getStream_type() {
        return stream_type;
    }

    public String getVideo_key() {
        return video_key;
    }

    public String getComment() {
        return comment;
    }

    public String getDing() {
        return ding;
    }

    public String getShare() {
        return share;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.video_channel_name);
        dest.writeString(this.video_column_id);
        dest.writeString(this.video_column_name);
        dest.writeString(this.video_type);
        dest.writeString(this.video_desc);
        dest.writeString(this.video_name);
        dest.writeString(this.video_icon);
        dest.writeString(this.video_publish_status);
        dest.writeString(this.video_img);
        dest.writeString(this.video_islive);
        dest.writeString(this.video_play_url);
        dest.writeString(this.tag);
        dest.writeString(this.is_seg);
        dest.writeInt(this.stream_type);
        dest.writeString(this.video_key);
        dest.writeString(this.comment);
        dest.writeString(this.ding);
        dest.writeString(this.share);
        dest.writeString(this.news_type);
        dest.writeString(this.news_from);
        dest.writeLong(this.pdate);
    }

    public LiveVideoInfo() {
    }

    protected LiveVideoInfo(Parcel in) {
        this.id = in.readString();
        this.video_channel_name = in.readString();
        this.video_column_id = in.readString();
        this.video_column_name = in.readString();
        this.video_type = in.readString();
        this.video_desc = in.readString();
        this.video_name = in.readString();
        this.video_icon = in.readString();
        this.video_publish_status = in.readString();
        this.video_img = in.readString();
        this.video_islive = in.readString();
        this.video_play_url = in.readString();
        this.tag = in.readString();
        this.is_seg = in.readString();
        this.stream_type = in.readInt();
        this.video_key = in.readString();
        this.comment = in.readString();
        this.ding = in.readString();
        this.share = in.readString();
        this.news_type = in.readString();
        this.news_from = in.readString();
        this.pdate = in.readLong();
    }

    public static final Parcelable.Creator<LiveVideoInfo> CREATOR = new Parcelable.Creator<LiveVideoInfo>() {
        public LiveVideoInfo createFromParcel(Parcel source) {
            return new LiveVideoInfo(source);
        }

        public LiveVideoInfo[] newArray(int size) {
            return new LiveVideoInfo[size];
        }
    };
}
