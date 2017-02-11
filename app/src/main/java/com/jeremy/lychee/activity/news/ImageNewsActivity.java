package com.jeremy.lychee.activity.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.reflect.TypeToken;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.activity.user.TransmitActivity;
import com.jeremy.lychee.db.NewsListData;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.news.Comment;
import com.jeremy.lychee.model.news.NewsContent;
import com.jeremy.lychee.model.news.NewsDetail;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.NewsListPreference;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.customview.news.HackyViewPager;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.eventbus.news.Events;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.model.news.CommentData;
import com.jeremy.lychee.model.news.NewsListDataWrapper;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.model.news.ResultCmt;
import com.jeremy.lychee.model.news.Zan;
import com.jeremy.lychee.net.CommentRetroAdapter;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.preference.ZanPreference;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.GsonUtils;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.widget.shareWindow.ShareManager;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;

/**
 * image news page
 */
public class ImageNewsActivity extends SlidingActivity {
    public static final String IMG_URL = "IMG_URL";
    public static final String NEW_ENTITY = "NEW_ENTITY";
    private static final String ISLOCKED_ARG = "isLocked";
    private static final int SEND_CMT_REQUESTCODE = 9;
    private String zm_url;
    private String url;
    private String title;
    private String img_url;


    @Bind(com.jeremy.lychee.R.id.view_pager)
    ViewPager mViewPager;

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar mToolbar;

    @Bind(com.jeremy.lychee.R.id.news_title)
    TextView news_title;

    @Bind(com.jeremy.lychee.R.id.news_summary)
    TextView news_summary;

    @Bind(com.jeremy.lychee.R.id.zan_btn)
    TextView zan_btn;

    @Bind(com.jeremy.lychee.R.id.summary_lay)
    View summary_lay;

    private String shareUrl;
    private String description;
    private String pic;
    private String nid;
    private int zan_num = 0;
    private int transmit_type;

    private NewsListDataWrapper mNewsEntity;

    public static void startActivity(Context context, NewsListDataWrapper entity, String img_url) {
        Intent intent = new Intent(context, ImageNewsActivity.class);
        intent.putExtra(NEW_ENTITY, entity);
        intent.putExtra(IMG_URL, img_url);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }

    public static void startActivity(Context context, NewsListDataWrapper entity) {
        Intent intent = new Intent(context, ImageNewsActivity.class);
        intent.putExtra(NEW_ENTITY, entity);
        IntentUtils.startPreviewActivity(context, intent, 0);
    }

    public static void startActivity(Context context, NewsListData data) {
        Intent intent = new Intent(context, ImageNewsActivity.class);
        intent.putExtra(NEW_ENTITY, new NewsListDataWrapper(data));
        IntentUtils.startPreviewActivity(context, intent, 0);
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
        url = mNewsEntity.getUrl();
        title = mNewsEntity.getTitle();
        zm_url = mNewsEntity.getZm();
        img_url = intent.getStringExtra(IMG_URL);
        pic = mNewsEntity.getAlbum_pic();
        nid = mNewsEntity.getNid();
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_image_news);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);
        news_summary.setMovementMethod(new ScrollingMovementMethod());
        mToolbar.inflateMenu(com.jeremy.lychee.R.menu.menu_images);
        mToolbar.setNavigationIcon(com.jeremy.lychee.R.drawable.live_icon_close);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mToolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            switch (id){
                case com.jeremy.lychee.R.id.action_cmt:
                    openCmtListActivity();
                    break;
            }
            return true;
        });

        getExtraParameters();
        setCmtNum(0);
        requestZmData();
        requestCmtData();
        getZanNum(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
    }


    private void openCmtListActivity(){
        CommentActivity.startActivity(this, 0, transmit_type, url, title, nid, mNewsEntity.getNews_from());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void requestZmData() {
        OldRetroApiService mRetroApiService = OldRetroAdapter.getService();
        final String cache_key = "zm_cache_key" + url;
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
//                        requestCmtData();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        loading_layout.setVisibility(View.GONE);
//                        error_layout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Result<NewsDetail> result) {
//                        loading_layout.setVisibility(View.GONE);
                        if (result == null || result.getData() == null) {
                            //net error
//                            error_layout.setVisibility(View.VISIBLE);
                            return;
                        }

                        setData(result);
                    }
                });

    }

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

    private SparseArray<String> summarys = new SparseArray<>();
    private int image_count;

    /**
     * save zm data
     *
     * @param zm_cache_key cache key
     * @param result_json  cache data
     */
    private void saveZmCache(String zm_cache_key, String result_json) {
        NewsListPreference.getInstance().saveStringValue(zm_cache_key, result_json);
    }

    private void setData(Result<NewsDetail> responseBean) {
        if (responseBean == null) {
            return;
        }
        NewsDetail newsDetail = responseBean.getData();
        if (newsDetail == null) {
            return;
        }
        transmit_type = newsDetail.getTransmit_type();

        ArrayList<NewsContent> contents = newsDetail.getContent();
        shareUrl = newsDetail.getShorturl();
        String subhead = newsDetail.getSubhead();
        description = TextUtils.isEmpty(subhead) ? " " : subhead;
        if (contents == null || contents.size() == 0) {
            //TODO 图集数据为空
            return;
        }
        summarys.clear();

        int firstPostion = 0;
        ArrayList<String> urls = new ArrayList<>();
        int size = contents.size();
        for (int i = 0; i < size; i++) {
            NewsContent content = contents.get(i);
            String type = content.getType();
            String data = content.getValue();
            if ("img".equals(type)) {
                urls.add(data);
                if (img_url != null && data != null && data.equals(img_url)) {
                    firstPostion = urls.size() - 1;
                }

                if (i + 1 < size) {
                    NewsContent content2 = contents.get(i + 1);
                    String type2 = content2.getType();
                    String data2 = content2.getValue();
                    String subType = content2.getSubtype();
                    if ("txt".equals(type2) && "img_title".equals(subType)) {
                        summarys.put(urls.size() - 1, data2);
                        i++;
                    }
                }
            }
        }

        image_count = urls.size();
        if (image_count == 0) {
            //TODO 图集数据为空
            return;
        }
        if (mViewPager != null) {
            updateImageText(firstPostion);
            SamplePagerAdapter adapter = new SamplePagerAdapter(urls);
            mViewPager.setAdapter(adapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    updateImageText(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            mViewPager.setCurrentItem(firstPostion);
        }
    }

    private void updateImageText(int position) {
        String summary = summarys.get(position);
        news_summary.setText(summary);
        String text = String.valueOf(position + 1) + "/" + image_count + "　" + title;
        news_title.setText(text);
    }

    class SamplePagerAdapter extends PagerAdapter {
        private ArrayList<String> urls;

        public SamplePagerAdapter(ArrayList<String> urls) {
            this.urls = urls;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            String imgUrl = urls.get(position);
            final PhotoView photoView = new PhotoView(container.getContext());
            loadImg(photoView, imgUrl);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        private void loadImg(final PhotoView photoView, String imgUrl){
            DrawableTypeRequest request = Glide.with(ImageNewsActivity.this).load(imgUrl);
            request.error(com.jeremy.lychee.R.drawable.loaded_loser);
            request.placeholder(com.jeremy.lychee.R.drawable.loading);
            //glide bug 会拉伸图片成正方形， 去掉动画效果后正常
            request.dontAnimate();
            request.listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    Logger.e("onException: model: " + model);
                    photoView.setOnPhotoTapListener((view, v, v1) -> loadImg(photoView, imgUrl));
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    Logger.e("onResourceReady: model: " + model);
                    photoView.setOnPhotoTapListener((view, v, v1) -> toggleSummaryView());
                    return false;
                }
            });
            if(!TextUtils.isEmpty(imgUrl)&&imgUrl.contains(".gif")){
                request.asGif();
                request.diskCacheStrategy(DiskCacheStrategy.SOURCE);
            } else {
                request.asBitmap();
                request.diskCacheStrategy(DiskCacheStrategy.ALL);
            }
            request.into(photoView);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }

    private void toggleSummaryView(){
        // TODO 切换显示状态
        int visibility = summary_lay.getVisibility();
        if(visibility!=View.VISIBLE){
            summary_lay.setVisibility(View.VISIBLE);
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            summary_lay.setVisibility(View.GONE);
            mToolbar.setVisibility(View.GONE);
        }
    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    private void setCmtNum(int total) {

        Menu menu = mToolbar.getMenu();
        MenuItem menuItem = menu.findItem(com.jeremy.lychee.R.id.action_cmt);
        if (total > 0) {
            menuItem.setVisible(true);
            String dtext = total + "条热评论 >";
            menuItem.setTitle(dtext);
        } else {
            menuItem.setVisible(false);
        }
    }

    private int cmt_total_num;

    private void requestCmtData() {
        //获取文章评论
        String pointer = "0";
        OldRetroApiService mRetroApiService = CommentRetroAdapter.getCommentService();
        String type = "1";
        String num = "20";

        User mUser = Session.getSession();
        String uid = null;
        String token = null;
        if(mUser!=null){
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResultCmt<CommentData> result) {
                        CommentData data = result.getData();
                        if(data!=null){
                            cmt_total_num = data.getTotal();
                            setCmtNum(cmt_total_num);
                        }
                    }
                });

    }

    private boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    private void login() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    public void xplClick(View view) {
        if (isLogin()) {
            Intent intent = new Intent(getApplicationContext(), TransmitActivity.class)
                    .putExtra(CommentActivity.NEWS_NID, nid)
                    .putExtra(Constants.BUNDLE_KEY_COMMENT_NID, url)
                    .putExtra(CommentActivity.COMMENTS_URL, url)
                    .putExtra(CommentActivity.COMMENTS_TITLE, title);
            startActivityForResult(intent, SEND_CMT_REQUESTCODE);
            overridePendingTransition(com.jeremy.lychee.R.anim.transmitactivity_anim_enter, 0);
        } else {
            login();
        }
    }
    public void shareClick(View view) {
        ShareInfo shareInfo = new ShareInfo(shareUrl,nid, title, description, pic, null, ShareInfo.SHARECONTENT_NEWS);
        new ShareManager(ImageNewsActivity.this, shareInfo, true,
                () -> HitLog.hitLogShare("null", "null", nid, mNewsEntity.getNews_from())) //分享打点
                .show();
    }

    public void zan_btn_Click(View view) {
        String key = url;
        boolean is_zan = ZanPreference.getInstance().getBooleanValue(url);
        if (is_zan){
            ToastHelper.getInstance(ImageNewsActivity.this).toast("已经赞过啦！");
        }else {
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
                            ToastHelper.getInstance(ImageNewsActivity.this).toast("点赞失败！");
                        }

                        @Override
                        public void onNext(ModelBase modelBase) {
                            if (modelBase.getErrno() == 0) {
                                ToastHelper.getInstance(ImageNewsActivity.this).toast("点赞成功！");
                                zan_btn.setText(String.valueOf(zan_num + 1));
                                Drawable left_drable = getResources().getDrawable(com.jeremy.lychee.R.drawable.article_icon_good_selected);
                                zan_btn.setCompoundDrawablesWithIntrinsicBounds(left_drable, null, null, null);
                                ZanPreference.getInstance().saveBooleanValue(url, true);
                                QEventBus.getEventBus().post(new Events.ZanClick(true));
                            } else {
                                ToastHelper.getInstance(ImageNewsActivity.this).toast("点赞失败！");
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
                        boolean is_zan = ZanPreference.getInstance().getBooleanValue(url);
                        if (is_zan){
                            zan_btn.setText(String.valueOf(zan_num));
                            Drawable left_drable = getResources().getDrawable(com.jeremy.lychee.R.drawable.article_icon_good_selected);
                            zan_btn.setCompoundDrawablesWithIntrinsicBounds(left_drable, null, null, null);
                        }else {
                            if (zan_num>0) {
                                zan_btn.setText(String.valueOf(zan_num));
                            }else {
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
                        if (is_zan){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Comment comment = data.getParcelableExtra(CommentActivity.TAG_DATA);
            if (comment != null) {
                //评论成功
                cmt_total_num++;
                setCmtNum(cmt_total_num);
            }
        }
    }

}
