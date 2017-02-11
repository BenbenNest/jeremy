package com.jeremy.lychee.manager.live;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class CameraManager {
    public static CameraManager createInstance() {
        return new CameraManager();
    }

    private CameraManager() {
    }

    private int frontCameraId = -1;
    private int backCameraId = -1;
    private Map<Integer, Camera.CameraInfo> cameraInfoMap = new HashMap<>(2);
    private CameraWrap currentCamera;

    public void init() {
        initCameraInfo();
    }

    public void unInit() {
        if (currentCamera != null) {
            currentCamera.close();
            currentCamera = null;
        }
        cameraInfoMap.clear();
    }

    private void initCameraInfo() {
        try {
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    frontCameraId = i;
                    cameraInfoMap.put(frontCameraId, cameraInfo);
                } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    backCameraId = i;
                    cameraInfoMap.put(backCameraId, cameraInfo);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public int getFrontCameraId() {
        return frontCameraId;
    }

    public int getBackCameraId() {
        return backCameraId;
    }

    private CameraWrap setCurrentCamera(CameraWrap camera) {
        this.currentCamera = camera;
        return this.currentCamera;
    }

    public CameraWrap getCurrentCamera() {
        return currentCamera;
    }

    public void toggleCamera() {
        if (getCurrentCamera() == null) {
            return;
        }

        try {
            getCurrentCamera().close();
            setCurrentCamera(new CameraWrap(getCurrentCamera().getCameraId() == getFrontCameraId() ? getBackCameraId() : getFrontCameraId()).inherit(getCurrentCamera()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public final CameraWrap open(int cameraId) {
        if (getCurrentCamera() != null) {
            getCurrentCamera().close();
        }

        try {
            CameraWrap cameraWrap = new CameraWrap(cameraId);
            cameraWrap.getRawCamera().getParameters();
            return setCurrentCamera(cameraWrap);
        } catch (Throwable e) {
            return setCurrentCamera(null);
        }
    }

    @SuppressWarnings("unused")
    public class CameraWrap {
        private Camera camera;
        private int id;
        private boolean isLightOn = false;
        private Activity previewActivity;
        private SurfaceHolder previewHolder;
        private boolean hasError = false;

        private CameraWrap(int id) {
            this.camera = Camera.open(id);
            this.id = id;

            if (camera == null) {
                hasError = true;
                return;
            }

            try {
                Camera.Parameters parameters = camera.getParameters();
                // 设置多媒体录制，会加快打开速度，但是在联想a820上会导致黑屏,小米4上会导致卡死,魅族mx4 pro前置摄像头花屏
                /* parameters.setRecordingHint(true); */
                if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                /*
                 * if(Build.VERSION.SDK_INT > 14){
                 * //设置防抖，但据有人说在小米4上会造成画面抖动，但我在一个小米4上未复现，为了稳定，暂时不加
                 * if(parameters.isVideoStabilizationSupported())
                 * parameters.setVideoStabilization(true); }
                 */
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setPreviewSize(1280, 720);
                camera.setParameters(parameters);
            } catch (Throwable e) {
                hasError = true;
                e.printStackTrace();
            }
        }

        public Camera getRawCamera() {
            return camera;
        }

        public int getCameraId() {
            return id;
        }

        public boolean hasError() {
            return hasError;
        }

        public void close() {
            if (camera == null) {
                return;
            }

            try {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.lock();
                camera.release();
                camera = null;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public CameraWrap inherit(CameraWrap camera) {
            previewActivity = camera.previewActivity;
            previewHolder = camera.previewHolder;
            isLightOn = camera.isLightOn;
            camera.previewActivity = null;
            camera.previewHolder = null;

            startPreview(previewActivity, previewHolder);
            toggleFlashLight(isLightOn);
            return this;
        }

        public boolean startPreview(Activity activity, SurfaceHolder holder) {
            if (camera == null) {
                return false;
            }

            try {
                camera.setDisplayOrientation(getCameraDisplayOrientation(activity));
                camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewActivity = activity;
                previewHolder = holder;
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                hasError = true;
                return false;
            }
        }

        public boolean toggleFlashLight(boolean isLightOn) {
            if (camera == null) {
                return false;
            }

            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(isLightOn ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.startPreview();
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean toggleFlashLight() {
            isLightOn = !isLightOn;
            return toggleFlashLight(isLightOn);
        }

        public boolean isFlashLightOn() {
            return isLightOn;
        }

        public int getCameraDisplayOrientation(final Activity activity) {
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
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

            int result;
            Camera.CameraInfo cameraInfo = cameraInfoMap.get(id);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (cameraInfo.orientation + degrees) % 360;
                result = (360 - result) % 360;
            } else { // back-facing
                result = (cameraInfo.orientation - degrees + 360) % 360;
            }
            return result;
        }

        public boolean saveThumbnail(byte[] bytes, int width, int height, String thumbnailFile) {
            try {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, width, height, null);
                yuvImage.compressToJpeg(new Rect(0, 0, width, height), 75, outputStream);

                Matrix matrix = new Matrix();
                matrix.postRotate(cameraInfoMap.get(id).orientation);
                byte[] outputBytes = outputStream.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(outputBytes, 0, outputBytes.length);
                bitmap = Bitmap.createBitmap(bitmap, 0 , 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                FileOutputStream fileOutputStream = new FileOutputStream(thumbnailFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fileOutputStream);

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
