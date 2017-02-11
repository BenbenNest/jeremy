package com.jeremy.lychee.base;


import com.jeremy.lychee.utils.AppUtil;

public class Constants {

    //HITLOG ENABLE
    public static final boolean HILOG_ENABLE = true;

    //TEMP FILE PATH
    public static final String APP_FOLDER = "360/lianxian";
    public static final String APP_ROOT_PATH = AppUtil.getAppSdRootPath();
    public static final String HITLOG_FILE = "hitlogs.txt";

    //Intent Bundle Data Key
    public static final String BUNDLE_KEY_NID = "news_nid";
    public static final String BUNDLE_KEY_URL = "url";
    public static final String BUNDLE_KEY_TYPE = "type";
    public static final String BUNDLE_KEY_COMMENT_NID = "comment_nid";
    public static final String BUNDLE_KEY_NEWS_FROM = "news_from";
    public static final String BUNDLE_KEY_ALBUM_ID = "album_id";
    public static final String BUNDLE_KEY_TRANSMIT_ID = "transmit_id";
    public static final String BUNDLE_KEY_IS_ALL_SELECT = "is_all_select";


    //Crop Picture
    public static final int NONE = 0;
    public static final int CAMERA_CHANNEL_IC = 1;
    public static final int GALLERY_CHANNEL_IC = 2;
    public static final int CHANNEL_IC_CROPED = 3;
    public static final int CAMERA_CHANNEL_BG = 4;
    public static final int GALLERY_CHANNEL_BG = 5;
    public static final int CHANNEL_BG_CROPED = 6;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int PICK_CHANNEL_IC = 0;
    public static final int PICK_CHANNEL_BG = 1;
    public static final int MEDIA_CHANNEL_NAME_MAX_LENGTH = 15;
    public static final int MEDIA_CHANNLE_DESCRIBE_LENGTH = 40;

    //OTHER
    public static final int TWICE_CLICK_MIN_INTERVAL = 500;

    public static final String DAILY_DATE_FORMAT = "yyyy.MM.dd";
    public static final String GIHUB_TRENDING = "https://github.com/trending?l=java";
}
