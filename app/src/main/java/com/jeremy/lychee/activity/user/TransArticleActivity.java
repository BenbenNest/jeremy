package com.jeremy.lychee.activity.user;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.lychee.activity.news.WebViewActivity;
import com.jeremy.lychee.channel.QHState;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.customview.user.AlbumSelectRecyclerView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.qihoo.sdk.report.QHStatAgent;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransArticleActivity extends SlidingActivity {
    @Bind(com.jeremy.lychee.R.id.toolbarview)
    View toolbarView;
    @Bind(com.jeremy.lychee.R.id.transmit_selecter)
    AlbumSelectRecyclerView mSelectRecyclerView;
    @Bind(com.jeremy.lychee.R.id.copy_url_text)
    EditText copy_url_text;
    @Bind(com.jeremy.lychee.R.id.copy_recommend_text)
    EditText copy_recommend_text;
    @Bind(com.jeremy.lychee.R.id.login_lay)
    View noLoginView;
    @Bind(com.jeremy.lychee.R.id.help_text)
    TextView help_text;
    @Bind(com.jeremy.lychee.R.id.loading_layout)
    View loading_view;
    @Bind(com.jeremy.lychee.R.id.error_layout)
    View error_view;
    @Bind(com.jeremy.lychee.R.id.contentview)
    View contentview;


    private   EditText copy_title_text;
    private static String currentSearchText = "";
    private String zt_Nid="";
    private static String mUrl="";
    private int mtype=3;
    private boolean isTrans = false;

    @Override
    protected void setContentView() {
        QEventBus.getEventBus().register(this);
        setContentView(com.jeremy.lychee.R.layout.activity_trans_article);
        ButterKnife.bind(this);


    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        configToolbar(com.jeremy.lychee.R.menu.menu_trans_article, com.jeremy.lychee.R.string.title_activity_transarticle, com.jeremy.lychee.R.drawable.ic_actionbar_back);
        if (isLogin()){
            noLoginView.setVisibility(View.GONE);
        }else {
            noLoginView.setVisibility(View.VISIBLE);

        }
        noLoginView.setOnClickListener(v -> login());
        copy_title_text = (EditText) findViewById(com.jeremy.lychee.R.id.copy_title_text);
        findViewById(com.jeremy.lychee.R.id.menu_transarticle_confirm2).setVisibility(View.GONE);
        findViewById(com.jeremy.lychee.R.id.menu_transarticle_confirm2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mUrl)) {
                    ToastHelper.getInstance(TransArticleActivity.this).toast("链接不能为空");
                    return;
                }
                if (TextUtils.isEmpty(copy_title_text.getText().toString().trim())) {
                    ToastHelper.getInstance(TransArticleActivity.this).toast("标题不能为空");
                    return;
                }
                if (!TextUtils.isEmpty(mSelectRecyclerView.getSubmitChannel())) {
                    transActicle(mSelectRecyclerView.getSubmitChannel());
                } else {
                    ToastHelper.getInstance(TransArticleActivity.this).toast("请选择需要转推到的专辑");
                }
            }
        });
        copy_url_text.addTextChangedListener(textWatcher);
        copy_url_text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    copy_url_text.setText("");
                    copy_title_text.setText("");

                }
                return false;
            }
        });
        help_text.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", "file:///android_asset/html/help-copy.html");
            openActivity(WebViewActivity.class, bundle, 0);
        });
        error_view.setOnClickListener(v ->{
            requestAndShowChannelData();
        });
        requestAndShowChannelData();
    }
    private boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }
    private void login() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private void transActicle(String subId ){
        WeakReference<ProgressDialog> dialog = new WeakReference<ProgressDialog>(new ProgressDialog(this));
        dialog.get().setIndeterminate(true);
        dialog.get().setMessage("新闻转推中...");
        dialog.get().setCancelable(false);
        dialog.get().show();
        Observable.just(subId)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                /*.flatMap(s -> Observable.from(s))*/
                .observeOn(Schedulers.io())
                .doOnNext(s -> {
                    String sign = AppUtil.signString(zt_Nid, s, mUrl, copy_recommend_text.getText().toString(), mtype, copy_title_text.getText().toString());
                    OldRetroAdapter.getService().transmitNews(
                            zt_Nid, mUrl, s, copy_recommend_text.getText().toString(), mtype, copy_title_text.getText().toString(), sign)
                            .subscribe(
                                    ss -> {
                                        if (ss.getErrno() == 0) {
                                            isTrans = true;
                                        }

                                    }, e -> {
                                        isTrans = false;
                                    }
                            );
                })
                /*.toList()*/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            if (ApplicationStatus.getRunningActivities().indexOf(this) != -1
                                    && dialog.get() != null && dialog.get().isShowing()) {
                            }
                            if (isTrans) {
                                QHStatAgent.onEvent(this, QHState.RECOMMENDARTICLE);
                                Toast.makeText(this, "转推成功!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "转推失败!", Toast.LENGTH_SHORT).show();
                            }
                            dialog.get().dismiss();
                            finish();
                            QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
                        }, e -> {
                            if (ApplicationStatus.getRunningActivities().indexOf(this) != -1
                                    && dialog.get() != null && dialog.get().isShowing()) {
                            }
                            isTrans = false;
                            dialog.get().dismiss();
                            Toast.makeText(this, "转推失败!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                );




    }
    private void requestAndShowChannelData(){
        contentview.setVisibility(View.GONE);
        noLoginView.setVisibility(View.GONE);
        error_view.setVisibility(View.GONE);
        loading_view.setVisibility(View.VISIBLE);

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
                        contentview.setVisibility(View.GONE);
                        loading_view.setVisibility(View.GONE);
                        noLoginView.setVisibility(View.GONE);
                        error_view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ModelBase<List<WeMediaChannel>> listModelBase) {
                        if (listModelBase.getErrno()==0){
                            mSelectRecyclerView.showAlbums(listModelBase.getData());
                            loading_view.setVisibility(View.GONE);
                            noLoginView.setVisibility(View.GONE);
                            error_view.setVisibility(View.GONE);
                            contentview.setVisibility(View.VISIBLE);
                        }else {
                            loading_view.setVisibility(View.GONE);
                            noLoginView.setVisibility(View.GONE);
                            error_view.setVisibility(View.VISIBLE);
                            contentview.setVisibility(View.GONE);

                        }
                    }
                });
    }

    private Toolbar configToolbar(int menuId, int titleId, int navigationIconId) {
        Toolbar mToolBar = (Toolbar) toolbarView.findViewById(com.jeremy.lychee.R.id.toolbar);
        if (menuId != 0) {
            mToolBar.inflateMenu(menuId);
        }
        if (titleId != 0) {
            mToolBar.setTitle(titleId);
        }
        if (navigationIconId != 0) {
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        return mToolBar;
    }

    final public void onEventMainThread(Events.OnAlbumListUpdated event) {
        requestAndShowChannelData();
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString())) {
                currentSearchText = s.toString();
                if (URLUtil.isValidUrl(currentSearchText)){
                    searchSubscription(currentSearchText);
                    findViewById(com.jeremy.lychee.R.id.menu_transarticle_confirm).setVisibility(View.GONE);
                    findViewById(com.jeremy.lychee.R.id.menu_transarticle_confirm2).setVisibility(View.VISIBLE);
                }else {
                    ToastHelper.getInstance(TransArticleActivity.this).toast("链接地址不合法!");
                    currentSearchText="";
                    copy_url_text.setText("");
                    findViewById(com.jeremy.lychee.R.id.menu_transarticle_confirm2).setVisibility(View.GONE);
                    findViewById(com.jeremy.lychee.R.id.menu_transarticle_confirm).setVisibility(View.VISIBLE);
                }
            }else {
                currentSearchText="";
                findViewById(com.jeremy.lychee.R.id.menu_transarticle_confirm2).setVisibility(View.GONE);
                findViewById(com.jeremy.lychee.R.id.menu_transarticle_confirm).setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
           /* search_subscription_text.setSelection(currentSearchText.length());*/

        }
    };

    private void searchSubscription(String word) {
        if (!TextUtils.isEmpty(word)) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(word!=null && !TextUtils.isEmpty(currentSearchText) && word.equals(currentSearchText)){
                        mHandler.sendEmptyMessage(1);
                    }


                }
            },300);
        }
    }

    private  android.os.Handler mHandler = new android.os.Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1:
                    beginSearch(currentSearchText);
                    break;
            }
        }
    };
    //去搜索url吧
    public  void beginSearch(String key){
        OldRetroAdapter.getService().getTitleByUrl(key).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    if (s.getErrno()==0){
                        if (s.getData()!=null){
                            copy_title_text.setText(s.getData().toString());
                            mUrl = currentSearchText;
                        }else {
                            ToastHelper.getInstance(TransArticleActivity.this).toast("获取标题失败!");
                            copy_url_text.setText("");
                        }
                    }else {
                        copy_title_text.setText("");
                    }
                }, Throwable::printStackTrace);
    }
    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
        QEventBus.getEventBus().unregister(this);
    }

    public void onEvent(Events.Logout event) {
        //退出登录
        noLoginView.setVisibility(View.VISIBLE);
    }

    public void onEvent(Events.LoginOk event) {
        //登录成功
        noLoginView.setVisibility(View.GONE);
        requestAndShowChannelData();
    }
}
