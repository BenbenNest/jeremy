package com.jeremy.lychee.share.wxapi;

/**
 * 微信相关的常量信息类
 * 
 * @author zourongbo
 *
 */
public final class WXConfig {
    /*
     * 微信接口的APP_ID和APP_SECRET,WX_SCOPE
     */
//    public static final String WX_APP_ID = "wx768117d6ac23353d"; //kandian
//    public static final String WX_APP_SECRET = "7dee9130cc2f80fa887e272eb14aab49"; //kandian
    public static final String WX_APP_ID = "wxac46af6e8d12554e";
    public static final String WX_APP_SECRET = "44a9cd417123a30cdfe5a3567190d7ef";
    public static final String WX_APP_SCOPE = "snsapi_userinfo";

    public static final String WX_GRANT_AUTH_CODE = "authorization_code";
    public static final String WX_GRANT_REFRESH_TOKEN = "refresh_token";
    
    public static final String WX_GRAND = "grant_type";
    public static final String WX_APPID = "appid";
    public static final String WX_SECRET = "secret";
    public static final String WX_CODE = "code";
    public static final String WX_REFRESH_TOKEN = "refresh_token";
    public static final String WX_ACCCESS_TOKEN = "access_token";
    public static final String WX_OPENID = "openid";
    
    
    
    /**
     * 通过code获取access_token
     */
    public final static String WX_API_URL = "api.weixin.qq.com/sns";
    
    /**
     * 微信Access Token
     */
    public final static String WX_API_ACCESS_TOKEN = "oauth2/access_token";
    
    /**
     * 刷新或续期access_token使用
     */
    public final static String WX_API_REFRESH_TOKEN = "oauth2/refresh_token";

    /**
     * 检验授权凭证（access_token）是否有效
     */
    public final static String WX_API_AUTH = "auth";
    
    /**
     * 获取用户个人信息（UnionID机制）
     */
    public final static String WX_API_USERINFO = "userinfo";
    
    // Suppress default constructor for noninstantiability
    private WXConfig() {
        throw new AssertionError();
    };
}
