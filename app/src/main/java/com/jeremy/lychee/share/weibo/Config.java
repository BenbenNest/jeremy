/**
 *
 */

package com.jeremy.lychee.share.weibo;

/**
 * Some constants for configuration.
 *
 * @author xiaozhongzhong
 */
public final class Config {
    public static final String WEIBO_APP_KEY = "3218553409";
    public static final String WEIBO_REDIRECT_URL = "http://sh.qihoo.com/api/callback.php";
    //    public static final String SCOPE = "all";
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

    /**
     * 新浪微博API Host
     */
    public final static String SINA_API_URL = "api.weibo.com/2";
    /**
     * 获取新浪微博用户信息API
     */
    public final static String SINA_API_USER = "users/show.json";

    // Suppress default constructor for noninstantiability
    private Config() {
        throw new AssertionError();
    }

}
