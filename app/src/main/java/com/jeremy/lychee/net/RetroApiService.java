package com.jeremy.lychee.net;

import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.news.NewsDetailContent;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by benbennest on 16/6/5.
 */
public interface RetroApiService {


    //获取新闻详情
    @GET("/trans?m=b21276e78fd22e7a9b64280fd6f479d78164fb88&fmt=json&news_from=1&news_id=195648&url=http%3A%2F%2Fnews.btime.com%2Fpicture%2F20160605%2Fn195648.shtml&os_type=Android&timestamp=1465185693&sid=652u084tq68f8k0a6kp5l4iqr6&os=KOT49H.I9508ZMUHNG2&token=afa4b1e17a4fc0d110f8784d0f56fd82&os_ver=19&carrier=&src=lx_android&ver=1.0.13.2&net=wifi&channel=dev&os_type=Android&sign=b7d148c")
    Observable<ModelBase<NewsDetailContent>> getNewsDetail();

//    http://api.btime.com/trans?m=b21276e78fd22e7a9b64280fd6f479d78164fb88&fmt=json&news_from=1&news_id=195648&url=http%3A%2F%2Fnews.btime.com%2Fpicture%2F20160605%2Fn195648.shtml&os_type=Android&timestamp=1465185693&sid=652u084tq68f8k0a6kp5l4iqr6&os=KOT49H.I9508ZMUHNG2&token=afa4b1e17a4fc0d110f8784d0f56fd82&os_ver=19&carrier=&src=lx_android&ver=1.0.13.2&net=wifi&channel=dev&os_type=Android&sign=b7d148c
}
