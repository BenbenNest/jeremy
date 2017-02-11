package com.jeremy.lychee.fragment.live;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.jeremy.lychee.activity.user.LoginActivity;
import com.jeremy.lychee.base.BaseFragment;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.live.LiveEvent;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.manager.QhLocationManager;
import com.jeremy.lychee.manager.live.LiveRecordingManager;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.qihu.mobile.lbs.location.QHLocation;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LiveShortVideoRecordedFragment extends BaseFragment {
    public static final String BUNDLE_VIDEO_PATH = "VideoPath";
    public static final String BUNDLE_VIDEO_LENGTH = "VideoLength";
    public static final String BUNDLE_THUMBNAIL_FILE = "ThumbnailFile";

    @Bind(com.jeremy.lychee.R.id.title_edit)
    EditText titleEdit;
    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar toolbar;

    @Bind(com.jeremy.lychee.R.id.desc_edit)
    EditText descEdit;

    private String videoPath;
    private int videoLength;
    private String thumbnailFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoPath = getArguments().getString(BUNDLE_VIDEO_PATH);
            videoLength = getArguments().getInt(BUNDLE_VIDEO_LENGTH);
            thumbnailFile = getArguments().getString(BUNDLE_THUMBNAIL_FILE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.jeremy.lychee.R.layout.fragment_live_short_video_recorded, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initUI() {
        toolbar.setTitle("发布短视频");
        toolbar.setNavigationOnClickListener(v1 -> onLeftButtonClicked());
        toolbar.inflateMenu(com.jeremy.lychee.R.menu.menu_publish);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == com.jeremy.lychee.R.id.menu_publish) {
                publishVideo();
                return true;
            }
            return false;
        });
    }

    protected void onLeftButtonClicked() {
        DialogUtil.showConfirmDialog(getActivity(), "是否放弃之前录制的视频内容并重新开始录制？",
                getString(com.jeremy.lychee.R.string.dialog_button_confirm), dialog -> {
            deleteTempFile();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(com.jeremy.lychee.R.id.root, LiveShortVideoRecordingFragment.instantiate(getActivity(), LiveShortVideoRecordingFragment.class.getName()))
                    .commit();
            AppUtil.hideSoft(getActivity());
            dialog.dismiss();
        }, getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss);
    }

    @Override
    public boolean onBackPressed() {
        onLeftButtonClicked();
        return true;
    }

    private String getTitle() {
        Editable editable = titleEdit.getText();
        if (editable == null) {
            return "";
        }
        return editable.toString();
    }

    private String getDescription() {
        Editable editable = descEdit.getText();
        if (editable == null) {
            return "";
        }
        return editable.toString();
    }

    private void publishVideo() {
        if (!isLogin()) {
            login(getActivity());
            return;
        }

        if (getTitle().trim().length() == 0) {
            Toast.makeText(getActivity(), "短视频标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        WeakReference<ProgressDialog> dialog = new WeakReference<>(new ProgressDialog(getContext()));
        dialog.get().setIndeterminate(true);
        dialog.get().setMessage("正在上传视频...");
        dialog.get().setCancelable(false);
        dialog.get().show();
        QHLocation location = QhLocationManager.getInstance().getLocation();
        float lat = 0.0f;
        float lon = 0.0f;
        String address = "";
        if (location != null) {
            lat = (float) location.getLatitude();
            lon = (float) location.getLongitude();
            address = location.getAddrStr();
        }
        LiveRecordingManager.uploadVideo(getTitle(), getDescription(), videoLength, thumbnailFile, videoPath,
                lat, lon, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(it -> {
                    if (it.getErrno() != 0) throw new RuntimeException(it.getErrmsg());
                })
                .map(ModelBase::getData)
                .subscribe(it -> {
                    if (it.getBytes() != new File(videoPath).length()) {
                        throw new RuntimeException("数据长度异常");
                    }

                    if (dialog.get() != null) {
                        dialog.get().dismiss();
                    }

                    Toast.makeText(getActivity(), "视频上传成功", Toast.LENGTH_SHORT).show();
                    deleteTempFile();
                    getActivity().finish();
                    QEventBus.getEventBus().post(new LiveEvent.showIntroLive());
                }, e -> {
                    Toast.makeText(getActivity(), "视频上传失败, 请检查网络后重试！", Toast.LENGTH_SHORT).show();

                    if (dialog.get() != null) {
                        dialog.get().dismiss();
                    }
                });
    }

    private void deleteTempFile() {
        if (videoPath != null) {
            File videoFile = new File(videoPath);
            videoFile.delete();
        }

        if (thumbnailFile != null) {
            File thumbFile = new File(thumbnailFile);
            thumbFile.delete();
        }
    }

    public boolean isLogin() {
        return !Session.isUserInfoEmpty();
    }

    public void login(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
