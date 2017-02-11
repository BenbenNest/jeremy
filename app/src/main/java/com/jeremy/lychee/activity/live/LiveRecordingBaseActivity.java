package com.jeremy.lychee.activity.live;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.lychee.BuildConfig;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.utils.AppUtil;
import com.qihoo.livecloud.LiveCloudRecorder;
import com.qihoo.livecloud.recorder.callback.RecorderCallBack;
import com.qihoo.livecloud.recorder.setting.BaseSettings.ECropMode;
import com.qihoo.livecloud.recorder.setting.BaseSettings.ELogLevel;
import com.qihoo.livecloud.recorder.setting.BaseSettings.ELogModule;
import com.qihoo.livecloud.recorder.setting.MediaSettings;
import com.qihoo.livecloud.recorder.setting.PublishSettings;
import com.qihoo.livecloud.tools.DeviceIDUtils;
import com.qihoo.livecloud.tools.LiveCloudConfig;
import com.qihoo.livecloud.tools.Logger;
import com.qihoo.livecloud.tools.MD5;
import com.qihoo.livecloud.tools.NetUtil;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class LiveRecordingBaseActivity extends Activity implements
        SurfaceHolder.Callback {
    private final static String TAG = "LiveRecordingBaseActivity";

    private final static String CID = "live_news";
    private final static String BID = "news";

    private LiveCloudRecorder mCloudRecorder = null;
    private TextView text = null;
    private SurfaceView mSurfaceView;

    private boolean isSnReturn = false;// sn是否返回
    private String mSn = null;

    public static int frontCameraID = -1;
    public static int backCameraID = -1;
    public int mCurrentCameraID = -10;
    protected int degree = 90;
    protected Camera mCamera;
    protected int previewWidth = 720, previewHeight = 480;
    boolean changeLock = false;

    private Object mObject = new Object();
    protected boolean horizontal;

    public void setContent(int resId) {
        View parent = findViewById(com.jeremy.lychee.R.id.parent_layout);
        if (parent != null) {
            ((ViewGroup) parent).addView(View.inflate(getApplicationContext(), resId, null));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreate");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onDestroy() {
        Logger.d(TAG, "onDestroy");
        super.onDestroy();
//		closeCamera();
    }

    @Override
    public void onBackPressed() {
        Logger.d(TAG, "onBackPressed");
        closeCamera();
        super.onBackPressed();
    }

    private void initView() {
        setContentView(com.jeremy.lychee.R.layout.activity_live_base_recording);
        mSurfaceView = (SurfaceView) findViewById(com.jeremy.lychee.R.id.surfaceView1);
        mSurfaceView.getHolder().addCallback(this);
        text = (TextView) findViewById(com.jeremy.lychee.R.id.textrecorder);
        text.setVisibility(BuildConfig.DEBUG ? View.VISIBLE : View.GONE);
    }

    public void closeCamera() {
        try {
            Logger.d(TAG, "closeCamera");
            long time = System.currentTimeMillis();
            if (mCloudRecorder != null) {
                mCloudRecorder.stop();
                mCloudRecorder.release();
                mCloudRecorder = null;
                Logger.d(TAG,
                        "关闭LiveCloudRecorder时间："
                                + (System.currentTimeMillis() - time));
            }

            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;
                Logger.d(TAG, "关闭总时间：" + (System.currentTimeMillis() - time));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    public synchronized void changeCamera(boolean changeCamera) {
        if (changeLock) {
            return;
        }
        changeLock = true;
        try {

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    frontCameraID = i;
                } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    backCameraID = i;
                }
            }
            if (changeCamera || mCurrentCameraID == -10) {
                if (backCameraID != -1 && backCameraID != mCurrentCameraID)
                    mCurrentCameraID = backCameraID;
                else if (frontCameraID != -1
                        && frontCameraID != mCurrentCameraID)
                    mCurrentCameraID = frontCameraID;
            }

            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;
            }
            mCamera = Camera.open(mCurrentCameraID);

            if (mCamera == null) {
                this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "打开相机失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                return;
            }

            Parameters parameters = mCamera.getParameters();

            // 设置多媒体录制，会加快打开速度，但是在联想a820上会导致黑屏,小米4上会导致卡死,魅族mx4 pro前置摄像头花屏
            /* parameters.setRecordingHint(true); */

            if (parameters.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters
                        .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }

			/*
             * if(Build.VERSION.SDK_INT > 14){
			 * //设置防抖，但据有人说在小米4上会造成画面抖动，但我在一个小米4上未复现，为了稳定，暂时不加
			 * if(parameters.isVideoStabilizationSupported())
			 * parameters.setVideoStabilization(true); }
			 */
            parameters.setPreviewFormat(ImageFormat.NV21);

            List<Size> list = parameters.getSupportedPreviewSizes();
            for (int j = 0; j < list.size(); j++) {
                Size s = list.get(j);
                Logger.d(TAG, s.width + "===支持的size===" + s.height);
            }
            parameters.setPreviewSize(1280, 720);
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(getCameraDisplayOrientation(this,
                    mCurrentCameraID));
            try {
                mCamera.setPreviewDisplay(mSurfaceView.getHolder());
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "相机出现问题",
                            Toast.LENGTH_LONG).show();
                }
            });

            e.printStackTrace();
            return;
        }
        changeLock = false;
        if (recording) {
            doRecord();
        }
    }

    private PublishSettings getPublishSettings() {
        PublishSettings setting = new PublishSettings();
        // 基本设置
        setting.setUid(AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        setting.setVersion(1);
        setting.setPublishProtocol(PublishSettings.ETransportProtocol.PROTOCOL_RTMP);
        String str = MD5.encryptMD5(String.valueOf(System.currentTimeMillis())
                + String.valueOf(new Random().nextInt()));
        str = str.substring(0, 20);
        String sn = "_XW_" + str;
        setting.setR_sn(sn);

        setting.setSid(getConfig().getSid());
        setting.setMax_retry(0);
        setting.setConnect_timeout(0);
        setting.setSend_timeout(0);
        setting.setAuto_close(0);// 0 false,其他为true
        setting.setLog_level(ELogLevel.LOG_LEVEL_NONE);
        setting.setLog_module(ELogModule.LOG_MOD_NONE);
        setting.setLog_path("");

//		 setting.setMp4FileName(MP4_PATHNAME); // mp4文件路径
//		 setting.setOnlyToFile(ISONLYTOFILE); // 设为1只保存成mp4文件，设为0保存的同时还上传推流

        return setting;
    }


    private MediaSettings getMediaSettings() {
        MediaSettings setting = new MediaSettings();
        // 视频设置
        setting.setInputVideoFormat(PublishSettings.EVideoCodecID.V_CODEC_ID_NV21);
        setting.setAnnexB(0);
        setting.setSourceWidth(1280);
        setting.setSourceHeight(720);
        if (horizontal) {
            setting.setCodecWidth(640);
            setting.setCodecHeight(360);
            if (mCurrentCameraID == frontCameraID) {
                setting.setRotate(0);
            } else if (mCurrentCameraID == backCameraID) {
                setting.setRotate(0);
            }
        } else {
            setting.setCodecWidth(360);
            setting.setCodecHeight(640);
            if (mCurrentCameraID == frontCameraID) {
                setting.setRotate(270);
            } else if (mCurrentCameraID == backCameraID) {
                setting.setRotate(90);
            }
        }


//		setting.setCodecWidth(640);
//		setting.setCodecHeight(360);
//
//		if (mCurrentCameraID == frontCameraID) {
//			setting.setRotate(0);
//		} else if (mCurrentCameraID == backCameraID) {
//			setting.setRotate(0);
//		}

        setting.setCropMode(ECropMode.ECROP_CENTER_SCALE);
        setting.setAvgBitrate(600 * 1024);
        setting.setPeekBitrate(800 * 1024);
        setting.setFps(15);

        // 音频设置
        setting.setInputAudioFormat(PublishSettings.EAudioCodecID.A_CODEC_ID_PCM);
        setting.setOutputAudioFormat(PublishSettings.EAudioCodecID.A_CODEC_ID_AAC);
        setting.setSampleRate(16000);
        setting.setTargetBitrate(32000);
        setting.setChannelConfig(1);// 设置声道数
        setting.setSampleDepth(16);// 设置采样宽度，为16bit
        return setting;
    }

    public boolean isFrontCamera(final int id) {
        CameraInfo2 info = new CameraInfo2();
        getCameraInfo(id, info);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return true;
        }
        return false;
    }

    public boolean isFrontCamera() {
        CameraInfo2 info = new CameraInfo2();
        getCameraInfo(mCurrentCameraID, info);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return true;
        }
        return false;
    }

    public void onOrientationChanged(int orientation) {
        /*
         * if (orientation == ORIENTATION_UNKNOWN) return;
		 * android.hardware.Camera.CameraInfo info = new
		 * android.hardware.Camera.CameraInfo();
		 * android.hardware.Camera.getCameraInfo(cameraId, info); orientation =
		 * (orientation + 45) / 90 * 90; int rotation = 0; if (info.facing ==
		 * CameraInfo.CAMERA_FACING_FRONT) { rotation = (info.orientation -
		 * orientation + 360) % 360; } else { // back-facing camera rotation =
		 * (info.orientation + orientation) % 360; }
		 * mParameters.setRotation(rotation);
		 */
    }

    public int getCameraDisplayOrientation(final Activity activity,
                                           final int cameraId) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Logger.d(TAG, "degrees = " + degrees);
        int result;
        CameraInfo2 info = new CameraInfo2();
        getCameraInfo(cameraId, info);
        Logger.d(TAG, "info.orientation = " + info.orientation);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public void getCameraInfo(final int cameraId, final CameraInfo2 cameraInfo) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        cameraInfo.facing = info.facing;
        cameraInfo.orientation = info.orientation;
    }

    public static class CameraInfo2 {
        public int facing;
        public int orientation;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN
//                && keyCode != KeyEvent.KEYCODE_BACK) {
//            changeCamera(true);
//			/*
//			 * if(mCloudRecorder != null){ mCloudRecorder.stopRecorder(); }
//			 */
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    boolean surfaceCreated = false;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!surfaceCreated) {
            new Thread(new Runnable() {
                public void run() {
                    Logger.d(TAG, "begin change");
                    changeCamera(false);
                    Logger.d(TAG, "end change");
                }
            }).start();
        }
        surfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceCreated = false;
    }

    LiveCloudConfig config;
    /*LiveCloudConfig config;
    LiveCloudConfig getConfig() {
        if (config != null) {
            return config;
        }
          config = new LiveCloudConfig();
        config.setCid(CID);
        config.setUid(AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        config.setVer(String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        config.setBid(BID);

        // 需由业务服务端生成token和ts
        String ts = String.valueOf(System.currentTimeMillis() / 1000);
        String token = "channel__" + CID + "sn__" + mSn + "ts__" + ts + "key_"
                + "tokentest";
        String utoken = MD5.encryptMD5(token);
        config.setToken(utoken);
        config.setTs(ts);

        String sid = MD5
                .encryptMD5(String.valueOf(System.currentTimeMillis()) + String.valueOf(new Random().nextInt()));
        config.setSid(sid);
        config.setMid(DeviceIDUtils.getIMEI2(this));
        config.setNet(NetUtil.getNetworkTypeName(this));
        config.setRid("");

        return config;
    }
*/


    LiveCloudConfig getConfig() {
        if (config != null) {
            return config;
        }

        config = new LiveCloudConfig();
        config.setCid(CID);
        config.setUid(AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        config.setVer(String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        config.setBid(BID);

        String sid = MD5
                .encryptMD5(String.valueOf(System.currentTimeMillis()) + String.valueOf(new Random().nextInt()));
        config.setSid(sid);
        config.setMid(DeviceIDUtils.getIMEI2(this));
        config.setNet(NetUtil.getNetworkTypeName(this));
        config.setRid(mSn);

        return config;
    }


    private void onGetSn(String sn) {
        mCloudRecorder.setCloudPreviewCallback(new LiveCloudRecorder.CloudPreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] bytes, int i, int i1) {
                LiveRecordingBaseActivity.this.onPreviewFrame(bytes, sn);
            }
        });
    }

    protected void onLiveSuccess(String sn) {

    }

    private boolean recording;

    public void stopRecord() {
        recording = false;
        Logger.d(TAG, "stopLive");
        if (mCloudRecorder != null) {
            mCloudRecorder.stop();
            mCloudRecorder.release();
            mCloudRecorder = null;
        }
    }

    public void startRecord(boolean horizontal) {
        this.horizontal = horizontal;
        recording = true;
        mCloudRecorder = LiveCloudRecorder.staticCreate();// 获取静态对象
        mCloudRecorder.setConfig(getConfig());
        LiveCloudRecorder.init();// 初始化环境，静态初始化，可以在程序一开始调用
        int id = mCloudRecorder.createSession(getPublishSettings());// 创建一个session
        // 必须先设置回调，才能去请求sn
        mCloudRecorder.setCallBack(new RecorderCallBack() {
            public void snCallback(int sessionId, String sn) {
                Logger.d(TAG, "snCallback sn = " + sn);
                if (!TextUtils.isEmpty(sn)) {
                    mSn = sn;
                    onGetSn(mSn);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            text.setText(text.getText().toString() + "\nsn="
                                    + mSn);
                        }
                    });
                } else {
                    mSn = null;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            text.setText(text.getText().toString()
                                    + "\n获取的sn为空");
                        }
                    });
                }
                synchronized (mObject) {
                    mObject.notify();
                    Logger.d(TAG, "mObject.notify()");
                }
                isSnReturn = true;
            }

            @Override
            public void recorderState(int sessionId, int pubEvent, int param) {
                Logger.e(TAG, "recorderState pubEvent= " + pubEvent);
                switch (pubEvent) {
                    case EVENT_CODE.EVENT_UNKNOWN:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                text.setText(text.getText().toString() + "\n返回状态未知");
                            }
                        });
                        break;
                    case EVENT_CODE.EVENT_CONNECTED:
                        // 连接成功
                        runOnUiThread(new Runnable() {
                            public void run() {
                                text.setText(text.getText().toString() + "\n连接成功");
                            }
                        });
                        break;
                    case EVENT_CODE.EVENT_CONNECT_FAILED:
                        // 连接失败
                        onRecordError();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                text.setText(text.getText().toString() + "\n连接失败");
                            }
                        });
                        break;
                    case EVENT_CODE.EVENT_DISCONNECTED:
                        // 连接断开
                        runOnUiThread(new Runnable() {
                            public void run() {
                                text.setText(text.getText().toString() + "\n连接断开");
                            }
                        });
                        break;
                    case EVENT_CODE.EVENT_PREPARE_FAILED:
                        // 获取SN与调度信息失败
                        onRecordError();
                        mSn = null;
                        isSnReturn = true;
                        synchronized (mObject) {
                            mObject.notify();
                            Logger.d(TAG, "mObject.notify()2");
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                text.setText(text.getText().toString()
                                        + "\n获取的sn失败");
                            }
                        });
                        break;
                    case EVENT_CODE.EVENT_STREAM_CONNECT_FAIL:
                        // 开流失败
                        onRecordError();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                text.setText(text.getText().toString() + "\n开流失败");
                            }
                        });
                        break;
                    case EVENT_CODE.EVENT_STREAM_CONNECT:
                        // 开流完成
                        onLiveSuccess(mSn);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                text.setText(text.getText().toString() + "\n开流完成");
                            }
                        });
                        break;
                }
            }
        });
        Logger.e(TAG, "createSession#id= " + id);
        id = mCloudRecorder.prepare();// 获取sn
        Logger.e(TAG, "prepare#id = " + id);
        doRecord();
    }

    public void doRecord() {
        if(mCloudRecorder==null){
            return;
        }
        mCloudRecorder.setInput(0, mCamera, getMediaSettings());

        // 如果sn没有返回，则等待返回后再开流
        new Thread() {
            public void run() {
                if (isSnReturn) {
                    Logger.d(TAG, "isSnReturn : " + isSnReturn);
                    if (!TextUtils.isEmpty(mSn)) {
                        if (mCloudRecorder != null) {
                            Logger.d(TAG, "LiveCloudRecorder start");
                            mCloudRecorder.start();
                        }
                    }
                } else {
                    synchronized (mObject) {

                        try {
                            long time = System.currentTimeMillis();
                            Logger.d(TAG, "mObject wait start");
                            mObject.wait();
                            Logger.d(
                                    TAG,
                                    "mObject wait end, cost : "
                                            + (System.currentTimeMillis() - time));
                            if (!TextUtils.isEmpty(mSn)) {
                                if (mCloudRecorder != null) {
                                    Logger.d(TAG, "LiveCloudRecorder start");
                                    mCloudRecorder.start();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }.start();
    }

    protected void onPreviewFrame(byte[] data, String sn) {

    }

    protected void onRecordError() {

    }
}