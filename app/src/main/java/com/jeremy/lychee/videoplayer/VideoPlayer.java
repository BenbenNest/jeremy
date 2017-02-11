package com.jeremy.lychee.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.manager.live.LiveBrowseHistoryManager;
import com.jeremy.lychee.manager.live.LiveVideoListManager;
import com.jeremy.lychee.model.live.LiveChannelStreams;
import com.jeremy.lychee.base.ActivityState;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.live.LiveVideoInfo;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.qihoo.livecloud.ILiveCloudPlayer;
import com.qihoo.livecloud.LiveCloudPlayer;
import com.qihoo.livecloud.tools.LiveCloudConfig;
import com.qihoo.livecloud.tools.Stats;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class VideoPlayer {
    private final static VideoPlayer instance = new VideoPlayer();

    private VideoPlayer() {
        QEventBus.getEventBus().register(this);
    }

    @Override
    protected void finalize() throws Throwable {
        QEventBus.getEventBus().unregister(this);
        super.finalize();
    }

    public static VideoPlayer getInstance() {
        return instance;
    }

    @IntDef({ErrorCode.LIVE_CLOUD_SERVER_ERROR, ErrorCode.INVALID_DATA, ErrorCode.BUSINESS_SERVER_ERROR,
            ErrorCode.PLAYER_KERNEL_ERROR, ErrorCode.NO_NETWORK_AVAILABLE, ErrorCode.MOBILE_NETWORK_WITHOUT_PERMISSION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {
        int LIVE_CLOUD_SERVER_ERROR = 1;
        int INVALID_DATA = 2;
        int BUSINESS_SERVER_ERROR = 3;
        int PLAYER_KERNEL_ERROR = 4;
        int NO_NETWORK_AVAILABLE = 5;
        int MOBILE_NETWORK_WITHOUT_PERMISSION = 6;
    }

    private ILiveCloudPlayer playerCore;
    private VideoRenderer videoRenderer;
    private ImageView pausePlaceHolder;
    private static Drawable pausePlaceHolderDrawable;

    private LiveCloudCallbackWrapper liveCloudCallbackWrapperOuter = LiveCloudCallbackWrapper.createInstance();
    private LiveCloudCallbackWrapper liveCloudCallbackWrapper = LiveCloudCallbackWrapper.createInstance()
            .addOnPreparedAction(this::onPlayCorePrepared, Schedulers.immediate())
            .addOnPreparedAction(liveCloudCallbackWrapperOuter::onPrepared, Schedulers.immediate())
            .addOnInfoAction(this::onPlayCoreInfo)
            .addOnInfoAction(liveCloudCallbackWrapperOuter::onInfo, Schedulers.immediate())
            .addOnErrorAction(this::onPlayCoreError)
            .addOnErrorAction(liveCloudCallbackWrapperOuter::onError, Schedulers.immediate())
            .addOnCompletionAction(this::onPlayerCoreCompletion)
            .addOnBufferingUpdateAction(this::onPlayCoreBufferingUpdate, AndroidSchedulers.mainThread())
            .addOnBufferingUpdateAction(liveCloudCallbackWrapperOuter::onBufferingUpdate, Schedulers.immediate())
            .addOnProgressChangeAction(liveCloudCallbackWrapperOuter::onProgressChange, Schedulers.immediate())
            .addOnSeekCompleteAction(liveCloudCallbackWrapperOuter::onSeekComplete, Schedulers.immediate())
            .addOnSizeChangedAction(liveCloudCallbackWrapperOuter::onSizeChanged, Schedulers.immediate());


    private LiveChannelStreams liveChannelStreams;
    private LiveChannelStreams.VideoStreamEntity currentStream;
    private LiveChannelStreams.VideoStreamEntity streamToPlay;
    private List<LiveVideoInfo> videoInfoList;
    private int idxToPlay = 0;
    private int currentIdx = 0;
    private int positionToResume = -1;
    private boolean kernelStopping = false;
    private boolean userStopped = false;
    private boolean pausing = false;
    private boolean isPaused = false;
    private boolean isPlayingWhenActivityPaused = false;
    private boolean isMuteBeforeFakePause = false;
    private boolean isCompletion = false;
    private boolean isLoading = false;

    private List<Action1<Integer>> onErrorActionList = new ArrayList<>();
    private Map<Context, ApplicationStatus.ActivityStateListener> activityStateListenerMap = new HashMap<>();

    //save for hitlog
    private List<String[]> mHitLogCache = new ArrayList<>();
    public List<String[]> getHitLogCache() {
        return mHitLogCache;
    }

    public void setRendererContainer(ViewGroup view) {
        setRendererContainer(view, true);
    }

    public void setRendererContainer(ViewGroup view, boolean bindToActivityLifecycle) {
        detachPlayerView();

        if (view == null) {
            return;
        }

        videoRenderer = new VideoRenderer(view.getContext(), null);
        videoRenderer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        view.addView(videoRenderer);
        videoRenderer.setPaused(false);

        pausePlaceHolder = new ImageView(view.getContext(), null);
        pausePlaceHolder.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        view.addView(pausePlaceHolder);
        if (pausePlaceHolderDrawable != null) {
            pausePlaceHolder.setImageDrawable(pausePlaceHolderDrawable);
        }

        if (!isPlaying() && !isPaused()) {
            videoRenderer.setVisibility(View.GONE);
        } else {
            videoRenderer.setVisibility(View.VISIBLE);
        }

        if (isPaused() && playerCore != null && !playerCore.isInitialized()) {
            pausePlaceHolder.setVisibility(View.VISIBLE);
        } else {
            pausePlaceHolder.setVisibility(View.GONE);
        }

        if (bindToActivityLifecycle && !activityStateListenerMap.containsKey(view.getContext())) {
            ApplicationStatus.ActivityStateListener activityStateListener = (activity, newState) -> {
                switch (newState) {
                    case ActivityState.DESTROYED:
                        onActivityDestroyed(activity);
                        break;
                    case ActivityState.PAUSED:
                        onActivityPaused(activity);
                        break;
                    case ActivityState.RESUMED:
                        onActivityResumed(activity);
                        break;
                }
            };
            ApplicationStatus.registerStateListenerForActivity(activityStateListener, (Activity) view.getContext());
            activityStateListenerMap.put(view.getContext(), activityStateListener);
        }

        ((Activity) view.getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    protected void detachPlayerView() {
        if (videoRenderer == null) {
            return;
        }

        videoRenderer.setPaused(true);
        ViewGroup parent = (ViewGroup) videoRenderer.getParent();
        if (parent != null) {
            ((Activity) parent.getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            parent.removeView(videoRenderer);
        }
        videoRenderer = null;

        parent = (ViewGroup) pausePlaceHolder.getParent();
        if (parent != null) {
            parent.removeView(pausePlaceHolder);
        }
        pausePlaceHolder = null;
    }

    private Activity getAttachedActivity() {
        if (videoRenderer == null) {
            return null;
        }

        return (Activity) videoRenderer.getContext();
    }

    protected void onActivityDestroyed(Activity activity) {
        stop();
        detachPlayerView();
        currentStream = null;

        if (activityStateListenerMap.containsKey(activity)) {
            ApplicationStatus.unregisterActivityStateListener(activityStateListenerMap.get(activity));
            activityStateListenerMap.remove(activity);
        }
    }

    protected void onActivityPaused(Activity activity) {
        isPlayingWhenActivityPaused = !isPaused();
        if (!isPaused()) {
            pause();
        }
    }

    protected void onActivityResumed(Activity activity) {
        if (isPaused() && isPlayingWhenActivityPaused) {
            start();
        }
    }

    public void onBackPressed(Activity activity) {
        if (videoRenderer == null || getAttachedActivity() != activity) {
            return;
        }

        setMute(true);
    }

    public void onEventMainThread(SlidingActivity.SlideOut event) {
        if (event == null || !activityStateListenerMap.containsKey(getAttachedActivity())) {
            return;
        }

        onBackPressed(event.activity);
    }

    public void load(Activity activity, String id, String topicId, String tag) {
        AppUtil.show3GTrafficAlarm(ApplicationStatus.getLastTrackedFocusedActivity(),
                ApplicationStatus.getApplicationContext().getString(com.jeremy.lychee.R.string.dialog_use_mobile_net_play), () -> {
                    hidePausePlaceHolder();

                    isLoading = true;
                    isCompletion = false;
                    userStopped = false;

                    LiveVideoListManager.createInstance().getLiveVideoList(topicId, id, tag, false)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .compose(((RxAppCompatActivity) activity).bindToLifecycle())
                            .filter(it -> !userStopped)
                            .map(it -> it.second)
                            .doOnNext(it -> {
                                if (it == null || it.size() == 0) {
                                    throw new RuntimeException(VideoPlayer.textMessageForErrorCode(VideoPlayer.ErrorCode.INVALID_DATA));
                                }
                            })
                            .subscribe(it -> {
                                videoInfoList = it.get(0).getStream_type() == LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_NEWS_REPLAY ? it : null;
                                idxToPlay = 0;
                                currentIdx = idxToPlay;
                                loadStream(activity, it.get(idxToPlay++));
                            }, e -> {
                                if (e instanceof RuntimeException) {
                                    raiseErrorNotify(VideoPlayer.ErrorCode.INVALID_DATA);
                                } else {
                                    raiseErrorNotify(VideoPlayer.ErrorCode.BUSINESS_SERVER_ERROR);
                                }
                            });
                }, () -> raiseErrorNotify(ErrorCode.MOBILE_NETWORK_WITHOUT_PERMISSION));
    }

    public void loadStream(Activity activity, LiveVideoInfo videoInfo) {
        AppUtil.show3GTrafficAlarm(ApplicationStatus.getLastTrackedFocusedActivity(),
                ApplicationStatus.getApplicationContext().getString(com.jeremy.lychee.R.string.dialog_use_mobile_net_play), () -> {
                    hidePausePlaceHolder();

                    isLoading = true;
                    isCompletion = false;
                    userStopped = false;

                    LiveBrowseHistoryManager.getInstance().addBrowseItem(videoInfo);
                    OldRetroAdapter.getService().getLiveChannelStreams(videoInfo.getVideo_play_url()).map(ModelBase::getData).subscribeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .compose(((RxAppCompatActivity) activity).bindToLifecycle())
                            .filter(it -> !userStopped)
                            .doOnNext(it -> {
                                if (it == null || it.getVideo_stream().size() == 0) {
                                    throw new RuntimeException(VideoPlayer.textMessageForErrorCode(VideoPlayer.ErrorCode.INVALID_DATA));
                                }
                            })
                            .subscribe(this::load, e -> {
                                if (e instanceof RuntimeException) {
                                    raiseErrorNotify(VideoPlayer.ErrorCode.INVALID_DATA);
                                } else {
                                    raiseErrorNotify(VideoPlayer.ErrorCode.BUSINESS_SERVER_ERROR);
                                }
                            });
                }, () -> raiseErrorNotify(ErrorCode.MOBILE_NETWORK_WITHOUT_PERMISSION));
    }

    public void load(LiveChannelStreams streams) {
        load(streams, -1);
    }

    public void load(LiveChannelStreams streams, int position) {
        hidePausePlaceHolder();

        if (streams == null || streams.getVideo_stream() == null || streams.getVideo_stream().size() == 0) {
            raiseErrorNotify(ErrorCode.INVALID_DATA);
            return;
        }

        AppUtil.show3GTrafficAlarm(ApplicationStatus.getLastTrackedFocusedActivity(),
                ApplicationStatus.getApplicationContext().getString(com.jeremy.lychee.R.string.dialog_use_mobile_net_play), () -> {
                    liveChannelStreams = streams;
                    switchStream(getDefaultStream(liveChannelStreams), position);
                }, () -> {
                    isLoading = false;
                    raiseErrorNotify(ErrorCode.MOBILE_NETWORK_WITHOUT_PERMISSION);
                });
    }

    private void hidePausePlaceHolder() {
        pausePlaceHolderDrawable = null;

        if (pausePlaceHolder != null) {
            pausePlaceHolder.setVisibility(View.GONE);
        }
    }

    public void switchStream(LiveChannelStreams.VideoStreamEntity streamEntity, int positionToResume) {
        if (streamEntity == null || TextUtils.isEmpty(streamEntity.getStream_url())) {
            raiseErrorNotify(ErrorCode.INVALID_DATA);
            return;
        }

        isLoading = true;
        isCompletion = false;
        userStopped = false;

        this.positionToResume = positionToResume;

        if (playerCore != null && playerCore.isInitialized()) {
            streamToPlay = streamEntity;
            stop();
            userStopped = false;
        } else {
            loadStreamInternal(streamEntity);
        }
    }

    private void loadStreamInternal(LiveChannelStreams.VideoStreamEntity streamEntity) {
        kernelStopping = false;

        if (userStopped) {
            userStopped = false;
            return;
        }

        @LiveCloudConfigManager.STREAM_TYPE int streamType = streamEntity.getStream_type();
        LiveCloudConfig liveCloudConfig = LiveCloudConfigManager.getInstance().getConfig(streamType);
        liveCloudConfig.setToken(liveChannelStreams.getUtoken());
        liveCloudConfig.setTs(liveChannelStreams.getTs());
        liveCloudConfig.setRid(streamEntity.getStream_url());
        Stats.userStart(liveCloudConfig.getSid(), liveCloudConfig.getUid(), liveCloudConfig.getBid(),
                liveCloudConfig.getCid(), liveCloudConfig.getVer(), liveCloudConfig.getOs(),
                liveCloudConfig.getNet(), liveCloudConfig.getMid(), liveCloudConfig.getRid());
        playerCore = new LiveCloudPlayer(ApplicationStatus.getApplicationContext());
        playerCore.setConfig(liveCloudConfig);
        playerCore.setDataSource(streamEntity.getStream_url(), LiveCloudConfigManager.getInstance().getStreamPlayType(streamType));
        playerCore.setOnLiveCloudCallback(liveCloudCallbackWrapper);

        hidePausePlaceHolder();

        Schedulers.computation().createWorker().schedule(() -> {
            if (playerCore != null) {
                playerCore.prepareAsync();
            }

            currentStream = streamEntity;
            streamToPlay = null;
        });

//        //打点3002
//        mHitLogCache.clear();
    }

    private LiveChannelStreams.VideoStreamEntity getDefaultStream(LiveChannelStreams liveChannelStreams) {
        if (liveChannelStreams == null || liveChannelStreams.getVideo_stream() == null || liveChannelStreams.getVideo_stream().size() == 0) {
            return null;
        }

        try {
            Collections.sort(liveChannelStreams.getVideo_stream(), (lhs, rhs) ->
                    new Integer(Integer.parseInt(lhs.getStream_rate())).compareTo(Integer.parseInt(rhs.getStream_rate())));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return liveChannelStreams.getVideo_stream().get(0);
    }

    public LiveChannelStreams.VideoStreamEntity getCurrentStream() {
        return currentStream;
    }

    public LiveChannelStreams getStreams() {
        return liveChannelStreams;
    }

    public void start() {
        if (playerCore != null && playerCore.isInitialized()) {
            if (isPaused()) {
                AndroidSchedulers.mainThread().createWorker().schedule(() -> {
                    if (videoRenderer != null) {
                        videoRenderer.setVisibility(View.VISIBLE);
                    }
                });
            }

            if (!isPlaying()) {
                playerCore.start(0);
            }

            isPaused = false;

//            //打点3002
//            mHitLogCache.add(
//                    new String[]{Integer.toString(playerCore.getCurrentPosition()), ""});
        } else if (isPaused() && currentStream != null) {
            loadStreamInternal(currentStream);
        }
    }

    public boolean isPlaying() {
        return playerCore != null && playerCore.isInitialized() && playerCore.isPlaying() && !isCompletion;
    }

    public void pause() {
        if (playerCore != null && playerCore.isInitialized()) {
            isPaused = true;
            if (currentStream != null) {
                @LiveCloudConfigManager.STREAM_TYPE int streamType = currentStream.getStream_type();

                if (streamType == LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_NEWS_LIVE ||
                        streamType == LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_BTV_LIVE ||
                        streamType == LiveCloudConfigManager.STREAM_TYPE.STREAM_TYPE_HUAJIAO_LIVE) {
                    if (videoRenderer != null) {
                        pausePlaceHolderDrawable = new BitmapDrawable(videoRenderer.getBitmap());
                        videoRenderer.setVisibility(View.GONE);
                    }

                    if (pausePlaceHolder != null) {
                        pausePlaceHolder.setImageDrawable(pausePlaceHolderDrawable);
                        pausePlaceHolder.setVisibility(View.VISIBLE);
                    }

                    isMuteBeforeFakePause = playerCore.isMute();
                    pausing = true;
                    Stats.userStop(LiveCloudConfigManager.generateSID());
                    playerCore.stop();
//                    //打点3002
//                    if (mHitLogCache != null && mHitLogCache.size() > 0) {
//                        mHitLogCache.get(mHitLogCache.size() - 1)[1]
//                                = Integer.toString(playerCore.getCurrentPosition());
//                    }

                } else {
                    playerCore.pause();
                }
            } else {
                playerCore.pause();
            }
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setMute(boolean mute) {
        if (playerCore != null && playerCore.isInitialized()) {
            playerCore.setMute(mute);
        }
    }

    public boolean isMute() {
        return playerCore == null || (playerCore.isMute() && !isPaused());
    }

    public void seekTo(int position) {
        if (playerCore != null && playerCore.isInitialized()) {
//            mHitLogCache.get(mHitLogCache.size() - 1)[1]
//                    = Integer.toString(playerCore.getCurrentPosition());
            playerCore.seekTo(position);
            isLoading = true;
//            mHitLogCache.add(
//                    new String[]{Integer.toString(position), ""});
        }
    }

    public int getPosition() {
        if (playerCore != null && playerCore.isInitialized()) {
            return playerCore.getCurrentPosition();
        }

        return -1;
    }

    public int getDuration() {
        if (playerCore != null && playerCore.isInitialized()) {
            return playerCore.getDuration();
        }

        return -1;
    }

    public void stop() {
        userStopped = true;

        if (playerCore == null || !playerCore.isInitialized()) {
            return;
        }

//        //打点3002
//        if (mHitLogCache != null && mHitLogCache.size() > 0) {
//            mHitLogCache.get(mHitLogCache.size() - 1)[1]
//                    = Integer.toString(playerCore.getCurrentPosition());
//            if(videoInfoList != null && currentIdx < videoInfoList.size()){
//                HitLog.hitLogVideoPlay(currentStream.getStream_vbt(),
//                        videoInfoList.get(currentIdx).getId(), mHitLogCache);
//            }
//        }

        playerCore.setMute(true);
        Stats.userStop(LiveCloudConfigManager.generateSID());
        playerCore.stop();
        playerCore.release();
        pausePlaceHolderDrawable = null;
        isPaused = false;
        isLoading = false;

        if (videoRenderer != null) {
            videoRenderer.setVisibility(View.GONE);
        }

        kernelStopping = true;
    }

    public Bitmap capture() {
        if (videoRenderer == null) {
            return null;
        }

        return videoRenderer.getBitmap();
    }

    public boolean isLoading() {
        return isLoading;
    }

    private void onPlayCorePrepared() {
        if (playerCore == null) {
            return;
        }

        if (positionToResume == -1) {
            playerCore.start(0);
        } else {
            playerCore.start(positionToResume);
            positionToResume = -1;
        }
    }

    private void onPlayCoreInfo(int what, long extra) {
        if (what == ILiveCloudPlayer.Info.JPLAYER_SESSION_CLOSED) {
            if (kernelStopping) {
                playerCore = null;
                if (pausing) {
                    pausing = false;
                    isPaused = true;
                }
                isPlayingWhenActivityPaused = false;

                if (streamToPlay != null) {
                    loadStreamInternal(streamToPlay);
                }
                kernelStopping = false;
            }
        } else if (what == ILiveCloudPlayer.Info.LIVE_PLAY_START) {
            if (videoRenderer != null) {
                AndroidSchedulers.mainThread().createWorker().schedule(() -> videoRenderer.setVisibility(View.VISIBLE));
            }

            if (isPaused && isMuteBeforeFakePause) {
                playerCore.setMute(true);
                isMuteBeforeFakePause = false;
            }
            isLoading = false;
            isPaused = false;
            isCompletion = false;
        }
    }

    private void onPlayCoreError(int what, long extra) {
        if (playerCore == null) {
            return;
        }

        switch ((int) extra) {
            case ILiveCloudPlayer.Extra.ERROR_PLAY_OPEN_URL_FAILED:
            case ILiveCloudPlayer.Extra.ERROR_PLAY_CONNECT_FAILED:
            case ILiveCloudPlayer.Extra.ERROR_PLAY_JPLAYER_OPEN_FAILED:
            case ILiveCloudPlayer.Extra.ERROR_PLAY_SN_FAILED:
            case ILiveCloudPlayer.Extra.ERROR_PLAY_SUBSCRIBE_FAILED:
                raiseErrorNotify(ErrorCode.LIVE_CLOUD_SERVER_ERROR);
                break;
            default:
                raiseErrorNotify(ErrorCode.PLAYER_KERNEL_ERROR);
        }
    }

    private void onPlayerCoreCompletion() {
        isCompletion = true;
        isLoading = false;
        if (videoInfoList == null || idxToPlay >= videoInfoList.size()) {
            liveCloudCallbackWrapperOuter.onCompletion();
            return;
        }

        LiveVideoInfo videoInfo = videoInfoList.get(idxToPlay++);
        if (getAttachedActivity() != null) {
            isCompletion = false;
            loadStream(getAttachedActivity(), videoInfo);
        }
    }

    private void onPlayCoreBufferingUpdate(int buffering, int progress) {
        if (progress == 0 && isPlaying()) {
            isLoading = true;
        } else if (progress == 100 && isPlaying()) {
            isLoading = false;
        }
    }

    public void registerErrorHandler(Action1<Integer> action) {
        boolean isRegistered = false;
        for (Action1<Integer> actionInList : onErrorActionList) {
            if (AppUtil.actionEquals(action, actionInList)) {
                isRegistered = true;
                break;
            }
        }

        if (isRegistered) {
            return;
        }
        onErrorActionList.add(action);
    }

    public void unregisterErrorHandler(Action1<Integer> action) {
        for (Action1<Integer> actionInList : onErrorActionList) {
            if (AppUtil.actionEquals(action, actionInList)) {
                onErrorActionList.remove(actionInList);
                break;
            }
        }
    }

    public void raiseErrorNotify(@ErrorCode int errorCode) {
        AndroidSchedulers.mainThread().createWorker().schedule(() -> {
            for (Action1<Integer> action : onErrorActionList) {
                try {
                    action.call(errorCode);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LiveCloudCallbackWrapper getLiveCloudCallback() {
        return liveCloudCallbackWrapperOuter;
    }

    static public String textMessageForErrorCode(@ErrorCode int code) {
        int errorCode = code;
        try {
            boolean isNetworkAvailable = AppUtil.isNetAvailable(ApplicationStatus.getApplicationContext());
            if (!isNetworkAvailable) {
                errorCode = ErrorCode.NO_NETWORK_AVAILABLE;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        switch (errorCode) {
            case ErrorCode.BUSINESS_SERVER_ERROR:
                return ApplicationStatus.getApplicationContext().getResources().getString(com.jeremy.lychee.R.string.live_business_server_error);
            case ErrorCode.INVALID_DATA:
                return ApplicationStatus.getApplicationContext().getResources().getString(com.jeremy.lychee.R.string.live_invalid_data_error);
            case ErrorCode.LIVE_CLOUD_SERVER_ERROR:
                return ApplicationStatus.getApplicationContext().getResources().getString(com.jeremy.lychee.R.string.live_cloud_server_error);
            case ErrorCode.PLAYER_KERNEL_ERROR:
                return ApplicationStatus.getApplicationContext().getResources().getString(com.jeremy.lychee.R.string.live_player_kernel_error);
            case ErrorCode.NO_NETWORK_AVAILABLE:
                return ApplicationStatus.getApplicationContext().getResources().getString(com.jeremy.lychee.R.string.live_no_network_available_error);
            case ErrorCode.MOBILE_NETWORK_WITHOUT_PERMISSION:
                return ApplicationStatus.getApplicationContext().getResources().getString(com.jeremy.lychee.R.string.live_mobile_network_without_permission);
            default:
                return ApplicationStatus.getApplicationContext().getResources().getString(com.jeremy.lychee.R.string.live_unknown_error);
        }
    }
}
