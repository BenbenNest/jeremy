package com.jeremy.lychee.utils.hitLog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;

import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.base.BaseActivity;
import com.jeremy.lychee.manager.QhLocationManager;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.Des3;
import com.jeremy.lychee.utils.DeviceUtil;
import com.jeremy.lychee.utils.StringUtil;
import com.qihu.mobile.lbs.location.QHLocation;

import java.util.List;
import java.util.TreeSet;

import rx.functions.Action0;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by daibangwen-xy on 15/12/21.
 */
public class HitLog {
    public static String access = "";
    public static String access_subtype = "";
    public static final String optimize_img = "1";

    public static final String WIFI = "wifi";
    public static final String MOBILE = "mobile";
    public static final String MOBILE_4G = "4G";
    public static final String MOBILE_3G = "3G";
    public static final String MOBILE_2G = "2G";

    //0-直接输入关键词或点击搜索历史中的关键词，1-点击“大家都在搜”中的关键词
    public static final String ACTION_SEARCH_INPUT_HISTORY = "0";
    public static final String ACTION_SEARCH_HOTWORD = "1";
    //0-评论，1-分享，2-转推
    public static final String ACTION_COMMENT = "0";
    public static final String ACTION_SHARE = "1";
    public static final String ACTION_TRANSMIT = "2";

    //0-新闻列表页的普通新闻；1-新闻列表页的置顶新闻；2-新闻详情页的相关新闻；3-新闻详情页的热点新闻
    public static final String POSITION_NORMAL_NEWS = "0";
    public static final String POSITION_STICK_NEWS = "1";
    public static final String POSITION_RELATED_NEWS = "2";
    public static final String POSITION_HOT_NEWS = "3";

    //0-新闻详情页；1-发现页的推荐视频；2-发现页的最火视频；3-发现页的最新；4-发现页的我在现场
    // 5-发现页的分类视频；6-频道页的正在直播；7-频道页的往期节目；8-频道页的精彩栏目；9-频道页的相关推荐
    public static final String POSITION_NEWS_DETAIL = "0";
    public static final String POSITION_DISCOVERY_RECOMMEND = "1";
    public static final String POSITION_DISCOVERY_HOT = "2";
    public static final String POSITION_DISCOVERY_NEW = "3";
    public static final String POSITION_DISCOVERY_IN_LIVE = "4";
    public static final String POSITION_DISCOVERY_COLUMN = "5";
    public static final String POSITION_CHANNEL_LIVE = "6";
    public static final String POSITION_CHANNEL_HISTORY = "7";
    public static final String POSITION_CHANNEL_FINE = "8";
    public static final String POSITION_CHANNEL_RECOMMEND = "9";

    public static final String POSITION_LIVE_HOT = "1";


    //video_type只在展现时传，播放时传null，内容为：1-直播，2-花椒直播类型，3-长视频，4-短视频
    public static final String VIDEO_TYPE_LIVE = "1";
    public static final String VIDEO_TYPE_HUAJIAO_LIVE = "2";
    public static final String VIDEO_TYPE_LONG_VIDEO = "3";
    public static final String VIDEO_TYPE_SHORT_VIDEO = "4";

    //stream_type只在播放时传，展现时为null，流类型：1-花椒直播，2-花椒回放，3-mp4格式播放，4-录播，
    // 5-直播流播放（btv），6-我在现场直播，7-我在现场回放。
    public static final String STREAM_TYPE_HUAJIAO_LIVE = "1";
    public static final String STREAM_TYPE_HUAJIAO_REPLAY = "2";
    public static final String STREAM_TYPE_MP4 = "3";
    public static final String STREAM_TYPE_RECORD = "4";
    public static final String STREAM_TYPE_BTV = "5";
    public static final String STREAM_TYPE_IN_LIVE = "6";
    public static final String STREAM_TYPE_IN_LIVE_REPLAY = "7";

    private static final byte[] DES_KEY = StringUtil.toUnicode("BTIME.qihoo@20160406.asdf1234").getBytes();

    public static String pingString(Context context, String actionType, String actionInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(AppUtil.getTokenID(context));
        sb.append("\t");
        sb.append(System.currentTimeMillis());
        sb.append("\t");
        updateNetworkInfo();
        sb.append(HitLog.access);
        sb.append("\t");
        sb.append("" + actionType);
        sb.append("\t");
        sb.append(actionInfo);
        sb.append("\t");
        sb.append("1.01");
        return sb.toString();
    }

    public static String pingActionInfo(String... str) {
        StringBuilder sb = new StringBuilder();
        for (String s : str) {
            sb.append("|");
            sb.append(s);
        }
        if (sb.length() > 0) {
            sb.replace(0, 1, "");
            return sb.toString();
        }
        return "null";
    }

    public static void updateNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ApplicationStatus.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnectedOrConnecting()) {
            return;
        }
        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
            case ConnectivityManager.TYPE_ETHERNET:
                HitLog.access = WIFI;
                HitLog.access_subtype = "";
                break;
            case ConnectivityManager.TYPE_MOBILE:
                HitLog.access = MOBILE;
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_LTE:  // 4G
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        HitLog.access_subtype = MOBILE_4G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        HitLog.access_subtype = MOBILE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 2G
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        HitLog.access_subtype = MOBILE_2G;
                        break;
                    default:
                }
                break;
            default:
        }
    }

    public static String getUserInfo() {
        if (Session.isUserInfoEmpty()) return "";
        return Session.getSession().getUid();
    }

    @Deprecated
    public static String getLocationOnScreen(View view) {
        int[] location = new int[2];
        if (view != null) {
            view.getLocationOnScreen(location);
        }
        location[0] = (location[0] + view.getWidth()) / 2;
        location[1] = (location[1] + view.getHeight()) / 2;
        return location[0] + "," + location[1];
    }

    /**
     * 1001APP启动打点
     * <type>|<gps>|<duration>|<社交账号类型：账号id>
     */
    public static void hitLogAppStart() {
        Context context = ApplicationStatus.getApplicationContext();
        QHLocation location = QhLocationManager.getInstance().getLocation();
        String gpsInfo =
                location == null ? "unknown" :
                        location.getLatitude() + "_" + location.getLongitude() + ","
                        + location.getProvince() + ","
                        + location.getCity() + ","
                        + location.getDistrict() + ","
                        + location.getStreet() + ","
                        + location.getAddrStr();
        try {
            HitLogHelper.getHitLogHelper().hitLog(
                    pingString(context,
                            LogType.APPSTATE,
                            pingActionInfo("0",
                                    Base64.encodeToString(Des3.des3EncodeECB(DES_KEY, gpsInfo.getBytes("UTF-8")), Base64.DEFAULT),
                                    "null",  HitLog.getUserInfo())),
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //首次启动APP打点
        BaseActivity.sAppForgroundDuration = 0L;
    }

    /**
     * 1001APP结束打点
     *
     * @param duration <type>|<gps>|<duration>|<社交账号类型：账号id>
     */
    public static void hitLogAppEnd(long duration) {
        Context context = ApplicationStatus.getApplicationContext();
        QHLocation location = QhLocationManager.getInstance().getLocation();
        String gpsInfo =
                location == null ? "unknown" :
                        location.getLatitude() + "_" + location.getLongitude() + ","
                                + location.getProvince() + ","
                                + location.getCity() + ","
                                + location.getDistrict() + ","
                                + location.getStreet() + ","
                                + location.getAddrStr();
        try {
            HitLogHelper.getHitLogHelper().hitLog(
                    pingString(context,
                            LogType.APPSTATE,
                            pingActionInfo(
                                    "1",
                                    Base64.encodeToString(Des3.des3EncodeECB(DES_KEY, gpsInfo.getBytes("UTF-8")), Base64.DEFAULT),
                                    Long.toString(duration),
                                    HitLog.getUserInfo())),
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1002设备信息打点
     * <设备名称>|<设备屏幕>|<OS版本>|<App版本>|<有无安Applist>
     */
    public static void hitLogDeviceInfo() {
        Context context = ApplicationStatus.getApplicationContext();
        List<String> list = AppUtil.getAppList(context);
        TreeSet<String> set = new TreeSet<>();
        for (String str : list) {
            set.add(str);
        }
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.DEVICE_INFO,
                        pingActionInfo(
                                new Build().MODEL,
                                DeviceUtil.getScreenWidth(context) + "," + DeviceUtil.getScreenHeight(context),
                                String.valueOf(Build.VERSION.SDK_INT),
                                Integer.toString(AppUtil.getVersionCode(context)),
                                StringUtil.MD5Encode(set.toString()))
                ), null);
    }


    /**
     * 2001新闻点击打点
     * <scene>|<tag>|<position>|<action>|<(news_list:)<nid,news_from;nid,news_from...>|<screen_pos>
     */
    public static void hitLogNewsListClick(String scene, String channel,
                                           String position, NewsListData data) {
        Context context = ApplicationStatus.getApplicationContext();
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.NEWS_CLICK_OR_VIEW,
                        pingActionInfo(
                                scene,
                                channel,
                                position,
                                "click",
                                data.getNid() + "," + data.getNews_from() + "," + data.getNews_type())
                ), null);
    }

    /**
     * 2001新闻列表显示打点
     * <scene>|<tag>|<position>|<action>|<(news_list:)<nid,news_from;nid,news_from...>|<screen_pos>
     */
    public static void hitLogNewsListShow(String scene, String channel,
                                          String position, List<NewsListData> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        Context context = ApplicationStatus.getApplicationContext();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i) == null ? "" : list.get(i).getNid()); // TODO：Bug 74142 偶现FE返回null的情况
            sb.append(",");
            sb.append(list.get(i) == null ? "" : list.get(i).getNews_from());
            sb.append(",");
            sb.append(list.get(i) == null ? "" : list.get(i).getNews_type());
            if (i != list.size() - 1) {
                sb.append(";");
            }
        }
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.NEWS_CLICK_OR_VIEW,
                        pingActionInfo(
                                scene,
                                channel,
                                position,
                                "view",
                                sb.toString(),
                                "null")
                ), null);
    }

    /**
     * 2002新闻详情页的浏览时间和百分比
     * <scene>|<tag>|<nid,news_from>|<duration>|<percent>|<sliding_cnt>
     *
     * @param nid
     * @param scene,
     * @param tag,
     * @param news_from
     * @param duration
     * @param percent
     * @param sliding_cnt
     */
    public static void hitLogNewsDetailBrowse(String nid,
                                              String scene,
                                              String tag,
                                              String news_from,
                                              long duration,
                                              float percent,
                                              int sliding_cnt) {
        Context context = ApplicationStatus.getApplicationContext();
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.NEWS_DETAIL,
                        pingActionInfo(
                                scene,
                                tag,
                                nid + "," + news_from,
                                Long.toString(duration),
                                Float.toString(percent),
                                Integer.toString(sliding_cnt))
                ), null);
    }


    /**
     * 2003评论打点
     * <scene>|<tag>|<nid,news_from>|<action>
     *
     * @param scene
     * @param channel
     * @param nid
     * @param news_from
     */
    public static void hitLogComment(String scene, String channel,
                                     String nid, String news_from,
                                     Action0 onCompleted) {
        Context context = ApplicationStatus.getApplicationContext();
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.COMMENT_SHARE_TRANSMIT,
                        pingActionInfo(
                                scene,
                                channel,
                                nid + "," + news_from,
                                ACTION_COMMENT)
                ), onCompleted);
    }


    /**
     * 2004热搜打点
     * <keyword>|<action>|<screen_pos>
     */
    public static void hitLogSearch(String keyword, String action) {
        Context context = ApplicationStatus.getApplicationContext();
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.HOT_SEARCH,
                        pingActionInfo(
                                keyword,
                                action)
                ), null);
    }

    /**
     * 2005不喜欢功能
     * <scene>|<tag>|<action>|<nid,news_from,news_type>
     */
    public static void hitDislike(String scene, String channel, String action, NewsListData data) {
        Context context = ApplicationStatus.getApplicationContext();
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.DISLIKE,
                        pingActionInfo(
                                scene,
                                channel,
                                action,
                                data.getNid() + "," + data.getNews_from() + "," + data.getNews_type())
                ), null);
    }


    /**
     * 2003转发打点
     * <scene>|<tag>|<nid,news_from>|<action>
     *
     * @param scene
     * @param channel
     * @param nid
     * @param news_from
     */
    public static void hitLogShare(String scene, String channel,
                                   String nid, String news_from) {
        Context context = ApplicationStatus.getApplicationContext();
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.COMMENT_SHARE_TRANSMIT,
                        pingActionInfo(
                                scene,
                                channel,
                                nid + "," + news_from,
                                ACTION_SHARE)
                ), null);
    }

    /**
     * 2003转发打点
     * <scene>|<tag>|<nid,news_from>|<action>
     *
     * @param scene
     * @param channel
     * @param nid
     * @param news_from
     */
    public static void hitLogTransmit(String scene, String channel,
                                      String nid, String news_from) {
        Context context = ApplicationStatus.getApplicationContext();
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.COMMENT_SHARE_TRANSMIT,
                        pingActionInfo(
                                scene,
                                channel,
                                nid + "," + news_from,
                                ACTION_TRANSMIT)
                ), null);
    }


    /**
     * 3001视频列表显示
     * <position>|<action>|(videoId_list:)<vid;vid;vid...>
     *
     * @param position
     * @param videoId_list
     */
    public static void hitLogVideoShow(String position, List<String> videoId_list) {
        if (videoId_list == null || videoId_list.size() == 0) {
            return;
        }
        Context context = ApplicationStatus.getApplicationContext();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < videoId_list.size(); i++) {
            sb.append(videoId_list.get(i));
            if (i != videoId_list.size() - 1) {
                sb.append(";");
            }
        }
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.VIDEO_CLICK_OR_VIEW,
                        pingActionInfo(
                                position,
                                "view",
                                sb.toString())
                ), null);
    }

    /**
     * 3001视频点击
     * <position>|tag|<action>|(videoId_list:)<vid;vid;vid...>
     *
     * @param position
     * @param vid
     */
//    public static void hitLogVideoClick(String position, String columnId, String vid) {
    public static void hitLogVideoClick(String position, String vid) {
        Context context = ApplicationStatus.getApplicationContext();
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.VIDEO_CLICK_OR_VIEW,
                        pingActionInfo(
                                position,
                                "click",
                                vid)
                ), null);
    }

    /**
     * 视频列表页
     * 3003	(video_list:)<vid,news_from,news_type;vid,news_from,news_type...>
     */

    public static void hitLogVideoPlayList(List<String> videoId_list) {
        if (videoId_list == null || videoId_list.size() == 0) {
            return;
        }
        Context context = ApplicationStatus.getApplicationContext();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < videoId_list.size(); i++) {
            sb.append(videoId_list.get(i));
            if (i != videoId_list.size() - 1) {
                sb.append(";");
            }
        }
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.VIDEO_PLAY_LIST,
                        pingActionInfo(
                                sb.toString()
                        )
                ), null);
    }


    /**
     * 3002视频播放
     * <stream_vbt>|<vid>|<(play_seq:)beg_pos,end_pos;beg_pos,end_pos...>
     *
     * @param stream_vbt
     * @param vid
     * @param play_seq
     */
    public static void hitLogVideoPlay(String stream_vbt,
                                       String vid, List<String[]> play_seq) {
        if (play_seq == null || play_seq.size() == 0) {
            return;
        }
        Context context = ApplicationStatus.getApplicationContext();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < play_seq.size(); i++) {
            sb.append(play_seq.get(i)[0]);
            sb.append(",");
            sb.append(play_seq.get(i)[1]);
            if (i != play_seq.size() - 1) {
                sb.append(";");
            }
        }
        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.VIDEO_PLAY,
                        pingActionInfo(
                                StringUtil.toUnicode(stream_vbt),
                                vid,
                                sb.toString())
                ), null);
    }


    /**
     * 4001频道订阅
     * (subscribeId_list:)<sId,sId,sId...>
     *
     * @param list
     */
    public static void hitLogSubChannels(List<String> list) {
        Context context = ApplicationStatus.getApplicationContext();
        if (list == null || list.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(",");
            sb.append(str);
        }
        if (sb.length() > 0) {
            sb.replace(0, 1, "");
        }

        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.SUB_CHANNEL,
                        pingActionInfo(sb.toString())
                ), null);
    }

    /**
     * 4003频道订阅
     * (tagId_list:)<tId,tId,tId...>
     *
     * @param list
     */
    public static void hitLogFirstChooseChannels(List<String> list) {
        Context context = ApplicationStatus.getApplicationContext();
        if (list == null || list.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(",");
            sb.append(str);
        }
        if (sb.length() > 0) {
            sb.replace(0, 1, "");
        }

        HitLogHelper.getHitLogHelper().hitLog(
                pingString(context,
                        LogType.FIRST_CHOOSE_CHANNEL,
                        pingActionInfo(sb.toString())
                ), null);
    }
}
