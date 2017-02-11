package com.jeremy.lychee.model.user;

import java.io.Serializable;

/**
 * Created by zhangying-pd on 2016/2/16.
 * 注册result data
 */
public class RegisterData implements Serializable {
    private String uid;
    private String nickname;
    private String sid;
    private String userpic;
    private String expire_time;
    private String real_uid;
    private String uid_type;
    private String uname;
    private String errmsg;

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

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
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

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
