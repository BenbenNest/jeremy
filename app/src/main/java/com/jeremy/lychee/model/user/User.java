package com.jeremy.lychee.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zourongbo on 2015/11/19.
 */

// http://add.corp.qihoo.net/pages/viewpage.action?pageId=12890567
//        {
//        "errno": "0",
//        "data": {
//        "uid": "微博uid",
//        "nickname": "微博xxxx",
//        "sid": "13f49ae45q88r9dun49vobs076",
//        "userpic": "http://xxx.png",
//        "expire_time": 1445840030,
//        "real_uid": "10086",
//        "uid_type":"weibo",
//        "uname":"微博xxx",
//        }
//        }

public class User implements Parcelable {
    private String uid;
    private String nickname;
    private String sid;
    private String token;
    private String userpic;
    private long expire_time;
    private String real_uid;
    private String uid_type;
    private String uname;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(long expire_time) {
        this.expire_time = expire_time;
    }

    public String getReal_uid() {
        return real_uid;
    }

    public void setReal_uid(String real_uid) {
        this.real_uid = real_uid;
    }

    public String getUid_type() {
        return uid_type;
    }

    public void setUid_type(String uid_type) {
        this.uid_type = uid_type;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.sid);
        dest.writeString(this.token);
        dest.writeString(this.userpic);
        dest.writeLong(this.expire_time);
        dest.writeString(this.real_uid);
        dest.writeString(this.uid_type);
        dest.writeString(this.uname);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.uid = in.readString();
        this.nickname = in.readString();
        this.sid = in.readString();
        this.token = in.readString();
        this.userpic = in.readString();
        this.expire_time = in.readLong();
        this.real_uid = in.readString();
        this.uid_type = in.readString();
        this.uname = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
