package com.jeremy.lychee.model.live;

import java.util.List;

/**
 * Created by daibangwen-xy on 16/1/14.
 */
public class LiveChannelsAndCategory {
    public List<TVList> channels;
    public List<LiveDiscoveryTitle> category;

    public List<TVList> getChannels() {
        return channels;
    }

    public void setChannels(List<TVList> channels) {
        this.channels = channels;
    }

    public List<LiveDiscoveryTitle> getCategory() {
        return category;
    }

    public void setCategory(List<LiveDiscoveryTitle> category) {
        this.category = category;
    }
}
