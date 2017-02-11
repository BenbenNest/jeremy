package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daibangwen-xy on 15/12/23.
 */
public class Program implements Parcelable {
    public String id;
    public String column_name;
    public String channel_name;
    public String start_time;
    public String end_time;
    public String is_live;
    public boolean is_sub;

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

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
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

    public String getIs_live() {
        return is_live;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
    }

    public boolean is_sub() {
        return is_sub;
    }

    public void setIs_sub(boolean is_sub) {
        this.is_sub = is_sub;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.column_name);
        dest.writeString(this.channel_name);
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
        dest.writeString(this.is_live);
        dest.writeByte(is_sub ? (byte) 1 : (byte) 0);
    }

    public Program() {
    }

    protected Program(Parcel in) {
        this.id = in.readString();
        this.column_name = in.readString();
        this.channel_name = in.readString();
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.is_live = in.readString();
        this.is_sub = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Program> CREATOR = new Parcelable.Creator<Program>() {
        public Program createFromParcel(Parcel source) {
            return new Program(source);
        }

        public Program[] newArray(int size) {
            return new Program[size];
        }
    };
}
