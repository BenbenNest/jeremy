package com.jeremy.lychee.activity.live;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeremy.lychee.adapter.live.LiveVideoListAdapter;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.preference.UserPreference;
import com.jeremy.lychee.R;
import com.jeremy.lychee.manager.live.LiveBrowseHistoryManager;
import com.jeremy.lychee.manager.live.LiveVideoListManager;
import com.jeremy.lychee.manager.live.LiveVideoManager;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ScrollSmoothLineaerLayoutManager;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.videoplayer.VideoPlayer;
import com.jeremy.lychee.videoplayer.VideoPlayerView;
import com.jeremy.lychee.customview.PullListRecyclerView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class LiveVideoListActivity extends SlidingActivity implements LiveVideoListAdapter.LiveVideoListAdapterDelegate {
    private static final String INTENT_TOPIC_ID_BUNDLE = "topic_id";
    private static final String INTENT_ID_BUNDLE = "id";
    private static final String INTENT_TAG_BUNDLE = "tag";
    private static final String INTENT_LIVE_VIDEO_LIST_BUNDLE = "live_video_list";
    private static final String INTENT_LIVE_VIDEO_LIST_MGR_BUNDLE = "live_video_list_mgr";

    @Bind(R.id.live_video_list)
    PullListRecyclerView liveVideoListRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.loading_image_view)
    ImageView loadingView;
    @Bind(R.id.video_list_panel)
    RelativeLayout videoListPanel;

    private ValueAnimator loadingAnimator;

    private LiveVideoListAdapter liveVideoListAdapter;
    private Subscription playSubscription;

    private Subject<Integer, Integer> videoPlayEventSubject;
    private Object onThumbsUpSuccessSubscription;

    private int currentIdx = -1;
    private LiveVideoListManager liveVideoListManager;

    static public void loadVideo(SlidingActivity context, String id, String topicId, String tag) {
        AppUtil.show3GTrafficAlarm(context, context.getString(R.string.dialog_use_mobile_net_play), () -> {
            Bundle bundle = new Bundle();
            bundle.putString(LiveVideoListActivity.INTENT_TOPIC_ID_BUNDLE, topicId);
            bundle.putString(LiveVideoListActivity.INTENT_ID_BUNDLE, id);
            bundle.putString(LiveVideoListActivity.INTENT_TAG_BUNDLE, tag);

            context.openActivity(LiveVideoListActivity.class, bundle, 0);
        });
    }

    static public void loadVideo(SlidingActivity context, String topicId, String id, String tag, List<LiveVideoInfo> videoList, LiveVideoListManager liveVideoListManager) {
        AppUtil.show3GTrafficAlarm(context, context.getString(R.string.dialog_use_mobile_net_play), () -> {
            ArrayList<LiveVideoInfo> videoArrayList = new ArrayList<>();
            videoArrayList.addAll(videoList);

            Bundle bundle = new Bundle();
            bundle.putString(LiveVideoListActivity.INTENT_TOPIC_ID_BUNDLE, topicId);
            bundle.putString(LiveVideoListActivity.INTENT_ID_BUNDLE, id);
            bundle.putString(LiveVideoListActivity.INTENT_TAG_BUNDLE, tag);
            bundle.putParcelableArrayList(LiveVideoListActivity.INTENT_LIVE_VIDEO_LIST_BUNDLE, videoArrayList);
            bundle.putParcelable(LiveVideoListActivity.INTENT_LIVE_VIDEO_LIST_MGR_BUNDLE, liveVideoListManager);

            context.openActivity(LiveVideoListActivity.class, bundle, 0);
        });
    }

    static public void loadVideo(Context context, String id, String topicId, String tag) {
        AppUtil.show3GTrafficAlarm(context, context.getString(R.string.dialog_use_mobile_net_play), () ->
                context.startActivity(new Intent(context, LiveVideoListActivity.class)
                        .putExtra(LiveVideoListActivity.INTENT_TOPIC_ID_BUNDLE, topicId)
                        .putExtra(LiveVideoListActivity.INTENT_ID_BUNDLE, id)
                        .putExtra(LiveVideoListActivity.INTENT_TAG_BUNDLE, tag)));
    }

    static public void loadVideo(Context context, String topicId, String id, String tag, List<LiveVideoInfo> videoList, LiveVideoListManager liveVideoListManager) {
        AppUtil.show3GTrafficAlarm(context, context.getString(R.string.dialog_use_mobile_net_play), () -> {
            ArrayList<LiveVideoInfo> videoArrayList = new ArrayList<>();
            videoArrayList.addAll(videoList);

            context.startActivity(new Intent(context, LiveVideoListActivity.class)
                    .putExtra(LiveVideoListActivity.INTENT_TOPIC_ID_BUNDLE, topicId)
                    .putExtra(LiveVideoListActivity.INTENT_ID_BUNDLE, id)
                    .putExtra(LiveVideoListActivity.INTENT_TAG_BUNDLE, tag)
                    .putParcelableArrayListExtra(LiveVideoListActivity.INTENT_LIVE_VIDEO_LIST_BUNDLE, videoArrayList)
                    .putExtra(LiveVideoListActivity.INTENT_LIVE_VIDEO_LIST_MGR_BUNDLE, liveVideoListManager));
        });
    }

    private void initVideoList(String topicId, String id, String tag, List<LiveVideoInfo> videoList, LiveVideoListManager liveVideoListManager) {
        videoListPanel.setVisibility(View.VISIBLE);
        hideLoadingAnim();

        mToolbar.setNavigationOnClickListener(v -> finish());

        videoPlayEventSubject = new SerializedSubject<>(PublishSubject.create());
        bindVideoPlayEvent();

        VideoPlayer.getInstance().getLiveCloudCallback().addOnCompletionAction(this::onPlayerCoreCompletion);
        VideoPlayer.getInstance().registerErrorHandler(this::onError);

        liveVideoListAdapter = new LiveVideoListAdapter(this, topicId, id, tag, videoList, liveVideoListManager, this);
        liveVideoListRecyclerView.setAdapter(liveVideoListAdapter, true);
        liveVideoListRecyclerView.enablePullToRefresh(false);
        liveVideoListRecyclerView.getInternalRecyclerView().addOnScrollListener(recyclerViewScrollListener);
        liveVideoListRecyclerView.getInternalRecyclerView().setBackgroundColor(Color.parseColor("#ffffff"));

        onThumbsUpSuccessSubscription = LiveVideoManager.getInstance().registerCallback(LiveVideoManager.LiveVideoCallback.ON_POST_THUMBS_UP, (o) -> {
            if (liveVideoListAdapter != null) {
                liveVideoListAdapter.onThumbsUpSuccess(o);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        VideoPlayer.getInstance().getLiveCloudCallback().removeOnCompletionAction(this::onPlayerCoreCompletion);
        VideoPlayer.getInstance().unregisterErrorHandler(this::onError);

        if (onThumbsUpSuccessSubscription != null) {
            LiveVideoManager.getInstance().unRegisterCallback(onThumbsUpSuccessSubscription);
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_live_video_list);
        ButterKnife.bind(this);

        videoListPanel.setVisibility(View.GONE);
        showLoadingAnim();

        String topicId = getIntent().getExtras().getString(INTENT_TOPIC_ID_BUNDLE);
        String id = getIntent().getExtras().getString(INTENT_ID_BUNDLE);
        String tag = getIntent().getExtras().getString(INTENT_TAG_BUNDLE);
        if (topicId == null || id == null || tag == null) {
            Toast.makeText(this, VideoPlayer.textMessageForErrorCode(VideoPlayer.ErrorCode.INVALID_DATA), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        liveVideoListManager = getIntent().getExtras().getParcelable(INTENT_LIVE_VIDEO_LIST_MGR_BUNDLE);
        List<LiveVideoInfo> videoList = getIntent().getExtras().getParcelableArrayList(INTENT_LIVE_VIDEO_LIST_BUNDLE);

        if (liveVideoListManager == null || videoList == null || videoList.size() == 0) {
            liveVideoListManager = LiveVideoListManager.createInstance();

            liveVideoListManager.getLiveVideoList(topicId, id, tag, false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .map(it -> it.second)
                    .subscribe(it -> {
                        initVideoList(topicId, id, tag, it, liveVideoListManager);
                    }, e -> {
                        e.printStackTrace();
                        Toast.makeText(this, VideoPlayer.textMessageForErrorCode(VideoPlayer.ErrorCode.BUSINESS_SERVER_ERROR), Toast.LENGTH_SHORT).show();
                        finish();
                    });
        } else {
            initVideoList(topicId, id, tag, videoList, liveVideoListManager);
        }
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();

        //状态栏沉浸
        setFitsSystemWindows(true);
    }

    private void bindVideoPlayEvent() {
        videoPlayEventSubject
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .filter(it -> it != -1)
                .throttleWithTimeout(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    try {
                        playVideo(it);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        onError(VideoPlayer.ErrorCode.PLAYER_KERNEL_ERROR);
                    }
                }, e -> {
                    e.printStackTrace();
                    onError(VideoPlayer.ErrorCode.PLAYER_KERNEL_ERROR);
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

    private void playVideo(int itemIdx) {
        if (itemIdx < 0 || itemIdx >= liveVideoListAdapter.getVideoList().size()) {
            return;
        }

        if (playSubscription != null && !playSubscription.isUnsubscribed()) {
            playSubscription.unsubscribe();
        }

        VideoPlayer.getInstance().stop();

        currentIdx = itemIdx;
        ScrollSmoothLineaerLayoutManager layoutManager = (ScrollSmoothLineaerLayoutManager) liveVideoListRecyclerView.getInternalRecyclerView().getLayoutManager();
        ViewGroup item = (ViewGroup) layoutManager.findViewByPosition(itemIdx);
        if (item == null) {
            onError(VideoPlayer.ErrorCode.PLAYER_KERNEL_ERROR);
            return;
        }

        VideoPlayerView videoPlayer = (VideoPlayerView) item.findViewById(R.id.video_player_view);
        if (videoPlayer == null) {
            onError(VideoPlayer.ErrorCode.PLAYER_KERNEL_ERROR);
            return;
        }
        videoPlayer.bindToVideoPlayer();

        ImageView thumbnailImageView = (ImageView) item.findViewById(R.id.live_thumbnail);
        if (thumbnailImageView != null) {
            videoPlayer.setPlaceHolderDrawable(thumbnailImageView.getDrawable(), ImageView.ScaleType.CENTER);
        }

        LiveVideoInfo currentVideoInfo = liveVideoListAdapter.getVideoList().get(itemIdx);
        LiveBrowseHistoryManager.getInstance().addBrowseItem(currentVideoInfo);
        playSubscription = OldRetroAdapter.getService().getLiveChannelStreams(currentVideoInfo.getVideo_play_url()).map(ModelBase::getData).subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .doOnNext(it -> {
                    if (it == null || it.getVideo_stream().size() == 0) {
                        throw new RuntimeException(VideoPlayer.textMessageForErrorCode(VideoPlayer.ErrorCode.BUSINESS_SERVER_ERROR));
                    }
                })
                .subscribe(it -> {
                    VideoPlayer.getInstance().load(it);
                }, e -> {
                    e.printStackTrace();
                    if (e instanceof RuntimeException) {
                        onError(VideoPlayer.ErrorCode.INVALID_DATA);
                    } else {
                        onError(VideoPlayer.ErrorCode.BUSINESS_SERVER_ERROR);
                    }
                });
    }

    private RecyclerView.OnScrollListener recyclerViewScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            ScrollSmoothLineaerLayoutManager layoutManager = (ScrollSmoothLineaerLayoutManager) recyclerView.getLayoutManager();
            int itemIdx = layoutManager.findFirstCompletelyVisibleItemPosition();
            if (itemIdx == RecyclerView.NO_POSITION) {
                itemIdx = layoutManager.findFirstVisibleItemPosition();
            }

            int lastCompletelyVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            int totalCount = layoutManager.getItemCount();
            if (lastCompletelyVisibleItem == totalCount - 1 && totalCount > 1) {
                itemIdx = lastCompletelyVisibleItem - 1;
            }

            if (itemIdx != -1) {
                if (AppUtil.isNetTypeWifi(getApplicationContext())) {
                    videoPlayEventSubject.onNext(itemIdx);
                }
            }
        }
    };

    private void onPlayerCoreCompletion() {
        RecyclerView.ViewHolder viewHolder = liveVideoListRecyclerView.getInternalRecyclerView().findViewHolderForAdapterPosition(currentIdx);
        if (viewHolder == null || currentIdx >= liveVideoListAdapter.getVideoList().size() - 1) {
            if (VideoPlayerView.getBindingView() != null ) {
                VideoPlayerView.getBindingView().unBindToVideoPlayer();
            }
        } else {
            liveVideoListRecyclerView.getInternalRecyclerView().smoothScrollBy(0, viewHolder.itemView.getHeight());
        }
    }

    private void onError(@VideoPlayer.ErrorCode int errorCode) {
        AndroidSchedulers.mainThread().createWorker().schedule(() -> {
            if (liveVideoListRecyclerView == null || liveVideoListRecyclerView.getInternalRecyclerView() == null) {
                return;
            }

            if (liveVideoListRecyclerView.getInternalRecyclerView().getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                Toast.makeText(this, VideoPlayer.textMessageForErrorCode(errorCode), Toast.LENGTH_SHORT).show();
                onPlayerCoreCompletion();
            }
        });
    }

    @Override
    public void onPlayVideo(int nIndex) {
        try {
            if (!AppUtil.isNetTypeWifi(getApplicationContext()) && !UserPreference.getInstance().getUseMobileNetEnabled()) {
                DialogUtil.showConfirmDialog(this, getString(R.string.dialog_use_mobile_net_play),
                        getString(R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                            UserPreference.getInstance().setUseMobileNetEnabled(true);
                            dialog.dismiss();
                            liveVideoListRecyclerView.getInternalRecyclerView().getLayoutManager().smoothScrollToPosition(liveVideoListRecyclerView.getInternalRecyclerView(), null, nIndex);
                            videoPlayEventSubject.onNext(-1);
                            videoPlayEventSubject.onNext(nIndex);
                        }, getString(R.string.dialog_button_cancel), DialogInterface::dismiss
                );
            } else {
                liveVideoListRecyclerView.getInternalRecyclerView().getLayoutManager().smoothScrollToPosition(liveVideoListRecyclerView.getInternalRecyclerView(), null, nIndex);
                videoPlayEventSubject.onNext(-1);
                videoPlayEventSubject.onNext(nIndex);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public void showLoadingAnim() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);

            loadingAnimator = ObjectAnimator.ofFloat(loadingView, View.ROTATION, 0f, 360f).setDuration(1000);
            loadingAnimator.setInterpolator(new LinearInterpolator());
            loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
            loadingAnimator.start();
        }
    }

    public void hideLoadingAnim() {
        if (loadingAnimator != null && loadingAnimator.isRunning()) {
            loadingAnimator.cancel();
            loadingAnimator = null;
        }

        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }
}
