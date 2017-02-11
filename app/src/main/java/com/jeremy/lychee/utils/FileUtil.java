/**
 *
 */

package com.jeremy.lychee.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public final class FileUtil {

    public static <T> void saveObject2File(T t, String name) {
        if (AppUtil.isMounted()) {
            String dir = AppUtil.getAppSdRootPath();
            File Dir = new File(dir);
            if (!Dir.exists()) {
                Dir.mkdirs();
            }
            File file = new File(Dir, name);
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                objectOutputStream.writeObject(t);
                objectOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T getObjectFromAssertFile(Context context, String path) {
        try {
            InputStream inputStream = context.getResources().getAssets().open(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            T object = (T) objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public static <T> T getObjectFromCacheFile(String name) {
        if (AppUtil.isMounted()) {
            String dir = AppUtil.getAppSdRootPath();
            File Dir = new File(dir);
            if (!Dir.exists()) {
                Dir.mkdirs();
            }
            File file = new File(Dir, name);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                T object = (T) objectInputStream.readObject();
                objectInputStream.close();
                return object;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] getFileBytes(String path) {
        // if file not exist, then return null
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            // load existing file
            int fileSize = (int) file.length(); // get file size
            try {
                FileInputStream in = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(in);

                // read all
                byte[] bs = new byte[fileSize];
                if (dis.read(bs, 0, fileSize) == -1)
                    return null;

                dis.close();
                in.close();
                return bs;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] getBytesFromAssertFile(Context context, String path) {
        try {
            InputStream inputStream = context.getResources().getAssets().open(path);
            int len = inputStream.available();
            byte[] buffer = new byte[len];
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadAssertFile(Context context, String file) {

        StringBuilder buf = new StringBuilder();
        BufferedReader in = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(file);
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return buf.toString();
    }


//    public static void saveObject2File(NewsDetailContent object, String name) {
//        if (AppUtil.isMounted()) {
//            String dir = AppUtil.getAppSdRootPath();
//            File Dir = new File(dir);
//            if (!Dir.exists()) {
//                Dir.mkdirs();
//            }
//            File file = new File(Dir, name);
//            try {
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
//                objectOutputStream.writeObject(object);
//                objectOutputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static NewsDetailContent getObjectFromAssertFile(Context context, String path) {
//        try {
//            InputStream inputStream = context.getResources().getAssets().open(path);
//            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//            NewsDetailContent object = (NewsDetailContent) objectInputStream.readObject();
//            objectInputStream.close();
//            return object;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public static String computeSize(long cacheSize) {
        String result = "";
        if (cacheSize == 0) {
            result = "";
        } else if (cacheSize > 0 && cacheSize < 1024) {
            result = cacheSize + "B";
        } else if (cacheSize >= 1024 && cacheSize < (1024 * 1024)) {
            result = cacheSize / 1024 + "K";
        } else if (cacheSize >= 1024 * 1024) {
            result = cacheSize / (1024 * 1024) + "M";
        }
        return result;
    }

    /**
     * 统计目录文件大小
     *
     * @param file
     * @return
     */
    public static long countFile(File file) {

        long size = 0L;
        if (file == null || !file.exists()) {
            return size;
        }

        if (file.isFile()) {
            return file.length();
        }

        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list == null) {
                return size;
            }
            for (File childFile : list) {
                size += countFile(childFile);
            }
        }

        return size;
    }

    /**
     * 删除目录下的文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists() || !file.canWrite()) {
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        }

        boolean success = true;
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list == null) {
                return true;
            }
            for (File childFile : list) {
                success &= deleteFile(childFile);
            }
        }

        return success;
    }

    // Suppress default constructor for noninstantiability
    private FileUtil() {
        throw new AssertionError();
    }

    ;

    /*
     * 保存一个Bitmap到文件系统中
     */
    public static synchronized boolean saveBitmapToFileSystem(Context context, String path,
                                                              String name,
                                                              final Bitmap bitmap) {
        // File sdcardDir =Environment.getExternalStorageDirectory();
        // 得到一个路径，内容是sdcard的文件夹路径和名字
        // String path=sdcardDir.getPath()+"/cardImages";
        FileOutputStream out = null;
        //if path is null,it mains bitmap will save in data/data/pkgname/files/
        if (path == null) {
            try {
                out = context.openFileOutput(name, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            File thumbNail = new File(path, name);
            try {
                out = new FileOutputStream(thumbNail);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            int byteCount = bitmap.getRowBytes() * bitmap.getHeight(); //--> 12才有bitmap.getByteCount();
            int quality = 100;
            int kb = 1000000;
            if (byteCount > kb) {// 超过一百万byte就压缩一下
                quality = quality * (kb / byteCount);
            }
            if (out != null && bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
            }
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /*
     * 异步保存一个Bitmap到文件系统中
     */
    public static void saveBitmapToFileSystemAsync(final Context context, final String path, final String name, final Bitmap bitmap) {
        /*new Thread(new Runnable() {

			@Override
			public void run() {
				saveBitmapToFileSystem(context, path, name, bitmap);
			}
		}).start();*/
        saveBitmapToFileSystem(context, path, name, bitmap);
    }

    public static void writeFileFromStream(InputStream stream, File file)
            throws FileNotFoundException, IOException {
        copyStream(stream, new FileOutputStream(file));
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int length = 0;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        out.flush();
        in.close();
        out.close();
    }

    /**
     * 读取源文件字符数组
     *
     * @param file 获取字符数组的文件
     * @return 字符数组
     */
    public static byte[] readFileByte(File file) {
        FileInputStream fis = null;
        FileChannel fc = null;
        byte[] data = null;
        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            data = new byte[(int) (fc.size())];
            fc.read(ByteBuffer.wrap(data));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        return data;
    }

    /**
     * 字符数组写入文件
     *
     * @param file 被写入的文件
     * @return 字符数组
     * @parambytes 被写入的字符数组
     */
    public static boolean writeByteFile(byte[] bytes, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                } catch (Exception e) {
                }
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
        return true;
    }

    static boolean mExternalStorageAvailable = false;
    static boolean mExternalStorageWriteable = false;

    private static void checkStorageState() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    public static boolean isSdcardWriteable() {
        checkStorageState();
        return mExternalStorageWriteable;
    }


    /*
*  判断文件是否存在
*  filePath的参数格式：/storage/emulated/0/.ToDayNote/UserInfo/28017616_1459134625049.jpg
*/
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /*
    *    文件删除
    */
    public static boolean fileDelete(String filePath) {
        File file = new File(filePath);
        if (file.exists() == false) {
            return false;
        }
        return file.delete();
    }

    /**
     * 创建文件夹
     */
    public static boolean fileMkdirs(String filePath) {
        File file = new File(filePath);
        return file.mkdirs();
    }

    /**
     * 主目录地址。在SD卡中，创建一个项目的根目录，根目录的包名为 ToDayNote
     * 获取项目主目录的地址
     */
    public static String toRootPath() {
        String dir;
        if (checkSDcard()) {
            dir = Environment.getExternalStorageDirectory().getPath();
        } else {
            dir = Environment.getDataDirectory().getPath();
        }
        return dir + "/.myAppPath";
    }

    /**
     * 检测是否存在Sdcard
     *
     * @return 存在返回true，不存在返回false
     */
    public static boolean checkSDcard() {
        boolean flag = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return flag;
    }


    /**
     * 在主目录下，再添加一层子目录。获取子目录的名称
     */
    public static String toDayNoteResources() {
        return toRootPath() + "/Resources";
    }


    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

}
