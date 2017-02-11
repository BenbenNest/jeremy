package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
public class VideoColumn implements Parcelable {
    private String video_column_id;
    private String video_column_name;
    private String video_column_time;
    private boolean column_history;

    public boolean isColumn_history() {
        return column_history;
    }

    public void setColumn_history(boolean column_history) {
        this.column_history = column_history;
    }

    public String getVideo_column_time() {
        return video_column_time;
    }

    public void setVideo_column_time(String video_column_time) {
        this.video_column_time = video_column_time;
    }

    public String getVideo_column_name() {
        return video_column_name;
    }

    public void setVideo_column_name(String video_column_name) {
        this.video_column_name = video_column_name;
    }

    public String getVideo_column_id() {
        return video_column_id;
    }

    public void setVideo_column_id(String video_column_id) {
        this.video_column_id = video_column_id;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.video_column_id);
        dest.writeString(this.video_column_name);
        dest.writeString(this.video_column_time);
        dest.writeByte(column_history ? (byte) 1 : (byte) 0);
    }

    public VideoColumn() {
    }

    protected VideoColumn(Parcel in) {
        this.video_column_id = in.readString();
        this.video_column_name = in.readString();
        this.video_column_time = in.readString();
        this.column_history = in.readByte() != 0;
    }

    public static final Parcelable.Creator<VideoColumn> CREATOR = new Parcelable.Creator<VideoColumn>() {
        public VideoColumn createFromParcel(Parcel source) {
            return new VideoColumn(source);
        }

        public VideoColumn[] newArray(int size) {
            return new VideoColumn[size];
        }
    };
}
