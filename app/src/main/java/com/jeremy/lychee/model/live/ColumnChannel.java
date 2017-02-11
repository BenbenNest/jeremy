package com.jeremy.lychee.model.live;

import java.io.Serializable;
import java.util.List;

/**
 * Created by daibangwen-xy on 15/12/23.
 */
public class ColumnChannel implements Serializable{
    private String channel_id;
    private String channel_icon;
    private String channel_name;
    private boolean isClicked;
    private String channel_type;
    private String channel_ctype;
    private List<LanMu> lanmu;

    public List<LanMu> getLanmu() {
        return lanmu;
    }

    public void setLanmu(List<LanMu> lanmu) {
        this.lanmu = lanmu;
    }

    public String getChannel_ctype() {
        return channel_ctype;
    }

    public void setChannel_ctype(String channel_ctype) {
        this.channel_ctype = channel_ctype;
    }

    public String getChannel_type() {
        return channel_type;
    }
    public void setChannel_type(String channel_type) {
        this.channel_type = channel_type;
    }
    public boolean isClicked() {
        return isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_icon() {
        return channel_icon;
    }

    public void setChannel_icon(String channel_icon) {
        this.channel_icon = channel_icon;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public static class ChannelType{
        public static final String UNORDERDELANDMOVE = "1";
        public static final String UNORDERDEL = "2";
        public static final String NOMAL = "0";
    }
    public static class ChannelCType{
        public static final String NOMAL = "0";
        public static final String TWENTFOUR = "1";
        public static final String WATER = "2";
        public static final String HOTVIDEO = "3";
    }

}
