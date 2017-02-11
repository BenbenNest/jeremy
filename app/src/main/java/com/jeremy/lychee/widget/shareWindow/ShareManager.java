package com.jeremy.lychee.widget.shareWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.net.OldRetroAdapter;
import com.jeremy.lychee.share.weibo.WBShareManager;
import com.jeremy.lychee.share.wxapi.WXShareManager;
import com.jeremy.lychee.utils.FileUtil;
import com.jeremy.lychee.videoplayer.VideoPlayer;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.model.news.Result;
import com.jeremy.lychee.net.OldRetroApiService;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ImageOptiUrl;
import com.jeremy.lychee.utils.ImageToSdcard;
import com.jeremy.lychee.utils.ThreadUtils;
import com.jeremy.lychee.utils.ToastHelper;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/*
 * 分享模块管理类
 */
public class ShareManager implements OnClickListener {
    private DialogView mDialogView;
    private Activity mActivity;
    private Bitmap mShareBm;
    private ShareInfo mShareInfo;

    public final static String TITLE_LEFT = "【";
    public final static String TITLE_RIGHT = "】";
    public final static String NEWS_FROM = "（分享自@北京时间）。";
    public final static String DOWNLOAD = "下载：";
    public final static String DOWNLOAD_URL = "http://kandian.360.cn";

    private static final String SHARE_IMG_NAME = "discovery_share_image.jpg";

    private Action0 mCb = null;

    public ShareManager(Activity activity, final ShareInfo share, boolean isFromNet, Action0 cb) {
        this.mActivity = activity;
        this.mShareInfo = share;
        if (mShareInfo.getTitle() == null) {
            mShareInfo.setTitle("");
        }
        if (mShareInfo.getContent() == null) {
            mShareInfo.setContent("");
        }

        this.mCb = cb;
        if (isFromNet) {
            new Thread() {
                public void run() {
                    try {
                        String imgUrl = mShareInfo.getImgUrl();
                        if (!TextUtils.isEmpty(imgUrl)) {
                           /* mShareBm = Glide.with(activity.getApplicationContext()).load(ImageOptiUrl.get(imgUrl, 100, 100)).asBitmap().into(100, 100).get();*/
                            mShareBm = Glide.with(activity.getApplicationContext()).load(ImageOptiUrl.get(imgUrl, 200, 200)).asBitmap().into(200, 200).get();
                        }else {
                            mShareInfo.setImgPath(ImageToSdcard.getPathByUrl(mShareInfo.getImgPath()));
                            mShareInfo.setType(ShareInfo.IS_IMG_LOGO);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    init(mActivity, mShareInfo, mShareBm);
                }
            }.start();
        } else {
            mShareInfo.setImgPath(ImageToSdcard.getPathByUrl(mShareInfo.getImgPath()));
            init(activity, mShareInfo, null);
        }
    }

    /**
     * @param activity
     * @param title
     * @param shareUrl
     * @param shareBm
     */
    public ShareManager(Activity activity, String title, String suggest, String shareUrl, Bitmap shareBm, Action0 cb) {
        this.mActivity = activity;
        mShareInfo = new ShareInfo();
        mShareInfo.setTitle(title);
        mShareInfo.setUrl(shareUrl);
        if (TextUtils.isEmpty(suggest)) {
            mShareInfo.setContent(title);
        } else {
            mShareInfo.setContent(suggest);
        }
        this.mShareBm = shareBm;
        this.mCb = cb;
        init(activity, mShareInfo, mShareBm);
    }

    private void init(Activity activity, ShareInfo shareInfo, Bitmap bmp) {
        int isLogo = ShareInfo.ISNOT_IMG_LOGO;
        File file = activity.getApplicationContext().getFileStreamPath(ShareManager.SHARE_IMG_NAME);
        if (bmp != null) {
            FileUtil.saveBitmapToFileSystemAsync(activity.getApplicationContext(), null, ShareManager.SHARE_IMG_NAME, bmp);
            if (file != null) {
                shareInfo.setImgPath(file.getAbsolutePath());
            }
        }
    }


    public DialogView show() {
        if (null == mDialogView) {
            // 加载布局文件
            View mContentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(com.jeremy.lychee.R.layout.layout_share_box, null);
            View mShareCancel = mContentView.findViewById(com.jeremy.lychee.R.id.tv_share_cancel);
            mShareCancel.setOnClickListener(this);
            View mShareWeibo = mContentView.findViewById(com.jeremy.lychee.R.id.tv_share_weibo);
            mShareWeibo.setOnClickListener(this);
            View mShareWeixin = mContentView.findViewById(com.jeremy.lychee.R.id.tv_share_weixin);
            mShareWeixin.setOnClickListener(this);
            View mShareCircle = mContentView.findViewById(com.jeremy.lychee.R.id.tv_share_circle);
            mShareCircle.setOnClickListener(this);
            View mcopy_url = mContentView.findViewById(com.jeremy.lychee.R.id.tv_copy_url);
            mcopy_url.setOnClickListener(this);

            mDialogView = new DialogView(mActivity, mContentView);
        }
        mDialogView.setGravity(Gravity.BOTTOM);
        mDialogView.setFullWidth(true);
        mDialogView.showDialog();

        boolean needResume = VideoPlayer.getInstance().isPlaying();
        if (needResume) {
            VideoPlayer.getInstance().pause();
        }

        mDialogView.setOnDialogDismissListener(dialog -> {
            if (needResume) {
                VideoPlayer.getInstance().start();
            }
        });

        return mDialogView;
    }

    private void dissMissDialog() {
        mDialogView.disMissDialog();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case com.jeremy.lychee.R.id.share:
                break;
            case com.jeremy.lychee.R.id.tv_share_cancel:
                break;
            case com.jeremy.lychee.R.id.tv_share_weibo:
               /* WBShareManager.share2Weibo(mActivity, mShareInfo.getUrl(), mShareInfo.getTitle(),mShareInfo.getShareContentType(),mShareBm);*/
                WBShareManager.share2Weibo(mActivity, mShareInfo);
                doShareDot();
                break;
            case com.jeremy.lychee.R.id.tv_share_weixin:
                WXShareManager.share2Weixin(mActivity.getApplicationContext(), mShareInfo, false);
                doShareDot();
                break;
            case com.jeremy.lychee.R.id.tv_share_circle:
                WXShareManager.share2Weixin(mActivity.getApplicationContext(), mShareInfo, true);
                doShareDot();
                break;
            case com.jeremy.lychee.R.id.tv_copy_url:
                AppUtil.copy(mActivity.getApplicationContext(), mShareInfo.getUrl());
                ThreadUtils.postOnUiThread(() -> {
                    ToastHelper.getInstance(mActivity.getApplicationContext()).toast(com.jeremy.lychee.R.string.copy_ok);

                });
                break;
        }
        //complete回调
        if (mCb != null) mCb.call();
        dissMissDialog();
    }

    /**
     * 分享打点
     */
    private void doShareDot(){
        OldRetroApiService mRetroApiService = OldRetroAdapter.getService();
        int shareType = mShareInfo.getShareContentType();
        String type = shareType==ShareInfo.SHARECONTENT_NEWS ? "news" : "video";
        String sign = signString(mShareInfo.getUrl(), mShareInfo.getNid(), type);
        Observable<Result> mObservable = mRetroApiService.doShare(mShareInfo.getUrl(), mShareInfo.getNid(), type, sign);

        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();

    }

    private String signString(String url, String nid, String type) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("ver", String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        treeMap.put("channel", "cid");
        treeMap.put("os", Build.DISPLAY);
        treeMap.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        treeMap.put("os_type", "Android");
        treeMap.put("carrier", ((TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
        treeMap.put("token", AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        treeMap.put("sid", Session.getSid());
        treeMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));

        treeMap.put("url", url);
        treeMap.put("nid", nid);
        treeMap.put("type", type);
        Set<String> keySet = treeMap.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            stringBuilder.append(key);
            stringBuilder.append("=");
            if (treeMap.get(key) != null) {
                stringBuilder.append(treeMap.get(key));
            } else {
                stringBuilder.append("");
            }
            if (key != treeMap.lastKey()) {
                stringBuilder.append("&");
            }
        }
        stringBuilder.append("shi!@#$%^&*[xian!@#]*");
        String md5Str = AppUtil.getMD5code(stringBuilder.toString());
        return md5Str != null ? md5Str.substring(3, 10) : null;

    }

}
