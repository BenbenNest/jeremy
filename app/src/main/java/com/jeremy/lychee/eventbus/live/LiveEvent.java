package com.jeremy.lychee.eventbus.live;

import com.jeremy.lychee.model.live.ColumnChannel;

import java.util.List;

/**
 * Created by daibangwen-xy on 15/12/26.
 */
public class LiveEvent {
    public static class SubAlarm {
        public String channelId;
        public String tvtype;
    }
    public static class SubProgram {

    }
    public static class TVClick {
        public String channel_cid;
        public List<ColumnChannel> columnChannels;
        public String tv_type;
    }
    public static class ColumnClick {
        public String column_id;
        public String channel_id;
        public ColumnClick(String column_id, String channel_id) {
            this.column_id = column_id;
            this.channel_id = channel_id;
        }
    }
    public static class liveChannelItem {
        public String id;
        public String type;
        public String tag;
        public String video_img;

    }
    public static class SubscriptedChannel{
        public List<ColumnChannel> selectChannels;
    }
    public static class SubscriptChannel{
        public int position;
        public ColumnChannel columnChannel;
    }
    public static class MoveChannel{

    }
    public static class LongClickChannel{

    }
    public static class LiveChannelClick{
        public int positon;
        public String type;
        public String channel_id;
    }

    public static class ClickSelectChannel{

    }
    public static class UnSubscriptChannel{
        public int position;
        public ColumnChannel columnChannel;
    }

    public static class isPlayColumnClick{
        public String channel_cid;
        public String channelId;
    }
    public static class ZanClick{
        public int position;
        public String url;
    }

    public static class ShareClick{
        public int position;
        public String url;
    }

    public static class showIntroLive{
    }
    public static class showIntroLiveDiscoveryOpen{
    }
    public static class showIntroLiveDiscoveryColse{
    }
    public static class HotShareClick{
        public int position;
        public String url;
    }
    public static class HotCommentUpdata {
        public int num;
        public String nid;
    }


}
