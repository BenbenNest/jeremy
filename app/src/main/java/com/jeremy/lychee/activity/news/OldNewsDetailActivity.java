package com.jeremy.lychee.activity.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.jeremy.lychee.activity.live.LivePlayerActivity;
import com.jeremy.lychee.activity.live.LiveVideoListActivity;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.activity.user.TransmitActivity;
import com.jeremy.lychee.activity.user.WeMediaAlbumDetailActivity;
import com.jeremy.lychee.activity.user.WeMediaChangeAlbumActivity;
import com.jeremy.lychee.activity.user.WeMediaChannelActivity;
import com.jeremy.lychee.activity.user.WeMediaTopicDetailActivity;
import com.jeremy.lychee.activity.user.WeMediaTransmitedChannelListActivity;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.channel.QHState;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.db.WeMediaTopic;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.manager.live.LiveBrowseHistoryManager;
import com.jeremy.lychee.manager.live.LiveVideoListManager;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.model.news.Comment;
import com.jeremy.lychee.model.news.CommentData;
import com.jeremy.lychee.model.news.NewsBrowse;
import com.jeremy.lychee.model.news.NewsDetail;
import com.jeremy.lychee.model.news.NewsListDataWrapper;
import com.jeremy.lychee.model.news.NewsMedia;
import com.jeremy.lychee.model.news.NewsRelated;
import com.jeremy.lychee.model.news.NewsVideoBody;
import com.jeremy.lychee.model.news.RelatedMedia;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.model.news.ResultCmt;
import com.jeremy.lychee.model.news.Zan;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.net.CommentRetroAdapter;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.preference.NewsListPreference;
import com.jeremy.lychee.preference.ZanPreference;
import com.jeremy.lychee.push.RetrievePushService;
import com.jeremy.lychee.share.weibo.WBShareManager;
import com.jeremy.lychee.share.wxapi.WXShareManager;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.utils.GsonUtils;
import com.jeremy.lychee.utils.ImageOptiUrl;
import com.jeremy.lychee.utils.ThreadUtils;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.videoplayer.LiveCloudConfigManager;
import com.jeremy.lychee.videoplayer.VideoPlayer;
import com.jeremy.lychee.videoplayer.VideoPlayerBottomProgressDecorator;
import com.jeremy.lychee.videoplayer.VideoPlayerPanelBasic;
import com.jeremy.lychee.videoplayer.VideoPlayerPanelNews;
import com.jeremy.lychee.videoplayer.VideoPlayerView;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.shareWindow.ShareManager;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.qihoo.sdk.report.QHStatAgent;
import com.trello.rxlifecycle.ActivityEvent;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OldNewsDetailActivity extends SlidingActivity implements NestedScrollView.OnScrollChangeListener {
    private final static String TAG = "OldNewsDetailActivity";
    /**
     * login request code
     */
    private static final int LOGIN_REQUESTCODE = 8;
    /**
     * send cmt request code
     */
    private static final int SEND_CMT_REQUESTCODE = 9;
    /**
     * cmts list request code
     */
    private static final int CMTS_REQUESTCODE = 10;


    public static final String NEW_ENTITY = "NEW_ENTITY";
    public static final String PUSH_ID = "PUSH_ID";
    private static final String ALBUM_ID = "ALBUM_ID";
    private static final String ALBUM_NAME = "ALBUM_POSTER";
    private static final String ALBUM_IS_MY = "IS_MY";
    private static final String TRANSMIT_ID = "TRANSMIT_ID";
    private static final String NEWS_TAG = "NEWS_TAG";
    private static final String NEWS_SCENE = "NEWS_SCENE";
    public static final String IMG_URL = "IMG_URL";

    private String html_url = "file:///android_asset/html/news-detail.html";
//        String html_url = "http://cmsapi.kandian.360.cn/static/app/html/news-detail-nodata.html";

    private volatile long mActivityResumeTimeStamp;
    private volatile long mActivityForgroundDuration;
    private NewsBrowse mNewsBrowseData;

    @Bind(com.jeremy.lychee.R.id.webView)
    WebView mWebView;

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar mToolBar;

    @Bind(com.jeremy.lychee.R.id.zan_btn)
    TextView zan_btn;

    @Bind(com.jeremy.lychee.R.id.share_btn)
    View share_btn;

    @Bind(com.jeremy.lychee.R.id.xpl_btn)
    View xpl_btn;

    @Bind(com.jeremy.lychee.R.id.go_top_btn)
    FloatingActionButton go_top_btn;

    @Bind(com.jeremy.lychee.R.id.loading_layout)
    View loading_layout;

    @Bind(com.jeremy.lychee.R.id.error_layout)
    View error_layout;

    @Bind(com.jeremy.lychee.R.id.article_icon_logo_iv)
    View article_icon_logo_iv;

    @Bind(com.jeremy.lychee.R.id.scrollview)
    NestedScrollView scrollview;

    private String url;
    private String title;
    private String zm_url;
    private String nid;
    private String description;
    private String pic;
    private String shareUrl;
    private int zan_num = 0;
    private boolean isLoginGoCmt;
    private String mAlbumId;
    private String mTransmitId;
    private String mNewsTag;//新闻分类频道（打点用）
    private String mNewsScene;//新闻场景（打点用）

    private String feed_id;
    private String newsUid;

    private int newsType;

    @Override
    public void onResume() {
        super.onResume();
        if (mActivityResumeTimeStamp == 0) {
            //APP被切换到前台
            mActivityResumeTimeStamp = System.currentTimeMillis() / 1000L;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityForgroundDuration +=
                System.currentTimeMillis() / 1000L - mActivityForgroundDuration;
        mActivityResumeTimeStamp = 0;
    }

    private NewsListDataWrapper mNewsEntity;

    public static void startActivity(Context context, NewsListData entity, int requestCode) {

        Intent intent = new Intent(context, OldNewsDetailActivity.class);
        intent.putExtra(NEW_ENTITY, new NewsListDataWrapper(entity));

        ThreadUtils.runOnUiThread(() -> {

            IntentUtils.startPreviewActivity(context, intent, requestCode);
        });
    }

    public static void startActivity(Context context,
                                     NewsListData entity,
                                     int requestCode,
                                     String albumId,//TODO：禁止从新闻底层页切换专辑，此参数后期删除
                                     String transmitId,
                                     String tag,
                                     String scene) {

        Intent intent = new Intent(context, OldNewsDetailActivity.class);
        intent.putExtra(NEW_ENTITY, new NewsListDataWrapper(entity));
        intent.putExtra(TRANSMIT_ID, transmitId);
        intent.putExtra(NEWS_TAG, tag);
        intent.putExtra(NEWS_SCENE, scene);

        ThreadUtils.runOnUiThread(() -> {
            IntentUtils.startPreviewActivity(context, intent, requestCode);
        });
    }

    private void getExtraParameters() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        mNewsEntity = intent.getParcelableExtra(NEW_ENTITY);
        if (mNewsEntity == null) {
            finish();
            return;
        }

        int push_id = intent.getIntExtra(PUSH_ID, -1);
        if (push_id > 0) {
            QHStatAgent.onPushEvent(this, String.valueOf(push_id), RetrievePushService.QDUS_PUSH_EVNET_TYPE_CLICK);
        }
        url = mNewsEntity.getUrl();
        title = mNewsEntity.getTitle();
        zm_url = mNewsEntity.getZm();
//        zm_url = "http://10.117.83.51:8080/trans?m=258e29ceeb919847c42c91d892fb6b5253a2ecaa&fmt=json&news_from=4&news_id=295799&url=http%3A%2F%2Fwebcms.kandian.360.cn%2Fnews%2Fvideo%2F20160309%2Fv295799.shtml&os_type=Android";
        nid = mNewsEntity.getNid();
        pic = mNewsEntity.getAlbum_pic();
        feed_id = mNewsEntity.getFeed_id();

        mAlbumId = intent.getStringExtra(ALBUM_ID);
        mTransmitId = intent.getStringExtra(TRANSMIT_ID);

        mNewsTag = intent.getStringExtra(NEWS_TAG);
        mNewsScene = intent.getStringExtra(NEWS_SCENE);

    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);
        QEventBus.getEventBus().register(this);
        initWebView(mWebView);
        mWebView.loadUrl(html_url);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(v -> onBackPressed());

        if (video_img != null && pic != null) {
            video_img.loadImage(pic);
        }

        zan_btn.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(url)) {
                zan_btn_Click(url);
            }
        });
        xpl_btn.setOnClickListener(v -> {
            if (isLogin()) {
                Intent intent = new Intent(getApplicationContext(), TransmitActivity.class)
                        .putExtra(Constants.BUNDLE_KEY_TYPE, transmit_type)
                        .putExtra("newsUid", newsUid)
                        .putExtra(CommentActivity.NEWS_NID, nid)
                        .putExtra(CommentActivity.COMMENTS_URL, url)
                        .putExtra(CommentActivity.COMMENTS_TITLE, title)
                        .putExtra(Constants.BUNDLE_KEY_COMMENT_NID, url)
                        .putExtra(Constants.BUNDLE_KEY_NEWS_FROM, mNewsEntity.getNews_from());
                startActivityForResult(intent, SEND_CMT_REQUESTCODE);
                //overridePendingTransition(R.anim.transmitactivity_anim_enter, R.anim.newsdetailactivity_anim_out);
            } else {
                login();
            }
        });
        go_top_btn.setVisibility(View.INVISIBLE);
        go_top_btn.setOnClickListener(v -> scrollview.smoothScrollTo(0, 0));
        share_btn.setOnClickListener(v -> shareClick());
        loading_layout.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
        error_layout.setOnClickListener(v -> {
            requestZmData();
            loading_layout.setVisibility(View.VISIBLE);
            error_layout.setVisibility(View.GONE);
        });

        try {
            newsType = Integer.parseInt(mNewsEntity.getNew_type());
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (newsType) {
            case 3:
                initVideoUI();
                mToolBar.setBackgroundResource(com.jeremy.lychee.R.color.transparent);
                mToolBar.setNavigationIcon(com.jeremy.lychee.R.drawable.ic_actionbar_white_back);
                article_icon_logo_iv.setAlpha(0);
                break;
            default:
                int topPadding = getResources().getDimensionPixelSize(com.jeremy.lychee.R.dimen.toolbar_height);
                scrollview.setPadding(0, topPadding, 0, 0);
                vedio_container.setVisibility(View.GONE);
                mToolBar.setBackgroundResource(com.jeremy.lychee.R.color.toolbar_color);
                mToolBar.setNavigationIcon(com.jeremy.lychee.R.mipmap.ic_nav_back);
                break;
        }

        scrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        scrollview.setOnScrollChangeListener(OldNewsDetailActivity.this);
    }

    private void zan_btn_Click(String key) {
        boolean is_zan = ZanPreference.getInstance().getBooleanValue(url);
        if (is_zan) {
            ToastHelper.getInstance(OldNewsDetailActivity.this).toast("已经赞过啦！");
        } else {
            OldRetroAdapter.getService().punch(key).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .compose(bindToLifecycle())
                    .subscribe(new Subscriber<ModelBase>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            ToastHelper.getInstance(OldNewsDetailActivity.this).toast("点赞失败！");
                        }

                        @Override
                        public void onNext(ModelBase modelBase) {
                            if (modelBase.getErrno() == 0) {
                                ToastHelper.getInstance(OldNewsDetailActivity.this).toast("点赞成功！");
                                zan_btn.setText(String.valueOf(zan_num + 1));
                                Drawable left_drable = getResources().getDrawable(com.jeremy.lychee.R.drawable.article_icon_good_selected);
                                zan_btn.setCompoundDrawablesWithIntrinsicBounds(left_drable, null, null, null);
                                ZanPreference.getInstance().saveBooleanValue(url, true);
                            } else {
                                ToastHelper.getInstance(OldNewsDetailActivity.this).toast("点赞失败！");
                            }
                        }
                    });
        }

    }

    private void getZanNum(String key) {
        String signStr = AppUtil.zanSignString(key);
        OldRetroAdapter.getService().getpunch(key, signStr).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .subscribe(new Subscriber<ModelBase<Zan>>() {
                    @Override
                    public void onCompleted() {
                        if (zan_num <= 0) {
                            if (!TextUtils.isEmpty(url)) {
                                ZanPreference.getInstance().saveBooleanValue(url, false);
                            }
                        }
                        boolean is_zan = ZanPreference.getInstance().getBooleanValue(url);
                        if (is_zan) {
                            zan_btn.setText(String.valueOf(zan_num));
                            Drawable left_drable = getResources().getDrawable(com.jeremy.lychee.R.drawable.article_icon_good_selected);
                            zan_btn.setCompoundDrawablesWithIntrinsicBounds(left_drable, null, null, null);
                        } else {
                            if (zan_num > 0) {
                                zan_btn.setText(String.valueOf(zan_num));
                            } else {
                                zan_btn.setText(String.valueOf("赞个"));
                            }
                            Drawable left_drable = getResources().getDrawable(com.jeremy.lychee.R.drawable.article_icon_good_default);
                            zan_btn.setCompoundDrawablesWithIntrinsicBounds(left_drable, null, null, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        boolean is_zan = ZanPreference.getInstance().getBooleanValue(url);
                        if (is_zan) {
                            zan_btn.setText(String.valueOf("赞个"));
                            Drawable left_drable = getResources().getDrawable(com.jeremy.lychee.R.drawable.article_icon_good_default);
                            zan_btn.setCompoundDrawablesWithIntrinsicBounds(left_drable, null, null, null);
                            ZanPreference.getInstance().remove(url);
                        }
                    }

                    @Override
                    public void onNext(ModelBase<Zan> modelBase) {
                        if (modelBase.getErrno() == 0) {
                            zan_num = modelBase.getData().getDing();

                        }
                    }
                });
    }

    private void updateHotData(int num,boolean isUpdata) {
        //更新直播热点的点赞数
//        LiveHotItemDao dao = ContentApplication.getDaoSession().getLiveHotItemDao();
//        QueryBuilder qb = dao.queryBuilder();
//        qb.where(LiveHotItemDao.Properties.Id.eq(nid));
//        List<LiveHotItem> listdata = qb.list();
//        if (listdata.size() > 0) {
//            LiveHotItem itemdata = listdata.get(0);
//            if (itemdata != null) {
//                if (!isUpdata){
//                    num = Integer.parseInt(itemdata.getComment())+1;
//                }
//                itemdata.setComment("" + num);
//                dao.insertOrReplace(itemdata);
//
//                LiveEvent.HotCommentUpdata commentUpdata = new LiveEvent.HotCommentUpdata();
//                commentUpdata.num = num;
//                commentUpdata.nid = nid;
//                QEventBus.getEventBus().post(commentUpdata);
//            }
//        }

    }

    private void shareClick() {

        ShareInfo shareInfo = new ShareInfo(shareUrl, nid, title, description, pic, null, ShareInfo.SHARECONTENT_NEWS);
        new ShareManager(OldNewsDetailActivity.this, shareInfo, true,
                () -> HitLog.hitLogShare("null", "null", nid, mNewsEntity.getNews_from())) //分享打点
                .show();
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_news_detail_old);
        getExtraParameters();
        mActivityResumeTimeStamp = 0L;
    }

    @Override
    protected void onDestroy() {
        if (mNewsBrowseData != null) {
            //打点相关新闻显示
            HitLog.hitLogNewsListShow(
                    "null", "null", HitLog.POSITION_RELATED_NEWS, mNewsBrowseData.getShowedRelatedList());
            //打点热门新闻显示
            HitLog.hitLogNewsListShow(
                    "null", "null", HitLog.POSITION_HOT_NEWS, mNewsBrowseData.getShowedHotList());
            //打点新闻详情浏览
            HitLog.hitLogNewsDetailBrowse(
                    mNewsEntity.getNid(),
                    mNewsScene,
                    mNewsTag,
                    mNewsEntity.getNews_from(),
                    mActivityForgroundDuration, mNewsBrowseData.getPercent(),
                    mNewsBrowseData.getSlidingCnt());
        }
        super.onDestroy();
        ButterKnife.unbind(this);
        QEventBus.getEventBus().unregister(this);
        Logger.t(TAG).e("onDestroy");
        if (newsType == 3) {
            destrotyVideoUI();
        }
    }

    private MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!TextUtils.isEmpty(mAlbumId)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(com.jeremy.lychee.R.menu.menu_news_detail, menu);
            mToolBar.post(() -> {
                final View v = findViewById(com.jeremy.lychee.R.id.menu);
                if (v != null) {
                    //禁止一级menu长按toast
                    v.setOnLongClickListener(v1 -> false);
                }
            });
        }
        super.onCreateOptionsMenu(menu);
        item = menu.findItem(com.jeremy.lychee.R.id.menu);
        if (item != null) {
            if (newsType == 3) {
                //使用白色icon
                item.setIcon(com.jeremy.lychee.R.drawable.living_icon_more);
            } else {
                item.setIcon(com.jeremy.lychee.R.drawable.living_icon_more2);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case com.jeremy.lychee.R.id.menu_edit_parent_album:
                WeMediaChangeAlbumActivity.startActivity(this, false, new String[]{mTransmitId}, mAlbumId);
                return true;
            case com.jeremy.lychee.R.id.menu_delete:
                doDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doDelete() {
        DialogUtil.showConfirmDialog(this, "确定要删除本条转推文章吗？",
                this.getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                    //远程删除
                    String sign = AppUtil.transmitDeleteSignString(mTransmitId);
                    OldRetroAdapter.getService().deleteTransmit(mTransmitId, sign)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    s -> {
                                        if (s.getErrno() == 0) {
                                            //成功删除后：本地删除
                                            QEventBus.getEventBus().post(
                                                    new Events.OnAlbumDetailUpdated(mTransmitId));
                                            finish();
                                        }
                                    },
                                    e -> {
                                        e.printStackTrace();
                                        //网络错误
                                        ToastHelper.getInstance(
                                                this.getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                                    }
                            );
                    dialog.dismiss();
                }, this.getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss);
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initWebView(WebView webView) {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (TextUtils.isEmpty(url)) {
                    return true;
                }
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                requestZmData();
                //获取顶的个数
                getZanNum(mNewsEntity.getUrl());
            }
        };
        mWebView.setWebViewClient(webViewClient);
        WebChromeClient webChromeClient = new WebChromeClient();
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.addJavascriptInterface(this, "$_news");
        mWebView.addJavascriptInterface(this, "$_photo");
        mWebView.addJavascriptInterface(this, "$_related");
        mWebView.addJavascriptInterface(this, "$_dingyue");
        mWebView.addJavascriptInterface(this, "$_video");
    }

    private NewsRelated newsRelated;

    private void requestRelated() {
        OldRetroApiService mRetroApiService = OldRetroAdapter.getService();
        mRetroApiService.getNewsDetail(url, nid, feed_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new Subscriber<Result<NewsRelated>>() {
                    @Override
                    public void onCompleted() {
                        requestCmtData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        requestCmtData();
                    }

                    @Override
                    public void onNext(Result<NewsRelated> newsRelatedResult) {
                        if (newsRelatedResult != null && newsRelatedResult.errno == 0) {
                            newsRelated = newsRelatedResult.getData();
                            if (newsRelated != null && mWebView != null) {
                                String value = GsonUtils.toJson(newsRelated);
                                if (newsRelated.getTransmit_data() != null && newsRelated.getTransmit_data().size() > 0) {
                                    String renderGrayBar = "javascript:$ContentRender.renderGrayBar(" + value + ",false);";
                                    mWebView.loadUrl(renderGrayBar);
                                }

                                if (newsRelated.getRelated() != null && newsRelated.getRelated().size() > 0) {
                                    String recommendMethod = "javascript:$ContentRender.renderRecommendArtices(" + value + ");";
                                    mWebView.loadUrl(recommendMethod);
                                }
                                if (newsRelated.getHotNewsList() != null && newsRelated.getHotNewsList().size() > 0) {
                                    String hotMethod = "javascript:$ContentRender.renderhotArtices(" + value + ");";
                                    mWebView.loadUrl(hotMethod);
                                }
                                if (newsRelated.getMedia() != null && !newsRelated.getMedia().is_my()) {
                                    String rssMethod = "javascript:$ContentRender.renderRssArtices(" + value + ");";
                                    mWebView.loadUrl(rssMethod);
                                }

                                if (newsRelated.getRelated_video() != null && newsRelated.getRelated_video().size() > 0) {
                                    String rssMethod = "javascript:$ContentRender.renderRecommendVideo(" + value + ");";
                                    mWebView.loadUrl(rssMethod);
                                }

                                if (newsRelated.getTopic() != null && newsRelated.getTopic().size() > 0) {
                                    String rssMethod = "javascript:$ContentRender.renderTopics(" + value + ");";
                                    mWebView.loadUrl(rssMethod);
                                }
                            }
                        }
                    }
                });
    }

    private void requestZmData() {
        OldRetroApiService mRetroApiService = OldRetroAdapter.getService();
        String cache_key = "zm_cache_key" + url;
        Observable.just(url)
                .observeOn(Schedulers.io())
                        //get zm cache
                .flatMap(news_url -> Observable.just(getZmCache(cache_key)))
                .flatMap(result -> {
                    if (result != null) {
                        //zm cache not null
                        return Observable.just(result);
                    }

                    return mRetroApiService.getZmData(zm_url)
                            //cache the net data
                            .flatMap(net_result -> {
                                if (net_result != null && net_result.getData() != null && net_result.errno == 0) {
                                    String json = GsonUtils.toJson(net_result);
                                    saveZmCache(cache_key, json);
                                }
                                return Observable.just(net_result);
                            });
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new Subscriber<Result<NewsDetail>>() {
                    @Override
                    public void onCompleted() {
                        requestRelated();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading_layout.setVisibility(View.GONE);
                        error_layout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Result<NewsDetail> result) {
                        if (newsType == 3) {
                            try {
                                String newsData = mNewsEntity.getNews_data();
                                JSONObject jsonObject = new JSONObject(newsData);
                                String video_id = jsonObject.getString("video_id");
                                String source_type = jsonObject.getString("source_type");
                                String tag = jsonObject.getString("tag");
                                loadVideoInfo(source_type, video_id, tag);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        //news data
                        loading_layout.setVisibility(View.GONE);
                        if (result == null || result.getData() == null) {
                            //net error
                            error_layout.setVisibility(View.VISIBLE);
                            return;
                        }
                        if (mWebView != null) {
                            NewsDetail mNewsDetail = result.getData();
                            if (mNewsDetail != null) {
                                transmit_type = mNewsDetail.getTransmit_type();
                                WeMediaChannel media = mNewsDetail.getMedia();
                                if (media != null) {
                                    newsUid = media.getId();
                                }
                                shareUrl = mNewsDetail.getShorturl();
                                String subhead = mNewsDetail.getSubhead();
                                description = TextUtils.isEmpty(subhead) ? " " : subhead;
                                String json = GsonUtils.toJson(mNewsDetail);
                                String renderArticle = "javascript:$ContentRender.renderArticle(" + json + ",false);";
//                            String renderArticle = "javascript:$ContentRender.renderArticle({\"content\":[{\"type\":\"video\",\"value\":\"{\\\"title\\\":\\\"\\\\u89c6\\\\u9891\\\",\\\"items\\\":[{\\\"video_id\\\":\\\"1\\\",\\\"customSort\\\":0,\\\"id\\\":\\\"299336\\\",\\\"url\\\":\\\"http:\\\\/\\\\/play2.kandian.360.cn\\\\/vod_xinwen\\\\/vod-media\\\\/293893_806703.m3u8\\\",\\\"imgurl\\\":\\\"http:\\\\/\\\\/p3.qhimg.com\\\\/t01182513963211158a.jpg?size\\u003d704x576\\\",\\\"title\\\":\\\"\\\\u91d1\\\\u6b63\\\\u6069\\\\uff1a\\\\u671d\\\\u9c9c\\\\u5df2\\\\u5b9e\\\\u73b0\\\\u6838\\\\u5f39\\\\u5934\\\\u5c0f\\\\u578b\\\\u5316\\\",\\\"src\\\":\\\"\\\\u5929\\\\u8109\\\",\\\"video\\\":[{\\\"stream_vbt\\\":\\\"\\\\u6d41\\\\u7545\\\",\\\"stream_type\\\":3,\\\"stream_url\\\":\\\"http:\\\\/\\\\/play2.kandian.360.cn\\\\/vod_xinwen\\\\/vod-media\\\\/293893_806701.m3u8\\\",\\\"stream_rate\\\":\\\"500\\\"},{\\\"stream_vbt\\\":\\\"\\\\u6807\\\\u6e05\\\",\\\"stream_type\\\":3,\\\"stream_url\\\":\\\"http:\\\\/\\\\/play2.kandian.360.cn\\\\/vod_xinwen\\\\/vod-media\\\\/293893_806702.m3u8\\\",\\\"stream_rate\\\":\\\"800\\\"},{\\\"stream_vbt\\\":\\\"\\\\u9ad8\\\\u6e05\\\",\\\"stream_type\\\":3,\\\"stream_url\\\":\\\"http:\\\\/\\\\/play2.kandian.360.cn\\\\/vod_xinwen\\\\/vod-media\\\\/293893_806703.m3u8\\\",\\\"stream_rate\\\":\\\"1000\\\"}],\\\"type\\\":\\\"3\\\",\\\"tag\\\":\\\"\\\"}]}\"},{\"type\":\"txt\",\"value\":\"据外媒报道：朝鲜宣布所有与韩国的协议作废，朝鲜将清除朝鲜境内所有韩方资产，将通过政治、军事和经济措施打击韩国。\"}],\"errno\":\"0\",\"shorturl\":\"http://t.m.so.com/t/N_fd6np\",\"source\":\"军事综合\",\"summary\":\"据外媒报道：朝鲜宣布所有与韩国的协议作废，朝鲜将清除朝鲜境内所有韩方资产，将通过政治、军事和经济措施打击韩国。\",\"time\":\"1457577270\",\"title\":\"朝鲜宣布所有与韩国的协议作废\",\"transmit_data\":[],\"transmit_num\":\"0\",\"url\":\"http://webcms.kandian.360.cn/news/news/20160310/n24192.shtml\",\"wapurl\":\"http://webcms.kandian.360.cn/news/news/20160310/n24192.shtml\"},false);";
                                mWebView.loadUrl(renderArticle);
                            }
                        }
                    }
                });

    }

    private int transmit_type;

    /**
     * get zm cache data
     *
     * @param zm_cache_key cache key
     * @return Result<NewsDetail>
     */
    private Result<NewsDetail> getZmCache(String zm_cache_key) {
        try {
            String json = NewsListPreference.getInstance().getString(zm_cache_key);
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            Type type = new TypeToken<Result<NewsDetail>>() {
            }.getType();
            return GsonUtils.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * save zm data
     *
     * @param zm_cache_key cache key
     * @param result_json  cache data
     */
    private void saveZmCache(String zm_cache_key, String result_json) {
        NewsListPreference.getInstance().saveStringValue(zm_cache_key, result_json);
    }

    private void requestCmtData() {
        //获取文章评论
        String pointer = "0";
        OldRetroApiService mRetroApiService = CommentRetroAdapter.getCommentService();
        String type = "1";
        String num = "5";

        User mUser = Session.getSession();
        String uid = null;
        String token = null;
        if (mUser != null) {
            uid = mUser.getUid();
            token = mUser.getToken();
        }

        String key = url;
        try {
            key = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Observable<ResultCmt<CommentData>> resultObservable = mRetroApiService.getCommentList( key, type, num, pointer, uid, token);
        resultObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .subscribe(new Subscriber<ResultCmt<CommentData>>() {
                    @Override
                    public void onCompleted() {
                        if (mWebView != null) {
                            String lazyLoadImage = "javascript:$ContentRender.lazyLoadImage();";
                            mWebView.loadUrl(lazyLoadImage);
                            mWebView.loadUrl("javascript:$ContentRender.done()");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (mWebView != null) {
                            String lazyLoadImage = "javascript:$ContentRender.lazyLoadImage();";
                            mWebView.loadUrl(lazyLoadImage);
                            mWebView.loadUrl("javascript:$ContentRender.done()");
                        }
                    }

                    @Override
                    public void onNext(ResultCmt<CommentData> result) {
                        if (mWebView != null && result != null && result.getErrno() == 0 && result.getData() != null) {
                            CommentData data = result.getData();
                            int total = data.getTotal();
                            if (total > 0) {
                                updateHotData(total,true);
                                String renderSeparator = "javascript:$ContentRender.renderSeparator();";
                                mWebView.loadUrl(renderSeparator);
                            }

                            String json = GsonUtils.toJson(data);
                            String method = "javascript:$ContentRender.renderComments(" + json + ");";
                            mWebView.loadUrl(method);

                        }
                    }
                });

    }

    private void login() {
        isLoginGoCmt = true;
        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), LOGIN_REQUESTCODE);
    }

    private boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && requestCode == LOGIN_REQUESTCODE) {
            isLoginGoCmt = false;
        }
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case SEND_CMT_REQUESTCODE:
                case CMTS_REQUESTCODE:
                    updateHotData(1,false);
                    Comment comment = data.getParcelableExtra(CommentActivity.TAG_DATA);
                    if (comment != null) {
                        //无热评数据则渲染最新评论数据
                        String json = GsonUtils.toJson(comment);
                        String method = "javascript:$ContentRender.prependComment(" + json + ");";
                        mWebView.loadUrl(method);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isLoginGoCmt = savedInstanceState.getBoolean("isLoginGoCmt");
        }
        QHStatAgent.onEvent(this, QHState.NEWSDETAIL_PV);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isLoginGoCmt", isLoginGoCmt);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void onEvent(Events.Logout event) {
        Logger.t(TAG).e("onEvent Logout");
        //退出登录
        isLoginGoCmt = false;
    }

    public void onEvent(Events.LoginOk event) {
        Logger.t(TAG).e("onEvent LoginOk");
        //登录成功
        if (isLoginGoCmt) {
            isLoginGoCmt = false;
            Intent intent = new Intent(getApplicationContext(), TransmitActivity.class)
                    .putExtra(Constants.BUNDLE_KEY_TYPE, transmit_type)
                    .putExtra(CommentActivity.NEWS_NID, nid)
                    .putExtra("newsUid", newsUid)
                    .putExtra(CommentActivity.COMMENTS_URL, url)
                    .putExtra(CommentActivity.COMMENTS_TITLE, title)
                    .putExtra(Constants.BUNDLE_KEY_COMMENT_NID, url)
                    .putExtra(Constants.BUNDLE_KEY_NEWS_FROM, mNewsEntity.getNews_from());
            startActivityForResult(intent, SEND_CMT_REQUESTCODE);
        }
    }

    public void onEvent(Events.LoginCancel event) {
        Logger.t(TAG).e("onEvent LoginCancel");
        //登录取消
        isLoginGoCmt = false;
    }

    public void onEvent(Events.LoginErr event) {
        Logger.t(TAG).e("onEvent LoginErr");
        //登录失败
        isLoginGoCmt = false;
    }

    private void openCmtListActivity() {
        ThreadUtils.postOnUiThread(() -> CommentActivity.startActivity(this, CMTS_REQUESTCODE, transmit_type, url, title, nid, mNewsEntity.getNews_from()));

    }

    @JavascriptInterface
    public void OnClickCmtMore() {
        openCmtListActivity();
    }

    @JavascriptInterface
    public void OnClickShare(String arg) {
        Bitmap thumb = null;
        try {
            thumb = Glide.with(getApplicationContext()).load(ImageOptiUrl.get(pic, 100, 100)).asBitmap().into(100, 100).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if ("circle".equals(arg)) {
            WXShareManager.shareToWX(getApplicationContext(), shareUrl, title, description, thumb, true);
            return;
        }
        if ("weixin".equals(arg)) {
            WXShareManager.shareToWX(getApplicationContext(), shareUrl, title, description, thumb, false);
            return;
        }
        if ("weibo".equals(arg)) {
            ShareInfo shareInfo = new ShareInfo();
            shareInfo.setUrl(shareUrl);
            shareInfo.setImgPath(pic);
            shareInfo.setTitle(title);
            shareInfo.setShareContentType(ShareInfo.SHARECONTENT_NEWS);
            WBShareManager.share2Weibo(this, shareInfo);
            // WBShareManager.share2Weibo(this, shareUrl, title,ShareInfo.SHARECONTENT_NEWS ,thumb);
            return;
        }
        if ("copy".equals(arg)) {
            AppUtil.copy(getApplicationContext(), shareUrl);
            ThreadUtils.postOnUiThread(() -> ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.copy_ok));
            return;
        }
    }


    @JavascriptInterface
    public void comment() {
        if (isLogin()) {
            Intent intent = new Intent(getApplicationContext(), TransmitActivity.class)
                    .putExtra(Constants.BUNDLE_KEY_TYPE, transmit_type)
                    .putExtra("newsUid", newsUid)
                    .putExtra(CommentActivity.NEWS_NID, nid)
                    .putExtra(CommentActivity.COMMENTS_URL, url)
                    .putExtra(CommentActivity.COMMENTS_TITLE, title)
                    .putExtra(Constants.BUNDLE_KEY_COMMENT_NID, url)
                    .putExtra(Constants.BUNDLE_KEY_NEWS_FROM, mNewsEntity.getNews_from());
            startActivityForResult(intent, SEND_CMT_REQUESTCODE);
            //overridePendingTransition(R.anim.transmitactivity_anim_enter, R.anim.newsdetailactivity_anim_out);
        } else {
            login();
        }
    }

    @JavascriptInterface
    public void OnClickCommUser(String uid) {
        //TODO uid
        //传递参数错误
        Logger.e("OnClickCommUser: " + uid);
        ThreadUtils.runOnUiThread(() -> WeMediaChannelActivity.startActivity(OldNewsDetailActivity.this, uid));
    }


    @JavascriptInterface
    public void OnClickRecommend() {
        ThreadUtils.postOnUiThread(() -> {
            Intent intent = new Intent(this, WeMediaTransmitedChannelListActivity.class);
            intent.putExtra("nid", nid);
            intent.putExtra("url", url);
            startActivity(intent);
        });

    }

    @JavascriptInterface
    public void OnClickCmtLike(String cid) {
        diggCmt(cid);
    }

    private void diggCmt(String comment_id) {
        OldRetroApiService mRetroApiService = CommentRetroAdapter.getCommentService();
        String uid = AppUtil.getTokenID(ApplicationStatus.getApplicationContext()) + System.currentTimeMillis();

        String key = url;
        try {
            key = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Observable<Result> resultObservable = mRetroApiService.diggComment(key, comment_id, uid);
        resultObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .subscribe();
    }


    @JavascriptInterface
    public void OnClickImg(String img_url, String arg1, String arg2, String arg3, String arg4) {
        ThreadUtils.postOnUiThread(() -> {
            Intent intent = new Intent(OldNewsDetailActivity.this, ImageNewsActivity.class);
            intent.putExtra(NEW_ENTITY, mNewsEntity);
            intent.putExtra(IMG_URL, img_url);
            startActivity(intent);
        });
    }

    @JavascriptInterface
    public void OnClickText(String url) {
        ThreadUtils.postOnUiThread(() -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    /**
     * 点击查看图片
     */
    @JavascriptInterface
    public void OnClickPhoto(String imgUrl, int l, int t, int r, int b, String data) {
        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }
        if (TextUtils.isEmpty(data)) {
            //检查相关图集中的数据
            return;
        }

        if (data.startsWith("{") && data.endsWith("}")) {
            // 跳转到图集
            try {
                Type type = new TypeToken<NewsListDataWrapper>() {
                }.getType();
                NewsListDataWrapper photo = GsonUtils.fromJson(data, type);

                ThreadUtils.postOnUiThread(() -> {
                    Intent intent = new Intent(OldNewsDetailActivity.this, ImageNewsActivity.class);
                    intent.putExtra(NEW_ENTITY, photo);
                    intent.putExtra(IMG_URL, imgUrl);
                    startActivity(intent);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @JavascriptInterface
    public void OnClickRelated(String json) {
        try {
            NewsBrowse data = GsonUtils.fromJson(json, NewsBrowse.class);
            if (data == null) return;
            //新闻点击打点
            QHStatAgent.onEvent(this, QHState.NEWSDETAIL_RELATED_CLICK);
            HitLog.hitLogNewsListClick("null", "null",
                    HitLog.POSITION_RELATED_NEWS, data.getClickNews());
            NewsListData related = data.getClickNews();
            ThreadUtils.runOnUiThread(() -> {
                startActivity(this, related, 0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void OnClickHotArticle(String json) {
        try {
            NewsBrowse data = GsonUtils.fromJson(json, NewsBrowse.class);
            if (data == null) return;
            //新闻点击打点
            QHStatAgent.onEvent(this, QHState.NEWSDETAIL_HOT_CLICK);
            HitLog.hitLogNewsListClick("null", "null",
                    HitLog.POSITION_HOT_NEWS, data.getClickNews());
            NewsListData related = data.getClickNews();
            ThreadUtils.runOnUiThread(() -> {
                startActivity(this, related, 0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public void OnClickTopic(String json) {
        try {
            WeMediaTopic topic = GsonUtils.fromJson(json, WeMediaTopic.class);
            ThreadUtils.runOnUiThread(() -> WeMediaTopicDetailActivity.startActivity(this, topic.getId(), topic.getTitle()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void OnClickVideo(String json) {
        try {
            NewsVideoBody videoBody = GsonUtils.fromJson(json, NewsVideoBody.class);
            ThreadUtils.runOnUiThread(() -> {
                playInHeader(false, false);
                VideoPlayer.getInstance().stop();
                LivePlayerActivity.loadVideo(OldNewsDetailActivity.this, videoBody.getId(), videoBody.getType(), videoBody.getTag());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void OnClickVideoList(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            jsonObject = jsonObject.getJSONObject("clickNews");
            String video_json = jsonObject.toString();
            LiveVideoInfo liveVideoInfo = GsonUtils.fromJson(video_json, LiveVideoInfo.class);
            ThreadUtils.runOnUiThread(() -> {
                playInHeader(false, false);
                VideoPlayer.getInstance().stop();
                LiveVideoListActivity.loadVideo(this, liveVideoInfo.getId(), liveVideoInfo.getVideo_type(), liveVideoInfo.getTag());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void OnClickRssList(String json) {
        try {
            NewsBrowse data = GsonUtils.fromJson(json, NewsBrowse.class);
            if (data != null) {
                ThreadUtils.runOnUiThread(() -> {
                    startActivity(this, data.getClickNews(), 0);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void OnClickRss(String json) {
        if (isLogin()) {
            try {
                RelatedMedia media = GsonUtils.fromJson(json, RelatedMedia.class);
                OldRetroAdapter.getService().subscribeColumn(media.getId(), 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            if (s != null && s.getErrno() == 0) {
                                ToastHelper.getInstance(getApplicationContext()).toast("订阅成功");
                                if (mWebView != null) {
                                    mWebView.loadUrl("javascript:$ContentRender.afterClickRss(0)");
                                }
                            } else {
                                String msg = "订阅失败";
                                if (s != null) {
                                    msg = s.getErrmsg();
                                }
                                ToastHelper.getInstance(getApplicationContext()).toast(msg);
                            }
                        }, Throwable::printStackTrace);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), LOGIN_REQUESTCODE);
        }
    }


    @JavascriptInterface
    public void OnClickAlbum(String json) {
        NewsMedia media = GsonUtils.fromJson(json, NewsMedia.class);
        if (media == null) {
            return;
        }

        NewsMedia.NewsAlbum album = media.getAlbum();
        if (album != null) {
            ThreadUtils.runOnUiThread(() -> {
                WeMediaAlbumDetailActivity.startActivity(
                        this, album.getId(), album.getName(), false);
            });
        }
    }


    @JavascriptInterface
    public void OnHitLog(String json) {
        Logger.t(TAG).d("OnHitLog: " + json);
        NewsBrowse data = GsonUtils.fromJson(json, NewsBrowse.class);
        if (data != null) {
            mNewsBrowseData = data;
        }
    }

    final public void onEventMainThread(
            com.jeremy.lychee.eventbus.news.Events.OnNewsAlbumChanged event) {
        if (event != null) {
            mAlbumId = event.albumId;
            mWebView.loadUrl(html_url);
            loading_layout.setVisibility(View.VISIBLE);
            error_layout.setVisibility(View.GONE);
        }
    }

    public void onEvent(com.jeremy.lychee.eventbus.news.Events.ZanClick event) {
        if (event != null) {
            boolean is_zan = ZanPreference.getInstance().getBooleanValue(url);
            if (is_zan) {
                getZanNum(url);
            }
        }
    }

    @Bind(com.jeremy.lychee.R.id.live_video_player)
    VideoPlayerView playerView;

    @Bind(com.jeremy.lychee.R.id.vedio_container)
    ViewGroup vedio_container;

    @Bind(com.jeremy.lychee.R.id.mini_player_view)
    VideoPlayerView mini_player_view;

    @Bind(com.jeremy.lychee.R.id.mini_player)
    RelativeLayout miniPlayerContainer;

    @Bind(com.jeremy.lychee.R.id.mini_place_holder)
    ImageView miniPlayerPlaceHolder;

    @Bind(com.jeremy.lychee.R.id.close_mini_player)
    View close_mini_player;

    @Bind(com.jeremy.lychee.R.id.video_img)
    GlideImageView video_img;


    private void initVideoUI() {
        VideoPlayer.getInstance().getLiveCloudCallback().addOnCompletionAction(this::onPlayCoreCompletion, AndroidSchedulers.mainThread());
        VideoPlayer.getInstance().registerErrorHandler(errorCode -> {
            if (VideoPlayerView.isFullScreen()) {
                VideoPlayerView.exitFullScreen();
            }
            if (playerView != null) {
                playerView.unBindToVideoPlayer();
            }
            Toast.makeText(getApplicationContext(), VideoPlayer.textMessageForErrorCode(errorCode), Toast.LENGTH_SHORT).show();
        });
        VideoPlayer.getInstance().setRendererContainer(vedio_container);
        playerView.addControlPanel(new VideoPlayerPanelBasic());
        playerView.addDecorator(new VideoPlayerBottomProgressDecorator());

        miniPlayerContainer.setVisibility(View.INVISIBLE);
        miniPlayerPlaceHolder.setVisibility(View.INVISIBLE);
        mini_player_view.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector mDetector = new GestureDetector(getApplicationContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    playInHeader(true, true);
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (e2.getX() - e1.getX() > 20 && Math.abs(velocityX) > 0) {
                        playInHeader(false, true);
                    }
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                mDetector.onTouchEvent(event);
                return false;
            }
        });

        close_mini_player.setOnClickListener(v -> {
            playInHeader(false, true);
        });

    }

    private void destrotyVideoUI() {
        if (playerView != null) {
            playerView.unBindToVideoPlayer();
        }
        if (mini_player_view != null) {
            mini_player_view.unBindToVideoPlayer();
        }
        VideoPlayer.getInstance().unregisterErrorHandler(errorCode -> Toast.makeText(getApplicationContext(), VideoPlayer.textMessageForErrorCode(errorCode), Toast.LENGTH_SHORT).show());
        VideoPlayer.getInstance().getLiveCloudCallback().removeOnCompletionAction(this::onPlayCoreCompletion);
    }

    private void loadVideoInfo(String topicId, String video_id, String video_tag) {
        LiveVideoListManager.createInstance().getLiveVideoList(topicId, video_id, video_tag, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .map(it -> it.second)
                .subscribe(it -> {
                    if (it == null || it.size() == 0) {
                        onFailed(VideoPlayer.ErrorCode.INVALID_DATA);
                        return;
                    }
                    if (it.get(0).getStream_type() == LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_NEWS_REPLAY) {
//                        video_img.loadImage(it.get(0).getVideo_img());
                        video_img.setOnClickListener(v -> {
                            loadVideo(it.get(0));
                            playerView.bindToVideoPlayer();
                            if (miniPlayerContainer != null) {
                                miniPlayerContainer.setVisibility(View.INVISIBLE);
                            }
                            if (miniPlayerPlaceHolder != null) {
                                miniPlayerPlaceHolder.setVisibility(View.INVISIBLE);
                            }
                            if (playerView != null) {
                                playerView.bindToVideoPlayer();
                            }
                        });

                        //3G网络不自动加载视频
                        boolean isNetTypeMobile = AppUtil.isNetTypeMobile(getApplicationContext());
                        if (!isNetTypeMobile) {
                            VideoPlayer.getInstance().load(this, video_id, topicId, video_tag);
                        }

                        //s视频列表 打点信息上传3003
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < it.size(); i++) {
                            list.add(it.get(i).getId() + "," + it.get(i).getNews_from() + "," + it.get(i).getNews_type());
                        }
                        HitLog.hitLogVideoPlayList(list);
                    } else {
                        if (it.size() > 1) {
                            VideoPlayerPanelNews videoPlayerPanelNews = new VideoPlayerPanelNews();
                            videoPlayerPanelNews.setVideoList(it);
                            playerView.addControlPanel(videoPlayerPanelNews);
                        }

//                        video_img.loadImage(it.get(0).getVideo_img());
                        video_img.setOnClickListener(v -> {
                            loadVideo(it.get(0));
                            if (miniPlayerContainer != null) {
                                miniPlayerContainer.setVisibility(View.INVISIBLE);
                            }
                            if (miniPlayerPlaceHolder != null) {
                                miniPlayerPlaceHolder.setVisibility(View.INVISIBLE);
                            }
                            if (playerView != null) {
                                playerView.bindToVideoPlayer();
                            }
                        });
                        //3G网络不自动加载视频
                        boolean isNetTypeMobile = AppUtil.isNetTypeMobile(getApplicationContext());
                        if (!isNetTypeMobile) {
                            loadVideo(it.get(0));
                            playerView.bindToVideoPlayer();
                        }
                        //s视频列表 打点信息上传3003
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < it.size(); i++) {
                            list.add(it.get(i).getId() + "," + it.get(i).getNews_from() + "," + it.get(i).getNews_type());
                        }
                        HitLog.hitLogVideoPlayList(list);

                    }
                }, e -> onFailed(VideoPlayer.ErrorCode.BUSINESS_SERVER_ERROR));
    }

    private void onFailed(int errorCode) {
        Toast.makeText(this, VideoPlayer.textMessageForErrorCode(errorCode), Toast.LENGTH_SHORT).show();
    }

    private void loadVideo(LiveVideoInfo liveVideoInfo) {
        LiveBrowseHistoryManager.getInstance().addBrowseItem(liveVideoInfo);
        OldRetroAdapter.getService().getLiveChannelStreams(liveVideoInfo.getVideo_play_url()).map(ModelBase::getData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .doOnNext(it -> {
                    if (it == null || it.getVideo_stream().size() == 0) {
                        throw new RuntimeException(VideoPlayer.textMessageForErrorCode(VideoPlayer.ErrorCode.INVALID_DATA));
                    }
                })
                .subscribe(it -> {
                    if (it == null) {
                        VideoPlayer.getInstance().raiseErrorNotify(VideoPlayer.ErrorCode.INVALID_DATA);
                        return;
                    }

                    VideoPlayer.getInstance().load(it);
                }, e -> {
                    if (e instanceof RuntimeException) {
                        onFailed(VideoPlayer.ErrorCode.INVALID_DATA);
                    } else {
                        onFailed(VideoPlayer.ErrorCode.BUSINESS_SERVER_ERROR);
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (VideoPlayerView.isFullScreen()) {
            VideoPlayerView.exitFullScreen();
            return;
        }

        VideoPlayer.getInstance().onBackPressed(this);
        super.onBackPressed();
    }

    private int webHeight;

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if(mWebView!=null){
            //TODO  webview 中的图片懒加载调用
            String lazyLoadImage = "javascript:$ContentRender.lazyLoadImage();";
            mWebView.loadUrl(lazyLoadImage);
        }

        if (VideoPlayerView.isFullScreen()) {
            return;
        }

        if (newsType == 3) {
            //小窗口视频显示逻辑
            int h = vedio_container.getHeight();
            if (scrollY >= h && oldScrollY < h) {
                //打开小窗口播放视频
                playInMiniPlayer();
            }
            if (scrollY <= h && oldScrollY > h && onScrollChangeCloseMiniPlayerAble) {
                //关闭小窗口播放视频
                playInHeader(false, true);
            }
            int alpha = scrollY * 255 / h;
            alpha = alpha > 255 ? 255 : alpha;
            alpha = alpha < 0 ? 0 : alpha;
            mToolBar.setBackgroundColor(Color.argb(alpha, 247, 249, 250));
            float a = (float) alpha / (float) 255;
            article_icon_logo_iv.setAlpha(a);

            if (alpha >= 128) {
                mToolBar.setNavigationIcon(com.jeremy.lychee.R.mipmap.ic_nav_back);
                if (item != null) {
                    item.setIcon(com.jeremy.lychee.R.drawable.living_icon_more2);
                }
            } else {
                if (item != null) {
                    item.setIcon(com.jeremy.lychee.R.drawable.living_icon_more);
                }
                mToolBar.setNavigationIcon(com.jeremy.lychee.R.drawable.ic_actionbar_white_back);
            }

        } else {
            if (webHeight == 0) {
                webHeight = mWebView.getHeight();
            }
            int percent = scrollY * 100 / webHeight;
            //判断FloatingActionButton显示逻辑
            if (percent > 30) {
                go_top_btn.show();
                if (go_top_btn.getVisibility() != View.VISIBLE) {
                    go_top_btn.setVisibility(View.VISIBLE);
                }
            } else {
                go_top_btn.hide();
            }
        }
    }

    private void playInMiniPlayer() {
        Logger.e("playInMiniPlayer");
        if (!VideoPlayer.getInstance().isPlaying() && !VideoPlayer.getInstance().isPaused() && !VideoPlayer.getInstance().isLoading()) {
            return;
        }
        onScrollChangeCloseMiniPlayerAble = true;
        if (mini_player_view != null) {
            mini_player_view.bindToVideoPlayer();
        }

        if (miniPlayerContainer != null) {
            TranslateAnimation animation = new TranslateAnimation(miniPlayerContainer.getMeasuredWidth() + DensityUtils.dip2px(getApplicationContext(), 20), 0, 0, 0);
            animation.setDuration(300);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    miniPlayerContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            miniPlayerContainer.startAnimation(animation);
        }
    }

    private void playInHeader(boolean shouldScrollToHeader, boolean hasAnim) {
        Logger.e("playInHeader");
        if (!VideoPlayer.getInstance().isPlaying() && !VideoPlayer.getInstance().isPaused() && !VideoPlayer.getInstance().isLoading()) {
            return;
        }
        if (miniPlayerContainer == null || miniPlayerContainer.getVisibility() != View.VISIBLE) {
            return;
        }

        miniPlayerPlaceHolder.setImageDrawable(new BitmapDrawable(VideoPlayer.getInstance().capture()));
        miniPlayerPlaceHolder.setVisibility(View.VISIBLE);

        if (hasAnim) {
            TranslateAnimation animation = new TranslateAnimation(0, miniPlayerContainer.getMeasuredWidth() + DensityUtils.dip2px(getApplicationContext(), 20), 0, 0);
            animation.setDuration(300);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            miniPlayerContainer.startAnimation(animation);
        }
        miniPlayerContainer.setVisibility(View.INVISIBLE);
        miniPlayerPlaceHolder.setVisibility(View.INVISIBLE);

        if (playerView != null) {
            playerView.bindToVideoPlayer();
        }
        if (shouldScrollToHeader) {
            scrollview.smoothScrollTo(0, 0);
            onScrollChangeCloseMiniPlayerAble = false;
        }
    }

    //是否监听视频view的scroll state
    private boolean onScrollChangeCloseMiniPlayerAble;

    private void onPlayCoreCompletion() {
        if (miniPlayerContainer != null) {
            miniPlayerContainer.setVisibility(View.INVISIBLE);
        }
        if (miniPlayerPlaceHolder != null) {
            miniPlayerPlaceHolder.setVisibility(View.INVISIBLE);
        }
        if (mini_player_view != null) {
            mini_player_view.unBindToVideoPlayer();
        }
        if (VideoPlayerView.isFullScreen()) {
            VideoPlayerView.exitFullScreen();
        }
        if (playerView != null) {
            playerView.unBindToVideoPlayer();
        }
    }
}
