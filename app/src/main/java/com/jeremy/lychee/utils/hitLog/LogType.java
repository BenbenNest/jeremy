package com.jeremy.lychee.utils.hitLog;

/**
 * Created by daibangwen-xy on 15/12/21.
 */
public class LogType {
    public static final String APPSTATE = "1001"; //app 启动，杀死
    public static final String DEVICE_INFO = "1002"; //设备信息
    public static final String NEWS_CLICK_OR_VIEW = "2001"; //news 点击，展示
    public static final String NEWS_DETAIL = "2002"; //news 浏览时间，百分比
    public static final String COMMENT_SHARE_TRANSMIT = "2003"; //评论，分享，转推
    public static final String HOT_SEARCH = "2004"; //热搜
    public static final String DISLIKE = "2005"; //不喜欢功能
    public static final String VIDEO_CLICK_OR_VIEW = "3001"; //视频显示，点击
    public static final String VIDEO_PLAY = "3002"; //视频播放
    public static final String SUB_CHANNEL = "4001"; //订阅频道
    public static final String FIRST_CHOOSE_CHANNEL = "4003"; //订阅频道
    public static final String VIDEO_PLAY_LIST = "3003"; //视频播放列表
}
