package com.jeremy.lychee.model.update;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by houwenchang
 15/12/18.
 */
public class UpdateInfo implements Parcelable {
//    data: {
//        id: "1",
//                version_code: "1",
//                version_name: "1",
//                apk_url: "http://down.360safe.com/kandian/kandian_shengji_301.apk",
//                apk_path: "/360/apk",
//                apk_name: "lianxian.apk",
//                apk_size: "5MB",
//                update_msg: "已经有57%的小伙伴更新了，就等你来!",
//                update_content: "兴趣热点一网打尽兴趣热点一网打尽",
//                force_update: 0
//    }
    public String id;
    public int version_code;
    public String version_name;
    public String apk_url;
    public String apk_path;
    public String apk_name;
    public String apk_size;
    public String update_msg;
    public List<String> update_content;
    public int force_update;


    protected UpdateInfo(Parcel in) {
        id = in.readString();
        version_code = in.readInt();
        version_name = in.readString();
        apk_url = in.readString();
        apk_path = in.readString();
        apk_name = in.readString();
        apk_size = in.readString();
        update_msg = in.readString();
        update_content = in.createStringArrayList();
        force_update = in.readInt();
    }

    public static final Creator<UpdateInfo> CREATOR = new Creator<UpdateInfo>() {
        @Override
        public UpdateInfo createFromParcel(Parcel in) {
            return new UpdateInfo(in);
        }

        @Override
        public UpdateInfo[] newArray(int size) {
            return new UpdateInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(version_code);
        dest.writeString(version_name);
        dest.writeString(apk_url);
        dest.writeString(apk_path);
        dest.writeString(apk_name);
        dest.writeString(apk_size);
        dest.writeString(update_msg);
        dest.writeStringList(update_content);
        dest.writeInt(force_update);
    }
}
