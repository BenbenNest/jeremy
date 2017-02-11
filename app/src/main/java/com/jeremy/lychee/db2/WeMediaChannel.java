package com.jeremy.lychee.db2;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table WE_MEDIA_CHANNEL.
 */
public class WeMediaChannel {

    /** Not-null value. */
    private String id;
    private String uid;
    private String c_id;
    private String source_type;
    private String topic_tag;
    /** Not-null value. */
    private String name;
    private String domain;
    private String icon;
    private String backimg;
    private String summary;
    private String weight;
    private Integer is_deleted;
    private Integer is_public;
    private String create_time;
    private Boolean is_sub;
    private Boolean is_my;
    private Boolean is_hot;
    private String nickname;
    private Integer news_num;
    private Integer sub_num;
    private Integer increase_sub_num;
    private Integer update_news_num;
    private String follow;
    private String fans;

    public WeMediaChannel() {
    }

    public WeMediaChannel(String id) {
        this.id = id;
    }

    public WeMediaChannel(String id, String uid, String c_id, String source_type, String topic_tag, String name, String domain, String icon, String backimg, String summary, String weight, Integer is_deleted, Integer is_public, String create_time, Boolean is_sub, Boolean is_my, Boolean is_hot, String nickname, Integer news_num, Integer sub_num, Integer increase_sub_num, Integer update_news_num, String follow, String fans) {
        this.id = id;
        this.uid = uid;
        this.c_id = c_id;
        this.source_type = source_type;
        this.topic_tag = topic_tag;
        this.name = name;
        this.domain = domain;
        this.icon = icon;
        this.backimg = backimg;
        this.summary = summary;
        this.weight = weight;
        this.is_deleted = is_deleted;
        this.is_public = is_public;
        this.create_time = create_time;
        this.is_sub = is_sub;
        this.is_my = is_my;
        this.is_hot = is_hot;
        this.nickname = nickname;
        this.news_num = news_num;
        this.sub_num = sub_num;
        this.increase_sub_num = increase_sub_num;
        this.update_news_num = update_news_num;
        this.follow = follow;
        this.fans = fans;
    }

    /** Not-null value. */
    public String getId() {
        return id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getTopic_tag() {
        return topic_tag;
    }

    public void setTopic_tag(String topic_tag) {
        this.topic_tag = topic_tag;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBackimg() {
        return backimg;
    }

    public void setBackimg(String backimg) {
        this.backimg = backimg;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Integer getIs_public() {
        return is_public;
    }

    public void setIs_public(Integer is_public) {
        this.is_public = is_public;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Boolean getIs_sub() {
        return is_sub;
    }

    public void setIs_sub(Boolean is_sub) {
        this.is_sub = is_sub;
    }

    public Boolean getIs_my() {
        return is_my;
    }

    public void setIs_my(Boolean is_my) {
        this.is_my = is_my;
    }

    public Boolean getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(Boolean is_hot) {
        this.is_hot = is_hot;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getNews_num() {
        return news_num;
    }

    public void setNews_num(Integer news_num) {
        this.news_num = news_num;
    }

    public Integer getSub_num() {
        return sub_num;
    }

    public void setSub_num(Integer sub_num) {
        this.sub_num = sub_num;
    }

    public Integer getIncrease_sub_num() {
        return increase_sub_num;
    }

    public void setIncrease_sub_num(Integer increase_sub_num) {
        this.increase_sub_num = increase_sub_num;
    }

    public Integer getUpdate_news_num() {
        return update_news_num;
    }

    public void setUpdate_news_num(Integer update_news_num) {
        this.update_news_num = update_news_num;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

}
