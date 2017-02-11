package com.jeremy.lychee.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daibangwen-xy on 15/11/20.
 */
public class MyFans implements Parcelable {
    private String uid;
    private String nickname;
    private String userpic;
    private String transmit_num;
    private String likes;

    public MyFans(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getTransmit_num() {
        return transmit_num;
    }

    public void setTransmit_num(String transmit_num) {
        this.transmit_num = transmit_num;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String ding) {
        this.likes = ding;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MyFans(Parcel in) {
        this.uid = in.readString();
        this.nickname = in.readString();
        this.userpic = in.readString();
        this.transmit_num = in.readString();
        this.likes = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.userpic);
        dest.writeString(this.transmit_num);
        dest.writeString(this.likes);
    }
    public static final Parcelable.Creator<MyFans> CREATOR = new Parcelable.Creator<MyFans>() {
        public MyFans createFromParcel(Parcel source) {
            return new MyFans(source);
        }

        public MyFans[] newArray(int size) {
            return new MyFans[size];
        }
    };
}
