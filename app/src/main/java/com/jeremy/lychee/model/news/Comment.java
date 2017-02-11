package com.jeremy.lychee.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhangying-pd on 2016/3/22.
 *
 * 评论对象
 */
public class Comment implements Parcelable{
    private String page_id;
    private String uid;
    private String pid;
    private String user_info;
    private String message;
    private String likes;
    private String ip;
    private String longitude;
    private String latitude;
    private String status;
    private String pdate;
    private String anonymous;
    private String client_type;
    private String id;
    private int diggok;
    private List<Comment> sub_comment;


    protected Comment(Parcel in) {
        page_id = in.readString();
        uid = in.readString();
        pid = in.readString();
        user_info = in.readString();
        message = in.readString();
        likes = in.readString();
        ip = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        status = in.readString();
        pdate = in.readString();
        anonymous = in.readString();
        client_type = in.readString();
        id = in.readString();
        diggok = in.readInt();
        sub_comment = in.createTypedArrayList(Comment.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(page_id);
        dest.writeString(uid);
        dest.writeString(pid);
        dest.writeString(user_info);
        dest.writeString(message);
        dest.writeString(likes);
        dest.writeString(ip);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(status);
        dest.writeString(pdate);
        dest.writeString(anonymous);
        dest.writeString(client_type);
        dest.writeString(id);
        dest.writeInt(diggok);
        dest.writeTypedList(sub_comment);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<Comment> getSub_comment() {
        return sub_comment;
    }

    public void setSub_comment(List<Comment> sub_comment) {
        this.sub_comment = sub_comment;
    }

    public int getDiggok() {
        return diggok;
    }

    public void setDiggok(int diggok) {
        this.diggok = diggok;
    }
}
