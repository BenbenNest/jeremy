package com.jeremy.lychee.activity.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jeremy.lychee.base.ImmersionActivity;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.news.Comment;
import com.jeremy.lychee.model.user.User;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.customview.user.AlbumSelectRecyclerView;
import com.jeremy.lychee.activity.news.CommentActivity;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.news.ResultCmt;
import com.jeremy.lychee.net.CommentRetroAdapter;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.StringUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.utils.statusbar.StatusBarUtil;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangp on 15-11-25.
 */
public class TransmitActivity extends ImmersionActivity {
    public static final int COMMENT_TOO_FREQUENT = 109;
    public static final int LOGIN_INFO_EXPIRY = 103;
    public static final int RESULT_TOKEN_TIME_OUT = 2;

    @Bind(com.jeremy.lychee.R.id.transmit_toolbar)
    Toolbar mToolBar;
    @Bind(com.jeremy.lychee.R.id.transmit_editor)
    EditText mEditor;

    @Bind(com.jeremy.lychee.R.id.transmit_selecter)
    AlbumSelectRecyclerView mSelectRecyclerView;

    @Bind(com.jeremy.lychee.R.id.loading_layout)
    View loading_view;
    @Bind(com.jeremy.lychee.R.id.error_layout)
    View error_view;
    @Bind(com.jeremy.lychee.R.id.num)
    TextView num;

    private String ztNid;
    private String plNid;
    private String mUrl;
    private int mType;
    private String mNewsFrom;
    private String newsUid ="";
    private String title;

    @Override
    protected void setContentView() {
        QEventBus.getEventBus().register(this);
        setContentView(com.jeremy.lychee.R.layout.activity_transmit);
        ButterKnife.bind(this);
        overridePendingTransition(com.jeremy.lychee.R.anim.transmitactivity_anim_enter, com.jeremy.lychee.R.anim.newsdetailactivity_anim_out);
        ztNid = getIntent().getStringExtra(Constants.BUNDLE_KEY_NID);
        if (ztNid==null){
            ztNid="";
        }
        newsUid = getIntent().getStringExtra("newsUid");

        if (getIntent().getStringExtra(CommentActivity.COMMENTS_URL) != null) {
            mUrl = getIntent().getStringExtra(CommentActivity.COMMENTS_URL);
        } else {
            mUrl = "";
        }
        mType = getIntent().getIntExtra(Constants.BUNDLE_KEY_TYPE, 1);//1:news ; 2:video
        title = getIntent().getStringExtra(CommentActivity.COMMENTS_TITLE);
        if (title==null){
            title="";
        }
        plNid = getIntent().getStringExtra(Constants.BUNDLE_KEY_COMMENT_NID);
        if (plNid==null){
            plNid="";
        }
        mNewsFrom = getIntent().getStringExtra(Constants.BUNDLE_KEY_NEWS_FROM);
        if (mNewsFrom==null){
            mNewsFrom="";
        }


        configToolbar(com.jeremy.lychee.R.menu.menu_transmit_confirm, com.jeremy.lychee.R.string.title_activity_transmit, com.jeremy.lychee.R.mipmap.ic_transmit_close);
        findViewById(com.jeremy.lychee.R.id.menu_transmit_confirm2).setVisibility(View.GONE);
        findViewById(com.jeremy.lychee.R.id.menu_transmit_confirm2).setOnClickListener(
                v -> {
                    if (!TextUtils.isEmpty(mSelectRecyclerView.getSubmitChannel())) {
                        submitTransmitData(mSelectRecyclerView.getSubmitChannel());
                    } else {
                        //发评论
                        String content = mEditor.getText().toString();
                        if (!TextUtils.isEmpty(content.trim())) {
                            if (content.length() > 140) {
                                ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.content_too_large);
                            } else {
                                sendCmtRequest(plNid, content);
                            }
                        } else {
                            ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.no_content);
                        }

                    }

                }
        );
        mEditor.addTextChangedListener(textWatcher);
    }



//    public static void startActivity(Context context, String nid, String url, int type){
//        Intent intent = new Intent(context, TransmitActivity.class);
//        intent.putExtra(Constants.BUNDLE_KEY_NID, nid);
//        intent.putExtra(Constants.BUNDLE_KEY_URL, url);
//        intent.putExtra(Constants.BUNDLE_KEY_TYPE, type);
//        context.startActivity(intent);
//    }

    @Override
    public View onGetFitWindowView() {
        return findViewById(com.jeremy.lychee.R.id.parent_layout);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏沉浸
            setFitsSystemWindows(false);
            setStatusBarColor(getResources().getColor(com.jeremy.lychee.R.color.toolbar_color));
            int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
            mToolBar.setPadding(0, statusBarHeight, 0, 0);
            ViewGroup.LayoutParams params = mToolBar.getLayoutParams();
            params.height += statusBarHeight;
            mToolBar.setLayoutParams(params);
            error_view.setOnClickListener(v -> {
                requestAndShowChannelData();
            });

        }


        requestAndShowChannelData();

    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString())) {
               findViewById(com.jeremy.lychee.R.id.menu_transmit_confirm2).setVisibility(View.VISIBLE);
                findViewById(com.jeremy.lychee.R.id.menu_transmit_confirm).setVisibility(View.GONE);
                num.setText(s.length()+"/140");
                //findViewById(R.id.menu_transarticle_confirm).setVisibility(View.GONE);
            }else {
                findViewById(com.jeremy.lychee.R.id.menu_transmit_confirm2).setVisibility(View.GONE);
                findViewById(com.jeremy.lychee.R.id.menu_transmit_confirm).setVisibility(View.VISIBLE);
                num.setText("0/140");
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    //发评论
    private void sendCmtRequest(String key, String content) {
        OldRetroApiService mRetroApiService = CommentRetroAdapter.getCommentService();
        User mUser = Session.getSession();
        if(mUser==null){
            //TODO 未登陆
            return;
        }
        String uid = mUser.getUid();
        String token = mUser.getToken();

        try {
            key = URLEncoder.encode(key, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String sign = getSendCmtSign(content);

        Observable<ResultCmt<Comment>> mObservable = mRetroApiService.sendComment(key, content, token, uid, sign);
        final String finalContent = content;
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResultCmt<Comment>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        //网络错误、解析错误
                        ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
                    }

                    @Override
                    public void onNext(ResultCmt<Comment> result) {
                        int code = result.getErrno();
                        if(code==0){
                            addMessage(finalContent);
                        }

                        resultData(result);
                    }
                });
    }

    private String getSendCmtSign(String message){
        String salt = getResources().getString(com.jeremy.lychee.R.string.salt);
        String client_id = "25";
        String sign = AppUtil.getMD5code(client_id + "_" + message + "_" + salt);
        int len = sign != null ? sign.length() : 0;
        int start = 8;
        int end = len - 8;
        sign = sign != null && end > start ? sign.substring(start, end) : null;
        return sign;
    }

    public void addMessage(String Messagecontent){
        if (TextUtils.isEmpty(newsUid)){
            return;
        }
        OldRetroAdapter.getService().addMessage(newsUid,"2",Messagecontent,ztNid,mUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ModelBase modelBase) {

                    }
                });
    }

    private void resultData(ResultCmt<Comment> result) {
        if (result != null) {
            int code = result.getErrno();
            if (code == 0) {
                Intent data = new Intent();
                Comment comment = result.getData();
                data.putExtra(CommentActivity.TAG_DATA, comment);
                setResult(RESULT_OK, data);
                //打点
                HitLog.hitLogComment(
                        "null", "null", plNid, mNewsFrom,
                        TransmitActivity.this::finish);
                ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.send_ok);
            } else if (code == COMMENT_TOO_FREQUENT) {
                ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.cmd_too_frequent);
            } else if (code == LOGIN_INFO_EXPIRY) {
                setResult(RESULT_TOKEN_TIME_OUT);
                finish();
            } else {
                //服务端返回错误
                ToastHelper.getInstance(getApplicationContext()).toast(result.getMessage());
            }
        } else {
            //网络错误、解析错误
            ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.net_error);
        }
    }

    /**
     * check param，if null set 0
     */
    private static String checkParams(String param) {
        if (TextUtils.isEmpty(param)) {
            param = "0";
        }
        return param;
    }

    private Toolbar configToolbar(int menuId, int titleId, int navigationIconId) {
        if (menuId != 0) {
            mToolBar.inflateMenu(menuId);
        }
        if (titleId != 0) {
            mToolBar.setTitle(titleId);
       /*mToolBar.setTitleTextColor(getResources().getColor(R.color.news_item_text_color));*/
        }
        if (navigationIconId != 0) {
            mToolBar.setNavigationIcon(navigationIconId);
            mToolBar.setNavigationOnClickListener(v -> onBackPressed());
        }
        return mToolBar;
    }

    private void requestAndShowChannelData() {
        loading_view.setVisibility(View.VISIBLE);
        error_view.setVisibility(View.GONE);
        OldRetroAdapter.getService().getUserCreateMediaChannelList(1, 100, "")
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase<List<WeMediaChannel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loading_view.setVisibility(View.GONE);
                        error_view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ModelBase<List<WeMediaChannel>> listModelBase) {
                        if (listModelBase.getErrno()==0){
                            loading_view.setVisibility(View.GONE);
                            error_view.setVisibility(View.GONE);
                            mSelectRecyclerView.showAlbums(listModelBase.getData());
                        }else {
                            loading_view.setVisibility(View.GONE);
                            error_view.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }


    public void submitTransmitData(String subId) {
        if (StringUtil.isEmpty(mEditor.getText().toString())) {
            ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.no_content);
        } else {
            String content = mEditor.getText().toString();
            if (content.length() > 140) {
                ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.content_too_large);
            } else {
                WeakReference<ProgressDialog> dialog = new WeakReference<>(new ProgressDialog(this));
                dialog.get().setIndeterminate(true);
                dialog.get().setMessage("新闻转推中...");
                dialog.get().setCancelable(false);
                dialog.get().show();
                Observable.just(subId)
                        .compose(bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                       /* .flatMap(s -> Observable.from(s))*/
                        .observeOn(Schedulers.io())
                        .doOnNext(s -> {
                            String sign = AppUtil.signString(ztNid, s, mUrl, mEditor.getText().toString(), mType, title);
                            OldRetroAdapter.getService().transmitNews(
                                    ztNid, mUrl, s, mEditor.getText().toString(), mType, title, sign)
                                    .subscribe(
                                            ss -> {

                                            }, e -> {

                                            }
                                    );
                        })
                        /*.toList()*/
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                                    if (ApplicationStatus.isActivityRunning(TransmitActivity.this)
                                            && dialog.get() != null && dialog.get().isShowing()) {
                                        dialog.get().dismiss();
                                        QEventBus.getEventBus().postSticky(new Events.OnAlbumListUpdated(null));
                                        /*ToastHelper.getInstance(TransmitActivity.this).toast("转推成功");*/
                                        /*Log.v("sub","sssssss===="+s);*/
                                        //转发评论打点
                                        HitLog.hitLogTransmit("null", "null", ztNid, mNewsFrom);
                                        //发送评论
                                        sendCmtRequest(plNid, content);
                                    }
                                }, e -> {
                                    if (ApplicationStatus.isActivityRunning(TransmitActivity.this)
                                            && dialog.get() != null && dialog.get().isShowing()) {
                                        dialog.get().dismiss();
                                        ToastHelper.getInstance(TransmitActivity.this).toast("转推失败");
                                        e.printStackTrace();
                                    }
                                }
                        );
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QEventBus.getEventBus().unregister(this);
    }

    final public void onEventMainThread(Events.OnAlbumListUpdated event) {
        requestAndShowChannelData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, com.jeremy.lychee.R.anim.transmitactivity_anim_out);
    }

    @Override
    public void onBackPressed() {
        doExit();
    }

    private void doExit() {
        if (!TextUtils.isEmpty(mEditor.getText().toString().trim())
                || !TextUtils.isEmpty(mSelectRecyclerView.getSubmitChannel())) {
            DialogUtil.showConfirmDialog(this, "确定要放弃点评吗？",
                    getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                dialog.dismiss();
                finish();
            }, getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss);

        } else {
            finish();
        }
    }
}
