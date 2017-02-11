package com.jeremy.lychee.model.user;

/**
 * @author：leikang on 15-11-23 14:12
 * @mail：leikang@360.cn
 */
public class HotWeMediaChannel {
    private int c_id;
//    private String uid;
    private String type;
    private String source_type;
    private String topic_tag;
    private String name;
    private String domain;
    private String icon;
    private String backimg;
    private String summary;
    private String weight;
    private int status;
    private String news_num;
    private String sub_num;
    private String increase_sub_num;
    private String update_news_num;
    private String create_time;
    private boolean is_sub;

    public void setC_id(int id) {
        this.c_id = id;
    }

//    public void setUid(String uid) {
//        this.uid = uid;
//    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public void setTopic_tag(String topic_tag) {
        this.topic_tag = topic_tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setBackimg(String backimg) {
        this.backimg = backimg;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setNews_num(String news_num) {
        this.news_num = news_num;
    }

    public void setSub_num(String sub_num) {
        this.sub_num = sub_num;
    }

    public void setIncrease_sub_num(String increase_sub_num) {
        this.increase_sub_num = increase_sub_num;
    }

    public void setUpdate_news_num(String update_news_num) {
        this.update_news_num = update_news_num;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getC_id() {
        return c_id;
    }

//    public String getUid() {
//        return uid;
//    }

    public String getType() {
        return type;
    }

    public String getSource_type() {
        return source_type;
    }

    public String getTopic_tag() {
        return topic_tag;
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public String getIcon() {
        return icon;
    }

    public String getBackimg() {
        return backimg;
    }

    public String getSummary() {
        return summary;
    }

    public String getWeight() {
        return weight;
    }

    public int getStatus() {
        return status;
    }

    public String getNews_num() {
        return news_num;
    }

    public String getSub_num() {
        return sub_num;
    }

    public String getIncrease_sub_num() {
        return increase_sub_num;
    }

    public String getUpdate_news_num() {
        return update_news_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public boolean is_sub() {
        return is_sub;
    }

    public void setIs_sub(boolean is_sub) {
        this.is_sub = is_sub;
    }

    @Override
    public String toString() {
        return "HotWeMediaChannel{" +
                "c_id=" + c_id +
                ", type='" + type + '\'' +
                ", source_type='" + source_type + '\'' +
                ", topic_tag='" + topic_tag + '\'' +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", icon='" + icon + '\'' +
                ", backimg='" + backimg + '\'' +
                ", summary='" + summary + '\'' +
                ", weight='" + weight + '\'' +
                ", status=" + status +
                ", news_num='" + news_num + '\'' +
                ", sub_num='" + sub_num + '\'' +
                ", increase_sub_num='" + increase_sub_num + '\'' +
                ", update_news_num='" + update_news_num + '\'' +
                ", create_time='" + create_time + '\'' +
                ", is_sub=" + is_sub +
                '}';
    }
}
