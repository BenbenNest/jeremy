package com.jeremy.lychee.model.news;

import com.jeremy.lychee.db.NewsListData;

import java.util.List;

/**
 * Created by chengyajun on 2015/12/22.
 */
public class SubjectColumnGroup {
    private String id;
    private String name;
    private List<NewsListData> data;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NewsListData> getData() {
        return data;
    }

    public void setData(List<NewsListData> data) {
        this.data = data;
    }
}
