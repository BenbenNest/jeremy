
package com.jeremy.lychee.model.news;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsContent implements Parcelable {

    private String type;
    private String subtype;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.subtype);
        dest.writeString(this.value);
    }

    public NewsContent() {
    }

    private NewsContent(Parcel in) {
        this.type = in.readString();
        this.subtype = in.readString();
        this.value = in.readString();
    }

    public static final Creator<NewsContent> CREATOR = new Creator<NewsContent>() {
        public NewsContent createFromParcel(Parcel source) {
            return new NewsContent(source);
        }

        public NewsContent[] newArray(int size) {
            return new NewsContent[size];
        }
    };
}
