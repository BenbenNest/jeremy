package com.jeremy.lychee.model;

public class PushInfo {

    //	(
//                                [title] => 有些人活着，可他已经死了
//                                [summary] =>
//                        [url] => http://hao.360.cn/zt/daojie359.html
//                        [url_md5] => e95864fc5a735af8f8dd8a66d1e07041
//                                [news_type] => 1
//                                [m] => 686ad371e2b2a04409a7e371af79b6c35801ac08
//                                [zm] => http://cmsapi.kandian.360.cn//trans?m=06d9d22b94f213fb9d9da52fcff915e7d58c5a8e&id=1124515206350104&fmt=json&news_from=1&news_id=1892&url=http%3A%2F%2Fwww.ce.cn%2Fxwzx%2Fshgj%2Fgdxw%2F201512%2F10%2Ft20151210_7458877.shtmlhttp://cmsapi.kandian.360.cn//trans?m=06d9d22b94f213fb9d9da52fcff915e7d58c5a8e&id=1124515206350104&fmt=json&news_from=1&news_id=1892&url=http%3A%2F%2Fwww.ce.cn%2Fxwzx%2Fshgj%2Fgdxw%2F201512%2F10%2Ft20151210_7458877.shtml
//                        )
    public int id;
    public String title;
    public String summary;
    public String news_type;
    public String m;
    public String zm;
    public String url;
    public String url_md5;

    @Override
    public String toString() {
        return "PushInfo{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", news_type='" + news_type + '\'' +
                ", m='" + m + '\'' +
                ", zm='" + zm + '\'' +
                ", url='" + url + '\'' +
                ", url_md5='" + url_md5 + '\'' +
                '}';
    }
}
