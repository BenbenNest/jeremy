package com.jeremy.library.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by benbennest on 17/2/14.
 */

public class FetchImageUtils {
    private static final int CAMERA_CROP_WITH_DATA = 3025;
    private static final int CAMERA_WITH_DATA = 3024;
    private static final int PHOTO_CROP_PHOTO_WITH_DATA = 3023;
    private static final int PHOTO_PICK_PHOTO_WITH_DATA = 3022;
    private static final int PHOTO_ONLY_PICKED_WITH_DATA = 3021;
    private static int DEFAULT_IMAGE_SIZE = 640;
    private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private Activity mActivity;
    private File mCurrentPhotoFile;
    private FetchImageUtils.OnPickFinishedCallback callback;
    private int aspectX = 1;
    private int aspectY = 1;
    Uri tempPhotoUri;

    public FetchImageUtils(Activity activity) {
        this.mActivity = activity;
    }

    public void setAspectX(int aspectX) {
        this.aspectX = aspectX;
    }

    public void setAspectY(int aspectY) {
        this.aspectY = aspectY;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && data.getData() != null) {
            Log.d("hui", "data = " + data.getData().toString());
        } else {
            Log.d("hui", "data = " + null);
        }

        switch(requestCode) {
            case 3021:
                if(0 == resultCode) {
                    if(this.callback != null) {
                        this.callback.onPickCancel();
                    }
                } else {
                    if(data == null) {
                        if(this.callback != null) {
                            this.callback.onPickFailed();
                        }

                        return;
                    }

                    Uri uri = data.getData();
                    if(this.callback != null) {
                        if(uri == null) {
                            this.callback.onPickFailed();
                        } else {
                            this.callback.onPickSuccessed(uri);
                        }
                    }
                }
                break;
            case 3022:
                if(0 == resultCode) {
                    if(this.callback != null) {
                        this.callback.onPickCancel();
                    }
                } else {
                    this.mActivity.startActivityForResult(this.getCameraCropImageIntent(data.getData()), 3023);
                }
                break;
            case 3023:
                if(0 == resultCode) {
                    if(this.callback != null) {
                        this.callback.onPickCancel();
                    }
                } else {
                    if(this.tempPhotoUri == null) {
                        if(this.callback != null) {
                            this.callback.onPickFailed();
                        }

                        return;
                    }

                    if(this.callback != null) {
                        if(this.tempPhotoUri == null) {
                            this.callback.onPickFailed();
                        } else {
                            this.callback.onPickSuccessed(this.tempPhotoUri);
                        }
                    }
                }
                break;
            case 3024:
                if(0 == resultCode) {
                    if(this.callback != null) {
                        this.callback.onPickCancel();
                    }
                } else {
                    this.doCameraPhoto(this.mCurrentPhotoFile);
                }
                break;
            case 3025:
                if(0 == resultCode) {
                    if(this.callback != null) {
                        this.callback.onPickCancel();
                    }
                } else {
                    this.doCameraCropPhoto(this.mCurrentPhotoFile);
                }
        }

    }

    private void doCameraCropPhoto(File f) {
        try {
            MediaScannerConnection.scanFile(this.mActivity, new String[]{f.getAbsolutePath()}, new String[]{null}, (MediaScannerConnection.OnScanCompletedListener)null);
            Intent e = this.getCameraCropImageIntent(Uri.fromFile(f));
            this.mActivity.startActivityForResult(e, 3023);
        } catch (Exception var3) {
            var3.printStackTrace();
            if(this.callback != null) {
                this.callback.onPickFailed();
            }
        }

    }

    private void doCameraPhoto(File f) {
        try {
            MediaScannerConnection.scanFile(this.mActivity, new String[]{f.getAbsolutePath()}, new String[]{null}, (MediaScannerConnection.OnScanCompletedListener)null);
            if(this.callback != null) {
                this.callback.onPickSuccessed(Uri.fromFile(f));
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            if(this.callback != null) {
                this.callback.onPickFailed();
            }
        }

    }

    private Intent getCameraCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", "true");
        intent.putExtra("aspectX", this.aspectX);
        intent.putExtra("aspectY", this.aspectY);
        intent.putExtra("outputX", DEFAULT_IMAGE_SIZE);
        intent.putExtra("outputY", DEFAULT_IMAGE_SIZE * this.aspectY / this.aspectX);
        intent.putExtra("return-data", false);
        intent.putExtra("output", this.getTempUri());
        return intent;
    }

    private Intent getPhotoPickIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT", (Uri)null);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        return intent;
    }

    public Intent getPickPhotoIntent() {
        Intent intent = new Intent("android.intent.action.PICK", (Uri)null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    public void doTakeCropPhotoFromCamera(FetchImageUtils.OnPickFinishedCallback callback) {
        this.callback = callback;

        try {
            PHOTO_DIR.mkdirs();
            this.mCurrentPhotoFile = new File(PHOTO_DIR, this.getPhotoFileName());
            Intent e = this.getTakePickIntent(this.mCurrentPhotoFile);
            this.mActivity.startActivityForResult(e, 3025);
        } catch (ActivityNotFoundException var3) {
            var3.printStackTrace();
            if(callback != null) {
                callback.onPickFailed();
            }
        }

    }

    public void doTakePhotoFromCamera(FetchImageUtils.OnPickFinishedCallback callback) {
        this.callback = callback;

        try {
            PHOTO_DIR.mkdirs();
            this.mCurrentPhotoFile = new File(PHOTO_DIR, this.getPhotoFileName());
            Intent e = this.getTakePickIntent(this.mCurrentPhotoFile);
            this.mActivity.startActivityForResult(e, 3024);
        } catch (ActivityNotFoundException var3) {
            var3.printStackTrace();
            if(callback != null) {
                callback.onPickFailed();
            }
        }

    }

    public void doPickPhotoFromGallery(FetchImageUtils.OnPickFinishedCallback callback) {
        this.callback = callback;

        try {
            Intent e = this.getPhotoPickIntent();
            this.mActivity.startActivityForResult(e, 3021);
        } catch (ActivityNotFoundException var3) {
            var3.printStackTrace();
            if(callback != null) {
                callback.onPickFailed();
            }
        }

    }

    public void doPickCropPhotoFromGallery(FetchImageUtils.OnPickFinishedCallback callback) {
        this.callback = callback;

        try {
            Intent e = this.getPickPhotoIntent();
            this.mActivity.startActivityForResult(e, 3022);
        } catch (ActivityNotFoundException var3) {
            var3.printStackTrace();
            if(callback != null) {
                callback.onPickFailed();
            }
        }

    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("\'IMG\'_yyyyMMdd_HHmmss", Locale.getDefault());
        return dateFormat.format(date) + ".jpg";
    }

    private Intent getTakePickIntent(File f) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE", (Uri)null);
        intent.putExtra("output", Uri.fromFile(f));
        return intent;
    }

    private Uri getTempUri() {
        this.tempPhotoUri = Uri.fromFile(this.getTempFile());
        return this.tempPhotoUri;
    }

    private File getTempFile() {
        if(this.isSDCARDMounted()) {
            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

            try {
                f.createNewFile();
            } catch (IOException var3) {
                ;
            }

            return f;
        } else {
            return null;
        }
    }

    private boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals("mounted");
    }

    public interface OnPickFinishedCallback {
        void onPickSuccessed(Uri var1);

        void onPickFailed();

        void onPickCancel();
    }
}