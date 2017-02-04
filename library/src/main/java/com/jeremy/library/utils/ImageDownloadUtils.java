package com.jeremy.library.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * Created by changqing.zhao on 2017/1/25.
 */
public class ImageDownloadUtils {
    private static ImageDownloadUtils instance = null;
    private Context context;
    private int buffer_size = 2048;

    private ImageDownloadUtils(Context context) {
        this.context = context;
    }

    public static ImageDownloadUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageDownloadUtils.class) {
                if (instance == null) {
                    instance = new ImageDownloadUtils(context);
                }
            }
        }
        return instance;
    }

    public void downLoadImage(final String url) {
//        Retrofit retrofit = new RetrofitService().getRetrofit();
//        RetrofitAPI api = retrofit.create(RetrofitAPI.class);
//        Call<ResponseBody> call = api.downLoadImage(url);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.body() == null) {
//                    ToastUtils.showToast(context, context.getString(R.string.image_down_fail));
//                }
//                InputStream is = null;
//                byte[] buf = new byte[buffer_size];
//                int len = 0;
//                FileOutputStream fos = null;
//                try {
//                    File dir = StorageUtils.getCacheDirectory(context);
//                    if (dir == null) {
//                        ToastUtils.showToast(context, context.getString(R.string.image_down_fail));
//                        return;
//                    }
//                    File cacheDir = new File(dir, "web_image");
//                    if (!cacheDir.exists()) {
//                        if (!cacheDir.mkdirs()) {
//                            return;
//                        }
//                    }
//                    File file = new File(cacheDir, new Date().getTime() + url.substring(url.lastIndexOf(".")));
//                    is = response.body().byteStream();
//                    fos = new FileOutputStream(file);
//                    while ((len = is.read(buf)) != -1) {
//                        fos.write(buf, 0, len);
//                    }
//                    fos.flush();
//                    ToastUtils.showToast(context, context.getString(R.string.image_down_success));
//                    exportToGallery(file.getAbsolutePath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    ToastUtils.showToast(context, context.getString(R.string.image_down_fail));
//                } finally {
//                    try {
//                        if (is != null) {
//                            is.close();
//                        }
//                    } catch (IOException e) {
//
//                    }
//                    try {
//                        if (fos != null) {
//                            fos.close();
//                        }
//                    } catch (IOException e) {
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                ToastUtils.showToast(context, context.getString(R.string.image_down_fail));
//            }
//        });
    }

    /**
     * 把下载的图片路径映射到相册中去，如果从相册或者相应的文件夹中的一个删除该图片，则该图片就被删除
     *
     * @param filePath
     */
    private void exportToGallery(String filePath) {
        //Android 4.4 前后的兼容性问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final ContentValues values = new ContentValues(2);
            values.put(MediaStore.Video.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Video.Media.DATA, filePath);
            final Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
//        http://www.cnblogs.com/DarrenChan/p/5631687.html
    }


//    方案一：
//
//            　发送广播通知系统更新相册。
//
//    在网上大部分时候是这样做的，为什么呢？因为简单，只需要发送一个广播就OK了。但是这个方法是一个坑，坑在哪里，一会在说，这里先说一个怎么实现：
//
//
//
//    复制代码
//    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//            Uri.fromFile(new File(url.webviewImagePath+"/image.jpg")));
//    File file= (File) object;
//    Uri uri = Uri.fromFile(file);
//    intent.setData(uri);
//    ShowImageActivity.this.sendBroadcast(intent);
//    复制代码
//
//
//    　　上面代码就是发送广播中的方式，其中url.webviewImagePath+"/image.jpg"是保存到本地的地址和图片格式，file是把下载到图片的对象object转换为file。之后发送广播通知系统更新相册就好。
//
//            　　那么现在问题来了，坑在哪里。主要是下面两个问题：
//
//            　　1、相册更新很慢
//
//    　　这是因为我们向系统发送广播通知，却不能告知系统刷新特定的文件，所以系统会刷新整个sd卡。这样一来，自然相册更新会很慢。
//
//            　　2、部分安卓版本相册无法更新，重启手机后会更新
//
//    　　这是因为在部分安卓版本中，考虑到系统安全的问题，是不允许app向系统发送广播，这样一来自然无法更新相册，而另一个方面，手机重新启动，会重新挂载sd卡，这样就会刷新sd开所有东西。
//
//            　　那么问题来了，既然通过广播的方式刷新相册有这么多坑，那么有没有什么好的方式呢？这就是第二种方式：
//
//    方案二：
//
//            　　使用MediaScanner
//
//    　　什么是MediaScanner?MediaScanner完成Android中的多媒体文件的扫描工作。例如，mediascanner扫描系统内存和SD卡文件之后，会将扫描的结果加载在数据库中，在Music这个应用程序中看到的显示在activity 的list列表上歌曲专辑名，流派，歌曲时长等信息，都是扫描后的结果放在数据库中，最后读到的数据库中的信息。这里可以明白的一点是：多媒体数据库中的信息是由MediaScanner添加的。下面就是使用的方式：
//
//    复制代码
//    class SannerClient implements
//            MediaScannerConnection.MediaScannerConnectionClient {
//
//        public void onMediaScannerConnected() {
//
//            if (mFile == null) {
//                return;
//            }
//            scan(mFile, mMimeType);
//        }
//
//        public void onScanCompleted(String path, Uri uri) {
//            mConn.disconnect();
//        }
//
//        private void scan(File file, String type) {
//            if (file.isFile()) {
//                mConn.scanFile(file.getAbsolutePath(), null);
//                return;
//            }
//            //该方法可以遍历多个文件，这里不需要
//            File[] files = file.listFiles();
//            if (files == null) {
//                return;
//            }
//            for (File f : file.listFiles()) {
//                scan(f, type);
//            }
//        }
//    }
//
//
//    public void scanFile(File file, String mimeType) {
//        mFile = file;
//        mMimeType = mimeType;
//        mConn.connect();
//    }
//    复制代码
//    　　在我们需要添加照片到相册的地方，这几调用scanFile就可以了，其中两个参数：file是指添加的文件，mimeType是扫描文件的格式。这样我们可以直接扫描添加特定的文件。提高了速度

}
