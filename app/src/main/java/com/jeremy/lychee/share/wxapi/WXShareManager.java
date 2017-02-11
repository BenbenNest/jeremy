package com.jeremy.lychee.share.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jeremy.lychee.model.ShareInfo;
import com.jeremy.lychee.utils.ThreadUtils;
import com.jeremy.lychee.utils.ToastHelper;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * wechat share manager
 * Created by zhangying-pd on 2015/11/30.
 */
public class WXShareManager {
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private static String WX_APP_ID = "wx768117d6ac23353d";
    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
    private static final int THUMB_SIZE = 150;

    public static void shareToWX(Context mContext, String url, String title, String description, Bitmap thumb, boolean toCircle){
        IWXAPI wxAPI = WXAPIFactory.createWXAPI(mContext, WX_APP_ID, true);
        if (!wxAPI.isWXAppInstalled()) {
            ThreadUtils.postOnUiThread(() -> ToastHelper.getInstance(mContext).toast(com.jeremy.lychee.R.string.share_weixin_uninstalled));
            return;
        }

        if(toCircle && !isWeixinCircleSupported(mContext)) {
            ThreadUtils.postOnUiThread(() -> ToastHelper.getInstance(mContext).toast(com.jeremy.lychee.R.string.share_weixin_uninstalled));
            return;
        }
        wxAPI.registerApp(WX_APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        if (thumb !=null){
            msg.thumbData = bmpToByteArray(thumb, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage" + System.currentTimeMillis();
        req.message = msg;
        req.scene = toCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxAPI.sendReq(req);
    }

    private static boolean isWeixinCircleSupported(Context mContext) {
        IWXAPI wxAPI = WXAPIFactory.createWXAPI(mContext, WX_APP_ID, true);
        int wxSdkVersion = wxAPI.getWXAppSupportAPI();
        return wxSdkVersion >= TIMELINE_SUPPORTED_VERSION;
    }
    public static void share2Weixin(Context context, ShareInfo shareInfo, boolean toCircle) {
        if(context == null || shareInfo == null) {
            return;
        }
        IWXAPI wxAPI = WXAPIFactory.createWXAPI(context, WXConfig.WX_APP_ID, true);
        if (!wxAPI.isWXAppInstalled()) {
            ToastHelper.getInstance(context).toast(com.jeremy.lychee.R.string.share_weixin_uninstalled);
            return;
        }

        if(toCircle && !isWeixinCircleSupported(context)) {
            ToastHelper.getInstance(context).toast(com.jeremy.lychee.R.string.share_weixin_uninstalled);
            return;
        }
        //加上来源
        String url = null;
        if(toCircle) {
            url = shareInfo.getUrl();
        } else {
            url = shareInfo.getUrl();
        }
        wxAPI.registerApp(WXConfig.WX_APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title =  shareInfo.getTitle();
        msg.description = shareInfo.getContent();
        Bitmap thumb = extractThumbNail(shareInfo.getImgPath(), THUMB_SIZE, THUMB_SIZE, true);
        if (thumb==null) {
            thumb = BitmapFactory.decodeResource(context.getResources(), com.jeremy.lychee.R.drawable.logo_share);
        }

        msg.thumbData = getWeixinThumb(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage" + System.currentTimeMillis();
        req.message = msg;
        req.scene = toCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxAPI.sendReq(req);
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



    public static byte[] getWeixinThumb(Bitmap bmp) {
        byte[] res = bmp2ByteArray100(bmp, true);
        while(res != null && res.length >= 32 * 1024) {
            bmp = BitmapFactory.decodeByteArray(res, 0, res.length);
            res = bmp2ByteArray100(bmp, true);
        }
        return res;
    }
    /**
     * bitmap转为byte
     *
     * @param
     * @return
     */
    public static byte[] bmp2ByteArray100(final Bitmap bmp, boolean needRecycle) {
        if (bmp == null || bmp.isRecycled()) {
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 65, output);
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

    private static byte[] bmpToByteArray(final Bitmap bmp, boolean needRecycle) {
        needRecycle = false;
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

}
