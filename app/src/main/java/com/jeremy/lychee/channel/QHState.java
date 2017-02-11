package com.jeremy.lychee.channel;

/**
 * Created by daibangwen-xy on 16/3/21.
 */
public class QHState {
    public static final String LIVE_TAB_PV = "live_tab_pv";//直播Tab PV
    public static final String USER_TAB_PV = "user_tab_pv";//自频道Tab PV
    public static final String RECOMMENDARTICLE = "recommend_article";//自频道推荐文章
    public static final String WRITINGARTICLE = "writing_article";//自频道创作文章
    public static final String PHONEREGISTER = "phone_register";//手机号注册
    public static final String NEWSDETAIL_PV = "newsdetail_pv";//底层页PV
    public static final String NEWSDETAIL_HOT_CLICK = "newsdetail_hot_click";//底层页热门推荐点击
    public static final String NEWSDETAIL_RELATED_CLICK = "newsdetail_related_click";//底层页相关推荐点击
    public static final String NEWSCLICK = "newsclick";//资讯tab新闻点击
    public static final String NEWS_REFRESH = "news_refresh";//资讯tab下拉刷新
    public static final String UNINTERESTED = "uninterested";//资讯tab新闻点击不感兴趣
    public static final String USER_WRITING_TITLE = "user_writing_title";//发起直播填写标题
    public static final String USER_INITIATE_LIVE = "initiate_live";//发起直播
    public static final String HOTVIDEO_REFRESH = "hotvideo_refresh";//热点下拉刷新


   // QHStatAgent.onEvent(this, QHState.NEWSDETAIL_PV,QHState.NEWSDETAIL_PV,1, Long.valueOf(90));
    public static final String LOADING_VIDEO_PLAYLIST = "loading_video_playlist";//加载视频playlist
    public static final String LOADING_VIDEO_LOADSTREAM = "loading_video_loadstream";//加载视频stream
    public static final String LOADING_VIDEO_RENDERER = "loading_video_renderer";//渲染视频
    public static final String LOADING_VIDEO_ALL_TIME = "loading_video_all_time";//加载视频总时间

    //QHStatAgent.onEvent(this, QHState.NEWSDETAIL_PV);
    public static final String LOADING_VIDEO_PLAYLIST_ERROR = "loading_video_playlist_error";//加载playlist失败
    public static final String LOADING_VIDEO_LOADSTREAM_ERROR = "loading_video_loadstream_error";//加载视频stream失败
    public static final String LOADING_VIDEO_RENDERER_ERROR = "loading_video_renderer_error";//渲染失败



}
