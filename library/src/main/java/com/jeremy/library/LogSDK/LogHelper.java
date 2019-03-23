package com.jeremy.library.LogSDK;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by changqing on 2017/6/30.
 */
public class LogHelper {

    private static LogHelper INSTANCE = null;
    private static String PATH_LOG;
    private LogDumper mLogDumper = null;

    /**
     * 初始化目录
     */
    public void init(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            PATH_LOG = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hawaii_log";
        } else {
            PATH_LOG = context.getFilesDir().getAbsolutePath() + File.separator + "hawaii_log";
        }
        File file = new File(PATH_LOG);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static LogHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LogHelper(context);
        }
        return INSTANCE;
    }

    private LogHelper(Context context) {
        init(context);
    }

    public void log(String content) {
        checkFile();
        mLogDumper = new LogDumper(PATH_LOG, content);
        new Thread(mLogDumper).start();
    }

    public void checkFile() {
        File dir = new File(PATH_LOG);
        File[] files = dir.listFiles();
        Arrays.sort(files, new FileOrederComparator());
        if (files.length > 30) {
            File file = files[0];
            file.delete();
        }
    }

    public void writeFile(String fileName, String content) {
        try {
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class LogDumper implements Runnable {
        private String content;
        private String dir;

        public LogDumper(String dir, String content) {
            this.dir = dir;
            this.content = content;
        }

        @Override
        public void run() {
            writeFile(dir + File.separator + "HAWAII-" + MyDate.getFileName() + ".log", content);
        }
    }

    static class FileOrederComparator implements Comparator<File> {
        public int compare(File f1, File f2) {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }

        public boolean equals(Object obj) {
            return true;
        }
    }

}

class Singleton {
    public static volatile Singleton singleton;

    /**
     * 构造函数私有，禁止外部实例化
     */
    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (singleton) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}

