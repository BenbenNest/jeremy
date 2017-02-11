package com.jeremy.lychee.videoplayer;

import android.content.Context;
import android.view.ViewGroup;

public abstract class VideoPlayerPanel {
    public interface PanelType {
        int PanelType_Normal = 1;
        int PanelType_FullScreen = 1 << 1;
    }

    private VideoPlayerView videoPlayerView;
    private ViewGroup panelView;
    private int panelType = PanelType.PanelType_Normal | PanelType.PanelType_FullScreen;

    public void init() {}
    public void unInit() {}

    public void setPanelType(int panelType) {
        this.panelType = panelType;
    }

    public boolean isPanelSuitFor(int panelType) {
        return (this.panelType & panelType) != 0;
    }

    public Context getContext() {
        if (videoPlayerView == null) {
            return null;
        }
        return videoPlayerView.getContext();
    }

    public abstract int getLayoutId();
    public void onViewCreated(ViewGroup panelView) {
        this.panelView = panelView;
    }

    public LiveCloudCallbackWrapper getLiveCloudCallbackWrapper() {
        return null;
    }

    public void setVideoPlayerView(VideoPlayerView videoPlayerView) {
        this.videoPlayerView = videoPlayerView;
    }
    public VideoPlayerView getVideoPlayerView() {
        return videoPlayerView;
    }

    public ViewGroup getPanelView() {
        return panelView;
    }

    public void onShowPanel(boolean isFullScreen) {}
    public void onHidePanel(boolean isFullScreen) {}
}
