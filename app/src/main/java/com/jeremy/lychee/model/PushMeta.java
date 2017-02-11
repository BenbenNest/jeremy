package com.jeremy.lychee.model;

/**
 * @author houwenchang
 *         <p/>
 *         2015/7/8.
 */
public class PushMeta {
    public String msgID;
    public String expire;
    public String channelId;
    public String important;
    public String pushUrl;
    public int type;


//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public PushMeta(Parcel in) {
//        this.msgID = in.readString();
//        this.expire = in.readString();
//        this.channelId = in.readString();
//        this.important = in.readString();
//        this.pushUrl = in.readString();
//        this.type = in.readInt();
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(msgID);
//        dest.writeString(expire);
//        dest.writeString(channelId);
//        dest.writeString(important);
//        dest.writeString(pushUrl);
//        dest.writeInt(type);
//    }
//
//    public static final Creator<PushMeta> CREATOR = new Creator<PushMeta>() {
//        public PushMeta createFromParcel(Parcel in) {
//            return new PushMeta(in);
//        }
//
//        public PushMeta[] newArray(int size) {
//            return new PushMeta[size];
//        }
//    };

    @Override
    public String toString() {
        return "PushMeta{" +
                "msgID='" + msgID + '\'' +
                ", expire='" + expire + '\'' +
                ", channelId='" + channelId + '\'' +
                ", important='" + important + '\'' +
                ", pushUrl='" + pushUrl + '\'' +
                ", type=" + type +
                '}';
    }
}
