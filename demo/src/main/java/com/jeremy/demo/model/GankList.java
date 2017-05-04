package com.jeremy.demo.model;

import java.util.List;

/**
 * Created by changqing.zhao on 2017/5/3.
 */
public class GankList {
    /**
     * error : false
     * results : [{"_id":"590859ce421aa90c7d49ad1f","createdAt":"2017-05-02T18:05:02.18Z","desc":"Android 多状态加载布局的开发 Tips","publishedAt":"2017-05-03T12:00:31.516Z","source":"chrome","type":"Android","url":"http://gudong.name/2017/04/26/loading_layout_practice.html","used":true,"who":"咕咚"},{"_id":"59088f4f421aa90c7a8b2aa7","createdAt":"2017-05-02T21:53:19.611Z","desc":"loading button","images":["http://img.gank.io/d426bc04-06e0-4574-911b-b609c7deb077"],"publishedAt":"2017-05-03T12:00:31.516Z","source":"chrome","type":"Android","url":"https://github.com/leandroBorgesFerreira/LoadingButtonAndroid","used":true,"who":"Jason"},{"_id":"5908a423421aa90c7fefdd27","createdAt":"2017-05-02T23:22:11.39Z","desc":"轻松实现炫酷动画，让 app 加载动画像加载图片一样简单。","images":["http://img.gank.io/13c4b927-fca6-4025-b781-4048b4e93b8a"],"publishedAt":"2017-05-03T12:00:31.516Z","source":"web","type":"Android","url":"http://www.liujun.info/2017/04/25/Lottie库实现直播礼物动画/","used":true,"who":"Jun Liu"},{"_id":"59092501421aa90c83a513bd","createdAt":"2017-05-03T08:32:01.0Z","desc":"带有进度动画的Android自定义提交按钮。","images":["http://img.gank.io/88bd8d9d-f9f0-4fdc-9dd2-22348f450620","http://img.gank.io/642a380c-f624-416c-a18e-9550e1dd61e6"],"publishedAt":"2017-05-03T12:00:31.516Z","source":"web","type":"Android","url":"https://github.com/Someonewow/SubmitButton","used":true,"who":"Unstoppable"},{"_id":"59094cde421aa90c83a513c4","createdAt":"2017-05-03T11:22:06.338Z","desc":"把 Awesome 系列再筛选一次，优质的 Android 开发库集合。","publishedAt":"2017-05-03T12:00:31.516Z","source":"chrome","type":"Android","url":"https://github.com/aritraroy/UltimateAndroidReference","used":true,"who":"带马甲"},{"_id":"59094e7d421aa90c7a8b2aac","createdAt":"2017-05-03T11:29:01.389Z","desc":"Java 实现的漂亮二维码生成工具","images":["http://img.gank.io/5462991b-0d0d-4635-9dc6-f8310da39586"],"publishedAt":"2017-05-03T12:00:31.516Z","source":"chrome","type":"Android","url":"https://github.com/SumiMakito/AwesomeQRCode","used":true,"who":"代码家"},{"_id":"590300d6421aa9544ed88a05","createdAt":"2017-04-28T16:44:06.945Z","desc":"这次换一种方式来讲解 Java 的反射机制","publishedAt":"2017-05-02T12:00:17.802Z","source":"web","type":"Android","url":"http://wingjay.com/2017/04/26/Java-%E6%8A%80%E6%9C%AF%E4%B9%8B%E5%8F%8D%E5%B0%84/","used":true,"who":"wingjay"},{"_id":"59032ae1421aa954511ebf2c","createdAt":"2017-04-28T19:43:29.533Z","desc":"Android App Optimization Using ArrayMap and SparseArray","publishedAt":"2017-05-02T12:00:17.802Z","source":"web","type":"Android","url":"https://blog.mindorks.com/android-app-optimization-using-arraymap-and-sparsearray-f2b4e2e3dc47","used":true,"who":"AMIT SHEKHAR"},{"_id":"590568e5421aa90c7a8b2a98","createdAt":"2017-04-30T12:32:37.940Z","desc":"可进行 UI 定制的日历组件，轻松完成签到日历功能。","images":["http://img.gank.io/ac4982fe-05a2-4704-ad12-11bdfc074769"],"publishedAt":"2017-05-02T12:00:17.802Z","source":"web","type":"Android","url":"https://github.com/shichaohui/EasyCalendar","used":true,"who":"石朝辉"},{"_id":"5906a055421aa90c7d49ad16","createdAt":"2017-05-01T10:41:25.883Z","desc":"APP一秒接入指纹识别SDK，0奔溃，0闪退，炒鸡稳定！额外支持三星和魅族6.0以下系统！","publishedAt":"2017-05-02T12:00:17.802Z","source":"web","type":"Android","url":"https://github.com/uccmawei/FingerprintIdentify?tt=22","used":true,"who":"Awei"}]
     */
    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 590859ce421aa90c7d49ad1f
         * createdAt : 2017-05-02T18:05:02.18Z
         * desc : Android 多状态加载布局的开发 Tips
         * publishedAt : 2017-05-03T12:00:31.516Z
         * source : chrome
         * type : Android
         * url : http://gudong.name/2017/04/26/loading_layout_practice.html
         * used : true
         * who : 咕咚
         * images : ["http://img.gank.io/d426bc04-06e0-4574-911b-b609c7deb077"]
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
