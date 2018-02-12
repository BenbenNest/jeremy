package com.jeremy.library.okhttp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by changqing on 2018/2/6.
 */

public class ImageUpload {

    //    public static final String MULTIPART_FORM_DATA = "image/jpg";  // 指明要上传的文件格式
    public static final String MULTIPART_FORM_DATA = "image/*";  // 指明要上传的文件格式


    public static void uploadImage(String partName, String filePath, String uploadUrl) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
        // 初始化请求体对象，设置Content-Type以及文件数据流
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)   // multipart/form-data
                .addFormDataPart(partName, file.getName(), requestFile)
                .build();

        // 封装OkHttp请求对象，初始化请求参数
        Request request = new Request.Builder().url(uploadUrl).post(requestBody).build();
        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                .connectTimeout(100, TimeUnit.SECONDS)   // 设置请求超时时间
                .writeTimeout(150, TimeUnit.SECONDS)
                .build();
        // 发起异步网络请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }
//
//// 调用文件上传方法，需要传入requestBody的key值，本地文件路径以及请求回调方法
//UploadWrapper.okHttpUpload("file",mImagePath,new UploadWrapper.UploadCallback()
//
//    {
//        @Override
//        public void onResponse (Call call,final okhttp3.Response response){
//        try {
//            final String result = response.body().string();
//            JSONObject jsonObject = new JSONObject(result);
//            mImageUrl = jsonObject.getJSONObject("data").getString("url");
//            showImage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//        @Override
//        public void onFailure (Call arg0, IOException e){
//        e.printStackTrace();
//    }
//    });


}
