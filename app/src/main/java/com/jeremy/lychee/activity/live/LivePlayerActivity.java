package com.jeremy.lychee.activity.live;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeremy.lychee.base.ImmersionActivity;
import com.jeremy.lychee.videoplayer.LiveCloudCallbackWrapper;
import com.jeremy.lychee.videoplayer.VideoPlayer;
import com.jeremy.lychee.videoplayer.VideoPlayerPanelBasic;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.videoplayer.VideoPlayerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

public class LivePlayerActivity extends ImmersionActivity {
    public static final String INTENT_ID_BUNDLE = "id";
    public static final String INTENT_TOPIC_ID_BUNDLE = "topicId";
    public static final String INTENT_TAG_BUNDLE = "tag";

    @Bind(com.jeremy.lychee.R.id.live_video_player)
    VideoPlayerView playerView;

    private LiveCloudCallbackWrapper liveCloudCallback = LiveCloudCallbackWrapper.createInstance()
            .addOnSizeChangedAction(this::onVideoSizeChanged, AndroidSchedulers.mainThread())
            .addOnCompletionAction(this::onCompletion, AndroidSchedulers.mainThread());

    static public void loadVideo(Context context, String id, String topicId, String tag) {
        AppUtil.show3GTrafficAlarm(context, context.getString(com.jeremy.lychee.R.string.dialog_use_mobile_net_play), () ->
                context.startActivity(new Intent(context, LivePlayerActivity.class)
                        .putExtra(LivePlayerActivity.INTENT_TOPIC_ID_BUNDLE, topicId)
                        .putExtra(LivePlayerActivity.INTENT_ID_BUNDLE, id)
                        .putExtra(LivePlayerActivity.INTENT_TAG_BUNDLE, tag)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (playerView != null) {
            playerView.unBindToVideoPlayer();
        }
        VideoPlayer.getInstance().unregisterErrorHandler(this::onError);
        VideoPlayer.getInstance().getLiveCloudCallback().unchainLiveCloudCallback(liveCloudCallback);
        ButterKnife.unbind(this);
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_live_player);
        ButterKnife.bind(this);

        VideoPlayer.getInstance().getLiveCloudCallback().chainLiveCloudCallback(liveCloudCallback);
        VideoPlayer.getInstance().registerErrorHandler(this::onError);
        VideoPlayer.getInstance().setRendererContainer((ViewGroup) findViewById(com.jeremy.lychee.R.id.root));

        playerView.addControlPanel(new VideoPlayerPanelBasic());
        playerView.bindToVideoPlayer();

        String id = getIntent().getStringExtra(INTENT_ID_BUNDLE);
        String topicId = getIntent().getStringExtra(INTENT_TOPIC_ID_BUNDLE);
        String tag = getIntent().getStringExtra(INTENT_TAG_BUNDLE);

        VideoPlayer.getInstance().load(this, id, topicId, tag);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();

        //状态栏沉浸
        setFitsSystemWindows(true);
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

    @Override
    public View onGetFitWindowView() {
        return findViewById(com.jeremy.lychee.R.id.root);
    }

    private void onError(int errorCode) {
        Toast.makeText(this, VideoPlayer.textMessageForErrorCode(errorCode), Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnClick(com.jeremy.lychee.R.id.close)
    public void onCloseClicked() {
        VideoPlayer.getInstance().onBackPressed(this);
        finish();
    }

    private void onVideoSizeChanged(int width, int height) {
        if (playerView == null || width <= 0 || height <= 0 || playerView.getMeasuredWidth() <= 0) {
            return;
        }

        int containerWidth = playerView.getMeasuredWidth();
        int controlPanelHeight = containerWidth * height / width;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(containerWidth, controlPanelHeight);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        playerView.setLayoutParams(layoutParams);
    }

    private void onCompletion() {
        finish();
    }
}
