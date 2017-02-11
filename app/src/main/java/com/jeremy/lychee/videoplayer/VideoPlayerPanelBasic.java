package com.jeremy.lychee.videoplayer;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jeremy.lychee.model.live.LiveChannelStreams;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.R;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoPlayerPanelBasic extends VideoPlayerPanel {
    @Bind(R.id.time_elapsed)
    TextView timeElapsed;
    @Bind(R.id.time_remains)
    TextView timeRemains;
    @Bind(R.id.progress_bar)
    SeekBar progressBar;
    @Bind(R.id.play_pause)
    ImageView btnPlay;
    @Bind(R.id.full_screen)
    ImageView fullScreen;
    @Bind(R.id.living_text)
    TextView livingText;
    @Bind(R.id.resolution)
    TextView resolutionText;
    @Bind(R.id.mute_btn)
    ImageView muteBtn;

    private Observable<Pair<Integer, Integer>> positionObservable;
    private Subscription positionSubscription;
    private boolean progressDragging = false;

    private PopupWindow resolutionPopMenu;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void unInit() {
        super.unInit();

        if (positionSubscription != null && !positionSubscription.isUnsubscribed()) {
            positionSubscription.unsubscribe();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.player_ctrl_panel_base;
    }

    @Override
    public void onShowPanel(boolean isFullScreen) {
        super.onShowPanel(isFullScreen);

        fullScreen.setImageResource(isFullScreen ? R.drawable.live_icon_contraction : R.drawable.live_icon_open);
        resolutionText.setVisibility(isFullScreen ? View.VISIBLE : View.GONE);

        updateMuteBtnUI();
        updatePlayBtnUI();
        updateProgress(VideoPlayer.getInstance().getDuration(), VideoPlayer.getInstance().getPosition());

        switch (VideoPlayer.getInstance().getCurrentStream().getStream_type()) {
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_HUAJIAO_LIVE:
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_BTV_LIVE:
            case LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_NEWS_LIVE:
                livingText.setVisibility(View.VISIBLE);
                timeRemains.setVisibility(View.GONE);
                timeElapsed.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                break;
        }

        if (isFullScreen) {
            if (VideoPlayer.getInstance().getCurrentStream() == null || VideoPlayer.getInstance().getStreams() == null ||
                    VideoPlayer.getInstance().getStreams().getVideo_stream() == null || VideoPlayer.getInstance().getStreams().getVideo_stream().size() <= 1) {
                resolutionText.setVisibility(View.GONE);
            } else {
                resolutionText.setVisibility(View.VISIBLE);
                resolutionText.setText(VideoPlayer.getInstance().getCurrentStream().getStream_vbt());
            }
        }
    }

    @Override
    public void onHidePanel(boolean isFullScreen) {
        super.onHidePanel(isFullScreen);
    }

    @Override
    public void onViewCreated(ViewGroup panelView) {
        super.onViewCreated(panelView);
        ButterKnife.bind(this, panelView);

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    getVideoPlayerView().delayHide();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progressDragging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                VideoPlayer.getInstance().seekTo(seekBar.getProgress());
                progressDragging = false;
            }
        });

        positionSubscription = positionObserver().subscribe(pos -> {
            updateProgress(pos.first, pos.second);
        }, Throwable::printStackTrace);
    }

    private String formatTime(int time, boolean negative) {
        time /= 1000;
        int hour = time / 3600;
        int minute = (time - hour * 3600) / 60;
        int second = (time - hour * 3600 - minute * 60);

        String template = negative ? "-%02d:%02d:%02d" : "%02d:%02d:%02d";
        return String.format(template, hour, minute, second);
    }

    private Observable<Pair<Integer, Integer>> positionObserver() {
        if (positionObservable == null) {
            positionObservable = Observable.interval(500, TimeUnit.MILLISECONDS, Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(((RxAppCompatActivity) getPanelView().getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                    .map(it -> new Pair<>(VideoPlayer.getInstance().getDuration(), VideoPlayer.getInstance().getPosition()))
                    .doOnNext(it -> {
                        updatePlayBtnUI();
                        updateMuteBtnUI();
                    })
                    .filter(it -> it.first > 0)
                    .onBackpressureDrop();
        }
        return positionObservable;
    }

    private void updatePlayBtnUI() {
        if (VideoPlayer.getInstance().isPlaying()) {
            btnPlay.setImageResource(R.drawable.live_icon_suspend);
        } else {
            btnPlay.setImageResource(R.drawable.live_icon_play);
        }
    }

    private void updateMuteBtnUI() {
        if (!VideoPlayer.getInstance().isMute()) {
            muteBtn.setImageResource(R.drawable.play_sound_open);
        } else {
            muteBtn.setImageResource(R.drawable.play_sound_close);
        }
    }

    private void updateProgress(int duration, int elapsed) {
        if (duration < 0) {
            return;
        }

        timeElapsed.setText(formatTime(elapsed, false));
        timeRemains.setText(formatTime(duration - elapsed, true));
        progressBar.setMax(duration);
        if (!progressDragging) {
            progressBar.setProgress(elapsed);
        }
    }

    @OnClick(R.id.play_pause)
    void onPauseClicked() {
        if (VideoPlayer.getInstance().isPlaying()) {
            VideoPlayer.getInstance().pause();
        } else {
            VideoPlayer.getInstance().start();
        }
    }

    @OnClick(R.id.mute_btn)
    void onMuteClicked() {
        VideoPlayer.getInstance().setMute(!VideoPlayer.getInstance().isMute());
    }

    @OnClick(R.id.full_screen)
    void onFullScreenClicked() {
        if (getVideoPlayerView() == null) {
            return;
        }

        if (VideoPlayerView.isFullScreen()) {
            VideoPlayerView.exitFullScreen();
        } else {
            VideoPlayerView.enterFullScreen();
        }
    }

    @OnClick(R.id.resolution)
    void onResolutionClicked() {
        if (VideoPlayer.getInstance().getStreams() == null ||
                VideoPlayer.getInstance().getStreams().getVideo_stream() == null ||
                VideoPlayer.getInstance().getStreams().getVideo_stream().size() <= 1) {
            return;
        }

        if (resolutionPopMenu == null) {
            showResolutionPopMenu();
        } else {
            hideResolutionPopMenu();
        }
    }

    private void showResolutionPopMenu() {
        if (getPanelView() == null) {
            return;
        }

        if (getVideoPlayerView() != null) {
            getVideoPlayerView().hideControlPanel(true);
        }

        RelativeLayout popMenuLayout = (RelativeLayout) LayoutInflater.from(getPanelView().getContext()).inflate(R.layout.video_resolution_popmenu, null);
        LinearLayout resolutionList = (LinearLayout) popMenuLayout.findViewById(R.id.resolution_list);
        for (LiveChannelStreams.VideoStreamEntity streamEntity : VideoPlayer.getInstance().getStreams().getVideo_stream()) {
            TextView textView = new TextView(getPanelView().getContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dip2px(getPanelView().getContext(), 66), DensityUtils.dip2px(getPanelView().getContext(), 28));
            params.setMargins(DensityUtils.dip2px(getPanelView().getContext(), 35), 0, DensityUtils.dip2px(getPanelView().getContext(), 35), 0);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setTextColor(streamEntity.getStream_url().equals(VideoPlayer.getInstance().getCurrentStream().getStream_url()) ? 0xffff1e50 : 0xffffffff);
            textView.setTextSize(18);
            textView.setText(streamEntity.getStream_vbt());
            if (streamEntity.getStream_url().equals(VideoPlayer.getInstance().getCurrentStream().getStream_url())) {
                textView.setBackgroundResource(R.drawable.resolution_list_item_border);
            }
            textView.setOnClickListener(v -> {
                if (streamEntity != null && streamEntity.getStream_url() != null && VideoPlayer.getInstance().getCurrentStream() != null && VideoPlayer.getInstance().getCurrentStream().getStream_url() != null) {
                    if (!streamEntity.getStream_url().equals(VideoPlayer.getInstance().getCurrentStream().getStream_url())) {
                        VideoPlayer.getInstance().switchStream(streamEntity, VideoPlayer.getInstance().getPosition());
                    }
                }
                if (resolutionPopMenu != null) {
                    resolutionPopMenu.dismiss();
                }
            });

            resolutionList.addView(textView);
        }
        popMenuLayout.setOnClickListener(v -> {
            if (resolutionPopMenu != null) {
                resolutionPopMenu.dismiss();
            }
        });

        resolutionPopMenu = new PopupWindow(popMenuLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        resolutionPopMenu.setFocusable(true);
        resolutionPopMenu.setBackgroundDrawable(new BitmapDrawable());
        resolutionPopMenu.setOnDismissListener(() -> resolutionPopMenu = null);
        resolutionPopMenu.setAnimationStyle(R.style.dialog_view_animation);
        resolutionPopMenu.showAtLocation((View) getPanelView().getParent(), Gravity.NO_GRAVITY, 0, 0);
    }

    private void hideResolutionPopMenu() {
        if (resolutionPopMenu == null) {
            return;
        }

        resolutionPopMenu.dismiss();
    }
}
