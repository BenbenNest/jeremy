package com.jeremy.lychee.model.live;

import java.io.Serializable;
import java.util.List;

/**
 * Created by daibangwen-xy on 15/12/30.
 * 用Serializable  主要为了传递list对象
 */
public class TVList implements Serializable {
    private String channel_cid;
    private String channel_cicon;
    private boolean isShowIconBelow;
    private String channel_cname;
    private String channel_ctype;

    public String getChannel_ctype() {
        return channel_ctype;
    }

    public void setChannel_ctype(String channel_ctype) {
        this.channel_ctype = channel_ctype;
    }

    private List<ColumnChannel> subs;

    public String getChannel_cid() {
        return channel_cid;
    }

    public void setChannel_cid(String channel_cid) {
        this.channel_cid = channel_cid;
    }

    public String getChannel_cicon() {
        return channel_cicon;
    }

    public void setChannel_cicon(String channel_cicon) {
        this.channel_cicon = channel_cicon;
    }

    public boolean isShowIconBelow() {
        return isShowIconBelow;
    }

    public void setIsShowIconBelow(boolean isShowIconBelow) {
        this.isShowIconBelow = isShowIconBelow;
    }

    public String getChannel_cname() {
        return channel_cname;
    }

    public void setChannel_cname(String channel_cname) {
        this.channel_cname = channel_cname;
    }

    public List<ColumnChannel> getSubs() {
        return subs;
    }

    public void setSubs(List<ColumnChannel> subs) {
        this.subs = subs;
    }
/*
    @Override
    public int describeContents() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.channel_cid);
        dest.writeString(this.channel_cicon);
        dest.writeByte(isShowIconBelow ? (byte) 1 : (byte) 0);
        dest.writeString(this.channel_cname);
        dest.writeList(this.subs);
    }

    public TVList() {
    }

    protected TVList(Parcel in) {
        this.channel_cid = in.readString();
        this.channel_cicon = in.readString();
        this.isShowIconBelow = in.readByte() != 0;
        this.channel_cname = in.readString();
        this.subs = new ArrayList<ColumnChannel>();
        in.readList(this.subs, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<TVList> CREATOR = new Parcelable.Creator<TVList>() {
        public TVList createFromParcel(Parcel source) {
            return new TVList(source);
        }

        public TVList[] newArray(int size) {
            return new TVList[size];
        }
    };*/
}
