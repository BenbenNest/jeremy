package com.jeremy.lychee.model.live;

import java.util.List;

/**
 * Created by chengyajun on 2015/12/22.
 */
public class LiveDiscoveryColumnGroup {
    private String id;
    private String name;
    private List<LiveChannel> list;
    private boolean isLiveDiscoveryMoreHide = true;

    public boolean isLiveDiscoveryMoreHide() {
        return isLiveDiscoveryMoreHide;
    }

    public void setIsLiveDiscoveryMoreHide(boolean isLiveDiscoveryMoreHide) {
        this.isLiveDiscoveryMoreHide = isLiveDiscoveryMoreHide;
    }

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

    public List<LiveChannel> getList() {
        return list;
    }

    public void setList(List<LiveChannel> list) {
        this.list = list;
    }


}
