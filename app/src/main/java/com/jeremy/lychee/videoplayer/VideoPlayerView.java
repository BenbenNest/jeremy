package com.jeremy.lychee.videoplayer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jeremy.lychee.base.ActivityState;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.utils.AppUtil;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;

public class VideoPlayerView extends RelativeLayout {
    private static VideoPlayerView viewHasBinding;
    private static ApplicationStatus.ActivityStateListener bindingViewActivityListener;
    private static VideoPlayerView fullScreenView;
    private static ApplicationStatus.ActivityStateListener fullScreenViewActivityListener;

    private LiveCloudCallbackWrapper liveCloudCallbackWrapper = LiveCloudCallbackWrapper.createInstance()
            .addOnBufferingUpdateAction(this::onPlayCoreBufferingUpdate);

    private ImageView loadingView;
    private ValueAnimator loadingAnimator;

    private List<VideoPlayerPanel> panelList = new ArrayList<>();
    private Map<VideoPlayerPanel, ViewGroup> panelViewMap = new HashMap<>();
    private Subscription panelHideSubscription;
    private long lastPanelShownTime = -1;
    private boolean isPanelShown = false;

    private List<VideoPlayerDecorator> decoratorList = new ArrayList<>();
    private Map<VideoPlayerDecorator, ViewGroup> decoratorViewMap = new HashMap<>();

    private GlideImageView placeHolderImage;
    private WeakReference<Drawable> placeHolderDrawable;
    private String placeHolderUrl;

    private List<Action2<UIActionNotification, VideoPlayerView>> uiActionNotificationList = new ArrayList<>();
    public enum UIActionNotification {
        OnBinded,
        OnUnBinded,
        OnEnterFullScreen,
        OnExitFullScreen,
        OnShowControlPanel,
        OnHideControlPanel
    }

    public VideoPlayerView(Context context) {
        super(context);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initUI() {
        setBackgroundColor(Color.BLACK);

        placeHolderImage = new GlideImageView(getContext());
        placeHolderImage.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(placeHolderImage);

        if ((placeHolderDrawable == null || placeHolderDrawable.get() == null) && TextUtils.isEmpty(placeHolderUrl)) {
            placeHolderImage.setVisibility(GONE);
        } else if (placeHolderDrawable != null && placeHolderDrawable.get() != null) {
            placeHolderImage.setImageDrawable(placeHolderDrawable.get());
            placeHolderImage.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(placeHolderUrl)) {
            placeHolderImage.loadImage(placeHolderUrl);
            placeHolderImage.setVisibility(View.VISIBLE);
        }

        loadingView = new ImageView(getContext());
        loadingView.setImageResource(com.jeremy.lychee.R.drawable.live_loading);

        RelativeLayout.LayoutParams loadingViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        loadingViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        loadingView.setLayoutParams(loadingViewLayoutParams);

        addView(loadingView);
        loadingView.setVisibility(GONE);

        setOnClickListener(v -> switchPanelVisible());
    }

    private void destroyUI() {
        if (loadingView != null && loadingView.getParent() != null) {
            hideLoadingAnim();
            ((ViewGroup) loadingView.getParent()).removeView(loadingView);
            loadingView = null;
        }
        if (placeHolderImage != null && placeHolderImage.getParent() != null) {
            ((ViewGroup) placeHolderImage.getParent()).removeView(placeHolderImage);
            placeHolderImage = null;
        }
        if (panelHideSubscription != null && !panelHideSubscription.isUnsubscribed()) {
            panelHideSubscription.unsubscribe();
        }
        clearControlPanels();
        clearDecorators();
        uiActionNotificationList.clear();
        setOnClickListener(null);
    }

    public void bindToVideoPlayer() {
        bindToVideoPlayer(true);
    }

    public void bindToVideoPlayer(boolean bindToActivityLifecycle) {
        if (viewHasBinding != null && viewHasBinding != this) {
            viewHasBinding.unBindToVideoPlayer();
        }

        initUI();

        VideoPlayer.getInstance().setRendererContainer(this, bindToActivityLifecycle);
        VideoPlayer.getInstance().getLiveCloudCallback().chainLiveCloudCallback(liveCloudCallbackWrapper);

        if (shouldShowLoadingAnim()) {
            placeHolderImage.setVisibility(View.VISIBLE);
            showLoadingAnim();
        }

        createDecorators();

        if (!isFullScreen()) {
            viewHasBinding = this;
            setVisibility(View.VISIBLE);

            Logger.d("VideoPlayerView, bindToVideoPlayer, this = " + this);
            bindingViewActivityListener = (activity, newState) -> {
                switch (newState) {
                    case ActivityState.DESTROYED:
                        if (viewHasBinding != null) {
                            Logger.d("VideoPlayerView, ActivityDestroyed， unBindVideoPlayer");
                            viewHasBinding.unBindToVideoPlayer();
                        } else {
                            Logger.d("VideoPlayerView, ActivityDestroyed, 没有Binding的View");
                        }
                        break;
                }
            };
            ApplicationStatus.registerStateListenerForActivity(bindingViewActivityListener, ((Activity) viewHasBinding.getContext()));
        }

        raiseUIActionNotification(UIActionNotification.OnBinded);
    }

    public void unBindToVideoPlayer() {
        if (viewHasBinding != this) {
            return;
        }

        VideoPlayer.getInstance().setRendererContainer(null);
        VideoPlayer.getInstance().getLiveCloudCallback().unchainLiveCloudCallback(liveCloudCallbackWrapper);

        destroyUI();

        if (!isFullScreen()) {
            Logger.d("VideoPlayerView, unBindToVideoPlayer, this = " + viewHasBinding);

            viewHasBinding.setVisibility(View.GONE);
            viewHasBinding = null;
            ApplicationStatus.unregisterActivityStateListener(bindingViewActivityListener);
            bindingViewActivityListener = null;
        }

        raiseUIActionNotification(UIActionNotification.OnUnBinded);
    }

    public static VideoPlayerView getBindingView() {
        return viewHasBinding;
    }

    public LiveCloudCallbackWrapper getLiveCloudCallback() {
        return liveCloudCallbackWrapper;
    }

    // - PlaceHolderImage相关
    public void setPlaceHolderDrawable(Drawable drawable) {
        setPlaceHolderDrawable(drawable, ImageView.ScaleType.CENTER_CROP);
    }

    public void setPlaceHolderDrawable(Drawable drawable, ImageView.ScaleType scaleType) {
        placeHolderDrawable = new WeakReference<>(drawable);
        if (placeHolderImage == null) {
            return;
        }

        placeHolderImage.setScaleType(scaleType);
        placeHolderImage.setImageDrawable(drawable);
        if (shouldShowLoadingAnim()) {
            placeHolderImage.setVisibility(View.VISIBLE);
        }
    }

    public void setPlaceHolderUrl(String url) {
        setPlaceHolderUrl(url, ImageView.ScaleType.CENTER_CROP);
    }

    public void setPlaceHolderUrl(String url, ImageView.ScaleType scaleType) {
        placeHolderUrl = url;
        if (placeHolderImage == null) {
            return;
        }

        placeHolderImage.loadImage(url);
        placeHolderImage.setScaleType(scaleType);
        if (shouldShowLoadingAnim()) {
            placeHolderImage.setVisibility(View.VISIBLE);
        }
    }

    // - LoadingAnimation相关
    public boolean shouldShowLoadingAnim() {
        return !VideoPlayer.getInstance().isPlaying() && !VideoPlayer.getInstance().isPaused();
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

        if (placeHolderImage != null) {
            placeHolderImage.setVisibility(View.GONE);
            placeHolderDrawable = null;
            placeHolderUrl = null;
        }
    }

    // - ControlPanel相关
    public void showControlPanel(boolean animate) {
        if (VideoPlayer.getInstance().getCurrentStream() == null || VideoPlayer.getInstance().getStreams() == null) {
            return;
        }

        lastPanelShownTime = System.currentTimeMillis();

        if (isPanelShown()) {
            return;
        }

        createControlPanels();

        raiseUIActionNotification(UIActionNotification.OnShowControlPanel);

        for (VideoPlayerPanel panel : panelViewMap.keySet()) {
            if ((isFullScreen() && !panel.isPanelSuitFor(VideoPlayerPanel.PanelType.PanelType_FullScreen)) ||
                    (!isFullScreen() && !panel.isPanelSuitFor(VideoPlayerPanel.PanelType.PanelType_Normal))) {
                continue;
            }

            panel.onShowPanel(isFullScreen());

            View panelView = panelViewMap.get(panel);
            if (panelView == null) {
                continue;
            }

            panelView.setVisibility(View.VISIBLE);

            if (animate) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(panelView, "alpha", 0.0f, 1.0f);
                animator.setDuration(500);
                animator.start();
            }
        }

        if (panelHideSubscription != null && !panelHideSubscription.isUnsubscribed()) {
            panelHideSubscription.unsubscribe();
        }
        panelHideSubscription = Observable.interval(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(((RxAppCompatActivity) getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                .skipWhile(it -> System.currentTimeMillis() - lastPanelShownTime < 3000 || !isShown())
                .first()
                .subscribe(it -> hideControlPanel(true), Throwable::printStackTrace);

        isPanelShown = true;
    }

    public void hideControlPanel(boolean animate) {
        if (!isPanelShown()) {
            return;
        }

        raiseUIActionNotification(UIActionNotification.OnHideControlPanel);
        for (VideoPlayerPanel panel : panelViewMap.keySet()) {
            panel.onHidePanel(isFullScreen());

            View panelView = panelViewMap.get(panel);
            if (panelView == null) {
                continue;
            }

            if (animate) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(panelView, "alpha", 1.0f, 0.0f);
                animator.setDuration(500);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        panelView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                animator.start();
            } else {
                panelView.setVisibility(View.GONE);
            }
        }

        isPanelShown = false;
    }

    public void delayHide() {
        lastPanelShownTime = System.currentTimeMillis();
    }

    public void switchPanelVisible() {
        if (isPanelShown()) {
            hideControlPanel(true);
        } else {
            showControlPanel(true);
        }
    }

    public boolean isPanelShown() {
        return isPanelShown;
    }

    public void addControlPanel(VideoPlayerPanel panel) {
        if (panelList.indexOf(panel) == -1) {
            panelList.add(panel);
            liveCloudCallbackWrapper.chainLiveCloudCallback(panel.getLiveCloudCallbackWrapper());
        }
    }

    public void addControlPanelView(VideoPlayerPanel panel, ViewGroup panelView) {
        addControlPanel(panel);
        panelViewMap.put(panel, panelView);
        panel.setVideoPlayerView(this);
    }

    private void clearControlPanels() {
        for (VideoPlayerPanel panel : panelViewMap.keySet()) {
            panel.unInit();

            View panelView = panelViewMap.get(panel);
            if (panelView == null) {
                continue;
            }

            ViewGroup panelParent = (ViewGroup) panelView.getParent();
            if (panelParent == null) {
                continue;
            }
            panelParent.removeView(panelView);
        }
        panelViewMap.clear();
        lastPanelShownTime = -1;
        isPanelShown = false;
    }

    private void createControlPanels() {
        for (VideoPlayerPanel panel : panelList) {
            if (panelViewMap.containsKey(panel)) {
                ViewGroup panelView = panelViewMap.get(panel);
                if (panelView != null && panelView.getParent() == null) {
                    addView(panelView);
                    panelView.setVisibility(View.GONE);
                }
                continue;
            }

            panel.setVideoPlayerView(this);
            panel.init();

            ViewGroup panelView = (ViewGroup) LayoutInflater.from(getContext()).inflate(panel.getLayoutId(), this, false);
            addView(panelView);
            panelViewMap.put(panel, panelView);
            panel.onViewCreated(panelView);
            panelView.setVisibility(View.GONE);
        }
    }

    private static void transferPanel(VideoPlayerView from, VideoPlayerView to) {
        if (from == null || to == null) {
            return;
        }

        for (VideoPlayerPanel panel : from.panelViewMap.keySet()) {
            ViewGroup panelView = from.panelViewMap.get(panel);
            if (panelView != null && panelView.getParent() != null) {
                ((ViewGroup) panelView.getParent()).removeView(panelView);
            }
            to.addControlPanelView(panel, panelView);
        }
        from.panelViewMap.clear();
        from.panelList.clear();
    }

    // - DecoratorView 相关
    public void addDecorator(VideoPlayerDecorator decorator) {
        if (decoratorList.indexOf(decorator) == -1) {
            decoratorList.add(decorator);
            liveCloudCallbackWrapper.chainLiveCloudCallback(decorator.getLiveCloudCallbackWrapper());
        }
    }

    public void addDecoratorView(VideoPlayerDecorator decorator, ViewGroup decoratorView) {
        addDecorator(decorator);
        decoratorViewMap.put(decorator, decoratorView);
        decorator.setVideoPlayerView(this);
    }

    private void clearDecorators() {
        for (VideoPlayerDecorator decorator : decoratorViewMap.keySet()) {
            decorator.unInit();

            View decoratorView = decoratorViewMap.get(decorator);
            if (decoratorView == null) {
                continue;
            }

            ViewGroup decoratorViewParent = (ViewGroup) decoratorView.getParent();
            if (decoratorViewParent == null) {
                continue;
            }
            decoratorViewParent.removeView(decoratorView);
        }
        decoratorViewMap.clear();
    }

    private void createDecorators() {
        for (VideoPlayerDecorator decorator : decoratorList) {
            if (decoratorViewMap.containsKey(decorator)) {
                ViewGroup decoratorView = decoratorViewMap.get(decorator);
                if (decoratorView != null && decoratorView.getParent() == null) {
                    addView(decoratorView);
                }
                continue;
            }

            decorator.setVideoPlayerView(this);
            decorator.init();

            ViewGroup decoratorView = (ViewGroup) LayoutInflater.from(getContext()).inflate(decorator.getLayoutId(), this, false);
            addView(decoratorView);
            decoratorViewMap.put(decorator, decoratorView);
            decorator.onViewCreated(decoratorView);
        }
    }

    private static void transferDecorator(VideoPlayerView from, VideoPlayerView to) {
        if (from == null || to == null) {
            return;
        }

        for (VideoPlayerDecorator decorator : from.decoratorViewMap.keySet()) {
            ViewGroup decoratorView = from.decoratorViewMap.get(decorator);
            if (decoratorView != null && decoratorView.getParent() != null) {
                ((ViewGroup) decoratorView.getParent()).removeView(decoratorView);
            }
            to.addDecoratorView(decorator, decoratorView);
        }
        from.decoratorViewMap.clear();
        from.decoratorList.clear();
    }

    private void raiseUIActionNotificationToDecorators(UIActionNotification notification) {
        for (VideoPlayerDecorator decorator : decoratorList) {
            if (decorator == null) {
                continue;
            }
            decorator.onUIActionNotification(notification, this);
        }
    }

    // - FullScreen 相关
    public static boolean isFullScreen() {
        return fullScreenView != null && fullScreenView.getParent() != null;
    }

    public static void enterFullScreen() {
        if (isFullScreen() || viewHasBinding == null) {
            return;
        }

        ((Activity) viewHasBinding.getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        viewHasBinding.hideControlPanel(false);

        Logger.d("videoPlayerView, 开始全屏");

        fullScreenView = new VideoPlayerView(viewHasBinding.getContext());
        transferPanel(viewHasBinding, fullScreenView);
        transferDecorator(viewHasBinding, fullScreenView);

        ViewGroup rootView = (ViewGroup) viewHasBinding.getRootView();
        if (rootView == null) {
            return;
        }

        rootView.addView(fullScreenView);

        Logger.d("VideoPlayerView, bind到全屏播放器");
        fullScreenView.bindToVideoPlayer();

        fullScreenView.raiseUIActionNotification(UIActionNotification.OnEnterFullScreen);

        fullScreenViewActivityListener = (activity, newState) -> {
            switch (newState) {
                case ActivityState.DESTROYED:
                    if (fullScreenView != null) {
                        Logger.d("VideoPlayerView, 全屏ActivityDestroyed， unBind全屏窗口");

                        ((ViewGroup) fullScreenView.getParent()).removeView(fullScreenView);
                        VideoPlayer.getInstance().getLiveCloudCallback().unchainLiveCloudCallback(fullScreenView.liveCloudCallbackWrapper);
                        fullScreenView.destroyUI();
                        fullScreenView = null;

                        ApplicationStatus.unregisterActivityStateListener(fullScreenViewActivityListener);
                        fullScreenViewActivityListener = null;
                    } else {
                        Logger.d("VideoPlayerView, 全屏ActivityDestroyed， 没有找到全屏播放器");
                    }
                    break;
            }
        };
        ApplicationStatus.registerStateListenerForActivity(fullScreenViewActivityListener, ((Activity) fullScreenView.getContext()));
    }

    public static void exitFullScreen() {
        if (!isFullScreen()) {
            return;
        }

        ((Activity) fullScreenView.getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (fullScreenView != null) {
            transferPanel(fullScreenView, viewHasBinding);
            transferDecorator(fullScreenView, viewHasBinding);

            if (fullScreenView.getParent() != null) {
                ((ViewGroup) fullScreenView.getParent()).removeView(fullScreenView);
            }

            VideoPlayer.getInstance().setRendererContainer(null);
            VideoPlayer.getInstance().getLiveCloudCallback().unchainLiveCloudCallback(fullScreenView.liveCloudCallbackWrapper);
            fullScreenView.destroyUI();
            fullScreenView = null;

            Logger.d("VideoPlayerView, 退出全屏");
            ApplicationStatus.unregisterActivityStateListener(fullScreenViewActivityListener);
            fullScreenViewActivityListener = null;
        }

        if (viewHasBinding != null) {
            viewHasBinding.bindToVideoPlayer();
            viewHasBinding.raiseUIActionNotification(UIActionNotification.OnExitFullScreen);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ((Activity) getContext()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ((Activity) getContext()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                break;
        }
    }

    // - UIAction回调相关
    public void registerUIActionNotification(Action2<UIActionNotification, VideoPlayerView> action) {
        boolean isRegistered = false;
        for (Action2<UIActionNotification, VideoPlayerView> actionInList : uiActionNotificationList) {
            if (AppUtil.actionEquals(action, actionInList)) {
                isRegistered = true;
                break;
            }
        }

        if (isRegistered) {
            return;
        }
        uiActionNotificationList.add(action);
    }

    public void unregisterUIActionNotification(Action2<UIActionNotification, VideoPlayerView> action) {
        for (Action2<UIActionNotification, VideoPlayerView> actionInList : uiActionNotificationList) {
            if (AppUtil.actionEquals(action, actionInList)) {
                uiActionNotificationList.remove(actionInList);
                break;
            }
        }
    }

    public void raiseUIActionNotification(UIActionNotification notification) {
        AndroidSchedulers.mainThread().createWorker().schedule(() -> {
            raiseUIActionNotificationToDecorators(notification);
            for (Action2<UIActionNotification, VideoPlayerView> action : uiActionNotificationList) {
                try {
                    action.call(notification, this);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // - 播放内核回调
    private void onPlayCoreBufferingUpdate(int buffering, int progress) {
        if (progress == 0) {
            showLoadingAnim();
        } else if (progress == 100) {
            hideLoadingAnim();
        }
    }
}
