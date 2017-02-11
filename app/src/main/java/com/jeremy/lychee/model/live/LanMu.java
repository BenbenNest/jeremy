package com.jeremy.lychee.model.live;

import java.io.Serializable;

/**
 * Created by daibangwen-xy on 16/3/30.
 */
public class LanMu implements Serializable {
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
