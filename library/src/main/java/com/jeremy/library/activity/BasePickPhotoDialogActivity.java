package com.jeremy.library.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.jeremy.library.utils.FetchImageUtils;

public abstract class BasePickPhotoDialogActivity extends AppCompatActivity implements OnClickListener, FetchImageUtils.OnPickFinishedCallback {

    protected Bitmap mBitmap;
    private Dialog mPickPhotoDialog;
    private FetchImageUtils mImageUtil;
    private int mPhotoType;
    private boolean mIsCrop;

    protected abstract void pickPhotoSuccessed(Uri uri);

    @Override
    protected void onDestroy() {
        dismissPickPhotoDialog();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImageUtil != null) {
            mImageUtil.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPickSuccessed(Uri uri) {
        pickPhotoSuccessed(uri);
    }

    @Override
    public void onPickFailed() {
//        Toast.makeText(this, R.string.errcode_take_photo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPickCancel() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        if (R.id.tv_choose_frome_album == id) {
//            dismissPickPhotoDialog();
//            if (mIsCrop) {
//                mImageUtil.doPickCropPhotoFromGallery(this);
//            } else {
//                mImageUtil.doPickPhotoFromGallery(this);
//            }
//        } else if (R.id.tv_choose_frome_camera == id) {
//            dismissPickPhotoDialog();
//            if (mIsCrop) {
//                mImageUtil.doTakeCropPhotoFromCamera(this);
//            } else {
//                mImageUtil.doTakePhotoFromCamera(this);
//            }
//        }

    }

    private void dismissPickPhotoDialog() {
        if (mPickPhotoDialog != null && mPickPhotoDialog.isShowing()) {
            mPickPhotoDialog.dismiss();
        }
    }

    protected int getPhotoType() {
        return mPhotoType;
    }

    public void showPickPhotoDialog(int photoType, boolean isCrop) {
        showPickPhotoDialog(photoType, isCrop, 1, 1);
    }

    public void showPickPhotoDialog(int photoType, boolean isCrop, int aspectX, int aspectY) {
        if (isFinishing()) {
            return;
        }
        mIsCrop = isCrop;
        if (mImageUtil == null) {
            mImageUtil = new FetchImageUtils(this);
        }
        mImageUtil.setAspectX(aspectX);
        mImageUtil.setAspectY(aspectY);
        mPhotoType = photoType;
        if (mPickPhotoDialog == null) {
            mPickPhotoDialog = new Dialog(this);
//            View layout = getLayoutInflater().inflate(R.layout.dialog_pick_photo, null);
//            layout.findViewById(R.id.tv_choose_frome_album).setOnClickListener(this);
//            layout.findViewById(R.id.tv_choose_frome_camera).setOnClickListener(this);
//            mPickPhotoDialog.setContentView(layout);
        }
        if (!mPickPhotoDialog.isShowing()) {
            mPickPhotoDialog.show();
        }
    }

}
