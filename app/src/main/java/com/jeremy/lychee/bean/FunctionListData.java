package com.jeremy.lychee.bean;

/**
 * Created by benbennest on 16/4/25.
 */
public class FunctionListData {
    private String title;
    private String url;
    private String des;
    private int resId;

    public FunctionListData(String title, String url, String des, int resId) {
        this.title = title;
        this.url = url;
        this.des = des;
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
