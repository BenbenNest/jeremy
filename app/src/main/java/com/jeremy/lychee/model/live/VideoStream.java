package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daibangwen-xy on 15/12/25.
 */
public class VideoStream implements Parcelable {
    private String stream_vbt;
    private String stream_url;
    private int stream_type;

    public String getStream_vbt() {
        return stream_vbt;
    }

    public void setStream_vbt(String stream_vbt) {
        this.stream_vbt = stream_vbt;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String tream_url) {
        this.stream_url = tream_url;
    }

    public int getStream_type() {
        return stream_type;
    }

    public void setStream_type(int stream_type) {
        this.stream_type = stream_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stream_vbt);
        dest.writeString(this.stream_url);
        dest.writeInt(this.stream_type);
    }

    public VideoStream() {
    }

    protected VideoStream(Parcel in) {
        this.stream_vbt = in.readString();
        this.stream_url = in.readString();
        this.stream_type = in.readInt();
    }

    public static final Parcelable.Creator<VideoStream> CREATOR = new Parcelable.Creator<VideoStream>() {
        public VideoStream createFromParcel(Parcel source) {
            return new VideoStream(source);
        }

        public VideoStream[] newArray(int size) {
            return new VideoStream[size];
        }
    };
}
