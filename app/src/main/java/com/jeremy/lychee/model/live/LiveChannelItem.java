package com.jeremy.lychee.model.live;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
/*
public class LiveChannelItem implements Parcelable {
    private String id;
    private String video_icon;
    private String video_channel_name;
    private String video_name;
    private String video_img;
    private String video_play_url;
    private String video_column_name;
    private String tag;
    private Integer video_type;

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

    public String getVideo_play_url() {
        return video_play_url;
    }

    public void setVideo_play_url(String video_play_url) {
        this.video_play_url = video_play_url;
    }

    public String getVideo_column_name() {
        return video_column_name;
    }

    public void setVideo_column_name(String video_column_name) {
        this.video_column_name = video_column_name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getVideo_type() {
        return video_type;
    }

    public void setVideo_type(Integer video_type) {
        this.video_type = video_type;
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
        dest.writeString(this.video_play_url);
        dest.writeString(this.video_column_name);
        dest.writeString(this.tag);
        dest.writeValue(this.video_type);
    }

    public LiveChannelItem() {
    }

    protected LiveChannelItem(Parcel in) {
        this.id = in.readString();
        this.video_icon = in.readString();
        this.video_channel_name = in.readString();
        this.video_name = in.readString();
        this.video_img = in.readString();
        this.video_play_url = in.readString();
        this.video_column_name = in.readString();
        this.tag = in.readString();
        this.video_type = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<LiveChannelItem> CREATOR = new Creator<LiveChannelItem>() {
        public LiveChannelItem createFromParcel(Parcel source) {
            return new LiveChannelItem(source);
        }

        public LiveChannelItem[] newArray(int size) {
            return new LiveChannelItem[size];
        }
    };
}
*/
