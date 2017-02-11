package com.jeremy.lychee.model.user;

import com.jeremy.lychee.model.news.TransmitEntity;

/**
 * @author：leikang on 15-11-23 13:59
 * @mail：leikang@360.cn
 */
public class FeedNewsEntity {
    //0:原始 1:选择 -1:反选
    static public final int NONE = 0;
    static public final int SELECT = 1;
    static public final int UNSELECT = -1;

    private String sub_id;//所在专辑ID
    private String c_id;
    private String name;
    private String icon;
    private String news_num;
    private String backimg;
    private String sub_num;
    private String sub_name;
    private boolean is_sub;
    private TransmitEntity transmit;
    private int is_select = NONE;//标记是否被选择（客户端使用


    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setNews_num(String news_num) {
        this.news_num = news_num;
    }

    public void setBackimg(String backimg) {
        this.backimg = backimg;
    }

    public void setSub_num(String sub_num) {
        this.sub_num = sub_num;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public void setIs_sub(boolean is_sub) {
        this.is_sub = is_sub;
    }

    public void setTransmit(TransmitEntity transmit) {
        this.transmit = transmit;
    }

    public void setIs_select(int is_select) {
        this.is_select = is_select;
    }

    public String getSub_id() {
        return sub_id;
    }

    public String getC_id() {
        return c_id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getNews_num() {
        return news_num;
    }

    public String getBackimg() {
        return backimg;
    }

    public String getSub_num() {
        return sub_num;
    }

    public String getSub_name(){
        return sub_name;
    }

    public boolean isIs_sub() {
        return is_sub;
    }

    public TransmitEntity getTransmit() {
        return transmit;
    }

    public int isIs_select() {
        return is_select;
    }
    @Override
    public String toString() {
        return "FeedNewsEntity{" +
                "sub_id=" + sub_id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", news_num='" + news_num + '\'' +
                ", backimg='" + backimg + '\'' +
                ", sub_num='" + sub_num + '\'' +
                ", sub_name='" + sub_name + '\'' +
                ", is_sub=" + is_sub +
                ", transmit=" + transmit +
                '}';
    }
}
