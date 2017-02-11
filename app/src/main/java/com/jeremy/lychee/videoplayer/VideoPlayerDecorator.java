package com.jeremy.lychee.videoplayer;

import android.content.Context;
import android.view.ViewGroup;

public abstract class VideoPlayerDecorator {
    private VideoPlayerView videoPlayerView;
    private ViewGroup decoratorView;

    public void init() {}
    public void unInit() {}

    public Context getContext() {
        if (videoPlayerView == null) {
            return null;
        }
        return videoPlayerView.getContext();
    }
    public abstract int getLayoutId();
    public void onViewCreated(ViewGroup decoratorView) {
        this.decoratorView = decoratorView;
    }

    public LiveCloudCallbackWrapper getLiveCloudCallbackWrapper() {
        return null;
    }

    public void onUIActionNotification(VideoPlayerView.UIActionNotification notification, VideoPlayerView videoPlayerView) {
    }

    public void setVideoPlayerView(VideoPlayerView videoPlayerView) {
        this.videoPlayerView = videoPlayerView;
    }
    public VideoPlayerView getVideoPlayerView() {
        return videoPlayerView;
    }

    public ViewGroup getDecoratorView() {
        return decoratorView;
    }
}
