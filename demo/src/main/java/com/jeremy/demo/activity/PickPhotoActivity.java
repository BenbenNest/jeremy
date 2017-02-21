package com.jeremy.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.jeremy.crop.Crop;
import com.jeremy.demo.R;
import com.jeremy.library.activity.BasePickPhotoDialogActivity;
import com.jeremy.library.common.Constants;
import com.jeremy.library.utils.BitmapUtil;
import com.jeremy.library.utils.FetchImageUtils;
import com.jeremy.library.utils.StorageUtils;
import com.jeremy.library.widget.CircleAvatarView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickPhotoActivity extends Activity {

    @BindView(R.id.iv_avatar)
    CircleAvatarView ivAvatar;
    ImageView resultView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        ButterKnife.bind(this);
//        mPhotoType = Constants.PHOTO_TYPE_HEAD;
        resultView = (ImageView) findViewById(R.id.result_image);
    }


//    @Override
//    protected void pickPhotoSuccessed(Uri uri) {
//        clearBitmap();
//        if (uri != null) {
//            try {
//                mBitmap = BitmapUtil.getimage(this, uri);
//            } catch (Exception e) {
//                File dir = StorageUtils.getPhotoDir(this);
//                File file = new File(dir, StorageUtils.getPhotoFileName(Constants.PHOTO_TYPE_HEAD));
//                FetchImageUtils fetchImageUtils = new FetchImageUtils(this);
//                fetchImageUtils.doCameraCropPhoto(file);
//            }
//            if (mBitmap != null) {
//                performAction();
//            } else {
//                Toast.makeText(this, R.string.errcode_take_photo, Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, R.string.errcode_take_photo, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void clearBitmap() {
//        if (mBitmap != null && !mBitmap.isRecycled()) {
//            mBitmap.recycle();
//            mBitmap = null;
//            System.gc();
//        }
//    }
//
//    private void performAction() {
//        ivAvatar.setImageBitmap(mBitmap);
////        showProgressDialog();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                InputStream mInputStream = BitmapUtils.BitmapToInputStream(mBitmap);
////                Message m = new Message();
////                m.what = HANLDER_TPYE_UPLOAD_PHOTO;
////                mHandler.sendMessage(m);
//            }
//        }).start();
//    }

    @OnClick(R.id.result_image)
    public void onClick() {
//        showPickPhotoDialog(Constants.PHOTO_TYPE_STORE_PHOTO, true);
//        ivAvatar.setImageDrawable(null);
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
