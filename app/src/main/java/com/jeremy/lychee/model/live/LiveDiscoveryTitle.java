package com.jeremy.lychee.model.live;

import java.io.Serializable;

/**
 * Created by chengyajun on 2015/12/22.
 */
public class LiveDiscoveryTitle implements Serializable {

    /**
     *  "id": "rec",
       "name": "推荐"
     */
    private String id;
    private String name;

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



}
