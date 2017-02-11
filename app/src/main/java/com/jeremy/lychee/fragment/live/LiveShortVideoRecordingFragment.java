package com.jeremy.lychee.fragment.live;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.videoplayer.LiveCloudConfigManager;
import com.jeremy.lychee.manager.live.CameraManager;
import com.jeremy.lychee.manager.live.LiveRecordingManager;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.qihoo.livecloud.LiveCloudRecorder;
import com.qihoo.livecloud.recorder.callback.RecorderCallBack;
import com.qihoo.livecloud.recorder.setting.PublishSettings;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class LiveShortVideoRecordingFragment extends BaseFragment {
    @Bind(com.jeremy.lychee.R.id.recording_progress)
    ProgressBar recordingProgress;
    @Bind(com.jeremy.lychee.R.id.btn_record)
    ImageView recordBtn;
    @Bind(com.jeremy.lychee.R.id.btn_delete)
    ImageView deleteBtn;
    @Bind(com.jeremy.lychee.R.id.btn_ok)
    ImageView okBtn;
    @Bind(com.jeremy.lychee.R.id.switch_camera)
    ImageView cameraSwitchBtn;
    @Bind(com.jeremy.lychee.R.id.preview_view)
    SurfaceView previewView;
    @Bind(com.jeremy.lychee.R.id.thumbnail_img)
    ImageView thumbnailImgView;
    @Bind(com.jeremy.lychee.R.id.video_view)
    VideoView videoView;
    @Bind(com.jeremy.lychee.R.id.video_area)
    FrameLayout videoArea;
    @Bind(com.jeremy.lychee.R.id.timerText)
    TextView timerTextView;

    static private final int MAX_RECORD_DURATION = 60 * 1000;
    static private final int PROGRESS_UPDATE_INTERVAL = 100;
    static private final int BUTTON_SPLASH_FACTOR = 8;

    private CameraManager cameraManager = CameraManager.createInstance();

    private Subscription progressSubscription;
    private LiveRecordingManager liveRecordingManager = LiveRecordingManager.createInstance();
    private LiveCloudRecorder mCloudRecorder;
    private String videoPath;
    private AtomicReference<String> thumbnailFile = new AtomicReference<>();
    private int videoLength;

    private boolean isWritingFile;

    Subject<Void, Void> keyDownFilter = new SerializedSubject<>(PublishSubject.create());

    private static class Event_OnSnCallback {
        public int sessionId;
        public String sn;

        public Event_OnSnCallback(int sessionId, String sn) {
            this.sessionId = sessionId;
            this.sn = sn;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.jeremy.lychee.R.layout.fragment_live_short_video_recording, container, false);
        ButterKnife.bind(this, rootView);

        cameraManager.init();
        initFSM();
        initUI();
        initPreview();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        closeRecorder();
        cameraManager.unInit();
    }

    private void initFSM() {
        liveRecordingManager.registerAction(LiveRecordingManager.RECORDING_STATE.PREPARE,
                LiveRecordingManager.RECORDING_STATE.RECORDING, this::startRecord);
        liveRecordingManager.registerAction(LiveRecordingManager.RECORDING_STATE.RECORDING,
                LiveRecordingManager.RECORDING_STATE.AFTER_RECORD, this::finishRecord);
        liveRecordingManager.registerAction(LiveRecordingManager.RECORDING_STATE.RECORDING,
                LiveRecordingManager.RECORDING_STATE.PREPARE, this::tooShortToRecord);
        liveRecordingManager.registerAction(LiveRecordingManager.RECORDING_STATE.AFTER_RECORD,
                LiveRecordingManager.RECORDING_STATE.PUBLISH_PAGE, this::gotoPublishPage);
        liveRecordingManager.registerAction(LiveRecordingManager.RECORDING_STATE.AFTER_RECORD,
                LiveRecordingManager.RECORDING_STATE.PREPARE, this::reFlow);
    }

    private void initUI() {
        updateUIState();

        keyDownFilter
                .throttleFirst(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(it -> {
                    if (liveRecordingManager.getCurrentState() == LiveRecordingManager.RECORDING_STATE.PREPARE) {
                        liveRecordingManager.doNext(LiveRecordingManager.RECORDING_STATE.RECORDING);
                    }
                });

        recordBtn.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    keyDownFilter.onNext(null);
                    break;
                case MotionEvent.ACTION_UP:
                    switch (liveRecordingManager.getCurrentState()) {
                        case LiveRecordingManager.RECORDING_STATE.RECORDING:
                            if (videoLength < 2000 || thumbnailFile == null || thumbnailFile.get() == null) {
                                Toast.makeText(getContext(), "视频录制时间太短，请重新录制", Toast.LENGTH_SHORT).show();
                                liveRecordingManager.doNext(LiveRecordingManager.RECORDING_STATE.PREPARE);
                            } else {
                                liveRecordingManager.doNext(LiveRecordingManager.RECORDING_STATE.AFTER_RECORD);
                            }
                            break;
                        case LiveRecordingManager.RECORDING_STATE.AFTER_RECORD:
                            if (isWritingFile) {
                                break;
                            }

                            if (videoView.isPlaying()) {
                                stopPlay();
                            } else {
                                startPlay();
                            }
                            break;
                    }
                    break;
            }
            return true;
        });
    }

    private void initPreview() {
        previewView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                CameraManager.CameraWrap camera = cameraManager.open(cameraManager.getBackCameraId());
                if (camera != null) {
                    camera.startPreview(getActivity(), holder);
                }
                QEventBus.getEventBus().registerSticky(LiveShortVideoRecordingFragment.this);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (cameraManager.getCurrentCamera() != null) {
                    cameraManager.getCurrentCamera().close();
                }
                QEventBus.getEventBus().unregister(LiveShortVideoRecordingFragment.this);
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (liveRecordingManager.getCurrentState() != LiveRecordingManager.RECORDING_STATE.PREPARE) {
            DialogUtil.showConfirmDialog(getActivity(), "是否终止短视频录制？",
                    getString(com.jeremy.lychee.R.string.dialog_button_confirm), dialog -> {
                        reset();
                        getActivity().finish();
                        dialog.dismiss();
                    }, getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss);
            return true;
        }
        return super.onBackPressed();
    }

    @OnClick(com.jeremy.lychee.R.id.close)
    void onCloseClicked() {
        if (!onBackPressed()) {
            getActivity().finish();
        }
    }

    @OnClick(com.jeremy.lychee.R.id.switch_camera)
    void onSwitchCamera() {
        cameraManager.toggleCamera();
    }

    @OnClick(com.jeremy.lychee.R.id.btn_ok)
    void onPublish() {
        liveRecordingManager.doNext(LiveRecordingManager.RECORDING_STATE.PUBLISH_PAGE);
    }

    @OnClick(com.jeremy.lychee.R.id.btn_delete)
    void onDelete() {
        DialogUtil.showConfirmDialog(getActivity(), "是否放弃之前录制的视频内容并重新开始录制？", "确认", dialog -> {
            liveRecordingManager.doNext(LiveRecordingManager.RECORDING_STATE.PREPARE);
            dialog.dismiss();
        }, "取消", DialogInterface::dismiss);
    }

    private void updateUIState() {
        @LiveRecordingManager.RECORDING_STATE int currentState = liveRecordingManager.getCurrentState();
        switch (currentState) {
            case LiveRecordingManager.RECORDING_STATE.PREPARE:
                recordBtn.setImageResource(com.jeremy.lychee.R.drawable.living_dsp_default);
                recordBtn.setClickable(true);
                recordingProgress.setVisibility(View.GONE);
                recordingProgress.setMax(MAX_RECORD_DURATION / PROGRESS_UPDATE_INTERVAL - 1);
                recordingProgress.setProgress(0);
                deleteBtn.setVisibility(View.GONE);
                okBtn.setVisibility(View.GONE);
                cameraSwitchBtn.setVisibility(View.VISIBLE);
                previewView.setVisibility(View.VISIBLE);
                thumbnailImgView.setVisibility(View.GONE);
                videoArea.setVisibility(View.GONE);
                timerTextView.setVisibility(View.GONE);
                break;
            case LiveRecordingManager.RECORDING_STATE.RECORDING:
                recordBtn.setImageResource(com.jeremy.lychee.R.drawable.living_dsp_press_down);
                recordBtn.setClickable(true);
                recordingProgress.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.GONE);
                okBtn.setVisibility(View.GONE);
                cameraSwitchBtn.setVisibility(View.GONE);
                videoArea.setVisibility(View.GONE);
                previewView.setVisibility(View.VISIBLE);
                thumbnailImgView.setVisibility(View.GONE);
                timerTextView.setVisibility(View.VISIBLE);
                break;
            case LiveRecordingManager.RECORDING_STATE.AFTER_RECORD:
                recordBtn.setImageResource(com.jeremy.lychee.R.drawable.living_dsp_play);
                recordBtn.setClickable(false);
                recordingProgress.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.VISIBLE);
                okBtn.setVisibility(View.VISIBLE);
                cameraSwitchBtn.setVisibility(View.GONE);
                videoArea.setVisibility(View.GONE);
                previewView.setVisibility(View.GONE);
                thumbnailImgView.setVisibility(View.VISIBLE);
                timerTextView.setVisibility(View.GONE);
                break;
        }
    }

    private boolean startRecord(@LiveRecordingManager.RECORDING_STATE int from,
                                @LiveRecordingManager.RECORDING_STATE int to) {

        if (cameraManager.getCurrentCamera() == null) {
            cameraManager.open(cameraManager.getBackCameraId());
            if (cameraManager.getCurrentCamera() == null) {
                Toast.makeText(getContext(), "请开启摄像头访问权限后重试", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (previewView != null) {
                    cameraManager.getCurrentCamera().startPreview(getActivity(), previewView.getHolder());
                }
            }
        }

        updateUIState();
        reset();

        mCloudRecorder = LiveCloudRecorder.staticCreate();
        mCloudRecorder.setConfig(LiveCloudConfigManager.getInstance().getConfig());
        PublishSettings publishSettings = LiveCloudConfigManager.getInstance().getPublishSettings();
        publishSettings.setOnlyToFile(1);
        try {
            File tempFile = File.createTempFile("Record", ".mp4");
            videoPath = tempFile.getPath();
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        publishSettings.setMp4FileName(videoPath);
        mCloudRecorder.createSession(publishSettings);
        mCloudRecorder.setCloudPreviewCallback((bytes, width, height) -> Schedulers.io().createWorker().schedule(() -> {
            if ((thumbnailFile != null && thumbnailFile.get() != null) || cameraManager.getCurrentCamera() == null) {
                return;
            }

            try {
                thumbnailFile.set(File.createTempFile("Thumbnail", ".jpg").getPath());
                cameraManager.getCurrentCamera().saveThumbnail(bytes, width, height, thumbnailFile.get());
                mCloudRecorder.setCloudPreviewCallback(null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }));
        mCloudRecorder.setCallBack(new RecorderCallBack() {
            @Override
            public void snCallback(int sessionId, String sn) {
                QEventBus.getEventBus().postSticky(new Event_OnSnCallback(sessionId, sn));
            }

            @Override
            public void recorderState(int sessionId, int pubEvent, int param) {
                switch (pubEvent) {
                    case ERROR_CODE.TERROR_INPUT_AUDIO:
                    {
                        AndroidSchedulers.mainThread().createWorker().schedule(() -> {
                            Toast.makeText(getContext(), "请开启录音权限后重试", Toast.LENGTH_SHORT).show();
                            liveRecordingManager.doNext(LiveRecordingManager.RECORDING_STATE.PREPARE);
                        });
                        break;
                    }
                }
            }
        });
        mCloudRecorder.prepare();
        return true;
    }

    private boolean finishRecord(@LiveRecordingManager.RECORDING_STATE int from,
                                 @LiveRecordingManager.RECORDING_STATE int to) {
        finishCounter();
        closeRecorder();

        isWritingFile = true;
        waitForSaveToSD(() -> {
            updateUIState();
            loadThumbnail();
            isWritingFile = false;
        });

        return true;
    }

    private boolean tooShortToRecord(@LiveRecordingManager.RECORDING_STATE int from,
                                     @LiveRecordingManager.RECORDING_STATE int to) {
        finishCounter();
        reset();
        updateUIState();

        return true;
    }

    private boolean gotoPublishPage(@LiveRecordingManager.RECORDING_STATE int from,
                                    @LiveRecordingManager.RECORDING_STATE int to) {
        stopPlay();
        updateUIState();

        Bundle bundle = new Bundle();
        bundle.putString(LiveShortVideoRecordedFragment.BUNDLE_VIDEO_PATH, videoPath);
        bundle.putInt(LiveShortVideoRecordedFragment.BUNDLE_VIDEO_LENGTH, videoLength / 1000);
        bundle.putString(LiveShortVideoRecordedFragment.BUNDLE_THUMBNAIL_FILE, thumbnailFile.get());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(com.jeremy.lychee.R.id.root, instantiate(getActivity(), LiveShortVideoRecordedFragment.class.getName(), bundle))
                .commit();

        return true;
    }

    private boolean reFlow(@LiveRecordingManager.RECORDING_STATE int from,
                           @LiveRecordingManager.RECORDING_STATE int to) {
        stopPlay();
        updateUIState();
        reset();

        return true;
    }

    private void closeRecorder() {
        if (mCloudRecorder != null) {
            mCloudRecorder.stop();
            mCloudRecorder.release();
            mCloudRecorder = null;
        }
    }

    private void reset() {
        closeRecorder();
        deleteTempFile();
        thumbnailFile = new AtomicReference<>();
        videoPath = null;
        videoLength = 0;
    }

    private void deleteTempFile() {
        if (videoPath != null) {
            File videoFile = new File(videoPath);
            videoFile.delete();
        }

        if (thumbnailFile != null && thumbnailFile.get() != null) {
            File thumbFile = new File(thumbnailFile.get());
            thumbFile.delete();
        }
    }

    public void onEventMainThread(Event_OnSnCallback event) {
        if (mCloudRecorder == null || event == null || cameraManager.getCurrentCamera() == null) {
            return;
        }

        mCloudRecorder.setInput(0, cameraManager.getCurrentCamera().getRawCamera(),
                LiveCloudConfigManager.getInstance().getMediaSettings(cameraManager.getCurrentCamera().getCameraId() == cameraManager.getBackCameraId()));
        mCloudRecorder.start();

        countDown();
    }

    private void countDown() {
        progressSubscription = Observable.interval(PROGRESS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(((RxAppCompatActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .take(MAX_RECORD_DURATION / PROGRESS_UPDATE_INTERVAL)
                .map(Long::intValue)
                .doOnNext(it -> timerTextView.setText(String.format("%02ds", it * PROGRESS_UPDATE_INTERVAL / 1000)))
                .doOnNext(it -> videoLength = it * PROGRESS_UPDATE_INTERVAL)
                .doOnNext(it -> {
                    if (it % BUTTON_SPLASH_FACTOR == 0) {
                        recordBtn.setImageResource(com.jeremy.lychee.R.drawable.living_dsp_press_down);
                    } else if (it % BUTTON_SPLASH_FACTOR == BUTTON_SPLASH_FACTOR / 2) {
                        recordBtn.setImageResource(com.jeremy.lychee.R.drawable.living_dsp_default);
                    }
                })
                .subscribe(recordingProgress::setProgress, Throwable::printStackTrace,
                        () -> liveRecordingManager.doNext(LiveRecordingManager.RECORDING_STATE.AFTER_RECORD));
    }

    private void finishCounter() {
        if (progressSubscription != null && !progressSubscription.isUnsubscribed()) {
            progressSubscription.unsubscribe();
            progressSubscription = null;
        }
    }

    private void loadThumbnail() {
        if (thumbnailFile == null || thumbnailFile.get() == null) {
            return;
        }

        try {
            Bitmap thumbnailBitmap = BitmapFactory.decodeFile(thumbnailFile.get());
            thumbnailImgView.setImageBitmap(thumbnailBitmap);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // 录制视频后，后台线程仍然在不断的向SD卡写入视频数据，因此需要等待视频数据写入完毕后才能继续后续操作
    private void waitForSaveToSD(Action0 action) {
        WeakReference<ProgressDialog> dialog = new WeakReference<>(new ProgressDialog(getContext()));
        dialog.get().setIndeterminate(true);
        dialog.get().setMessage("正在保存录制的视频，请稍等...");
        dialog.get().setCancelable(false);
        dialog.get().show();

        Observable.using(() -> new Object() {
                    public long i = 0;
                },
                i -> Observable.interval(500, TimeUnit.MILLISECONDS, Schedulers.io()).map(it -> i), o -> {
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureDrop()
                .filter(it -> {
                    long fileLen = new File(videoPath).length();
                    if (fileLen == it.i) {
                        return true;
                    } else {
                        it.i = fileLen;
                        return false;
                    }
                })
                .take(4)
                .subscribe(it -> {
                }, Throwable::printStackTrace, () -> {
                    if (dialog.get() != null)
                        dialog.get().dismiss();
                    action.call();
                });
    }

    private void stopPlay() {
        videoArea.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        recordBtn.setImageResource(com.jeremy.lychee.R.drawable.living_dsp_play);
    }

    private void startPlay() {
        recordBtn.setImageResource(com.jeremy.lychee.R.drawable.living_dsp_stop);
        videoView.setVideoPath(videoPath);
        videoView.setOnCompletionListener(mp -> AndroidSchedulers.mainThread().createWorker().schedule(this::stopPlay));
        videoView.start();
        videoArea.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.VISIBLE);
    }
}
