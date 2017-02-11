
package com.jeremy.lychee.model.update;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jeremy.lychee.R;
import com.jeremy.lychee.activity.NotificationActivity;
import com.jeremy.lychee.base.ContentApplication;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.widget.shareWindow.DialogView;

import java.io.File;
import java.util.List;

public class UpdateApkHelper {

    private Activity mActivity;

    /* 下载包安装路径 */
    private String mSavePath;
    /* 下载包文件名 */
    private String mSaveFileName = "DailyDiscovery.apk";

    private UpdateInfo updateInfo;

    private NotificationManager mNotifyManager;
    private PendingIntent mPendingIntent;

    private Builder mBuilder;
    private boolean mConfirmForceUpdate;

    public UpdateApkHelper(final Activity activity, UpdateInfo info) {
        this.mActivity = activity;
        this.updateInfo = info;
        if (!TextUtils.isEmpty(info.apk_name)) {
            mSaveFileName = info.apk_name;
        }
        mSavePath = ContentApplication.getInstance().getExternalFilesDir(null) + "/apk/";

    }

    private void showNoticeDialogNew(final boolean forceUpdate) {
        if (mActivity.isFinishing() || updateInfo == null) {
            return;
        }
        View updateView = View.inflate(ContentApplication.getInstance(), R.layout.dialog_update_new2, null);
        final DialogView dialogView = new DialogView(mActivity, updateView);
        dialogView.setFullScreen(true);
        dialogView.setCanceledOnTouchOutside(false);
        if (!forceUpdate) {

            TextView notNow = (TextView) updateView.findViewById(R.id.tv_not_now);
            notNow.setVisibility(View.VISIBLE);
            notNow.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            notNow.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialogView.disMissDialog();
                    // showTip();
                    showDownloadNotification(false);
                }
            });
        }
        View btnUpdate = updateView.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mConfirmForceUpdate = true;
                dialogView.disMissDialog();
                // 没有SD卡的时候,不让下载
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    ToastHelper.getInstance(mActivity).toast("存储卡不可用");
                    return;
                }
                // if(存储空间太小)
                // showDownloadDialog();
                showDownloadNotification(true);
            }
        });

        dialogView.setOnDialogDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (forceUpdate && !mConfirmForceUpdate) {
//					SharePreferenceUtil.saveCheckUpdateHour(mActivity, 0);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        });

        TextView updateContent = (TextView) updateView.findViewById(R.id.tv_dialog_content);
        updateContent.setText(getUpdateContent());
        if (!mActivity.isFinishing()) {
            dialogView.showDialog();
        }
    }

    private String getUpdateContent() {
        if (updateInfo == null || updateInfo.update_content == null
                || updateInfo.update_content.size() == 0)
            return null;
        StringBuilder content = new StringBuilder();
        List<String> contents = updateInfo.update_content;
        if (contents != null) {
            for (int i = 0; i < contents.size(); i++) {
                content.append(i + 1).append(".");
                content.append(contents.get(i)).append("\n");
            }
        }
        if (content.length() > 0)
            content.deleteCharAt(content.length() - 1);
        return content.toString();
    }

    // 外部接口让主Activity调用
    public void confirmUpdate(int forceUpdate) {
//        showNoticeDialog(forceUpdate != 0);
        showNoticeDialogNew(forceUpdate != 0);
    }

    public void showDownloadNotification(boolean startDownload) {
        if (mActivity.isFinishing()) {
            return;
        }

        mNotifyManager = (NotificationManager) mActivity
                .getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(mActivity);

        Intent intent = new Intent(mActivity, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        if (startDownload) {
            downloadApk();
            mBuilder.setOngoing(true);
            mBuilder.setContentText("正在下载" + updateInfo.version_name + "版本,完成" + 0 + "%");
        } else {
            intent.putExtra(NotificationActivity.EXTRA_DOWNLOAD, updateInfo);
            mBuilder.setContentText("北京时间最新版本" + updateInfo.version_name + ",立即点击下载");
        }

        mPendingIntent = PendingIntent.getActivity(mActivity, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentTitle("北京时间版本更新")
                .setSmallIcon(R.mipmap.notification_icon)
                .setWhen(System.currentTimeMillis())
                .setTicker("北京时间版本更新")
                .setLargeIcon(BitmapFactory.decodeResource(mActivity.getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(mPendingIntent);
        mNotifyManager.notify(0, mBuilder.build());
    }

    private static boolean isDownLoading = false;

    /**
     * 下载apk
     */
    private void downloadApk() {
        if (isDownLoading)
            return;
        new Thread(new Runnable() {

            @Override
            public void run() {
                new SimpleDownloader().performDownload(updateInfo.apk_url, mSavePath
                                + mSaveFileName,
                        new SimpleDownloader.OnDownloadListener() {
                            int last;
                            long lastTime;

                            @Override
                            public void onSuccess() {
                                isDownLoading = false;
                                mBuilder.setContentText(updateInfo.version_name + ",下载完成")
                                        .setProgress(0, 0, false);
                                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                                mNotifyManager.notify(0, mBuilder.build());
                                installApk();
                                mNotifyManager.cancel(0);
                            }

                            @Override
                            public void onStart() {
                                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
                                mBuilder.setProgress(100, 0, false);
                                mBuilder.setContentText("正在下载" + updateInfo.version_name + "版本,完成" + 0 + "%");
                                mNotifyManager.notify(0, mBuilder.build());
                            }

                            @Override
                            public void onFailed() {
                                isDownLoading = false;
                                deleteErrApk();

                                mNotifyManager.cancel(0);
                                showDownloadNotification(false);
                                mBuilder.setProgress(100, 0, false);
                                mBuilder.setOngoing(false);
                                mBuilder.setContentText("下载失败,点击重新下载");

                                Intent intent = new Intent(mActivity, NotificationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra(NotificationActivity.EXTRA_DOWNLOAD, updateInfo);

                                mPendingIntent = PendingIntent.getActivity(mActivity, 1, intent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                mBuilder.setContentIntent(mPendingIntent);
                                mNotifyManager.notify(0, mBuilder.build());
                            }

                            @Override
                            public void onDownloading(int percent) {
                                if (percent > last) {
                                    long now = System.currentTimeMillis();
                                    if (now - lastTime > 1000 || percent == 100) {
                                        mBuilder.setProgress(100, percent, false);
                                        mNotifyManager.notify(0, mBuilder.build());
                                        mBuilder.setContentText("正在下载" + updateInfo.version_name + "版本,完成" + percent + "%");
                                        lastTime = now;
                                    }
                                    last = percent;
                                }
                            }

                            @Override
                            public void onCancle() {
                                isDownLoading = false;
                            }
                        });
            }
        }).start();
        isDownLoading = true;
    }

    /**
     * 删除未下载完的apk
     */
    private void deleteErrApk() {
        File apkfile = new File(mSavePath + mSaveFileName);
        if (!apkfile.exists()) {
            return;
        }
        // 路径为文件
        if (apkfile.isFile()) {
            apkfile.delete();
        }
    }

    /**
     * 安装apk
     */
    private void installApk() {
        String filePth = mSavePath + mSaveFileName;
        File apkfile = new File(filePth);
        if (!apkfile.exists()) {
            return;
        }

        /**
         * 检查APK签名
         */
        boolean variflied = AppUtil.checkPackageName(mActivity, filePth);
        if (variflied) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + apkfile.getAbsolutePath()),
                    "application/vnd.android.package-archive");
            mActivity.getApplicationContext().startActivity(intent);
        }
    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void showTip() {
//
//        try {
//            final WindowManager wm = (WindowManager) mActivity
//                    .getSystemService(Context.WINDOW_SERVICE);
//            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//            params.type = WindowManager.LayoutParams.TYPE_TOAST;
//            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            params.format = PixelFormat.TRANSLUCENT;
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            params.gravity = Gravity.TOP;
//
//            final TextView tv = new TextView(mActivity);
//            tv.setText(mActivity.getResources().getString(R.string.app_update_tip));
//            tv.setTextColor(Color.WHITE);
//            tv.setGravity(Gravity.CENTER);
//            int padding = mActivity.getResources().getDimensionPixelSize(
//                    R.dimen.update_tip_textview_padding);
//            tv.setPadding(padding, padding, padding, padding);
//            tv.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.top_in_ainm));
//            tv.setBackgroundResource(R.drawable.update_tip_bg);
//            wm.addView(tv, params);
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    try {
//                        tv.startAnimation(AnimationUtils.loadAnimation(mActivity,
//                                R.anim.top_out_ainm));
//                        wm.removeView(tv);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 3000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
