package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
public class LiveChannelList implements Parcelable {
    private String name;
    private String type;
    private List<LiveChannel> list;
    private  boolean isLoadFour;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeList(this.list);
        dest.writeByte(isLoadFour ? (byte) 1 : (byte) 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LiveChannel> getList() {
        return list;
    }

    public void setList(List<LiveChannel> list) {
        this.list = list;
    }

    public boolean isLoadFour() {
        return isLoadFour;
    }

    public void setIsLoadFour(boolean isLoadFour) {
        this.isLoadFour = isLoadFour;
    }

    public LiveChannelList() {
    }

    protected LiveChannelList(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.list = new ArrayList<LiveChannel>();
        in.readList(this.list, List.class.getClassLoader());
        this.isLoadFour = in.readByte() != 0;
    }

    public static final Parcelable.Creator<LiveChannelList> CREATOR = new Parcelable.Creator<LiveChannelList>() {
        public LiveChannelList createFromParcel(Parcel source) {
            return new LiveChannelList(source);
        }

        public LiveChannelList[] newArray(int size) {
            return new LiveChannelList[size];
        }
    };
}
