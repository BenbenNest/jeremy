package com.jeremy.lychee.model.news;

import com.jeremy.lychee.db.NewsListData;

/**
 * @author：leikang on 15-11-24 17:24
 * @mail：leikang@360.cn
 */
public class TransmitEntity {

    private String transmitid;
    private String likes;
    private String uid;
    private String nickname;
    private String userpic;
    private String content;
    private int createtime;
    private NewsListData news;
    private boolean is_ding = false;
    private int type;//1:internal-news 2:live-video 3:external-news

    public void setTransmitid(String transmitid) {
        this.transmitid = transmitid;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNews(NewsListData news) {
        this.news = news;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTransmitid() {
        return transmitid;
    }

    public String getLikes() {
        return likes;
    }

    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserpic() {
        return userpic;
    }

    public String getContent() {
        return content;
    }

    public NewsListData getNews() {
        return news;
    }

    public int getCreatetime() {
        return createtime;
    }

    public int getType() {
        return type;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public boolean is_ding() {
        return is_ding;
    }

    public void setIs_ding(boolean is_ding) {
        this.is_ding = is_ding;
    }

    @Override
    public String toString() {
        return "TransmitEntity{" +
                "transmitid='" + transmitid + '\'' +
                ", likes='" + likes + '\'' +
                ", uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userpic='" + userpic + '\'' +
                ", content='" + content + '\'' +
                ", createtime=" + createtime +
                ", news=" + news +
                ", is_ding=" + is_ding +
                ", type=" + type +
                '}';
    }
}
