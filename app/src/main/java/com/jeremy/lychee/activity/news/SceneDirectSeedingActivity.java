package com.jeremy.lychee.activity.news;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.model.live.IsPlayingLive;
import com.jeremy.lychee.model.news.Comment;
import com.jeremy.lychee.model.news.NewsListDataWrapper;
import com.jeremy.lychee.net.CommentRetroAdapter;
import com.jeremy.lychee.utils.hitLog.HitLog;
import com.jeremy.lychee.videoplayer.VideoPlayer;
import com.jeremy.lychee.videoplayer.VideoPlayerPanelBasic;
import com.jeremy.lychee.widget.shareWindow.ShareManager;
import com.jeremy.lychee.activity.live.LivePlayerActivity;
import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.model.news.ResultCmt;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.videoplayer.VideoPlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SceneDirectSeedingActivity extends WebViewActivity {


    public static final String NEW_ENTITY = "NEW_ENTITY";
    @Bind(com.jeremy.lychee.R.id.et_comment)
    public EditText et_comment;

    @Bind(com.jeremy.lychee.R.id.btn_share)
    public TextView btn_share;

    @Bind(com.jeremy.lychee.R.id.player_view)
    public VideoPlayerView videoPlayerView;

    @Bind(com.jeremy.lychee.R.id.player_area)
    public View player_area;


    private String hostName;
    private String hostMessage;
    private NewsListDataWrapper mNewsEntity;

    @Override
    protected int getLayoutId() {
        return com.jeremy.lychee.R.layout.activity_scene_direct_seeding;
    }

    @Override
    protected WebView createWebView() {
        return new WebView(this);
    }

    @Override
    protected void setContentView() {
        super.setContentView();
        ButterKnife.bind(this);
        getWebView().addJavascriptInterface(this, "$_video");
        mNewsEntity = getIntent().getParcelableExtra(SceneDirectSeedingActivity.NEW_ENTITY);
        RxView.clicks(btn_share)
                .map(viewClickEvent -> TextUtils.isEmpty(btn_share.getText()))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        share();
                    } else {
                        sendComment(et_comment, hostName, hostMessage, mNewsEntity.getUrl());
                    }
                });
        RxView.focusChanges(et_comment)
                .map(focusChangeEvent -> et_comment.hasFocus())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        btn_share.setBackgroundDrawable(null);
                        btn_share.setText("发送");
                        boolean userInfoEmpty = Session.isUserInfoEmpty();
                        if (userInfoEmpty) {
                            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 1);
                            et_comment.clearFocus();
                        }
                    } else {
                        btn_share.setText("");
                        this.hostName = null;
                        this.hostMessage = null;
                        btn_share.postDelayed(() -> {
                            AppUtil.hideSoft(SceneDirectSeedingActivity.this);
                            btn_share.setBackgroundResource(com.jeremy.lychee.R.drawable.live_pd_icon_share);
                        }, 200);
                    }
                });
    }


    private void livePlay(String id) {
        IsPlayingLive item = new IsPlayingLive();
        item.setId(id);
        item.setVideo_type("1");
        item.setTag("");
        VideoPlayer.getInstance().stop();
        videoPlayerView.setVisibility(View.VISIBLE);
        videoPlayerView.showLoadingAnim();
        videoPlayerView.bindToVideoPlayer();
        videoPlayerView.addControlPanel(new VideoPlayerPanelBasic());
        VideoPlayer.getInstance().load(this, item.getId(), item.getVideo_type(), item.getTag());
    }

    private void share() {
        ShareInfo shareInfo = new ShareInfo(mNewsEntity.getShare(), mNewsEntity.getNid(), mNewsEntity.getTitle(), "", mNewsEntity.getAlbum_pic(), null, ShareInfo.SHARECONTENT_NEWS);
        new ShareManager(SceneDirectSeedingActivity.this, shareInfo, true,
                () -> HitLog.hitLogShare("null", "null", mNewsEntity.getNid(), mNewsEntity.getNews_from())) //分享打点
                .show();
    }


    private void sendComment(EditText et_comment, String hosterName, String sourceComment, String url) {
        et_comment.clearFocus();
        Observable.just(et_comment.getText().toString())
                .compose(bindToLifecycle())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(s -> {
                    if (TextUtils.isEmpty(s)) {
                        Toast.makeText(SceneDirectSeedingActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        AppUtil.hideSoft(this);
                    }

                    return true;
                })
                .map(message -> {
                    if (TextUtils.isEmpty(hosterName)) {
                        message = et_comment.getText().toString();
                    } else {
                        message = String.format("%s@@@%s$$$%s", et_comment.getText().toString(), hosterName, sourceComment);
                    }
                    return message;
                })
                .observeOn(Schedulers.io())
                .flatMap(message -> {

                    String sign = getSendCmtSign(message);
                    return CommentRetroAdapter.getCommentService().sendComment(url, message, Session.getSession().getToken(), Session.getSession().getUid(), sign);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultCmt<Comment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResultCmt<Comment> commentResultCmt) {
                        et_comment.setText("");
                        hostName = null;
                        hostMessage = null;
                        Toast.makeText(SceneDirectSeedingActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
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


    @JavascriptInterface
    public void OnClickReply(String json) {
//        Toast.makeText(SceneDirectSeedingActivity.this, json, Toast.LENGTH_SHORT).show();
//        {"id":"517","live_id":"17","host_name":"小胖亲亲","host_icon":"http://p3.qhimg.com/dc/100_100___/t01b206cc95ebff2c8f.jpg?size=81x80","time":"10:44:57","message":"放大放大","image":[],"gif":[]}
//        Log.e("json", json);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String hostName = jsonObject.getString("host_name");
                    String hostMessage = jsonObject.getString("message");
//                    showPopupWindow(hostName, hostMessage);
                    SceneDirectSeedingActivity.this.hostName = hostName;
                    SceneDirectSeedingActivity.this.hostMessage = hostMessage;
                    et_comment.requestFocus();
                    new Handler().postDelayed(() -> {
                        InputMethodManager keyboard = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(et_comment, 0);
                    }, 100);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void playListVideo(String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LivePlayerActivity.loadVideo(SceneDirectSeedingActivity.this, new JSONObject(json).getString("video_id"), "1", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void setLiveVideo(String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (player_area.getVisibility() == View.VISIBLE) {
                    return;
                }
                player_area.setVisibility(View.VISIBLE);
                try {
                    livePlay(new JSONObject(json).getString("video_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(SceneDirectSeedingActivity.this, "has video", Toast.LENGTH_SHORT).show();
            }
        });
    }

  /*  public PopupWindow showPopupWindow(String hostname, String hostMessage) {
        final TextView view = new TextView(this);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.WHITE);
        view.setBackgroundResource(R.drawable.ic_pop_reply);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setFocusableInTouchMode(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent))); // 否则 无法dismiss
        view.setText("回复");
        view.setOnClickListener(v -> {
            this.hostName = hostname;
            this.hostMessage = hostMessage;
            popupWindow.dismiss();
            et_comment.requestFocus();
            view.postDelayed(() -> {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(et_comment, 0);
            }, 100);
        });
//        popupWindow.setAnimationStyle(R.style.AnimationPopup);
//        bindPopView(popupWindow, view, post);
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int[] location = new int[2];
        getWebView().getLocationInWindow(location);
        location[0] += getWebView().getMeasuredWidth() / 2 - view.getMeasuredWidth() / 2;
        location[1] += getWebView().getMeasuredHeight() / 2;
        popupWindow.showAtLocation(getWebView(), Gravity.NO_GRAVITY, location[0], location[1]);
        return popupWindow;
    }*/
}
