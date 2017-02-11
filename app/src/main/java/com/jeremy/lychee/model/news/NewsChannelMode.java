package com.jeremy.lychee.model.news;

import com.jeremy.lychee.db.NewsChannel;

import java.util.List;

public class NewsChannelMode {

    /**
     * key : 64a1e6246a4da15e2a2c0a5be0ad7d5c
     * list : [{"cid":"recommend","cname":"要闻","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"要闻","init":"1","is_start":"0"},{"cid":"polity","cname":"时政","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"时政","init":"1","is_start":"0"},{"cid":"fun","cname":"娱乐","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"娱乐","init":"1","is_start":"0"},{"cid":"it","cname":"科技","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"科技","init":"1","is_start":"0"},{"cid":"economy","cname":"财经","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"财经","init":"1","is_start":"0"},{"cid":"sport","cname":"体育","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"体育","init":"1","is_start":"0"},{"cid":"militery","cname":"军事","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"军事","init":"1","is_start":"0"},{"cid":"militery","cname":"文艺","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"文艺范","init":"0","is_start":"1"},{"cid":"art","cname":"文艺","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"文艺范","init":"0","is_start":"1"},{"cid":"life","cname":"生活","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"生活家","init":"0","is_start":"1"},{"cid":"militery","cname":"fashion","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"时尚族","init":"0","is_start":"1"},{"cid":"militery","cname":"dimension","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"二次元","init":"0","is_start":"1"},{"cid":"militery","cname":"essay","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"占星师","init":"0","is_start":"1"},{"cid":"duanzi","cname":"搞笑","icon":"http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101","tagname":"段子手","init":"0","is_start":"1"}]
     */

    private String key;
    /**
     * cid : recommend
     * cname : 要闻
     * icon : http://p7.qhimg.com/dmfd/300_150_/t01c62296fb99a596ee.png?size=100x101
     * tagname : 要闻
     * init : 1
     * is_start : 0
     */

    private List<NewsChannel> list;

    public void setKey(String key) {
        this.key = key;
    }

    public void setList(List<NewsChannel> list) {
        this.list = list;
    }

    public String getKey() {
        return key;
    }

    public List<NewsChannel> getList() {
        return list;
    }

}
