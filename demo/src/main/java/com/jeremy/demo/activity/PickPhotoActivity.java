package com.jeremy.demo.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

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

    @BindView(R.id.iv_avatar)
    CircleAvatarView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        ButterKnife.bind(this);
        mPhotoType = Constants.PHOTO_TYPE_HEAD;

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

    @OnClick(R.id.iv_avatar)
    public void onClick() {
        showPickPhotoDialog(Constants.PHOTO_TYPE_STORE_PHOTO, true);
    }
}
