package com.jeremy.lychee.model.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 转推数据model
 * Created by zhangying-pd on 2015/11/30.
 */
public class TransmitData implements Parcelable {
    private String sub_id;
    private String name;
    private String icon;
    private String news_num;
    private String backimg;
    private String sub_num;
    private String ding;
    private String is_sub;

    public static final Creator<TransmitData> CREATOR = new Creator<TransmitData>() {
        @Override
        public TransmitData createFromParcel(Parcel in) {
            return new TransmitData(in);
        }

        @Override
        public TransmitData[] newArray(int size) {
            return new TransmitData[size];
        }
    };

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNews_num() {
        return news_num;
    }

    public void setNews_num(String news_num) {
        this.news_num = news_num;
    }

    public String getBackimg() {
        return backimg;
    }

    public void setBackimg(String backimg) {
        this.backimg = backimg;
    }

    public String getSub_num() {
        return sub_num;
    }

    public void setSub_num(String sub_num) {
        this.sub_num = sub_num;
    }

    public String getDing() {
        return ding;
    }

    public void setDing(String ding) {
        this.ding = ding;
    }

    public String getIs_sub() {
        return is_sub;
    }

    public void setIs_sub(String is_sub) {
        this.is_sub = is_sub;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public TransmitData(Parcel in) {
        this.sub_id = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
        this.news_num = in.readString();
        this.backimg = in.readString();
        this.sub_num = in.readString();
        this.ding = in.readString();
        this.is_sub = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sub_id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.news_num);
        dest.writeString(this.backimg);
        dest.writeSerializable(this.sub_num);
        dest.writeString(this.ding);
        dest.writeString(this.is_sub);
    }
}
