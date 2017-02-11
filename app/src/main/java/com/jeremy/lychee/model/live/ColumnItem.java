package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
public class ColumnItem implements Parcelable {
    private String id;
    private String column_name;
    private String channel_id;
    private String column_image;
    private String video_type;
    private String tag;
    private String rec;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getColumn_image() {
        return column_image;
    }

    public void setColumn_image(String column_image) {
        this.column_image = column_image;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.column_name);
        dest.writeString(this.channel_id);
        dest.writeString(this.column_image);
        dest.writeString(this.video_type);
        dest.writeString(this.tag);
        dest.writeString(this.rec);
    }

    public ColumnItem() {
    }

    protected ColumnItem(Parcel in) {
        this.id = in.readString();
        this.column_name = in.readString();
        this.channel_id = in.readString();
        this.column_image = in.readString();
        this.video_type = in.readString();
        this.tag = in.readString();
        this.rec = in.readString();
    }

    public static final Creator<ColumnItem> CREATOR = new Creator<ColumnItem>() {
        public ColumnItem createFromParcel(Parcel source) {
            return new ColumnItem(source);
        }

        public ColumnItem[] newArray(int size) {
            return new ColumnItem[size];
        }
    };
}
