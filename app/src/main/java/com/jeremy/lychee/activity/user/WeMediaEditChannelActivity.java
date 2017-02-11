package com.jeremy.lychee.activity.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.BlurUtil;
import com.jeremy.lychee.utils.DensityUtils;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.customview.user.EditChannelIcon;
import com.jeremy.lychee.widget.GlideTransforms.GlideCircleTransform;
import com.jeremy.lychee.widget.MaterialCheckBox;
import com.jeremy.lychee.widget.TransformDrawable;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ClickWatcher;
import com.jeremy.lychee.utils.ImageOptiUrl;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.statusbar.StatusBarUtil;
import com.jeremy.lychee.widget.GlideControl.GlideImageView;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeMediaEditChannelActivity extends SlidingActivity {

    @Bind(com.jeremy.lychee.R.id.channel_bg_image)
    GlideImageView mChannelBgImage;
    @Bind(com.jeremy.lychee.R.id.channel_icon)
    EditChannelIcon mChannelIcon;
    @Bind(com.jeremy.lychee.R.id.channel_name)
    TextView mChannelName;
    @Bind(com.jeremy.lychee.R.id.channel_describe)
    TextView mChannelDescribe;
    @Bind(com.jeremy.lychee.R.id.edit_channel_toolbar)
    Toolbar mEditChannelToolbar;
    @Bind(com.jeremy.lychee.R.id.channel_name_editor)
    EditText mChannelNameEditor;
    @Bind(com.jeremy.lychee.R.id.channel_describe_editor)
    EditText mChannelDescribeEditor;
    @Bind(com.jeremy.lychee.R.id.public_checkbox)
    MaterialCheckBox mPublicCheck;

    private final String CROP_ACTION = "com.android.camera.action.CROP";
    private final String CROP = "crop";
    private final String SCALE = "scale";
    private final String TRUE_STRING = "true";
    private final String ASPECT_X = "aspectX";
    private final String ASPECT_Y = "aspectY";
    private final String OUTPUT_X = "outputX";
    private final String OUTPUT_Y = "outputY";
    private final String RETURN_DATA = "return-data";
    private final String EXTEND_NAME = ".jpg";
    private final String OUTPUT_FORMAT = "outputFormat";
    private final String NO_FACE_DETEC = "noFaceDetection";

    private final int CHANNEL_ICON_ASPECTX = 1;
    private final int CHANNEL_ICON_ASPECTY = 1;
    private final int CHANNEL_ICON_OUTPUTX = 100;
    private final int CHANNEL_ICON_OUTPUTY = 100;
    private final int CHANNEL_BG_IMG_ASPECTX = 3;
    private final int CHANNEL_BG_IMG_ASPECTY = 2;
    private final int CHANNEL_BG_IMG_OUTPUTX = 900;
    private final int CHANNEL_BG_IMG_OUTPUTY = 600;

    private static final String INTENT_EXTRA_KEY_PICK_TYPE = "pick_type";
    private static final String INTENT_EXTRA_KEY_CHANNEL_BG = "channel_bg";
    private static final String INTENT_EXTRA_KEY_CHANNEL_IC = "channel_ic";
    private static final String INTENT_EXTRA_KEY_CHANNEL_ID = "channel_id";
    private static final String INTENT_EXTRA_KEY_CHANNEL_NAME = "channel_name";
    private static final String INTENT_EXTRA_KEY_CHANNEL_DESCRIBE = "channel_describe";
    private static final String INTENT_EXTRA_KEY_CHANNEL_IS_PUBLIC = "channel_is_public";
    private static final String TEMP_LOCATION = Constants.APP_ROOT_PATH + "tmp/";
    private static final String CROP_TEMP_LOCATION = TEMP_LOCATION + "crop.jpg";

    private String mTempPictureName;
    private String mChannelId;
    private String mBgUri;
    private String mIcUri;
    private Bitmap mBgBmp;
    private Bitmap mIcBmp;
    private Uri mTmpFileUri;

    //avoid clicking twice quickly
    private ClickWatcher mClickWatcher =
            new ClickWatcher(Constants.TWICE_CLICK_MIN_INTERVAL);

    private volatile boolean mIsContentChanged = false;

    public static void startActivity(Context context,
                                     String bgImg,
                                     String icon,
                                     String id,
                                     String name,
                                     String desc,
                                     int pub) {
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_EXTRA_KEY_CHANNEL_BG, bgImg);
        bundle.putString(INTENT_EXTRA_KEY_CHANNEL_IC, icon);
        bundle.putString(INTENT_EXTRA_KEY_CHANNEL_ID, id);
        bundle.putString(INTENT_EXTRA_KEY_CHANNEL_NAME, name);
        bundle.putString(INTENT_EXTRA_KEY_CHANNEL_DESCRIBE, desc);
        bundle.putInt(INTENT_EXTRA_KEY_CHANNEL_IS_PUBLIC, pub);
//        ((SlidingActivity)context).openActivity(EditMediaChannelActivity.class, bundle, 0);
        Intent intent = new Intent(context, WeMediaEditChannelActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_edit_media_channel);
        ButterKnife.bind(this);
        mChannelIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mChannelBgImage.setScaleType(ImageView.ScaleType.FIT_XY);
        mBgUri = getIntent().getStringExtra(INTENT_EXTRA_KEY_CHANNEL_BG);
        mIcUri = getIntent().getStringExtra(INTENT_EXTRA_KEY_CHANNEL_IC);
        mChannelId = getIntent().getStringExtra(INTENT_EXTRA_KEY_CHANNEL_ID);
        mPublicCheck.setChecked(
                getIntent().getIntExtra(INTENT_EXTRA_KEY_CHANNEL_IS_PUBLIC, 0) == 0, true);
        mChannelName.setText(getIntent().getStringExtra(INTENT_EXTRA_KEY_CHANNEL_NAME));
        mChannelNameEditor.setText(mChannelName.getText().toString().trim());
        mChannelDescribe.setText(getIntent().getStringExtra(INTENT_EXTRA_KEY_CHANNEL_DESCRIBE));
        mChannelDescribeEditor.setText(mChannelDescribe.getText().toString().trim());

        mChannelNameEditor.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Constants.MEDIA_CHANNEL_NAME_MAX_LENGTH)});
        mChannelDescribeEditor.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Constants.MEDIA_CHANNLE_DESCRIBE_LENGTH)});
        mChannelNameEditor.setSelection(mChannelNameEditor.getText().length());
        mChannelDescribeEditor.setSelection(mChannelDescribeEditor.getText().length());
        mPublicCheck.setOncheckListener((s1, s2) -> mIsContentChanged = true);

        initToolBar();
        initImage();
        initMenuListener();

    }

    private void initToolBar() {
        mEditChannelToolbar.inflateMenu(com.jeremy.lychee.R.menu.menu_channel_edit_confirm);
        mEditChannelToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initImage() {
        if (!TextUtils.isEmpty(mBgUri)) {
            mChannelBgImage.loadImage(mBgUri, (req, v) -> req
                    .placeholder(com.jeremy.lychee.R.drawable.topic_head_backimage)
                    .error(com.jeremy.lychee.R.drawable.topic_head_backimage)
                    .centerCrop()
                    .crossFade()
                    .into(v));
        }
        if (!TextUtils.isEmpty(mIcUri)) {
            Glide.with(this)
                    .load(ImageOptiUrl.get(mIcUri, mChannelIcon))
                    .asBitmap()
                    .placeholder(AppUtil.getDefaultCircleIcon(this))
                    .transform(new GlideCircleTransform(this))
                    .into(mChannelIcon);
        }
    }

    private void initMenuListener() {
        findViewById(com.jeremy.lychee.R.id.menu_item_edit_channel).setOnClickListener(
                e -> mClickWatcher.doClick(this::submitMediaChannelInfo)
        );
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.NONE) {
            if (mTempPictureName != null) {
                File file = new File(mTempPictureName);
                if (file.exists()) {
                    runMediaScanner();
                }
            }
            return;
        }
        Uri uri = null;
        switch (requestCode) {
            case Constants.CAMERA_CHANNEL_IC:
            case Constants.CAMERA_CHANNEL_BG:
                if (!TextUtils.isEmpty(mTempPictureName)) {
                    uri = Uri.fromFile(new File(mTempPictureName));
                }
                break;
            case Constants.GALLERY_CHANNEL_IC:
            case Constants.GALLERY_CHANNEL_BG:
                if (data != null) {
                    uri = data.getData();
                }
                break;
        }
        switch (requestCode) {
            case Constants.CAMERA_CHANNEL_IC:
            case Constants.GALLERY_CHANNEL_IC:
                startPhotoCrop(uri, Constants.PICK_CHANNEL_IC);
                return;
            case Constants.CAMERA_CHANNEL_BG:
            case Constants.GALLERY_CHANNEL_BG:
                startPhotoCrop(uri, Constants.PICK_CHANNEL_BG);
                return;
        }
        if (data != null) {
            Bitmap photo = null;
            try {
                photo = BitmapFactory.decodeStream(
                        this.getContentResolver().openInputStream(mTmpFileUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photo != null) {
                if (requestCode == Constants.CHANNEL_BG_CROPED) {
                    //背景高斯模糊
                    mChannelBgImage.setImageBitmap(photo);
                    try {
                        mBgBmp = BlurUtil.blur(this, photo);
                    } catch (OutOfMemoryError err) {
                        mBgBmp = photo;
                    }
                    mChannelBgImage.setImageBitmap(mBgBmp);
                } else if (requestCode == Constants.CHANNEL_IC_CROPED) {
                    mChannelIcon.setImageDrawable(
                            new TransformDrawable(photo,
                                    DensityUtils.dip2px(this, 4),
                                    TransformDrawable.TRANSFORM_CIRCLE));
                    mIcBmp = photo;
                }
                mIsContentChanged = true;
                deleteTempFiles();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void deleteTempFiles() {
        File file = new File(CROP_TEMP_LOCATION);
        if (file.exists()) {
            file.delete();
        }
        if (mTempPictureName != null) {
            file = new File(mTempPictureName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private void startPhotoCrop(Uri uri, int type) {
        if (uri == null) return;
        Intent intent = new Intent(CROP_ACTION);
        intent.setDataAndType(uri, Constants.IMAGE_UNSPECIFIED);
        intent.putExtra(CROP, TRUE_STRING);
        intent.putExtra(SCALE, true);//长宽比1:1
        intent.putExtra(NO_FACE_DETEC, true);//禁用人脸识别
        intent.putExtra(OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(RETURN_DATA, false);
        mTmpFileUri = Uri.parse("file:///" + CROP_TEMP_LOCATION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTmpFileUri);
        switch (type) {
            case Constants.PICK_CHANNEL_IC:
                intent.putExtra(ASPECT_X, CHANNEL_ICON_ASPECTX);
                intent.putExtra(ASPECT_Y, CHANNEL_ICON_ASPECTY);
                intent.putExtra(OUTPUT_X, CHANNEL_ICON_OUTPUTX);
                intent.putExtra(OUTPUT_Y, CHANNEL_ICON_OUTPUTY);
                startActivityForResult(intent, Constants.CHANNEL_IC_CROPED);
                return;
            case Constants.PICK_CHANNEL_BG:
                intent.putExtra(ASPECT_X, CHANNEL_BG_IMG_ASPECTX);
                intent.putExtra(ASPECT_Y, CHANNEL_BG_IMG_ASPECTY);
                intent.putExtra(OUTPUT_X, CHANNEL_BG_IMG_OUTPUTX);
                intent.putExtra(OUTPUT_Y, CHANNEL_BG_IMG_OUTPUTY);
                startActivityForResult(intent, Constants.CHANNEL_BG_CROPED);
        }
    }

    private void runMediaScanner() {
        String[] path = new String[]{mTempPictureName};
        String[] mimeType = new String[]{"image/jpeg"};
        MediaScannerConnection.scanFile(this, path, mimeType, null);
    }

    @OnTextChanged(value = com.jeremy.lychee.R.id.channel_name_editor,
            callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onEditChannelName(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(mChannelName.getText())) {
            mChannelName.setText(s.toString().trim());
            mIsContentChanged = true;
        }
    }

    @OnTextChanged(value = com.jeremy.lychee.R.id.channel_describe_editor,
            callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onEditChannelDescribe(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(mChannelDescribe.getText())) {
            mChannelDescribe.setText(s.toString().trim());
            mIsContentChanged = true;
        }
    }

    @Deprecated
    @OnClick(com.jeremy.lychee.R.id.btn_delete_channel)
    void onClickDeleteChannel() {
        DialogUtil.showConfirmDialog(this, "确定要删除这个直播号吗？",
                getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                    dialog.dismiss();
                    deleteMediaChannel();
                }, getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss);

    }

    @OnClick(com.jeremy.lychee.R.id.channel_icon)
    void onClickChannelIcon(View v) {
        mClickWatcher.doClick(
                () -> doPickPicture(Constants.PICK_CHANNEL_IC));
    }

    @OnClick(com.jeremy.lychee.R.id.change_bg)
    void onClickChannelBgImage() {
        mClickWatcher.doClick(
                () -> doPickPicture(Constants.PICK_CHANNEL_BG));
    }

    @Override
    public void onBackPressed() {
        doExit();
    }

    private void doExit() {
        if (mIsContentChanged) {
            DialogUtil.showConfirmDialog(this, "确定要放弃修改吗？",
                    getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                dialog.dismiss();
                finish();
            }, getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss);
        } else {
            finish();
        }
    }

    private void doPickPicture(int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (type) {
            case Constants.PICK_CHANNEL_IC:
                builder.setTitle("设置直播号图标");
                break;
            case Constants.PICK_CHANNEL_BG:
                builder.setTitle("设置直播号背景");
                break;
        }
        builder.setItems(com.jeremy.lychee.R.array.pick_picture, (dialog, which) -> {
            Intent intent = null;
            //TODO(wangp):文件路径与其他统一
            File path = new File(TEMP_LOCATION);
            if (!path.exists()) {
                path.mkdirs();
            }

            switch (which) {
                case 0://从相册选取
                    intent = new Intent(Intent.ACTION_PICK, null);
                    intent.putExtra(INTENT_EXTRA_KEY_PICK_TYPE, type);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            Constants.IMAGE_UNSPECIFIED);
                    try {
                        switch (type) {
                            case Constants.PICK_CHANNEL_IC:
                                startActivityForResult(intent,
                                        Constants.GALLERY_CHANNEL_IC);
                                break;
                            case Constants.PICK_CHANNEL_BG:
                                startActivityForResult(intent,
                                        Constants.GALLERY_CHANNEL_BG);
                                break;
                        }
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(WeMediaEditChannelActivity.this,
                                "没有可以使用的相册", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1://拍摄照片
                    mTempPictureName = getTempPictureName(path);
                    File file = new File(mTempPictureName);
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(INTENT_EXTRA_KEY_PICK_TYPE, type);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    try {
                        switch (type) {
                            case Constants.PICK_CHANNEL_IC:
                                startActivityForResult(intent,
                                        Constants.CAMERA_CHANNEL_IC);
                                break;
                            case Constants.PICK_CHANNEL_BG:
                                startActivityForResult(intent,
                                        Constants.CAMERA_CHANNEL_BG);
                                break;
                        }
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(WeMediaEditChannelActivity.this,
                                "没有可以使用的照相机", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        });
        builder.create().show();
    }

    private String getTempPictureName(File directory) {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        String temporaryFileName = directory + "/" + format.format(date);
        File checkFile = new File(temporaryFileName + EXTEND_NAME);
        if (checkFile.exists()) {
            for (int n = 1; n < 100; n++) {
                checkFile = new File(temporaryFileName + "-" + n + EXTEND_NAME);
                if (!checkFile.exists()) {
                    temporaryFileName = temporaryFileName + "-" + n;
                    break;
                }
            }
        }
        return temporaryFileName + EXTEND_NAME;
    }

    @Deprecated
    private void deleteMediaChannel() {
        OldRetroAdapter.getService().deleteAlbum(mChannelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> {
                            if (s.getErrno() == 0) {
                                QEventBus.getEventBus().post(new Events.OnMediaChannelDelete());
                                QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
                                Toast.makeText(WeMediaEditChannelActivity.this, "频道删除成功",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(WeMediaEditChannelActivity.this, "频道删除失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        },
                        e -> {
                            e.printStackTrace();
                            Toast.makeText(WeMediaEditChannelActivity.this, "频道删除失败",
                                    Toast.LENGTH_SHORT).show();
                        });
    }

    static volatile boolean sflg_ = true;

    private void submitMediaChannelInfo() {
        String name = mChannelNameEditor.getText().toString().trim();
        String describe = mChannelDescribeEditor.getText().toString().trim();
        //判空
        if (TextUtils.isEmpty(name)) {
            ToastHelper.getInstance(this).toast("频道名不能为空");
            return;
        }
        if (TextUtils.isEmpty(describe)) {
            ToastHelper.getInstance(this).toast("频道描述不能为空");
            return;
        }

        synchronized (WeMediaEditChannelActivity.this) {
            sflg_ = true;
        }
        WeakReference<ProgressDialog> dialog =
                new WeakReference<ProgressDialog>(new ProgressDialog(WeMediaEditChannelActivity.this));
        dialog.get().setIndeterminate(true);
        dialog.get().setMessage("频道更新中...");
        dialog.get().setCancelable(false);
        if (sflg_ && dialog.get() != null) {
            dialog.get().show();
        }

        //上传数据
        OldRetroAdapter.getService().editUserChannel(mChannelId, name, describe,
                mBgBmp == null ? "" : AppUtil.convertBitmapToString(mBgBmp),
                mIcBmp == null ? "" : AppUtil.convertBitmapToString(mIcBmp),
                mPublicCheck.isChecked() ? 0 : 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .flatMap(s -> {
                    if (s.getErrno() != 0) {
                        return Observable.just(s.getErrmsg());
                    } else {
                        return Observable.just(s.getData());
                    }
                })
                .subscribe(
                        s -> {
                            if (s instanceof String) {
                                ToastHelper.getInstance(this).toast((String)s);
                            } else {
                                ToastHelper.getInstance(this).toast("编辑更新成功");
                                QEventBus.getEventBus().post(new Events.OnWeMediaChannelInfoUpdated());
                                finish();
                            }
                            dismissProgressDialog(dialog);
                        }, e -> {
                            ToastHelper.getInstance(this).toast("编辑更新失败");
                            dismissProgressDialog(dialog);
                        }, () -> dismissProgressDialog(dialog)
                );

    }

    private void dismissProgressDialog(WeakReference<ProgressDialog> dialog) {
        synchronized (WeMediaEditChannelActivity.this) {
            sflg_ = false;
            if (dialog.get() != null && dialog.get().isShowing())
                dialog.get().dismiss();
        }
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏沉浸
            setFitsSystemWindows(false);
            setStatusBarTransparent(true);
            int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
            mEditChannelToolbar.setPadding(0, statusBarHeight, 0, 0);
            ViewGroup.LayoutParams params = mEditChannelToolbar.getLayoutParams();
            params.height += statusBarHeight;
            mEditChannelToolbar.setLayoutParams(params);
        }
    }
}
