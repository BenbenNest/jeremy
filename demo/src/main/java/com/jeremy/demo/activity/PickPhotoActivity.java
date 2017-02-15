package com.jeremy.demo.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeremy.demo.R;
import com.jeremy.library.activity.BasePickPhotoDialogActivity;
import com.jeremy.library.common.Constants;

public class PickPhotoActivity extends BasePickPhotoDialogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        showPickPhotoDialog(Constants.PHOTO_TYPE_STORE_PHOTO, true);
    }


    @Override
    protected void pickPhotoSuccessed(Uri uri) {

    }
}
