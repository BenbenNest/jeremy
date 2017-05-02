package com.jeremy.demo.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

//使用的接口来自：http://gank.io/api

public interface RetroApiService {
    /**
     * 获取所有频道
     */
    @GET("data/Android/10/1")
    Observable<ResponseBody> getAll();


//    /**
//     * 内容页详情
//     *
//     * @param id
//     * @return
//     */
//    @GET("/api/1/content/{id}")
//    Call<ContentArticle> getContent(@Path("id") String id);
//
//    /**
//     * 获取首页列表数据
//     *
//     * @param count
//     * @return
//     */
//    @GET("/api/1/resources/promotions")
//    Call<BaseModel<ArrayList<Stream.DataBean>>> getPromotions(@Query("count") int count);
//
//    /**
//     * 获取首页列表数据（offset表示从哪个位置开始获取）
//     *
//     * @param count
//     * @return
//     */
//    @GET("/api/1/resources/stream")
//    Call<BaseModel<ArrayList<Stream.DataBean>>> getStream(@Query("count") int count, @Query("offset") int offset);
//
//    /**
//     * 检查APP更新信息
//     *
//     * @param app_ver_code
//     * @param package_md5
//     * @param brand
//     * @param model
//     * @param os
//     * @param os_ver
//     * @return
//     */
//    @GET("/api/1/app/check_update")
//    Call<BaseModel<UpdateInfo>> getUpdateInfo(@Query("app_ver_code") int app_ver_code, @Query("package_md5") String package_md5, @Query("brand") String brand, @Query("model") String model, @Query("os") String os, @Query("os_ver") String os_ver);
//
//    /**
//     * 点赞
//     */
//    @POST("/api/1/comment/{id}/like")
//    Call<BaseModel> commentLike(@Path("id") String id);
//
//    /**
//     * 取消点赞
//     */
//    @POST("/api/1/comment/{id}/unlike")
//    Call<BaseModel> commentUnLike(@Path("id") String id);
//
//    @GET("/api/1/{type}/{id}/comment_section")
//    Call<BaseModel<List<NewComments.DataBean>>> getCommentList(@Path("type") String type, @Path("id") String id);
//
//    /**
//     * 获取HTML模板
//     */
//    @GET("/dist/html/android-container.html")
//    Call<ResponseBody> getHtmlTemplate(@Header("If-None-Match") String header);
//
//    @GET("/dist/html/android-container.html")
//    Call<ResponseBody> getHtmlTemplateNoCache();
//
//    /**
//     * 获取更多评论
//     *
//     * @param url
//     * @return
//     */
//    @GET
//    Call<BaseModel<List<Comments.DataBean>>> getMoreComments(@Url String url);
//
//    /**
//     * 删除评论
//     *
//     * @param id
//     * @return
//     */
//    @GET("/api/1/comment/delete/{id}")
//    Call<BaseModel> delComment(@Path("id") String id);
//
//    /**
//     * 通用的请求方式
//     * 这里主要用来下载图片
//     *
//     * @param url
//     * @return
//     */
//    @GET
//    Call<ResponseBody> downLoadImage(@Url String url);
//
//    /**
//     * 获取未读消息数量
//     */
//    @GET("/api/1/message/history_unread_num")
//    Call<BaseModel<Integer>> getUnreadMessageCount(@QueryMap Map<String, String> para);
//
//
//    /**
//     * 喜欢(event)
//     */
//    @POST("/api/1/event/{id}/like")
//    Call<BaseModel<EventLikeModel>> eventLike(@Path("id") String id);
//
//    /**
//     * 不喜欢(event)
//     */
//    @POST("/api/1/event/{id}/unlike")
//    Call<BaseModel<EventLikeModel>> eventUnLike(@Path("id") String id);
//
//    /**
//     * 喜欢(Content)
//     */
//    @POST("/api/1/content/{id}/like")
//    Call<BaseModel<EventLikeModel>> contentLike(@Path("id") String id);
//
//    /**
//     * 不喜欢(Content)
//     */
//    @POST("/api/1/content/{id}/unlike")
//    Call<BaseModel<EventLikeModel>> contentUnLike(@Path("id") String id);
//
//    /**
//     * checkin
//     */
//    @GET("/api/1/app/daily_checkin")
//    Call<BaseModel<ActionResponse>> isCheckIn();
//
//    @GET("/api/1/comment/{id}")
//    Call<BaseModel<CommentDetail>> getComment(@Path("id") String id, @Query("count") int count, @Query("offset") int offset);
//
//    /**
//     * 发表评论
//     */
//    @FormUrlEncoded
//    @POST("/api/1/{type}/{id}/comment")
//    Call<BaseModel<ActionResponse>> sendComment(@Path("type") String type, @Path("id") String id,
//                                                @Field("comment") String comment, @Field("reply_to") String reply_to);
//
//    /**
//     * 拉取专题数据
//     */
//    @GET("/api/1/resources/stream")
//    Call<BaseModel<TopicBean>> getTopic(@Query("count") int count, @Query("offset") int offset, @Query("collection_id") String collection_id);
//
//    @GET("/api/1/comment/{id}/snap")
//    Call<CommentsDetailSnap> getCommentSnap(@Path("id") String id, @Query("count") int count, @Query("offset") int offset);
//
//    @GET
//    Call<Comments> getMyComments(@Url String url, @Query("count") int count, @Query("offset") int offset);
//
//    @GET("/api/1/content/{id}")
//    Call<ContentVote> getVoteDetail(@Path("id") String id);
//
//    @FormUrlEncoded
//    @POST("/api/1/content/{id}/vote")
//    Call<BaseResponse> contentVote(@Path("id") String id, @FieldMap Map<String, String> para);
//
//    /**
//     * 获取简单的key value config
//     */
//    @GET("/api/1/app/key_value_config")
//    Call<BaseModel<KeyValueConfigBean>> getKeyValueConfig(@Query("key") String key);
//
//    /**
//     * 重新获取推流地址
//     */
//    @POST
//    Call<LiveBaseModel<Address>> liveWatchGetAddress(@Url String url,
//                                                     @Header("AppKey") String AppKey,
//                                                     @Header("Nonce") String Nonce,
//                                                     @Header("CurTime") String CurTime,
//                                                     @Header("CheckSum") String CheckSum,
//                                                     @Body LiveChannelID object);
//
//    /**
//     * 获取所有频道
//     */
//    @POST
//    Call<LiveBaseModel<LiveChannelList>> liveWatchGetChannels(@Url String url,
//                                                              @Header("AppKey") String AppKey,
//                                                              @Header("Nonce") String Nonce,
//                                                              @Header("CurTime") String CurTime,
//                                                              @Header("CheckSum") String CheckSum,
//                                                              @Body LiveChannelParam object);
//
//    /**
//     * 直播抽奖接口  application/x-www-form-urlencoded
//     */
//    @POST("/api/1/lottery/{lottery_id}/draw")
//    Call<LotteryBaseModel<LiveLottery>> liveLottery(@Path("lottery_id") int lottery_id);
//
//    /**
//     * 获取直播详情
//     *
//     * @param id
//     * @return
//     */
//    @GET("/api/1/content/{id}")
//    Call<BaseModel<LiveDetailModel>> getLiveDetail(@Path("id") String id);
//
//
//    /**
//     * 获取所有频道
//     */
//    @FormUrlEncoded
//    @POST
//    Call<JsonObject> liveWatchCreateAccount(@Url String url,
//                                            @HeaderMap Map<String, String> map,
//                                            @FieldMap Map<String, String> object);
//
//    /**
//     * 创建聊天室
//     */
//    @FormUrlEncoded
//    @POST
//    Call<LiveChatController.RoomModel> liveWatchCreateChatRoom(@Url String url,
//                                                               @HeaderMap Map<String, String> map,
//                                                               @FieldMap Map<String, String> object);
//
//
//    /**
//     * 获取聊天室信息
//     */
//    @FormUrlEncoded
//    @POST
//    Call<LiveChatController.RoomModel> liveWatchGetChatRoomInfo(@Url String url,
//                                                                @HeaderMap Map<String, String> map,
//                                                                @FieldMap Map<String, String> object);
//
//    @GET("/api/1/lifestyle/livestream/account")
//    Call<BaseModel<LiveAccount>> liveGetNetEaseAccount(@Query("nonce") String nonce, @Query("timestamp") long timestamp, @Query("sig") String sig);
//
//
//    @POST
//    Call<LiveBaseModel<LiveChannel>> liveWatchGetChannelState(@Url String url,
//                                                              @HeaderMap Map<String, String> map,
//                                                              @Body LiveChannelID object);
//
//    @POST("/api/1/lottery/{id}/{index}/status")
//    Call<BaseModel<LiveLottery>> liveCheckLotteryState(@Path("id") int id, @Path("index") int index);
//
//    /**
//     * 新的全屏推广接口
//     */
//    @GET("/api/1/resources/promotions/v2")
//    Call<BaseModel<NewPromotionBean>> getNewPromotionCall();


}
