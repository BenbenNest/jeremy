
package com.jeremy.lychee.model.news;

import android.os.Parcel;
import android.os.Parcelable;

public class Suggest implements Parcelable {
    private String q;
    private String p;
    private String[] s;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String[] getS() {
        return s;
    }

    public void setS(String[] s) {
        this.s = s;
    }

    public Suggest(Parcel in) {
        q = in.readString();
        p = in.readString();
        in.readStringArray(s);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(q);
        dest.writeString(p);
        dest.writeStringArray(s);
    }

    public static final Creator<Suggest> CREATOR = new Creator<Suggest>() {
        public Suggest createFromParcel(Parcel in) {
            return new Suggest(in);
        }

        public Suggest[] newArray(int size) {
            return new Suggest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
