
package com.jeremy.lychee.model.update;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleDownloader {

    private static final int TIME_OUT = 15000;
    private OnDownloadListener listener;
    public boolean isCancle;

    public void cancle() {
        this.isCancle = true;
    }

    public SimpleDownloader() {
    }

    public void performDownload(String address, String filePath, OnDownloadListener listener) {

        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(filePath)) {
//            throw new IllegalArgumentException("filePath can not be null or blank");
            return;
        }

        this.listener = listener;
        FileOutputStream fos = null;
        InputStream in = null;
        onDownloadStart();
        try {
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(TIME_OUT);
                conn.setReadTimeout(TIME_OUT);
                long length = conn.getContentLength();
                in = conn.getInputStream();
                fos = streamToFile(filePath, in, length);

        } catch (IOException e) {
            e.printStackTrace();
            onDownloadFall();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private FileOutputStream streamToFile(String filePath,
            InputStream in,
            long length) throws IOException {
        FileOutputStream fos;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File f = new File(filePath);
        if (!f.getParentFile().exists()) {
            boolean mkdirs = f.getParentFile().mkdirs();
            boolean newFile = f.createNewFile();
        }
        fos = new FileOutputStream(f);
        byte[] buffer = new byte[1024 * 32];
        int len;
        long data = 0;
        while ((len = in.read(buffer)) != -1) {
            if (isCancle) {
                onCancleDownload();
                break;
            }
            data += len;
            int percent = (int) (data * 100 * 1.0f / length);
            baos.write(buffer, 0, len);
            onDownloading(percent);
//            if (percent == 100) {
//
//            }
            fos.write(buffer, 0, len);
        }
        
        onDownloadSuccess(baos.toByteArray());
        return fos;
    }

    private void onCancleDownload() {
        if (listener != null) {
            listener.onCancle();
        }
    }

    private void onDownloading(int percent) {
        if (listener != null) {
            listener.onDownloading(percent);
        }
    }

    private void onDownloadSuccess(byte[] byteArray) {
        if (listener != null) {
            listener.onSuccess();
        }
    }

    private void onDownloadFall() {
        if (listener != null) {
            listener.onFailed();
        }
    }

    private void onDownloadStart() {
        if (listener != null) {
            listener.onStart();
        }
    }

    public interface OnDownloadListener {
        void onStart();

        void onDownloading(int percent);

        void onSuccess();

        void onFailed();

        void onCancle();
    }


}
