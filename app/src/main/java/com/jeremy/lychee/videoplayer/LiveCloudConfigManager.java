package com.jeremy.lychee.videoplayer;

import android.support.annotation.IntDef;

import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.utils.AppUtil;
import com.qihoo.livecloud.ILiveCloudPlayer;
import com.qihoo.livecloud.recorder.setting.BaseSettings;
import com.qihoo.livecloud.recorder.setting.MediaSettings;
import com.qihoo.livecloud.recorder.setting.PublishSettings;
import com.qihoo.livecloud.tools.DeviceIDUtils;
import com.qihoo.livecloud.tools.LiveCloudConfig;
import com.qihoo.livecloud.tools.MD5;
import com.qihoo.livecloud.tools.NetUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Random;

public class LiveCloudConfigManager {
    final static String LIVE_CLOUD_BID = "news";
    final static String LIVE_CLOUD_CID = "live_news";
    final static String LIVE_CLOUD_CID_PLAYBACK = "huikan_news";

    @IntDef({STREAM_TYPE.STREAM_TYPE_HUAJIAO_LIVE, STREAM_TYPE.STREAM_TYPE_HUAJIAO_REPLAY,
            STREAM_TYPE.STREAM_TYPE_BTV_SPLIT, STREAM_TYPE.STREAM_TYPE_NEWS_SHORT_VIDEO,
            STREAM_TYPE.STREAM_TYPE_BTV_LIVE, STREAM_TYPE.STREAM_TYPE_NEWS_LIVE,
            STREAM_TYPE.STREAM_TYPE_NEWS_REPLAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface STREAM_TYPE {
        int STREAM_TYPE_HUAJIAO_LIVE = 1;
        int STREAM_TYPE_HUAJIAO_REPLAY = 2;
        int STREAM_TYPE_BTV_SPLIT = 3;
        int STREAM_TYPE_NEWS_SHORT_VIDEO = 4;
        int STREAM_TYPE_BTV_LIVE = 5;
        int STREAM_TYPE_NEWS_LIVE = 6;
        int STREAM_TYPE_NEWS_REPLAY = 7;
    }

    static private final LiveCloudConfigManager instance = new LiveCloudConfigManager();

    static public LiveCloudConfigManager getInstance() {
        return instance;
    }

    private LiveCloudConfigManager() {
    }

    public LiveCloudConfig getConfig(String cid) {
        LiveCloudConfig config = new LiveCloudConfig();
        config.setCid(cid);
        config.setIbMode(1);
        config.setUid(AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        config.setVer(String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        config.setBid(LIVE_CLOUD_BID);
        config.setSid(generateSID());
        config.setMid(DeviceIDUtils.getIMEI2(ApplicationStatus.getApplicationContext()));
        config.setNet(NetUtil.getNetworkTypeName(ApplicationStatus.getApplicationContext()));
        return config;
    }

    public static String generateSID() {
        return MD5.encryptMD5(String.valueOf(System.currentTimeMillis()) + String.valueOf(new Random().nextInt()));
    }

    public LiveCloudConfig getConfig(@LiveCloudConfigManager.STREAM_TYPE int streamType) {
        if (streamType == LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_HUAJIAO_REPLAY ||
                streamType == LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_NEWS_REPLAY) {
            return getConfig(LIVE_CLOUD_CID_PLAYBACK);
        } else {
            return getConfig(LIVE_CLOUD_CID);
        }
    }

    public LiveCloudConfig getConfig() {
        return getConfig(LIVE_CLOUD_CID);
    }

    public PublishSettings getPublishSettings() {
        PublishSettings setting = new PublishSettings();
        setting.setUid(AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        setting.setVersion(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext()));
        setting.setPublishProtocol(PublishSettings.ETransportProtocol.PROTOCOL_UDP);
        setting.setApp_name(LIVE_CLOUD_CID);

        setting.setMax_retry(0);
        setting.setConnect_timeout(0);
        setting.setSend_timeout(0);
        setting.setAuto_close(0);// 0 false,其他为true
        setting.setLog_level(BaseSettings.ELogLevel.LOG_LEVEL_NONE);
        setting.setLog_module(BaseSettings.ELogModule.LOG_MOD_NONE);
        setting.setLog_path("");

        return setting;
    }

    public MediaSettings getMediaSettings(boolean backCamera) {
        MediaSettings setting = new MediaSettings();
        // 视频设置
        setting.setInputVideoFormat(PublishSettings.EVideoCodecID.V_CODEC_ID_NV21);
        setting.setAnnexB(0);
        setting.setSourceWidth(1280);
        setting.setSourceHeight(720);
        setting.setCodecWidth(360);
        setting.setCodecHeight(640);

        setting.setRotate(backCamera ? 90 : 270);
        setting.setCropMode(BaseSettings.ECropMode.ECROP_CENTER_SCALE);
        setting.setAvgBitrate(600 * 1024);
        setting.setPeekBitrate(800 * 1024);
        setting.setFps(15);

        // 音频设置
        setting.setInputAudioFormat(PublishSettings.EAudioCodecID.A_CODEC_ID_PCM);
        setting.setOutputAudioFormat(PublishSettings.EAudioCodecID.A_CODEC_ID_AAC);
        setting.setSampleRate(16000);
        setting.setTargetBitrate(32000);
        setting.setChannelConfig(1);// 设置声道数
        setting.setSampleDepth(16);// 设置采样宽度，为16bit
        return setting;
    }

    public int getStreamPlayType(@LiveCloudConfigManager.STREAM_TYPE int streamType) {
        switch (streamType) {
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_BTV_LIVE:
                return ILiveCloudPlayer.PlayType.LIVECLOUD_LIVE_HLS;
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_BTV_SPLIT:
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_NEWS_SHORT_VIDEO:
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_NEWS_REPLAY:
                return ILiveCloudPlayer.PlayType.LIVECLOUD_VOD_HLS;
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_HUAJIAO_LIVE:
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_NEWS_LIVE:
                return ILiveCloudPlayer.PlayType.LIVECLOUD_LIVE_RELAY;
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_HUAJIAO_REPLAY:
                return ILiveCloudPlayer.PlayType.LIVECLOUD_REPLAY_M3U8;
            default:
                return ILiveCloudPlayer.PlayType.LIVECLOUD_UNKNOWN;
        }
    }
}
