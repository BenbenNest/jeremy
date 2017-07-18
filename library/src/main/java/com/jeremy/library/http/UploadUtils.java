package com.jeremy.library.http;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.jeremy.library.thread.MyThreadPool;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * Created by changqing on 2017/6/30.
 */
public class UploadUtils {
    private static final String TAG = "UploadUtils";
    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";
    private static File file;
    private static UploadUtils instance = null;

    private UploadUtils() {

    }

    public static UploadUtils getInstance() {
        if (instance == null) {
            instance = new UploadUtils();
        }
        return instance;
    }

    public File getLogFile(Context context) {
        String path;
        if (Environment.isExternalStorageEmulated()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hawiilog.txt";
        } else {
            path = context.getCacheDir() + File.separator + "test.zip";
        }
        File file = new File(path);
        return file;
    }

    public void test(Context context) {
        try {
            file = getLogFile(context);
            MyThreadPool.execute(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (file != null) {
                try {
//                    uploadFile(file,"http://100.90.75.52:8057/upload_file");
//                    uploadFile(null, "hawii_log", file, "hawii_log", "http://100.90.75.52:8059");
//                    uploadFile(null, "hawii_log", file, "hawii_log", "http://10.97.182.1:8899");
//                    FileTransferClient client = new FileTransferClient();
//                    client.sendFile();
                } catch (Exception e) {

                }
            }
        }
    };

    /**
     * @param params       传递的普通参数
     * @param uploadFile   需要上传的文件名
     * @param fileFormName 需要上传文件表单中的名字
     * @param newFileName  上传的文件名称，不填写将为uploadFile的名称
     * @param urlStr       上传的服务器的路径
     * @throws IOException
     */
    public static void uploadFile(Map<String, String> params,
                                  String fileFormName, File uploadFile, String newFileName,
                                  String urlStr) throws IOException {
        if (newFileName == null || newFileName.trim().equals("")) {
            newFileName = uploadFile.getName();
        }

        StringBuilder sb = new StringBuilder();
        /**
         * 普通的表单数据
         */

        if (params != null)
            for (String key : params.keySet()) {
                sb.append("--" + BOUNDARY + "\r\n");
                sb.append("Content-Disposition: form-data; name=\"" + key
                        + "\"" + "\r\n");
                sb.append("\r\n");
                sb.append(params.get(key) + "\r\n");
            }
        else {
            sb.append("\r\n");
        }
        /**
         * 上传文件的头
         */
        sb.append("--" + BOUNDARY + "\r\n");
        sb.append("Content-Disposition: form-data; name=\"" + fileFormName
                + "\"; filename=\"" + newFileName + "\"" + "\r\n");
        sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
        sb.append("\r\n");

        byte[] headerInfo = sb.toString().getBytes("UTF-8");
        byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");

        System.out.println(sb.toString());

        URL url = new URL(urlStr);
        Socket socket = new Socket(url.getHost(), url.getPort());
        OutputStream os = socket.getOutputStream();
        PrintStream ps = new PrintStream(os, true, "UTF-8");

        // 写出请求头
        ps.println("POST " + urlStr + " HTTP/1.1");
        ps.println("Content-Type: multipart/form-data; boundary=" + BOUNDARY);
        ps.println("Content-Length: "
                + String.valueOf(headerInfo.length + uploadFile.length()
                + endInfo.length));
        ps.println("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        InputStream in = new FileInputStream(uploadFile);
        // 写出数据
        os.write(headerInfo);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1)
            os.write(buf, 0, len);

        os.write(endInfo);

        in.close();
        os.close();
    }


    private static final int TIME_OUT = 10 * 1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码


    public static String uploadFile(File file, String RequestURL) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            if (file != null) {
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);

                sb.append("Content-Disposition: form-data; name=\"zipfile\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

                int res = conn.getResponseCode();
                Log.e(TAG, "response code:" + res);
                if (res == 200) {
                    Log.e(TAG, "request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    Log.e(TAG, "result : " + result);
                } else {
                    Log.e(TAG, "request error");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
