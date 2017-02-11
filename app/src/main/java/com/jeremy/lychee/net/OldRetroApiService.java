package com.jeremy.lychee.net;

import com.google.gson.JsonObject;
import com.jeremy.lychee.db.LiveColumnListData;
import com.jeremy.lychee.db.LiveHotItem;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.db.WeMediaTopic;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.live.IsPlayingLive;
import com.jeremy.lychee.model.live.LiveChannel;
import com.jeremy.lychee.model.live.LiveChannelStreams;
import com.jeremy.lychee.model.live.LiveChannelsAndCategory;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.model.live.ShorVideoPostResult;
import com.jeremy.lychee.model.live.ShuiDiVideo;
import com.jeremy.lychee.model.live.TVList;
import com.jeremy.lychee.model.live.VideoRelateNews;
import com.jeremy.lychee.model.news.ChannelCategory;
import com.jeremy.lychee.model.news.Comment;
import com.jeremy.lychee.model.news.NewsChannelMode;
import com.jeremy.lychee.model.news.NewsDetail;
import com.jeremy.lychee.model.news.NewsRelated;
import com.jeremy.lychee.model.news.NewsSubjectObject;
import com.jeremy.lychee.model.news.TransmitedChannel;
import com.jeremy.lychee.model.update.UpdateInfo;
import com.jeremy.lychee.model.user.ColumnBaseInfo;
import com.jeremy.lychee.model.user.FeedNewsEntity;
import com.jeremy.lychee.model.user.HotNews;
import com.jeremy.lychee.model.user.HotWeMediaChannel;
import com.jeremy.lychee.model.user.ImgCodeData;
import com.jeremy.lychee.model.user.MyFans;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.model.user.UserMessage;
import com.jeremy.lychee.model.live.ColumnChannel;
import com.jeremy.lychee.model.live.ColumnItem;
import com.jeremy.lychee.model.live.LiveDiscoveryColumnGroup;
import com.jeremy.lychee.model.live.LiveDiscoveryTitle;
import com.jeremy.lychee.model.live.Program;
import com.jeremy.lychee.model.news.CommentData;
import com.jeremy.lychee.model.news.Config;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.model.news.ResultCmt;
import com.jeremy.lychee.model.news.SquareModel;
import com.jeremy.lychee.model.news.Zan;

import java.util.List;


import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface OldRetroApiService {

    //获取自媒体(自频道)详情接口
    @GET("/media/getUserChannel")
    Observable<ModelBase<WeMediaChannel>> getUserChannel(@Query("uid") String uid);

    //编辑频道接口
    @FormUrlEncoded
    @POST("/media/editUserChannel")
    Observable<ModelBase<Object>> editUserChannel(@Field("id") String id,
                                                  @Field("name") String name,
                                                  @Field("summary") String summary,
                                                  @Field("backimg") String backimg,
                                                  @Field("icon") String icon,
                                                  @Field("is_public") int is_public);

    //创建频道接口
    @FormUrlEncoded
    @POST("/media/createUserChannel")
    Observable<ModelBase<Object>> createUserChannel(/*TODO*/);


    //创建自媒体(自频道)接口
    @FormUrlEncoded
    @POST("/media/create")
    Observable<ModelBase<WeMediaChannel>> createMediaChannel(@Field("name") String name,
                                                             @Field("summary") String summary);

    //编辑专辑接口
    @FormUrlEncoded
    @POST("/media/edit")
    Observable<ModelBase<Object>> editAlbum(@Field("sub_id") String sub_id,
                                            @Field("name") String name);

    //删除专辑接口
    @FormUrlEncoded
    @POST("/media/delete")
    Observable<ModelBase<Object>> deleteAlbum(@Field("sub_id") String sub_id);

    //获取某人的订阅接口
    @GET("/media/getusersub")
    Observable<ModelBase<List<WeMediaChannel>>> getUserSubMediaChannelList(@Query("pageNo") int pageNo,
                                                                           @Query("pageRow") int pageRow,
                                                                           @Query("uid") String uid);

    //获取某人创建的自频道接口
    @GET("/media/getusercreate")
    Observable<ModelBase<List<WeMediaChannel>>> getUserCreateMediaChannelList(@Query("pageNo") int pageNo,
                                                                              @Query("pageRow") int pageRow,
                                                                              @Query("uid") String uid);

    //获取某用户转推feed流接口
    @GET("/media/getuserfeed")
    Observable<ModelBase<List<FeedNewsEntity>>> getUserFeedList(@Query("start") int start,
                                                                @Query("uid") String uid,
                                                                @Query("limit") int limit);

    //自媒体(自频道)首页接口
    @GET("/media/home")
    Observable<ModelBase<List<FeedNewsEntity>>> getUserHomeArticle(@Query("start") int start,
                                                                   @Query("limit") int limit);

    //自媒体(自频道)热门文章接口
    @GET("/news/hotnews")
    Observable<ModelBase<List<NewsListData>>> getUserHotArticle(@Query("pageNo") int pageNo,
                                                                  @Query("pageRow") int pageRow);

    //获取热门自媒体(自频道)接口 pageNo: 1代表热门频道，2代表换一换，默认1
    @GET("/media/gethot")
    Observable<ModelBase<List<HotWeMediaChannel>>> getUserHotChannelList(@Query("pageNo") int pageNo,
                                                                         @Query("pageRow") int pageRow);

    //获取自媒体(自频道)详情接口
    @Deprecated
    @GET("/media/getinfo")
    Observable<ModelBase<ColumnBaseInfo>> getColumnBaseInfo(@Query("sub_id") int id);

    //获取自媒体feed流首页接口
    @GET("/media/getfeed")
    Observable<ModelBase<List<FeedNewsEntity>>> getFeed(@Query("start") int start,
                                                        @Query("sub_id") String sub_id,
                                                        @Query("limit") int limit);

    //订阅自媒体(自频道)接口
    @GET("/media/sub")
    Observable<ModelBase> subscribeColumn(@Query("c_id") int c_id,
                                          @Query("type") int type);

    //取消订阅自媒体(自频道)接口
    @GET("/media/unsub")
    Observable<ModelBase> unSubscribeColumn(@Query("c_id") int c_id,
                                            @Query("type") int type);

    //赞
    @GET("/media/feedLike")
    Observable<ModelBase> feedLike(@Query("feed_id") String feed_id,
                                   @Query("sign") String sign );

    //新闻转推接口
    @FormUrlEncoded
    @POST("/media/transmit")
    Observable<ModelBase<Object>> transmitNews(@Field("nid") String nid,
                                               @Field("url") String url,
                                               @Field("sub_id") String sub_id,
                                               @Field("content") String content,
                                               @Field("type") int type,
                                               @Field("title") String title,
                                               @Field("sign") String sign
    );

    //新闻创作
    @FormUrlEncoded
    @POST("/media/transmit")
    Observable<ModelBase<Object>> createArticle(
            @Field("sub_id") String sub_id,
            @Field("content") String content,
            @Field("type") int type,
            @Field("title") String title,
            @Field("article") String article,
            @Field("sign") String sign
    );

    //文章转移接口
    @GET("/media/moveFeed")
    Observable<ModelBase<Object>> moveFeed(@Query("src_sub_id") String src_sub_id,
                                           @Query("to_sub_id") String to_sub_id,
                                           @Query("feed_id") String feed_id,
                                           @Query("stay_feed_id") String stay_feed_id);

    //新闻转推自频道列表接口
    @GET("/media/getnewssub")
    Observable<ModelBase<List<TransmitedChannel>>> getTransmitedChannelList(@Query("nid") String nid,
                                                                            @Query("url") String url,
                                                                            @Query("start_time") long start_time,
                                                                            @Query("limit") int limit);

    @GET("/media/getfans")
    Observable<ModelBase<List<MyFans>>> getFans(@Query("c_id") String cid,
                                                @Query("pageNo") int pageNo,
                                                @Query("pageRow") int pageRow);

    //获取视频流地址接口
    @GET
    Observable<ModelBase<LiveChannelStreams>> getLiveChannelStreams(@Url String url);

    //获取视频列表接口
    @GET("/video/playlist")
    Observable<ModelBase<List<LiveVideoInfo>>> getLiveVideoList(@Query("id") int id, @Query("type_id") int type_id, @Query("tag") String tag, @Query("page") int page, @Query("limit") int item_per_page);

    //短视频上传接口
    @Multipart
    @POST("/video/upreplay")
    Observable<ModelBase<ShorVideoPostResult>> postShortVideo(@Part("title") RequestBody title,
                                                              @Part("video_desc") RequestBody desc,
                                                              @Part("video_length") int length,
                                                              @Part("image") RequestBody thumbnailImage,
                                                              @Part("file") RequestBody videoContent,
                                                              @Part("latitude") Float latitude,
                                                              @Part("longitude") Float longitude,
                                                              @Part("poi_name") RequestBody poi_name);

    @GET("/api/config")
    Observable<ModelBase<Config>> getConfig();

    @GET("/api/channel")
    Observable<ModelBase<NewsChannelMode>> getNewChannel();

    //-----广场接口--------
//    @GET("/api/square")
//    Observable<ModelBase<SquareModel>> getSquareData(@Query("pos") int pos);
    @GET("/api/squares")
    Observable<ModelBase<SquareModel>> getSquareData(@Query("pos") int pos);

    @GET("/news/getlmlist")
    Observable<ModelBase<List<SquareModel.ElementModel>>> refreshSquareArticle(@Query("id") String id);
    //-------------

//    @GET("/news/focus")
//    Observable<ModelBase<List<NewsListData>>> getNewsImageList(@Query("cid") String id);

    @GET("/news/scene")
    Observable<ModelBase<List<NewsListData>>> getNewsSceneList(@Query("cid") String id, @Query("sceneid") String sceneid,
                                                               @Query("init") String init);

    @GET("/news/getlist")
    Observable<ModelBase<List<NewsListData>>> getNewsDataList(@Query("cid") String id, @Query("init") String init,
                                                              @Query("refreshcount") int refreshCount);

    @GET("/api/getcategory")
    Observable<ModelBase<ChannelCategory>> getChannelCategory();

    @GET("/api/getchannel")
    Observable<ModelBase<List<ChannelCategory.ChannelInfo.SubChannel>>> getSubChannel(@Query("cid") String cid);

    @GET("/comment/lists")
    Observable<ResultCmt<CommentData>> getCommentList(
            @Query("url") String url,
            @Query("type") String type,
            @Query("num") String num,
            @Query("start_id") String start_id,
            @Query("token") String token,
            @Query("uid") String uid);

    @GET("/comment/hot")
    Observable<ResultCmt<CommentData>> getCommentHotList(@Query("url") String url);

    @GET("/comment/like")
    Observable<Result> diggComment(
            @Query("url") String url,
            @Query("comment_id") String comment_id,
            @Query("uid") String uid);

    @GET("/comment/post")
    Observable<ResultCmt<Comment>> sendComment(
            @Query("url") String url,
            @Query("message") String message,
            @Query("token") String token,
            @Query("uid") String uid,
            @Query("sign") String sign);

    @POST
    Observable<Result<NewsDetail>> getZmData(@Url String url);

    /**
     * 直播 -> 我的 -> 获取直播、上传历史列表
     * http://cmsapi.kandian.360.cn//video/gethistory/
     *
     */
    @GET("/video/gethistory")
    Observable<Result<List<LiveHotItem>>> getHistoryLives(@Query("page") int page,
                                                          @Query("limit") int limit,
                                                          @Query("uid") String uid);

    /**
     * @return 获取新闻相关数据
     */
    @GET("/news/detail")
    Observable<Result<NewsRelated>> getNewsDetail(@Query("url") String url, @Query("nid") String nid, @Query("feed_id") String feed_id);

    @GET("/news/doshare")
    Observable<Result> doShare(@Query("url") String url, @Query("nid") String nid, @Query("type") String type, @Query("sign") String sign);


    /**
     * 删除历史数据
     *
     * @param id       live id
     */
    @GET("/video/delhistory")
    Observable<Result> delHistoryLive(@Query("id") String id, @Query("sign") String sign);

    /**
     * @param rand 	随机数
     * @param u_sign 	参照签名规则
     * @return 用户注册验证码
     */
    @GET("/user/rimgcode")
    Observable<Result<ImgCodeData>> getImgCode(@Query("rand") String rand, @Query("u_salt") String salt, @Query("u_sign") String u_sign );


    /**
     * @param rtime 请求时间戳(请求图像下发的时间戳)
     * @param mobile    手机号
     * @param imgcode   图像标识码
     * @param vcode 图像验证码
     * @param ac    操作标识reg或者findpwd
     * @param u_sign    参照签名规则
     * @return 短信验证码
     */
    @GET("/user/rcode")
    Observable<Result> getMsgCode(
            @Query("rtime") String rtime,
            @Query("mobile") String mobile,
            @Query("imgcode") String imgcode,
            @Query("vcode") String vcode,
            @Query("ac") String ac,
            @Query("u_salt") String u_salt,
            @Query("u_sign") String u_sign );

    /**
     * @param mobile    	手机号
     * @param password  密码加密后的串(参照密码加密规则部分)
     * @param code  短信验证码
     * @param u_salt    盐
     * @param u_sign    参照签名规则
     *
     * @return 注册
     */
    @GET("/user/reg")
    Observable<Result<User>> register(
            @Query("mobile") String mobile,
            @Query("password") String password,
            @Query("code") String code,
            @Query("u_salt") String u_salt,
            @Query("u_sign") String u_sign );

    /**
     * @param mobile    	手机号
     * @param password  密码加密后的串(参照密码加密规则部分)
     * @param code  短信验证码
     * @param u_salt    盐
     * @param u_sign    参照签名规则
     *
     * @return 提交找回密码
     */
    @GET("/user/findpwd")
    Observable<Result<User>> findPwd(
            @Query("mobile") String mobile,
            @Query("password") String password,
            @Query("code") String code,
            @Query("u_salt") String u_salt,
            @Query("u_sign") String u_sign );


    /**
     * 获取用户详情接口
     * @param sid 	登录会话标识
     * @param real_uid 用户系统uid
     * @param u_salt 盐
     * @param u_sign 参照签名规则
     * @return
     */
    @GET("/user/getinfo")
    Observable<Result<User>> getinfo(
            @Query("sid") String sid,
            @Query("real_uid") String real_uid,
            @Query("u_salt") String u_salt,
            @Query("u_sign") String u_sign );

    /**
     *
     * @param mobile    手机号
     * @param password  密码加密后的串(参照密码加密规则部分)
     * @param imgcode   图像标识码
     * @param vcode 图像验证码
     * @param u_salt    盐
     * @param u_sign    参照签名规则
     * @return  登陆
     */
    @GET("/user/login")
    Observable<Result<User>> login(
            @Query("mobile") String mobile,
            @Query("password") String password,
            @Query("imgcode") String imgcode,
            @Query("vcode") String vcode,
            @Query("u_salt") String u_salt,
            @Query("u_sign") String u_sign );

    //SSO登录后获取用户信息
    // from 1是微博，2为微信
    // fuid 微博是微博uid，微信为空，但是code必传
    // access_token 微博必传
    // code 微信必传
    // u_sign 参照签名规则
    // u_salt 随机8位字符串和数字组合
    // u_time 请求时间戳
    @FormUrlEncoded
    @POST
    Observable<ModelBase<User>> getUserInfo(@Url String url,
                                            @Field("from") String from,
                                            @Field("fuid") String fuid,
                                            @Field("access_token") String access_token,
                                            @Field("code") String code,
                                            @Field("u_sign") String u_sign,
                                            @Field("u_salt") String u_salt,
                                            @Field("u_time") String u_time,
                                            @Field("src") String src);

    @GET
    Observable<List<HotNews>> getHotWords(@Url String url,
                                          @Query("type") String type,
                                          @Query("sign") String sign);

    @GET("/support/punch")
    Observable<ModelBase> punch(@Query("key") String key);

    @GET("/support/getpunch")
    Observable<ModelBase<Zan>> getpunch(@Query("key") String key,
                                        @Query("sign") String sign);

    //删除我的转推接口
    @FormUrlEncoded
    @POST("/media/deltransmit")
    Observable<ModelBase<Object>> deleteTransmit(@Field("feed_id") String feedId, @Field("sign") String sign);

    @GET("/media/sharearticle")
    Observable<ModelBase> getTitleByUrl(@Query("url") String url);

    @GET("/api/checkupdate")
    Observable<ModelBase<UpdateInfo>> checkUpdate();

    @FormUrlEncoded
    @POST("/api/stats")
    Observable<ModelBase> postHitLogs(@Field("s") String s, @Field("sign") String sign);

//    @GET("/video/livestart")
//    Observable<ModelBase<JsonObject>> liveStart(@Query("title") String title,
//                                                @Query("video_desc") String video_desc,
//                                                @Query("image") String image,
//                                                @Query("latitude") String latitude,
//                                                @Query("longitude") String longitude,
//                                                @Query("poi_name") String poi_name,
//                                                @Query("sn") String sn,
//                                                @Query("is_seg") String is_seg,
//                                                @Query("pid") String pid,
//                                                @Query("lanid") String lanid);

    @Multipart
    @POST("/video/livestart")
    Observable<ModelBase<JsonObject>> liveStart(@Part("title") RequestBody title,
                                                @Part("video_desc") RequestBody video_desc,
                                                @Part("image") RequestBody image,
                                                @Part("audio") RequestBody audio,
                                                @Part("latitude") RequestBody latitude,
                                                @Part("longitude") RequestBody longitude,
                                                @Part("poi_name") RequestBody poi_name,
                                                @Part("sn") RequestBody sn,
                                                @Part("is_seg") RequestBody is_seg,
                                                @Part("pid") RequestBody pid,
                                                @Part("lanid") RequestBody lanid);

    @Multipart
    @POST("/video/liveend")
    Observable<ModelBase<JsonObject>> liveEnd(@Part("title") RequestBody title,
//                                    @Part("video_desc") RequestBody video_desc,
                                              @Part("video_length") RequestBody video_length,
//                                              @Part("image") RequestBody image,
//                                    @Field("latitude") String latitude,
//                                    @Field("longitude") String longitude,
//                                    @Field("poi_name") String poi_name,
                                              @Part("sn") RequestBody sn,
                                              @Part("id") RequestBody id,
                                              @Part("is_seg") RequestBody is_seg,
                                              @Part("pid") RequestBody pid);

    @GET("/video/getlivepeople")
    Observable<ModelBase<JsonObject>> getLivePeople(@Query("sn") String sn);


    @GET("/video/getmenu")
    Observable<ModelBase<List<Program>>> getMenu(@Query("channel_id") String channel_id);

    @GET("/video/channel")
    Observable<ModelBase<LiveChannelsAndCategory>> getColumnChannel();

    @GET("/video/channels")
    Observable<ModelBase<List<TVList>>> getColumnChannels();

    @GET("/video/index")
    Observable<ModelBase<List<LiveDiscoveryColumnGroup>>> getLiveDiscoveryVideoList();

    @GET("/video/getlive")
    Observable<ModelBase<IsPlayingLive>> getIsPlayingLive(@Query("channel_id") String channel_id);

    @GET("/video/getrelate")
    Observable<ModelBase<JsonObject>> getRelate(@Query("channel_id") String channel_id);

    //获取推荐列表
    @GET("/video/getlist")
    Observable<Result<List<LiveChannel>>> getRecommendDataList(@Query("cid") String cid, @Query("lid") String lid, @Query("page") String start, @Query("limit") String limit,@Query("init") String init);

    //获取推荐列表
    @GET("/video/getlist")
    Observable<Result<List<LiveColumnListData>>> getRecommendDataListNew(@Query("cid") String cid, @Query("lid") String lid, @Query("page") String start, @Query("limit") String limit,@Query("init") String init);


    @GET("/video/category")
    Observable<ModelBase<List<LiveDiscoveryTitle>>> getLiveDiscoveryTitleList();

    @GET("/video/getcolumn")
    Observable<ModelBase<List<ColumnItem>>> getColumn(@Query("channel_id") String channel_id,
                                                      @Query("cid") String cid,
                                                      @Query("page") String page,
                                                      @Query("limit") String limit);

    //获取我的消息
    @GET("/message/messageList")
    Observable<ModelBase<List<UserMessage>>> getUserMessage(@Query("start") String start, @Query("limit") int limit);

    //获取更多栏目
    @GET("/video/getcolumnvideo")
    Observable<ModelBase<List<LiveChannel>>> getcolumnvideo(@Query("channel_id") String channel_id,
                                                            @Query("column_id") String column_id,
                                                            @Query("page") String page,
                                                            @Query("limit") String limit
    );

    @GET("/news/feedback")
    Observable<ModelBase<Object>> newsFeedback(@Query("ac") String ac,
                                  @Query("url") String url,
                                  @Query("nid") String nid,
                                  @Query("news_type") String newType,
                                  @Query("sign") String sign);

    @GET("/api/syncsub")
    Observable<ModelBase<Object>> newsChannelAsyn(@Query("sub") String sub,
                                                  @Query("sign") String sign);
    //获取直播回放
    @GET("/video/getchannelreplay")
    Observable<ModelBase<List<LiveHotItem>>> getchannelreplay(@Query("channel_id") String channel_id,
                                                            @Query("page") String page,
                                                            @Query("limit") String limit
    );

    @FormUrlEncoded
    @POST("/api/log")
    Observable<ModelBase<Object>> appLog(
            @Field("tag") String tag,
            @Field("content") String content
    );
    @GET("/video/channels")
    Observable<ModelBase<List<ColumnChannel>>> getLiveChannels();

    @GET("/video/getRelateNews")
    Observable<ModelBase<List<VideoRelateNews>>> getVideoRelateNews(@Query("vid") String videoId);

    //获取热点数据
    @GET("/video/gethot")
    Observable<Result<List<LiveHotItem>>> getLiveHotDataList();

    //话题列表接口
    @GET("/topic/topicList")
    Observable<ModelBase<List<WeMediaTopic>>> getHotTopicList(@Query("pageNo") int pageNo,
                                                              @Query("pageRow") int pageRow);

    //获取订阅话题列表接口
    @GET("/media/getUserTopic")
    Observable<ModelBase<List<WeMediaTopic>>> getUserTopicList(@Query("pageNo") int pageNo,
                                                               @Query("pageRow") int pageRow,
                                                               @Query("uid") String uid);

    //话题新闻列表接口
    @GET("/topic/topicNewsList")
    Observable<ModelBase<List<NewsListData>>> getTopicNewsList(@Query("topicId") int topicId,
                                                                      @Query("start") int start,
                                                                      @Query("limit") int limit);

    //小水滴直播接口
    @GET("/video/getshuidi")
    Observable<ModelBase<List<ShuiDiVideo>>> getShuiDiList(@Query("page") int page,
                                                               @Query("limit") int limit,
                                                               @Query("lid") String lid);


    //小水滴直播接口
    @GET("/Message/addMessage")
    Observable<ModelBase> addMessage(@Query("newsUid") String newsUid,
                                     @Query("messageType") String messageType,
                                     @Query("commentData") String commentData,
                                     @Query("nid") String nid,
                                     @Query("newsUrl") String newUrl);

    //是否订阅自媒体/话题接口
    @GET("/media/issub")
    Observable<ModelBase<List<String>>> getIsSub(@Query("c_id") int c_id,
                                                 @Query("type") int type);//频道id或者话题id

    //专题详情接口
    @GET("/news/special")
    Observable<ModelBase<NewsSubjectObject>> getNewsSubjectList(@Query("nid") String nid,
                                                                @Query("url") String url);

//    //专题详情接口
//    @GET("/news/special")
//    Observable<ModelBase<NewsSubjectObject>> getNewsSubjectList(@Query("nid") String nid,
//                                                                @Query("url") String url);
//
}
