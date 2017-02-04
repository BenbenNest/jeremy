package com.jeremy.library.jsobject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.jeremy.library.R;
import com.jeremy.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by changqing.zhao on 2017/2/3.
 */

public class ImageViewJavaScriptObj {
    private Context context;
    List<String> listImgSrc = new ArrayList<>();

    public ImageViewJavaScriptObj(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void fun2(String name) {
        Toast.makeText(context, "调用fun2:" + name, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void getAllImageUrlFromHtml(String html) {
        Toast.makeText(context, html, Toast.LENGTH_LONG).show();

        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(html);
        while (m_image.find()) {
            img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                String url = m.group(1);
                if (isPicture(url)) {
                    listImgSrc.add(url);
                }
            }
        }
    }

    @android.webkit.JavascriptInterface
    public void startShowImageActivity(String url) {
        Toast.makeText(context, url, Toast.LENGTH_LONG).show();
//        Intent intent = new Intent();
//        intent.putExtra(ImageBrowseActivity.IMAGE, url);
//        intent.putStringArrayListExtra(ImageBrowseActivity.IMAGES, (ArrayList<String>) listImgSrc);
//        intent.setClass(context, ImageBrowseActivity.class);
//        context.startActivity(intent);
    }

    public void parseHTML(WebView view) {
        view.loadUrl("javascript:window.imageListener.getAllImageUrlFromHtml(document.getElementsByTagName('html')[0].innerHTML);");
//        view.loadUrl("javascript:window.imageListener.getAllImageUrlFromHtml('<head>'+"
//                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

    public void setImageClickListner(WebView view) {
        view.loadUrl("javascript:(function(){" + "alert('tasdf')" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imageListener.startShowImageActivity(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    public void setWebImageLongClickListener(View v) {
        if (v instanceof WebView) {
            final WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            if (result != null) {
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getResources().getString(R.string.webview_image_tip));
                    builder.setMessage(context.getResources().getString(R.string.webview_image_save));
                    builder.setPositiveButton(context.getResources().getString(R.string.webview_image_save_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String url = result.getExtra();
                            ToastUtils.showCenter(context, url);
//                            ImageDownloadUtils.getInstance(context).downLoadImage(url);
                        }
                    });
                    builder.setNegativeButton(context.getResources().getString(R.string.webview_image_save_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }

    public static boolean isPicture(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String suffix = url.substring(url.lastIndexOf("."));
        String imgeArray[] = {".bmp", ".dib", ".gif", ".jfif", ".jpe", ".jpeg", ".jpg", ".png", ".tif", ".tiff", ".ico"};
        for (int i = 0; i < imgeArray.length; i++) {
            if (imgeArray[i].equals(suffix)) {
                return true;
            }
        }
        return false;
    }

}
