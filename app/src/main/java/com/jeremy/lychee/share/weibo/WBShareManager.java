package com.jeremy.lychee.share.weibo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.utils.OptionsManager;
import com.jeremy.lychee.utils.ThreadUtils;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.widget.shareWindow.ShareManager;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;

import java.io.ByteArrayOutputStream;

/**
 * Weibo Share Manager
 * Created by zhangying-pd on 2015/11/30.
 */
public class WBShareManager {
    private static final int TIMELINE_SUPPORTED_VERSION = 10351;
    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
    private static final int THUMB_SIZE = 150;
    public static void share2Weibo(Activity mActivity,ShareInfo shareInfo) {
        Log.v("sub","share2weibo");
        if (shareInfo==null){
            return;
        }
        // 微博分享时，没安装微博不需要提示
        IWeiboShareAPI weiboAPI = WeiboShareSDK.createWeiboAPI(mActivity, Config.WEIBO_APP_KEY, false);
        if (!weiboAPI.isWeiboAppInstalled()) {
            ThreadUtils.postOnUiThread(() -> ToastHelper.getInstance(mActivity).toast(com.jeremy.lychee.R.string.share_weibo_uninstalled));
            return;
        }

        weiboAPI.registerApp();
        int supportApi = weiboAPI.getWeiboAppSupportAPI();
       /* String content = ShareManager.TITLE_LEFT + shareInfo.getTitle() + ShareManager.TITLE_RIGHT + "阅读全文： " + shareInfo.getUrl() + " " + ShareManager.NEWS_FROM + ShareManager.DOWNLOAD + ShareManager.DOWNLOAD_URL;*/
        String content = ShareManager.TITLE_LEFT + shareInfo.getTitle() + ShareManager.TITLE_RIGHT + "阅读全文： " + shareInfo.getUrl() + " " + ShareManager.NEWS_FROM;
        if (shareInfo.getShareContentType()==ShareInfo.SHARECONTENT_LIVE){
            /*content = ShareManager.TITLE_LEFT + shareInfo.getTitle() + ShareManager.TITLE_RIGHT + "观看： " + shareInfo.getUrl() + " " + ShareManager.NEWS_FROM + ShareManager.DOWNLOAD + ShareManager.DOWNLOAD_URL;*/
            content = ShareManager.TITLE_LEFT + shareInfo.getTitle() + ShareManager.TITLE_RIGHT + "观看： " + shareInfo.getUrl() + " " + ShareManager.NEWS_FROM;
        }
        Bitmap thumb = null;
        if (shareInfo.getType() != ShareInfo.IS_IMG_LOGO) {
            thumb = extractThumbNail(shareInfo.getImgPath(), THUMB_SIZE, THUMB_SIZE, true);
        } else {
            thumb = BitmapFactory.decodeResource(mActivity.getResources(), com.jeremy.lychee.R.drawable.logo_share);
        }

        // 是否支持mutl发送消息
        if (supportApi >= TIMELINE_SUPPORTED_VERSION) {
            // 初始化微博的分享消息
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

            ImageObject imageObject = new ImageObject();
            imageObject.identify = Utility.generateGUID();// 创建一个唯一的ID

            BitmapFactory.Options options = OptionsManager.getBitmapOptions(shareInfo.getImgPath(), OptionsManager.ORIGINAL_IMAGE);
            Bitmap bm = null;
            if (shareInfo.getType() != ShareInfo.IS_IMG_LOGO) {
                bm = BitmapFactory.decodeFile(shareInfo.getImgPath(), options);
            } else {
                bm = thumb;
            }
            imageObject.setImageObject(bm);
            imageObject.thumbData =bmpToByteArray(bm);

            weiboMessage.imageObject = imageObject;
            TextObject textObject = new TextObject();
            textObject.text = content;
            weiboMessage.textObject = textObject;

            SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.multiMessage = weiboMessage;
            weiboAPI.sendRequest(mActivity,req);
        } else {
            WeiboMessage weiboMessage = new WeiboMessage();
            WebpageObject webpageObject = new WebpageObject();
            webpageObject.identify = Utility.generateGUID();// 创建一个唯一的ID
            webpageObject.title = shareInfo.getTitle();
            webpageObject.description = shareInfo.getTitle();
            webpageObject.thumbData = bmpToByteArray(thumb);
            webpageObject.actionUrl = shareInfo.getUrl();
            webpageObject.defaultText = content;
            weiboMessage.mediaObject = webpageObject;

            SendMessageToWeiboRequest req = new SendMessageToWeiboRequest();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = weiboMessage;
            weiboAPI.sendRequest(mActivity,req);
        }


    }

    private static byte[] bmpToByteArray(final Bitmap bmp) {
        if (bmp==null){
            return null;
        }
        boolean needRecycle = false;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public static Bitmap extractThumbNail(String path, int height, int width, boolean crop) {

        if(TextUtils.isEmpty(path)) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inJustDecodeBounds = true;

            // get original options
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null && !tmp.isRecycled()) {
                tmp.recycle();
                tmp = null;
            }

            // calculate options inSampleSize
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }
            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }
            options.inJustDecodeBounds = false;

            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                return null;
            }

            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
//				bm.recycle();
                bm = null;
                bm = scale;
            }
            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }
                bm.recycle();
                bm = cropped;
            }
            return bm;
        } catch (final Exception e) {
            e.printStackTrace();
            options = null;
        }
        return null;
    }
}
