package com.jeremy.lychee.videoplayer;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class VideoPlayerBottomProgressDecorator extends VideoPlayerDecorator {
    @Bind(com.jeremy.lychee.R.id.bottom_progress)
    ProgressBar progressBar;

    private LiveCloudCallbackWrapper liveCloudCallbackWrapper = LiveCloudCallbackWrapper.createInstance()
            .addOnProgressChangeAction(this::onProgressChange, AndroidSchedulers.mainThread());

    @Override
    public void onViewCreated(ViewGroup decoratorView) {
        super.onViewCreated(decoratorView);
        ButterKnife.bind(this, decoratorView);

        int duration = VideoPlayer.getInstance().getDuration();
        int progress = VideoPlayer.getInstance().getPosition();
        if (duration > 0 && progress > 0) {
            progressBar.setMax(duration);
            progressBar.setProgress(progress);
        }
    }

    @Override
    public int getLayoutId() {
        return com.jeremy.lychee.R.layout.video_view_decorator_bottom_progress;
    }

    @Override
    public LiveCloudCallbackWrapper getLiveCloudCallbackWrapper() {
        return liveCloudCallbackWrapper;
    }

    private void onProgressChange(int total, int progress) {
        if (total <= 0 || progress <= 0) {
            return;
        }

        progressBar.setMax(total);

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", progress);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.start();
    }

    @Override
    public void onUIActionNotification(VideoPlayerView.UIActionNotification notification, VideoPlayerView videoPlayerView) {
        if (notification == VideoPlayerView.UIActionNotification.OnEnterFullScreen) {
            if (getDecoratorView() != null) {
                getDecoratorView().setVisibility(View.GONE);
            }
        } else if (notification == VideoPlayerView.UIActionNotification.OnExitFullScreen) {
            if (getDecoratorView() != null) {
                getDecoratorView().setVisibility(View.VISIBLE);
            }
        } else if (notification == VideoPlayerView.UIActionNotification.OnShowControlPanel) {
            if (VideoPlayerView.isFullScreen()) {
                return;
            }
            if (getDecoratorView() != null) {
                getDecoratorView().setVisibility(View.GONE);
            }
        } else if (notification == VideoPlayerView.UIActionNotification.OnHideControlPanel) {
            if (VideoPlayerView.isFullScreen()) {
                return;
            }
            if (getDecoratorView() != null) {
                getDecoratorView().setVisibility(View.VISIBLE);
            }
        }
    }
}
