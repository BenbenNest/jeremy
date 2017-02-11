package com.jeremy.lychee.model.live;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
public class ColumnLiveList implements Parcelable {
    private String name;
    private String type;
    private List<ColumnItem> list;
    private  boolean isLoadFour;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnItem> getList() {
        return list;
    }

    public void setList(List<ColumnItem> list) {
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLoadFour() {
        return isLoadFour;
    }

    public void setIsLoadFour(boolean isLoadFour) {
        this.isLoadFour = isLoadFour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeTypedList(list);
        dest.writeByte(isLoadFour ? (byte) 1 : (byte) 0);
    }

    public ColumnLiveList() {
    }

    protected ColumnLiveList(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.list = in.createTypedArrayList(ColumnItem.CREATOR);
        this.isLoadFour = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ColumnLiveList> CREATOR = new Parcelable.Creator<ColumnLiveList>() {
        public ColumnLiveList createFromParcel(Parcel source) {
            return new ColumnLiveList(source);
        }

        public ColumnLiveList[] newArray(int size) {
            return new ColumnLiveList[size];
        }
    };
}
