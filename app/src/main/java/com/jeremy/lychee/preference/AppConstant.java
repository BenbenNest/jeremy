package com.jeremy.lychee.preference;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhaozuotong on 2015/11/19.
 */
public class AppConstant {

    @IntDef({LayoutManager.LINEAR_LAOUT, LayoutManager.GRID_LAYOUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutManager{
        int LINEAR_LAOUT = 0;
        int GRID_LAYOUT = 1;
    }

    // http://add.corp.qihoo.net/pages/viewpage.action?pageId=12890567
    public static class UserType {
        public static final String TYPE_WB = "weibo";
        public static final String TYPE_WX = "weixin";
        public static final String TYPE_KD = "kd";
    }

    // http://add.corp.qihoo.net/pages/viewpage.action?pageId=12890567
    public static class UserFrom {
        public static final String FROM_WB = "1";
        public static final String FROM_WX = "2";
    }

    // http://add.corp.qihoo.net/pages/viewpage.action?pageId=12890567
    public static String APP_SECRET = "d1ebg72e85b817353de4e28efdc180d1";
}
