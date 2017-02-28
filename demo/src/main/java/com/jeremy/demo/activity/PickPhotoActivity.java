package com.jeremy.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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

public class PickPhotoActivity extends BasePickPhotoDialogActivity {

    /**
     * 知识点
     * 1. 问题描述：图片文件已改变，第二次调用ImageView.setImageURI时无法更新图片
     * 分析：setImageURI方法中对uri进行了缓存，由于第一次加载过了该uri的资源，即使该文件内容改变了，判断中仍然会使用之前加载的。
     * 有2种解决办法：
     * 1、使用不同的文件名（不同的URI）
     * 2、使用setImageBitmap的方式代替
     * Bitmap bmp;
     * try {
     * bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));
     * } catch (Exception e) {
     * <p>
     * }
     * iv.setImageBitmap(bmp);
     */

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


    @Override
    protected void pickPhotoSuccessed(Uri uri) {
        clearBitmap();
        if (uri != null) {
            try {
                mBitmap = BitmapUtil.getimage(this, uri);
            } catch (Exception e) {
                File dir = StorageUtils.getPhotoDir(this);
                File file = new File(dir, StorageUtils.getPhotoFileName(Constants.PHOTO_TYPE_HEAD));
                FetchImageUtils fetchImageUtils = new FetchImageUtils(this);
                fetchImageUtils.doCameraCropPhoto(file);
            }
            if (mBitmap != null) {
                performAction();
            } else {
                Toast.makeText(this, R.string.errcode_take_photo, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.errcode_take_photo, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearBitmap() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }

    private void performAction() {
        ivAvatar.setImageBitmap(mBitmap);
//        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                InputStream mInputStream = BitmapUtils.BitmapToInputStream(mBitmap);
//                Message m = new Message();
//                m.what = HANLDER_TPYE_UPLOAD_PHOTO;
//                mHandler.sendMessage(m);
            }
        }).start();
    }

    @OnClick({R.id.result_image, R.id.iv_avatar})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.result_image:
            case R.id.iv_avatar:
                showPickPhotoDialog(Constants.PHOTO_TYPE_STORE_PHOTO, true);
//                Crop.pickImage(this);
                break;
        }

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
            //方法1
            Bitmap bmp;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
                resultView.setImageBitmap(bmp);
                ivAvatar.setImageBitmap(bmp);
            } catch (Exception e) {

            }
            //方法2
//            resultView.setImageURI(null);
//            ivAvatar.setImageURI(null);
//            resultView.setImageURI(Crop.getOutput(result));
//            ivAvatar.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
