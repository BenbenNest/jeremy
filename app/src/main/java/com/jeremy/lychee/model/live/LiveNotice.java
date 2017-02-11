package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daibangwen-xy on 16/1/30.
 */
public class LiveNotice implements Parcelable {
    private String text;
    private String start_time;
    private String end_time;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
    }

    public LiveNotice() {
    }

    protected LiveNotice(Parcel in) {
        this.text = in.readString();
        this.start_time = in.readString();
        this.end_time = in.readString();
    }

    public static final Parcelable.Creator<LiveNotice> CREATOR = new Parcelable.Creator<LiveNotice>() {
        public LiveNotice createFromParcel(Parcel source) {
            return new LiveNotice(source);
        }

        public LiveNotice[] newArray(int size) {
            return new LiveNotice[size];
        }
    };
}
