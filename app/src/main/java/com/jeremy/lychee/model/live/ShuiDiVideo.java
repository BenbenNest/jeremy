package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daibangwen-xy on 16/3/8.
 * 水滴直播实体
 */
public class ShuiDiVideo implements Parcelable {
    private String id;
    private String video_type;
    private String video_name;

    private String video_img;
    private String video_play_url;
    private Integer pdate;
    private String video_key;
    private String comment;
    private String watches;
    private String author_uid;
    private String author_nickname;
    private String author_img;
    private String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
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

    public String getVideo_play_url() {
        return video_play_url;
    }

    public void setVideo_play_url(String video_play_url) {
        this.video_play_url = video_play_url;
    }

    public Integer getPdate() {
        return pdate;
    }

    public void setPdate(Integer pdate) {
        this.pdate = pdate;
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

    public String getWatches() {
        return watches;
    }

    public void setWatches(String watches) {
        this.watches = watches;
    }

    public String getDing() {
        return ding;
    }

    public void setDing(String ding) {
        this.ding = ding;
    }

    private String ding;


    public String getAuthor_uid() {
        return author_uid;
    }

    public void setAuthor_uid(String author_uid) {
        this.author_uid = author_uid;
    }

    public String getAuthor_nickname() {
        return author_nickname;
    }

    public void setAuthor_nickname(String author_nickname) {
        this.author_nickname = author_nickname;
    }

    public String getAuthor_img() {
        return author_img;
    }

    public void setAuthor_img(String author_img) {
        this.author_img = author_img;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.video_type);
        dest.writeString(this.video_name);
        dest.writeString(this.video_img);
        dest.writeString(this.video_play_url);
        dest.writeValue(this.pdate);
        dest.writeString(this.video_key);
        dest.writeString(this.comment);
        dest.writeString(this.watches);
        dest.writeString(this.author_uid);
        dest.writeString(this.author_nickname);
        dest.writeString(this.author_img);
        dest.writeString(this.location);
        dest.writeString(this.ding);
    }

    public ShuiDiVideo() {
    }

    protected ShuiDiVideo(Parcel in) {
        this.id = in.readString();
        this.video_type = in.readString();
        this.video_name = in.readString();
        this.video_img = in.readString();
        this.video_play_url = in.readString();
        this.pdate = (Integer) in.readValue(Integer.class.getClassLoader());
        this.video_key = in.readString();
        this.comment = in.readString();
        this.watches = in.readString();
        this.author_uid = in.readString();
        this.author_nickname = in.readString();
        this.author_img = in.readString();
        this.location = in.readString();
        this.ding = in.readString();
    }

    public static final Creator<ShuiDiVideo> CREATOR = new Creator<ShuiDiVideo>() {
        public ShuiDiVideo createFromParcel(Parcel source) {
            return new ShuiDiVideo(source);
        }

        public ShuiDiVideo[] newArray(int size) {
            return new ShuiDiVideo[size];
        }
    };
}
