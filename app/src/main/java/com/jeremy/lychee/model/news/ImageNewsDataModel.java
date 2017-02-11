package com.jeremy.lychee.model.news;

import java.util.List;

public class ImageNewsDataModel {

    private int count;
    private List<String> pics;

    public void setCount(int count) {
        this.count = count;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public int getCount() {
        return count;
    }

    public List<String> getPics() {
        return pics;
    }
}
