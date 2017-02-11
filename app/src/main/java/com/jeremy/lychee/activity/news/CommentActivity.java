package com.jeremy.lychee.activity.news;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.activity.user.TransmitActivity;
import com.jeremy.lychee.adapter.news.CommentAdapter;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.news.Comment;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.model.news.CommentData;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.model.news.ResultCmt;
import com.jeremy.lychee.net.CommentRetroAdapter;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.widget.RecyclerViewDecoration.CmtDividerItemDecoration;
import com.jeremy.lychee.widget.slidingactivity.IntentUtils;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CommentActivity extends SlidingActivity implements CommentAdapter.OnReplayListener {
    public static final String TAG_DATA = "tag_data";
    public static final String NEWS_NID = "news_nid";
    public static final String COMMENTS_URL = "comments_url";
    public static final String COMMENTS_TITLE = "comments_title";
    public static final String COMMENTS_NID = "comments_nid";
    public static final String COMMENTS_FROM = "comments_from";
    public static final int REQ_CODE_EDIT = 1;

    @Bind(com.jeremy.lychee.R.id.pull_refresh_view)
    SwipeRefreshLayout mPullToRefreshView;

    @Bind(com.jeremy.lychee.R.id.recyclerView)
    RecyclerView mCommentListView;

    @Bind(com.jeremy.lychee.R.id.loading_layout)
    View loading_layout;

    @Bind(com.jeremy.lychee.R.id.error_layout)
    View error_layout;

    @Bind(com.jeremy.lychee.R.id.no_cmt_layout)
    View no_cmt_layout;

    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar mToolbar;

    private CommentAdapter mCommentAdapter;
    private int total;
    private String url;
    private String title;
    private String nid;
    private String newsFrom;
    private int transmit_type;

    public static void startActivity(Context mContext, int requestCode, int transmit_type, String url, String title, String nid, String newsFrom){
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra(Constants.BUNDLE_KEY_TYPE, transmit_type);
        intent.putExtra(CommentActivity.COMMENTS_URL, url);
        intent.putExtra(CommentActivity.COMMENTS_TITLE, title);
        intent.putExtra(CommentActivity.COMMENTS_NID, nid);
        intent.putExtra(CommentActivity.COMMENTS_FROM, newsFrom);
        IntentUtils.startPreviewActivity(mContext, intent, requestCode);
    }

    @Override
    protected void onPreInflation() {
        Intent intent = getIntent();
        url = intent.getStringExtra(COMMENTS_URL);
        title = intent.getStringExtra(COMMENTS_TITLE);
        nid = intent.getStringExtra(COMMENTS_NID);
        newsFrom = intent.getStringExtra(COMMENTS_FROM);
        transmit_type = intent.getIntExtra(Constants.BUNDLE_KEY_TYPE, 1);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);

        error_layout.setOnClickListener(mOnClickListener);

        mPullToRefreshView.setColorSchemeResources(com.jeremy.lychee.R.color.swipe_refresh_color);
        mPullToRefreshView.setOnRefreshListener(() -> requestCmtData("0"));
        LinearLayoutManager layoutManager = new LinearLayoutManager(CommentActivity.this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        };

        mCommentListView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration dividerItemDecoration = new CmtDividerItemDecoration(CommentActivity.this, CmtDividerItemDecoration.VERTICAL_LIST);
        mCommentListView.addItemDecoration(dividerItemDecoration);
        mCommentAdapter = new CommentAdapter(CommentActivity.this, null, this, true);
        mCommentAdapter.setOnLoadMoreListener(this::requestCmtData);

        mCommentListView.setAdapter(mCommentAdapter);

        loading_layout.setVisibility(View.VISIBLE);

        initToolbar();

        requestCmtData("0");
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_comment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initToolbar(){
        mToolbar.setTitle(com.jeremy.lychee.R.string.user_comments);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolbar.setOnClickListener(mOnClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_CODE_EDIT == requestCode && RESULT_OK == resultCode) {
            if (data != null) {
                Comment comment = data.getParcelableExtra(TAG_DATA);
                if(comment!=null){
                    mCommentAdapter.insertComment(comment);
                    mCommentAdapter.notifyDataSetChanged();
                    int p = 0;
                    if(mCommentAdapter.hasHotComments()){
                        p = 1;
                    }
                    mCommentListView.scrollToPosition(p);

                    total++;

                    if(total==0){
                        no_cmt_layout.setVisibility(View.VISIBLE);
                    }else{
                        no_cmt_layout.setVisibility(View.GONE);
                    }
                }

            }
        }

    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case com.jeremy.lychee.R.id.error_layout:
                    error_layout.setVisibility(View.GONE);
                    loading_layout.setVisibility(View.VISIBLE);
                    requestCmtData("0");

                    break;
                case com.jeremy.lychee.R.id.toolbar:
                    mPullToRefreshView.setRefreshing(true);
                    mPullToRefreshView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Field f = SwipeRefreshLayout.class.getDeclaredField("mProgress");
                                f.setAccessible(true);
                                Object o = f.get(mPullToRefreshView);

                                Method m2 = f.getType().getDeclaredMethod("setProgressRotation", float.class);
                                m2.invoke(o, 1f);

                                Method m3 = f.getType().getDeclaredMethod("setArrowScale", float.class);
                                m3.invoke(o, 1f);

                                Method m4 = f.getType().getDeclaredMethod("setStartEndTrim", float.class, float.class);
                                m4.invoke(o, 0.75f, 0.75f);

                                Method m = f.getType().getDeclaredMethod("showArrow", boolean.class);
                                m.invoke(o, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 500);
                    mCommentListView.scrollToPosition(0);
                    new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            requestCmtData("0");
                        }
                    }.sendEmptyMessageDelayed(0, 1500);
                    break;
            }
        }
    };
    
    @Override
    public void onReplay(List<Comment> chain_comments, Comment comment) {
        //评论回复功能  暂不使用
    }

    @Override
    public void onDiggClick(String cid) {
        diggCmt(cid);
    }

    private void login(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private boolean isLogin(){
        return !Session.isUserInfoEmpty();
    }

    private void toast(String msg){
        ToastHelper.getInstance(getApplicationContext()).toast(msg);
    }

    /**
     *
     * @param pointer request cmt data from pointer
     */
    private void requestCmtData(final String pointer){
        //获取文章评论
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

        if("0".equals(pointer)){
            //刷新时更新热评
            Observable<ResultCmt<CommentData>> resultHotObservable = mRetroApiService.getCommentHotList(key);
            resultHotObservable.observeOn(AndroidSchedulers.mainThread())
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
                        public void onNext(ResultCmt<CommentData> commentDataResultCmt) {
                            if(commentDataResultCmt!=null&&commentDataResultCmt.getErrno()==0){
                                CommentData data = commentDataResultCmt.getData();
                                if(data!=null){
                                    List<Comment> hotComments = data.getComments();
                                    mCommentAdapter.setHotComment(hotComments);
                                    mCommentAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

        Observable<ResultCmt<CommentData>> resultObservable = mRetroApiService.getCommentList(key, type, num, pointer, uid, token);
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
                        loading_layout.setVisibility(View.GONE);
                        error_layout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ResultCmt<CommentData> result) {
                        loading_layout.setVisibility(View.GONE);
                        if ("0".equals(pointer)) {
                            refreshData(result);
                        } else {
                            getMoreData(result);
                        }
                    }
                });

    }

    /**
     * get more coment data
     * @param result coment data
     */
    private void getMoreData(ResultCmt<CommentData> result) {
        if (result != null ) {
            int status = result.getErrno();
            if(status==0&&result.getData()!=null){
                CommentData data = result.getData();
                List<Comment> commentList = data.getComments();
                int size = mCommentAdapter.getSize()+commentList.size();
                if(total>size&&commentList.size()>0){
                    mCommentAdapter.setOver(false);
                }else{
                    mCommentAdapter.setOver(true);
                }

                mCommentAdapter.addCommentResponse(commentList);
                mCommentAdapter.notifyDataSetChanged();
            }else{
                toast(result.getMessage());
            }

        }else{
            mCommentAdapter.setLoadFail(true);
        }
    }

    /**
     * refresh comment data
     * @param result coment data
     */
    private void refreshData(ResultCmt<CommentData> result){
        mPullToRefreshView.setRefreshing(false);
        if (result != null) {
            int status = result.getErrno();
            CommentData data = result.getData();
            if(status==0&&data!=null){
                total = data.getTotal();
                List<Comment> commentList = data.getComments();

                int totalSize = 0;
                if(mCommentAdapter!=null){
                    totalSize = mCommentAdapter.getSize();
                }
                int size = commentList.size();

                if(total>totalSize&&size>0){
                    if(mCommentAdapter!=null){
                        mCommentAdapter.setOver(false);
                    }
                }else{
                    if(mCommentAdapter!=null){
                        mCommentAdapter.setOver(true);
                    }
                }
                if(mCommentAdapter!=null){
                    mCommentAdapter.setCommentResponse(commentList);
                    mCommentAdapter.notifyDataSetChanged();
                }

                if(total==0){
                    no_cmt_layout.setVisibility(View.VISIBLE);
                }else{
                    no_cmt_layout.setVisibility(View.GONE);
                }

            } else {
                toast(result.getMessage());
            }

        }else{
            int count = mCommentAdapter.getItemCount();
            if(count==0){
                error_layout.setVisibility(View.VISIBLE);
            }else{
                mCommentAdapter.setLoadFail(true);
            }
        }
    }

    private void diggCmt(String comment_id){
        OldRetroApiService mRetroApiService = CommentRetroAdapter.getCommentService();
        String uid = AppUtil.getTokenID(ApplicationStatus.getApplicationContext())+System.currentTimeMillis();

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

    /**
     * 发送评论
     */
    public void sendCmt(View view){
        if (isLogin()) {
            Intent intent = new Intent(getApplicationContext(), TransmitActivity.class)
                    .putExtra(Constants.BUNDLE_KEY_TYPE, transmit_type)
                    .putExtra(NEWS_NID, nid)
                    .putExtra(COMMENTS_URL, url)
                    .putExtra(COMMENTS_TITLE, title)
                    .putExtra(Constants.BUNDLE_KEY_COMMENT_NID, url)
                    .putExtra(Constants.BUNDLE_KEY_NEWS_FROM, newsFrom);
            startActivityForResult(intent, REQ_CODE_EDIT);
        } else {
            login();
        }
    }
}
