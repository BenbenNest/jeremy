package com.jeremy.lychee.activity.user;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.db.WeMediaChannel;
import com.jeremy.lychee.eventbus.QEventBus;
import com.jeremy.lychee.eventbus.user.Events;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;
import com.jeremy.lychee.utils.ClickWatcher;
import com.jeremy.lychee.utils.ToastHelper;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeMediaCreateAlbumActivity extends SlidingActivity {

    EditText albumName, albumDescrip;

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_create_album);
        initToolbar();
        initView();
        initListener();
    }

    //avoid clicking twice quickly
    private ClickWatcher mClickWatcher =
            new ClickWatcher(Constants.TWICE_CLICK_MIN_INTERVAL);

    private void initListener() {
        findViewById(com.jeremy.lychee.R.id.menu_item_create_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickWatcher.doClick(() -> submitMediaChannelInfo());
            }
        });
    }


    @Override
    public void onBackPressed() {
        doExit();
    }

    private void doExit() {
        if (!TextUtils.isEmpty(albumName.getText().toString().trim())
                || !TextUtils.isEmpty(albumDescrip.getText().toString().trim())) {
            DialogUtil.showConfirmDialog(this, "确定要放弃创建新专辑吗？",
                    getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                        dialog.dismiss();
                        finish();
                    }, getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss);
        } else {
            finish();
        }
    }

    static volatile boolean sflg_ = true;
    private void submitMediaChannelInfo() {
        String name = albumName.getText().toString().trim();
        String describe = albumDescrip.getText().toString().trim();
        //判空
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(WeMediaCreateAlbumActivity.this, "专辑名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        synchronized (WeMediaCreateAlbumActivity.this) {
            sflg_ = true;
        }
        WeakReference<ProgressDialog> dialog = new WeakReference<ProgressDialog>(new ProgressDialog(WeMediaCreateAlbumActivity.this));
        dialog.get().setIndeterminate(true);
        dialog.get().setMessage("专辑创建中...");
        dialog.get().setCancelable(false);
        new Handler().postDelayed(() -> {
            if (sflg_ && dialog.get() != null) {
                dialog.get().show();
            }
        }, 1000);

        //上传数据
        OldRetroAdapter.getService().createMediaChannel(name, describe)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .flatMap(s -> {
                    if (s.getErrno() != 0) {
                        return Observable.just(s.getErrmsg());
                    } else {
                        //TODO：暂时禁止DB存储
//                        saveCreatedChannelToDb(s.getData());
                        return Observable.just(s.getData());
                    }
                })
                .subscribe(
                        s -> {
                            if ( s instanceof WeMediaChannel) {
                                QEventBus.getEventBus().post(new Events.OnAlbumListUpdated(null));
                                ToastHelper.getInstance(this).toast("创建成功");
                            }else if(s instanceof String){
                                ToastHelper.getInstance(this).toast((String)s);
                            }
                            dismissProgressDialog(dialog);
                            finish();
                        }, e -> {
                            dismissProgressDialog(dialog);
                            ToastHelper.getInstance(this).toast("创建失败");
                        }
                );
    }

    private void dismissProgressDialog(WeakReference<ProgressDialog> dialog) {
        synchronized (WeMediaCreateAlbumActivity.this) {
            sflg_ = false;
            if (dialog.get() != null && dialog.get().isShowing())
                dialog.get().dismiss();
        }
    }


    @Deprecated
    private void saveCreatedChannelToDb(WeMediaChannel data) {
        try {
            data.setIs_my(true);
            ContentApplication.getDaoSession().getWeMediaChannelDao().insertOrReplace(data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        albumName = (EditText) findViewById(com.jeremy.lychee.R.id.create_album_name);
        albumDescrip = (EditText) findViewById(com.jeremy.lychee.R.id.create_album_describe);

    }


    private void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.create_channel_toolbar);
        configToolbar(com.jeremy.lychee.R.menu.menu_channel_submit, com.jeremy.lychee.R.string.title_activity_create_album);

    }


    public Toolbar configToolbar(int menuId, int titleId) {
        Toolbar toolbar = (Toolbar) findViewById(com.jeremy.lychee.R.id.toolbar);
        if (menuId != 0) {
            toolbar.inflateMenu(menuId);
        }
        if (titleId != 0) {
            toolbar.setTitle(titleId);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        return toolbar;
    }

}
