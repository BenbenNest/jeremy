package com.jeremy.camera.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by changqing on 2018/2/13.
 * <p>
 * Camera类的的PreviewCallback回调接口回调的是相机的预览图像，但是是YUV格式的，
 * 起初我想吧这个byte数组转换成bitmap，用BitmapFactory转后拿到null的对象
 * 显然，这个byte数组不一样。
 * 然后我利用YuvImage对象的compressToJpeg方法生成图片文件保存在本地，算是看清了图片的内容
 * YuvImage对象内部的方法少的可怜，感觉只能单方向的转成文件流，然后文件再转成bitmap。
 * 要是从bitmap回到YuvImage感觉不太可能。（知道的大神可以说下）
 * 贴上YuvImage格式byte数组转成文件的办法：
 * 再提及一下，为什么要旋转YuvImage，因为安卓的摄像Camera天生是横的，当用竖屏牌照或者摄像的时候，需要设置
 * Camera.setDisplayOrientation(90);
 * 这样，照片的方向是对的，但是尺寸缺不对劲，这时，surfaceView的宽高比和Camera的宽高比刚好是倒数关系，预览图的输出倒了，相机还是原来的，这个貌似无法改变。就此时你拍摄一张照片，拿到的bitmap也是有90度的角度差别和你竖屏所见。后续处理bitmap旋转并不麻烦：
 http://blog.csdn.net/illidantao/article/details/51366047
 */

public class YuvImageUtils {

    public static void saveMyBitmap(String filePath, byte[] data, int width, int high) {
        File f = new File(filePath);
        try {
            f.createNewFile();
        } catch (IOException e) {
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        int w = cameraManager.getPreviewWidth();
//        int h = cameraManager.getPreviewHeight();
        int w = 960;
        int h = 720;
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, width, high, null);
        try {
            yuvImage.compressToJpeg(new Rect(0, 0, w, h), 50, fOut);
        } catch (Exception e) {

        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPictureTaken(byte[] data, Camera camera) {
        //将得到的照片进行270°旋转，使其竖直
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.preRotate(270);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap
                .getHeight(), matrix, true);
    }


    private byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }


}
