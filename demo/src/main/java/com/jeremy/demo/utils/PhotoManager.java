package com.jeremy.demo.utils;


import com.jeremy.library.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by changqing on 2017/11/16.
 */
public class PhotoManager {
    static String TAG = "PhotoManager";

    public static String root;

//    public void test() {
//        String photoDir = File.separator + "DCIM" + File.separator + "Camera";
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        } else {
//            root = mainActivityActivityTestRule.getActivity().getFilesDir().getAbsolutePath();
//        }
//        String photoPath = root + photoDir;
//        File newDir = new File(root + File.separator + "Photo_Mu" + File.separator);
//        if (!newDir.exists()) {
//            newDir.mkdirs();
//        }
//        sortFile(new File(photoPath));
//        try {
//            Thread.sleep(1000 * 60 * 10);
//        } catch (Exception e) {
//
//        }
//    }

    public static void sortFile(File root) {
        File[] files = root.listFiles();
        if (files == null || files.length == 0) return;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                sortFile(file);
            } else {
//                file.lastModified()
                if (FileUtils.isImage(file.getAbsolutePath())) {
//                    fileInfo(file.getAbsolutePath());
                    handlePhoto(file);
                }
//                handlePhoto(file);
//                String newPath = file.getParent() + File.separator + "test";
//                File newDir = new File(newPath);
//                if (!newDir.exists()) {
//                    newDir.mkdirs();
//                }
//                copyFile(file.getAbsolutePath(), file.getParent() + File.separator + "test" + File.separator + file.getName());
            }
        }
    }

    public static void handlePhoto(File file) {
        //Java第三方库方法
        Date date = Metadata_Extractor.getDate(file);
        if (date != null) {
            File yearDir = new File(root + File.separator + "Photo_Mu" + File.separator + (date.getYear() + 1900));
            if (!yearDir.exists()) {
                yearDir.mkdirs();
            }
            File monthDir = new File(yearDir, date.getMonth() + "");
            if (!monthDir.exists()) {
                monthDir.mkdirs();
            }
            copyFile(file.getAbsolutePath(), monthDir.getAbsolutePath() + File.separator + file.getName());
        }
        //Android方法
//        try {
//            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
//            String TAG_DATETIME = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
//            Date date = new Date(TAG_DATETIME);
//            File yearDir = new File(root + File.separator + "Photo_Mu" + File.separator + date.getYear());
//            if (!yearDir.exists()) {
//                yearDir.mkdirs();
//            }
//            File monthDir = new File(root + File.separator + "Photo_Mu" + File.separator + date.getYear() + File.separator + date.getMonth());
//            if (!monthDir.exists()) {
//                monthDir.mkdirs();
//            }
//            copyFile(file.getAbsolutePath(), root + File.separator + "Photo_Mu" + File.separator + date.getYear() + File.separator + date.getMonth() + File.separator + file.getName());
//
//        } catch (IOException e) {
//
//        }

    }

    /**
     * 复制单个文件
     *
     * @param oldPath String  原文件路径  如：c:/fqf.txt
     * @param newPath String  复制后路径  如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
//           int  bytesum  =  0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {  //文件存在时
                InputStream inStream = new FileInputStream(oldPath);  //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
//               int  length;
                while ((byteread = inStream.read(buffer)) != -1) {
//                   bytesum  +=  byteread;  //字节数  文件大小
//                   System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

//    public static void fileInfo(String path) {
//        try {
//            Log.i(TAG, "path:" + path);
//            ExifInterface exifInterface = new ExifInterface(path);
//
//            String TAG_APERTURE = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
//            String TAG_DATETIME = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
//
//            Date date = simpleDateFormat.parse(TAG_DATETIME);
//
//            String TAG_EXPOSURE_TIME = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
//            String TAG_FLASH = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
//            String TAG_FOCAL_LENGTH = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
//            String TAG_IMAGE_LENGTH = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
//            String TAG_IMAGE_WIDTH = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
//            String TAG_ISO = exifInterface.getAttribute(ExifInterface.TAG_ISO);
//            String TAG_MAKE = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
//            String TAG_MODEL = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
//            String TAG_ORIENTATION = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
//            String TAG_WHITE_BALANCE = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
//
//            Log.i(TAG, "光圈值:" + TAG_APERTURE);
//            Log.i(TAG, "拍摄时间:" + TAG_DATETIME);//2016:05:23 17:30:11
//            Log.i(TAG, "曝光时间:" + TAG_EXPOSURE_TIME);
//            Log.i(TAG, "闪光灯:" + TAG_FLASH);
//            Log.i(TAG, "焦距:" + TAG_FOCAL_LENGTH);
//            Log.i(TAG, "图片高度:" + TAG_IMAGE_LENGTH);
//            Log.i(TAG, "图片宽度:" + TAG_IMAGE_WIDTH);
//            Log.i(TAG, "ISO:" + TAG_ISO);
//            Log.i(TAG, "设备品牌:" + TAG_MAKE);
//            Log.i(TAG, "设备型号:" + TAG_MODEL);
//            Log.i(TAG, "旋转角度:" + TAG_ORIENTATION);
//            Log.i(TAG, "白平衡:" + TAG_WHITE_BALANCE);
//            Log.i(TAG, "----------------------------------");
//                /*
//                Date date = UtilsTime.stringTimeToDate(TAG_DATETIME, new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault()));
//
//                String FStringTime = UtilsTime.dateToStringTime(date, new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()));
//
//                mTextView.setText("TAG_DATETIME = " + TAG_DATETIME + "\n" + "FStringTime = " + FStringTime);
//                */
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 读取文件创建时间
     */
    public static void getCreateTime() {
        String filePath = "C:\\test.txt";
        String strTime = null;
        try {
            Process p = Runtime.getRuntime().exec("cmd /C dir "
                    + filePath
                    + "/tc");
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.endsWith(".txt")) {
                    strTime = line.substring(0, 17);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("创建时间    " + strTime);
        //输出：创建时间   2009-08-17  10:21
    }

    /**
     * 读取文件修改时间的方法1
     */
    @SuppressWarnings("deprecation")
    public static void getModifiedTime_1() {
        File f = new File("C:\\test.txt");
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        cal.setTimeInMillis(time);
        //此处toLocalString()方法是不推荐的，但是仍可输出
        System.out.println("修改时间[1] " + cal.getTime().toLocaleString());
        //输出：修改时间[1]    2009-8-17 10:32:38
    }

    /**
     * 读取修改时间的方法2
     */
    public static void getModifiedTime_2() {
        File f = new File("C:\\test.txt");
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        System.out.println("修改时间[2] " + formatter.format(cal.getTime()));
        //输出：修改时间[2]    2009-08-17 10:32:38
    }


}
