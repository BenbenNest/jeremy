package com.jeremy.library.jsobject;

/**
 * Created by changqing.zhao on 2017/2/7.
 */

public class BrowserJsInject {
    /**
     * Js注入
     *
     * @param url 加载的网页地址
     * @return 注入的js内容，若不是需要适配的网址则返回空javascript
     */
    public static String fullScreenByJs(String url) {
        String refer = referParser(url);
        if (null != refer) {
            String js3 = "document.getElementsByClassName('"
                    + referParser(url) + "')[0].addEventListener('click',function(){alert('test');});";

//            console.full();

//            String js3 = "alert('120');window.onload=function(){document.getElementsByClassName('"
//                    + referParser(url) + "')[0].addEventListener('click',function(){alert('120');" +
//                    "console.log();" +
//                    "alert('110');})}"
//                    + ";";


            return "javascript:" + js3;
        } else {
            return "javascript:";
        }
    }

    /**
     * 对不同的视频网站分析相应的全屏控件
     *
     * @param url 加载的网页地址
     * @return 相应网站全屏按钮的class标识
     */
    public static String referParser(String url) {
        if (url.contains("letv")) {
            return "hv_ico_screen";   //乐视Tv
        } else if (url.contains("youku")) {
            return "x-zoomin";//优酷
        } else if (url.contains("bilibili")) {
            return "icon-widescreen"; //bilibili
        } else if (url.contains("qq")) {
            return "tvp_fullscreen_button";   //腾讯视频
        }

        return null;
    }
}
