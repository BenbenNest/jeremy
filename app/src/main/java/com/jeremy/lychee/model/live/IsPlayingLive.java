package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
public class IsPlayingLive implements Parcelable {
    private String id;
    private String video_icon;
    private String video_channel_name;
    private String video_name;
    private String video_img;
    private String tag;
    //private VideoColumn video_column;
    private String type;
    //private List<Program> video_next_column;
    //private VideoStream video_stream;
    private String video_type;
    private String online;
   // private LiveNotice notice;
    private String video_key;
    private String comment;
    private String ding;
    private String share;

    private String title;
    private String summary;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_icon() {
        return video_icon;
    }

    public void setVideo_icon(String video_icon) {
        this.video_icon = video_icon;
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

    public String getVideo_img() {
        return video_img;
    }

    public void setVideo_img(String video_img) {
        this.video_img = video_img;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }



    public String getVideo_key() {
        return video_key;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDing() {
        return ding;
    }

    public void setDing(String ding) {
        this.ding = ding;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.video_icon);
        dest.writeString(this.video_channel_name);
        dest.writeString(this.video_name);
        dest.writeString(this.video_img);
        dest.writeString(this.tag);
        dest.writeString(this.type);
        dest.writeString(this.video_type);
        dest.writeString(this.online);
        dest.writeString(this.video_key);
        dest.writeString(this.comment);
        dest.writeString(this.ding);
        dest.writeString(this.share);
        dest.writeString(this.title);
        dest.writeString(this.summary);
    }

    public IsPlayingLive() {
    }

    protected IsPlayingLive(Parcel in) {
        this.id = in.readString();
        this.video_icon = in.readString();
        this.video_channel_name = in.readString();
        this.video_name = in.readString();
        this.video_img = in.readString();
        this.tag = in.readString();
        this.type = in.readString();
        this.video_type = in.readString();
        this.online = in.readString();
        this.video_key = in.readString();
        this.comment = in.readString();
        this.ding = in.readString();
        this.share = in.readString();
        this.title = in.readString();
        this.summary = in.readString();
    }

    public static final Creator<IsPlayingLive> CREATOR = new Creator<IsPlayingLive>() {
        public IsPlayingLive createFromParcel(Parcel source) {
            return new IsPlayingLive(source);
        }

        public IsPlayingLive[] newArray(int size) {
            return new IsPlayingLive[size];
        }
    };
}
