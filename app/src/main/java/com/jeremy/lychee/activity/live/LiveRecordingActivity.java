package com.jeremy.lychee.activity.live;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jeremy.lychee.channel.QHState;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.manager.QhLocationManager;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.AudioUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.dialog.ConfirmDialog;
import com.jeremy.lychee.customview.live.FlipRotateAnimation;
import com.jeremy.lychee.customview.live.ProgressDrawable;
import com.qihoo.livecloud.tools.Logger;
import com.qihoo.sdk.report.QHStatAgent;
import com.qihu.mobile.lbs.location.QHLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LiveRecordingActivity extends LiveRecordingBaseActivity implements
        SurfaceHolder.Callback {
    private final static String TAG = "LiveRecordingActivity";
    private final static int COUNT = 1;
    private final static int COUNT_STOP = -1;

    @Bind(com.jeremy.lychee.R.id.live_title)
    TextView tv_live_title;

    @Bind(com.jeremy.lychee.R.id.live_info)
    TextView live_info;

    @Bind(com.jeremy.lychee.R.id.close)
    View close;

    @Bind(com.jeremy.lychee.R.id.info)
    View info;

    @Bind(com.jeremy.lychee.R.id.flash_light)
    View flash_light;

    @Bind(com.jeremy.lychee.R.id.camera)
    View camera;

    @Bind(com.jeremy.lychee.R.id.start)
    Button start;

    @Bind(com.jeremy.lychee.R.id.stop)
    Button stop;

    @Bind(com.jeremy.lychee.R.id.pause)
    Button pause;

    @Bind(com.jeremy.lychee.R.id.more)
    TextView more;

    @Bind(com.jeremy.lychee.R.id.change_title)
    View change_title;

    @Bind(com.jeremy.lychee.R.id.cb_location)
    CheckBox cb_location;

    @Bind(com.jeremy.lychee.R.id.cb_orientation)
    CheckBox cb_orientation;

    @Bind(com.jeremy.lychee.R.id.et_title)
    EditText et_title;

    @Bind(com.jeremy.lychee.R.id.record_audio)
    Button record_audio;

    @Bind(com.jeremy.lychee.R.id.play_audio)
    Button play_audio;

    @Bind(com.jeremy.lychee.R.id.root)
    View root;

    @Bind(com.jeremy.lychee.R.id.cover_hint)
    View cover_hint;

    @Bind(com.jeremy.lychee.R.id.iv_living_pause)
    View iv_living_pause;

    @Bind(com.jeremy.lychee.R.id.iv_voice_input)
    View iv_audio_input;

    @Bind(com.jeremy.lychee.R.id.iv_soft_input)
    View iv_soft_input;

    @Bind(com.jeremy.lychee.R.id.voice_input)
    View voice_input;

    @Bind(com.jeremy.lychee.R.id.soft_input)
    View soft_input;

    @Bind(com.jeremy.lychee.R.id.btn_voice_input)
    TextView btn_voice_input;

    @Bind(com.jeremy.lychee.R.id.btn_voice_play)
    TextView btn_voice_play;

    @Bind(com.jeremy.lychee.R.id.btn_voice_play_group)
    View btn_voice_play_group;

    @Bind(com.jeremy.lychee.R.id.iv_delete_voice)
    View iv_delete_voice;

    @Bind(com.jeremy.lychee.R.id.finish)
    View finish;

    @Bind(com.jeremy.lychee.R.id.iv_living_pause_text)
    View iv_living_pause_text;

    @Bind(com.jeremy.lychee.R.id.voice_play_progress)
    View voice_play_progress;

    private StaticHandler playingHandler;

    private String liveTitle = "";
    private int timer;
    private String mSn;
    private String id;
    private String pid;
    private String is_seg;
    private String lanid;

    private double latitude;
    private double longitude;
    private String poi;
    private int watchedNum;
    private int currentNum;
    private int period;
    private File screenShotFile;
    private boolean recording;
    private boolean coverCaptured;
    private AudioUtil audioUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Session.isUserInfoEmpty()) {
            Toast.makeText(LiveRecordingActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }*/
        audioUtil = new AudioUtil(getApplicationContext());
        setContent(com.jeremy.lychee.R.layout.activity_live_recording);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        Logger.d(TAG, "onDestroy");
        super.onDestroy();
        if (playingHandler != null) {
            playingHandler.removeMessages(COUNT);
            playingHandler.sendEmptyMessage(COUNT_STOP);
        }
    }


    @Override
    public void onBackPressed() {
        if (!recording) {
            QEventBus.getEventBus().post(new LiveEvent.showIntroLive());
            super.onBackPressed();
        }
    }

    private void initView() {
        Calendar c = Calendar.getInstance();

        tv_live_title.setText(String.format("%s月%s日的直播", c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)));
        camera.setOnClickListener(clickListener);
        flash_light.setOnClickListener(clickListener);
        close.setOnClickListener(clickListener);
        start.setOnClickListener(clickListener);
        pause.setOnClickListener(clickListener);
        stop.setOnClickListener(clickListener);
        more.setOnClickListener(clickListener);
        change_title.setOnClickListener(clickListener);
        iv_audio_input.setOnClickListener(clickListener);
        iv_soft_input.setOnClickListener(clickListener);
        iv_delete_voice.setOnClickListener(clickListener);
        btn_voice_play.setOnClickListener(clickListener);
        finish.setOnClickListener(clickListener);

        playingHandler = new StaticHandler(new WeakReference<>(this));

        QhLocationManager.getInstance().updateLocation(() -> {
            QHLocation location = QhLocationManager.getInstance().getLocation();
            this.poi = location.getAddrStr();
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        });


        btn_voice_input.setOnTouchListener(new View.OnTouchListener() {

            MyAnimation animation;
            ProgressDrawable background;

            class MyAnimation extends Animation {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);
                    background.setProgress(interpolatedTime);
                }
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_voice_input.setText("录音中");
                        audioUtil.startRecord();

                        background = new ProgressDrawable();
                        voice_play_progress.setBackgroundDrawable(background);

                        btn_voice_input.clearAnimation();//...
                        animation = new MyAnimation();
                        animation.setDuration(10 * 1000);
                        animation.setInterpolator(new LinearInterpolator());
                        btn_voice_input.setAnimation(animation);
                        animation.reset();
                        animation.start();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        background.setProgress(0);
                        btn_voice_input.clearAnimation();//...
                        btn_voice_input.setText("按住录音");
                        audioUtil.stopRecord();
                        btn_voice_input.setVisibility(View.GONE);
                        btn_voice_play_group.setVisibility(View.VISIBLE);
                        animation.cancel();
                        break;
                }
                return true;
            }
        });

        cb_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && latitude == 0 && longitude == 0) {
                    QhLocationManager.getInstance().updateLocation(() -> {
                        QHLocation location = QhLocationManager.getInstance().getLocation();
                        poi = location.getAddrStr();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    });
                    ToastHelper.getInstance(buttonView.getContext()).toast("定位失败");
                    buttonView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonView.setChecked(false);
                        }
                    }, 300);
                }
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case com.jeremy.lychee.R.id.close:
                    stopRecord();
                    finish();
                    break;
                case com.jeremy.lychee.R.id.flash_light:
                    toggleFlashLight();
                    break;
                case com.jeremy.lychee.R.id.camera:
                    v.clearAnimation();

                    float centerX = v.getWidth() / 2.0f;
                    float centerY = v.getHeight() / 2.0f;
                    FlipRotateAnimation animation = new FlipRotateAnimation(1, 0, 180, centerX, centerY, 310f);
                    animation.setDuration(500);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setFillAfter(true);
                    v.startAnimation(animation);
                    changeCamera(true);
                    break;
                case com.jeremy.lychee.R.id.more:
                    View changeTitle = findViewById(com.jeremy.lychee.R.id.change_title);
                    changeTitle.setVisibility(changeTitle.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
                case com.jeremy.lychee.R.id.change_title:
                    v.setVisibility(View.GONE);
                    showUpdateTitleDialog();
                    break;
                case com.jeremy.lychee.R.id.start:
                    if (!AppUtil.isNetAvailable(getApplicationContext())) {
                        Toast.makeText(LiveRecordingActivity.this, getString(com.jeremy.lychee.R.string.news_net_error_text), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (cb_orientation.isChecked()) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        changeCamera(false);
                    }
                    AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(com.jeremy.lychee.R.drawable.living_bling_dot);
                    live_info.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    drawable.start();
                    QHStatAgent.onEvent(LiveRecordingActivity.this, QHState.USER_INITIATE_LIVE);
                    liveTitle = et_title.getText().toString();
                    if (!TextUtils.isEmpty(liveTitle)){
                        QHStatAgent.onEvent(LiveRecordingActivity.this, QHState.USER_WRITING_TITLE);
                    }
                    start.setVisibility(View.GONE);
//                    stop.setVisibility(View.VISIBLE);
                    info.setVisibility(View.GONE);
                    close.setVisibility(View.GONE);
                    cover_hint.setVisibility(View.GONE);
                    tv_live_title.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(et_title.getText().toString().trim())) {
                        tv_live_title.setText(et_title.getText().toString().trim());
                    }
                    live_info.setVisibility(View.VISIBLE);
                    finish.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.VISIBLE);
//                    changeCamera(false);
                    info.setBackgroundColor(Color.TRANSPARENT);
                    startRecord(cb_orientation.isChecked());
                    audioUtil.stopPlay();
                    break;
                case com.jeremy.lychee.R.id.stop:
                    showFinishDialog();
                    break;
                case com.jeremy.lychee.R.id.pause:
                    togglePauseAndPlay();
                    break;
                case com.jeremy.lychee.R.id.iv_voice_input:
                    voice_input.setVisibility(View.VISIBLE);
                    soft_input.setVisibility(View.GONE);
                    break;
                case com.jeremy.lychee.R.id.iv_soft_input:
                    voice_input.setVisibility(View.GONE);
                    soft_input.setVisibility(View.VISIBLE);
                    break;
                case com.jeremy.lychee.R.id.iv_delete_voice:
                    btn_voice_input.setVisibility(View.VISIBLE);
                    btn_voice_play_group.setVisibility(View.GONE);
                    btn_voice_play.setText("播放录音");
                    btn_voice_play.setTag(null);
                    break;
                case com.jeremy.lychee.R.id.btn_voice_play:
                    if (btn_voice_play.getTag() == null) {
                        audioUtil.startPlay(new AudioUtil.PlayCallback() {
                            @Override
                            public void onPlayOver() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btn_voice_play.setText("播放录音");
                                        btn_voice_play.setTag(null);
                                    }
                                });

                            }
                        });
                        btn_voice_play.setText("停止播放");
                        btn_voice_play.setTag(1);
                    } else {
                        audioUtil.stopPlay();
                        btn_voice_play.setText("播放录音");
                        btn_voice_play.setTag(null);
                    }
                    break;
                case com.jeremy.lychee.R.id.finish:
                    showFinishDialog();
                    break;
            }
        }
    };


    static class StaticHandler extends Handler {
        final WeakReference<LiveRecordingActivity> outer;

        StaticHandler(WeakReference<LiveRecordingActivity> outer) {
            this.outer = outer;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (outer.get() != null) {
                if (msg.what == COUNT) {
                    removeMessages(COUNT);
                    sendEmptyMessageDelayed(COUNT, 1000);
                    int timer = outer.get().timer++;
                    int period = outer.get().period;
                    String sn = outer.get().mSn;
                    outer.get().updateTimer(timer);
                    if (period == 0 && sn != null) {
                        outer.get().getLivePeopleNum(sn);
                    } else if (period > 0) {
                        if (timer % period == 0) {
                            outer.get().getLivePeopleNum(sn);
                        }
                    }
                } else {
                    removeMessages(COUNT);
                }
            }
        }
    }

    private void updateTimer(int timer) {
        int s = timer % 60;
        int m = timer / 60 % 60;
        int h = timer / 60 / 60;
        String hh = String.format("%02d", h);
        String mm = String.format("%02d", m);
        String ss = String.format("%02d", s);
        String time = hh + ":" + mm + ":" + ss + " " + watchedNum + "人观看/" + currentNum + "人在线";
        live_info.setText(time);
    }


    private void showUpdateTitleDialog() {
        View view = View.inflate(getApplicationContext(), com.jeremy.lychee.R.layout.change_title_dialog_header, null);
        EditText title = (EditText) view.findViewById(com.jeremy.lychee.R.id.et_title);
        title.setText(et_title.getText());
        ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.setCustomContentView(view).setOnConfirmListener(v -> {
            dialog.dismiss();
            liveTitle = title.getText().toString().trim();
            if (!TextUtils.isEmpty(liveTitle)) {
                tv_live_title.setText(liveTitle);
                et_title.setText(liveTitle);
            }
        }).show();
    }

    private void showFinishDialog() {
        DialogUtil.showConfirmDialog(this, "确定要结束直播吗",
                getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                    dialog.dismiss();
                    closeCamera();
                    if (recording) {
                        endLiveTask(liveTitle, String.valueOf(timer), mSn, id, is_seg, pid);
                    }
                    playingHandler.removeMessages(COUNT);
                    playingHandler.sendEmptyMessage(COUNT_STOP);
                    QEventBus.getEventBus().post(new LiveEvent.showIntroLive());
                    finish();
                }, getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss);
    }

    @Override
    protected void onLiveSuccess(String sn) {
        super.onLiveSuccess(sn);
        is_seg = "0";
        if (mSn != null) {
            is_seg = "1";
        }
        mSn = sn;
        playingHandler.sendEmptyMessage(COUNT);
        if (is_seg.equals("1")) {
            startLiveTask(liveTitle, null, is_seg.equals("0") ? screenShotFile : null, audioUtil.getFile(), cb_location.isChecked() ? String.valueOf(latitude) : null, cb_location.isChecked() ? String.valueOf(longitude) : null, poi, mSn, is_seg, pid, lanid);
        }
    }


    private void startLiveTask(String title, String video_desc, File image, File audio, String latitude, String longitude, String poi_name, String sn, String isSeg, String pid, String lanid) {

        MediaType textType = MediaType.parse("text/plain; charset=UTF-8");
        MediaType streamType = MediaType.parse("application/octet-stream");

        RequestBody titlePart = title == null ? null : RequestBody.create(textType, title);
        RequestBody video_descPart = video_desc == null ? null : RequestBody.create(textType, video_desc);
        RequestBody imagePart = image == null ? null : RequestBody.create(streamType, image);
        RequestBody audioPart = audio == null || !audio.exists() ? null : RequestBody.create(streamType, audio);
        RequestBody latitudePart = latitude == null ? null : RequestBody.create(textType, latitude);
        RequestBody longitudePart = longitude == null ? null : RequestBody.create(textType, longitude);
        RequestBody poi_namePart = poi_name == null ? null : RequestBody.create(textType, poi_name);
        RequestBody snPart = sn == null ? null : RequestBody.create(textType, sn);
        RequestBody isSegPart = isSeg == null ? null : RequestBody.create(textType, isSeg);
        RequestBody pidPart = pid == null ? null : RequestBody.create(textType, pid);
        RequestBody lanidPart = lanid == null ? null : RequestBody.create(textType, lanid);

        OldRetroAdapter.getService().liveStart(titlePart, video_descPart, imagePart, audioPart, latitudePart, longitudePart, poi_namePart, snPart, isSegPart, pidPart, lanidPart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase<JsonObject>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
//                        Toast.makeText(LiveRecordingActivity.this, "直播失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ModelBase<JsonObject> modelBase) {
                        if (modelBase.getErrno() == 0) {
                            recording = true;
                            JsonObject data = modelBase.getData();
                            id = data.get("id").getAsString();
                            if (LiveRecordingActivity.this.pid == null) {
                                LiveRecordingActivity.this.pid = id;
                            }
                            audioUtil.clean();
//                            Toast.makeText(LiveRecordingActivity.this, "开始接口OK", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(LiveRecordingActivity.this, modelBase.getErrmsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void endLiveTask(String title, String video_length, String sn, String id, String isSeg, String pid) {
        recording = false;
        MediaType textType = MediaType.parse("text/plain; charset=UTF-8");

        RequestBody titlePart = title == null ? null : RequestBody.create(textType, title);
        RequestBody lengthPart = video_length == null ? null : RequestBody.create(textType, video_length);
        RequestBody snPart = sn == null ? null : RequestBody.create(textType, sn);
        RequestBody idPart = id == null ? null : RequestBody.create(textType, id);
        RequestBody pidPart = pid == null ? null : RequestBody.create(textType, pid);
        RequestBody isSegPart = isSeg == null ? null : RequestBody.create(textType, isSeg);

        OldRetroAdapter.getService().liveEnd(titlePart, lengthPart, snPart, idPart, isSegPart, pidPart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ModelBase modelBase) {

//                        if (modelBase.getErrno() == 0) {
//                            Toast.makeText(LiveRecordingActivity.this, "直播成功", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
    }

    private void getLivePeopleNum(String sn) {
        OldRetroAdapter.getService().getLivePeople(sn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase<JsonObject>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        period = 5;
                    }

                    @Override
                    public void onNext(ModelBase<JsonObject> modelBase) {
                        if (modelBase.getErrno() == 0) {
                            JsonObject data = modelBase.getData();
                            watchedNum = data.get("watchedNum").getAsInt();
                            currentNum = data.get("currentNum").getAsInt();
                            period = data.get("period").getAsInt();
                        }
                    }
                });
    }


    @Override
    protected void onPreviewFrame(byte[] data, String sn) {
        if (coverCaptured) {
            return;
        }
        coverCaptured = true;
        Observable.just(null)
                .observeOn(Schedulers.io())
                .doOnNext(o -> {
                    try {

                        screenShotFile = new File(getExternalFilesDir(null).getAbsolutePath() + "/" + "screenshot.jpg");
//                        screenShotFile = new File("/sdcard/zzzzzzzz.jpg");
                        if (screenShotFile.exists()) {
                            screenShotFile.delete();
                        }
                        screenShotFile.createNewFile();
                        FileOutputStream outputStream = new FileOutputStream(screenShotFile);
                        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, 1280, 720, null);

                        double ratio = 530 * 1.0 / 720;
                        boolean isFrontCamera = isFrontCamera();
                        if (!isFrontCamera) {
                            yuvImage.compressToJpeg(new Rect(0, 0, (int) (720 * ratio), 720), 75, outputStream);
                        } else {
                            yuvImage.compressToJpeg(new Rect(1280 - (int) (720 * ratio), 0, 1280, 720), 75, outputStream);
                        }

                        outputStream.flush();
                        outputStream.close();
                        screenShotFile = rotateImage(screenShotFile, isFrontCamera ? -90 : 90);
                        startLiveTask(liveTitle, null, screenShotFile, audioUtil.getFile(), cb_location.isChecked() ? String.valueOf(latitude) : null, cb_location.isChecked() ? String.valueOf(longitude) : null, poi, sn, is_seg, pid, lanid);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .subscribe();

    }

    private File rotateImage(File file, int degree) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file.getAbsolutePath()));
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            if (screenShotFile.exists()) {
                screenShotFile.delete();
            }
            screenShotFile.createNewFile();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    private void togglePauseAndPlay() {
        //用tag来标识暂停状态还是播放状态,就不用一个外部变量了
        if (pause.getTag() == null) {
            live_info.setCompoundDrawablesWithIntrinsicBounds(com.jeremy.lychee.R.drawable.living_icon_gray, 0, 0, 0);
            pause.setTag(1);
            stopRecord();
            root.setBackgroundColor(Color.parseColor("#a8000000"));
            pause.setText("继续");
            iv_living_pause.setVisibility(View.VISIBLE);
            iv_living_pause_text.setVisibility(View.VISIBLE);
            playingHandler.removeMessages(COUNT);
            playingHandler.removeMessages(COUNT_STOP);
            playingHandler.sendEmptyMessageDelayed(COUNT_STOP, 1000);
            if (recording) {
                endLiveTask(liveTitle, String.valueOf(timer), mSn, id, "1", pid);
            }
        } else {
            if (!AppUtil.isNetAvailable(getApplicationContext())) {
                Toast.makeText(LiveRecordingActivity.this, getString(com.jeremy.lychee.R.string.news_net_error_text), Toast.LENGTH_SHORT).show();
                return;
            }
            AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(com.jeremy.lychee.R.drawable.living_bling_dot);
            live_info.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            drawable.start();
            pause.setTag(null);
            startRecord(cb_orientation.isChecked());
            root.setBackgroundColor(Color.TRANSPARENT);
            pause.setText("暂停");
            iv_living_pause.setVisibility(View.GONE);
            iv_living_pause_text.setVisibility(View.GONE);
            playingHandler.removeMessages(COUNT);
            playingHandler.removeMessages(COUNT_STOP);
            playingHandler.sendEmptyMessageDelayed(COUNT, 1000);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        change_title.setVisibility(View.GONE);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onRecordError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                togglePauseAndPlay();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recording) {
            togglePauseAndPlay();
        }
        audioUtil.stopPlay();
    }

    private boolean isLighOn;

    public void toggleFlashLight() {
        try {
            if (mCamera != null) {
                Camera.Parameters p = mCamera.getParameters();
                if (!isLighOn) {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(p);
                    mCamera.startPreview();

                } else {
//                mCamera.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(p);
                    mCamera.startPreview();
                }
                isLighOn = !isLighOn;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

}
