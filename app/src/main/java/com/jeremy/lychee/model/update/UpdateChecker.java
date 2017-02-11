package com.jeremy.lychee.model.update;

import android.app.Activity;

import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UpdateChecker {

    private Activity mActivity;
    private boolean isToast;

    public UpdateChecker(Activity activity) {
        mActivity = activity;
    }

    public UpdateChecker(Activity activity, boolean isToast) {
        mActivity = activity;
        this.isToast = isToast;
    }


    public void startCheck() {
        OldRetroAdapter.getService().checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ModelBase<UpdateInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isToast) {
                            ToastHelper.getInstance(mActivity).toast(com.jeremy.lychee.R.string.net_error);
                        }
                    }

                    @Override
                    public void onNext(ModelBase<UpdateInfo> model) {
                        if (model != null && model.getErrno() == 0 && mActivity != null && !mActivity.isFinishing()) {

                            UpdateInfo updateInfo = model.getData();
                            int versionCode = AppUtil.getVersionCode(mActivity);
                            // 需要更新
                            if (versionCode >= 0 && updateInfo.version_code > versionCode) {
//								SharePreferenceUtil.saveUpdateInfo(mActivity, info);
                                UpdateApkHelper updateApk = new UpdateApkHelper(mActivity, updateInfo);
                                updateApk.confirmUpdate(updateInfo.force_update);
                            } else {
                                if (isToast) {
                                    ToastHelper.getInstance(mActivity).toast(com.jeremy.lychee.R.string.is_the_latest_version);
                                }
                            }

                            /*UpdateInfo updateInfo = model.getData();
                            updateInfo.update_content = new ArrayList<>();
                            updateInfo.update_msg = "2.0重磅来袭";
                            updateInfo.update_content.add("久久时间网，您的时间助手。久久时间网，您的时间助");
                            updateInfo.update_content.add("久久时间网，您的时间助手。");
                            updateInfo.update_content.add("久久时间网，您的时间助手。");
                            updateInfo.update_content.add("久久时间网，您的时间助手。");
                            updateInfo.update_content.add("久久时间网，您的时间助手。");
                            UpdateApkHelper updateApk = new UpdateApkHelper(mActivity, updateInfo);
                            updateApk.confirmUpdate(updateInfo.force_update);*/
                        }
                    }
                });
    }


}
